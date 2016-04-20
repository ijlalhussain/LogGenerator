package log.generation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;


import java.util.Random;



import minerful.logmaker.MinerFulLogMaker;
import minerful.logmaker.params.LogMakerCmdParameters;
import minerful.logmaker.params.LogMakerCmdParameters.Encoding;
import minerful.logparser.StringTaskClass;
import nl.tue.declare.appl.design.gui.ParameterSettings;
import nl.tue.declare.appl.design.gui.ParameterSettings2;
import nl.tue.declare.appl.worklist.gui.ActivitiesPane;
import nl.tue.declare.domain.*;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.organization.*;
import nl.tue.declare.domain.template.Parameter;
import nl.tue.declare.utils.*;
import nl.tue.declare.utils.prom.OperationalSupportUtils;
import nl.tue.declare.utils.prom.ProM;
//import org.sat4j.minisat.core.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import net.sf.javailp.*;
import net.sourceforge.jeval.Eval;
import net.sourceforge.jeval.MalformedExpression;
import nl.tue.declare.datamanagement.AssignmentViewBroker;
import nl.tue.declare.datamanagement.XMLBrokerFactory;
import nl.tue.declare.execution.WorkItemData;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.model.XLog;



public class DeclareLogGenerator {
	public static long traceLength = 0;
	static LinkedHashMap<String, Alphabet> abMap = new LinkedHashMap<String, Alphabet>();
	static LinkedHashMap<String, Alphabet> abMapx = new LinkedHashMap<String, Alphabet>();
	static LinkedHashMap<String, Alphabet> abMapxx = new LinkedHashMap<String, Alphabet>();
	static LinkedHashMap<String, ArrayList<String>> abMapCombine = new LinkedHashMap<String, ArrayList<String>>();
	ArrayList<WorkItemData> data;
	static ArrayList<String> alphabets1 = new ArrayList();
	static ArrayList<String> firstact = new ArrayList();
	static ArrayList<String> firstcont = new ArrayList();
	static ArrayList<String> addedList = new ArrayList();
	static ArrayList<String> countedList = new ArrayList();
	static ArrayList<String> alphabetList = new ArrayList();
	static ArrayList<String> repeatList = new ArrayList();
	static ArrayList<String> combinedList = new ArrayList();
	HashMap<String, String> attributes;
	private LogMakerCmdParameters parameters;
	public static Encoding OUTPUT_ENCODING = Encoding.xes;
	public static final File OUTPUT_LOG = new File("");

