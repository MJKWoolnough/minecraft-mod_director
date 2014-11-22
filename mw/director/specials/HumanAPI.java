package mw.director.specials;

import mw.director.API;
import mw.director.APIEntityLiving;
import mw.director.DirectorPacketHandler;
import mw.director.EntityDirector;
import net.minecraft.entity.Entity;

public class HumanAPI extends APIEntityLiving {

	public HumanAPI(API api, EntityDirector e, String name, Class<? extends Entity> ec) {
		super(api, e, name, ec);
	}

	@Override
	public void skin(String skin) {
		DirectorPacketHandler.sendSetHumanSkin(this.entity.entityId, skin);
	}

	public void cape(String cape) {
		DirectorPacketHandler.sendSetHumanCape(this.entity.entityId, cape);
	}

}
