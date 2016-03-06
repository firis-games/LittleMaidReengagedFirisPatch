package net.blacklab.lmr.entity.ai;

public interface IEntityAI {
	
	//実行可能判定
	public void setEnable(boolean pFlag);
	public boolean getEnable();
	/**
	 * モードチェンジ実行時に設定される動作状態。
	 */
//	public void setDefaultEnable();

}
