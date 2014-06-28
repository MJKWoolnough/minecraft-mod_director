package mw.director;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;

public class ProxyServer {

	public void load() {
		
	}

	public void serverStart(MinecraftServer server) {
		Commands dc = new Commands();
		TickRegistry.registerScheduledTickHandler(dc, Side.SERVER);
        ((ServerCommandManager) server.getCommandManager()).registerCommand(dc);
	}

}
