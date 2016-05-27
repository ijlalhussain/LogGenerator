package log.generation;

import java.util.Map;

public class LogMapping {

	public int traceNo;
	public String constrains;
	public Map<String, String> mapAlphabet;
	public Map<Integer, Integer> mapIndex;
	public int maxValue;
	public int minValue;
	
	public void traceNo(int traceNo) {
		this.traceNo = traceNo;
	}
	
	public void constrains(String constrains) {
		this.constrains = constrains;
	}

	public void mapAlphabet(Map<String, String> mapAlphabet) {
		this.mapAlphabet = mapAlphabet;
	}

	public void secondAlphabetKey(Map<Integer, Integer> mapIndex) {
		this.mapIndex = mapIndex;
	}

	public void maxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public void minValue(int minValue) {
		this.minValue = minValue;
	}
}
