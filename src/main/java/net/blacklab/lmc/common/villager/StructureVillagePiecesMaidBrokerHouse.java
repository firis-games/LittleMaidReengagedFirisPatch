package net.blacklab.lmc.common.villager;

import java.util.List;
import java.util.Random;

import net.blacklab.lmr.LittleMaidReengaged;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

/**
 * メイドさん斡旋村人スポーン用の家
 * 
 * 小さな家を一つだけ生成しそこで仲介人村人をスポーンさせる
 * @author firis-games
 *
 */
public class StructureVillagePiecesMaidBrokerHouse extends StructureVillagePieces.House4Garden implements IVillageCreationHandler {

	/**
	 * 構造物の初期化処理
	 */
	public static void init() {
		MapGenStructureIO.registerStructureComponent(StructureVillagePiecesMaidBrokerHouse.class, "MaidBrokerHouse");
    	VillagerRegistry.instance().registerVillageCreationHandler(new StructureVillagePiecesMaidBrokerHouse());
	}
	
	/**
	 * IVillageCreationHandler用コンストラクタ
	 */
	public StructureVillagePiecesMaidBrokerHouse() {
	}
	
	/**
	 * StructureVillagePieces.Village用コンストラクタ
	 * @param start
	 * @param type
	 * @param rand
	 * @param p_i45571_4_
	 * @param facing
	 */
	public StructureVillagePiecesMaidBrokerHouse(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox p_i45571_4_, EnumFacing facing) {
		super(start, type, rand, p_i45571_4_, facing);
	}
	
    /**
     * 生成する村人の職業
     */
	@Override
	protected VillagerRegistry.VillagerProfession chooseForgeProfession(int count, net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof)
    {
        return LittleMaidReengaged.MAID_BROKER;
    }
	
	/**
	 * 構造物の差し替え
	 */
	@Override
	protected IBlockState getBiomeSpecificBlockState(IBlockState blockstateIn) {
		return super.getBiomeSpecificBlockState(blockstateIn);
	}

	/**
	 * @interface IVillageCreationHandler
	 * 
	 * 生成優先度設定
	 * 優先度は高めにして最大一つしか生成しないように設定
	 */
	@Override
	public PieceWeight getVillagePieceWeight(Random random, int i) {
		return new StructureVillagePieces.PieceWeight(StructureVillagePiecesMaidBrokerHouse.class, 100, 1);
	}

	/**
	 * @interface IVillageCreationHandler
	 * 
	 * 生成対象のStructureVillagePieces.Villageクラスを返却する
	 */
	@Override
	public Class<?> getComponentClass() {
		return StructureVillagePiecesMaidBrokerHouse.class;
	}

	/**
	 * @interface IVillageCreationHandler
	 * 
	 * 構造物生成用のStructureVillagePieces.Villageインスタンスを返却する
	 * 
	 * house1のcreatePieceをベースに判断する
	 */
	@Override
	public Village buildComponent(PieceWeight villagePiece, Start startPiece, List<StructureComponent> pieces,
			Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, 5, 6, 5, facing);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null 
        		? new StructureVillagePiecesMaidBrokerHouse(startPiece, p5, random, structureboundingbox, facing) 
        		: null;
	}
}