	public static boolean GenerateLog(int minlength, int maxlength, long LogSize,
			String filename, String destitionfile) {
		AssignmentViewBroker broker = XMLBrokerFactory
				.newAssignmentBroker(filename);
		AssignmentModel model = broker.readAssignment();
		// addNewData(model);
		checkActivation(model); // add activation conditions
		getAlphabets(model);
		ParameterSettings2.jProgressBar1.setValue(3);
		AddFirstLetter(model);
		generateCombination();
		abMapx.clear();
		SetCombinationCondtion();
		IlpSolver.CheckIlpConditions(combinedList, abMapx);
		ParameterSettings2.jProgressBar1.setValue(4);
		IlpSolver.purifyLog(combinedList, abMapx);
		if (abMapx.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Unable to Generate Log!");
			return false;
		}
		XLog xlog = LogService
				.GenerateLog(model, abMapx, LogSize, combinedList);
		XLifecycleExtension lifeExtension = XLifecycleExtension.instance();
		lifeExtension.assignModel(xlog, model.getName());
		LogMakerCmdParameters logMakParameters = new LogMakerCmdParameters(
				minlength, maxlength, LogSize);
		OUTPUT_ENCODING = Encoding.xes;
		logMakParameters.outputEncoding = OUTPUT_ENCODING;

		File OUTPUT_LOG = new File(destitionfile);
		logMakParameters.outputLogFile = OUTPUT_LOG;
		try {
			LogService.storeLog(xlog, logMakParameters);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println(lifeExtension.getName());
		return true;
	}
	

	public static int selectRandom(int max) {
		Random rand = new Random();
		return rand.nextInt(max);
	}

	public static String getPayload(String c) {
		String[] values = null;
		values = c.split(" ");
		return values[0].substring(1);
	}

	public static String getCondition(String c) {
		String[] values = null;
		values = c.split(" ");
		String s = values[1];
		return s;
	}

	public static String getPayloadValue(String c) {
		try {
			String[] values = null;
			values = c.split(" ");
			String s = values[2];
			return values[2].substring(0, values[2].length() - 1);

		} catch (IndexOutOfBoundsException e) {
			return "0";
		}

	}

	
	public static void generateCombination()
	{
		repeatList.clear();
		combinedList.clear();
		ArrayList<String> temp = new ArrayList<String>();
		for (int i=0; i< countedList.size(); i++)
		{
			String[] lst = countedList.get(i).split("=");
			System.out.println(lst[0]);
			System.out.println(lst[1]);			
			int max = Integer.parseInt(lst[1]);
			BranchCombination.GetCombination(lst[0], max, combinedList);
			repeatList.add(combinedList.get(combinedList.size()-1));
		}			
	}
	
	public static void SetFirstLetters(){
		for (int k=0; k< combinedList.size(); k++)
		{
			String[] kk = combinedList.get(k).toString().split(" ");
			String name= combinedList.get(k);
			System.out.println(name);
			if (kk.length <= 1){				
				zeroLevel(name.replaceAll(" ",""));				
			} else
			{
				//String name= combinedList.get(k).toString().replaceAll(" ","");
				UpperLevel(name);
			}
		}
	}
	public static String getAcitvationCondition(String a){
		for (int i = 0; i < firstact.size(); i++) {
			if (firstact.get(i).equals(a))
			{
				return firstcont.get(i);
			}
		}
		return "xxx";
	}
	
	public static String UpperLevel(String ss) {
		String cond = "";
		String temp = "";
		String found = "";
		String result = "";
		String[] sst = ss.split(" ");
		for (int rep = 0; rep < repeatList.size(); rep++) {
			for (int j = 0; j < sst.length; j++) {
				if (repeatList.get(rep).contains(sst[j])) {
					found = repeatList.get(rep).toString();
					break;
				}
			}
		}
		
		String[] sList = found.split(" ");
	//	List<String> finalDiff = ListUtils.subtract(Arrays.asList(sList), Arrays.asList(sst)); 
		
		/*for (int j = 0; j < finalDiff.size(); j++) {
			System.out.println("Missing::  "+ finalDiff.get(j));
		}*/
		
		for (int upper=0; upper < sList.length; upper++){
			temp = "";
			for (int ind = 0; ind < sst.length; ind++) {
				if (sst[ind].equals(sList[upper])){
					temp = sst[ind];
					result = getAcitvationCondition(temp);
					break;
				}
		    	}
			if (temp.isEmpty()) {
			temp = 	sList[upper];
			result = BranchCombination.divertCondition(getAcitvationCondition(temp));
			}
		

			if (!cond.isEmpty()) {
				cond = cond + "::";
			}
			cond = cond +result;// BranchCombination.divertCondition(temp);	
			
		}
		return cond;
	}// if contains ss
	
	
	public static String zeroLevel(String ss) {
		String allCond = "";
		String cond = "";
		String temp = "";
		for (int rep = 0; rep < repeatList.size(); rep++) {
			if (repeatList.get(rep).contains(ss)) {
				allCond = repeatList.get(rep).toString();
				String[] sst = allCond.split(" ");
					for (int ind = 0; ind < sst.length; ind++) {
						temp = getAcitvationCondition(sst[ind]);
						if (!cond.isEmpty())
								cond = cond + "::";
							if (sst[ind].equals(ss)) {
								cond = cond + temp;
							} else {
								cond = cond + BranchCombination.divertCondition(temp);
							}
					}// end of sst loop
				break;
			}//if contains ss
		}
		return cond;
	}
	public static void getAlphabets(AssignmentModel model) {
		
		countedList.clear();
		String firstName = "";	
		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model
				.getConstraintDefinitions()) {
			for (Parameter p : cd.getParameters()) {
				for (nl.tue.declare.domain.model.ActivityDefinition ad : cd
						.getBranches(p)) {
					if (p.getName().equals("A")) {
						firstName = ad.getName().toString();
						countedList.add(firstName);
						alphabetList.add(firstName);
					} else {
						alphabetList.add(ad.getName());
					}
				}
			}
		}		
		BranchCombination.countAlphabets(countedList);
		//alphabetList.clear();
//		Iterable<nl.tue.declare.domain.model.ActivityDefinition> dd = model
//				.getActivityDefinitions();
//		for (nl.tue.declare.domain.model.ActivityDefinition d : dd) {
//			System.out.println(d.getName());
//			alphabetList.add(d.getName());
//			countedList.add(d.getName());
//		} 
		
	}

	public static ArrayList<DataElement> getPayload(AssignmentModel model) {
		ArrayList<DataElement> lst = new ArrayList<DataElement>();
		for (int i = 0; i < model.getDataCount(); i++) {
			DataElement data = model.dataAt(i);
			lst.add(data);
			System.out.println(data);
		}
		return lst;
	}

