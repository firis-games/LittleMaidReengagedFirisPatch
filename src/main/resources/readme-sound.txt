-------------------------------------------------------------------------------------------------------
Original Text: https://github.com/dha-lo-jd/MMMLib_dev_forge/blob/master/littleMaidMob/readme-sound.txt

This document is remixed for LMMNX.
-------------------------------------------------------------------------------------------------------
LittleMaidMobNX NX4 4.3.**以降用サウンド設定の仕様

◇サウンドパック機能とは？

LMMNXでは，サウンドパック機能に対応しています．
サウンドパックをmodsディレクトリ内に入れておくと，サウンドパック内のcfgファイルを読取り，
リトルメイドの音声をサウンドパックに収録されている音声に置き換えることができます．

cfgファイルとogg形式のサウンドが共にmodsディレクトリの中に存在する時，LMMNXはサウンドパックを認識します．
cfg内の記述を元に，指定されたoggファイルを読み込んでリトルメイドの音声として設定します．
cfgとoggの各ファイルはzipに入れたままでも読取ります．


cfgファイルの書式は以下のとおりです．

◇ファイル名
	「littleMaidMob.cfg」である必要があります．(仕様変更予定)

◇文法

音声定義変数
	cfgファイルの前半に有る「#」で始まる文字列は，各変数の機能について記述されています. 
	「se_」で始まる変数には各種動作に関連付けられた音声設定を記述する事が出来ます．

	

音声設定文字列
	「se_」に記述できる音声の設定方法は以下の通りです．

	・例1
		se_living_daytime=mob.ghast.moan
			バニラのガストのmoanを再生します. (※バニラ音声の再生はB83時点未実装)

	・例2
		se_living_daytime=littleMaidMob.sample.live_d
			この記述の場合，mods/sample/live_d?.oggもしくは(何らかのzipファイル)/sample/live_d?.oggがランダムで再生されます．
			（?は1～9の数字）
			
			※標準では，最低でもoggファイルの直上のディレクトリ名まで一致していれば，
			　指定されているサウンドとみなして読み込みを行います．
			　この挙動は後のビルドでオプションとして変更できる機能を追加する予定ですが，
			　それまでの間にサウンドパック機能を使用する際は注意してください．

	・例3
		se_living_daytime=littleMaidMob.sample.live_d
		se_living_night=^
			この記述の場合，se_living_daytimeについては例2と同様になりますが，
			se_living_nightの内容もse_living_daytimeと同様になります．
			「^」を記述することにより内部的に優先順位の高い設定値と同じ内容に設定できます．



テクスチャ指定詞
	上記の音声設定文字列を「,」で区切り，テクスチャ指定詞を加えた指定を記述する事により，
	テクスチャパック，メイド色に応じた音声を定義する事が可能です．

	・記述方法
		テクスチャパック名;メイド色;音声	：区切り文字は「;」です．
	

	・テクスチャパック名
		ロードされているテクスチャの名称となります．
		値を省略した場合には全てのテクスチャに適用されます．


	・メイド色
		0～15の１０進数になります．
		各色については対応するテクスチャを参照してください．
		「-1」を設定又は省略した場合はテクスチャパック内の全ての色に適用されます．


	・音声
		音声設定文字列で説明した物と同じ記述です．


	・例
		se_living_daytime=mob.ghast.moan,okota.Hituji;-1;littleMaid.live_d,;3;littleMaid.livealt_d
			デフォルトの音声		：mob.ghast.moan
			okota.Hitujiテクスチャ	：littleMaid.live_d
			全てのパックのカラー３	：littleMaid.livealt_d

	・優先順位
		テクスチャパック＋色指定 ＞ テクスチャパック＋全色指定 ＞ 色指定 ＞ デフォルト
		上記の順番で値を参照します，複雑な設定をする場合は注意してください．



説明
	・サウンドパック用cfg
		se_hurt			：ダメージボイスです．
		se_hurt_snow		：雪玉に当たった時のボイスです．
		se_hurt_fire		：炎によるダメージを受けた時のボイスです．
		se_hurt_guard		：攻撃をガード出来た時のボイスです．
		se_hurt_fall		：落下ダメージを受けた時のボイスです．
		se_hurt_nodamege	：攻撃を受けてもダメージが無かった時のボイスです．
		se_death		：死亡ボイスです．
		se_attack		：攻撃ボイスです．
		se_attack_bloodsuck	：血に飢えている時の攻撃ボイスです．
		se_shoot		：溜を必要とするものの射撃ボイスです．
		se_shoot_burst		：溜を必要としないものの射撃ボイスです．
		se_sighting		：射撃武器を構えた時のボイスです．
		se_laughter		：Bloodsucker系の子が敵を倒した時のボイスです．
		se_eatSugar		：砂糖を摂取するときのボイスです．
		se_eatSugar_MaxPower	：体力が最大になるまで砂糖を食べたときのボイスです．
		se_getCake		：契約時のボイスです．
		se_Recontract		：再契約時のボイスです．
		se_addFuel		：竈に燃料を追加した時のボイスです．
		se_cookingStart		：竈に材料を投入した時のボイスです．
		se_cookingOver		：竈から完成品を取り出した時のボイスです．
		se_healing		：主を回復した時のボイスです．
		se_healing_potion	：ポーション使用時のボイスです．
		se_installation		：松明を設置した時のボイスです．
		se_farmer_farm		: 土を耕した時のボイスです．
		se_farmer_plant		: 種を植えた時のボイスです．
		se_farmer_harvest	: 作物を収穫した時のボイスです．
		se_collect_snow		：雪を集めるときのボイスです．

		se_findTarget_N		：敵性体を発見した時の通常ボイスです．
		se_findTarget_B		：Bloodsucker系の子が敵を発見した時のボイスです．
		se_findTarget_I		：アイテムを発見した時のボイスです．
		se_findTarget_D		：暗がりを発見した時のボイスです．
		se_TNT_D		：TNT-D発動時のボイスです．

		se_living_daytime	：通常の啼声です．
		se_living_morning	：時計を持っている時の朝の啼声です．
		se_living_night		：時計を持っている時の夜の啼声です．
		se_living_whine		：弱っている時の啼声です．
		se_living_rain		：雨が降っている時の啼き声です．時間による変化よりも優先順位が高いです．
		se_living_snow		：雪が降っている時の啼き声です．時間による変化よりも優先順位が高いです．
		se_living_cold		：寒いバイオームにいる時の啼き声です．
		se_living_hot		：熱いバイオームにいる時の啼き声です．
		se_goodmorning		：時計を持っている時のおはようの挨拶です．
		se_goodnight		：時計を持っている時のお休みの挨拶です．

		LivingVoiceRate		：普段の啼声の発生率を設定します．（現在は未使用）




	・mod_littleMaidMob.cfg
		LivingVoiceRate		：普段の啼声の発生率を設定します．1.0=100%，0.5=50%，0.0=0%



注意
	・今後仕様は変更される恐れがあります．



履歴
	20151214	NX改訂初版
	20120621	追加分の音声説明を記述


	20111125	追加分の音声説明を記述
	20111104	追加分の音声説明を記述
	20111006	サウンドパックの説明を追加
	20110905	設定パラメーターの説明を追加
	20110817	音声のテクスチャパックへの対応