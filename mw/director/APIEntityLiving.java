package mw.director;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class APIEntityLiving extends APIEntity {
	
	protected final Map<String, Integer> parts;
	
	public APIEntityLiving(API api, EntityDirector e, String name, Class<? extends Entity> ec) {
		super(api, e, name);
		this.parts = DirectorMod.instance.parts.get(ec);
	}

	public void die() {
		DirectorPacketHandler.sendDieAnimation(this.entity.entityId, true);
	}
	
	public void undie() {
		DirectorPacketHandler.sendDieAnimation(this.entity.entityId, false);
	}
	
	public void skin(String skin) {
		DirectorPacketHandler.sendSetSkin(this.entity.entityId, skin);
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
	
	public void hurt() {
		DirectorPacketHandler.sendSetHurt(this.entity.entityId);
		this.entity.playSound(this.entity.getHurtSound());
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
}
