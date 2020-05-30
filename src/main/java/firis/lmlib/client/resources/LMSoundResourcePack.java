package firis.lmlib.client.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import firis.lmlib.LMLibrary;
import firis.lmlib.api.LMLibraryAPI;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

/**
 * リトルメイドサウンドパック用リソースパック
 * @author firis-games
 *
 */
public class LMSoundResourcePack implements IResourcePack {

	/**
	 * リソースファイルの実体を取得する
	 */
	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException {
		//sounds.json
		if (location.getResourcePath().endsWith("sounds.json")) {
			return LMLibraryAPI.instance().getSoundManager().getResourcepackSoundsJson();
			
		//oggファイル
		} else if (location.getResourcePath().endsWith(".ogg")) {
			String soundPath = LMLibraryAPI.instance().getSoundManager().getResourceClassLoaderPath(location);
			return LMLibrary.class.getClassLoader().getResourceAsStream(soundPath);
		}
		return null;
	}

	/**
	 * リソースファイルの存在チェック
	 */
	@Override
	public boolean resourceExists(ResourceLocation location) {
		//sounds.json
		if (location.getResourcePath().endsWith("sounds.json")) {
			return true;
			
		//oggファイル
		} else if (location.getResourcePath().endsWith(".ogg")) {
			return LMLibraryAPI.instance().getSoundManager().isResourceExists(location);
		}
		return false;
	}

	/**
	 * 利用されるModId
	 */
	@Override
	public Set<String> getResourceDomains() {
		return ImmutableSet.of(LMLibrary.MODID);
	}

	@Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return null;
	}

	/**
	 * リソースパック名
	 */
	@Override
	public String getPackName() {
		return "LMSoundResourcePack";
	}
	
}
