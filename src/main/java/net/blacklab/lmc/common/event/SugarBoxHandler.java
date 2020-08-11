package net.blacklab.lmc.common.event;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.util.helper.ItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * シュガーボックスイベント
 * 
 * @author firis-games
 *
 */
public class SugarBoxHandler {

	// シュガーボックスの位置
	protected static Set<TileEntity> tileSugarBox = new HashSet<>();

	/**
	 * シュガーボックスを追加する
	 * 
	 * @param tile
	 */
	public static void AddBlock(TileEntity tile) {
		// 追加
		if (!tile.getWorld().isRemote && !tileSugarBox.contains(tile)) {
			tileSugarBox.add(tile);
		}
	}

	/**
	 * シュガーボックスを削除する
	 * 
	 * @param tile
	 */
	public static void removeBlock(TileEntity tile) {
		// 削除
		if (!tile.getWorld().isRemote && tileSugarBox.contains(tile)) {
			tileSugarBox.remove(tile);
		}
	}

	/**
	 * メイドさんのonUpdateで呼び出される
	 * 
	 * @param envet
	 */
	public static void onUpdate(EntityLittleMaid maid) {
		
		World world = maid.getEntityWorld();
		
		if (world.isRemote) return;
		
		//10sに1回処理を行う
		if (maid.ticksExisted % 200 != 0) return;
		
		//契約メイドのみ
		if (!maid.isContract()) return;
		
		//32個以上持っている場合は何もしない
		int sugar = ItemHelper.getSugarCount(maid);
		if (32 <= sugar) return;
		
		//空きがない場合は何もしない
		if (maid.maidInventory.getFirstEmptyStack() == -1) return;
		
		//メイドさんのいるチャンクとディメンションを取得
		ChunkPos checkChunk = new ChunkPos(maid.getPosition());
		int dimensionId = world.provider.getDimension();
		
		boolean isExtractSugar = false;
		
		//シュガーボックスで処理を行う
		Iterator<TileEntity> iterator = tileSugarBox.iterator();
		while (iterator.hasNext() && !isExtractSugar) {
			TileEntity tileSugarBox = iterator.next();
			try {
				ChunkPos tilePos = new ChunkPos(tileSugarBox.getPos());
				int tileDimensionId = tileSugarBox.getWorld().provider.getDimension();
				
				//有効範囲
				//デフォルト7x7チャンク判定
				int range = LMRConfig.cfg_general_sugar_box_range;
				
				// チャンクの範囲か判断する
				if (dimensionId == tileDimensionId 
						&& tilePos.x - range <= checkChunk.x && checkChunk.x <= tilePos.x + range
						&& tilePos.z - range <= checkChunk.z && checkChunk.z <= tilePos.z + range) {
					
					//シュガーボックスの上にチェスト系アイテムがあるかの判断
					BlockPos pos = new BlockPos(tileSugarBox.getPos().up());
					TileEntity tileInv = world.getTileEntity(pos);
					IItemHandler itemHandler = tileInv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
					//ItemHandlerが実装されている場合
					if (itemHandler != null) {
						//引き出す砂糖の総数を設定
						int extractSugar = 64 - sugar;
						//チェストの中身から砂糖を探す
						for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
							//チェストの中の砂糖を取得する
							ItemStack stack = itemHandler.getStackInSlot(slot);
							if (ItemHelper.isSugar(stack)) {
								//砂糖をチェストから引き出し
								ItemStack extractStack = itemHandler.extractItem(slot, extractSugar, false);
								extractSugar = extractSugar - extractStack.getCount();
								//メイドさんへ砂糖を追加
								maid.maidInventory.addItemStackToInventory(extractStack);
							}
							//規定量引き出し完了
							if (extractSugar == 0) {
								isExtractSugar = true;
								break;
							}
						}
					}
				}
			} catch (Exception e) {}
		}
		
		//引き出し成功した場合はハートマークと音
		if (isExtractSugar) {
			world.setEntityState(maid, (byte)7);
			maid.playSound("entity.item.pickup");
		}
	}
}
