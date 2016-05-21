package log.generation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;


import java.util.Random;

import minerful.concept.ProcessModel;
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
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;



public class DeclareLogGenerator {
	/*public static long traceLength = 0;*/
	static LinkedHashMap<String, Alphabet> abMap = new LinkedHashMap<String, Alphabet>();
	static LinkedHashMap<String, Alphabet> abMapx = new LinkedHashMap<String, Alphabet>();
	static LinkedHashMap<String, Alphabet> corrlationList = new LinkedHashMap<String, Alphabet>();	
	static LinkedHashMap<String, Alphabet> abMapdata = new LinkedHashMap<String, Alphabet>();
/*	static LinkedHashMap<String, Alphabet> abMapxx = new LinkedHashMap<String, Alphabet>();
*/	/*static LinkedHashMap<String, ArrayList<String>> abMapCombine = new LinkedHashMap<String, ArrayList<String>>();*/
	ArrayList<WorkItemData> data;
	static ArrayList<String> alphabets1 = new ArrayList();
	static ArrayList<String> firstact = new ArrayList();
	static ArrayList<String> secondact = new ArrayList();
	static ArrayList<String> firstcont = new ArrayList();
	static ArrayList<String> CorrelationContList = new ArrayList();
	static ArrayList<String> addedList = new ArrayList();
	static ArrayList<String> countedList = new ArrayList();
	static ArrayList<String> countedListB = new ArrayList();
	static ArrayList<String> countedListA = new ArrayList();
	static ArrayList<String> alphabetList = new ArrayList();
	static ArrayList<String> repeatList = new ArrayList();
	static ArrayList<String> combinedList = new ArrayList();
	static ArrayList<String> combinedListB = new ArrayList();
	static String secondkey ="";
	static String actcond ="";
	static String constrainB ="";
	private LogMakerCmdParameters parameters;
	public static Encoding OUTPUT_ENCODING = Encoding.xes;
	public static final File OUTPUT_LOG = new File("");

