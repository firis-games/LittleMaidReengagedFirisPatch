package net.blacklab.lmr.util;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.util.helper.OwnableEntityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

import java.io.*;
import java.util.*;

/**
 * IFFを管理するためのクラス、ほぼマルチ用。
 * username : null=ローカルプレイ時、Defaultを使う
 */
public class IFF {

	public static final byte iff_Enemy = 0;
	public static final byte iff_Unknown = 1;
	public static final byte iff_Friendry = 2;

	/**
	 * ローカル用、若しくはマルチのデフォルト設定
	 */
	private static Map<String, Byte> DefaultIFF = new TreeMap<>();
	/**
	 * ユーザ毎のIFF
	 */
	private static Map<UUID, Map<String, Byte>> UserIFF = new HashMap<>();

	/**
	 * IFFのゲット
	 */
	public static Map<String, Byte> getUserIFF(UUID pUsername) {
		if (pUsername == null) {
			return DefaultIFF;
		}
//		if (CommonHelper.isLocalPlay()) {
//			pUsername = EntityPlayer.getOfflineUUID("Player");
//		}

		if (!UserIFF.containsKey(pUsername)) {
			// IFFがないので作成
			Map<String, Byte> lmap = new HashMap<>();
			lmap.putAll(DefaultIFF);
			UserIFF.put(pUsername, lmap);
		}
		// 既にある
		return UserIFF.get(pUsername);
	}

	public static void setIFFValue(UUID pUsername, String pName, byte pValue) {
		Map<String, Byte> lmap = getUserIFF(pUsername);
		lmap.put(pName, pValue);
	}

	public static byte checkEntityStatic(String pName, Entity pEntity,
			int pIndex, Map<String, Entity> pMap) {
		byte liff = IFF.iff_Unknown;
		if (pEntity instanceof EntityLivingBase) {
			if (pEntity instanceof EntityArmorStand) {
				liff = iff_Friendry;
			}
			if (pEntity instanceof EntityLittleMaid) {
				switch (pIndex) {
				case 0:
					// 野生種
					liff = IFF.iff_Unknown;
					break;
				case 1:
					// 自分の契約者
					pName = (new StringBuilder()).append(pName).append(":Contract").toString();
					((EntityLittleMaid) pEntity).setContract(true);
					liff = IFF.iff_Friendry;
					break;
				case 2:
					// 他人の契約者
					pName = (new StringBuilder()).append(pName).append(":Others").toString();
					((EntityLittleMaid) pEntity).setContract(true);
					liff = IFF.iff_Friendry;
					break;
				}
			} else if (pEntity instanceof IEntityOwnable) {
				switch (pIndex) {
				case 0:
					// 野生種
					break;
				case 1:
					// 自分の家畜
					pName = (new StringBuilder()).append(pName).append(":Tame").toString();
					if (pEntity instanceof EntityTameable) {
						((EntityTameable) pEntity).setTamed(true);
					}
					liff = IFF.iff_Friendry;
					break;
				case 2:
					// 他人の家畜
					pName = (new StringBuilder()).append(pName).append(":Others").toString();
					if (pEntity instanceof EntityTameable) {
						((EntityTameable) pEntity).setTamed(true);
					}
					liff = IFF.iff_Unknown;
					break;
				}
				if (pIndex != 0) {
					if (pEntity instanceof EntityOcelot) {
						((EntityOcelot) pEntity).setTameSkin(1 + (new Random()).nextInt(3));
					}
				}
			}
			if (pMap != null) {
				// 表示用Entityの追加
				pMap.put(pName, pEntity);
				LittleMaidReengaged.Debug(pName + " added.");
			}

			// IFFの初期値
			if (!DefaultIFF.containsKey(pName)) {
				if (pEntity instanceof IMob) {
					liff = IFF.iff_Enemy;
				}
				DefaultIFF.put(pName, liff);
			}
		}

		return liff;
	}

	/**
	 * 敵味方識別判定
	 */
	public static byte getIFF(UUID pUsername, String entityname, World world) {
		if (entityname == null) {
			return iff_Friendry;
		}
		byte lt = iff_Enemy;
		Map<String, Byte> lmap = getUserIFF(pUsername);
		if (lmap.containsKey(entityname)) {
			lt = lmap.get(entityname);
		} else if (lmap != DefaultIFF && DefaultIFF.containsKey(entityname)) {
			// 未登録だけどDefaultには設定がある時は値をコピー
			lt = DefaultIFF.get(entityname);
			lmap.put(entityname, lt);
		} else {
			// 未登録Entityの場合は登録動作
			int li = entityname.indexOf(":");
			String ls;
			if (li > -1) {
				ls = entityname.substring(0, li);
			} else {
				ls = entityname;
			}
			Entity lentity = EntityList.createEntityByIDFromName(new ResourceLocation(ls), world);
			li = 0;
			if (entityname.indexOf(":Contract") > -1) {
				li = 1;
			} else
			if (entityname.indexOf(":Tame") > -1) {
				li = 1;
			} else
			if (entityname.indexOf(":Others") > -1) {
				li = 2;
			}
			lt = checkEntityStatic(ls, lentity, li, null);
			lmap.put(entityname, lt);
		}
		return lt;
	}

