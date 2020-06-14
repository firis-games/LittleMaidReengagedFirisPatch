import firis.lmmm.builtin.model.ModelLittleMaid_SR2;

/**
 * サンプルマルチモデル定義
 *
 * ModelMultiBaseを継承したクラスをマルチモデルと判断する
 *
 * 通常のメイドさんの改良
 * 　基準メイドさんを改変する場合はModelLittleMaidBaseを継承する
 * 　すでに存在するメイドさんを改変する場合はModelLittleMaid_SR2などを継承する
 * 　本クラスはSR2モデルを継承している
 *
 * 命名規則（モデル）
 * 　ModelLittleMaid_xxxxxxxxをクラス名とすること
 * 　xxxxxxxxがモデル名となりテクスチャとのリンクに利用される
 * 　本クラスの場合はSampleMaidModelがモデル名となる
 *
 * 命名規則（テクスチャ）
 * 　assets.minecraft.textures.entity.littleMaidが基準フォルダとなる
 * 　[パッケージ].[テクスチャ名]_[モデル名]形式でフォルダを作成する
 * 　　※[パッケージ]は省略可能
 * 　上記フォルダ内にメイドさんテクスチャを格納する
 * 　メイドさんテクスチャはxxxxxxxx_0.png～xxxxxxxx_f.pngまで
 * 　xxxxxxxxは任意の文字列となり末尾の英数字が16進数で16色の色情報を意味する
 *
 *
 * 本サンプルのテクスチャが格納されているフォルダは下記を意味している
 * 　Tutorial.DemoModel_SampleMaidModel
 *
 * 　Tutorial [パッケージ]
 * 　DemoModel [テクスチャ名]
 * 　SampleMaidModel [モデル名]
 *
 * 　[モデル名]はモデルクラスと同一の名前になるように設定すること
 * 　本サンプルの場合は[SampleMaidModel]がモデル名となる。
 *
 * 同一モデルで別テクスチャを使用する場合は
 * 別名称のパッケージまたはテクスチャ名のフォルダを用意すれば
 * モデルは同じでテクスチャは別のものを設定できる
 *
 * メイドさん選択画面でTutorial.DemoModel_SampleMaidModelが表示される
 *
 */
public class ModelLittleMaid_SampleMaidModel extends ModelLittleMaid_SR2 {

	public ModelLittleMaid_SampleMaidModel() {
		super();
	}
	public ModelLittleMaid_SampleMaidModel(float psize) {
		super(psize);
	}
	public ModelLittleMaid_SampleMaidModel(float psize, float pyoffset, int pTextureWidth, int pTextureHeight) {
		super(psize, pyoffset, pTextureWidth, pTextureHeight);
	}
}
