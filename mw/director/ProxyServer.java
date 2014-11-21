package mw.director;

import java.util.HashMap;
import java.util.Map;

import mw.director.specials.CreeperAPI;
import mw.director.specials.CreeperSpecial;
import mw.director.specials.HumanAPI;
import mw.director.specials.HumanSpecial;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ProxyServer {
	
	private void setParts(Class<? extends Entity> entityClass, String... parts) {
		Map<String, Integer> m = new HashMap<String, Integer>();
		for (int i = 0; i < parts.length; i++) {
			m.put(parts[i], i);
		}
		DirectorMod.instance.parts.put(entityClass, m);
	}
	
	private void setSpecial(Class<? extends Entity> entityClass, Class<? extends APIEntity> api, Class<? extends SpecialActions> actions) {
		DirectorMod.instance.apis.put(entityClass, new Special(api, actions));
	}
	
	public void load() {
		EntityRegistry.registerModEntity(EntityDirector.class, "Director", 0, DirectorMod.instance, 80, 1, false);
		EntityList.addMapping(Human.class, "Human", -1);
		this.loadEntities();
	}
	
	protected void loadEntities() {
		this.player();
		this.wolf();
		this.creeper();
	}

	public void serverStart(MinecraftServer server) {
		Commands dc = new Commands();
		TickRegistry.registerScheduledTickHandler(dc, Side.SERVER);
        ((ServerCommandManager) server.getCommandManager()).registerCommand(dc);
	}
	
	public void player() {
		this.setParts(Human.class,
			API.body, API.head, API.headwear, API.rightArm, API.leftArm,
			API.rightLeg, API.leftLeg, API.ears, API.cloak
		);
		this.setSpecial(Human.class, HumanAPI.class, HumanSpecial.class);
		this.setSpecial(HumanClient.class, HumanAPI.class, HumanSpecial.class);
	}
	
	public void wolf() {
		this.setParts(EntityWolf.class,
			API.body, API.head, API.backRightLeg, API.backLeftLeg,
			API.frontRightLeg, API.frontLeftLeg, API.tail, API.mane
		);
	}
	
	public void creeper() {
		this.setParts(EntityCreeper.class,
			API.body, API.head, API.backRightLeg,
			API.backLeftLeg, API.frontRightLeg, API.frontLeftLeg, ""
		);
		this.setSpecial(EntityCreeper.class, CreeperAPI.class, CreeperSpecial.class);
	}

}
