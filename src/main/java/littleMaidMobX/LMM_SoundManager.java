package littleMaidMobX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import mmmlibx.lib.FileManager;
import mmmlibx.lib.MMMLib;
import net.blacklab.lib.classutil.FileClassUtil;
import net.minecraft.util.ResourceLocation;

public class LMM_SoundManager {
	
	/** mods\littleMaidMobX を保持する */
	private File soundDir = null;
	public static final String SoundConfigName = "littleMaidMob.cfg";

	// soundindex, value
	public Map<Integer, String> soundsDefault = new HashMap<Integer, String>();
	// soundIndex, texturePack, color, value
	public Map<Integer, Map<String, Map<Integer, String>>> soundsTexture = new HashMap<Integer, Map<String,Map<Integer,String>>>();
	public float soundRateDefault;
	public Map<String, Map<Integer, Float>> soundRateTexture = new HashMap<String,Map<Integer,Float>>();
	
	public ZipFile soundsZipFile;
	public String tableSwitch = "dir";
	public String soundsDirRootDirString = "";
	public Map<String, File>	soundsStreamFile		= new HashMap<String, File>();
	public Map<String, String>	soundsStreamEntryName	= new HashMap<String, String>();

	public static LMM_SoundManager instance = new LMM_SoundManager();

	public void init() {
		// 初期設定
		soundDir = new File(FileManager.dirMods, "/littleMaidMobX/");
		if (!getSoundDir().exists() || !getSoundDir().isDirectory()) {
			getSoundDir().mkdirs();
			LMM_LittleMaidMobNX.Debug("Create SoundDir: %s", getSoundDir().toString());
		} else {
			LMM_LittleMaidMobNX.Debug("SoundDir: %s", getSoundDir().toString());
		}
	}
	
	public File getSoundDir()
	{
		return soundDir;
	}
	
	public InputStream getSoundJson()
	{
		// 起動時に自動生成される mods/littleMaidMobX/sounds/sounds.json を読み出す
		try
		{
			return new FileInputStream(new File(getSoundDir(), "sounds.json"));
		}
		catch (FileNotFoundException e) {}

		return null;
	}
	
	public InputStream getResourceStream(ResourceLocation resource)
	{
		String path = resource.getResourcePath().toLowerCase();
		
		// よく分からんが .mcmeta はいらないのと思うので消す
		if(path.endsWith(".mcmeta"))
		{
			path = path.substring(0, path.length()-7);
		}

		if(path.equalsIgnoreCase("sounds.json"))
		{
			return instance.getSoundJson();
		}
		
		String fileName = path;
		int c = fileName.lastIndexOf('/');
		if(c >= 0)
		{
			fileName = fileName.substring(c+1);
		}
		
		if(/*(soundsStreamFile.size() > 0||soundsStreamEntryName.size() > 0) && */fileName.endsWith(".ogg"))
		{
			// ClassLoaderにチェンジ
			if(tableSwitch.equals("zip") || tableSwitch.equals("dir")){
				String pString = soundsStreamEntryName.get(fileName);
				if(pString.startsWith("/")) pString = pString.substring(1);
				LMM_LittleMaidMobNX.Debug("REFER %s", pString);
				return FileManager.COMMON_CLASS_LOADER.getResourceAsStream(pString);
//				return soundsZipFile.getInputStream(soundsZipFile.getEntry(pString));
			}/*else if(tableSwitch.equals("dir")){
				try {
					return new FileInputStream(soundsStreamFile.get(fileName));
				} catch (Exception e) {}
			}*/
			//return soundStreamMap.get(fileName);
		}

		return null;
	}
	
	public boolean existsResource(ResourceLocation resource)
	{
		String path = resource.getResourcePath().toLowerCase();
		if(path.endsWith(".mcmeta"))
		{
			return false;
		}

		if(path.equalsIgnoreCase("sounds.json"))
		{
			return true;
		}
		
		String fileName = path;
		int c = fileName.lastIndexOf('/');
		if(c >= 0)
		{
			fileName = fileName.substring(c+1);
		}
		
		if(fileName.endsWith(".ogg"))
		{
			//1.8だとInputStreamをマップに入れておけない
			if(tableSwitch.equals("zip") || tableSwitch.equals("dir")){
				try{
					return soundsStreamEntryName.containsKey(fileName);
				}catch(Exception e){}
			}/*else if(tableSwitch.equals("dir")){
				try{
					return soundsStreamFile.containsKey(fileName);
				}catch(Exception e){}
			}*/
		}

		return false;
	}

