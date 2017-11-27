package net.blacklab.lib.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("ALL")
public class ConfigList {
	private final List<ConfigKey> list;
	private final Map<String, String> commentMap;
	
	/**
	 * Configファイルの設定内容の一覧を格納するためのConfigListの新しいインスタンスを生成します。
	 * これを行わずにConfigListを操作するとNullPointerExceptionが発生します。
	 * インスタンスは通常@Modアノテーションのついたクラスにstaticフィールドとして用意してください。
	 */
	public ConfigList(){
		list = new ArrayList<ConfigKey>();
		commentMap = new HashMap<String, String>();
	}
	
	//get
	/**
	 * 指定したKeyに合致するConfigKeyを返します。
	 * @param par1 Key
	 * @exception IllegalStateException 指定したKeyが見つからない場合にスローされます。
	 **/
	public ConfigKey getConfigByKey(String par1){
		if(!isExistKey(par1)) throw new IllegalStateException("Requested key is not found");
		for(ConfigKey key : list){
			if(key.getKey().equals(par1)) return key;
		}
		throw new IllegalStateException("Requested key is not found");
	}

	/**
	 * 指定したKeyがConfigList内に存在するか確認します。存在していればtrueを返します。
	 * 引数がnullの場合、falseを返します。
	 * @param par1 Key
	 */
	public boolean isExistKey(String par1){
		if(par1==null) return false;
		for(ConfigKey key : list){
			if(key.getKey().equals(par1)) return true;
		}
		return false;
	}
	
	private int indexOf(String par1){
		int i = 0;
		for(ConfigKey key:list){
			if(key.getKey().equals(par1)) return i;
			i++;
		}
		return -1;
	}

	//add
	/**
	 * ConfigKeyをConfigListに追加します。
	 * @param par1 Key
	 * @exception IllegalStateException Keyがすでに存在する場合にスローされます。
	 * **/
	public void putConfig(ConfigKey par1){
		if(isExistKey(par1.getKey())) remove(par1.getKey());
		list.add(par1);
	}

	/**
	 * 文字列の値を持つConfigKeyをConfigListに追加します。
	 * @param par1 Key
	 * @param par2 設定される値
	 * @exception IllegalStateException Keyがすでに存在する場合にスローされます。
	 * **/
	public void putString(String par1,String par2){
		putConfig(new ConfigKey(par1,par2));
	}
	
	/**
	 * 整数の値を持つConfigKeyをConfigListに追加します。
	 * @param par1 key
	 * @param par2 設定される値
	 * @exception IllegalStateException Keyがすでに存在する場合にスローされます。
	 */
	public void putInt(String par1,int par2){
		putConfig(new ConfigKey(par1,String.valueOf(par2)));
	}

	/**
	 * 実数の値を持つConfigKeyをConfigListに追加します。
	 * @param par1 key
	 * @param par2 設定される値
	 * @exception IllegalStateException Keyがすでに存在する場合にスローされます。
	 */
	public void putFloat(String par1,float par2){
		putConfig(new ConfigKey(par1,String.valueOf(par2)));
	}

	/**
	 * 真偽の値を持つConfigKeyをConfigListに追加します。
	 * @param par1 key
	 * @param par2 設定される値
	 * @exception IllegalStateException Keyがすでに存在する場合にスローされます。
	 */
	public void putBoolean(String par1,boolean par2){
		putConfig(new ConfigKey(par1,String.valueOf(par2)));
	}
	
	/**
	 * 指定したKeyに合致するConfigKeyを参照しStringとして値を返します。
	 * ConfigKeyが存在しない場合、該当するKeyに第二引数の内容を設定して第二引数の内容をそのまま返します。
	 * @param par1 Key
	 * @param defaultvalue デフォルトで返る値
	 * @exception IllegalStateException 指定したKeyが見つからない場合にスローされます。
	 **/
	public String getString(String par1, String defaultvalue){
		if(isExistKey(par1)){
			return getConfigByKey(par1).getValue();
		}
		putString(par1,defaultvalue);
		return defaultvalue;
	}
	
	private void remove(String par1){
		if(isExistKey(par1)) list.remove(indexOf(par1));
	}
	
	/**
	 * 指定したKeyに合致するConfigKeyを参照しintとして値を返します。
	 * ConfigKeyが存在しない場合、該当するKeyに第二引数の内容を設定して第二引数の内容をそのまま返します。
	 * @param par1 Key
	 * @param defaultvalue デフォルトで返る値
	 * @throws NumberFormatException ConfigKeyの取得ができたが値が整数値でない場合にスローされます。
	 * @exception IllegalStateException 指定したKeyが見つからない場合にスローされます。
	 **/
	public int getInt(String par1, int defaultvalue) throws NumberFormatException{
		System.out.println("SSS="+par1);
		if(isExistKey(par1)){
			return Integer.parseInt(getConfigByKey(par1).getValue());
		}
		putInt(par1,defaultvalue);
		return defaultvalue;
	}
	
	/**
	 * 指定したKeyに合致するConfigKeyを参照しfloatとして値を返します。
	 * ConfigKeyが存在しない場合、該当するKeyに第二引数の内容を設定して第二引数の内容をそのまま返します。
	 * @param par1 Key
	 * @param defaultvalue デフォルトで返る値
	 * @throws NumberFormatException ConfigKeyの取得ができたが値が実数値でない場合にスローされます。
	 * @exception IllegalStateException 指定したKeyが見つからない場合にスローされます。
	 **/
	public float getFloat(String par1, float defaultvalue) throws NumberFormatException{
		if(isExistKey(par1)){
			return Float.parseFloat(getConfigByKey(par1).getValue());
		}
		putFloat(par1,defaultvalue);
		return defaultvalue;
	}
	
