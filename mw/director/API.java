package mw.director;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class API {
	private final int playerId; 
	private final World world;
	private final CommandsEngine dce;
	
	private static final Random random = new Random(0);
	
	public API(EntityPlayer player, CommandsEngine dce) {
		this.world = player.worldObj;
		this.dce = dce;
		this.playerId = player.entityId;
	}
}
