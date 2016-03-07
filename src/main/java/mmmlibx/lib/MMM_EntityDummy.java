package mmmlibx.lib;

import java.util.ArrayList;
import java.util.List;

import net.blacklab.lmr.util.CommonHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * マーカーを表示します。
 */
public class MMM_EntityDummy extends Entity {
	
	private int livecount;
	private final int maxlivecount = 16;
	private int entityColor;
	public Entity entityOwner;
	/**
	 * 有効判定
	 */
	public static boolean isEnable = false;
	
	public static List<MMM_EntityDummy> appendList = new ArrayList<MMM_EntityDummy>();


	public MMM_EntityDummy(World world, int color, Entity owner) {
		super(world);
		livecount = maxlivecount;
		entityColor = color;
//		setSize(1F, 1F);
		entityOwner = owner;
	}
	
	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	public void onUpdate() {
//		super.onUpdate();
		
		if (--livecount < 0 || !isEnable) {
			setDead();
		}
	}

	public float getAlpha(float max) {
		if (livecount >= 0) {
			return max * livecount / maxlivecount;
		}
		return 0F;
	}

	public int getColor() {
		return entityColor;
	}

	public boolean setOwnerdEntityDead(Entity entity) {
		if (entityOwner == entity) {
			setDead();
			return true;
		}
		return false;
	}

	/**
	 * 指定されたオーナーに対応するマーカーを削除します。
	 */
	public static void clearDummyEntity(Entity entity) {
		if (!isEnable) return;
		if (!CommonHelper.isClient) return;
		
		List<Entity> liste = entity.worldObj.loadedEntityList;
		for (Entity entity1 : liste) {
			if (entity1 instanceof MMM_EntityDummy) {
				((MMM_EntityDummy)entity1).setOwnerdEntityDead(entity);
			}
		}
	}

	/**
	 * マーカーを表示する
	 */
	public static void setDummyEntity(Entity owner, int color, double posx, double posy, double posz) {
		if (!isEnable) return;
		if (!CommonHelper.isClient) return;
		
		// サーバー側でしか呼ばれないっぽい
		if (owner.worldObj.isRemote) {
			MMMLib.Debug("L");
		}
		
		MMM_EntityDummy ed = new MMM_EntityDummy(Minecraft.getMinecraft().theWorld, color, owner);
		ed.setPosition(posx, posy, posz);
		appendList.add(ed);
	}

}
