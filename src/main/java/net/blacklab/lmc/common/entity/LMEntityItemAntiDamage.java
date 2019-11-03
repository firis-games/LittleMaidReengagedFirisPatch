package net.blacklab.lmc.common.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 炎上と爆発に耐性を持つEntityItem
 * @author computer
 *
 */
@EventBusSubscriber
public class LMEntityItemAntiDamage extends EntityItem {

	public LMEntityItemAntiDamage(World worldIn, double x, double y, double z, ItemStack stack) {
		super(worldIn, x, y, z, stack);
		//炎上しない
		this.isImmuneToFire = true;
	}
	public LMEntityItemAntiDamage(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		//炎上しない
		this.isImmuneToFire = true;
	}
	
	public LMEntityItemAntiDamage(World worldIn) {
		super(worldIn);
		//炎上しない
		this.isImmuneToFire = true;
	}

	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		//voidダメージ以外は無効化する
		if (source.getDamageType().equals(DamageSource.OUT_OF_WORLD.damageType)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * EntityItemの寿命による消滅を回避する
	 * @param event
	 */
	@SubscribeEvent
	public static void onItemExpireEvent(ItemExpireEvent event) {
		if(event.getEntityItem() instanceof LMEntityItemAntiDamage) {
			event.setCanceled(true);
		}
	}
	
}
