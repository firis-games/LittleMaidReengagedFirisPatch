package net.blacklab.lmr.util.transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.blacklab.lmr.LittleMaidReengaged;
import net.minecraft.launchwrapper.IClassTransformer;

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

import mmmlibx.lib.MMMLib;


/**
 * 古いマルチモデルのロード用。<br>
 * 使用しているクラスを置換えて新しいものへ対応。
 *
 */
public class Transformer implements IClassTransformer, Opcodes {

	static String packageString = "mmmlibx/lib/multiModel/model/mc162/";
	@SuppressWarnings("serial")
	private static final Map<String, String> targets = new HashMap<String, String>() {
		{
			add("EquippedStabilizer");
			add("IModelBaseMMM");
			add("IModelCaps");
			add("ModelBase");
			add("ModelBaseDuo");
			add("ModelBaseNihil");
			add("ModelBaseSolo");
			add("ModelBox");
			add("ModelBoxBase");
			add("ModelCapsHelper");
			add("ModelLittleMaid_AC");
			add("ModelLittleMaid_Archetype");
			add("ModelLittleMaid_Orign");
			add("ModelLittleMaid_RX2");
			add("ModelLittleMaid_Aug");
			add("ModelLittleMaid_SR2");
			add("ModelLittleMaidBase");
			add("ModelMultiBase");
			add("ModelMultiMMMBase");
			add("ModelPlate");
			add("ModelRenderer");
			add("ModelStabilizerBase");
		}
		private void add(String pName) {
			String replaceName = pName;
			put("MMM_" + pName, packageString + replaceName);
		}
	};

	public static boolean isEnable = false;
	private boolean isChange;


	public static void Debug(String pText, Object... pData) {
		// デバッグメッセージ
		if(LittleMaidReengaged.cfg_PrintDebugMessage)
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
		//MMMLibが立ち上がった時点で旧モデル置き換えを開始
		Transformer.isEnable = true;

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
			if (pText.indexOf(le.getKey()) > -1) {
				String result = pText.replace(le.getKey(), le.getValue());
//				Debug("%d Hit and Replace: %s -> %s", debugOut, pText, result);
				isChange = true;
				return result;
			}
		}
		return pText;
	}

}
