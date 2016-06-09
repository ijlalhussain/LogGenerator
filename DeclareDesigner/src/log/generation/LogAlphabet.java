package log.generation;

import java.util.ArrayList;
import java.util.Map;

public class LogAlphabet {

	public int traceNo;
	public int eventNo;
	public String ilpCondition;
	public int maxValue;
	public int minValue;
	public boolean isSelected;
	public ArrayList<Integer> eventLists;
	public ArrayList<Integer> dfsList;
	public boolean isFirstKey;

	public void traceNo(int traceNo) {
		this.traceNo = traceNo;
	}

	public void isFirstKey(boolean isFirstKey) {
		this.isFirstKey = isFirstKey;
	}

	public void eventLists(ArrayList<Integer> eventLists) {
		this.eventLists = eventLists;
	}

	public void dfsList(ArrayList<Integer> dfsList) {
		this.dfsList = dfsList;
	}

	public void isSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void eventNo(int eventNo) {
		this.eventNo = eventNo;
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
}
