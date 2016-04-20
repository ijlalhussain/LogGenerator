package log.generation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
public class Permutations {
    public static void main(String args[]) {
        Permutations obj = new Permutations();
        Collection input = new ArrayList();
        input.add("A0");
        input.add("A1");
        input.add("A2");
        input.add("A3");
      
 
        Collection<List> output = obj.permute(input);
        int k = 0;
        Set<List> pnr = null;
        for (int i = 0; i <= input.size(); i++) {
            pnr = new HashSet<List>();
            for(List integers : output){
            pnr.add(integers.subList(i, integers.size()));
            }
            k = input.size()- i;
            System.out.println("P("+input.size()+","+k+") :"+ 
            "Count ("+pnr.size()+") :- "+pnr);
        }
    }
    public Collection<List> permute(Collection input) {
        Collection<List> output = new ArrayList<List>();
        if (input.isEmpty()) {
            output.add(new ArrayList());
            return output;
        }
        List list = new ArrayList(input);
        String head = list.get(0).toString();
        List rest = list.subList(1, list.size());
        for (List permutations : permute(rest)) {
            List<List> subLists = new ArrayList<List>();
            for (int i = 0; i <= permutations.size(); i++) {
                List subList = new ArrayList();
                subList.addAll(permutations);
                subList.add(i, head);
                subLists.add(subList);
            }
            output.addAll(subLists);
        }
        return output;
    }
}