	public void setSoundRate(int soundindex, String value, String target) {
		// 文字列を解析して値を設定
		String arg[] = value.split(",");
		Map<Integer, Float> mif;
		if (target == null) {
			target = "";
		} else {
			target = target.trim();
		}
		
		for (String s : arg) {
			if (s.indexOf(';') == -1) {
				// テクスチャ指定詞が無い
				s = s.trim();
				float lf = s.isEmpty() ? 1.0F : Float.valueOf(s);
				if (target.isEmpty()) {
					soundRateDefault = lf;
				} else {
					mif = soundRateTexture.get(target);
					if (mif == null) {
						mif = new HashMap<Integer, Float>();
						soundRateTexture.put(target.trim(), mif);
					}
					mif.put(-1, lf);
				}
			} else {
				// テクスチャ指定詞解析
				String ss[] = s.trim().split(";");
				String ls[];
				if (ss.length < 2) continue;
				if (target.isEmpty()) {
					if (ss.length > 2) {
						ss[0] = ss[0].trim();
						ls = new String[] { ss[0].isEmpty() ? ";" : ss[0], ss[1].trim(), ss[2].trim()};
					} else {
						ls = new String[] { ";", ss[0].trim(), ss[1].trim()};
					}
				} else {
					if (ss.length > 2) {
						ls = new String[] { target, ss[1].trim(), ss[2].trim()};
					} else {
						ls = new String[] { target, ss[0].trim(), ss[1].trim()};
					}
				}
					
				int li = ls[1].isEmpty() ? -1 : Integer.valueOf(ls[1]);
				float lf = ls[2].isEmpty() ? 1.0F : Float.valueOf(ls[2]);
				mif = soundRateTexture.get(ls[0]);
				if (mif == null) {
					mif = new HashMap<Integer, Float>();
					soundRateTexture.put(ls[0], mif);
				}
				mif.put(li, lf);
			}
		}
	}

	public float getSoundRate(String texturename, int colorvalue){
		if (texturename == null || texturename.length() == 0) texturename = ";";
		Map<Integer, Float> mif = soundRateTexture.get(texturename);
		if (mif == null) {
			// 指定詞のものが無ければ無指定のものを検索
			mif = soundRateTexture.get(";");
			if (mif == null) {
				return soundRateDefault;
			}
		}
		Float lf = mif.get(colorvalue);
		if (lf == null) {
			lf = mif.get(-1);
			if (lf == null) {
				return soundRateDefault;
			}
		}
		return lf;
	}

	public void setSoundValue(int soundindex, String value, String target) {
		// 文字列を解析して値を設定
		String arg[] = value.split(",");
		
		for (String s : arg) {
			String tvalue;
			if (s.indexOf(';') == -1) {
				// テクスチャ指定詞が無い
				if (target == null || target.isEmpty()) {
					tvalue = value;
				} else {
					tvalue = (new StringBuilder()).append(target).append(";-1;").append(value).toString();
				}
			} else {
				// テクスチャ指定詞解析
				String ss[] = s.trim().split(";");
				if (ss.length == 2) {
					tvalue = (new StringBuilder()).append(target).append(";").append(value).toString();
				} else {
					tvalue = value;
				}
			}
			setSoundValue(soundindex, tvalue);
		}
	}

	public void setSoundValue(int soundindex, String value) {
		// 文字列を解析して値を設定
		String arg[] = value.split(",");
		
		for (String s : arg) {
			if (s.indexOf(';') == -1) {
				// テクスチャ指定詞が無い
				soundsDefault.put(soundindex, s.trim());
			} else {
				// テクスチャ指定詞解析
				Map<String, Map<Integer, String>> msi = soundsTexture.get(soundindex);
				if (msi == null) {
					msi = new HashMap<String, Map<Integer,String>>();
					soundsTexture.put(soundindex, msi);
				}
				String ss[] = s.trim().split(";");
				if (ss.length < 2) continue;
				if (ss[0].length() == 0) ss[0] = ";";
				Map<Integer, String> mst = msi.get(ss[0]);
				if (mst == null) {
					mst = new HashMap<Integer, String>();
					msi.put(ss[0], mst);
				}
				ss[1] = ss[1].trim();
				int i = ss[1].length() == 0 ? -1 : Integer.valueOf(ss[1]);
				if (ss.length < 3) {
					mst.put(i, "");
				} else {
					mst.put(i, ss[2].trim());
				}
			}
		}
	}

