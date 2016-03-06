package net.blacklab.lmr.client.gui;

import mmmlibx.lib.ITextureEntity;
import mmmlibx.lib.MMM_GuiTextureSelect;
import mmmlibx.lib.MMM_TextureManager;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.network.NetworkSync;
import net.blacklab.lmr.util.Statics;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

/**
 * 選択時にサーバーへ染料の使用を通知するための処理。
 */
public class GuiTextureSelect extends MMM_GuiTextureSelect {

	public GuiTextureSelect(GuiScreen pOwner, ITextureEntity pTarget,
			int pColor, boolean pToServer) {
		super(pOwner, pTarget, pColor, pToServer);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		super.actionPerformed(par1GuiButton);
		switch (par1GuiButton.id) {
		case 200:
			if (toServer) {
				MMM_TextureManager.instance.postSetTexturePack(target, selectColor, target.getTextureBox());
				if (selectColor != selectPanel.color) {
					// 色情報の設定
//					theMaid.maidColor = selectPanel.color | 0x010000 | (selectColor << 8);
					// サーバーへ染料の使用を通知
					byte ldata[] = new byte[2];
					ldata[0] = Statics.LMN_Server_DecDyePowder;
					ldata[1] = (byte)selectColor;
					NetworkSync.sendToServer(ldata);
				}
			}
			if(target instanceof EntityLittleMaid){
				((EntityLittleMaid)target).requestChangeRenderParamTextureName();
			}
			break;
		}
	}

}
