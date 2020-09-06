これは書きかけの資料です。



ModelMultiBase

・マルチモデルについて
	MMM_TextureManagerで管理されるマルチモデルは、最低限MMM_ModelMultiBaseを継承し、
	一定の手続きに基づいて作成されていれば、対応MOD全てで表示を行うことができるようになっています。
	
・MMMLibのデフォルトでの動作
	以下の文字列を含むファイルを読み込み、その中に含まれるファイルを検索します。
		・MMMLib
		・littleMaidMob
	
	検索されたファイルを解析し、以下のディレクトリを検索しテクスチャとして認識します。
		・/mob/ModelMulti/
		・/mob/littleMaid/
	
	検索されたファイルとクライアントのJarファイルを検索し、以下の文字列を含むMMM_ModelMultiBase継承クラスをマルチモデルとして読み込みます。
		・ModelMulti_
		・ModelLittleMaid_
		※MMM_*のように先頭にMMM_とつける必要はありません。

	検索文字列は、mod独自に追加することができます。


ModelLittleMaidBase

・パーツの親子関係
	それぞれのパーツの親子関係は以下の通りになっています。

	+- mainFrame@
		|
		+- bipedTorso@
			|
			+- bipedNeck@
			|	|
			|	+- bipedHead
			|	|	|
			|	|	+- HeadTop
			|	|	|
			|	|	+- HeadMount
			|	|
			|	+- bipedRightArm
			|	|	|
			|	|	+- Arms[0]
			|	|
			|	+- biprdLeftArm
			|		|
			|		+- Arms[1]
			|
			+- bipedBody
			|
			+- bipedPelvic@
				|
				+- Skirt
				|
				+- bipedRightLeg
				|
				+- bipedLeftLeg


ModelSmartMovingBase(未実装)

・パーツの親子関係
	それぞれのパーツの親子関係は以下の通りになっています。
	+- bipedOuter@
		|
		+- bipedTorso@
			|
			+- bipedBreast@
			|	|
			|	+- bipedHead
			|	|	|
			|	|	+- bipedHeadwear
			|	|	|
			|	|	+- HeadTop
			|	|	|
			|	|	+- HeadMount
			|	|
			|	+- bipedRightShoulder@
			|	|	|
			|	|	+- bipedRightArm
			|	|		|
			|	|		+- Arms[0]
			|	|
			|	+- bipedLeftShoulder@
			|		|
			|		+- biprdLeftArm
			|			|
			|			+- Arms[1]
			|
			+- bipedBody
			|
			+- bipedPelvic@
				|
				+- bipedRightLeg
				|
				+- bipedLeftLeg