	public String getSoundValue(LMM_EnumSound enumsound, String texturename, int colorvalue){
		if (enumsound == LMM_EnumSound.Null) return null;
		
		Map<String, Map<Integer, String>> msi = soundsTexture.get(enumsound.index);
		if (msi == null) {
			return soundsDefault.get(enumsound.index);
		}
		
		if (texturename == null || texturename.length() == 0) texturename = ";";
		Map<Integer, String> mst = msi.get(texturename);
		if (mst == null) {
			// 指定詞のものが無ければ無指定のものを検索
			mst = msi.get(";");
			if (mst == null) {
				return soundsDefault.get(enumsound.index);
			}
		}
		String s = mst.get(colorvalue);
		if (s == null) {
			s = mst.get(-1);
			if (s == null) {
				return soundsDefault.get(enumsound.index);
			}
		}
		return LMM_LittleMaidMobNX.DOMAIN + ":" + s;
	}

	public void rebuildSoundPack() {
		// 特殊文字を値に変換
		// Default
		Map<Integer, String> lmap = new HashMap<Integer, String>();
		lmap.putAll(soundsDefault);
		for (Entry<Integer, String> lt : soundsDefault.entrySet()) {
			int li = lt.getKey();
			if (lt.getValue().equals("^")) {
				String ls = lmap.get(li & -16);
				if (ls != null && (li & 0x0f) != 0 && !ls.equals("^")) {
					lmap.put(li, ls);
//					soundsDefault.put(li, ls);
					LMM_LittleMaidMobNX.Debug(String.format("soundsDefault[%d] = [%d]", li, li & -16));
				} else {
//					soundsDefault.remove(li);
					LMM_LittleMaidMobNX.Debug(String.format("soundsDefault[%d] removed.", li));
				}
			} else {
				lmap.put(li, lt.getValue());
			}
		}
		soundsDefault = lmap;
		
		// Texture
		for (Entry<Integer, Map<String, Map<Integer, String>>> mim : soundsTexture.entrySet()) {
			for (Entry<String, Map<Integer, String>> msm : mim.getValue().entrySet()) {
				
				for (Entry<Integer, String> mis : msm.getValue().entrySet()) {
					if (mis.getValue().equals("^")) {
						boolean lf = false;
						if ((mim.getKey() & 0x0f) != 0) {
							Map<String, Map<Integer, String>> lmsm = soundsTexture.get(mim.getKey() & -16);
							if (lmsm != null) {
								Map<Integer, String> lmis = lmsm.get(msm.getKey());
								if (lmis != null) {
									String ls = lmis.get(mis.getKey());
									if (ls != null && !ls.equals("^")) {
										msm.getValue().put(mis.getKey(), ls);
										lf = true;
										LMM_LittleMaidMobNX.Debug(String.format("soundsTexture[%d, %s, %d] = [%d]", mim.getKey(), msm.getKey(), mis.getKey(), mim.getKey() & -16));
									}
								}
							}
						}
						if (!lf) {
							msm.getValue().remove(mis.getKey());
							LMM_LittleMaidMobNX.Debug(String.format("soundsTexture[%d, %s, %d] removed.", mim.getKey(), msm.getKey(), mis.getKey()));
						}
					}
				}
			}
		}
	}

