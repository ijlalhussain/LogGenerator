package log.generation;

import java.util.LinkedList;

public class Alphabet {

	public String alphabetname;
	public String alphabetkey;
	public String actCondition;
	public String relCondition;
	public String timeCondition;
	public String fullCondition;
	public String payLoadName;
	public int payLoadValue;
	public int payLoadILPValue;
	public int randomValue;
	public int leftValue;
	public int rightValue;
	public boolean isActivationTrue;
	public boolean isRetaiveTrue;
	public boolean isActivated;
	public boolean isILPTrue;
	public boolean logGenerate;
	public String constrain;
	public int maxValue;
	public int minValue;
	public String secondAlphabet;
	public String secondAlphabetKey;
	public String ilpCondition;
	public String[] conditionlist;
	public String[] correlationlist;
	public boolean isSingle;

	public void isSingle(boolean isSingle) {
		this.isSingle = isSingle;
	}

	public void correlationlist(String[] correlationlist) {
		this.correlationlist = correlationlist;
	}

	public void conditionlist(String[] conditionlist) {
		this.conditionlist = conditionlist;
	}

	public void secondAlphabetKey(String secondAlphabetKey) {
		this.secondAlphabetKey = secondAlphabetKey;
	}

	public void ilpCondition(String ilpCondition) {
		this.ilpCondition = ilpCondition;
	}

	public void maxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public void minValue(int minValue) {
		this.minValue = minValue;
	}

	public void secondAlphabet(String secondAlphabet) {
		this.secondAlphabet = secondAlphabet;
	}

	public void constrain(String constrain) {
		this.constrain = constrain;
	}

	public void alphabetkey(String alphabetkey) {
		this.alphabetkey = alphabetkey;
	}

	public void leftValue(int leftValue) {
		this.leftValue = leftValue;
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
