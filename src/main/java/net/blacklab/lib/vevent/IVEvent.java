package net.blacklab.lib.vevent;

public interface IVEvent {
	
	public static enum Result {
		PASS,
		SUCCESS,
		FAIL
	}
	
	public Result getResult();
	public void setResult(Result result);

}
