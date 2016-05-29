package log.generation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

public class ArrayChecking {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] trace = new String[] { "B0", "A0", "C0_C0C1", "B0_B0B1", "D1", "B0_B0B2"};
		String[] source = new String[] { "A0", "A1", "A0A1", "A0A2" };
		String[] target = new String[] { "B0", "B0_B0B1", "B0_B0B2" };

		ArrayList<String> sourceList = new ArrayList<String>();
		sourceList.add("A0");
		sourceList.add("A1");
		sourceList.add("A0A1");
		sourceList.add("A0A2");
		
		ArrayList<String> targetList = new ArrayList<String>();
		targetList.add("B0");
		targetList.add("B1");
		targetList.add("B0B1");
		targetList.add("B0B2");
		targetList.add("B0_B0B1");
		targetList.add("B0_B0B2");
		
		String key = "A0";
		for (int b = 0; b < trace.length; b++) {
			System.out.println("--");
			int index = sourceList.indexOf(key);
			if (index > -1) {
				System.out.println("trace  A = " + sourceList.get(index));
				for (int bb = b; bb < trace.length; bb++) {
					if (targetList.indexOf(trace[bb]) > -1) {
						System.out.println("Trace B : " + trace[bb] + " : "+ bb);
					}
				}
				break;
			}
		}
				 
			/*//	 System.out.println("index" + b);
				 int ndx = targetList.indexOf(trace[b]);
				 if(ndx > 0){
					  System.out.println("Trace B : "+ trace[ndx]);
				 for (int x = 0; x < targetList.size(); x++) {
				 if (trace[x].equals(trace[x])){
				  System.out.println("Trace B : "+ trace[x]);
			  }}}
			}
			*/
		//	int count = search(aa, bb[b]); // invoke the method
		//	System.out.println("Count = " + count);
	//	}
		
		
		/*for (int b = 0; b < bb.length; b++) {
			int count = search(aa, bb[b]); // invoke the method
			System.out.println("Count = " + count);
		}*/
	}
	
	public static int search(String[] list, String key) {
		int i, count = 0;
		for (i = 0; i < list.length; i++) {
			if (list[i].equals(key)) {
				System.out.println(list[i]);
				count = i;
				break;
			}
		}
		return (count);
	}

	public static void chec(String[] a, String[] b, String x) {
		
		for (int i = 0; i < a.length; i++) {
			System.out.println("a i: "+ a[i]);
			if((a[i]).equals(x)){
				System.out.println(a[i]);
				checB(b,a[i],i);
				break;
			}
		}

	}
	
	
	public static void checB(String[] a, String check, int start) {
		ArrayList<String> arrlist = new ArrayList<String>(a.length);
		for(int index = 0; index < a.length;index++){
			if(a[index].equals(check)){
				arrlist.add(a[index]);				
				System.out.println("Found on index "+index + ": "+ a[index]);
			}
		}
		

	}

}
