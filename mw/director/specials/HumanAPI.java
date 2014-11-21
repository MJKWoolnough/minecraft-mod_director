package mw.director.specials;

import net.minecraft.entity.Entity;
import mw.director.API;
import mw.director.APIEntity;
import mw.director.DirectorPacketHandler;
import mw.director.EntityDirector;

public class HumanAPI extends APIEntity {

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
