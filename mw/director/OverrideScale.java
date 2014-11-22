package mw.director;

import org.lwjgl.opengl.GL11;

public class OverrideScale extends Overrides {

	public OverrideScale() {
		super(1);
	}

	public void doScale(float pTick) {
		double[] scales = new double[3];
		boolean override = false;
		for (int i = 0; i < 3; i++) {
			if (this.overrides[0][i] == this.overrides[0][i]) {
				scales[i] = this.prevOverrides[0][i] + (this.overrides[0][i] - this.prevOverrides[0][i]) * pTick;
			} else {
				scales[i] = 1;
			}
		}
		GL11.glScaled(scales[0], scales[1], scales[2]);
	}

	public boolean needScale() {
		return this.overrides[0][0] == this.overrides[0][0] || this.overrides[0][1] == this.overrides[0][1] || this.overrides[0][2] == this.overrides[0][2];
	}

	public void scaleOverrides(EntityDirector e) {
		float width = e.entity.width;
		float height = e.entity.height;
		float x = width;
		float z = width;
		if (this.overrides[0][0] == this.overrides[0][0]) {
			x *= this.overrides[0][0];
		}
		if (this.overrides[0][1] == this.overrides[0][1]) {
			height *= this.overrides[0][1];
		}
		if (this.overrides[0][2] == this.overrides[0][2]) {
			z *= this.overrides[0][2];
		}
		if (x > z) {
			width = x;
		} else {
			width = z;
		}
		if (width != e.width || height != e.height) {
			e.setSize(width, height);
		}
	}

}
