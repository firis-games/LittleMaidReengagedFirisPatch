package net.blacklab.lmc.common.tileentity;

import net.blacklab.lmc.common.event.SugarBoxHandler;
import net.minecraft.util.ITickable;

/**
 * シュガーボックス
 * @author firis-games
 *
 */
public class LMTileSugarBox extends AbstractTileEntity implements ITickable {

	/**
	 * 毎tick処理
	 */
	@Override
	public void update() {
		//リストに登録
		if (!this.isInvalid()) {
			SugarBoxHandler.AddBlock(this);
		}
	}
	
	/**
	 * ブロック削除時
	 */
	@Override
	public void invalidate() {
		super.invalidate();
		SugarBoxHandler.removeBlock(this);
	}

}
