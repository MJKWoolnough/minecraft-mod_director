package mw.director;

import net.minecraft.client.Minecraft;

public class OverrideAngles extends Overrides {

	public OverrideAngles(int num) {
		super(num);
	}

	public void bodyOverrides(EntityDirector e) {
		if (this.overrides[0][0] == this.overrides[0][0]) {
			e.rotationPitch = (float) this.overrides[0][0];
		}
		if (this.overrides[0][1] == this.overrides[0][1]) {
			e.rotationYaw = (float) this.overrides[0][1];
		}
		e.prevRotationRoll = e.rotationRoll;
		if (this.overrides[0][2] == this.overrides[0][2]) {
			e.rotationRoll = (float) this.overrides[0][2];
			if (e.worldObj.isRemote) {
				Minecraft mc = Minecraft.getMinecraft();
				if (mc.renderViewEntity == e) {
					mc.entityRenderer.camRoll = e.rotationRoll;
				}
			}
		}
	}

	private float set(int i, int j, float pTick) {
		if (this.overrides[i][j] == this.overrides[i][j]) {
			return this.confineAngle(this.prevOverrides[i][j] + (this.overrides[i][j] - this.prevOverrides[i][j]) * pTick);
		}
		return Float.NaN;
	}

	public static float confineAngle(double f) {
		while (f >= 180f) {
			f -= 360f;
		}
		while (f < -180f) {
			f += 360f;
		}
		return (float) f;
	}

	public void setRotations(ModelOverrides[] parts, float pTick) {
		for (int i = 1; i < parts.length && i < this.overrides.length; i++) {
			ModelOverrides model = parts[i];
			for (int j = 0; j < 3; j++) {
				model.Angles[j] = this.set(i, j, pTick);
			}
		}
	}
}
