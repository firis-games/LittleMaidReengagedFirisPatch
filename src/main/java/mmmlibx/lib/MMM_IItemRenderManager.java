package mmmlibx.lib;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * アイテム用の特殊レンダーに継承させるインターフェース。
 * 現状、継承させていなくてもメソッドがItemに記述されていれば動作する。
 */
@SuppressWarnings("deprecation")
public interface MMM_IItemRenderManager {

	public static final int VM_FIRST_PERSON		= 0;
	public static final int VM_THERD_PERSON		= 1;
	public static final int VM_THERD_PERSON_INV	= 2;


	/**
	 * アイテムの描画のみ、位置補正はしない。
	 * @param pEntity
	 * @param pItemStack
	 * @param par3
	 * @return
	 */
	public boolean renderItem(Entity pEntity, ItemStack pItemStack, TransformType par3);
//	public boolean renderItemInFirstPerson(float pDeltaTimepRenderPhatialTick, MMM_ItemRenderer pItemRenderer);
	public boolean renderItemInFirstPerson(Entity pEntity, ItemStack pItemStack, float pDeltaTimepRenderPhatialTick);
	public boolean renderItemWorld(ItemStack pItemStack);
	public ResourceLocation getRenderTexture(ItemStack pItemStack);
	public boolean isRenderItem(ItemStack pItemStack);
	public boolean isRenderItemInFirstPerson(ItemStack pItemStack);
	public boolean isRenderItemWorld(ItemStack pItemStack);

}
