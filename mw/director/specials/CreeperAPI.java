package mw.director.specials;

import mw.director.API;
import mw.director.APIEntityLiving;
import mw.director.DirectorPacketHandler;
import mw.director.EntityDirector;
import net.minecraft.entity.Entity;

public class CreeperAPI extends APIEntityLiving {

	public CreeperAPI(API api, EntityDirector e, String name, Class<? extends Entity> ec) {
		super(api, e, name, ec);
	}

	public void startFuse(int fuseTime, int ticks) {
		DirectorPacketHandler.sendCreeperFuse(this.entity.entityId, fuseTime, ticks);
	}
}