	public void decodeSoundPack(String fileName, Reader reader, boolean iswrite, boolean isdefault) {
		// サウンドパックを解析して音声を設定
		try {
			List<LMM_EnumSound> list1 = new ArrayList<LMM_EnumSound>();
			list1.addAll(Arrays.asList(LMM_EnumSound.values()));
			list1.remove(LMM_EnumSound.Null);
			BufferedReader breader = new BufferedReader(reader);
			boolean loadsoundrate = false;
			String str;
			String packname = fileName;
			packname = packname.substring(0, packname.lastIndexOf("."));
			while ((str = breader.readLine()) != null) {
				str = str.trim();
				if (str.isEmpty() || str.startsWith("#")) continue;
				int i = str.indexOf('=');
				if (i > -1) {
					String name = str.substring(0, i).trim();
					String value = str.substring(i + 1).trim();
					
					int index = -1;
					if (name.startsWith("se_")) {

						//サウンドパックのファイル構成が正しいとは限らないため、ファイル構造を無視して読みだす
						int cd = value.lastIndexOf('.');
						if(cd >= 0) value = value.substring(cd+1);

						//音声ファイルの指定文字列の末尾に数値が付いてしまっているパックがあるので削除
						value = value.replaceAll("\\d+$", ""); // ファイルの終わりの数値部分を削除
						
						String ss = name.substring(3);
						try {
							index = LMM_EnumSound.valueOf(ss).index;
							list1.remove(LMM_EnumSound.valueOf(ss));
						}
						catch (Exception exception) {
							LMM_LittleMaidMobNX.Debug(String.format("unknown sound parameter:%s.cfg - %s", packname, ss));
						}
					} else if (name.equals("LivingVoiceRate")) {
						if (isdefault) {
							setSoundRate(index, value, null);
						} else {
							setSoundRate(index, value, packname);
						}
						loadsoundrate = true;
					}
					if (index > -1) {
						if (isdefault) {
							setSoundValue(index, value);
						} else {
							setSoundValue(index, value, packname);
						}
		    			LMM_LittleMaidMobNX.Debug(String.format("%s(%d) = %s", name, index, value));
					}
				}
			}
			breader.close();
			
			// コンフィグファイルがフォルダ内の場合のみ書き込む(Zip内の場合は書き込まない)
			if(iswrite)
			{
				// 無かった項目をcfgへ追加
				if (!list1.isEmpty()) {
					BufferedWriter bwriter = new BufferedWriter(new FileWriter(fileName, true));
					for (int i = 0; i < list1.size(); i++) {
						writeBuffer(bwriter, list1.get(i));
					}
					bwriter.close();
				}
				if (!loadsoundrate) {
					BufferedWriter bwriter = new BufferedWriter(new FileWriter(fileName, true));
					writeBufferSoundRate(bwriter, 1.0F);
					bwriter.close();
				}
			}
		}
		catch (Exception exception) {
			LMM_LittleMaidMobNX.Debug("decodeSound Exception.");
		}
	}

	public void loadDefaultSoundPack()
	{
		try
		{
			boolean loadCfg = loadSoundPackCfg();
		
			if(loadCfg == false)
			{
				File soundCfg = new File(getSoundDir(), "default_" + SoundConfigName);
				LMM_LittleMaidMobNX.Debug(soundCfg.getName());
				FileManager.COMMON_CLASS_LOADER.addURL(soundCfg.toURI().toURL());
				createDefaultSoundPack(soundCfg);
			}
		}
		catch (Exception e)
		{
			LMM_LittleMaidMobNX.Debug("Error: Create Sound cfg failed.");
			e.printStackTrace();
		}
		rebuildSoundPack();
	}
	
	/** mods 直下のディレクトリとZipを全て検索、ディレクトリ内のZipはチェックしない */
	public boolean loadSoundPackCfg() throws IOException
	{
		for(File file : FileManager.dirMods.listFiles())
		{
			System.out.println("DEBUG FILE "+file.getName());
			if(file.isDirectory())
			{
				if(searchSoundCfgDir(file))
				{
					tableSwitch = "dir";
					soundsDirRootDirString = FileClassUtil.getLinuxAntiDotName(file.getAbsolutePath());
					putAllSoundStream(file);
					createSoundJson(file);
					FileManager.COMMON_CLASS_LOADER.addURL(file.toURI().toURL());
					return true;
				}
			}
			else if(file.getName().toLowerCase().endsWith(".zip"))
			{
				if(searchSoundCfgZip(file))
				{
					tableSwitch = "zip";
					createSoundJson(file);
					FileManager.COMMON_CLASS_LOADER.addURL(file.toURI().toURL());
					return true;
				}
			}
		}
		return false;
	}

	public void putAllSoundStream(File dir) throws IOException
	{
		for(File file : dir.listFiles())
		{
			String name = file.getName().toLowerCase();
			if(file.isDirectory())
			{
				putAllSoundStream(file);
			}
			else if(name.endsWith(".ogg"))
			{
				String pString = FileClassUtil.getLinuxAntiDotName(file.getAbsolutePath());
				String rString = pString.substring(soundsDirRootDirString.length());
				LMM_LittleMaidMobNX.Debug("PUT %s %s", name, rString);
				soundsStreamEntryName.put(name, rString);
//				soundsStreamFile.put(name, file);
			}
		}
	}
	
	// mods配下の全フォルダからコンフィグファイルを検索する
	// 最初に見つけた時点で終了する。2つ以上サウンドパックを入れた場合、どちらが使われるかは保証できない。
	public boolean searchSoundCfgDir(File dir) throws IOException
	{
		for(File file : dir.listFiles())
		{
			if(file.isDirectory())
			{
				if(searchSoundCfgDir(file))
				{
					return true;
				}
			}
			else if(file.getName().equalsIgnoreCase(SoundConfigName))
			{
				Reader reader = new FileReader(file);
				
				decodeSoundPack(file.getName(), reader, false, true);
				
				reader.close();
				
				return true;
			}
		}
		return false;
	}
	
