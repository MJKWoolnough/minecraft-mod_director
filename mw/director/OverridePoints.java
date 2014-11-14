package mw.director;


public class OverridePoints extends Overrides {

	public OverridePoints(int num) {
		super(num);
	}
	
	private float set(int i, int j, float pTick) {
		if (this.overrides[i][j] == this.overrides[i][j]) {
			return (float) (this.prevOverrides[i][j] + (this.overrides[i][j] - this.prevOverrides[i][j]) * pTick);
		}
		return Float.NaN;
	}
	
	public void setPoints(ModelOverrides[] parts, float pTick) {
		for (int i = 0; i < parts.length && i < this.overrides.length; i++) {
			ModelOverrides model = parts[i];
			for (int j = 0; j < 3; j++) {
				model.Points[j] = this.set(i, j, pTick);
			}
		}
	}
}
