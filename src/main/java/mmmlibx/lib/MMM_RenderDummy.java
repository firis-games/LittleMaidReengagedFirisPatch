package mmmlibx.lib;

import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class MMM_RenderDummy extends Render {

	public MMM_RenderDummy() {
		super(Minecraft.getMinecraft().getRenderManager());
		shadowSize = 0.0F;
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2,
			float f, float f1) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		// RenderHelper.enableStandardItemLighting();
		RenderHelper.disableStandardItemLighting();
		Tessellator tessellator = Tessellator.getInstance();
		
		// TODO 1.8.8検証
		tessellator.getBuffer().begin(GL11.GL_QUADS, tessellator.getBuffer().getVertexFormat());
		
		GL11.glColor3f(1F, 1F, 1F);
		if (entity instanceof MMM_EntityDummy) {
			int cc = ((MMM_EntityDummy) entity).getColor();
			int ca = MathHelper.floor_float(((MMM_EntityDummy) entity)
					.getAlpha(1.0F) * 256);

			int i = tessellator.getBuffer().getColorIndex(ca);
			int j = cc >> 16 & 255;
			int k = cc >> 8 & 255;
			int l = cc & 255;
			int i1 = cc >> 24 & 255;
			tessellator.getBuffer().putColorRGBA(i, j, k, l, i1);
		}
		double xa = 0.3D;
		double xb = 0.7D;
		// double yy = 0D + 0.10D;// + 0.015625D;
		double yy = 0D + 0.015625D;
		double za = 0.3D;
		double zb = 0.7D;
		
		tessellator.getBuffer().putPosition(xa, yy, za);
		tessellator.getBuffer().putPosition(xa, yy, zb);
		tessellator.getBuffer().putPosition(xb, yy, zb);
		tessellator.getBuffer().putPosition(xb, yy, za);
		
		
		tessellator.draw();
		RenderHelper.disableStandardItemLighting();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
		GL11.glPopMatrix();
		
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
