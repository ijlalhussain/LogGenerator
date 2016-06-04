package log.generation;

import java.util.ArrayList;

public class NewTarget {
	
	public String targetKey;
	public String souceKey;
	public int targetIndex;
	public int souceIndex;
	public ArrayList<Integer> SouceList ;
	public ArrayList<Integer> SourceListIndex;
	public ArrayList<String> TargetList ;
	public ArrayList<Integer> TargetListIndex;
	public ArrayList<Integer> SelectedTargetList;
	
	public void targetKey(String targetKey) {
		this.targetKey = targetKey;
	}
	
	public void targetIndex(String souceKey) {
		this.souceKey = souceKey;
	}
	
	public void targetIndex(int targetIndex) {
		this.targetIndex = targetIndex;
	}
	
	
	public void souceIndex(int souceIndex) {
		this.souceIndex = souceIndex;
	}

	
	public void SouceList(ArrayList<Integer> SouceList) {
		this.SouceList = SouceList;
	}
	
	public void TargetList(ArrayList<String> TargetList) {
		this.TargetList = TargetList;
	}
	
	
	public void SourceListIndex(ArrayList<Integer> SourceListIndex) {
		this.SourceListIndex = SourceListIndex;
	}
	
	
	
	public void TargetListIndex(ArrayList<Integer> TargetListIndex) {
		this.TargetListIndex = TargetListIndex;
	}
	
	
	public void SelectedTargetList(ArrayList<Integer> SelectedTargetList) {
		this.SelectedTargetList = SelectedTargetList;
	}
 
}
