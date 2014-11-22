package mw.director;

import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityWolf;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ProxyClient extends ProxyServer {
	public void load() {
		super.load();
		RenderingRegistry.registerEntityRenderingHandler(EntityDirector.class, new RenderDirector());
	}
	
	@Override
	public void player() {
		super.player();
		DirectorMod.instance.apis.put(HumanClient.class, DirectorMod.instance.apis.get(Human.class));
		DirectorMod.instance.parts.put(HumanClient.class, DirectorMod.instance.parts.get(Human.class));
		RenderPlayer renderer = (RenderPlayer) RenderManager.instance.getEntityClassRenderObject(Human.class);
		OverrideRenderer ro = new OverrideRenderer(9, RenderPlayer.class, renderer.mainModel, renderer.modelArmorChestplate, renderer.modelArmor);
		ro.OverrideModelRenderer(2); //bipedBody
		ro.OverrideModelRenderer(0); //bipedHead
		ro.OverrideModelRenderer(1); //bipedHeadwear
		ro.OverrideModelRenderer(3); //bipedRightArm
		ro.OverrideModelRenderer(4); //bipedLeftArm
		ro.OverrideModelRenderer(5); //bipedRightLeg
		ro.OverrideModelRenderer(6); //bipedLeftLeg
		ro.OverrideModelRenderer(7); //bipedEars
		ro.OverrideModelRenderer(8); //bipedCloak
	}
	
	@Override
	public void wolf() {
		super.wolf();
		RenderWolf renderer = (RenderWolf) RenderManager.instance.getEntityClassRenderObject(EntityWolf.class);
		OverrideRenderer ro = new OverrideRenderer(8, RenderWolf.class, renderer.mainModel, renderer.renderPassModel);
		ro.OverrideModelRenderer(1); //wolfBody
		ro.OverrideModelRenderer(0); //wolfHeadMain
		ro.OverrideModelRenderer(2); //wolfLeg1
		ro.OverrideModelRenderer(3); //wolfLeg2
		ro.OverrideModelRenderer(4); //wolfLeg3
		ro.OverrideModelRenderer(5); //wolfLeg4
		ro.OverrideModelRenderer(6); //wolfTale
		ro.OverrideModelRenderer(7); //wolfMane
	}
	
	@Override
	public void creeper() {
		super.creeper();
		RenderCreeper renderer = (RenderCreeper) RenderManager.instance.getEntityClassRenderObject(EntityCreeper.class);
		OverrideRenderer ro = new OverrideRenderer(9, RenderCreeper.class, renderer.mainModel, renderer.creeperModel);
		ro.OverrideModelRenderer(2); //body
		ro.OverrideModelRenderer(0); //head
		ro.OverrideModelRenderer(3); //Back Right Leg
		ro.OverrideModelRenderer(4); //Back Left Leg
		ro.OverrideModelRenderer(5); //Front Right Leg
		ro.OverrideModelRenderer(6); //Front Left Leg
		ro.OverrideModelRenderer(1); //field_78133_b - something over the head?
	}
}
