package log.generation;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;

public class Test {
	
public static	ArrayList<String> lst = new ArrayList<String>(); 
	
	public static void main(String[] args) {
        String[] testArr = {"A", "B", "A", "D", "A", "C"};
        
        lst.add("A");
        lst.add("A");
        lst.add("B");
        lst.add("A");
        lst.add("c");
        
        String output = countNumberOfChild(lst);
 
      String[] res = output.split(";");
      lst.clear();
      for (int i=0; i < res.length; i++ )
    	  lst.add(res[i]);
        for (int k=0; k< lst.size(); k++)
        {
        	System.out.println (k+" : "+ lst.get(k));
        }
        System.out.println(output);
    }


    public static String countNumberOfChild(ArrayList<String> list){
       
        Collections.sort(list);
        TreeMap<String,Integer> noOfOccurences = new TreeMap<String,Integer>();
        

        for(int i=0;i<list.size() ;i++){
            if(noOfOccurences.containsKey(list.get(i))){
                noOfOccurences.put(list.get(i), noOfOccurences.get(list.get(i))+1);
            }
            else{
                noOfOccurences.put(list.get(i), 1);
            }
        }

        String outputString = null;
        while(!noOfOccurences.isEmpty()){
            String key = noOfOccurences.firstKey();
            Integer value = noOfOccurences.firstEntry().getValue();
            if(outputString==null){
                outputString = key+"="+value;
            }
            else{
                outputString = outputString + ";" + key+"="+value;
                
            }
            noOfOccurences.remove(key);
        }

        return outputString;
    }

}
