package net.blacklab.lmr.client.sound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.logging.log4j.Level;

import net.blacklab.lib.classutil.FileClassUtil;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.maidmodel.TextureBox;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.FileList;
import net.blacklab.lmr.util.manager.ModelManager;
import net.minecraftforge.fml.common.FMLLog;

/**
 * 新サウンドローディング(from 4.3)
 *
 */
public class SoundLoader {

	protected static SoundLoader instance = new SoundLoader();
	private boolean found = false;
	private boolean sound = false;

	private List<String> loadedTPNames;

	private List<String> pathStore;

	public SoundLoader() {
		pathStore = new ArrayList<String>();
		loadedTPNames = new ArrayList<String>();
	}

	public static boolean isFoundSoundpack() {
		return instance.found;
	}

	public static boolean isSoundLoaded() {
		return instance.sound;
	}

	public static void load() {
		// 読み込みを開始するstaticメソッド．
		// 処理用のメソッドは全てインスタンス内に．
		for (TextureBox box: ModelManager.getTextureList()) {
			String s = box.textureName;
			if (s!=null && !s.isEmpty()) {
				instance.loadedTPNames.add(s);
			}
		}

		instance.searchDir(FileList.dirMods);
		SoundRegistry.copySoundsAdjust();
		instance.appendPath();
		instance.createJson();
	}

	private static Pattern CFG_FILE_PATTERN = Pattern.compile("(.+)\\.cfg");

	private void searchDir(File f) {
		if (!f.isDirectory()) {
			throw new IllegalStateException(f.getName()+" is not a directory!");
		}
		for (File t: f.listFiles()) {
			if (t.isDirectory()) {
				searchDir(t);
			}
			if (t.getName().endsWith(".zip")) {
				searchZip(t, Charset.forName("UTF-8"));
			}
			if (t.getName().endsWith(".ogg")) {
				String c1 = FileClassUtil.getLinuxAntiDotName(t.getAbsolutePath());
				String c2 = FileClassUtil.getLinuxAntiDotName(FileList.dirMods.getAbsolutePath());
				String p = c1.substring(c2.length());
				if (p.startsWith("/")) {
					p = p.substring(1);
				}
				pathStore.add(p);
			}
			Matcher matcher = CFG_FILE_PATTERN.matcher(t.getName());
			if (matcher.find()) {
				String cfgName = matcher.group(1);
				if ("littleMaidMob".equals(cfgName)) {
					LittleMaidReengaged.Debug("Cfg found in file %s", t.getAbsolutePath());
					try {
						decodeConfig(new FileInputStream(t), null);
					} catch (FileNotFoundException e) {
						LittleMaidReengaged.Debug("Cfg read fail: UNEXPECTED NOT FOUND.");
					}
				}
				if (loadedTPNames.contains(cfgName)) {
					try {
						LittleMaidReengaged.Debug("Cfg found (T)in file %s", t.getAbsolutePath());
						decodeConfig(new FileInputStream(t), cfgName);
					} catch (FileNotFoundException e) {
						LittleMaidReengaged.Debug("Cfg read fail: UNEXPECTED NOT FOUND.");
					}
				}
			}
		}
	}

	private void searchZip(File f, Charset charset) {
		String zipname = FileClassUtil.getFileName(FileClassUtil.getLinuxAntiDotName(f.getAbsolutePath()));
		if (zipname.endsWith(".zip")) {
			zipname = zipname.substring(0, zipname.length()-4);
		}
		try {
			FileInputStream inputStream = new FileInputStream(f);
			ZipInputStream zipInputStream = new ZipInputStream(inputStream, charset);
			ZipEntry entry;
			while ((entry = zipInputStream.getNextEntry()) != null) {
				String fName = FileClassUtil.getFileName(entry.getName());
				Matcher matcher = CFG_FILE_PATTERN.matcher(fName);
				if (matcher.find()) {
					String cfgName = matcher.group(1);
					if ("littleMaidMob".equals(cfgName)) {
						LittleMaidReengaged.Debug("Cfg found in zip %s -> %s", f.getAbsolutePath(), entry.getName());
						ZipFile zipFile = new ZipFile(f);
						decodeConfig(zipFile.getInputStream(new ZipArchiveEntry(entry.getName())), loadedTPNames.contains(zipname) ? zipname : null);
						zipFile.close();
					}
					if (loadedTPNames.contains(cfgName)) {
						LittleMaidReengaged.Debug("Cfg found (T)in zip %s -> %s", f.getAbsolutePath(), entry.getName());
						ZipFile zipFile = new ZipFile(f);
						decodeConfig(zipFile.getInputStream(new ZipArchiveEntry(entry.getName())), cfgName);
						zipFile.close();
					}
				}
				if (entry.getName().endsWith(".ogg")) {
					String fString = entry.getName().substring(entry.getName().startsWith("/") ? 1 : 0);
					pathStore.add(fString);
				}
			}
			zipInputStream.close();
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// 文字コード違反
			System.err.println("Loading sound: Unexpected error occured. Maybe sound archive contains "+ charset.name() +" characters?");
			e.printStackTrace();
		}

	}

