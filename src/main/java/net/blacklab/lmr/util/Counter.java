package net.blacklab.lmr.util;



/**
 * カウンタ用。
 */
public class Counter {

	protected int fSetValue;
	protected int fMaxValue;
	protected int fDelayValue;
	protected int fCounter;


	public Counter() {
		this(25, 20, -10);
	}

	public Counter(int pSetValue, int pMaxValue, int pDelayValue) {
		fSetValue = pSetValue;
		fMaxValue = pMaxValue;
		fDelayValue = pDelayValue;
		fCounter = pDelayValue;
	}

	public void setValue(int pValue) {
		fCounter = pValue;
	}

	public int getValue() {
		return fCounter;
	}

	public void setEnable(boolean pFlag) {
		fCounter = pFlag ? (isEnable() ? fMaxValue : fSetValue) : fDelayValue;
	}

	public boolean isEnable() {
		return fCounter > 0;
	}

	public boolean isDelay() {
		return fCounter > fDelayValue;
	}

	public boolean isReady() {
		return fCounter >= fMaxValue;
	}

	public void onUpdate() {
		if (isDelay()) {
			fCounter--;
		}
	}

	public void updateClient(boolean pFlag) {
		if (pFlag) {
			fCounter = fMaxValue;
		} else if (fCounter > 0) {
			fCounter = 0;
		}
	}

}
