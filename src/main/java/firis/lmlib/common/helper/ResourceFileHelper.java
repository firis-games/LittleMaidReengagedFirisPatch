package firis.lmlib.common.helper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 独自設定系のファイルの読み書き処理
 * @author firis-games
 *
 */
public class ResourceFileHelper {

	//ベースディレクトリパス
	protected static Path resourceDir = Paths.get("mods", "LittleMaidResource");
	
	/**
	 * ファイルへ書き出す
	 * @param name
	 * @param json
	 */
	public static boolean writeToFile(String fileName, String write) {
		
		boolean ret = false;
		
		try {
			//出力
			List<String> jsonList = new ArrayList<>();
			jsonList.add(write);
			
			//Path
			Path filePath = Paths.get(resourceDir.toString(), fileName);
			
			//ディレクトリがない場合は作成する
			if (!Files.isDirectory(resourceDir)) {
				Files.createDirectories(resourceDir);
			}
			
			//ファイルの上書き
			Files.write(filePath, jsonList, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
			ret = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * ファイルを読み込む
	 * @param fileName
	 * @return
	 */
	public static String readFromFile(String fileName) {
		
		String text = "";
		try {
			
			//Path
			Path filePath = Paths.get(resourceDir.toString(), fileName);
			
			//存在チェック
			if (Files.notExists(filePath)) {
				return text;
			}
			
			//テキストとして読込
			List<String> fileList = Files.readAllLines(filePath);
			text = String.join("", fileList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
	
	/**
	 * Jsonファイルを読み込む
	 */
	public static <T> T readFromJson(String fileName, Class<T> clazz) {
		
		T jsonObject = null;
		
		try {
			
			//jsonファイルの読み込み
			String json = readFromFile(fileName);
			if ("".equals(json)) return jsonObject;
			
			//Jsonへパース
			Gson gson = new Gson();
			jsonObject = (T) gson.fromJson(json, clazz);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
	/**
	 * Jsonファイルを書き込む
	 * @return
	 */
	public static <T> boolean writeToJson(String fileName, T jsonObject) {
		
		return ResourceFileHelper.writeToFile(fileName, jsonToString(jsonObject));
		
	}
	
	/**
	 * オブジェクトをjson文字列へ変換する
	 * @param jsonObject
	 * @return
	 */
	public static <T> String jsonToString(T jsonObject) {
		return new GsonBuilder()
				.serializeNulls()
				.setPrettyPrinting()
				.disableHtmlEscaping()
				.create().toJson(jsonObject);
	}
	
}
