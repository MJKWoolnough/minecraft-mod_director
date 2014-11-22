package mw.director;

public class APIWaiter extends APITimer {

	protected boolean	run	= false;

	public APIWaiter(CommandsEngine dce, IAPICallback callback) {
		super(dce, 20, callback, true);
	}

	@Override
	public void run(long tickCounter) {
		if (this.run) {
			super.run(tickCounter);
			this.remove();
		}
	}
}
