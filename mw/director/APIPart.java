package mw.director;

public class APIPart {
	
	private final EntityDirector entity;
	private final String name;
	private final int id;
	private final boolean updateServer;
	
	public APIPart(EntityDirector entity, String name, int id, boolean updateServer) {
		this.entity = entity;
		this.name = name;
		this.id = id;
		this.updateServer = updateServer;
	}
	
	public String name() {
		return this.name;
	}
	
	public int id() {
		return this.id;
	}
	
	private void rotatePart(int ypr, float startValue, float endValue, int ticks) {
		DirectorPacketHandler.sendPartRotation(this.entity.entityId, this.id, ypr, startValue, endValue, ticks);
		if (this.updateServer) {
			this.entity.aod.addOverride(this.id, ypr, startValue, endValue, ticks);
		}
	}
	
	public void yaw(float startValue, float endValue, int ticks) {
		this.rotatePart(1, startValue, endValue, ticks);
	}
	
	public void pitch(float startValue, float endValue, int ticks) {
		this.rotatePart(0, startValue, endValue, ticks);
	}
	
	public void roll(float startValue, float endValue, int ticks) {
		this.rotatePart(2, startValue, endValue, ticks);
	}
	
	private void placePart(int xyz, float startValue, float endValue, int ticks) {
		DirectorPacketHandler.sendPartPosition(this.entity.entityId, this.id, xyz, startValue, endValue, ticks);
	}
	
	public void x(float startValue, float endValue, int ticks) {
		this.placePart(0, startValue, endValue, ticks);
	}
	
	public void y(float startValue, float endValue, int ticks) {
		this.placePart(1, startValue, endValue, ticks);
	}
	
	public void z(float startValue, float endValue, int ticks) {
		this.placePart(2, startValue, endValue, ticks);
	}
}
