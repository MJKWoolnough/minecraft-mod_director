package mw.director;

import net.minecraft.entity.Entity;

public interface SpecialActions {

	public void onUpdate();

	public void onClientUpdate();

	public void setEntity(EntityDirector ed, Entity e);
}
