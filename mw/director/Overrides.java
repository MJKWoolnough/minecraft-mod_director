package mw.director;

public class Overrides {

	protected final double[][]	overrides;
	protected final double[][]	prevOverrides;
	private final double[][][]	toOverride;

	public Overrides(int num) {
		this.overrides = new double[num][3];
		this.prevOverrides = new double[num][3];
		this.toOverride = new double[num][3][2];
		for (int i = 0; i < num; i++) {
			for (int j = 0; j < 3; j++) {
				this.overrides[i][j] = Double.NaN;
				this.toOverride[i][j][0] = 0;
				this.toOverride[i][j][1] = 0;
			}
		}
	}

	public void addOverride(int i, int j, double startValue, double endValue, int ticks) {
		if (ticks > 0 && startValue == startValue && endValue == endValue) {
			this.overrides[i][j] = startValue;
			this.prevOverrides[i][j] = startValue;
			this.toOverride[i][j][0] = (endValue - startValue) / ticks;
			this.toOverride[i][j][1] = ticks;
		}
	}

	public void tick() {
		for (int i = 0; i < this.toOverride.length; i++) {
			for (int j = 0; j < 3; j++) {
				this.prevOverrides[i][j] = this.overrides[i][j];
				if (this.toOverride[i][j][1] > 0) {
					this.toOverride[i][j][1]--;
					this.overrides[i][j] += this.toOverride[i][j][0];
				} else {
					this.overrides[i][j] = Double.NaN;
				}
			}
		}
	}
}
