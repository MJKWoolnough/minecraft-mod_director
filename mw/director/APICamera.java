package mw.director;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.Player;

public class APICamera {

	private final EntityPlayer player;
	protected int cameraId = -1;
	private final InventoryPlayer storedPlayerInventory = new InventoryPlayer(null);
	private final InventoryPlayer inventory = new InventoryPlayer(null);

	public APICamera(EntityPlayer player) {
		this.player = player;
	}

	public void bobbing(boolean bob) {
		DirectorPacketHandler.sendSetBobbing((Player) this.player, bob);
	}

	public void fov(float f) {
		if (f < -1.75f) {
			f = -1.75f;
		} else if (f > 2.65f) {
			f = 2.65f;
		}
		DirectorPacketHandler.sendSetFOV((Player) this.player, f);
	}

	public void zoom(float z) {
		DirectorPacketHandler.sendSetZoom((Player) this.player, z);
	}

	public void showGUI(boolean show) {
		DirectorPacketHandler.sendShowGUI((Player) this.player, show);
	}

	public void set(APIEntity entity) {
		this.set(entity.getId());
	}

	private void set(int entityId) {
		if (this.cameraId == -1) {
			this.storedPlayerInventory.copyInventory(this.player.inventory);
			this.player.inventory.clearInventory(-1, -1);
		}
		this.player.inventory.copyInventory(new InventoryPlayer(null));
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
	
	public void setInventorySlotContents(int slotId, int itemId, int count, int damage) {
		ItemStack is = new ItemStack(itemId, count, damage);
		this.inventory.setInventorySlotContents(slotId, is);
		if (this.cameraId != -1) {
			this.player.inventory.setInventorySlotContents(slotId, is);
		}
	}
	
	public void setCurrentItem(int id) {
		if (id < 0) {
			id = 0;
		} else if (id > 9) {
			id = 9;
		}
		this.inventory.currentItem = id;
		if (this.cameraId != -1) {
			this.player.inventory.currentItem = id;
		}
	}
}
