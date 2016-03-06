package littleMaidMobX;

import net.minecraft.tileentity.TileEntity;

public abstract class LMM_EntityModeBlockBase extends LMM_EntityModeBase {

//	protected TileEntity fTile;
	protected double fDistance;

	public LMM_EntityModeBlockBase(LMM_EntityLittleMaid pEntity) {
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
		for (int li = 0; li < owner.maidTiles.length; li++) {
			if (owner.maidTiles[li] != null) {
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
	public boolean overlooksBlock(int pMode) {
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
		for (Object lo : owner.worldObj.loadedEntityList) {
			if (lo == owner) continue;
			if (lo instanceof LMM_EntityLittleMaid) {
				LMM_EntityLittleMaid lem = (LMM_EntityLittleMaid)lo;
				if (lem.isUsingTile(pTile)) {
					// 誰かが使用中
					return true;
				}
			}
		}
		return false;
	}

}
