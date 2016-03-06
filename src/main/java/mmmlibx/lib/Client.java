package mmmlibx.lib;

import java.util.Random;

import mmmlibx.lib.multiModel.model.mc162.ModelBase;
import mmmlibx.lib.multiModel.model.mc162.ModelBoxBase;
import mmmlibx.lib.multiModel.model.mc162.ModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import network.W_Message;
import network.W_Network;

import org.lwjgl.opengl.GL11;

public class Client {

//	public static ItemRenderer itemRenderer;

	/**
	 * 初期化時実行コード
	 */
/*
	public static void init() {
		try {
			// TODO: バージョンアップ時には確認すること
			List lresourcePacks = (List)ModLoader.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), 63);
			lresourcePacks.add(new ModOldResourcePack(mod_MMMLib.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setItemRenderer() {
		if (itemRenderer == null) {
			itemRenderer = new ItemRenderer(Helper.mc);
		}
		if (!(Helper.mc.entityRenderer.itemRenderer instanceof ItemRenderer)) {
			mod_MMMLib.Debug("replace entityRenderer.itemRenderer.");
			Helper.mc.entityRenderer.itemRenderer = itemRenderer;
		}
		if (!(RenderManager.instance.itemRenderer instanceof ItemRenderer)) {
			mod_MMMLib.Debug("replace RenderManager.itemRenderer.");
			RenderManager.instance.itemRenderer = itemRenderer;
		}
		// GUIの表示を変えるには常時監視が必要？
	}
*/
	public static void clientCustomPayload(W_Message var2) {
		// クライアント側の特殊パケット受信動作
		byte lmode = var2.data[0];
		int leid = 0;
		Entity lentity = null;
		if ((lmode & 0x80) != 0) {
			leid = MMM_Helper.getInt(var2.data, 1);
			lentity = MMM_Helper.getEntity(var2.data, 1, MMM_Helper.mc.theWorld);
			if (lentity == null) return;
		}
		MMMLib.Debug("MMM|Upd Clt Call[%2x:%d].", lmode, leid);
		
		switch (lmode) {
		case MMM_Statics.Client_SetTextureIndex:
			// 問い合わせたテクスチャパックの管理番号を受け取る
			MMM_TextureManager.instance.reciveFormServerSetTexturePackIndex(var2.data);
			break;
		case MMM_Statics.Client_SetTexturePackName:
			// 管理番号に登録されているテクスチャパックの情報を受け取る
			MMM_TextureManager.instance.reciveFromServerSetTexturePackName(var2.data);
			break;
		}
	}
/*
	public static void clientConnect(NetClientHandler var1) {
		if (Helper.mc.isIntegratedServerRunning()) {
			Debug("Localmode: InitTextureList.");
			TextureManager.instance.initTextureList(true);
		} else {
			Debug("Remortmode: ClearTextureList.");
			TextureManager.instance.initTextureList(false);
		}
	}

	public static void clientDisconnect(NetClientHandler var1) {
//		super.clientDisconnect(var1);
//		Debug("Localmode: InitTextureList.");
//		TextureManager.initTextureList(true);
	}

	public static void sendToServer(byte[] pData) {
		ModLoader.clientSendPacket(new Packet250CustomPayload("MMM|Upd", pData));
		Debug("MMM|Upd:%2x:NOEntity", pData[0]);
	}
*/
	public static boolean isIntegratedServerRunning() {
		return Minecraft.getMinecraft().isIntegratedServerRunning();
	}

	/**
	 * Duoを使う時は必ずRender側のこの関数を置き換えること。
	 * @param par1EntityLiving
	 * @param par2
	 */
	public static void renderArrowsStuckInEntity(EntityLivingBase par1EntityLiving, float par2,
			ModelBase pModel) {
		int lacount = par1EntityLiving.getArrowCountInEntity();
		
		if (lacount > 0) {
			EntityArrow larrow = new EntityArrow(par1EntityLiving.worldObj, par1EntityLiving.posX, par1EntityLiving.posY, par1EntityLiving.posZ);
			Random lrand = new Random(par1EntityLiving.getEntityId());
			RenderHelper.disableStandardItemLighting();
			
			for (int var6 = 0; var6 < lacount; ++var6) {
				GL11.glPushMatrix();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				ModelRenderer var7 = pModel.getRandomModelBox(lrand);
				ModelBoxBase var8 = var7.cubeList.get(lrand.nextInt(var7.cubeList.size()));
				var7.postRender(0.0625F);
				float var9 = lrand.nextFloat();
				float var10 = lrand.nextFloat();
				float var11 = lrand.nextFloat();
				float var12 = (var8.posX1 + (var8.posX2 - var8.posX1) * var9) / 16.0F;
				float var13 = (var8.posY1 + (var8.posY2 - var8.posY1) * var10) / 16.0F;
				float var14 = (var8.posZ1 + (var8.posZ2 - var8.posZ1) * var11) / 16.0F;
				GL11.glTranslatef(var12, var13, var14);
				var9 = var9 * 2.0F - 1.0F;
				var10 = var10 * 2.0F - 1.0F;
				var11 = var11 * 2.0F - 1.0F;
				var9 *= -1.0F;
				var10 *= -1.0F;
				var11 *= -1.0F;
				float var15 = MathHelper.sqrt_float(var9 * var9 + var11 * var11);
				larrow.prevRotationYaw = larrow.rotationYaw = (float)(Math.atan2(var9, var11) * 180.0D / Math.PI);
				larrow.prevRotationPitch = larrow.rotationPitch = (float)(Math.atan2(var10, var15) * 180.0D / Math.PI);
				double var16 = 0.0D;
				double var18 = 0.0D;
				double var20 = 0.0D;
				float var22 = 0.0F;
//				pRender.renderManager.renderEntityWithPosYaw(larrow, var16, var18, var20, var22, par2);
				Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(larrow, var16, var18, var20, var22, par2);
				GL11.glPopMatrix();
			}
			
			RenderHelper.enableStandardItemLighting();
		}
	}
/*
	public static World getMCtheWorld() {
		if (Helper.mc !=  null) {
			return Helper.mc.theWorld;
		}
		return null;
	}
*/
	public static void setLightmapTextureCoords(int pValue) {
//		int ls = pValue % 65536;
//		int lt = pValue / 65536;
		int ls = pValue & 0xffff;
		int lt = pValue >>> 16;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
				ls / 1.0F, lt / 1.0F);
	}

	public static void setTexture(ResourceLocation pRLocation) {
		if (pRLocation != null) {
			Minecraft.getMinecraft().renderEngine.bindTexture(pRLocation);
		}
	}
/*
	public static String getVersionString() {
		return Minecraft.func_110431_a(Minecraft.getMinecraft());
	}
*/

	public static World getMCtheWorld()
	{
		return Minecraft.getMinecraft().theWorld;
	}

	public static void sendToServer(byte[] ldata) {
		W_Network.sendPacketToServer(1, ldata);
	}
}
