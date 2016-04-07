package log.generation;

public class Alphabet {
	
	public String alphabetname;
	public String actCondition;
	public String relCondition;
	public String timeCondition;
	public String fullCondition;
	public String payLoadName;
	public int payLoadValue;
	public int payLoadILPValue;
	public int randomValue;
	public int leftvVlue;
	public int rightValue;
	public boolean isActivationTrue;
	public boolean isRetaiveTrue;
	public boolean isActivated;
	public boolean isILPTrue;
	public boolean logGenerate;

	
	public void leftvVlue(int leftvVlue) {
		this.leftvVlue = leftvVlue;
	}
	
	public void rightValue(int rightValue) {
		this.rightValue = rightValue;
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
	
	public void timeCondition(String string) {
		this.timeCondition = string;
	}
	
	public void payLoadName(String payLoadName) {
		this.timeCondition = payLoadName;
	}
	
	public void payLoadValue(int payLoadValue) {
		this.payLoadValue = payLoadValue;
	}
	
	public void payLoadILPValue(int payLoadILPValue) {
		this.payLoadILPValue = payLoadILPValue;
	}
	
	public void randomValue(int randomValue) {
		this.payLoadValue = randomValue;
	}

}
