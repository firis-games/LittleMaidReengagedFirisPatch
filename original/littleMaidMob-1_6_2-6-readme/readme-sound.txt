Little Maid Mob 1.6.2 Rev1以降用サウンド設定の仕様


littleMaidMobではcfgファイルの記述を変更する事で、
各動作に関連付けられた音声を任意の音声で再生する事が可能です。
また、各種音声はテクスチャパック、メイド色毎に設定する事が可能です。
このテキストではcfgファイルで設定される音声設定の記述方法について説明します。

1.6.2からリソースの配置場所が変更になっているため注意して下さい。


使用可能音声形式
	クライアント1.6.2現在、使用可能なフォーマットは以下の通りです。
		.ogg	Ogg.Vorbis

		※確認済みのフォーマット・コーデックを記載しています。


音声定義変数
	cfgファイルの前半に有る「#」で始まる文字列は、各変数の機能について記述されています。
	「se_」で始まる変数には各種動作に関連付けられた音声設定を記述する事が出来ます。

	

音声設定文字列
	「se_」に記述できる音声の設定方法は以下の通りです。

	・例1
		se_living_daytime=mob.ghast.moan
			この記述の場合、%AppData%/.mincraft/assets/sound/mob/ghastに含まれる、
			moan1.ogg～moan7.oggがランダムで再生されます。

	・例2
		se_living_daytime=littleMaidMob.live_d
			この記述の場合、%AppData%/.mincraft/assets/sound/littleMaidMobに含まれる、
			live_d?.oggがランダムで再生されます。
			（?は1～9の数字）

	・例3
		se_living_daytime=mob.ghast.moan
		se_living_night=^
			この記述の場合、se_living_daytimeについては例1と同様になりますが、
			se_living_nightの内容もse_living_daytimeと同様になります。
			「^」を記述することにより内部的に優先順位の高い設定値と同じ内容に設定できます。



テクスチャ指定詞
	上記の音声設定文字列を「,」で区切り、テクスチャ指定詞を加えた指定を記述する事により、
	テクスチャパック、メイド色に応じた音声を定義する事が可能です。

	・記述方法
		テクスチャパック名;メイド色;音声	：区切り文字は「;」です。
	

	・テクスチャパック名
		ロードされているテクスチャパックの名称となります。
		正確にはテクスチャの含まれているフォルダ名称の区切り文字を
		「.」に変換したものになります。
		値を省略した場合には全てのテクスチャパックに適用されます。
			ディレクトリ			テクスチャパック名
			/mob/littleMaid/ALTERNATIVE/	:ALTERNATIVE
			/mob/littleMaid/okota/Hituji/	:okota.Hituji

	・メイド色
		0～15の１０進数になります。
		各色については対応するテクスチャを参照してください。
		「-1」を設定又は省略した場合はテクスチャパック内の全ての色に適用されます。


	・音声
		音声設定文字列で説明した物と同じ記述です。


	・例
		se_living_daytime=mob.ghast.moan,okota.Hituji;-1;littleMaid.live_d,;3;littleMaid.livealt_d
			デフォルトの音声	：mob.ghast.moan
			okota.Hitujiパック	：littleMaid.live_d
			全てのパックのカラー３	：littleMaid.livealt_d

	・優先順位
		テクスチャパック＋色指定 ＞ テクスチャパック＋全色指定 ＞ 色指定 ＞ デフォルト
		上記の順番で値を参照します、複雑な設定をする場合は注意してください。


