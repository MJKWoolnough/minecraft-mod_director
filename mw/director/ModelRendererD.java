package mw.director;

import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModelRendererD extends ModelRenderer {
	
	private final ModelOverrides overrides;
	
	public static final float DEGtoRAD = (float)Math.PI / 180;
	
	protected static boolean override = false;
	
	public ModelRendererD(ModelRenderer mr, ModelOverrides overrides) {
		super(mr.baseModel, mr.boxName);
		this.setTextureOffset(mr.textureOffsetX, mr.textureOffsetY);
		this.textureWidth = mr.textureWidth;
		this.textureHeight = mr.textureHeight;
		this.rotationPointX = mr.rotationPointX;
		this.rotationPointY = mr.rotationPointY;
		this.rotationPointZ = mr.rotationPointZ;
		this.rotateAngleX = mr.rotateAngleX;
		this.rotateAngleY = mr.rotateAngleY;
		this.rotateAngleZ = mr.rotateAngleZ;
		this.mirror = mr.mirror;
		this.showModel = mr.showModel;
		this.isHidden = mr.isHidden;
		this.cubeList = mr.cubeList;
		this.childModels = mr.childModels;
		this.overrides = overrides;
	}
	
	@SideOnly(Side.CLIENT)
	public void render(float par1) {
		if (override) {
			if (this.overrides.Angles[0] == this.overrides.Angles[0]) {
				this.rotateAngleX = this.overrides.Angles[0] * DEGtoRAD;
			}
			if (this.overrides.Angles[1] == this.overrides.Angles[1]) {
				this.rotateAngleY = this.overrides.Angles[1] * DEGtoRAD;
			}
			if (this.overrides.Angles[2] == this.overrides.Angles[2]) {
				this.rotateAngleZ = this.overrides.Angles[2] * DEGtoRAD;
			}
			if (this.overrides.Points[0] == this.overrides.Points[0]) {
				this.rotationPointX = this.overrides.Points[0];
			}
			if (this.overrides.Points[1] == this.overrides.Points[1]) {
				this.rotationPointY = this.overrides.Points[1];
			}
			if (this.overrides.Points[2] == this.overrides.Points[2]) {
				this.rotationPointZ = this.overrides.Points[2];
			}
		}
		super.render(par1);
	}
}
