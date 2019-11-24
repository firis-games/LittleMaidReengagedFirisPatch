package net.blacklab.lmr.entity;

import net.minecraft.world.World;

/**
 * 後方互換用ダミークラス
 * 
 * パッケージ階層がバージョンアップにより変更されているため
 * モデル内部でEntityLittleMaidクラスを利用している一部モデルが正常に動作しない
 * 暫定対応としてダミークラスを用意
 *
 */
public class EntityLittleMaid extends net.blacklab.lmr.entity.littlemaid.EntityLittleMaid {
	public EntityLittleMaid(World par1World) {
		super(par1World);
	}
}