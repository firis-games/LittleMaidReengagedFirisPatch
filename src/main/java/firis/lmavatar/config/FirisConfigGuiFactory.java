package firis.lmavatar.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import firis.lmavatar.LittleMaidAvatar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

/**
 * GuiConfig管理クラス
 */
public class FirisConfigGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {
	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new FirisGuiConfig(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	/**
	 * GuiConfigの表示設定クラス
	 */
	public static class FirisGuiConfig extends GuiConfig {
		
		public FirisGuiConfig(GuiScreen parentScreen) {
			super(parentScreen,
					getConfigElements(),
					LittleMaidAvatar.MODID,
					false,
					false,
					LittleMaidAvatar.NAME);
		}
		
		/**
		 * GuiConfigに表示する項目を設定する
		 */
		private static List<IConfigElement> getConfigElements() {
			List<IConfigElement> elements = new ArrayList<>();
			
			if (FirisConfig.cfg_lmabatar_maid_avatar) {
				elements.addAll((new ConfigElement(FirisConfig.config.getCategory(FirisConfig.CATEGORY_AVATAR))).getChildElements());
			}
			
			return elements;
		}
	}
}