package mw.director;

import java.lang.reflect.InvocationTargetException;

import net.minecraft.entity.Entity;

public class Special {
	private final Class<? extends APIEntity> api;
	private final Class<? extends SpecialActions> actions;
	
	public Special(Class<? extends APIEntity> api, Class<? extends SpecialActions> special) {
		this.api = api;
		this.actions = special;
	}
	
	public Class<? extends APIEntity> getAPI() {
		return this.api;
	}
	
	public APIEntity getAPI(API api, EntityDirector ed, String entityStr, Class<? extends Entity> entityClass) {
		try {
			return this.api.getConstructor(API.class, EntityDirector.class, String.class, Class.class).newInstance(api, ed, entityStr, entityClass);
		} catch (Exception e) {
			return new APIEntity(api, ed, entityStr, entityClass);
		}
	}
	
	public Class<? extends SpecialActions> getActions() {
		return this.actions;
	}
	
	public SpecialActions getActions(Entity e) {
		if (this.actions == null) {
			return null;
		}
		SpecialActions sa;
		try {
			sa = this.actions.getConstructor().newInstance();
		} catch (Exception e1) {
			return null;
		}
		sa.setEntity(e);
		return sa;
	}
}
