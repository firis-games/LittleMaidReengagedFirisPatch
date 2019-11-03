package net.blacklab.lmr.util.manager;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Modifier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.blacklab.lib.classutil.FileClassUtil;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.mode.EntityModeBase;
import net.blacklab.lmr.util.DevMode;
import net.blacklab.lmr.util.FileList;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

public abstract class ManagerBase {

	protected abstract String getPreFix();
	/**
	 * 追加処理の本体
	 */
	protected abstract boolean append(Class<? extends EntityModeBase> pclass);


	protected void load() {
		// ロード
		for (File pack :
				FileList.filesMods) {
			decodeZip(pack);
		}

		for (File classpathDir :
				FileList.dirClasspath) {
			decodeDirectory(classpathDir, classpathDir);
		}
	}

	private void startSearch(File root, boolean dev){
		if(dev){
			if (root.isDirectory()) {
				// ディレクトリの解析
				decodeDirectory(root, root);
			} else {
				// Zipの解析
				decodeZip(root);
			}
			return;
		}

		// mods]
		String mcv = (String) FMLInjectionData.data()[4];
		LittleMaidReengaged.Debug("MC %s", mcv);
		LittleMaidReengaged.Debug("START SEARCH MODS FOLDER");
		decodeDirectory(root, root);
		for (File lf : root.listFiles()) {
			if (lf.isFile() && (lf.getName().endsWith(".zip") || lf.getName().endsWith(".jar"))) {
				// パッケージ
				decodeZip(lf);
			} else if (lf.isDirectory()) {
				// ディレクトリの解析
				String md = FileClassUtil.getLinuxAntiDotName(lf.getAbsolutePath());
				if (md.endsWith("/")) {
					md = md.substring(0, md.length()-1);
				}

				LittleMaidReengaged.Debug("DIR SEARCH %s", md);
				String mf = FileClassUtil.getFileName(md);
				LittleMaidReengaged.Debug(" SPLICE %s", mf);
				if (mf.equals(mcv)) {
					LittleMaidReengaged.Debug("DEBUG START SEARCH DIVIDED FOLDER");
					startSearch(lf, false);
				}
			}
		}
	}

	private void decodeDirectory(File pfile, File pRoot) {
		// ディレクトリ内のクラスを検索
		for (File lf : pfile.listFiles()) {
			if (lf.isFile()) {
				String lname = lf.getName();
				if (lname.indexOf(getPreFix()) >= 0 && lname.endsWith(".class")) {
					// 対象クラスファイルなのでロード
					//ディレクトリはパスを自動で治してくれないので、手動で。
					loadClass(FileClassUtil.getClassName(
							FileClassUtil.getLinuxAntiDotName(lf.getAbsolutePath()),
							FileClassUtil.getLinuxAntiDotName(pRoot.getAbsolutePath())));
				}
			}else{
				//ディレクトリの場合は中身も捜索
				decodeDirectory(lf, pRoot);
			}
		}
	}

	private void decodeZip(File pfile) {
		// zipファイルを解析
		try {
			FileInputStream fileinputstream = new FileInputStream(pfile);
			ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
			ZipEntry zipentry;

			do {
				zipentry = zipinputstream.getNextEntry();
				if(zipentry == null) {
					break;
				}
				if (!zipentry.isDirectory()) {
					String lname = zipentry.getName();
					if (lname.indexOf(getPreFix()) >= 0 && lname.endsWith(".class")) {
						loadClass(zipentry.getName());
					}
				}
			} while(true);

			zipinputstream.close();
			fileinputstream.close();
		}
		catch (Exception exception) {
			LittleMaidReengaged.Debug("add%sZip-Exception.", getPreFix());
		}

	}

	private void loadClass(String pname) {
		String lclassname = "";
		// 対象ファイルをクラスとしてロード
		try {
			Package lpackage = LittleMaidReengaged.class.getPackage();
			lclassname = pname.endsWith(".class") ? pname.substring(0, pname.lastIndexOf(".class")) : pname;
			Class lclass;
			if(lpackage != null) {
	// TODO ★	lclassname = (new StringBuilder(String.valueOf(lpackage.getName()))).append(".").append(lclassname).toString();
				lclassname = lclassname.replace("/", ".");
// LMM_EntityModeManager でしか使ってないので暫定
				lclass = LittleMaidReengaged.class.getClassLoader().loadClass(lclassname);
			} else {
				lclass = Class.forName(lclassname);
			}
			if (Modifier.isAbstract(lclass.getModifiers())) {
				return;
			}
			if (append(lclass)) {
				LittleMaidReengaged.Debug("get%sClass-done: %s", getPreFix(), lclassname);
			} else {
				LittleMaidReengaged.Debug("get%sClass-fail: %s", getPreFix(), lclassname);
			}
			/*
            if (!(MMM_ModelStabilizerBase.class).isAssignableFrom(lclass) || Modifier.isAbstract(lclass.getModifiers())) {
            	LittleMaidReengaged.Debug(String.format(String.format("get%sClass-fail: %s", pprefix, lclassname)));
                return;
            }

            MMM_ModelStabilizerBase lms = (MMM_ModelStabilizerBase)lclass.newInstance();
            pmap.put(lms.getName(), lms);
            LittleMaidReengaged.Debug(String.format("get%sClass-done: %s[%s]", pprefix, lclassname, lms.getName()));
            */
		}
		catch (Exception exception) {
			LittleMaidReengaged.Debug("get%sClass-Exception.(%s)", getPreFix(), lclassname);
			if(DevMode.DEVELOPMENT_DEBUG_MODE && LMRConfig.cfg_PrintDebugMessage) exception.printStackTrace();
		}
		catch (Error error) {
			LittleMaidReengaged.Debug("get%sClass-Error: %s", getPreFix(), lclassname);
			if(DevMode.DEVELOPMENT_DEBUG_MODE && LMRConfig.cfg_PrintDebugMessage) error.printStackTrace();
		}

	}


}
