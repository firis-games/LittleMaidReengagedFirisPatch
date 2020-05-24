package net.blacklab.lmr.entity.maidmodel.texture;

import firis.lmlibrary.api.caps.IModelCaps;
import firis.lmlibrary.api.caps.ModelCapsHelper;
import net.blacklab.lmr.util.helper.NetworkHelper;

@Deprecated
public class TextureBoxServer extends TextureBoxBase {

	// ローカルでモデルを保持している時にリンク
	public TextureBox localBox;


	public TextureBoxServer() {
	}

	public TextureBoxServer(TextureBox pBox) {
		localBox		= pBox;
		contractColor	= pBox.getContractColorBits();
		wildColor		= pBox.getWildColorBits();
		textureName		= pBox.textureName;
		setUpdateSize((pBox.models != null && pBox.models[0] != null) ?
				ModelCapsHelper.getCapsValueBoolean(pBox.models[0], IModelCaps.caps_isUpdateSize) : false);
/*
		if (pBox.models != null) {
			modelHeight			= pBox.models[0].getHeight(null);
			modelWidth			= pBox.models[0].getWidth(null);
			modelYOffset		= pBox.models[0].getyOffset(null);
			modelMountedYOffset	= pBox.models[0].getMountedYOffset(null);
		}
*/
	}

	/*
	 * MMM_Statics.Server_GetTextureIndex
	 */
	public void setValue(byte[] pData) {
		contractColor		= NetworkHelper.getShortFromPacket(pData, 2);
		wildColor			= NetworkHelper.getShortFromPacket(pData, 4);
		modelHeight			= NetworkHelper.getFloatFromPacket(pData, 6);
		modelWidth			= NetworkHelper.getFloatFromPacket(pData, 10);
		modelYOffset		= NetworkHelper.getFloatFromPacket(pData, 14);
		modelMountedYOffset	= NetworkHelper.getFloatFromPacket(pData, 18);
		textureName			= NetworkHelper.getStrFromPacket(pData, 22);
	}

	@Override
	public float getHeight(IModelCaps pEntityCaps) {
		return localBox != null ? localBox.models[0].getHeight(pEntityCaps) : modelHeight;
	}

	@Override
	public float getWidth(IModelCaps pEntityCaps) {
		return localBox != null ? localBox.models[0].getWidth(pEntityCaps) : modelWidth;
	}

	@Override
	public float getYOffset(IModelCaps pEntityCaps) {
		return localBox != null ? localBox.models[0].getyOffset(pEntityCaps) : modelYOffset;
	}

	@Override
	public float getMountedYOffset(IModelCaps pEntityCaps) {
		return localBox != null ? localBox.models[0].getMountedYOffset(pEntityCaps) : modelMountedYOffset;
	}

}
