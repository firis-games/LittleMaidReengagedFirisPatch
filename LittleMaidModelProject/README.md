LittleMaidReengaged Firis's Patch  
マルチモデル作成環境構築手順
===

LittleMaidReengaged Firis's Patchを使ったメイドさんのマルチモデル作成環境の構築手順を簡単にまとめたものになります。


## 1.必要なファイルの取得
環境構築に必要なファイルをダウンロードして取得します

- [forge-1.12.2-14.23.5.2768-mdk.zip](https://files.minecraftforge.net/)
  - Forgeの開発環境に必要なものです。
  - リンク先サイトでMinecraft version 1.12.2 Recommended版 mdk 14.23.5.2768をダウンロードして下さい。
- [Eclipse 4.8 Photon](https://mergedoc.osdn.jp/)
  - Javaの開発を行うのに便利な統合開発環境です。
  - 予め日本語化され必要なものが内蔵されたPleiades All in Oneを利用します。
  - Eclipse 4.8 Photon Java Windows 64bit Full Editionをダウンロードして下さい。
- [メイドさんマルチモデル作成環境構築用ファイル](https://github.com/firis-games/LittleMaidReengagedFirisPatch/releases)
  - メイドさんのモデル開発に必要な設定ファイル・開発版Mod・サンプルモデルをまとめたものになります。
  - releasesにLittleMaidModelProject-8.1.6.141.fp.xxx.zipで公開していますのでダウンロードして下さい。

## 2.開発環境の準備（必要ファイルの準備）
1. forge-1.12.2-14.23.5.2768-mdk.zipを任意のフォルダ[LittleMaidModelProject]へ展開して下さい。
   - このフォルダがマルチモデルを管理するプロジェクトフォルダになります。
   - 以降この手順では任意のフォルダを[LittleMaidModelProject]と想定して説明を記載します。
1. [LittleMaidModelProject]フォルダ直下にあるsrcフォルダを削除して下さい。
   - mdkにはmod用のsampleが含まれているので削除します。
1. LittleMaidModelProject-xxx.fp.xxx.zipファイルの中身を[LittleMaidModelProject]の中へ上書きして下さい。
   - メイドさんのマルチモデルに必要なファイルが一式入っているので上書きして下さい。
1. pleiades-4.8.0-java-win-64bit-jre_20180923.zipを任意の場所へ展開して下さい。
   - 展開したフォルダのpleiades\eclipse\eclipse.exeが統合開発環境のEclipseになります。

## 3.開発環境の構築
1. Eclipseを起動します。（pleiades\eclipse\eclipse.exe）
1. [この選択をデフォルトとして使用し、今後この質問を表示しない]にチェックを入れ[起動]ボタンを押します。
1. ファイル -> インポートを選択します。
1. インポート画面でGradle -> 既存の Gradle プロジェクト を選択し[次へ]ボタンを押します。
1. [プロジェクト・ルート・ディレクトリー]で[LittleMaidModelProject]フォルダを選択し[完了]ボタンを押します。
1. 画面左下のウインドウのGradleタスクに選択した[LittleMaidModelProject]が表示されます。
1. Gradleタスクの[LittleMaidModelProject] -> forgegradle -> setupDecompWorkspaceを選択しダブルクリックします。
1. 構築処理が実行されます。Gradle実行に進捗が表示されます。初回だとかなり時間がかかります。
1. Gradle実行の最後にsetupDecompWorkspaceが表示されれば処理が完了です。
1. Gradleタスクの[LittleMaidModelProject] -> ide -> eclipseを選択しダブルクリックします。
1. Gradle実行の最後にeclipseが表示されれば処理が完了です。
1. 画面左上のウインドウのパッケージ・エクスプローラーの[LittleMaidModelProject]をクリックして選択状態にして下さい。
1. ファイル -> リフレッシュでプロジェクトのリフレッシュを実行して下さい。
1. ここまででエラーが出ていなければ開発環境の構築は完了です。

## 4.開発環境での実行
3.開発環境の構築までで作成した開発環境で準備ができました。あとは開発環境で動かしながら作ります。
1. 実行 -> デバッグの構成を選択します。
1. 左側の一覧の Javaアプリケーション を展開すると[LittleMaidModelProject]_Clientと[LittleMaidModelProject]_Serverがあります。
1. [LittleMaidModelProject]_Clientを選択し右下の[デバッグ]ボタンを押します。
1. Minecraftが起動するので動きを確認します。
1. メイドさんが導入されていれば問題ありません。
1. 一度デバッグの構成で設定したあとはEclipseの開発画面の左上にある虫マークのアイコンからMinecraftを起動することができます。

## 5.メイドモデルをzip化
開発環境でモデルやテクスチャを作ったあとにMinecraftで使用するためのzip化を行います。
1. [LittleMaidModelProject]フォルダ下のbuild.gradleを開き設定を変更します。
   - archivesBaseName = "SampleMaidModel"が出力するzip名になります。必要に応じて変更して下さい。
1. Gradleタスクの[LittleMaidModelProject] -> build -> buildを選択しダブルクリックします。
1. Gradle実行の最後にbuildが表示されれば処理が完了です。
1. [LittleMaidModelProject]フォルダ下のbuild/distributionsフォルダにメイドさんモデルのzipが生成されます。
1. zipファイルをMinecraftのmodsフォルダに導入すればメイドさんのマルチモデルを導入できます。
