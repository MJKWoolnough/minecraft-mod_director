package mw.director;

import mw.library.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class APIBlock {

	protected final Blocks	block	= new Blocks();

	public APIBlock(int blockId, int metadata) {
		this.block.blockId = blockId;
		this.block.metadata = metadata;
	}

	public APIBlock(World world, int x, int y, int z) {
		this.block.get(world, x, y, z);
	}

	public void setBlockId(int blockId) {
		this.block.blockId = blockId;
	}

	public void setMetadata(int metadata) {
		this.block.metadata = metadata;
	}

	public NBTTagCompound getNBT() {
		return this.block.nbtData;
	}
}
