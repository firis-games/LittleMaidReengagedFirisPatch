package net.blacklab.lmr.client.resource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.blacklab.lmr.LittleMaidReengaged;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class OldZipTexturesWrapper implements IResourcePack {

	public static ArrayList<String> keys = new ArrayList<String>();

	@Override
	public InputStream getInputStream(ResourceLocation arg0) throws IOException {
		if(resourceExists(arg0)){
			String key = texturesResourcePath(arg0);
			key = containsKey(key);
			return LittleMaidReengaged.class.getClassLoader().getResourceAsStream(key);
		}
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return null;
	}

	@Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer arg0,
			String arg1) throws IOException {
		return null;
	}

	@Override
	public String getPackName() {
		return "OldTexturesLoader";
	}

	@Override
	public Set<String> getResourceDomains() {
		return ImmutableSet.of("minecraft");
	}

	@Override
	public boolean resourceExists(ResourceLocation arg0) {
		
		String key = texturesResourcePath(arg0);
		
		return containsKey(key) == null ? false : true;
	}
	
	/**
	 * テクスチャパックのリソースパスへ変換する
	 * @param path
	 * @return
	 */
	public String texturesResourcePath(ResourceLocation path) {
		
		String key = path.getResourcePath();
		if(key.startsWith("/")) key = key.substring(1);
		
		//旧式用の判定処理
		if (key.toLowerCase().startsWith("mob/modelmulti")
				|| key.toLowerCase().startsWith("mob/littlemaid")) {
			//旧方式は何も加工しない
		} else {
			key = "assets/minecraft/" + key;
		}

		return key;
	}
	
	/**
	 * テクスチャリストの中に対象テクスチャが含まれるかチェックする
	 * 大文字小文字は区別しない
	 * @param path
	 * @return
	 */
	public String containsKey(String path) {
		
		String ret = null;
		
		for (String key : keys) {
			if (key.toLowerCase().equals(path.toLowerCase())) {
				ret = key;
				break;
			}
		}
		
		return ret;
		
	}

}
