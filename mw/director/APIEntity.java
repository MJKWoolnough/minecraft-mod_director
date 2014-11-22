package mw.director;

public class APIEntity {

	protected final EntityDirector	entity;
	protected final API		api;
	public final String		entityName;

	public APIEntity(API api, EntityDirector e, String name) {
		this.entity = e;
		this.entityName = name;
		this.api = api;
	}

	public void remove() {
		if (this.api.camera.cameraId == this.entity.entityId) {
			this.api.camera.reset();
		}
		this.entity.remove();
	}

	public void label(String label) {
		DirectorPacketHandler.sendSetLabel(this.entity.entityId, label);
	}

	public void speedX(float start, float stop, int ticks) {
		this.entity.sod.addOverride(0, 0, start, stop, ticks);
		DirectorPacketHandler.sendSetSpeed(this.entity.entityId, 0, start, stop, ticks);
	}

	public void speedY(float start, float stop, int ticks) {
		this.entity.sod.addOverride(0, 1, start, stop, ticks);
		DirectorPacketHandler.sendSetSpeed(this.entity.entityId, 1, start, stop, ticks);
	}

	public void speedZ(float start, float stop, int ticks) {
		this.entity.sod.addOverride(0, 2, start, stop, ticks);
		DirectorPacketHandler.sendSetSpeed(this.entity.entityId, 2, start, stop, ticks);
	}

	public void scaleX(float start, float stop, int ticks) {
		this.entity.scod.addOverride(0, 0, start, stop, ticks);
		DirectorPacketHandler.sendSetScale(this.entity.entityId, 0, start, stop, ticks);
	}

	public void scaleY(float start, float stop, int ticks) {
		this.entity.scod.addOverride(0, 1, start, stop, ticks);
		DirectorPacketHandler.sendSetScale(this.entity.entityId, 1, start, stop, ticks);
	}

	public void scaleZ(float start, float stop, int ticks) {
		this.entity.scod.addOverride(0, 2, start, stop, ticks);
		DirectorPacketHandler.sendSetScale(this.entity.entityId, 2, start, stop, ticks);
	}

	public void scale(float start, float stop, int ticks) {
		this.entity.scod.addOverride(0, 0, start, stop, ticks);
		this.entity.scod.addOverride(0, 1, start, stop, ticks);
		this.entity.scod.addOverride(0, 2, start, stop, ticks);
		DirectorPacketHandler.sendSetScale(this.entity.entityId, 4, start, stop, ticks);
	}

	public void moveToEntity(APIEntity entity) {

	}

	public void moveToEntity(APIEntity entity, IAPICallback function) {

	}

	public void moveTo(double x, double y, double z) {
		this.moveTo(x, y, z, null);
	}

	public void moveTo(double x, double y, double z, IAPICallback function) {
		this.entity.mtp.moveTo(x, y, z, this.api.dce.newWaiter(function));
	}

	public int getId() {
		return this.entity.entityId;
	}
}
