package log.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeMap;

public class BranchCombination {
	
	
	public static String getParentLetter(String string) {
		StringBuilder alphabetsBuilder = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			char ch = string.charAt(i);
			if (Character.isAlphabetic(ch)) {
				alphabetsBuilder.append(ch);
			} else if (Character.isDigit(ch)) {
				alphabetsBuilder.append(";");
			}
		}
		return alphabetsBuilder.toString().split(";")[0];
	}
	
	public static String getParentNumber(String string) {
	    	  StringBuilder alphabetsBuilder = new StringBuilder();
	        StringBuilder numbersBuilder = new StringBuilder();
	        StringBuilder symbolsBuilder = new StringBuilder();
	        for (int i = 0; i < string.length(); i++) {
	            char ch = string.charAt(i);
	            if (Character.isAlphabetic(ch)) {
	                alphabetsBuilder.append(ch);
	                if (numbersBuilder.length()>0)
	                numbersBuilder.append(";");
	            } else if (Character.isDigit(ch)) {
	                numbersBuilder.append(ch);
	                if (alphabetsBuilder.length()>0)
	                alphabetsBuilder.append(";");
	            } else {
	                symbolsBuilder.append(ch);
	            }
	        }	
		
		return alphabetsBuilder.toString().split(";")[0] +
				numbersBuilder.toString().split(";")[0];
	}
	
	
public static void separate(String string) {
        StringBuilder alphabetsBuilder = new StringBuilder();
        StringBuilder numbersBuilder = new StringBuilder();
        StringBuilder symbolsBuilder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (Character.isAlphabetic(ch)) {
                alphabetsBuilder.append(ch);
            } else if (Character.isDigit(ch)) {
                numbersBuilder.append(ch);
            } else {
                symbolsBuilder.append(ch);
            }
        }
        System.out.println("Alphabets in string: " + alphabetsBuilder.toString());
        System.out.println("Numbers in String: " + numbersBuilder.toString());
        System.out.println("Sysmbols in String: " + symbolsBuilder.toString()); 
    }
	
	public static String divertCondition(String s) {
		String r = "";
		if (s.contains(">")) {
			r = s.replace(">", "<");
		} else if (s.contains("<")) {
			r = s.replace("<", ">");
		} else if (s.contains("!")) {
			r = s.replace("!", "=");
		} else if (s.contains("<>")) {
			r = s.replace("<>", "=");
		} else {
			r = s.replace("=", "!");
		}
		return r;
	}
	
	
	public static void GetCombination(String branch, int repetition,
			ArrayList<String> list) {
		for (int id = 1; id <= repetition; id++) {
			Object[] array = setCombination(id, repetition, branch).toArray(new Object[repetition]);

			for (int i = 0; i < array.length; i++) {
				if (array[i] != null)
					list.add((String) array[i]);
				System.out.println(array[i]);
			}

		}
		System.out.println("Size:" + list.size());
	}

	 public static String addBranch(String a, int u){
         String s= "";
         for(int n= 0;u > 0;++n, u>>= 1)
                 if((u & 1) > 0) s+= a+ n + " ";
         return s;
 }
	
	    public static int countBranch(int u){
            int n;
            for(n= 0;u > 0;++n, u&= (u - 1));//Turn the last set bit to a 0
            return n;
    }


	  public static LinkedList<String> setCombination(int c, int n, String brch){
          LinkedList<String> s= new LinkedList<String>();
          for(int u= 0;u < 1 << n;u++)
                  if(countBranch(u) == c) s.push(addBranch(brch,u));
          Collections.sort(s);
          System.out.println("Combination:"+ s.size());
          return s;
  }
	
	
	
    public static void countAlphabets(ArrayList<String> list){
        Collections.sort(list);
        TreeMap<String,Integer> repeatItem = new TreeMap<String,Integer>();     

        for(int i=0;i<list.size() ;i++){
            if(repeatItem.containsKey(list.get(i))){
            	repeatItem.put(list.get(i), repeatItem.get(list.get(i))+1);
            }
            else{
            	repeatItem.put(list.get(i), 1);
            }
        }

        String result = null;
        while(!repeatItem.isEmpty()){
            String key = repeatItem.firstKey();
            Integer value = repeatItem.firstEntry().getValue();
            if(result==null){
            	result = key+"="+value;
            }
            else{
            	result = result + ";" + key+"="+value;
                
            }
            repeatItem.remove(key);
        }

        String[] res = result.split(";");
        list.clear();
        for (int i=0; i < res.length; i++ )
        	{
        	if (!res[i].isEmpty())
        	list.add(res[i]);}        
    }
}
