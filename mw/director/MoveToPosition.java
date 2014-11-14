package mw.director;

import net.minecraft.entity.Entity;

public class MoveToPosition {

	private final EntityDirector entity;
	
	private boolean active = false;
	
	private double posX;
	private double posY;
	private double posZ;
	
	private APIWaiter waiter;

	public MoveToPosition(EntityDirector e) {
		this.entity = e;
	}
	
	public void moveTo(double x, double y, double z, APIWaiter waiter) {
		this.active = true;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.waiter = waiter;
		//world.getEntityPathToXYZ
	}
	
	public void update() {
		
	}
}