	public static boolean GenerateLog(int minlength, int maxlength, long LogSize,
			String filename, String destitionfile) {
		AssignmentViewBroker broker = XMLBrokerFactory
				.newAssignmentBroker(filename);
		AssignmentModel model = broker.readAssignment();
		
		
		LogMakerCmdParameters logMakParameters = new LogMakerCmdParameters(
				minlength, maxlength, LogSize);
		OUTPUT_ENCODING = Encoding.xes;
		logMakParameters.outputEncoding = OUTPUT_ENCODING;

		File OUTPUT_LOG = new File(destitionfile);
		logMakParameters.outputLogFile = OUTPUT_LOG;
		
	
		
		// addNewData(model);
		getAlphabets(model); // add first aphabets and condition
		checkActivation(model); // add activation conditions
		ParameterSettings2.jProgressBar1.setValue(3);
		AddFirstLetter(model); //add into abMap
		generateCombination(); // count alphabets
		abMapx.clear();
		SetCombinationCondtion(); // combination without rep.
		IlpSolver.CheckIlpConditions(combinedList, abMapx);
		ParameterSettings2.jProgressBar1.setValue(4);
		addCorrelatedConditions();
		
		IlpSolver.purifyLog(combinedList, abMapx);
		CheckforPrecendence();
		
		if (abMapx.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Unable to Generate Log!");
			return false;
		}
		
		MinerFulLogMaker logMak = new MinerFulLogMaker(logMakParameters);		
		org.processmining.plugins.declareminer.visualizing.AssignmentModel proMod = null;  
		//proMod = MinerfulLogGenerator.fromDeclareMapToMinerfulProcessModel(model,null, combinedList, abMapx);
	 
		//addCorrelatedConditions();
		DeclareModelGenerator dm = new DeclareModelGenerator(model,abMapx);
		proMod=	dm.generateModel();
	    XLog xlog=	logMak.createLog(proMod);	
	  /*System.out.println("_____printing Map__________________");
		   LogService.PrintMyLog(model, abMapx, LogSize, combinedList, minlength, maxlength);
	 		XLog xxlog = LogService
				.GenerateLog(model, abMapx, LogSize, combinedList,minlength,maxlength);
		XLifecycleExtension lifeExtension = XLifecycleExtension.instance();
		lifeExtension.assignModel(xlog, model.getName());*/
		
		try {
			logMak.storeLog();	
			//LogService.storeLog(xlog, logMakParameters);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		//System.out.println(lifeExtension.getName());
		return true;
	}
	




	private static void CheckforPrecendence() {
		
	LinkedHashMap<String, Alphabet> abMaptemp = new LinkedHashMap<String, Alphabet>();
		String aa ="";
		String bb = "";
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet filter = activity.getValue();
			aa="";bb="";
			if (filter.constrain.equals("precedence")){
				
				System.out.println("A :" + filter.alphabetkey);
				System.out.println("B :" + filter.secondAlphabetKey);
				
				aa = BranchCombination.getParentLetter(filter.alphabetkey);
				bb = BranchCombination.getParentLetter(filter.secondAlphabetKey);
				k = k.replace(aa, bb);
				filter.alphabetkey = filter.alphabetkey.replace(aa,bb);
				filter.alphabetname = filter.alphabetname.replace(aa,bb);
				filter.secondAlphabetKey = filter.secondAlphabetKey.replace(bb,aa);
				String corr [] = filter.correlationlist;
				if (corr!=null)
				{
					for (int i=0; i < corr.length; i++){						
					corr[i] = corr[i].replace(bb, aa);	
					}
				filter.correlationlist = corr;	
				}
				
			}
			abMaptemp.put(k,filter);
		}
		abMapx.clear();
		abMapx=abMaptemp;
		System.out.println("-------Precendece Checking-----------");
		
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet filter = activity.getValue();
			System.out.println("___________________________");
			System.out.println("Key: "+ k );
			System.out.println("actCondition: "+ filter.actCondition );
			System.out.println("Corelation: "+ filter.relCondition );
			System.out.println("fullCondition: "+ filter.fullCondition );
			System.out.println("ILP: "+ filter.ilpCondition );
			System.out.println("secondAlphabetKey: "+ filter.secondAlphabetKey );
			System.out.println("Constrains : "+ filter.constrain );
			
			
			 
			if (filter.correlationlist != null){
			for (int i=0; i < filter.correlationlist.length; i++){
				if (i>=1){
					System.out.println("Cor : "+ filter.correlationlist[i]);
					//System.out.println("Cor : "+  filter.correlationlist[0] +"_"+filter.correlationlist[i]);
				}else{
				System.out.println("Cor : "+ filter.correlationlist[i]);
			}}}
		
		}
		System.out.println("Wait");
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

	
	public static void generateCombination() {
		repeatList.clear();
		combinedList.clear();

		for (int i = 0; i < countedList.size(); i++) {
			String[] lst = countedList.get(i).split("=");
			System.out.println(lst[0]);
			System.out.println(lst[1]);
			int max = Integer.parseInt(lst[1]);
			combinedList.add(lst[0]);
			
		 int ind =	alphabetList.indexOf(lst[0]);
		// String b = countedListB.get(ind);
		// combinedListB.add(b);
			if (max > 1) {
				BranchCombination.GetCombination(lst[0], max, combinedList);
			//	BranchCombination.GetCombination(b, max, combinedListB);
				repeatList.add(combinedList.get(combinedList.size() - 1));
		  } else {
			  combinedList.add(lst[0]);
		//	  combinedList.add(b);
		  }
		}
		System.out.println("");
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
		return "Error:ActivationCondition";
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
		String constrain = "";
		String lastname="";
		String vkey = "";
		System.out.println("-----------First Step--------------");
		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model
				.getConstraintDefinitions()) {
			 constrain = cd.getName().replace("-", "").replace(" ", "").toLowerCase();
			for (Parameter p : cd.getParameters()) {
				
				for (nl.tue.declare.domain.model.ActivityDefinition ad : cd
						.getBranches(p)) {	
				
				//	System.out.println(ad.getName());					
					if (constrain.equals("precedence"))
					{
						if (p.getName().equals("B")) {
							firstName = ad.getName().toString();
							countedList.add(firstName);
							alphabetList.add(lastname);
							alphabetList.add(firstName);
							countedListB.add(firstName);
							//countedListB.add(firstName);
						} else {
						//	alphabetList.add(ad.getName());
							lastname = ad.getName();
							
						}
					}
					else{
					if (p.getName().equals("A")) {
						firstName = ad.getName().toString();
						countedList.add(firstName);
						alphabetList.add(firstName);
						//countedListB.add(firstName);
					} else {
						alphabetList.add(ad.getName());
						countedListB.add(ad.getName());
					}
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
		abMapdata.clear();
		String vkey = "";
		int check = 0;
	//	static LinkedHashMap<String, Alphabet> abMapdata = new LinkedHashMap<String, Alphabet>();
		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model
				.getConstraintDefinitions()) {
			String constrain = cd.getName().replace("-", "").replace(" ", "").toLowerCase();
			Alphabet ab = new Alphabet();
			ab.constrain = constrain;
			ab.actCondition = 	getLeft(cd.getCondition().toString());
			ab.relCondition = getRelCond(cd.getCondition().toString());
			check=0;
			for (Parameter p : cd.getParameters()) {
				for (nl.tue.declare.domain.model.ActivityDefinition ad : cd
						.getBranches(p)) {	
				
					
					if (constrain.equals("precedence")){
						if (p.getName().equals("B")) {
							String sname = ad.getName();
							
							firstact.add(sname +checkList(sname));
							alphabets1.add(sname);
							if (!cd.getCondition().toString().isEmpty()) {
								firstcont.add(getLeft(cd.getCondition().toString()));
								CorrelationContList.add(getRelCond(cd.getCondition().toString()));
							} else {
								firstcont.add("");
								CorrelationContList.add("");
							}
						} /// end of A
						
					}
					else
					{
					
					//Start of A
					if (p.getName().equals("A")) {
						String sname = ad.getName();
						firstact.add(sname +checkList(sname));
						alphabets1.add(sname);
						if (!cd.getCondition().toString().isEmpty()) {
							firstcont.add(getLeft(cd.getCondition().toString()));
							CorrelationContList.add(getRelCond(cd.getCondition().toString()));
						} else {
							firstcont.add("");
							CorrelationContList.add("");
						}
					} /// end of A
					}		
					
					abMapdata.put(vkey, ab);
					
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
		String constrain ="";
		for (nl.tue.declare.domain.model.ConstraintDefinition cd1 : tmpcd) {
			for (Parameter p1 : cd1.getParameters()) {
				for (nl.tue.declare.domain.model.ActivityDefinition ad1 : cd1
						.getBranches(p1)) {
					constrain = cd1.getName();
					if (constrain.equals("precedence")){
						
						if (p1.getName().equals("B")
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
					
					//start
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
					} // end of resp
				}
			}
		}
		
		return Fullcondition;
		
	}
	
	public static void Addnew(String fname, String lname, String act, String ilpcond, boolean isActivated, String relcond,String FullCondition,String cons)
	{
		Alphabet ab = new Alphabet();
		ab.alphabetname = fname;
	
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
		if (relcond.isEmpty()){
		ab.isRetaiveTrue =false;
		ab.relCondition = "";
		} else {
		ab.relCondition = relcond;
		ab.isRetaiveTrue = true;
		}
		ab.secondAlphabet = lname;
		ab.secondAlphabetKey = lname;
		ab.constrain = cons;
		ab.fullCondition = FullCondition;
		abMap.put(fname, ab);
		abMapx.put(fname, ab);
		
	}
	
	public static void AddFirstLetter(AssignmentModel model) {
		String fname = "";
		String FullCondition="";
		String lname="";
		String ilpcond = "";
		String condition ="";
		String constrain ="";
		int ind=0;
		boolean addparent = false;
		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model
				.getConstraintDefinitions()) {
			FullCondition = cd.getCondition().toString();	
			condition = getLeft(FullCondition);
				addparent =false;
				for (Parameter p : cd.getParameters()) {
					for (nl.tue.declare.domain.model.ActivityDefinition ad : cd
							.getBranches(p)) {
						
						//constrain= cd.getName();
						constrain = cd.getName().replace("-", "").replace(" ", "").toLowerCase();
						
						
						if (constrain.equals("precedence")) {

							if (p.getName().equals("B")) {
								 fname = ad.getName().toString();
								 addparent= (checkIndex(fname) ==0);
								 addedList.add(fname);
								// lname =ad.getName().toString();
								 ilpcond =	getCondtions(model,lname,condition);
									ind = checkIndex(fname);					
									ind = ind - 1;
									
								if (ind==0){
									addparent =true;
									Addnew(fname,lname,condition,ilpcond,true,getRelCond(FullCondition),FullCondition,constrain);	
								}	
								fname = fname+ (Integer.toString(ind));
								Addnew(fname,lname,condition,ilpcond,true,getRelCond(FullCondition),FullCondition,constrain);
								/*if (addparent){
								Addnew(fname+"_1",lname, BranchCombination.divertCondition(condition),ilpcond,true,getRelCond(condition),FullCondition,constrain);
								}*/
								Addnew(lname,"","","",false,"",FullCondition,constrain);			 
								}else
								{
									lname= ad.getName().toString();
								}		
						} // end of precedence
						
						//start of response and ...
						else {
						if (p.getName().equals("A")) {
						 fname = ad.getName().toString();
						 addparent= (checkIndex(fname) ==0);
						 addedList.add(fname);
						 
							if(constrain.equals("existence")){
								ilpcond =	getCondtions(model,fname,condition);
								ind = checkIndex(fname);					
								ind = ind - 1;
								lname = fname;
							if (ind==0){
								addparent =true;
								Addnew(fname,lname,condition,ilpcond,true,getRelCond(FullCondition),FullCondition,constrain);	
							}	
							fname = fname+ (Integer.toString(ind));
							Addnew(fname,lname,condition,ilpcond,true,getRelCond(FullCondition),FullCondition,constrain);
							if (addparent){
							Addnew(fname+"_1",lname, BranchCombination.divertCondition(condition),ilpcond,true,getRelCond(condition),FullCondition,constrain);
							}
							Addnew(lname,"","","",false,"",FullCondition,constrain);
								}
					
						}else
						{
							lname= ad.getName().toString();
						}						
					}
					//start b
					if (p.getName().equals("B")) {
						
						ilpcond =	getCondtions(model,fname,condition);
						ind = checkIndex(fname);					
						ind = ind - 1;
						
					if (ind==0){
						addparent =true;
						Addnew(fname,lname,condition,ilpcond,true,getRelCond(FullCondition),FullCondition,constrain);	
					}	
					fname = fname+ (Integer.toString(ind));
					Addnew(fname,lname,condition,ilpcond,true,getRelCond(FullCondition),FullCondition,constrain);
					if (addparent){
					Addnew(fname+"_1",lname, BranchCombination.divertCondition(condition),ilpcond,true,getRelCond(condition),FullCondition,constrain);
					}
					Addnew(lname,"","","",false,"",FullCondition,constrain);
				
					} 
					// end of b response
				}
					}
		}
		
		System.out.println(abMap);
	}

	public static void SetCombinationCondtion() {
		
		String key = "";
		String codition = "";
		String tempp = "";
		String send = "";
		String[] clst;
		String b = "";
		String combinationCond = "";
		String constain = "";
		// Alphabet ab = new Alphabet();
		
		abMapx.clear();
		for (int i = 0; i < combinedList.size(); i++) {
			clst = combinedList.get(i).trim().split(" ");
			key = combinedList.get(i).toString().trim().replaceAll(" ", "");
			codition = "";
			System.out.println("key"+ key  + " : " + clst.length);
			if (clst.length <= 1) {
				codition = zeroLevel(clst[0]);
			} else {
				codition = UpperLevel(combinedList.get(i));
			}
			combinationCond = codition;
			Alphabet abX = abMap.get(key);
			if (abX == null) {
				Alphabet ab = new Alphabet();
				ab.ilpCondition = "";
				if (!codition.isEmpty())
					ab.ilpCondition = codition;
				ab.conditionlist = clst;
				ab.alphabetkey = key;
				ab.isSingle = false;
				ab.alphabetname =  BranchCombination.getParentLetter(key);
				if (clst.length > 1) {
					ab.isSingle = true;
					ab.secondAlphabet = getB(clst);					
				}	
				ab.constrain = constrainB;
				String cor = getCoRel(clst);// = abMap.get(ab.alphabetname);
				
				Alphabet bd = abMap.get(clst[0]);
				if (bd!=null){
					ab.secondAlphabetKey = bd.secondAlphabetKey;	
				}
				
				
				ab.actCondition = actcond;
				if (cor!=null){
					ab.relCondition = cor;
					ab.isRetaiveTrue =true;
				} else
				{
					ab.relCondition = "" ;
					ab.isRetaiveTrue =false;
				}
				abMapx.put(key, ab);
			} else {
				abX.isSingle = false;
				if (clst.length > 1) {
					abX.secondAlphabet = getB(clst);
					abX.isSingle = true;
				}
								
				if (!codition.isEmpty())
					abX.ilpCondition = codition;
				abX.conditionlist = clst;
				abX.alphabetkey = key;
				
				abMapx.put(key, abX);
			}

		}
	}
	
	

	public static void addCorrelatedConditions() {
		String corCondtion = "";
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet filter = activity.getValue();
			if (filter.isRetaiveTrue) {
				corCondtion = getCorrlationAlphabet(k);
				String[] corChoine = corCondtion.split(" ");

				if (corChoine.length <= 1) {
					String[] cor = getZeroCor(k, filter.alphabetname,
							filter.secondAlphabetKey,filter);
					filter.correlationlist = cor;
					filter.secondAlphabet = "";
				/*	SetSecondList(filter.alphabetname,
							filter.secondAlphabetKey,cor);*/
					
				} else {
					filter.correlationlist = getUpperCor(k,
							filter.alphabetname, filter.secondAlphabetKey);
					String snd = filter.secondAlphabet;
				//xxx	filter.secondAlphabet = snd.replaceAll(
					//		filter.secondAlphabetKey, "!@#@!");
					/*
					 * System.out.println("Key: " + k + " :  Upper corre: " +
					 * getUpperCor(k,filter.alphabetname,
					 * filter.secondAlphabetKey));
					 */
				}

				abMapx.put(k, filter);
			}
		}
		
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet filter = activity.getValue();
			System.out.println("___________________________");
			System.out.println("Key: "+ k );
			System.out.println("actCondition: "+ filter.actCondition );
			System.out.println("Corelation: "+ filter.relCondition );
			System.out.println("fullCondition: "+ filter.fullCondition );
			System.out.println("ILP: "+ filter.ilpCondition );
			System.out.println("secondAlphabetKey: "+ filter.secondAlphabetKey );
			System.out.println("Constrains : "+ filter.constrain );
			
			
			 
			if (filter.correlationlist != null){
			for (int i=0; i < filter.correlationlist.length; i++){
				if (i>=1){
					System.out.println("Cor : "+ filter.correlationlist[i]);
					//System.out.println("Cor : "+  filter.correlationlist[0] +"_"+filter.correlationlist[i]);
				}else{
				System.out.println("Cor : "+ filter.correlationlist[i]);
			}}}
		
		}
		System.out.println("Wait");
	}
	
	public static void SetSecondList(String a, String b, String[] lst){
		String aa = BranchCombination.getParentLetter(a);
		corrlationList.put(a, null);
	}
	
	public static String[] getZeroCor(String xkey, String a, String b, Alphabet ab) {
		String aa = BranchCombination.getParentLetter(a);
		String ret = "";
		for (int ind = 0; ind < combinedList.size(); ind++) {
			String  temp = combinedList.get(ind).replaceAll(" ", "");
			String root[] =  combinedList.get(ind).split(" ");
			if (temp.contains(xkey)) {
				if (!ret.isEmpty()) {
					ret = ret + "::";
				}
				if (ind==0)
				{	
					ret = ret + combinedList.get(ind).replaceAll(aa, b).replaceAll(" ", "").trim();
					}
				else {
					String vtemp =  combinedList.get(ind).replaceAll(aa, b).replaceAll(" ", "").trim();
					if(!vtemp.equals(root[0].replaceAll(aa, b)))
					{
						ret = ret+ vtemp + "::"+ root[0].replaceAll(aa, b) + "_"+ vtemp;
						}
					else{
						{
							ret = ret+ vtemp;
							}	
					}	
				}
			}
		}	
		return ret.split("::");
	}
	
	public static String[] getUpperCor(String xkey, String a, String b) {
		String aa = BranchCombination.getParentLetter(a);
		String ret = "";
		int vsize = 0;
		for (int ind = 0; ind < combinedList.size(); ind++) {
			String root[] =  combinedList.get(ind).split(" ");
			String  temp = combinedList.get(ind).replaceAll(" ", "");
			if(temp.length() >= xkey.length()){
			if (temp.contains(xkey)) {
				if (!ret.isEmpty()) {
					ret = ret + "::";
				}
				if (root.length == 0)
				{ret = ret + combinedList.get(ind).replaceAll(aa, b).replaceAll(" ", "").trim();}
				else
				{
					String vtemp =  combinedList.get(ind).replaceAll(aa, b).replaceAll(" ", "").trim();
					if(!vtemp.equals(root[0].replaceAll(aa, b)))
					{ret = ret+ vtemp + "::"+ root[0].replaceAll(aa, b) + "_"+ vtemp;}
					else{
						{ret = ret+ vtemp;}	
					}
				}
			}
			}
		}
		return ret.split("::");
	}
	
	
	public static String getCorrlationAlphabet(String xkey) {
		// Ali
		String ret = "";
		for (int ind = 0; ind < combinedList.size(); ind++) {
			if (xkey.equals(combinedList.get(ind).replaceAll(" ", "").trim())) {
				if (!ret.isEmpty()) {
					ret = ret + " ";
				}
				ret = ret + combinedList.get(ind);
			}
		}
		return ret;
	}
	
	public static String getB(String[] clst){
		String b="";
		constrainB = "";
		secondkey ="";
		for (int i2=0;i2< clst.length;i2++){
			Alphabet b2 = abMap.get(clst[i2]);
			if (b2!=null){
				
				if(b2.constrain.equals("precedence")){
					secondkey = b2.secondAlphabetKey;
					if(constrainB.isEmpty())
						constrainB =b2.constrain;
					
				}else{
				if(constrainB.isEmpty())
					constrainB =b2.constrain;
				if (!b.isEmpty()){
					b=b+"::";
				}}
				b=b+b2.secondAlphabet;
			}
		}
		return b;
	}
	
	
	public static String getCoRel(String[] clst){
		String b="";
		String act = "";
		secondkey ="";
		constrainB ="";
		for (int i2=0;i2< clst.length;i2++){
			Alphabet b2 = abMap.get(clst[i2]);
			if (b2!=null){
			if (!b.isEmpty()){
					b=b+"::";
				}
				if (!act.isEmpty()){
					act=act+"::";
				}
				b=b+b2.relCondition;
				act=act+b2.actCondition;
			}
		}
		actcond = act;
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
	
/*	public static void getSingleCodition() {
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
	*/



	public static String getLeft(String s) {
		if (s.isEmpty()) return "";
		ConstraintConditions c = ConstraintConditions.build(s);
		return c.getActivationCondition();
	}
	
	public static String getRelCond(String s) {
		if (s.isEmpty()) return "";
		ConstraintConditions c = ConstraintConditions.build(s);
		return c.getConstraintCondition();
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