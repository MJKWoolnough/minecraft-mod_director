package mw.director;

import mw.director.EntityDirector.RenderActor;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderDirector extends RenderLiving implements LabelRenderer {
	
	public RenderDirector() {
		super(null, 0);
	}
		
	private void renderDirector(EntityDirector entity, double x, double y, double z, float yaw, float pTick) {
		RenderActor renderer = entity.getRenderer();
		if (renderer == null) {
			return;
		}
		BossStatusCopy.backup();
		ModelRendererD.override = true;
		renderer.preRender(x, y, z, yaw, pTick, this);
		renderer.doRender(x, y, z, yaw, pTick);
		renderer.postRender(x, y, z, yaw, pTick);
		ModelRendererD.override = false;
		BossStatusCopy.restore();
	}
	
	public void renderLabel(EntityDirector entity, double x, double y, double z) {
		super.renderLivingLabel(entity, entity.label, x, y, z, 64);
	}
	
	private void renderDirectorShadowAndFire(EntityDirector entity, double x, double y, double z, float yaw, float pTick) {
		RenderActor renderer = entity.getRenderer();
		if (renderer == null) {
			return;
		}
		ModelRendererD.override = true;
		renderer.preRender(x, y, z, yaw, pTick, null);
		renderer.doRenderShadowAndFire(x, y, z, yaw, pTick);
		renderer.postRender(x, y, z, yaw, pTick);
		ModelRendererD.override = false;
	}
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pTick) {
		this.renderDirector((EntityDirector) entity, x, y, z, yaw, pTick);
	}
	
	@Override
	public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float pTick) {
		this.renderDirectorShadowAndFire((EntityDirector) entity, x, y, z, yaw, pTick);
	}
	
	@Override
	public ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}
}
