package mw.director;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="Director", name="Director", version="0.0.1")
@NetworkMod(clientSideRequired=true, serverSideRequired=true, channels = { PacketHandler.CHANNEL }, packetHandler = PacketHandler.class)
public class DirectorMod {
	@Instance("Director")
    public static DirectorMod instance;
	
	@SidedProxy(clientSide = "mw.director.ProxyClient", serverSide = "mw.director.ProxyServer")
    public static ProxyServer proxy;
   
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	this.proxy.load();
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
    	this.proxy.serverStart(event.getServer());
    }
	
}