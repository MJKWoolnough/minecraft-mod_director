package mw.director.specials;

import mw.director.API;
import mw.director.DirectorPacketHandler;
import mw.director.EntityDirector;
import net.minecraft.entity.Entity;

public class WolfAPI extends AnimalAPI {

	public WolfAPI(API api, EntityDirector e, String name, Class<? extends Entity> ec) {
		super(api, e, name, ec);
	}
	
	public void tamed(boolean tame) {
		((WolfSpecial) this.entity.getSpecial()).tamed(tame);
		DirectorPacketHandler.sendWolfTamed(this.entity.entityId, tame);
	}
	
	public void angry(boolean angry) {
		((WolfSpecial) this.entity.getSpecial()).angry(angry);
		DirectorPacketHandler.sendWolfAngry(this.entity.entityId, angry);
	}
	
	public void collarColor(int colour) {
		((WolfSpecial) this.entity.getSpecial()).collarColor(colour);
		DirectorPacketHandler.sendWolfCollarColor(this.entity.entityId, colour);
	}

}
