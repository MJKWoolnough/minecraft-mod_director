package mw.director.specials;

import net.minecraft.entity.Entity;
import mw.director.API;
import mw.director.APIEntity;
import mw.director.DirectorPacketHandler;
import mw.director.EntityDirector;

public class CreeperAPI extends APIEntity {
	
	public CreeperAPI(API api, EntityDirector e, String name, Class<? extends Entity> ec) {
		super(api, e, name, ec);
	}
	
	public void startFuse(int fuseTime, int ticks) {
		DirectorPacketHandler.sendCreeperFuse(this.entity.entityId, fuseTime, ticks);
	}
}
