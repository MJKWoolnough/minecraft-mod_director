package mw.director;

import java.util.Map;

import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class APIEntity {

	protected final EntityDirector entity;
	protected final Map<String, Integer> parts;
	protected final API api;
	public final String entityName;
	
	public APIEntity(API api, EntityDirector e, String name, Class<? extends Entity> ec) {
		this.entity = e;
		this.parts = DirectorMod.instance.parts.get(ec);
		this.entityName = name;
		this.api = api;
	}
	
	public void remove() {
		if (this.api.camera.cameraId == this.entity.entityId) {
			this.api.camera.reset();
		}
		this.entity.remove();
	}
	
	public void die() {
		DirectorPacketHandler.sendDieAnimation(this.entity.entityId, true);
	}
	
	public void undie() {
		DirectorPacketHandler.sendDieAnimation(this.entity.entityId, false);
	}
	
	public void label(String label) {
		DirectorPacketHandler.sendSetLabel(this.entity.entityId, label);
	}
	
	public void skin(String skin) {
		DirectorPacketHandler.sendSetSkin(this.entity.entityId, skin);
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
	
	public APIPart getPart(String partName) {
		if (this.parts == null) {
			return null;
		}
		Integer partId = this.parts.get(partName);
		if (partId == null) {
			return null;
		}
		return new APIPart(this.entity, partName, partId.intValue(), partName == API.body);
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
	
	public void hurt() {
		DirectorPacketHandler.sendSetHurt(this.entity.entityId);
		this.entity.playSound(this.entity.getHurtSound());
	}
	
	protected int getId() {
		return this.entity.entityId;
	}
	
	public int addArrow() {
		return this.entity.addArrow();
	}
	
	public int removeArrow() {
		return this.entity.removeArrow();
	}
	
	/*public void inventory(int slotId, String itemStr) {
		
	}*/
	
	public void inventory(int slotId, int itemId) {
		this.inventory(slotId, itemId, 0);
	}
	
	public void inventory(int slotId, int itemId, int meta) {
		this.entity.setCurrentItemOrArmor(slotId, new ItemStack(itemId, 1, meta));
		DirectorPacketHandler.sendSetInventory(this.entity.entityId, slotId, itemId, meta);
	}
	
	public Object special(Object... values) {
		return null;
	}
}
