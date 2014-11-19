package mw.director;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid="Director", name="Director", version="0.0.1", dependencies = "required-after:MWLibrary")
@NetworkMod(clientSideRequired=true, serverSideRequired=true, channels = { DirectorPacketHandler.CHANNEL }, packetHandler = DirectorPacketHandler.class)
public class DirectorMod {
	
	@SideOnly(Side.CLIENT)
	public final Map<Class<? extends Render>, ModelOverrides[]> partRenderers = new HashMap();
	
	public final Map<Class<? extends Entity>, Map<String, Integer>> parts = new HashMap();
	public final Map<Class<? extends Entity>, Special> apis = new HashMap();
	
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