	/**
	 * 敵味方識別判定
	 */
	public static int getIFF(UUID pUsername, Entity entity) {
		if (entity == null || !(entity instanceof EntityLivingBase)) {
			return iff_Friendry;
		}
		String lename = EntityList.getKey(entity) == null ? null : EntityList.getKey(entity).toString();
		String lcname = lename;
		if (lename == null) {
			// 名称未定義MOB、プレーヤーとか？
			return iff_Friendry;
			// return mod_LMM_littleMaidMob.Aggressive ? iff_Unknown :
			// iff_Friendry;
		}
		int li = 0;
		if (entity instanceof EntityLittleMaid) {
			if (((EntityLittleMaid) entity).isContract()) {
				if (((EntityLittleMaid) entity).getMaidMasterUUID().equals(pUsername)) {
					// 自分の
					lcname = (new StringBuilder()).append(lename).append(":Contract").toString();
					li = 1;
				} else {
					// 他人の
					lcname = (new StringBuilder()).append(lename).append(":Others").toString();
					li = 2;
				}
			}
		} else if (entity instanceof IEntityOwnable) {
			UUID loname = OwnableEntityHelper.getOwner((IEntityOwnable)entity);
			if (loname.equals(pUsername)) {
				// 自分の
				lcname = (new StringBuilder()).append(lename).append(":Tame").toString();
				li = 1;
			} else {
				// 他人の
				lcname = (new StringBuilder()).append(lename).append(":Others").toString();
				li = 2;
			}
		}
		if (!getUserIFF(pUsername).containsKey(lcname)) {
			checkEntityStatic(lename, entity, li, null);
		}
		return getIFF(pUsername, lcname, entity.getEntityWorld());
	}

	public static void loadIFFs() {
		// 1.10.2対策(#108)
//			loadIFF("");
		// 初期値
		loadIFF(null);
		
		// UUID別のデータを読み込む
		File lfile = new File((File) FMLInjectionData.data()[6], "config");
		if (!lfile.exists()) {
			lfile.mkdir();
		}
		for (File lf : lfile.listFiles()) {
			LittleMaidReengaged.Debug("FIND FILE %s", lf.getName());
			if (lf.getName().startsWith("littleMaidMob_")&&lf.getName().endsWith(".iff")) {
				String ls = lf.getName().substring(14, lf.getName().length() - 4);
				LittleMaidReengaged.Debug(ls);
				loadIFF(UUID.fromString(ls));
			}
		}
	}

	protected static File getFile(UUID pUsername) {
		LittleMaidReengaged.Debug("GetFile.");
		File lfile;
		if (pUsername == null) {
			lfile = new File("config/littleMaidMob.iff");
		} else {
			String lfilename;
			lfilename = "./config/littleMaidMob_".concat(pUsername.toString()).concat(".iff");
			lfile = new File(lfilename);
		}
		LittleMaidReengaged.Debug(lfile.getAbsolutePath());
		return lfile;
	}

	public static void loadIFF(UUID pUsername) {
		// IFF ファイルの読込み
		// 動作はサーバー側で想定
		File lfile = getFile(pUsername);
		if (!(lfile.exists() && lfile.canRead())) {
			return;
		}
		Map<String, Byte> lmap = getUserIFF(pUsername);

		try {
			FileReader fr = new FileReader(lfile);
			BufferedReader br = new BufferedReader(fr);

			String s;
			while ((s = br.readLine()) != null) {
				String t[] = s.split("=");
				if (t.length > 1) {
					byte i = Byte.valueOf(t[1]);
					if (i > 2) {
						i = iff_Unknown;
					}
					lmap.put(t[0], i);
				}
			}

			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveIFF(UUID pUsername) {
		// IFF ファイルの書込み
		LittleMaidReengaged.Debug("Save IFF, %s", pUsername.toString());
		File lfile = getFile(pUsername);
		Map<String, Byte> lmap = getUserIFF(pUsername);

		try {
			if ((lfile.exists() || lfile.createNewFile()) && lfile.canWrite()) {
				FileWriter fw = new FileWriter(lfile);
				BufferedWriter bw = new BufferedWriter(fw);

				for (Map.Entry<String, Byte> me : lmap.entrySet()) {
					bw.write(String.format("%s=%d\r\n", me.getKey(),
							me.getValue()));
				}

				bw.close();
				fw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
