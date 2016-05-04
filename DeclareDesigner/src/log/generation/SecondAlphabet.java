package log.generation;

public class SecondAlphabet {

	public String alphabetname;
	public String alphabetkey;
	public String actCondition;
	public String relCondition;
	public String fullCondition;
	public String payLoadName;
	public int payLoadValue;
	public boolean isActivationTrue;
	public boolean isRetaiveTrue;
	public boolean isActivated;
	public boolean isILPTrue;
	public boolean logGenerate;
	public String constrain;
	public int maxValue;
	public int minValue;
	
	public void maxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public void minValue(int minValue) {
		this.minValue = minValue;
	}

	
	public void constrain(String constrain) {
		this.constrain = constrain;
	}

	public void alphabetkey(String alphabetkey) {
		this.alphabetkey = alphabetkey;
	}

	
	public void fullCondition(String fullCondition) {
		this.fullCondition = fullCondition;
	}

	public void isActivationTrue(boolean isActivationTrue) {
		this.isActivationTrue = isActivationTrue;
	}

	public void isRetaiveTrue(boolean isRetaiveTrue) {
		this.isRetaiveTrue = isRetaiveTrue;
	}

	public void isActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

	public void isILPTrue(boolean isILPTrue) {
		this.isILPTrue = isILPTrue;
	}

	public void logGenerate(boolean logGenerate) {
		this.logGenerate = logGenerate;
	}

	public void alphabetname(String alphabetname) {
		this.alphabetname = alphabetname;
	}

	public void actCondition(String actCondition) {
		this.actCondition = actCondition;
	}

	public void relCondition(String relCondition) {
		this.relCondition = relCondition;
	}

	
	public void payLoadValue(int payLoadValue) {
		this.payLoadValue = payLoadValue;
	}

	
	public void randomValue(int randomValue) {
		this.payLoadValue = randomValue;
	}

}
