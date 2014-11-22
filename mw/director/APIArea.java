package mw.director;

import mw.library.Area;
import net.minecraft.world.World;

public class APIArea {

	private final Area	area;

	public APIArea(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		this.area = new Area(world, x1, y1, z1, x2, y2, z2);
	}

	public void fill(APIBlock block) {
		this.area.fill(block.block);
	}

	public boolean copyFrom(APIArea area) {
		return area.area.copyTo(this.area);
	}

	public boolean copyTo(APIArea area) {
		return this.area.copyTo(area.area);
	}

	public void mirrorX() {
		this.area.mirrorX();
	}

	public void mirrorZ() {
		this.area.mirrorZ();
	}

	public boolean rotate90() {
		return this.area.rotate90();
	}

	public void rotate180() {
		this.area.rotate180();
	}

	public boolean rotate270() {
		return this.area.rotate270();
	}
}
