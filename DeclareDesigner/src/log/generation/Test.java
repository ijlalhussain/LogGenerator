package log.generation;
/*
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
		
		getParentLetter("A0A1A2");
    }

	public static void getParentLetter(String string) {
        StringBuilder alphabetsBuilder = new StringBuilder();
       // StringBuilder numbersBuilder = new StringBuilder();
       // StringBuilder symbolsBuilder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (Character.isAlphabetic(ch)) {
                alphabetsBuilder.append(ch);
            } else if (Character.isDigit(ch)) {
         //       numbersBuilder.append(ch);
                alphabetsBuilder.append(";");
            }
        }
        System.out.println("Alphabets in string: " + alphabetsBuilder.toString().split(";")[0]);
        
        
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
*/

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import net.sf.javailp.Linear;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;

public class Test {
   // public static void main(String... args) {
    	
    	
    	public static void main(String[] args) {
    		
    		
    		
    		SolverFactory factory = new SolverFactoryLpSolve(); // use lp_solve
    		factory.setParameter(Solver.VERBOSE, 0); 
    		factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100 seconds

    		/**
    		* Constructing a Problem: 
    		* Maximize: 143x+60y 
    		* Subject to: 
    		* 120x+210y <= 15000 
    		* 110x+30y <= 4000 
    		* x+y <= 75
    		* 
    		* With x,y being integers
    		* 
    		*/
    		Problem problem = new Problem();

			
			Problem problem2 = new Problem();
			
			
			Linear linear = new Linear();
			linear.add(1, "x");
			//lnr.add(linear);

			problem.add(linear, ">=", 5);
			problem.setObjective(linear, OptType.MAX);
			problem.setVarType("x", Integer.class);

			Linear linear2 = new Linear();
			linear2.add(1, "Y");
			linear2.add(-1, "x");
			problem.add(linear2, ">=", 0);
			problem.setObjective(linear, OptType.MIN);
			problem.setVarType("Y", Integer.class);
			
			Solver solver = factory.get(); // you should use this solver
			// only once for one problem
net.sf.javailp.Result result = solver.solve(problem);

System.out.println("Key:");
//		System.out.println(result);

// Solver solver2 = factory.get(); // you should use this solver
// only once for one problem
net.sf.javailp.Result result2 = solver.solve(problem2);

//System.out.println(" Min : "+ result2.getObjective().intValue());
System.out.println(" Min : "+ result.getObjective().intValue());
    		/*
    		linear = new Linear();
    		linear.add(110, "x");
    		linear.add(30, "y");

    		problem.add(linear, "<=", 4000);

    		linear = new Linear();
    		linear.add(1, "x");
    		linear.add(1, "y");*/

    		problem.add(linear, "<=", 75);

    		problem.setVarType("x", Integer.class);
    		problem.setVarType("y", Integer.class);

    		/*Solver solver = factory.get(); // you should use this solver only once for one problem
    		Result result = solver.solve(problem);*/

    		System.out.println(result);

    		/**
    		* Extend the problem with x <= 16 and solve it again
    		*/
    		problem.setVarUpperBound("x", 16);

    		solver = factory.get();
    		result = solver.solve(problem);

    		System.out.println(result);	
    		System.out.println("ILP=Result");
    		System.out.println(result);
           
    		String[] strArray = {"123", "B0B1", "aa", "aa"};

    		 String string1 = "B0B1!";
    	        String string2 = "B0";

    	        if (string1 != null && string2 != null & string2.length() <= string1.length() & string1.contains(string2)) {
    	            System.out.println("string1 contains string2");
    	        }

    		
    		
    		Set<Object> strSet = Arrays.stream(strArray).collect(Collectors.toSet());
    		System.out.println(strSet);
    		
    		for(Object s: strSet){
    			// printing the contents of our array
    			System.out.println(s +"s");
    		}
    		
    			HashMap<Integer,NewTarget> traceMap = new HashMap<Integer,NewTarget>();
    			int count = 0;
    	        int cursor = 0;
    		String a[]= {"B0","D0D1","B0"};
            String b[] ={"B0","D0D1","B0"};
            
            for(int i=0; i<a.length; i++){
                for(int j=cursor; j<b.length; i++){
                    if(a[i].equals(b[i])){
                        count++;
                        cursor = j;
                        System.out.println(b[i]);
                        break;
                    }
                }
            }
            
            
            String arrayA[]= {"A0","A01","B0B1","B0_B1","D0D1"};
            String arrayB[] ={"B0","B0_B1","D0D1"};
            
           
            ArrayList arrayList = new ArrayList();
  		   
		      
		      arrayList.add("A0");
		      arrayList.add("A0A1");
		     /* arrayList.add("B0B1");
		      arrayList.add("C1");
		      arrayList.add("A1");*/
		      
		  	ArrayList arrayList2 = new ArrayList();
	 		   
		      
		  	arrayList2.add("B0B01");
		  	arrayList2.add("A0A1");
		  /*	arrayList2.add("A1");
		  	arrayList2.add("d");
		  	arrayList2.add("e");*/
            
		 	ArrayList arrayList22 = new ArrayList();
		 	ArrayList<String> Added = new ArrayList<String>();
		 	ArrayList<String> Added1 = new ArrayList<String>();
		 	ArrayList<String> Added2 = new ArrayList<String>();
		 	arrayList.retainAll(arrayList2);
            System.out.println("added " + arrayList2.toString());
          //  for (int i =0 ; i <3; i++){
            	NewTarget tb = new NewTarget();
            	//arrayList.add(i+"aas");
            	tb.SelectedTargetList = arrayList;
                tb.SouceList = arrayList2;
            traceMap.put(0, tb);
            //}
            
            
            arrayList.add("dd");
		      arrayList.add("ee");
		      arrayList.add("44");
		      arrayList.add("55");
		      
		      //  for (int i =0 ; i <3; i++){
          	NewTarget tb2 = new NewTarget();
          	//arrayList.add(i+"aas");
          	tb2.SelectedTargetList = arrayList;
              tb2.SouceList = arrayList2;
          traceMap.put(1, tb2);
		      
		      
            for (Entry<Integer, NewTarget> activity : traceMap.entrySet()) {
				Integer k = activity.getKey();
				NewTarget tx = activity.getValue();
		//	for (int i = 0; i < traceMap.size(); i++) {
				//System.out.println("Fileration in Trace : " + i);
			//	TraceAlphabet tx = traceMap.get(i);
				//if (!tx.isEmpty()) {
					//for (int j = 0; j < tx.size(); j++) {
						System.out.println("KEY  List");//Eevent : " + k + " Map (A): " + tx.selectedSource + ":" + tx.sourceIndex);
						if ((tx.SouceList !=null)) {
							System.out.println(tx.SouceList.toString());
							for (int m = 0; m < tx.SouceList.size(); m++) {
							//for (int m = 0; m < tx.targetListKey.size(); m++) {
								System.out.println("targetList :" + tx.SouceList.get(m) + " : "+tx.SouceList.get(m) );
							}
						}
						System.out.println("iNDEX PRINT List");
						if (tx.SelectedTargetList !=null){
							for (int mx = 0; mx < tx.SelectedTargetList.size(); mx++) {
								System.out.println("SelectedTargetList:" + tx.SelectedTargetList.get(mx) + " : "+tx.SelectedTargetList.get(mx) );
							}
						}
				//	}

				}
            
            
            
            boolean foundSwitch = false;

          //outer loop for all the elements in arrayA[i]
          for(int i = 0; i < arrayA.length; i++)
          {
          //inner loop for all the elements in arrayB[j]
          for (int j = 0; j < arrayB.length;j++)
          {
          //compare arrayA to arrayB and output results
          if( arrayA[i].equals(arrayB[j]))
          {
          foundSwitch = true;
          System.out.println( "FOUND" + arrayB[j]);
          }
          }
          if (foundSwitch == false)
          {
        	  System.out.println( "nOTFOUND");
          }
          //set foundSwitch bool back to false
          foundSwitch = false;
          }
          
            
            
            
          
     
    	List list = Arrays.asList("A0 A1 A2 A0 A0A1 AOA2 A0A1A2".split(" "));
    		      System.out.println("List :"+list);
    		      List sublist = Arrays.asList("A1".split(" "));
    		      System.out.println("SubList :"+sublist);
    		      System.out.println("indexOfSubList: "
    		      + Collections.indexOfSubList(list, sublist));
    		      System.out.println("lastIndexOfSubList: "
    		      + Collections.lastIndexOfSubList(list, sublist));
    		      ArrayList arrayList23 = new ArrayList();
    		   
    		      
    		      int retval=list.indexOf("A0");
    		      System.out.println("The element E is at index " + retval);
    		      //Add elements to Arraylist
    		      arrayList.add("1");
    		      arrayList.add("2");
    		      arrayList.add("3");
    		      arrayList.add("4");
    		      arrayList.add("5");
    		     
    		      /*
    		         To get a sub list of Java ArrayList use
    		         List subList(int startIndex, int endIndex) method.
    		         This method returns an object of type List containing elements from
    		         startIndex to endIndex - 1.
    		      */
    		     
    		      List lst = arrayList.subList(1,3);
    		         
    		      //display elements of sub list.
    		      System.out.println("Sub list contains : ");
    		      for(int i=0; i< lst.size() ; i++)
    		        System.out.println(lst.get(i));
    	
    	/* Bag<Integer> b = new Bag<>();
        b.countFor(1, 2);
        b.countFor(2, 1);
        b.countFor(3, 3);
        Set<String> set = new LinkedHashSet<>();
        for (List<Integer> list : b.combinations()) {
            System.out.println(list);
            String s = list.toString();
            if (!set.add(s))
                System.err.println("Duplicate entry " + s);
        }*/
    }
}

class Bag<E> {
    final Map<E, AtomicInteger> countMap = new LinkedHashMap<>();

    void countFor(E e, int n) {
        countMap.put(e, new AtomicInteger(n));
    }

    void decrement(E e) {
        AtomicInteger ai = countMap.get(e);
        if (ai.decrementAndGet() < 1)
            countMap.remove(e);
    }

    void increment(E e) {
        AtomicInteger ai = countMap.get(e);
        if (ai == null)
            countMap.put(e, new AtomicInteger(1));
        else
            ai.incrementAndGet();
    }

    List<List<E>> combinations() {
        List<List<E>> ret = new ArrayList<>();
        List<E> current = new ArrayList<>();
        combinations0(ret, current);
        return ret;
    }

    private void combinations0(List<List<E>> ret, List<E> current) {
        if (countMap.isEmpty()) {
            ret.add(new ArrayList<E>(current));
            return;
        }
        int position = current.size();
        current.add(null);
        List<E> es = new ArrayList<>(countMap.keySet());
        if (es.get(0) instanceof Comparable)
            Collections.sort((List) es);
        for (E e : es) {
            current.set(position, e);
            decrement(e);
            combinations0(ret, current);
            increment(e);
        }
        current.remove(position);
    }
}