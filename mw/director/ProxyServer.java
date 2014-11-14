package mw.director;

import java.util.HashMap;
import java.util.Map;

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

	public static class PartMap {
		private final Map<String, Integer> m;
		private int pos = 0;
		
		public PartMap(Class<? extends Entity> entityClass) {
			this.m = new HashMap<String, Integer>();
			DirectorMod.instance.parts.put(entityClass, this.m);
		}
		
		public PartMap(Class<? extends Entity> entityClass, String... parts) {
			this(entityClass);
			this.add(parts);
		}
		
		public void add(String... parts) {
			for (int i = 0; i < parts.length; i++) {
				this.m.put(parts[i], this.pos);
				this.pos++;
			}
		}
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
		new PartMap(Human.class,
			API.body, API.head, API.headwear, API.rightArm, API.leftArm,
			API.rightLeg, API.leftLeg, API.ears, API.cloak
		);
	}
	
	public void wolf() {
		new PartMap(EntityWolf.class,
			API.body, API.head, API.backRightLeg, API.backLeftLeg,
			API.frontRightLeg, API.frontLeftLeg, API.tail, API.mane
		);
	}
	
	public void creeper() {
		new PartMap(EntityCreeper.class,
			API.body, API.head, API.backRightLeg,
			API.backLeftLeg, API.frontRightLeg, API.frontLeftLeg, ""
		);
	}

}
