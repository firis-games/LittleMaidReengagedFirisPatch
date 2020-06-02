package net.firis.lmt.test.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.world.World;


/**
 * 描画テスト用メイドEntity
 */
public class EntityLittleMaidTest extends EntityTameable {

	/**
	 * コンストラクタ
	 */
	public EntityLittleMaidTest(World worldIn) {
		super(worldIn);
		
		//Sizeを設定しないとダメージを受けて自滅する
        this.setSize(1.0F, 1.0F);
        this.setTamed(false);
	}

	
	/**
	 * 子メイドは生成しない
	 */
	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}
	
}
