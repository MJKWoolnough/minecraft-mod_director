package mw.director;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityDirector extends EntityLivingBase implements IEntityAdditionalSpawnData {
	
	protected class RenderActor {
		private final Render renderer;
		private Object oldTexture;
		private boolean close = false;
		
		private RenderActor(Render renderer) {
			this.renderer = renderer;
		}
		
		public void preRender(double x, double y, double z, float yaw, float pTick, LabelRenderer nameRenderer) {
			boolean roll = EntityDirector.this.rotationRoll != 0 || EntityDirector.this.prevRotationRoll != 0;
			boolean pitch = EntityDirector.this.rotationPitch != 0 || EntityDirector.this.prevRotationPitch != 0;
			boolean scale = EntityDirector.this.scod.needScale();
			if (roll || pitch || scale){
				this.close = true;
				GL11.glPushMatrix();
				GL11.glTranslated(x, y, z);
			}
			
			if (scale) {
				EntityDirector.this.scod.doScale(pTick);
			}
			
			if (nameRenderer != null && EntityDirector.this.label != null && EntityDirector.this.label.length() > 0) {
				if (this.close) {
					float height = EntityDirector.this.height;
					EntityDirector.this.height = EntityDirector.this.entity.height; 
					nameRenderer.renderLabel(EntityDirector.this, 0, 0, 0);
					EntityDirector.this.height = height;
				} else {
					nameRenderer.renderLabel(EntityDirector.this, x, y, z);
				}
			}
			
			if (this.close) {
				GL11.glTranslated(0, EntityDirector.this.entity.height / 2, 0);
			}
			
			if (roll) {
				GL11.glRotatef(OverrideAngles.confineAngle(EntityDirector.this.prevRotationRoll + (EntityDirector.this.rotationRoll - EntityDirector.this.prevRotationRoll) * pTick), 0, 0, 1);
			}
			if (pitch) {
				GL11.glRotatef(OverrideAngles.confineAngle(EntityDirector.this.prevRotationPitch + (EntityDirector.this.rotationPitch - EntityDirector.this.prevRotationPitch) * pTick), 1, 0, 0);
			}
			if (this.close) {
				GL11.glTranslated(-x, -y - EntityDirector.this.entity.height / 2, -z);
			}
			if (EntityDirector.this.texture != null) {
				this.oldTexture = this.renderer.renderManager.renderEngine.mapTextureObjects.put(this.renderer.getEntityTexture(EntityDirector.this.entity), EntityDirector.this.texture);
			}
		}
		
		public void doRender(double x, double y, double z, float yaw, float pTick) {
			this.renderer.doRender(EntityDirector.this.entity, x, y, z, yaw, pTick);
		}
		
		public void postRender(double x, double y, double z, float yaw, float pTick) {
			if (EntityDirector.this.texture != null) {
				this.renderer.renderManager.renderEngine.mapTextureObjects.put(this.renderer.getEntityTexture(EntityDirector.this.entity), this.oldTexture);
			}
			if (this.close) {
				GL11.glPopMatrix();
				this.close = false;
			}
		}
		
		public void doRenderShadowAndFire(double x, double y, double z, float yaw, float pTick) {
			this.renderer.doRenderShadowAndFire(EntityDirector.this.entity, x, y, z, yaw, pTick);
		}
	}
	
	protected class RenderActorWithParts extends RenderActor {
		private final ModelOverrides[] mo;
		
		public RenderActorWithParts(Render renderer, ModelOverrides[] mo) {
			super(renderer);
			this.mo = mo;
		}
		
		@Override
		public void doRender(double x, double y, double z, float yaw, float pTick) {
			EntityDirector.this.aod.setRotations(this.mo, pTick);
			EntityDirector.this.pod.setPoints(this.mo, pTick);
			super.doRender(x, y, z, yaw, pTick);
		}
	}

	private final static String nbtName = "actorId";
	
	public OverrideAngles aod;
	public final OverrideSpeed sod = new OverrideSpeed();;
	public final OverrideScale scod = new OverrideScale();
	
	public final MoveToEntity mte = new MoveToEntity(this);
	public final MoveToPosition mtp = new MoveToPosition(this);
	
	protected String label = null;
	private TextureObject texture;
	
	public OverridePoints pod;
	
	private RenderActor renderer;
	
	protected Entity entity;
	private EntityLivingBase entityLB;
	
	protected float prevRotationRoll;
	protected float rotationRoll;

	
	public EntityDirector(World world) {
		super(world);
		this.forceSpawn = true;
	}
	
	public EntityDirector(World world, double x, double y, double z, int dEntityId) {
		this(world);
		this.setPosition(x, y, z);
		this.setEntity(dEntityId);
	}
	
	public void setEntity(int dEntityId) {
		Entity entity;
		if (dEntityId == -1 && this.worldObj.isRemote) {
			entity = new HumanClient(this.worldObj);
		} else {
			entity = EntityList.createEntityByID(dEntityId, this.worldObj);
		}
		if (this.worldObj.isRemote) {
			Render renderer = RenderManager.instance.getEntityRenderObject(entity);
			if (renderer == null) {
				this.isDead = true;
				entity.isDead = true;
				return;
			}
			Map<String, Integer> parts = DirectorMod.instance.parts.get(entity.getClass());
			if (parts != null) {
				this.aod = new OverrideAngles(parts.size());
				this.pod = new OverridePoints(parts.size());
				this.renderer = new RenderActorWithParts(renderer, DirectorMod.instance.partRenderers.get(renderer.getClass()));
			} else {
				this.aod = new OverrideAngles(1);
				this.pod = new OverridePoints(0);
				this.renderer = new RenderActor(renderer);
			}
		} else {
			this.aod = new OverrideAngles(1);
		}
		
		this.entity = entity;
		this.entity.rotationYaw = 0;
		entity.yOffset = 0;
		this.setSize(entity.width, entity.height);
		this.setPosition(this.posX, this.posY, this.posZ);
		if (entity instanceof EntityLivingBase) {
			this.entityLB = (EntityLivingBase) entity;
			this.entityLB.rotationYawHead = 0;
			this.maxHurtTime = this.entityLB.maxHurtTime;
		}
	}
	
	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
		this.aod.tick();
		this.scod.tick();
		this.scod.scaleOverrides(this);
		this.speedUpdates();
		this.aod.bodyOverrides(this);
		if (this.worldObj.isRemote) {
			this.onClientUpdate();
		}
	}
	
	@Override
	public void onLivingUpdate() {
        if (this.hurtTime > 0) {
            this.hurtTime--;
        }
	}
	
	public void onClientUpdate() {
		this.pod.tick();
		this.entity.prevRotationYaw = this.prevRotationYaw;
		this.entity.rotationYaw = this.rotationYaw;
		if (this.entityLB != null) {
			this.onLivingClientUpdate();
		}
	}
	
	public void onLivingClientUpdate() {
		this.entityLB.prevLimbSwingAmount = this.prevLimbSwingAmount;
		this.entityLB.prevLimbSwingAmount = this.prevLimbSwingAmount;
        this.entityLB.limbSwingAmount = this.limbSwingAmount;
        this.entityLB.limbSwing = this.limbSwing;
        this.entityLB.prevRenderYawOffset = this.prevRotationYaw;
		this.entityLB.renderYawOffset = this.rotationYaw;
        if (this.entityLB.deathTime > 0 && this.entityLB.deathTime < 20) {
			this.entityLB.deathTime++;
        }
        this.entityLB.setArrowCountInEntity(this.getArrowCountInEntity());
	}
	
	private void speedUpdates() {
		this.sod.updateSpeed(this);
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		this.moveEntityWithHeading(0, 0);
	}
	
	public void remove() {
		this.isDead = true;
	}
	
	public void die() {
		this.deathTime = 1;
	}
	
	public void undie() {
		this.deathTime = 0;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTag) {
		super.writeToNBT(nbtTag);
		/*if (this.entity instanceof Human) {
			nbtTag.setInteger(nbtName, -1);
		} else {*/
			nbtTag.setInteger(nbtName, EntityList.getEntityID(this.entity));
		//}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTag) {
		super.readFromNBT(nbtTag);
		this.setEntity(nbtTag.getInteger(nbtName));
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float amount) {
		return false;
	}
	
	public RenderActor getRenderer() {
		return this.renderer;
	}
	
	public void setLabel(String label) {
		if (label == "") {
			this.label = null;
		} else {
			this.label = label;
		}
	}
	
	public void setSkin(String skin) {
		if (skin == "") {
			this.texture = null;
		} else if (this.renderer != null) {
			this.texture = AbstractClientPlayer.getDownloadImage(new ResourceLocation("web", skin), skin, this.getDefaultResourceLocation(), new ImageBufferDownloadTransparency());
		}
	}
	
	private ResourceLocation getDefaultResourceLocation() {
		return this.renderer.renderer.getEntityTexture(this.entity);
	}
	
	public int addArrow() {
		int count = this.getArrowCountInEntity() + 1;
		this.setArrowCountInEntity(count);
		return count;
	}
	
	public int removeArrow() {
		int count = this.getArrowCountInEntity();
		if (count > 0) {
			count--;
			this.setArrowCountInEntity(count);
		}
		return count;
	}
	
	//Required for EntityLivingBase
	
	@Override
	public void performHurtAnimation() {
		if (this.entityLB != null) {
			this.entityLB.performHurtAnimation();
		}
	}
	
	@Override
	public ItemStack getHeldItem() {
		return this.entityLB == null ? null : this.entityLB.getHeldItem();
	}

	@Override
	public ItemStack getCurrentItemOrArmor(int i) {
		return this.entityLB == null ? null : this.entityLB.getCurrentItemOrArmor(i);
	}

	@Override
	public void setCurrentItemOrArmor(int i, ItemStack itemStack) {
		if (this.entity != null) {
			this.entity.setCurrentItemOrArmor(i, itemStack);
		}
	}

	@Override
	public ItemStack[] getLastActiveItems() {
		return this.entityLB == null ? null : this.entity.getLastActiveItems();
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		/*if (this.entity instanceof Human) {
			data.writeInt(-1);
		} else {*/
			data.writeInt(EntityList.getEntityID(this.entity));
		//}
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		this.setEntity(data.readInt());
	}
	
	@Override
	protected void setSize(float width, float height) {
		super.setSize(width, height);
	}
}
