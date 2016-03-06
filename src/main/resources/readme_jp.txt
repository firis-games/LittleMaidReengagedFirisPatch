Little Maid Mob NX for Minecraft 1.8 - Verclene(elise_blacklab)

EMB4氏作LittleMaidMobXをベースとし、Minecraft1.8用に改造したものです。このテキストにもEMB4氏のパッケージに同梱されていたReadmeの内容を含みます。

このMODのチュートリアルはhttps://github.com/Verclene/LittleMaidMobNX/wikiにあります．

移植MOD:
	http://forum.minecraftuser.jp/viewtopic.php?f=13&t=176

	littleMaidMob-1_6_2-6
	MMMLib-1_6_2-6
	MMMLib-1.7.2-1

	※現在公開されているマルチモデルは、対応しているものと対応していないものがあります。

	Zabutonは1.8移植にあたり削除しました。必要であれば竹MODの座布団等で代用してくださいませ。

要求環境
	Java Runtime Environment 7またはそれ以降のバージョンを利用し、Minecraft1.8にて以下のMODを導入し正常に起動する環境
前提MOD
	Forge 1.8-Recommended 11.14.1.1334 またはそれ以降のバージョン
	EBLib EL1 Build 4 またはそれ以降のバージョン
	※このMODは、Forge 11.14.3.1450でビルドされています。

コンフィグ
	configディレクトリにある「littleMaidMobNX.cfg」で設定できます。

使用方法
	Forgeを導入し、EBLibと一緒にjarをmodsフォルダに入れる。

利用条件
	・readme_LMM1.6.2.txt に従って下さい。

注意
	・1.6.x以前のメイドさんは移行はできません。

サウンドパック導入方法
	サウンドパックは「サウンドがogg形式で構成されているもの」を読み込めます。ダウンロードしたパックを、zipのままmodsディレクトリに入れてください。
	一度に認識できるサウンドパックは1つのみです。先に認識された方を優先して使用します。
	※パック内に日本語を含む名前のファイルがある場合は、パックを解凍してください。

マルチモデル・テクスチャパック導入方法
	ダウンロードしたパックをzip(またはjar)のままmodsに入れてください。
	※対応している旧版マルチモデル・テクスチャパックはLittleMaidMob(1.6.2版)用とLittleMaidMobX(1.7.x版)用のパックです。
	※なお、LittleMaidMobNX(1.8版)用パックはLMMX用に準じているので、ほぼ同じ構造で制作することができます。
	※モデルが表示されても、Beverly等特殊なアニメーション処理を行っているモデルに関してアニメーションが異常になる場合、
	　LittleMaidMobNX用に再コンパイルする必要があります
	※αブレンド(半透明)テクスチャに対応しています。
	　使用する場合はlittleMaidMobNX.cfgの「isModelAlphaBlend」が「true」(デフォルト)になっていることを確認してください。

未実装機能
	本トリガー

更新履歴
	NX1以降は、GitHubのリリースページ( http://github.com/Verclene/LittleMaidMobNX/releases )をご確認ください。
	20150702	openbeta-0.5.2-mc18f1450
		Added Achievement translate
	20150702	openbeta-0.5.1-mc18f1450
		いくつかの変更の差し戻し
	20150701	openbeta-0.5-mc18f1450
		テクスチャセレクトGUI実装
		旧LMM(1.6.2版)テクスチャパック読み込み対応
		バージョンチェック追加
		リトルメイドの名前描画を修正
	20150627	openbeta-0.4.6-unstable-mc18f1450
		ビルド変更(Minecraft Forge:#1446 → #1450)
		旧LMM(1.6.2版)モデル読み込みに対応
		αブレンド(半透明)テクスチャに対応、Config追加
		実績追加
		サウンド調整のConfig追加
		デバッグのConfig変更
		サウンド挙動調整
	20150621	openbeta-0.4.1-unstable-mc18f1446
		防具表示の暫定実装
		Torcherの挙動調整
		自然スポーン時、メイドが透明化しているバグの修正
		火薬トランザム修正
		戦闘系AI調整
	20150614	openbeta-0.3-unstable-mc18f1446
		ビルドベース変更(#1334→#1446)
		サウンドパック復活
		マルチモデルセレクト暫定実装
		パケットシステム修正
	20150606	openbeta-0.2-unstable-forge1334
		Healer、Torcher、Freedomのチェスト収納復活
	20150602	openbeta-0.1-unstable-forge1334
		1.8用openbeta公開
	20150524	0.0.9.pre3
	20140117	0.0.7
		不具合対応 : 肩車状態で水に触れるとクラッシュする不具合修正
	20140116	0.0.6
		1.7.2 / 1.7.10 どちらでも動作するように対応
		不具合対応 : http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&start=180#p211805
		不具合対応 : http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&start=180#p211806
		不具合対応 : http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&start=180#p212038
		不具合暫定対策 : http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&start=160#p211172
		不具合暫定対策 : http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&start=160#p210319
		中国語テキスト追加
	20141210	0.0.5
		不具合対応
	20141207	0.0.4
		jar 形式に対応した
		マルチモデル/サウンドパックに対応した
	20141116	Test2
		トリガーセレクト画面でクラッシュする不具合修正
		インベントリの右下に特定のアイテムを入れると頭上に表示する機能の修正
	20141115	Test1
		1.6.2から移植。
		ZabutonMODの統合→openbeta-1.8-unstable-forge1334にて削除
		MMMLibの統合