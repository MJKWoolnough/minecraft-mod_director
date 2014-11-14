package mw.director;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import cpw.mods.fml.common.FMLLog;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class API {
	
	public static final String head = "head";
	public static final String body = "body";
	
	public static final String headwear = "headwear";
	
	public static final String frontLeftLeg = "frontleftleg";
	public static final String frontRightLeg = "frontrightleg";
	public static final String backLeftLeg = "backleftleg";
	public static final String backRightLeg = "backrightleg";
	
	public static final String leftArm = "frontleftleg";
	public static final String rightArm = "frontrightleg";
	
	public static final String leftLeg = "backleftleg";
	public static final String rightLeg = "backrightleg";
	
	public static final String tail = "tail";
	public static final String mane = "mane";
	
	public static final String ears = "ears";
	
	public static final String cloak = "cloak";
	
	private final int playerId; 
	private final World world;
	protected final CommandsEngine dce;
	
	public final APICamera camera;
	
	private static final Random random = new Random(0);
	
	public API(EntityPlayer player, CommandsEngine dce) {
		this.world = player.worldObj;
		this.dce = dce;
		this.playerId = player.entityId;
		this.camera = new APICamera(player);
	}
	
	public APIEntity spawn(String entityStr, float x, float y, float z) {
		Integer entity;
		/*if (entityStr.equals("Human")) {
			entity = Integer.valueOf(-1);
		} else {*/
			entity = (Integer) EntityList.stringToIDMapping.get(entityStr);
		//}
		
		if (entity != null) {
			EntityDirector ed = new EntityDirector(this.world, x, y, z, entity.intValue());
			if (this.world.spawnEntityInWorld(ed)) {
				Class<? extends Entity> entityClass;
				/*if (entity == -1) {
					entityClass = Human.class;
				} else {*/
					entityClass = (Class<? extends Entity>) EntityList.IDtoClassMapping.get(entity);
				//}
				Class<? extends APIEntity> apiClass = DirectorMod.instance.apis.get(entityClass);
				if (apiClass == null) {
					return new APIEntity(this, ed, entityStr, entityClass);
				}
				try {
					return apiClass.getConstructor(API.class, EntityDirector.class, String.class, Class.class).newInstance(this, ed, entityStr, entityClass);
				} catch (Exception e) {}
			}
		}
		return null;
	}
	
	public void removeAll() {
		for (int i = 0; i < this.world.loadedEntityList.size(); i++) {
			Entity entity = (Entity) this.world.loadedEntityList.get(i);
			if (entity instanceof EntityDirector) {
				((EntityDirector)entity).remove();
			}
		}
	}
	
	public APITimer waitTicker(IAPICallback function, int ticks) {
		return this.dce.newTicker(function, ticks, false);
	}
	
	public APITimer repeatTicker(IAPICallback function, int ticks) {
		return this.dce.newTicker(function, ticks, true);
	}
	
	public long setTime(long newTime) {
		long oldTime = this.world.getWorldTime();
		this.world.setWorldTime(newTime);
		return oldTime;
	}
	
	public void setWeather(boolean enabled) {
		this.setWeather(enabled, Integer.MAX_VALUE);
	}
	
	public void setWeather(boolean enabled, int time) {
		WorldInfo wi = this.world.getWorldInfo();
		wi.setRaining(enabled);
		wi.setRainTime(time);
	}
	
	public void lightning(int x, int y, int z) {
		this.world.addWeatherEffect(new EntityLightningBolt(this.world, x, y, z));
	}
	
	public APIBlock block(int blockId) {
		return this.block(blockId, 0);
	}
	
	public APIBlock block(int blockId, int metadata) {
		return new APIBlock(blockId, metadata);
	}
	
	public APIBlock getBlock(int x, int y, int z) {
		return new APIBlock(this.world, x, y, z);
	}
	
	public void setBlock(APIBlock block, int x, int y, int z) {
		block.block.set(this.world, x, y, z);
	}
	
	public void damageBlock(int blockId, int x, int y, int z, int damage) {
		if (blockId > 0) {
			blockId = -blockId;
		} else if (blockId == 0) {
			blockId = Integer.MIN_VALUE;
		}
		this.world.destroyBlockInWorldPartially(blockId, x, y, z, damage);
	}
	
	public void explosion(double x, double y, double z, float radius) {
		this.explosion(x, y, z, radius, false, false);
	}
	
	public void explosion(double x, double y, double z, float radius, boolean destroyBlocks, boolean startFires) {
		this.world.newExplosion(null, x, y, z, radius, startFires, destroyBlocks);
	}
	
	public APIArea area(int x1, int y1, int z1, int x2, int y2, int z2) {
		return new APIArea(this.world, x1, y1, z1, x2, y2, z2);
	}
	
	public void log(String data) {
		Entity entity =	this.world.getEntityByID(this.playerId);
		EntityPlayer player = null;
		if (entity != null) {
			player = (EntityPlayer) entity;
		}
		if (player == null) {
			FMLLog.info(data);
		} else {
			player.addChatMessage(data);
		}
	}
}