	// zip配下からコンフィグファイルを検索する
	// 最初に見つけた時点で終了する。2つ以上サウンドパックを入れたら保証できない。
	public boolean searchSoundCfgZip(File file)
	{
		boolean foundCfg = false;
		try
		{
			FileInputStream fileinputstream = new FileInputStream(file);
			ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
			ZipEntry zipentry;
			do
			{
				zipentry = zipinputstream.getNextEntry();
				if(zipentry == null)
				{
					break;
				}
				if (!zipentry.isDirectory())
				{
					String name = zipentry.getName();
					int c = name.lastIndexOf("/");
					if(c >= 0)
					{
						name = name.substring(c+1);
					}
					name = name.toLowerCase();
					if (foundCfg==false && name.equalsIgnoreCase(SoundConfigName))
					{
						ZipFile zipfile = new ZipFile(file);
							InputStream inputStream = zipfile.getInputStream(zipentry);
								Reader reader = new InputStreamReader(inputStream);
									decodeSoundPack(name, reader, false, true);
								reader.close();
							inputStream.close();
						zipfile.close();

						foundCfg = true;
						break;
					}
				}
			}
			while(true);
			
			zipinputstream.close();
			fileinputstream.close();
			
			// .cfgを見つけたら、サウンドパックと判断し、oggを全て読み出す
			if(foundCfg)
			{
				soundsZipFile = new ZipFile(file, 1, StandardCharsets.UTF_8);
				fileinputstream = new FileInputStream(file);
				zipinputstream = new ZipInputStream(fileinputstream, StandardCharsets.UTF_8);
				do
				{
					zipentry = zipinputstream.getNextEntry();
					if(zipentry == null)
					{
						break;
					}
					if (!zipentry.isDirectory())
					{
						String name = zipentry.getName();
						int c = name.lastIndexOf("/");
						if(c >= 0)
						{
							name = name.substring(c+1);
						}
						name = name.toLowerCase();
						if (name.endsWith(".ogg"))
						{
							LMM_LittleMaidMobNX.Debug("PUT %s %s", name, zipentry.getName());
							soundsStreamEntryName.put(name, zipentry.getName());
						}
					}
				}
				while(true);
				
				zipinputstream.close();
				fileinputstream.close();
			}
		}
		catch (Exception exception)
		{
			MMMLib.Debug("Load Sound pack Zip-Exception.");
		}
		return foundCfg;
	}

