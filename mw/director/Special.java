package mw.director;

import net.minecraft.entity.Entity;

public class Special {

	public static final Special			none	= new Special(null, null);

	private final Class<? extends APIEntity>	api;
	private final Class<? extends SpecialActions>	actions;

	public Special(Class<? extends APIEntity> api, Class<? extends SpecialActions> special) {
		this.api = api;
		this.actions = special;
	}

	public Class<? extends APIEntity> getAPI() {
		return this.api;
	}

	public APIEntity getAPI(API api, EntityDirector ed, String entityStr, Class<? extends Entity> entityClass) {
		if (this.api != null) {
			try {
				return this.api.getConstructor(API.class, EntityDirector.class, String.class, Class.class).newInstance(api, ed, entityStr, entityClass);
			} catch (Exception e) {}
		}
		if (ed.entityLB != null) {
			return new APIEntityLiving(api, ed, entityStr, entityClass);
		}
		return new APIEntity(api, ed, entityStr);
	}

	public Class<? extends SpecialActions> getActions() {
		return this.actions;
	}

	public SpecialActions getActions(EntityDirector ed, Entity e) {
		if (this.actions == null) {
			return null;
		}
		SpecialActions sa;
		try {
			sa = this.actions.getConstructor().newInstance();
		} catch (Exception e1) {
			return null;
		}
		sa.setEntity(ed, e);
		return sa;
	}
}
