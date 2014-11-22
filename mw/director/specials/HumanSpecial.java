package mw.director.specials;

import mw.director.EntityDirector;
import mw.director.HumanClient;
import mw.director.SpecialActions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.entity.Entity;

public class HumanSpecial implements SpecialActions {

	private HumanClient entity;
	
	@Override
	public void onUpdate() {}

	@Override
	public void onClientUpdate() {
		double xDiff = this.entity.posX - this.entity.field_71094_bP;
		double yDiff = this.entity.posY - this.entity.field_71095_bQ;
		double zDiff = this.entity.posZ - this.entity.field_71085_bR;
		if (xDiff > 10D) {
			this.entity.field_71094_bP = this.entity.posX;
		}
		if (yDiff > 10D) {
			this.entity.field_71095_bQ = this.entity.posY;
		}
		if (zDiff > 10D) {
			this.entity.field_71085_bR = this.entity.posZ;
		}
		this.entity.field_71091_bM = this.entity.field_71094_bP;
		this.entity.field_71096_bN = this.entity.field_71095_bQ;
		this.entity.field_71097_bO = this.entity.field_71085_bR;
		this.entity.field_71094_bP += xDiff / 4;
		this.entity.field_71095_bQ += yDiff / 4;
		this.entity.field_71085_bR += zDiff / 4;
	}

	@Override
	public void setEntity(EntityDirector ed, Entity e) {
		if (e instanceof HumanClient) {
			this.entity = (HumanClient) e;
		}
	}
	
	public void setSkin(String skin) {
		Minecraft.getMinecraft().getTextureManager().mapTextureObjects.remove(this.entity.locationSkin);
		this.entity.downloadImageSkin = AbstractClientPlayer.getDownloadImage(this.entity.locationSkin, skin, AbstractClientPlayer.locationStevePng, new ImageBufferDownload());
	}
	
	public void setCape(String cape) {
		Minecraft.getMinecraft().getTextureManager().mapTextureObjects.remove(this.entity.locationCape);
		this.entity.downloadImageCape = AbstractClientPlayer.getDownloadImage(this.entity.locationCape, cape, null, null);
	}

}