	public boolean createDefaultSoundPack(File file1) {
		// サウンドのデフォルト値を設定
		for (LMM_EnumSound eslm : LMM_EnumSound.values()) {
			if (eslm == LMM_EnumSound.Null) continue;
			setSoundValue(eslm.index, eslm.DefaultValue);
		}
		
		// デフォルトサウンドパックを作成
		if (file1.exists()) {
			return false;
		}
		try {
			if (file1.createNewFile()) {
				BufferedWriter bwriter = new BufferedWriter(new FileWriter(file1));
				
				for (LMM_EnumSound eslm : LMM_EnumSound.values()) {
					writeBuffer(bwriter, eslm);
				}
				// LivingVoiceRate
				writeBufferSoundRate(bwriter, 1.0F);
				
				bwriter.close();
				LMM_LittleMaidMobNX.Debug("Success create Default Sound cfg.");
			}
		} catch (IOException e) {
			LMM_LittleMaidMobNX.Debug("Failed create Default Sound cfg(%s).", file1.getAbsolutePath());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected static void writeBuffer(BufferedWriter buffer, LMM_EnumSound enumsound) throws IOException {
		// 渡されたWBufferへ書き込む
		if (enumsound == LMM_EnumSound.Null) return;
		
		buffer.write("# ");
		buffer.write(enumsound.info);
		buffer.newLine();
		
		buffer.write("se_");
		buffer.write(enumsound.name());
		buffer.write("=");
		buffer.write(enumsound.DefaultValue);
		buffer.newLine();
		buffer.newLine();
	}

	protected static void writeBufferSoundRate(BufferedWriter buffer, float prate) throws IOException {
		// 渡されたWBufferへ書き込む
		buffer.write("# Living Voice Rate. 1.0=100%, 0.5=50%, 0.0=0%");
		buffer.newLine();
		buffer.write("LivingVoiceRate=" + prate);
		buffer.newLine();
		buffer.newLine();
	}

	/** 引数には、サウンドが入ったフォルダか、zipを指定 */
	public void createSoundJson(File dir)
	{
		if(!getSoundDir().exists() || !getSoundDir().isDirectory())
		{
			return;
		}
		
		File file1 = new File(getSoundDir(), "sounds.json");
		try {
			BufferedWriter bwriter = new BufferedWriter(new FileWriter(file1));
			
			String str = searchSoundAndWriteFile("", dir, "");
			bwriter.write("{\n" + str + "\n}\n");
			bwriter.newLine();
			
			bwriter.close();
			LMM_LittleMaidMobNX.Debug("Success create Sounds.json(%s).", file1.getAbsolutePath());
		} catch (IOException e) {
			LMM_LittleMaidMobNX.Debug("Failed create Sounds.json(%s).", file1.getAbsolutePath());
			e.printStackTrace();
		}
	}

	private static String searchSoundAndWriteFile(String string, File dir, String string2) throws IOException
	{
		if(dir.isDirectory())
		{
			return searchSoundAndWriteFileDir(string, dir, string2);
		}
		return searchSoundAndWriteFileZip(string, dir);
	}

	// 再帰的にフォルダを捜査し、音声ファイルをファイル出力する
	/* 出力例
		{
		"akari":{"category":"master","sounds":["akari1","akari2","akari3"]},
		"attack":{"category":"master","sounds":["attack01","attack02","attack03"]}
		}
	*/
	public static String searchSoundAndWriteFileDir(String output, File dir, String path) throws IOException
	{
		for(File file : dir.listFiles())
		{
			if(file.isDirectory())
			{
				output = output + searchSoundAndWriteFileDir(output, file, path + file.getName() +".");
			}
		}

		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		for(File file : dir.listFiles())
		{
			if(file.isFile() && file.getName().endsWith(".ogg"))
			{
				final String fileName  = file.getName().substring(0, file.getName().length()-4); // 拡張子削除
				final String soundName = fileName.replaceAll("\\d+$", ""); // ファイルの終わりの数値部分を削除
				final String name = fileName.replace(".", "/");
				if(!map.containsKey(soundName))
				{
					map.put(soundName, new ArrayList<String>());
				}
				map.get(soundName).add(name);
			}
		}
		for(String key : map.keySet())
		{
			String s = "";
			for(String name : map.get(key))
			{
				if(s.isEmpty())
				{
					s = "\""+key+"\":{\"category\":\"master\",\"sounds\":[";
				}
				else
				{
					s = s + ",";
				}
				s = s + "\"" + name + "\"";
			}
			s = s + "]}";

			if(!output.isEmpty())
			{
				output = output + ",\n";
			}
			output = output + s;
		}
		
		return output;
	}
	public static String searchSoundAndWriteFileZip(String output, File dir) throws IOException
	{
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		try
		{
			FileInputStream fileinputstream = new FileInputStream(dir);
			ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
			ZipEntry zipentry;
			do
			{
				zipentry = zipinputstream.getNextEntry();
				if(zipentry == null)
				{
					break;
				}
				String fileNameInZip = zipentry.getName();
				if (!zipentry.isDirectory() && fileNameInZip.endsWith(".ogg"))
				{
					String fileName  = fileNameInZip.substring(0, fileNameInZip.length()-4); // 拡張子削除
					int c = fileName.lastIndexOf('/');
					if(c >= 0)
					{
						fileName = fileName.substring(c+1);
					}
					
					final String soundName = fileName.replaceAll("\\d+$", ""); // ファイルの終わりの数値部分を削除
					final String name = fileName.replace(".", "/");
					if(!map.containsKey(soundName))
					{
						map.put(soundName, new ArrayList<String>());
					}
					map.get(soundName).add(name);
				}
			}
			while(true);

			zipinputstream.close();
			fileinputstream.close();

			for(String key : map.keySet())
			{
				String s = "";
				for(String name : map.get(key))
				{
					if(s.isEmpty())
					{
						s = "\""+key+"\":{\"category\":\"master\",\"sounds\":[";
					}
					else
					{
						s = s + ",";
					}
					s = s + "\"" + name + "\"";
				}
				s = s + "]}";

				if(!output.isEmpty())
				{
					output = output + ",\n";
				}
				output = output + s;
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		
		return output;
	}
}