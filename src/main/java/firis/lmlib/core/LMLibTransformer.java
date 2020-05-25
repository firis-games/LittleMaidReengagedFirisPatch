package firis.lmlib.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import com.google.common.collect.Lists;

import firis.lmlib.common.model.ModelLittleMaid_Aug;
import firis.lmlib.common.model.ModelLittleMaid_Orign;
import firis.lmlib.common.model.ModelLittleMaid_SR2;
import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.caps.ModelCapsHelper;
import firis.lmmm.api.model.ModelBase;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.ModelMultiBase;
import firis.lmmm.api.model.ModelMultiMMMBase;
import firis.lmmm.api.model.parts.ModelBox;
import firis.lmmm.api.model.parts.ModelBoxBase;
import firis.lmmm.api.model.parts.ModelPlate;
import firis.lmmm.api.renderer.ModelRenderer;
import net.blacklab.lmr.config.LMRConfig;
import net.minecraft.launchwrapper.IClassTransformer;


/**
 * 古いマルチモデルのロード用。
 * 使用しているクラスを置換えて新しいものへ対応。
 *
 */
public class LMLibTransformer implements IClassTransformer, Opcodes {

	static String oldPackageString = "mmmlibx/lib/multiModel/model/mc162/";
	static String newPackageString = "net/blacklab/lmr/entity/maidmodel/";
	@SuppressWarnings("serial")
	private static final Map<String, String> targets = new HashMap<String, String>() {
		{
			//リトルメイド側の制御に使ってるだけのはず
			//モデル側には不要
//			addModelClassToTransform("IModelBaseMMM", "");
//			addModelClassToTransform("ModelBaseDuo", "");
//			addModelClassToTransform("ModelBaseNihil", "");
//			addModelClassToTransform("ModelBaseSolo", "");

			addModelClassToTransform(IModelCaps.class, "IModelCaps", "caps");
			addModelClassToTransform(ModelCapsHelper.class, "ModelCapsHelper", "caps");

			addModelClassToTransform(ModelBox.class, "ModelBox", "modelparts");
			addModelClassToTransform(ModelBoxBase.class, "ModelBoxBase", "modelparts");
			addModelClassToTransform(ModelPlate.class, "ModelPlate", "modelparts");
			
			//ベースモデルとして継承している可能性があるので
			//差し替え対象
//			addModelClassToTransform("ModelLittleMaid_AC", "lmmodel");
//			addModelClassToTransform("ModelLittleMaid_Archetype", "lmmodel");
			addModelClassToTransform(ModelLittleMaid_Orign.class, "ModelLittleMaid_Orign", "lmmodel");
//			addModelClassToTransform("ModelLittleMaid_RX2", "lmmodel");
			addModelClassToTransform(ModelLittleMaid_Aug.class, "ModelLittleMaid_Aug", "lmmodel");
			addModelClassToTransform(ModelLittleMaid_SR2.class, "ModelLittleMaid_SR2", "lmmodel");
			
			addModelClassToTransform(ModelBase.class, "ModelBase", "base");
			addModelClassToTransform(ModelLittleMaidBase.class, "ModelLittleMaidBase", "base");
			addModelClassToTransform(ModelMultiBase.class, "ModelMultiBase", "base");
			addModelClassToTransform(ModelMultiMMMBase.class, "ModelMultiMMMBase", "base");
			
			addModelClassToTransform(ModelRenderer.class, "ModelRenderer", "renderer");
			
			//addModelClassToTransform("EquippedStabilizer", "");
			//addModelClassToTransform("ModelStabilizerBase", "");
			//addModelClassToTransform("ModelStabilizer_WitchHat", "");

//			put("mmmlibx/lib/MMM_EntityCaps", "net/blacklab/lmr/util/EntityCapsLiving");
//			put("littleMaidMobX/EntityCaps", "net/blacklab/lmr/util/EntityCaps");
			
			//後方互換用
			//撤去予定
			put("net/blacklab/lmr/entity/EntityLittleMaid", "net/blacklab/lmr/entity/littlemaid/EntityLittleMaid");
		}
		private void addModelClassToTransform(Class<?> clazz, String pName, String base) {
//			String newName = (base.equals("") ? "" : base + "/") + pName;
			String newPackageName = clazz.getName().replace(".", "/"); 
//			put("MMM_" + pName, newPackageString + newName);
//			put(oldPackageString + pName, newPackageString + newName);
			put("MMM_" + pName, newPackageName);
			put(oldPackageString + pName, newPackageName);
			
			//互換用
//			if (!base.equals("")) {
//				put(newPackageString + pName, newPackageString + newName);
//			}
			put(newPackageString + pName, newPackageName);
		}
	};

	public static boolean isEnable = false;
	private boolean isChange;