	private void decodeConfig(InputStream inputStream, String texture) {
		if (texture != null) {
			SoundRegistry.markTexVoiceReserved(texture);
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		List<String> readLines = new ArrayList<String>();
		// 読み込みだけ先に行う
		try {
			String buf;
			while ((buf = bufferedReader.readLine()) != null) {
				readLines.add(buf);
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 先にlivingVoiceRateのみを読み込み
		float livingVoiceRate = 0.2f;
		for (String buf: readLines) {
			if (buf.startsWith("LivingVoiceRate=")) {
				String vals[] = buf.split("=");
				try{
					livingVoiceRate = Float.valueOf(vals[1]);
				} catch(NumberFormatException exception) {}
			}
		}

		// 登録処理
		for (String buf: readLines) {

			int a = buf.indexOf("=");
			if (buf.startsWith("se_") && a != -1) {
				EnumSound sound = EnumSound.valueOf(buf.substring(3, a));
				String enmString = buf.substring(++a);
				LittleMaidReengaged.Debug("LINE READ: detected %s as %s", enmString, sound.toString());

				for (String s : enmString.split(",")) {
					LittleMaidReengaged.Debug("DECODING %s", s);
					String[] vlStrings = s.split(";");
					// テクスチャ色
					Integer col = -1;
					// サウンド名
					String name = vlStrings[vlStrings.length - 1];
					// テクスチャネーム
					String texname = SoundRegistry.DEFAULT_TEXTURE_REGISTRATION_KEY;
					switch (vlStrings.length) {
					case 3:
						texname = vlStrings[0];
					case 2:
						try {
							col = Integer.valueOf(vlStrings[vlStrings.length - 2]);
							if (col > 15) col = 15;
							if (col < -1) col = -1;
						} catch (NumberFormatException e) {
						}
					case 1:
						String tString = texture!=null ? texture : texname;
						if (texture == null && SoundRegistry.isTexVoiceMarked(texname)) {
							LittleMaidReengaged.Debug("TEXTURE %s is marked by cfg", texname);
							break;
						}
						LittleMaidReengaged.Debug("REGISTER NAME %s, %s, %s", tString, col, name);
						SoundRegistry.registerSoundName(sound, tString, col, name);
						break;
					default:
						break;
					}
					if ((sound.index & 0xf00) == EnumSound.living_daytime.index) {
						// LivingSound
						SoundRegistry.setLivingVoiceRatio(name, livingVoiceRate);
					}
				}
			}
		}
		found = true;
	}

	/**
	 * sounds.jsonの生成
	 */
	private void createJson() {
		File jsonDir = new File(FileList.dirMods, "LittleMaidReengaged");
		if (jsonDir.isFile()) {
			throw new IllegalStateException("Remove 'LittleMaidReengaged' file in the mods folder!");
		}
		if (!jsonDir.exists()) {
			if (!jsonDir.mkdir()) {
				FMLLog.log(Level.ERROR, "[LittleMaidReengaged]Making LittleMaidReengaged directory failed.");
				found = false;
				return;
			}
		}

		// JSON書き込み
		File jsonFile = new File(jsonDir, "sounds.json");
		if (jsonFile.isDirectory()) {
			FMLLog.log(Level.ERROR, "[LittleMaidReengaged]There is sounds.json folder?");
			found = false;
			return;
		}
		try {
			// 出力行を生成
			List<CharSequence> output = new ArrayList<CharSequence>();

			// トップブロック
			output.add("{");

				Iterator iterator = SoundRegistry.getRegisteredNamesList().iterator();
				while (iterator.hasNext()) {
					String soundName = (String) iterator.next();

					List m = SoundRegistry.getPathListFromRegisteredName(soundName);
					// サウンド登録名
					output.add("  \"" + soundName + "\": {");

					// サウンドの各種設定
					output.add("    \"category\": \"master\",");
					output.add("    \"sounds\": [");
					if (m!=null && !m.isEmpty()) {
						Iterator n = m.iterator();
						while (n.hasNext()) {
							String path = (String) n.next();
							output.add("      \""+LittleMaidReengaged.DOMAIN+":" + soundName + "//" + path + "\"" + (n.hasNext() ? "," : ""));
						}
					}

					output.add("    ]");

					output.add("  }");

					if (iterator.hasNext()) {
						output.add("  ,");
					}
				}

			output.add("}");

			// Files.writeはJava7以降を要求
			Files.write(jsonFile.toPath(), output, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void appendPath() {
		for (String path : pathStore) {
			// サーチ用に末尾の数値と拡張子を切り落とす
			String p1 = "";
			Pattern pattern = Pattern.compile("(.+?)[0-9]*+\\.ogg");
			Matcher matcher = pattern.matcher(path);
			if (matcher.find()) {
				p1 = matcher.group(1);
			} else {
				continue;
			}

			// サウンドネーム式に置換
			String p2 = p1.replace('/', '.');

			checkPathAndRegister(path, p2, "");
			checkPathAndRegister(path, p2, "littleMaidMob.");
		}
	}
	/**
	 * サウンドネームとパスの比較を行う．
	 * 最低限，サウンドファイルの直上のディレクトリ名が一致していれば登録する．
	 */
	private void checkPathAndRegister(String path, String str, String prefix) {
		int i = -1;
		do {
			String p3 = prefix + str.substring(++i);
			LittleMaidReengaged.Debug("NAME CHECK %s", p3);
			if (SoundRegistry.isSoundNameRegistered(p3)) {
				LittleMaidReengaged.Debug("APPEND SOUND PATH %s as %s", path, p3);
				SoundRegistry.registerSoundPath(p3, path);
			}
			i=str.indexOf(".", i);
		} while (i!=-1 && i!=str.lastIndexOf("."));
	}

}
