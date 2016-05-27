package log.generation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
 


public class Comb{
		
        @SuppressWarnings("null")
		public static void main(String[] args){
        Map<Integer,Map<String, String>> mapAlphabet = null;
        
        for (int i=0; i <10; i++){
        	Map<String, String> myMap = new HashMap<String, String>();
        	myMap.put("A1", "C1");
        	mapAlphabet.put(i, myMap);
        }/*
        mapAlphabet.put(0, mapAlphabet.put(arg0, arg1)("AO", "CO"));
        	Map<String, String> myMap = new HashMap<String, String>();
        	myMap.put("AO", "CO");
        	myMap.put("AOA1", "A1CO");
        	myMap.put("AOd", "COxxx");
        	myMap.put("AOd", "COd");
        	*/
        	System.out.println(mapAlphabet);
        	
        	
        	String[] arraydd = { "BO", "A0A1", "B1B2", "BOB1", "A0A1" };
            countStringOccurences(arraydd);
            getalldata(arraydd,"BOB");
        	
        	ArrayList<String> ab = new ArrayList<String>(); 
        	LinkedHashMap<String, String[]> ab1 = new LinkedHashMap<String, String[]>();
        	for (int id=1; id <= 4; id++)
        	{
        		Object[] array = comb(id,4).toArray(new Object[4]);
     		   // print the array
     	   for (int i = 0; i < array.length; i++) {
     	  ab.add((String) array[i]);
     		  // System.out.println(array[i]);
     	 if (array[i] != null)	 
     	 {
     		 ab1.put(array[i].toString().trim().replaceAll(" ", "") , array[i].toString().trim().split(" "));
     	  System.out.println(array[i].toString().trim().replaceAll(" ", ""));
     	 }}
	
        	}
        	
        	
        	 for (Entry<String, String[]> activity : ab1.entrySet()) {
        		 String k = activity.getKey();
        		 String[] dd2 =activity.getValue();
        		 System.out.print("key,val: ");
        		 String cond = "";
        		 
        		 for (int jj=0; jj< dd2.length; jj++)
        		 {
        			 if (jj==0){
        				 cond =dd2[jj].toString();
        			 } else {
        				 cond = cond + " ::: " + dd2[jj].toString();
        			 }
        		 }
        		 System.out.println("key :"+ k + "  cond: " +cond);
        		 }
        	
//        	for(Object objname:ab1.keySet()) {
//        		   System.out.println("ob: "+objname);
//        		   System.out.println("cond: "+ab1.get(objname));
//        		 }
        	
        	System.out.println("Size"+ab.size());    
        	System.out.println("Size"+ab1);
        	System.out.println(ab);   
        	System.out.println("Done");
        }
 
        public static String bitprint(int u){
                String s= "";
                for(int n= 0;u > 0;++n, u>>= 1)
                        if((u & 1) > 0) s+= "A"+ n + " ";
                return s;
        }
 
        public static int bitcount(int u){
                int n;
                for(n= 0;u > 0;++n, u&= (u - 1));//Turn the last set bit to a 0
                return n;
        }
 
        public static LinkedList<String> comb(int c, int n){
                LinkedList<String> s= new LinkedList<String>();
                for(int u= 0;u < 1 << n;u++)
                        if(bitcount(u) == c) s.push(bitprint(u));
                Collections.sort(s);
                System.out.println("Combination:"+ s.size());
                return s;
        }
        
        public static void countStringOccurences(String[] strArray) {
            HashMap<String, Integer> countMap = new HashMap<String, Integer>();
            for (String string : strArray) {
                if (!countMap.containsKey(string)) {
                    countMap.put(string, 1);
                } else {
                    Integer count = countMap.get(string);
                    count = count + 1;
                    countMap.put(string, count);
                }
            }
            System.out.println(countMap);
        }
        
        public static void getalldata(String[] strArray,String txt) {
            HashMap<String, Integer> countMap = new HashMap<String, Integer>();
            Integer count =0;
            for (String string : strArray) {
                if (string.contains(txt)) {
                    countMap.put(string, count);
                } 
                count = count + 1;
            }
            System.out.println(countMap);
        }
}