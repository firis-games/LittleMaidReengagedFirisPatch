package net.blacklab.lmr.client.resource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.blacklab.lmr.util.FileList;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class OldZipTexturesWrapper implements IResourcePack {

	public static ArrayList<String> keys = new ArrayList<String>();

	@Override
	public InputStream getInputStream(ResourceLocation arg0) throws IOException {
		if(resourceExists(arg0)){
			String key = arg0.getResourcePath();
			if(key.startsWith("/")) key = key.substring(1);
			return FileList.COMMON_CLASS_LOADER.getResourceAsStream(key);
		}
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return null;
	}

	@Override
	public IMetadataSection getPackMetadata(MetadataSerializer arg0,
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
		String key = arg0.getResourcePath();
		if(key.startsWith("/")) key = key.substring(1);
		return keys.contains(key);
	}

}
