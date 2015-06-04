package net.blacklab.lib;

public class ConfigKey {
	private String key;
	private String value;
	
	/**
	 * Configの各設定項目を格納するConfigKeyの新しいインスタンスを生成します。Key,設定値共にString型で格納されます。
	 * @param par1 key
	 * @param par2 設定する値
	 */
	public ConfigKey(String par1, String par2){
		key = par1;
		value = par2;
	}

	/**
	 * key,value共に未定義でConfigKeyのインスタンスを生成します。
	 */
	public ConfigKey(){
		this("","");
	}
	
	/**
	 * Keyを取得します。nullであれば、空の文字列を返します。
	 * @return Key
	 */
	public String getKey(){
		if(key!=null) return key; else return "";
	}
	
	/**
	 * Keyを変更します。
	 * @param par1 変更後のKey
	 */
	public ConfigKey setKey(String par1){
		key = par1;
		return this;
	}
	
	//get
	/**
	 * 設定値を返します。
	 * @return 設定値
	 */
	public String getValue(){
		return value;
	}
	
	/**
	 * 設定値を変更します。
	 * @param par1 変更後の設定値
	 */
	public ConfigKey setValue(String par1){
		value = par1;
		return this;
	}
}