サウンドパック
	b1.8.1-4からサウンドパックに対応しています。
	また、1.6.2からリソースの配置場所が変更になっているため注意して下さい。
	「%AppData%/.mincraft/assets/sound/littleMaidMob/」に含まれる
	設定ファイルを読み込んで音声を設定することができます。

	設定ファイルの記述方法は通常のcfgファイルのものと同様ですが、
	幾つかルールが追加されています。


	・記述方法
		メイド色;音声				：区切り文字は「;」です。
		音声					：メイド色指定は省略可です。

	・テクスチャパック名
		設定ファイル名が設定されるテクスチャパックの名称となります。
			テクスチャパック名		サウンドパック名
			ALTERNATIVE			:ALTERNATIVE.cfg
			okota.Hituji			:okota.Hituji.cfg

	・特殊設定ファイル
		ファイル名称が「littleMaidMob.cfg」のものがある場合、
		コレの内容が通常のcfgファイルの音声設定の代わりに読み込まれます。

	・優先順位
		littleMaidMob.cfgがある場合、mod_littleMaidMob.cfgの音声設定の代わりに適用されます。
		この後、サウンドパックの設定が読み込まれ、それぞれの設定値を上書きします。

	上記以外はcfgファイルのものと同様です。



説明
	・サウンドパック用cfg
		se_hurt			：ダメージボイスです。
		se_hurt_snow		：雪玉に当たった時のボイスです。
		se_hurt_fire		：炎によるダメージを受けた時のボイスです。
		se_hurt_guard		：攻撃をガード出来た時のボイスです。
		se_hurt_fall		：落下ダメージを受けた時のボイスです。
		se_hurt_nodamege	：攻撃を受けてもダメージが無かった時のボイスです。
		se_death		：死亡ボイスです。
		se_attack		：攻撃ボイスです。
		se_attack_bloodsuck	：血に飢えている時の攻撃ボイスです。
		se_shoot		：溜を必要とするものの射撃ボイスです。
		se_shoot_burst		：溜を必要としないものの射撃ボイスです。
		se_sighting		：射撃武器を構えた時のボイスです。
		se_laughter		：Bloodsucker系の子が敵を倒した時のボイスです。
		se_eatSugar		：砂糖を摂取するときのボイスです。
		se_eatSugar_MaxPower	：体力が最大になるまで砂糖を食べたときのボイスです。
		se_getCake		：契約時のボイスです。
		se_Recontract		：再契約時のボイスです。
		se_addFuel		：竈に燃料を追加した時のボイスです。
		se_cookingStart		：竈に材料を投入した時のボイスです。
		se_cookingOver		：竈から完成品を取り出した時のボイスです。
		se_healing		：主を回復した時のボイスです。
		se_healing_potion	：ポーション使用時のボイスです。
		se_installation		：松明を設置した時のボイスです。
		se_collect_snow		：雪を集めるときのボイスです。

		se_findTarget_N		：敵性体を発見した時の通常ボイスです。
		se_findTarget_B		：Bloodsucker系の子が敵を発見した時のボイスです。
		se_findTarget_I		：アイテムを発見した時のボイスです。
		se_findTarget_D		：暗がりを発見した時のボイスです。
		se_TNT_D		：TNT-D発動時のボイスです。

		se_living_daytime	：通常の啼声です。
		se_living_morning	：時計を持っている時の朝の啼声です。
		se_living_night		：時計を持っている時の夜の啼声です。
		se_living_whine		：弱っている時の啼声です。
		se_living_rain		：雨が降っている時の啼き声です。時間による変化よりも優先順位が高いです。
		se_living_snow		：雪が降っている時の啼き声です。時間による変化よりも優先順位が高いです。
		se_living_cold		：寒いバイオームにいる時の啼き声です。
		se_living_hot		：熱いバイオームにいる時の啼き声です。
		se_goodmorning		：時計を持っている時のおはようの挨拶です。
		se_goodnight		：時計を持っている時のお休みの挨拶です。

		LivingVoiceRate		：普段の啼声の発生率を設定します。（現在は未使用）




	・mod_littleMaidMob.cfg
		LivingVoiceRate		：普段の啼声の発生率を設定します。1.0=100%、0.5=50%、0.0=0%



注意
	・今後仕様は変更される恐れがあります。



履歴
	20130829.1	クライアント変更によるリソース位置の記述を変更。


	20120621.1	追加分の音声説明を記述
	20111125.1	追加分の音声説明を記述
	20111104.1	追加分の音声説明を記述
	20111006.1	サウンドパックの説明を追加
	20110905.1	設定パラメーターの説明を追加
	20110817.1	音声のテクスチャパックへの対応