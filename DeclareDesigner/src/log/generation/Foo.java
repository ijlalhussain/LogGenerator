package log.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Foo {
	
	 private static final List<String> productList = Arrays.asList(
		        "A0", "B0B1", "banana", "parrot", "B0B1", "bird", "flower", "B0B11",
		        "banana", "bird"
		    );

		    private static List<String> indicesFor(final List<String> list)
		    {
		        final Map<String, Integer> indices = new HashMap<String, Integer>();

		        final int size = list.size();

		        for (int i = 0; i < size; i++)
		            indices.put(list.get(i), i);

		        final List<String> ret = new ArrayList<String>(indices.size());
		        for (final Map.Entry<String, Integer> entry: indices.entrySet())
		            ret.add(entry.toString());
		        return ret;
		    }

		    public static void main(final String... args)
		    {
		        
		    	 String[] strArray = { "A0", "B0B1", "banana", "parrot", "B0B1", "bird", "flower", "B0B11",
		 		        "banana", "bird"};
		    	 
		         for (int i = 0; i < strArray.length-1; i++)
		         {
		             for (int j = i+1; j < strArray.length; j++)
		             {
		                 if( (strArray[i].equals(strArray[j])) && (i != j) )
		                 {
		                     System.out.println("Duplicate Element is : "+strArray[j]);
		                 }
		             }
		         }
		    	
		    	
		    	System.out.println(indicesFor(productList));
		        System.exit(0);
		    }
		}


