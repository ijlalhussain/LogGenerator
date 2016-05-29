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
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    public static void main(String... args) {
     
    	List list = Arrays.asList("A0 A1 A2 A0 A0A1 AOA2 A0A1A2".split(" "));
    		      System.out.println("List :"+list);
    		      List sublist = Arrays.asList("A1".split(" "));
    		      System.out.println("SubList :"+sublist);
    		      System.out.println("indexOfSubList: "
    		      + Collections.indexOfSubList(list, sublist));
    		      System.out.println("lastIndexOfSubList: "
    		      + Collections.lastIndexOfSubList(list, sublist));
    		      ArrayList arrayList = new ArrayList();
    		   
    		      
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