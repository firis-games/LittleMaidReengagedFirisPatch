package net.blacklab.plugin.hwyla.provider;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.SpecialChars;
import net.blacklab.lmc.common.item.LMItemMaidSugar;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.util.helper.ItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class DataProviderLMR implements IWailaEntityProvider {
	/**
	 * ハートの下に描画する
	 */
	@Override
	public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		
		if (!(entity instanceof EntityLittleMaid)) return currenttip;
		
		NBTTagCompound maidNbt = accessor.getNBTData();
		
		boolean icon = false;
		int sugar = maidNbt.getInteger("sugar");
		
		//砂糖アイコン
		String strSugar = "";
		if (sugar != 0) {
			icon = true;
			strSugar = SpecialChars.getRenderString("waila.stack",
					"1",
					"minecraft:sugar",
					String.valueOf(sugar), 
					String.valueOf(0));
		}
		
		//アニマルアイコン
		String strAnimal = "";
		if (maidNbt.hasKey("animal")) {
			icon = true;
			//アイコンの生成
			strAnimal = SpecialChars.getRenderString("waila.stack",
					"1",
					"lmreengaged:maid_sugar",
					String.valueOf(1), 
					String.valueOf(0));
		} else {
			//空アイコン
			strAnimal = SpecialChars.getRenderString("waila.stack",
					"0");
		}
		
		//アイコン描画
		if (icon) {
			currenttip.add(strSugar + strAnimal);
		}
		
        return currenttip;
    }
	
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {

		//初期化
		if (tag == null) tag = new NBTTagCompound();
		
		if (!(ent instanceof EntityLittleMaid)) return tag;
		
		//メイドさんの情報を取得
		EntityLittleMaid maid = (EntityLittleMaid) ent;
		
		//アニマルメイドさん
		NBTTagCompound animal = maid.getEntityData();
		if (animal.hasKey(LMItemMaidSugar.ANIMAL_MAID_KEY) ) {
			tag.setBoolean("animal", true);
		}
		
		//砂糖
		int sugar = ItemHelper.getSugarCount(maid);
		tag.setInteger("sugar", sugar);
		
		return tag;
        
    }
	
}
