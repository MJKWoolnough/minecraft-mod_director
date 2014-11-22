package mw.director.specials;

import mw.director.EntityDirector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;

public class WolfSpecial extends AnimalSpecial {

	private EntityWolf	entity;

	@Override
	public void setEntity(EntityDirector ed, Entity e) {
		super.setEntity(ed, e);
		this.entity = (EntityWolf) e;
	}

	public void tamed(boolean tame) {
		this.entity.setTamed(tame);
	}

	public void angry(boolean angry) {
		this.entity.setAngry(angry);
	}

	public void collarColor(int colour) {
		this.entity.setCollarColor(colour);
	}

}
