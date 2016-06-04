package log.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TraceAlphabet {

	public int traceNo;
	public int eventNo;
	public String selectedSource;
	public String selectedTarget;
	public String constrain;
	public int sourceIndex;
	public int targetIndex;
	public String alphabetKey;
	public int maxValue;
	public int minValue;
	public boolean isFirstKey;
	public boolean isMapped;
	public ArrayList<String> sourceList;
	public ArrayList<String> targetList;
	public ArrayList<Integer> targetListIndex;
	public ArrayList<String> targetListKey;
	public ArrayList<Integer> targetSelectedIndex;
	public ArrayList<Integer> ilpList;
	public ArrayList<Integer> ilpSelectedList;
	public Map<String, Map<String, ArrayList<Integer>>> targetedListwithIndex ;
	
	
	
	public void targetedListwithIndex(Map<String, Map<String, ArrayList<Integer>>>  targetedListwithIndex) {
		this.targetedListwithIndex = targetedListwithIndex;
	}
	
	public void ilpList(ArrayList<Integer> ilpList) {
		this.ilpList = ilpList;
	}
	
	public void ilpSelectedList(ArrayList<Integer> ilpSelectedList) {
		this.ilpSelectedList = ilpSelectedList;
	}
	public void targetListIndex(ArrayList<Integer> targetListIndex) {
		this.targetListIndex = targetListIndex;
	}
	
	public void targetSelectedIndex(ArrayList<Integer> targetSelectedIndex) {
		this.targetSelectedIndex = targetSelectedIndex;
	}
	
	
	
	public void targetListKey(ArrayList<String> targetListKey) {
		this.targetListKey = targetListKey;
	}
	
	
	public void traceNo(int traceNo) {
		this.traceNo = traceNo;
	}

	public void eventNo(int eventNo) {
		this.eventNo = eventNo;
	}
	
	public void selectedSource(String selectedSource) {
		this.selectedSource = selectedSource;
	}


	public void constrain(String constrain) {
		this.constrain = constrain;
	}

	public void selectedTarget(String selectedTarget) {
		this.selectedTarget = selectedTarget;
	}

	public void sourceIndex(int sourceIndex) {
		this.sourceIndex = sourceIndex;
	}

	public void targetIndex(int targetIndex) {
		this.targetIndex = targetIndex;
	}

	public void alphabetKey(String alphabetKey) {
		this.alphabetKey = alphabetKey;
	}

	public void maxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public void minValue(int minValue) {
		this.minValue = minValue;
	}

	public void isFirstKey(boolean isFirstKey) {
		this.isFirstKey = isFirstKey;
	}

	public void isMapped(boolean isMapped) {
		this.isMapped = isMapped;
	}

	
	
	public void sourceList(ArrayList<String> sourceList) {
		this.sourceList = sourceList;
	}

	public void targetList(ArrayList<String> targetList) {
		this.targetList = targetList;
	}
	
	
	
	

}
