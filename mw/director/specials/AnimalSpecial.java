package mw.director.specials;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import mw.director.EntityDirector;
import mw.director.SpecialActions;

public class AnimalSpecial implements SpecialActions {

	private EntityAnimal entity;
	private EntityDirector entityD;

	@Override
	public void onUpdate() {
		int age = this.entity.getGrowingAge();
		if (age < 0) {
			age++;
		} else if (age > 0) {
			age--;
		} else {
			return;
		}
		this.setAge(age);
		this.entity.setScaleForAge(this.entity.isChild());
		this.entityD.dSetSize(this.entity.width, this.entity.height);
	}

	@Override
	public void onClientUpdate() {
		if (this.entity.inLove > 0) {
			this.entity.inLove--;
			if (this.entity.inLove % 10 == 0) {
				this.entity.worldObj.spawnParticle(
					"heart",
					this.entity.posX + this.entity.rand.nextFloat() * this.entity.width * 2.0D - this.entity.width,
					this.entity.posY + this.entity.rand.nextFloat() * this.entity.height + 0.5,
					this.entity.posZ + this.entity.rand.nextFloat() * this.entity.width * 2.0F - this.entity.width,
					this.entity.rand.nextGaussian()/50,
					this.entity.rand.nextGaussian()/50,
					this.entity.rand.nextGaussian()/50
				);
			}
		}
	}

	@Override
	public void setEntity(EntityDirector ed, Entity e) {
		this.entity = (EntityAnimal) e;
		this.entityD = ed;
	}

	public void setLove(int ticks) {
		this.entity.inLove = ticks;
	}

	public void setAge(int age) {
		this.entity.setGrowingAge(age);
	}
}
