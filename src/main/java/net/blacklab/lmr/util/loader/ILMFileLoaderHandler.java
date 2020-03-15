package net.blacklab.lmr.util.loader;

/**
 * LMFileLoaderの委譲インターフェース
 * 
 * @author firis-games
 *
 */
public interface ILMFileLoaderHandler {
	
	/**
	 * isLoader
	 * 
	 * loadHandler実行可否を判断する
	 * trueの場合にloadHandlerが実行される
	 * @param path
	 * @return
	 */
	default public boolean isLoader(String path) {
		return false;
	}
	
	/**
	 * loadHandler
	 * 
	 * Loaderの対象ファイルに処理を行う
	 * @param path
	 */
	default public void loadHandler(String path) {
	}
	
}