	public static void Debug(String pText, Object... pData) {
		// デバッグメッセージ
		if(LMRConfig.cfg_PrintDebugMessage)
		{
			System.out.println(String.format("Transformer-" + pText, pData));
		}
	}

	public static List<String> ignoreNameSpace = Lists.newArrayList(
		"modchu.model",
		"modchu.lib",
		"net.minecraft.src.mod_Modchu_ModchuLib",
		"modchu.pflm",
		"modchu.pflmf");

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		LMLibTransformer.isEnable = true;

		for(String header : ignoreNameSpace){
			if(name.startsWith(header))	return basicClass;
		}

		if (basicClass != null && isEnable) {
			return replacer(name, transformedName, basicClass);
		}
		return basicClass;
	}

	/**
	 * バイナリを解析して旧MMMLibのクラスを置き換える。
	 * @param name
	 * @param transformedName
	 * @param basicClass
	 * @return
	 */
	private byte[] replacer(String name, String transformedName, byte[] basicClass) {
		ClassReader lcreader = new ClassReader(basicClass);
		final String superName = lcreader.getSuperName();
		final boolean replaceSuper = targets.containsKey(superName);

		// どのクラスがMMMLibのクラスを使っているかわからないので、全クラスチェックする。当然重い。
		// (親クラスだけでなく、引数や戻り値だけ使っている可能性もある)

		isChange = false;

		// 親クラスの置き換え
		ClassNode lcnode = new ClassNode();
		lcreader.accept(lcnode, 0);
		lcnode.superName = checkMMM(lcnode.superName);
		if(replaceSuper)
		{
			Debug("Load Old-MulitiModel: %s extends %s -> %s", name, superName, lcnode.superName);
		}

		// フィールドの置き換え
		for (FieldNode lfn : lcnode.fields) {
			lfn.desc = checkMMM(lfn.desc);
		}

		// メソッドの置き換え
		for (MethodNode lmn : lcnode.methods) {
			lmn.desc = checkMMM(lmn.desc);

			if(lmn.localVariables != null)
			{
				for(LocalVariableNode lvn : lmn.localVariables)
				{
					if(lvn.desc != null) lvn.desc = checkMMM(lvn.desc);
					if(lvn.name != null) lvn.name = checkMMM(lvn.name);
					if(lvn.signature != null) lvn.signature = checkMMM(lvn.signature);
				}
			}

			AbstractInsnNode lin = lmn.instructions.getFirst();
			while(lin != null) {
				if (lin instanceof FieldInsnNode) {	//4
					((FieldInsnNode)lin).desc = checkMMM(((FieldInsnNode)lin).desc);
					((FieldInsnNode)lin).name = checkMMM(((FieldInsnNode)lin).name);
					((FieldInsnNode)lin).owner = checkMMM(((FieldInsnNode)lin).owner);
				} else if (lin instanceof InvokeDynamicInsnNode) {	//6
					((InvokeDynamicInsnNode)lin).desc = checkMMM(((InvokeDynamicInsnNode)lin).desc);
					((InvokeDynamicInsnNode)lin).name = checkMMM(((InvokeDynamicInsnNode)lin).name);
				} else if (lin instanceof MethodInsnNode) {	//5
					((MethodInsnNode)lin).desc = checkMMM(((MethodInsnNode)lin).desc);
					((MethodInsnNode)lin).name = checkMMM(((MethodInsnNode)lin).name);
					((MethodInsnNode)lin).owner = checkMMM(((MethodInsnNode)lin).owner);
				} else if (lin instanceof MultiANewArrayInsnNode) {	//13
					((MultiANewArrayInsnNode)lin).desc = checkMMM(((MultiANewArrayInsnNode)lin).desc);
				} else if (lin instanceof TypeInsnNode) {	//3
					((TypeInsnNode)lin).desc = checkMMM(((TypeInsnNode)lin).desc);
				}
				lin = lin.getNext();
			}
		}

		// バイナリコードの書き出し
		if (isChange) {
			ClassWriter lcwriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
			lcnode.accept(lcwriter);
			byte[] lb = lcwriter.toByteArray();
			Debug("Replace: %s", name);
			return lb;
		}
		return basicClass;
	}

	private String checkMMM(String pText) {
		for (Entry<String, String> le : targets.entrySet()) {
			//pTextに入ってくる名称が L[クラス名]; と [クラス名] のみの2パターンがある
			//そのためindexOfで照合をやっているがその場合例えば
			//ModelBaseSoloがModelBaseで条件一致しておかしくなる
			//対応として末尾に ; を追加して上のような条件を除外できるように対応
			if ((pText+";").indexOf(le.getKey() + ";") > -1) {
				String result = pText.replace(le.getKey(), le.getValue());
//				Debug("%d Hit and Replace: %s -> %s", debugOut, pText, result);
				isChange = true;
				return result;
			}
		}
		return pText;
	}

}
