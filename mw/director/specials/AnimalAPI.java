package mw.director.specials;

import mw.director.API;
import mw.director.APIEntityLiving;
import mw.director.DirectorPacketHandler;
import mw.director.EntityDirector;
import net.minecraft.entity.Entity;

public class AnimalAPI extends APIEntityLiving {

	public AnimalAPI(API api, EntityDirector e, String name, Class<? extends Entity> ec) {
		super(api, e, name, ec);
	}

	public void setAge(int age) {
		DirectorPacketHandler.sendSetAge(this.entity.entityId, age);
	}

	public void setLove(int love) {
		DirectorPacketHandler.sendSetLove(this.entity.entityId, love);
	}

}
