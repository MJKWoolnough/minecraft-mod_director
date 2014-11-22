package mw.director;

import java.lang.reflect.Field;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;

public class OverrideRenderer {

	private final ModelBase[]	models;
	private final ModelOverrides[]	modelOverrides;
	private int			pos	= 0;

	public OverrideRenderer(int numParts, Class<? extends Render> renderClass, ModelBase... models) {
		this.models = models;
		this.modelOverrides = new ModelOverrides[numParts];
		DirectorMod.instance.partRenderers.put(renderClass, this.modelOverrides);
	}

	public void OverrideModelRenderer(int... fieldNum) {
		if (this.pos >= this.modelOverrides.length) {
			return;
		}
		ModelRenderer mr;
		ModelOverrides mo = new ModelOverrides();
		this.modelOverrides[pos] = mo;
		pos++;
		int value = -1;
		for (int i = 0; i < models.length; i++) {
			if (i < fieldNum.length) {
				value = fieldNum[i];
			}
			if (value >= 0) {
				try {
					Field f = this.models[i].getClass().getFields()[value];
					f.set(this.models[i], new ModelRendererD((ModelRenderer) f.get(this.models[i]), mo));
				} catch (Exception e) {
					return;
				}
			}
		}
	}
}
