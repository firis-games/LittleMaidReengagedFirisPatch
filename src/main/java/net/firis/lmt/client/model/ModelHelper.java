package net.firis.lmt.client.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelHelper {

	public static final float degFactor = (float)Math.PI / 180F;
	
	public static ModelRenderer setRotateAngle(ModelRenderer render, float x, float y, float z) {
		
		render.rotateAngleX = x;
		render.rotateAngleY = y;
		render.rotateAngleZ = z;
		
		return render;
	}
	
}
