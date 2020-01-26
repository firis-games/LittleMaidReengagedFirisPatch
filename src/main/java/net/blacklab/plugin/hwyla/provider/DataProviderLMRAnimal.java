package net.blacklab.plugin.hwyla.provider;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.SpecialChars;
import net.blacklab.lmc.common.item.LMItemMaidSugar;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class DataProviderLMRAnimal implements IWailaEntityProvider {
	/**
	 * ハートの下に描画する
	 */
	@Override
	public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		
		//メイドさんの場合は何もしない
		if (entity instanceof EntityLittleMaid) return currenttip;
		
		NBTTagCompound maidNbt = accessor.getNBTData();
		
		boolean icon = false;
		
		//アニマルアイコン
		String strAnimal = "";
		if (maidNbt.hasKey("animal")) {
			icon = true;
			//アイコンの生成
			strAnimal = SpecialChars.getRenderString("waila.stack",
					"1",
					"lmreengaged:maid_souvenir",
					String.valueOf(1), 
					String.valueOf(0));
		}
		
		//アイコン描画
		if (icon) {
			currenttip.add(strAnimal);
		}
		
        return currenttip;
    }
	
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {

		//初期化
		if (tag == null) tag = new NBTTagCompound();
		
		//メイドさんの場合は処理を行わない
		if (ent instanceof EntityLittleMaid) return tag;
		
		//EntityLivingチェック
		if (!(ent instanceof EntityLiving)) return tag;
		
		//メイドさんの情報を取得
		EntityLiving living = (EntityLiving) ent;
		
		//アニマルメイドさん
		NBTTagCompound animal = living.getEntityData();
		if (animal.hasKey(LMItemMaidSugar.ANIMAL_MAID_KEY) ) {
			tag.setBoolean("animal", true);
		}
		
		return tag;
        
    }
	
}
