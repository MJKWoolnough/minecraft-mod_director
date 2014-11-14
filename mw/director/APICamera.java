package mw.director;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import cpw.mods.fml.common.network.Player;

public class APICamera {
	
	private final EntityPlayer player;
	protected int cameraId = -1;
	private InventoryPlayer storedPlayerInventory = new InventoryPlayer(null);
	
	public APICamera(EntityPlayer player) {
		this.player = player;
	}
	
	public void bobbing(boolean bob) {
		DirectorPacketHandler.sendSetBobbing((Player) this.player, bob);
	}
	
	public void fov(float fov) {
		if (fov < -1.75f) {
			fov = -1.75f;
		} else if (fov > 2.65f) {
			fov = 2.65f;
		}
		DirectorPacketHandler.sendSetFOV((Player) this.player, fov);
	}
	
	public void showGUI(boolean show) {
		DirectorPacketHandler.sendShowGUI((Player) this.player, show);
	}
	
	public void set(APIEntity entity) {
		if (this.cameraId == -1) {
			this.storedPlayerInventory.copyInventory(this.player.inventory);
		}
		this.player.inventory.copyInventory(new InventoryPlayer(null));
		int entityId = entity.getId();
		DirectorPacketHandler.sendSetCamera((Player) this.player, entityId);
		this.cameraId = entityId;
	}
	
	public void reset() {
		if (this.cameraId != -1) {
			this.player.inventory.copyInventory(this.storedPlayerInventory);
			DirectorPacketHandler.sendSetCamera((Player) this.player, -1);
			this.cameraId = -1;
		}
	}
}