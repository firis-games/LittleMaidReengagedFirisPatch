package firis.lmlibrary.client.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.imageio.ImageIO;

import com.google.common.collect.ImmutableSet;

import firis.lmlibrary.manager.SoundManager;
import net.blacklab.lmr.LittleMaidReengaged;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

/**
 * サウンドパック用
 */
public class SoundResourcePack implements IResourcePack {

	public SoundResourcePack() {
	}

	@Override
	public InputStream getInputStream(ResourceLocation par1ResourceLocation) throws IOException {
		LittleMaidReengaged.Debug("GET STREAM %s", par1ResourceLocation.getResourcePath());
		InputStream inputstream = getResourceStream(par1ResourceLocation);
//		if (inputstream != null) {
		return inputstream;
//		} else {
//			throw new FileNotFoundException(par1ResourceLocation.getResourcePath());
//		}
	}

	private InputStream getResourceStream(ResourceLocation resource) {
		InputStream lis = null;
		if (resource.getResourcePath().endsWith("sounds.json")) {
			
			//sounds.jsonのinputstreamを返却する
			return SoundManager.instance.getResourcepackSoundsJson();
			//Path sound = Paths.get("mods/LittleMaidReengaged/lm_sounds.json");
			//try {
			//	return new FileInputStream(sound.toFile());
			//} catch (FileNotFoundException e) {
			//	e.printStackTrace();
			//}
			//return LittleMaidReengaged.class.getClassLoader().getResourceAsStream("LittleMaidReengaged/sounds.json");
		}
		if (resource.getResourcePath().endsWith(".ogg")) {
			//String soundPath = SoundRegistry.convertPathNameListFromMc1_12_2(decodePathGetPath(resource));
			String soundPath = SoundManager.instance.getResourceClassLoaderPath(resource);
			lis = LittleMaidReengaged.class.getClassLoader().getResourceAsStream(soundPath);
		}
		return lis;
	}

	@Override
	public boolean resourceExists(ResourceLocation resource) {
		LittleMaidReengaged.Debug("RESOURCE CHECK %s", resource.getResourcePath());
		if (resource.getResourcePath().endsWith("sounds.json")) {
			return true;
		}
		if (resource.getResourcePath().endsWith(".ogg")) {
			//String f = decodePathGetName(resource);
			//return SoundRegistry.isSoundNameRegistered(f) ? SoundRegistry.getPathListFromRegisteredName(f)!=null : false;
			return SoundManager.instance.isResourceExists(resource);
		}
		return false;
	}

	/*
	private String decodePathSplicePathStr(ResourceLocation rl) {
		String path = rl.getResourcePath();
		Pattern pattern = Pattern.compile("^/*?sounds/(.+)\\.ogg");
		Matcher matcher = pattern.matcher(path);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private String decodePathGetName(ResourceLocation rl) {
		String f = decodePathSplicePathStr(rl);
		String[] gs = f.split("//");
		return gs[0];
	}

	private String decodePathGetPath(ResourceLocation rl) {
		String f = decodePathSplicePathStr(rl);
		String[] gs = f.split("//");
		return gs.length>1 ? gs[1] : null;
	}
	*/

	public static final Set<String> lmmxResourceDomains = ImmutableSet.of(LittleMaidReengaged.MODID);

	@Override
	public Set<String> getResourceDomains() {
		return lmmxResourceDomains;
	}

	@Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer par1MetadataSerializer, String par2Str)
	{ //throws IOException {
		return null;
	}

	// 未使用
	@Override
	public BufferedImage getPackImage() {// throws IOException {
		try {
			return ImageIO.read(DefaultResourcePack.class.getResourceAsStream("/"
					+ (new ResourceLocation("pack.png")).getResourcePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getPackName() {
		return "SoundPackLMR";
	}

}
