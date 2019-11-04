package net.blacklab.lmr.entity.littlemaid.mode;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

/**
 * TileEntityに対する作業を行うモードの基底クラス
 *
 */
public abstract class EntityModeBlockBase extends EntityModeBase {

//	protected TileEntity fTile;
	protected double fDistance;

	public EntityModeBlockBase(EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public void updateBlock() {
		// 基準となるTileをセット
		owner.setTilePos(0);
	}

	/**
	 * すでに使用中のTileがある場合はshouldBlockへ飛ぶようにする。
	 */
	@Override
	public boolean isSearchBlock() {
		boolean lflag = false;
		for (int li = 0; li < owner.getMaidTiles().length; li++) {
			if (owner.getMaidTiles()[li] != null) {
				TileEntity ltile = owner.getTileEntity(li);
				if (ltile != null && !checkWorldMaid(ltile)) {
					if (!lflag) {
						owner.setTilePos(ltile);
					}
					lflag = true;
				}
			}
		}
		return !lflag;
	}

	@Override
	public boolean overlooksBlock(String pMode) {
		if (owner.isTilePos()) {
			owner.setTilePos(0);
		}
		return true;
	}


	/**
	 * 他のメイドが使用しているかをチェック。
	 * @return
	 */
	protected boolean checkWorldMaid(TileEntity pTile) {
		// 世界のメイドから
		for (Entity lo : owner.getEntityWorld().loadedEntityList) {
			if (lo == owner) continue;
			if (lo instanceof EntityLittleMaid) {
				EntityLittleMaid lem = (EntityLittleMaid)lo;
				if (lem.isUsingTile(pTile)) {
					// 誰かが使用中
					return true;
				}
			}
		}
		return false;
	}

}
