package mw.director;

import net.minecraft.entity.Entity;

public class MoveToEntity {

	private final EntityDirector entity;
	
	private boolean active = false;
	
	private Entity toEntity;

	public MoveToEntity(EntityDirector e) {
		this.entity = e;
	}
	
	public void moveTo(Entity entity, IAPICallback function) {
		this.active = true;
		this.toEntity = entity;
	}
	
	public void update() {
		if (!this.active) {
			return;
		}
		this.entity.mtp.moveTo(this.toEntity.posX, this.toEntity.posY, this.toEntity.posZ, null);
	}
}
