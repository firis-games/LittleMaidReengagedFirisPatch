2015/10/12

□開発環境構築について

    ソースコードはhttp://github.com/Verclene/LittleMaidMobNXに公開しています。
    以下は、NX1 Build 52以降での開発環境構築の手順になります。

◇ソースコードから構築する場合

1.  Minecraft Forge srcをセットアップ。
    ソースディレクトリはsrc/main/java、リソースディレクトリはsrc/main/resources。
    runDirは'eclipse'にする。
2.  GitHubからソースコードをダウンロードし、srcディレクトリをプロジェクトの直下に置く。
3.  EBLibのdev版をダウンロードし、プロジェクトのdependenciesに追加する。
4.  src/main/java/net/blacklab/lmmnx/util/LMMNX_DevMode.javaを開き、定数DEVMODEの値を変更する。
    ・IDEを使わず開発する場合 = DEVMODE_NO_IDE
    ・eclipseを使用して開発する場合 = DEVMODE_ECLIPSE
    ※eclipseからの実行時「作業ディレクトリ」は必ず"${workspace_loc:<PROJECTNAME>/eclipse}"としてください
    ※ForgeGradleでビルドするときは必ず「NOT_IN_DEV」に戻してください。
    ※IDEAはサポートしていません
5.  メイドの機能モードやマルチモデルの開発を別プロジェクトで行う場合、そのプロジェクト名を配列INCLUDEPROJECTに追加する。
    ・例："Beverly18"で開発しているマルチモデルを読み込む　⇒　INCLUDEPROJECT = new String[]{"Beverly18"};
6.  デバッグメッセージを出力させる場合、定数DEBUG_PRINT_SWITCHの値をtrueにする。