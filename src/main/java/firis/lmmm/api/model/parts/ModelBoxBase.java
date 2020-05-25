package firis.lmmm.api.model.parts;

import firis.lmmm.api.renderer.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;

public abstract class ModelBoxBase {

	protected PositionTextureVertex[] vertexPositions;
	protected TexturedQuad[] quadList;
	public float posX1;
	public float posY1;
	public float posZ1;
	public float posX2;
	public float posY2;
	public float posZ2;
	public String boxName;


	/**
	 * こちらを必ず実装すること。
	 * @param pMRenderer
	 * @param pArg
	 */
	public ModelBoxBase(ModelRenderer pMRenderer, Object ... pArg) {
	}

	public void render(Tessellator par1Tessellator, float par2) {
		for (int var3 = 0; var3 < quadList.length; ++var3) {
			quadList[var3].draw(par1Tessellator.getBuffer(), par2);
		}
	}

	public ModelBoxBase setBoxName(String pName) {
		boxName = pName;
		return this;
	}

}
