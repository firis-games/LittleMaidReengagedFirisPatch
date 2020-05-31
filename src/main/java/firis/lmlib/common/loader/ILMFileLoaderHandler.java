package firis.lmlib.common.loader;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * LMFileLoaderの委譲インターフェース
 * 
 * @author firis-games
 *
 */
public interface ILMFileLoaderHandler {
	
	/**
	 * Loaderの事前準備
	 * キャッシュの読み込み等を行う
	 */
	default public void init() {
		
	}
	
	/**
	 * ファイル読み込み処理を行うか
	 * falseの場合は
	 * loadHandler系処理が行われない
	 * 
	 */
	default public boolean isFileLoad() {
		return true;
	}
	
	/**
	 * isLoader
	 * 
	 * loadHandler実行可否を判断する
	 * trueの場合にloadHandlerが実行される
	 * @param path
	 * @param fileName
	 * @return
	 */
	default public boolean isLoadHandler(String path, Path filePath) {
		return false;
	}
	
	/**
	 * loadHandler
	 * 
	 * Loaderの対象ファイルに処理を行う
	 * @param path
	 * @param fileName
	 */
	default public void loadHandler(String path, Path filePath, InputStream inputstream) {
	}
	
	/**
	 * postLoadHandler
	 * すべてのロード処理終了後に呼び出される
	 */
	default public void postLoadHandler() {
	}
	
}
