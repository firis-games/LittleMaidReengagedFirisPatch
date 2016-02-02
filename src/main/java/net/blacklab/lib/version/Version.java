package net.blacklab.lib.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Version {
	
	public static class VersionData{
		public int code = -1;
		public String name;
		public String shownName;
		public VersionData(int i, String s){
			code = i;
			name = s;
			shownName = "undef";
		}
		
		public VersionData(int i, String v, String s) {
			code = i;
			name = v;
			shownName = s;
		}
		
		/**
		 * Returns 1 if [data] has newer version info than this object.
		 * Returns -1 if [data] has older version info than this object.
		 * Returns 0 if neither.
		 * @param data
		 */
		public int compareVersion(VersionData data) {
			int compared = 0;
			String[] newVersionSplit = data.name.split("\\.");
			String[] curVersionSplit = this.name.split("\\.");
			int elements = Math.min(newVersionSplit.length, curVersionSplit.length);
			for (int i = 0; i < elements; i++) {
				try {
					compared = Integer.compare(Integer.parseInt(newVersionSplit[i]), Integer.parseInt(curVersionSplit[i]));
					if (compared != 0) {
						return compared;
					}
				} catch (NumberFormatException exception) {
					System.err.println("Version check failed: Invalid number");
					break;
				}
			}
			return Integer.compare(data.code, this.code);
		}
	}
	
	/**
	 * 指定したURLにアクセスし，最新バージョン情報を取得します．タイムアウトは10秒です
	 * @param address チェック先URL
	 * @return 最新バージョンの情報が入ったVersionData
	 */
	public static VersionData getLatestVersion(String address) {
		return getLatestVersion(address, 10000);
	}
	
	/**
	 * 指定したURLにアクセスし，最新バージョン情報を取得します．タイムアウトをミリ秒で指定します．
	 * @param address チェック先URL
	 * @param timeout タイムアウト時間
	 * @return 最新バージョンの情報が入ったVersionData
	 */
	public static VersionData getLatestVersion(String address, int timeout){
		int latestcode = 0;
		String latestversions = "";
		String latestversionc = "";
		
		URL url;
		HttpURLConnection connection;
		BufferedReader reader;
		
		try{
			url = new URL(address);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setReadTimeout(timeout);
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String str;
			while((str = reader.readLine()) != null){
				if(str.startsWith("CODE=")){
					latestcode = Integer.valueOf(str.split("=")[1]);
				}
				if(str.startsWith("NAME=")){
					latestversions = str.split("=")[1];
				}
				if (str.startsWith("VERSION=")) {
					latestversionc = str.split("=")[1];
				}
			}
			reader.close();
			connection.disconnect();
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.printf("latest %s/%s/%s\n", latestcode, latestversionc, latestversions);
		return new VersionData(latestcode, latestversionc, latestversions);
	}
	
}
