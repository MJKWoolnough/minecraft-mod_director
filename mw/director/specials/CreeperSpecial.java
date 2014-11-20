package mw.director.specials;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import mw.director.SpecialActions;

public class CreeperSpecial implements SpecialActions {

	private EntityCreeper creeper;
	private int fuseTimer = 0;
	
	@Override
	public void onUpdate() {
		
	}

	@Override
	public void onClientUpdate() {
		this.creeper.lastActiveTime = this.creeper.timeSinceIgnited;
		if (this.fuseTimer > 0) {
			this.fuseTimer--;
			if (this.creeper.timeSinceIgnited < this.creeper.fuseTime) {
				this.creeper.timeSinceIgnited++;
			}
		} else if (this.creeper.timeSinceIgnited > 0) {
			this.creeper.timeSinceIgnited--;
		}
	}

	@Override
	public void setEntity(Entity e) {
		this.creeper = (EntityCreeper) e;
	}
	
	public void startFuse(int fuseTime, int ticks) {
		this.creeper.fuseTime = fuseTime;
		this.fuseTimer = ticks;
		this.creeper.playSound("random.fuse", 1.0F, 0.5F);
	}

}