	public static void addNewData(AssignmentModel model) {
		int indx = 0;
		int id = 1000;

		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model
				.getConstraintDefinitions()) {
			System.out.println(cd.getCondition() + ""
					+ cd.getCondition().isTrue());
			// checkCondtion(cd.getCondition().toString());
			for (Parameter p : cd.getParameters()) {
				System.out.println(p.getName());
				for (nl.tue.declare.domain.model.ActivityDefinition ad : cd
						.getBranches(p)) {
					if (p.getName().equals("A")) {
						id++;
						String stName = ad.getName() + indx;
						nl.tue.declare.domain.model.ActivityDefinition ad2 = ad;// new
																				// nl.tue.declare.domain.model.ActivityDefinition(stName,id,model);
						ad2.setName(stName);
						Parameter newp = new Parameter(id, stName);
						newp.setName(stName);
						newp.setBranchable(p.isBranchable());
						cd.addBranch(newp, ad);

					}
				}
			}
		}

	}
	
	public static int checkList(String alph)
	{
		int ret = 0;
		for (int i=0; i<alphabets1.size(); i++)
		{
			if (alphabets1.get(i).toString() ==alph){
				ret++;
			}
		}
		return ret;
	}
	

	public static void checkActivation(AssignmentModel model) {
		String condef;
		// alphabets.clear();
		alphabets1.clear();
		// conditions.clear();
		firstact.clear();
		firstcont.clear();
		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model
				.getConstraintDefinitions()) {
			String constrain = cd.getName().replace("-", "").replace(" ", "").toLowerCase();
			for (Parameter p : cd.getParameters()) {
				for (nl.tue.declare.domain.model.ActivityDefinition ad : cd
						.getBranches(p)) {
					if (p.getName().equals("A")) {
						String sname = ad.getName();
						firstact.add(sname +checkList(sname));
						alphabets1.add(sname);
						if (!cd.getCondition().toString().isEmpty()) {
							firstcont.add(getLeft(cd.getCondition().toString()));
						} else {
							firstcont.add("");
						}
					} 
					}					
				}
			}
		System.out.println(firstact);
		
		}	
	
	public static int checkIndex(String s) {
		int ret = 0;
		for (int i = 0; i < addedList.size(); i++) {
			if (addedList.get(i).toString() == s)
				ret++;
		}
		return ret;
	}

	public static String getCondtions(AssignmentModel model, String activity, String condition){
		String Fullcondition ="";		
		Iterable<nl.tue.declare.domain.model.ConstraintDefinition> tmpcd = model
				.getConstraintDefinitions();
		
		for (nl.tue.declare.domain.model.ConstraintDefinition cd1 : tmpcd) {
			for (Parameter p1 : cd1.getParameters()) {
				for (nl.tue.declare.domain.model.ActivityDefinition ad1 : cd1
						.getBranches(p1)) {
					if (p1.getName().equals("A")
							&& (ad1.getName()
									.equals(activity))) {
						if (getLeft(
								cd1.getCondition()
										.toString())
								.equals(condition)) {
							if (!Fullcondition.isEmpty()) {
								Fullcondition = Fullcondition
										+ "::";
							}
							Fullcondition = Fullcondition
									+ BranchCombination.divertCondition(getLeft(cd1
											.getCondition()
											.toString()));
						}
					}
				}
			}
		}
		
		return Fullcondition;
		
	}
	
	public static void Addnew(String fname, String lname, String act, String ilpcond, boolean isActivated)
	{
		Alphabet ab = new Alphabet();
		ab.alphabetname = fname;
		
		ab.isActivationTrue = false;
		int val =  0;
		if (!act.isEmpty())
		{
			val=Integer.parseInt(getPayloadValue(act));
			ab.payLoadValue = val;
			ab.randomValue = val;
			ab.payLoadILPValue = val;
			ab.randomValue = val;
			ab.actCondition = act;
		} else
		{
			ab.actCondition = "";
			ab.payLoadValue = val;
			ab.randomValue = val;
			ab.payLoadILPValue = val;
			ab.randomValue = val;
		}
		
		ab.alphabetkey = fname;
		ab.ilpCondition = ilpcond;
		ab.isActivated = isActivated;
		ab.secondAlphabet = lname;
		abMap.put(fname, ab);
		abMapx.put(fname, ab);
		
	}
	
	public static void AddFirstLetter(AssignmentModel model) {
		String fname = "";
		String lname="";
		String ilpcond = "";
		String condition ="";
		int ind=0;
		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model
				.getConstraintDefinitions()) {
				condition = getLeft(cd.getCondition().toString());
				for (Parameter p : cd.getParameters()) {
					for (nl.tue.declare.domain.model.ActivityDefinition ad : cd
							.getBranches(p)) {
						if (p.getName().equals("A")) {
						 fname = ad.getName().toString();
						 addedList.add(fname);
						}else
						{
							lname= ad.getName().toString();
						}						
					}
					
					if (p.getName().equals("B")) {
						
						ilpcond =	getCondtions(model,fname,condition);
						ind = checkIndex(fname);
						ind = ind - 1;
						fname = fname+(Integer.toString(ind));
						
					Addnew(fname,lname,condition,ilpcond,true);
					Addnew(fname+"1",lname, BranchCombination.divertCondition(condition),ilpcond,true);
					Addnew(lname,"","","",false);
					//AssignmentModel model, String activity, String condition
					System.out.println("A0: "+fname);
					System.out.println("A01: "+fname+"1");
					System.out.println("B: "+lname);
					System.out.println("Ind: "+ind);
					}
				}
		}
		
		System.out.println(abMap);
	}

	public static void SetCombinationCondtion() {
		abMapx.clear();
		String key = "";
		String codition = "";
		String tempp = "";
		String send = "";
		String[] clst;
		String b = "";
		// Alphabet ab = new Alphabet();
		
		abMapx.clear();
		for (int i = 0; i < combinedList.size(); i++) {
			clst = combinedList.get(i).trim().split(" ");
			key = combinedList.get(i).toString().trim().replaceAll(" ", "");
			codition = "";
			System.out.println("key"+ key  + " xx" + clst.length);
			if (clst.length <= 1) {
				codition = zeroLevel(clst[0]);
			} else {
				codition = UpperLevel(combinedList.get(i));
			}
			Alphabet abX = abMap.get(key);
			if (abX == null) {
				Alphabet ab = new Alphabet();
				//ab.secondAlphabet = send;
				if (!codition.isEmpty())
					ab.ilpCondition = codition;
				ab.conditionlist = clst;
				ab.alphabetkey = key;
				if (clst.length > 1) {
					ab.secondAlphabet = getB(clst);
				}					
				abMapx.put(key, ab);
			} else {
				if (clst.length > 1) {
					abX.secondAlphabet = getB(clst);
				}
								
				if (!codition.isEmpty())
					abX.ilpCondition = codition;
				abX.conditionlist = clst;
				abX.alphabetkey = key;
				abMapx.put(key, abX);
			}

		}
	}
	
	public static String getB(String[] clst){
		String b="";
		for (int i2=0;i2< clst.length;i2++){
			Alphabet b2 = abMap.get(clst[i2]);
			if (b2!=null){
				if (!b.isEmpty()){
					b=b+"::";
				}
				b=b+b2.secondAlphabet;
			}
		}
		return b;
	}
	
	
	public static void removeDuplicates() {
		ArrayList<String> result = new ArrayList<>();
		HashSet<String> set = new HashSet<>();
		for (String item : alphabets1) {
			if (!set.contains(item)) {
				result.add(item);
				set.add(item);
			}
		}
		alphabets1.clear();
		alphabets1 = result;
	}
	
	public static void getSingleCodition() {
		removeDuplicates();
		String cnd ="";
		for (int ind = 0; ind < alphabets1.size(); ind++) {
			for (int i = 0; i < firstact.size(); i++) {
				String s =  alphabets1.get(ind)+i+"";
				System.out.println(s + ":"+firstact.get(i).toString());
				if (firstact.get(i).toString().equals(s)) {
					if(!cnd.isEmpty())
						cnd = cnd +"::";
						cnd = cnd + firstcont.get(i).toString();					
				}
			}
			
			for (int ii = 0; ii < firstact.size(); ii++) {
				String temp = alphabets1.get(ind) + ii;
				System.out.println(temp);
				
				if (firstact.get(ii).toString().equals(temp)) {
					Alphabet ab = abMapx.get(temp);
					if (ab != null) {
						ab.ilpCondition = cnd;
						ab.isSingle = false;
						abMapx.put(temp, ab);
					}
				}
			}
		}		
	}
	



	public static String getLeft(String s) {
		ConstraintConditions c = ConstraintConditions.build(s);
		return c.getActivationCondition();
	}

	public static int getRandomTrace(int min, int max) {
		try {
			Random random = new Random();
			return random.nextInt(max - min) + min;
		} catch (IllegalArgumentException e) {
			return max;
		}

	}

	public static int getXvalue(String s) {
		int ret = 0;
		String regex = "\\d+";
		if (s.matches(regex)) {
			ret = Integer.parseInt(s);
		} else {
			String[] sp = s.split("\\.");
			if (sp[1].matches(regex)) {
				ret = Integer.parseInt(sp[1]);
			} else {
				ret = getRandomTrace(0, 40000);
			}
		}
		return ret;
	}

}
/*	public static void CheckILPCondition() {
//getSingleCodition();
String s = "";
for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
	String k = activity.getKey();
	Alphabet ddd = activity.getValue();
	String[] ss = ddd.conditionlist;
    System.out.println("key:"+ k + " cond: " + ddd.alphabetkey + "condition:" +  ddd.ilpCondition + "BranchB:" + ddd.secondAlphabet);
	for (int i=0; i< ddd.conditionlist.length; i++){
		System.out.println(ddd.conditionlist[i]);
		
	}
}

}*/