	/**
	 * 指定したKeyに合致するConfigKeyを参照しbooleanとして値を返します。
	 * ConfigKeyが存在しない場合、該当するKeyに第二引数の内容を設定して第二引数の内容をそのまま返します。
	 * @param par1 Key
	 * @param defaultvalue デフォルトで返る値
	 * @exception IllegalStateException 指定したKeyが見つからない場合にスローされます。
	 **/
	public boolean getBoolean(String par1, boolean defaultvalue){
		if(isExistKey(par1)){
			return Boolean.parseBoolean(getConfigByKey(par1).getValue());
		}
		putBoolean(par1,defaultvalue);
		return defaultvalue;
	}
	
	/**
	 * 指定したKeyにコメントを付加します．コメントはcfgファイルに出力されます．
	 * @param key Key
	 * @param comment 設定するコメント
	 */
	public void setComment(String key, String comment) {
		commentMap.put(key, comment);
	}
	
	/**
	 * Configファイルを読み出します。
	 * "(第一引数で指定した名前).cfg"の情報をConfigListに格納します。
	 * このメソッドはPreInitializeイベント内で呼び出される必要があります。
	 * configファイル内のkeyと設定値の区切り文字は"="です。
	 * @param configFileName コンフィグファイルの名前(.cfgを除いて記述します)
	 * @param event EventHandlerに渡されるFMLPreInitializationEvent
	 * @throws IOException  何らかの理由でconfigのロードに失敗した場合にスローされます。
	 */
	public void loadConfig(String configFileName,FMLPreInitializationEvent event) throws IOException{
		File file = new File(event.getModConfigurationDirectory(), configFileName+".cfg");
		loadConfig(file);
	}

	/**
	 * Configファイルを読み出します。
	 * このメソッドではファイル名を指定せずFMLPreInitializationEvent#getSuggestedConfigurationFile()の指すファイルを使用します。
	 * このメソッドはPreInitializeイベント内で呼び出される必要があります。
	 * configファイル内のkeyと設定値の区切り文字は"="です。
	 * @param event EventHandlerに渡されるFMLPreInitializationEvent
	 * @throws IOException  何らかの理由でconfigのロードに失敗した場合にスローされます。
	 */
	public void loadConfig(FMLPreInitializationEvent event) throws IOException{
		loadConfig(event.getSuggestedConfigurationFile());
	}
	
	/**
	 * Configファイルを読み出します。
	 * このメソッドではFileを直接指定します。
	 * configファイル内のkeyと設定値の区切り文字は"="です。
	 * @param file Fileオブジェクト
	 * @throws IOException 何らかの理由でconfigのロードに失敗した場合にスローされます。
	 */
	public void loadConfig(File file) throws IOException{
		if(file.exists()&&file.canRead()){
			for(String line:Files.readAllLines(file.toPath(), Charset.forName("UTF-8"))){
				try{
					if(!line.contains("=") || line.startsWith("#")) continue;
					String[] sp = line.split("=",2);
					putString(sp[0],sp[1]);
				}catch(NullPointerException e){
					
				}
			}
		}
	}
	
	/**
	 * Configファイルを書き出します。
	 * "(第一引数で指定した名前).cfg"の情報をConfigListに格納します。
	 * このメソッドはPreInitializeイベント内で呼び出される必要があります。
	 * configファイル内のkeyと設定値の区切り文字は"="です。
	 * @param configFileName コンフィグファイルの名前(.cfgを除いて記述します)
	 * @param event EventHandlerに渡されるFMLPreInitializationEvent
	 * @throws IOException  何らかの理由でconfigのセーブに失敗した場合にスローされます。
	 */
	public void saveConfig(String configFileName,FMLPreInitializationEvent event) throws IOException{
		File file = new File(event.getModConfigurationDirectory(), configFileName+".cfg");
		saveConfig(file);
	}
	
	/**
	 * Configファイルを書き出します。
	 * このメソッドではファイル名を指定せずFMLPreInitializationEvent#getSuggestedConfigurationFile()の指すファイルを使用します。
	 * このメソッドはPreInitializeイベント内で呼び出される必要があります。
	 * configファイル内のkeyと設定値の区切り文字は"="です。
	 * @param event EventHandlerに渡されるFMLPreInitializationEvent
	 * @throws IOException  何らかの理由でconfigのセーブに失敗した場合にスローされます。
	 */
	public void saveConfig(FMLPreInitializationEvent event) throws IOException{
		saveConfig(event.getSuggestedConfigurationFile());
	}

	/**
	 * Configファイルを書き出します。このメソッドではFileを直接指定します。
	 * @param file Fileオブジェクト
	 * @throws IOException 何らかの理由でconfigのセーブに失敗した場合にスローされます。
	 */
	public void saveConfig(File file) throws IOException{
		List<CharSequence> temp = new ArrayList<CharSequence>();
		for(ConfigKey k:list){
			if(commentMap.containsKey(k.getKey())) {
				temp.add("#"+commentMap.get(k.getKey()));
			}
			temp.add(k.getKey()+"="+k.getValue());
		}
		Files.write(file.toPath(), temp, Charset.forName("UTF-8"));
	}
}
