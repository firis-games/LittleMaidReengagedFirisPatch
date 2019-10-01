package net.blacklab.lib.vevent;

public interface IVEvent {
	
	enum Result {
		PASS,
		SUCCESS,
		FAIL
	}
	
	Result getResult();
	void setResult(Result result);

}
