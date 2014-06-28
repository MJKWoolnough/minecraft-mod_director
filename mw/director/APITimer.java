package mw.director;

import cpw.mods.fml.common.FMLLog;

public class APITimer implements Comparable<APITimer>{
	
	private int ticks;
	private long tickAt;
	private final IAPICallback function;
	private boolean constant;
	private final CommandsEngine dce;
	
	public APITimer(CommandsEngine dce, int ticks, IAPICallback callback, boolean repeat) {
		this.dce = dce;
		this.ticks = ticks;
		this.function = callback;
		this.constant = repeat;
	}
	
	public int compareTo(APITimer t) {
		return (int)(this.tickAt - t.tickAt);
	}
	
	public void update(long tickCounter) {
		this.tickAt = tickCounter + this.ticks;
	}
	
	public boolean ready(long tickCounter) {
		return tickCounter >= this.tickAt;
	}
	
	public boolean repeat() {
		return this.constant;
	}
	
	public void run(long tickCounter) {
		try {
			this.function.run(tickCounter);
		} catch (Exception e) {
			FMLLog.warning(e.getMessage());
		}
	}
	
	public int tickGap(long tickCounter) {
		return (int)(this.tickAt - tickCounter);
	}
	
	public void remove() {
		this.dce.removeTicker(this);
		this.ticks = 0;
		this.tickAt = 0;
		this.constant = false;
	}
}