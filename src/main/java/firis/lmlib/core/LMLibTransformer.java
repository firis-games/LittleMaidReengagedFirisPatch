package firis.lmlib.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import firis.lmmm.builtin.model.ModelLittleMaid_Aug;
import firis.lmmm.builtin.model.ModelLittleMaid_Orign;
import firis.lmmm.builtin.model.ModelLittleMaid_SR2;
import net.minecraft.launchwrapper.IClassTransformer;


/**
 * 古いマルチモデルのロード用。
 * 使用しているクラスを置換えて新しいものへ対応。
 *
 */
public class LMLibTransformer implements IClassTransformer, Opcodes {

	/** logger */
    public static Logger logger = LogManager.getLogger("lmlibrary_coremod");
    
	/** 1.7.2  パッケージ階層 */
	private static final String oldPackageString172 = "mmmlibx/lib/multiModel/model/mc162/";
	
	/** 1.10.2パッケージ階層 */
	private static final String oldPackageString1102 = "net/blacklab/lmr/entity/maidmodel/";
	
	/**
	 * 変換前 -> 変換後のクラス定義
	 */
	@SuppressWarnings("serial")
	private static final Map<String, String> targets = new HashMap<String, String>() {
		{
			//LittleMaidAPI
			addModelClassToTransform(IModelCaps.class, "IModelCaps");
			addModelClassToTransform(ModelCapsHelper.class, "ModelCapsHelper");
			addModelClassToTransform(ModelBox.class, "ModelBox");
			addModelClassToTransform(ModelBoxBase.class, "ModelBoxBase");
			addModelClassToTransform(ModelPlate.class, "ModelPlate");
			addModelClassToTransform(ModelRenderer.class, "ModelRenderer");
			addModelClassToTransform(ModelBase.class, "ModelBase");
			addModelClassToTransform(ModelLittleMaidBase.class, "ModelLittleMaidBase");
			addModelClassToTransform(ModelMultiBase.class, "ModelMultiBase");
			addModelClassToTransform(ModelMultiMMMBase.class, "ModelMultiMMMBase");
			
			//標準LittleMaidモデル
			//標準拡張版を考慮して変換対象とする
			addModelClassToTransform(ModelLittleMaid_Orign.class, "ModelLittleMaid_Orign");
			addModelClassToTransform(ModelLittleMaid_Aug.class, "ModelLittleMaid_Aug");
			addModelClassToTransform(ModelLittleMaid_SR2.class, "ModelLittleMaid_SR2");
			
			//LMR 後方互換用
			put("net/blacklab/lmr/entity/EntityLittleMaid", "net/blacklab/lmr/entity/littlemaid/EntityLittleMaid");
		}
		/**
		 * 各バージョン変換定義を追加する
		 * @param newClazz
		 * @param className
		 * 
		 * 1.6.2  MMM_xxxxx
		 * 1.7.2  mmmlibx/lib/multiModel/model/mc162/xxxxx
		 * 1.10.2 net/blacklab/lmr/entity/maidmodel/xxxxx
		 */
		private void addModelClassToTransform(Class<?> newClazz, String className) {
			String newPackageClassName = newClazz.getName().replace(".", "/"); 
			//1.6.2
			put("MMM_" + className, newPackageClassName);
			//1.7.2
			put(oldPackageString172 + className, newPackageClassName);
			//1.10.2
			put(oldPackageString1102 + className, newPackageClassName);
		}
	};
	
	/**
	 * transform
	 */
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (basicClass != null) {
			return replacer(name, transformedName, basicClass);
		}
		return basicClass;
	}

	/** 変換保持用一時変数 */
	private boolean isChange = false;;

	/**
	 * バイナリを解析して旧MMMLibのクラスを置き換える。
	 * @param name
	 * @param transformedName
	 * @param basicClass
	 * @return
	 */
	private byte[] replacer(String name, String transformedName, byte[] basicClass) {
		
		ClassReader lcreader = new ClassReader(basicClass);
		
		// どのクラスがMMMLibのクラスを使っているかわからないので、全クラスチェックする。当然重い。
		// (親クラスだけでなく、引数や戻り値だけ使っている可能性もある)

		isChange = false;

		// 親クラスの置き換え
		ClassNode lcnode = new ClassNode();
		lcreader.accept(lcnode, 0);
		lcnode.superName = checkMMM(lcnode.superName);
		
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
			logger.info("MultiModel Transform　: " + name);
			return lb;
		}
		return basicClass;
	}
	
	/**
	 * 変換対象のクラスチェックを行う
	 * @param pText
	 * @return
	 * 
	 * clazzNameに入ってくる名称が L[クラス名]; と [クラス名] のみの2パターンがある
	 * そのためindexOfで照合をやっているがその場合例えば
	 * ModelBaseSoloがModelBaseで条件一致しておかしくなる
	 * 対応として末尾に ; を追加して上のような条件を除外できるように対応
	 */
	private String checkMMM(String clazzName) {
		for (Entry<String, String> le : targets.entrySet()) {
			if ((clazzName + ";").indexOf(le.getKey() + ";") > -1) {
				String result = clazzName.replace(le.getKey(), le.getValue());
				isChange = true;
				return result;
			}
		}
		return clazzName;
	}
}
