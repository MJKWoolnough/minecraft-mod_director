package mw.director;

import net.minecraft.entity.EntityLivingBase;

public class OverrideSpeed extends Overrides {

	// public static final float DEGtoRAD = (float)Math.PI / 180;

	public OverrideSpeed() {
		super(3);
	}

	public void updateSpeed(EntityLivingBase entity) {
		this.tick();
		if (this.overrides[0][0] == this.overrides[0][0]) {
			entity.motionX = this.overrides[0][0];
		}
		if (this.overrides[0][1] == this.overrides[0][1]) {
			entity.motionY = this.overrides[0][1];
		}
		if (this.overrides[0][2] == this.overrides[0][2]) {
			entity.motionZ = this.overrides[0][2];
		}
		// float sinT = MathHelper.sin(entity.rotationYaw * DEGtoRAD);
		// float cosT = MathHelper.cos(entity.rotationYaw * DEGtoRAD);
		// entity.moveStrafing = (float) (entity.motionX) * cosT +
		// (float)entity.motionZ * sinT;
		// entity.moveForward = (float) (entity.motionZ) * cosT -
		// (float)entity.motionX * sinT;
	}
}
