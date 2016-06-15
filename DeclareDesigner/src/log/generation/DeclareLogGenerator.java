package log.generation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;


import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import minerful.concept.ProcessModel;
import minerful.concept.TaskChar;
import minerful.concept.TaskCharArchive;
import minerful.concept.TaskCharSet;
import minerful.concept.constraint.Constraint;
import minerful.concept.constraint.ConstraintsBag;
import minerful.concept.constraint.relation.AlternateResponse;
import minerful.concept.constraint.existence.AtMostOne;
import minerful.concept.constraint.existence.End;
import minerful.concept.constraint.existence.Init;
import minerful.concept.constraint.existence.Participation; //EXISTENCE;
import minerful.concept.constraint.relation.AlternatePrecedence;
import minerful.concept.constraint.relation.ChainPrecedence;
import minerful.concept.constraint.relation.NotChainSuccession;
import minerful.concept.constraint.relation.NotCoExistence;
import minerful.concept.constraint.relation.NotSuccession;
import minerful.concept.constraint.relation.Precedence;
import minerful.concept.constraint.relation.RespondedExistence;
import minerful.concept.constraint.relation.Response;
import minerful.io.encdec.TaskCharEncoderDecoder;
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
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.ArrayUtils;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;



public class DeclareLogGenerator {
	/*public static long traceLength = 0;*/
	
	static LinkedHashMap<String, Alphabet> abMap = new LinkedHashMap<String, Alphabet>();
	static LinkedHashMap<String, Alphabet> abMap2 = new LinkedHashMap<String, Alphabet>();
	static LinkedHashMap<String, Alphabet> abMapAll = new LinkedHashMap<String, Alphabet>();
	static LinkedHashMap<String, Alphabet> abMapx = new LinkedHashMap<String, Alphabet>();
	static LinkedHashMap<String, Alphabet> corrlationList = new LinkedHashMap<String, Alphabet>();	
	static LinkedHashMap<String, Alphabet> abMapdata = new LinkedHashMap<String, Alphabet>();
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
	static ArrayList<String> constrainList = new ArrayList<String>();
	static 	TreeMap<TaskChar,String> newLogIndex = new TreeMap<TaskChar,String>();
	static 	TreeMap<Integer,TraceAlphabet> traceMap = new TreeMap<Integer,TraceAlphabet>();
	static 	TreeMap<Integer,LogAlphabet> logMap = new TreeMap<Integer,LogAlphabet>();
	//static 	HashMap<Integer,TraceAlphabet> traceMap2 = new HashMap<Integer,TraceAlphabet>();
	
	static TreeMap<Integer, TraceAlphabet> traceMap2 = new TreeMap<Integer,TraceAlphabet>();
	static TreeMap<Integer, TraceAlphabet> traceMaptest = new TreeMap<Integer,TraceAlphabet>();
	//static TreeMap<Integer, TraceAlphabet> traceMap3 = new TreeMap<Integer,TraceAlphabet>();
//	static 	HashMap<Integer,List<TraceAlphabet>> traceDuplicates = new HashMap<Integer,List<TraceAlphabet>>();
	
	
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
		IlpSolver.CheckIlpConditions(abMapx);
		ParameterSettings2.jProgressBar1.setValue(4);
		
		
		IlpSolver.purifyLog(combinedList, abMapx);
	
		
		if (abMapx.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Unable to Generate Log!");
			return false;
		}
		addCorrelatedConditions();
	//	CheckforPrecendence();
		MinerFulLogMaker logMak = new MinerFulLogMaker(logMakParameters);		
		ProcessModel proMod = null;  
		//CheckforPrecendence();
		proMod =newStyleLog();// MinerfulLogGenerator.fromDeclareMapToMinerfulProcessModel(model,null, combinedList, abMapx);
	 
		//addCorrelatedConditions();
		//DeclareModelGenerator dm = new DeclareModelGenerator(model,abMapx);
		//proMod=	dm.generateModel();
		
	    XLog xlog=	logMak.createLog(proMod);
	   // twist(xlog);
	    ProcessLog(xlog);
	    /* GetRandomSelection(xlog);
	   CreateGraphofLog(xlog);
	   CombineSelectedLog();*/
	   addCorrelationtoArray();
	   mergeLists();
	  // CheckForSameList(xlog);
	   //SetMappedILPCondition();
	   
	    //twistcopy();
	    //twistRandomSelection();
	   	    
	   // IlpSolver.CheckIlpConditions(abMap2);
	   
	    RestoreKeys(xlog);
	   
	    // LogService.printLog(xlog);
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
	
	
	private static void SetMappedILPCondition() {
		// TODO Auto-generated method stub
		
		for (Entry<String, Alphabet> activity : abMapAll.entrySet()) {
			Alphabet tc = activity.getValue();
			
			if (tc.isSingle){
				System.out.println("Key..:" +activity.getKey() + " Condition: "+
						tc.fullCondition + " : mapped:" + tc.secondAlphabet);
			}else
			{
				System.out.println("Key:" +activity.getKey() + " Condition: "+
						tc.fullCondition + " : mapped:" + tc.secondAlphabetKey  );
			}
	
		}
		/*
		for (Entry<Integer, LogAlphabet> activity : logMap.entrySet()) {
			int k = activity.getKey();
			LogAlphabet tc = activity.getValue();
			TraceAlphabet key = traceMap.get(k);
			if (tc.isFirstKey == true) {
				System.out.println("key" + key.alphabetKey + " event : " + k + ":Trace:" +tc.traceNo);
				//ArrayList<Integer> dfsList = new ArrayList<Integer>();
				String condition = "";
				for (int i = 0; i < tc.eventLists.size(); i++) {
						TraceAlphabet tc2 = traceMap.get(tc.eventLists.get(i));
						Alphabet ab = abMapAll.get(tc2.alphabetKey);
						
						if (condition.isEmpty()){
							condition = condition +"::";
						}
						if (tc2.isFirstKey) {
							System.out.println("Related Key: "+ tc2.alphabetKey +":" 	+ tc.eventLists.get(i));
							if(ab!=null && ab.actCondition != null){
							condition = condition +  ab.actCondition.split("::")[0];
							System.out.println("acc.Cond" + ab.actCondition);
							}
						} else {
							
							System.out.println("DFS Trace: "+ tc2.alphabetKey +":" 	+ tc.eventLists.get(i));
							if(ab!=null && ab.relCondition != null)
							{
								condition = condition + ab.relCondition;
								System.out.println("corr.Cond" + ab.actCondition);
							}
								
						}
					}
				System.out.println("full condition: "+ condition);
			}
		}*/		
	}


	private static void CombineSelectedLog() {
		System.out.println("--------------Log Graph Combinations------------------------");
		int count = 0;
		int loopcheck = 0;
		for (Entry<Integer, LogAlphabet> activity : logMap.entrySet()) {
			int k = activity.getKey();
			LogAlphabet tc = activity.getValue();
			//System.out.println("Trace:" + tc.traceNo + " event : " + k);
			if (tc.isFirstKey == true) {
				for (int i = 0; i < tc.eventLists.size(); i++) {
					if (tc.eventLists.get(i) > k) {
							TraceAlphabet tc2 = traceMap.get(tc.eventLists.get(i));
							if (tc2.isFirstKey) {
								tc.eventLists.remove(tc.eventLists.get(i));
							}
						} else {
							tc.eventLists.remove(tc.eventLists.get(i));
						}
					

				}
				logMap.put(k, tc);

			}
		}
		// remove duplications
		for (Entry<Integer, LogAlphabet> activity : logMap.entrySet()) {
			int k = activity.getKey();
			LogAlphabet tc = activity.getValue();
			TraceAlphabet key = traceMap.get(k);
			if (tc.isFirstKey == true) {
				ArrayList<Integer> dfsList = new ArrayList<Integer>();
				dfsList.clear();
				for (int i = 0; i < tc.eventLists.size(); i++) {
					if (tc.eventLists.get(i) > k) {
						TraceAlphabet tc2 = traceMap.get(tc.eventLists.get(i));
						dfsList.add(tc.eventLists.get(i));
					}
				}
				tc.eventLists = dfsList;
				logMap.put(k, tc);
			}
		}
		
		//print combinations

	}
	
	
	public static ArrayList<Integer> GetMapppedList(int envetNo){
		 ArrayList<Integer> dfsList = new ArrayList<Integer>();
		 dfsList.clear();	
		LogAlphabet tc = logMap.get(envetNo);
			if (tc==null){
		//		System.out.println(" No Connected Events: "+ envetNo );
			return dfsList;	
			}
			TraceAlphabet key = traceMap.get(envetNo);
			
			 if (tc.isFirstKey == true) {
				System.out.println("key" + key.alphabetKey + " event : " + envetNo + ":Trace:" +tc.traceNo);
			
				String ilpCondition = "";
				 for (int i = 0; i < tc.eventLists.size(); i++) {
				//	if (tc.eventLists.get(i) > k) {
					if 	(!ilpCondition.isEmpty()){
						ilpCondition =ilpCondition +"::";	
					}
					 TraceAlphabet tc2 = traceMap.get(tc.eventLists.get(i));
					 
				//		dfsList.add(tc.eventLists.get(i));
						if (tc2.isFirstKey) {
						
							//	tc.eventLists.remove(tc.eventLists.get(i));
							System.out.println("Related Key: "+ tc2.alphabetKey +":" 	+ tc.eventLists.get(i));
						} else {
							dfsList.add(tc.eventLists.get(i));
							System.out.println("Connected Events: "+ tc2.alphabetKey +":" 	+ tc.eventLists.get(i));
						}
					}			
			}	
			 return dfsList;
	}
	
	
	private static void CheckForSameList(XLog xlog) {
		int eventnumber =0;
	    int traceno =0;
	   int check =0;
	   ArrayList<String> sameEvents = new ArrayList<String>();
	    for (XTrace xtrace : xlog) {
			ArrayList<Integer> traceLog = new ArrayList<Integer>();
			ArrayList<String> traceEvents = new ArrayList<String>();
			
			traceEvents.clear();
			traceLog.clear();
			for (XEvent event : xtrace) {
				traceLog.add(eventnumber);
				eventnumber++;
			}

			for (int i = 0; i < traceLog.size(); i++) {
				ArrayList<Integer> mappedList = new ArrayList<Integer>();

				int event = traceLog.get(i);
				TraceAlphabet tc2 = traceMap.get(event);

			//	if (tc2.isFirstKey) {
					mappedList = GetMapppedList(event);
						if(!mappedList.isEmpty()){
							for (int i2 = i + 1; i2 < traceLog.size(); i2++) {
							//	System.out.println("Mappedex:" + tc2.alphabetKey + ":"+tc2.Mappedkeys);
								TraceAlphabet tc = traceMap.get(event);
								if (tc.Mappedkeys != null) {
									ArrayList<Integer> mappedList2 = new ArrayList<Integer>();
									int event2 = traceLog.get(i2);
									mappedList2 = GetMapppedList(event2);
									if (!mappedList2.isEmpty() && checkforRepeart(mappedList, mappedList2)) {
										sameEvents.add(event + "::" + event2);

									}

								}

						//	}

						}
							
						}
				

			}

			

			check = 0;
			traceno++;
		}		

	    
	    for (int i = 0; i < sameEvents.size(); i++) {
			System.out.println("Similar Events with:" + sameEvents.get(i));
			String[] ex =  sameEvents.get(i).split("::");
			
			for (int count =0; count < ex.length; count++){
				TraceAlphabet tc = traceMap2.get(Integer.parseInt(ex[count]));
				System.out.println("xxx souce:"+ex[count]);
				if (tc != null){
					if (tc.ilpSelectedList != null) {					
							for (int t = 0; t < tc.ilpSelectedList.size(); t++) {
							System.out.println("xxx"+tc.ilpSelectedList.get(t));
							}
						}		
				}	
			}
			
		}
	}

	private static void CreateGraphofLog(XLog xlog) {
		// TODO Auto-generated method stub
		
		int trace  =0;
		int check = 0;
		for (Entry<Integer, TraceAlphabet> activity : traceMap2.entrySet()) {
			int k = activity.getKey();
			TraceAlphabet tc = activity.getValue();
			Map<Integer,List<Integer>> ilpIndex = new HashMap<Integer,List<Integer>>();
		//	System.out.println("Source:" + tc.alphabetKey);
			if (tc.Mappedkeys != null) {
				Graph2 graph2 = new Graph2(600);
				for (int i = 0; i < tc.Mappedkeys.size(); i++) {
					graph2.addEdge(k, tc.MappedKeysIndex.get(i));
				/*	System.out.println("Key : " + tc.Mappedkeys.get(i)
							+ " index: " + tc.MappedKeysIndex.get(i));*/
				}
				
			
		//	System.out.println(" DFS: "+	graph2.DFS(k));
			ArrayList<Integer> dfsList = new ArrayList<Integer>();
			dfsList = graph2.DFS(k);
			ilpIndex.put(k,dfsList);
			//TraceAlphabet tc2 = new TraceAlphabet();
			tc.ilpIndex = ilpIndex;
			dfsList.remove(0);
			tc.ilpSelectedList =dfsList;
			tc.ilpList = dfsList;
			traceMap2.put(k, tc);
			}
		}	
		// - trace action
		
		int eventnumber =0;
	    int traceno =0;
	    check =0;
	   
	    for (XTrace xtrace : xlog) {
	    	 ArrayList<Integer> traceLog = new ArrayList<Integer>();
	 	    ArrayList<String> traceEvents = new ArrayList<String>();
	    	for (XEvent event : xtrace) {
				TraceAlphabet tc = traceMap2.get(eventnumber);
				if (tc != null){
					if (tc.ilpSelectedList != null) {
						traceLog.add(eventnumber);
							for (int i = 0; i < tc.ilpSelectedList.size(); i++) {
								traceEvents.add(eventnumber +"::"+tc.ilpSelectedList.get(i) );
							}
						}		
				}
				eventnumber++;
			}
			
			
			Set<Object> strSet = Arrays.stream(traceLog.toArray())
					.collect(Collectors.toSet());
			traceLog.clear();

			for (Object s : strSet) {
				traceLog.add((Integer) s);
			}
			for (int l = 0; l < traceLog.size(); l++) {						
				TraceAlphabet tb = traceMap.get(traceLog.get(l));
			//	if (tb.alphabetKey.substring(0,1).equals("A")){
				LogAlphabet logalphabet = new LogAlphabet();
				logalphabet.eventNo = tb.eventNo;
				logalphabet.traceNo = tb.traceNo;
				Graph2 g33 = new Graph2(600);
				ArrayList<Integer> eventList = new ArrayList<Integer>();
				eventList.clear();
				for (int l2 = 0; l2 < traceEvents.size(); l2++) {
					String[] jj = traceEvents.get(l2).split("::");
					g33.addEdge(Integer.parseInt(jj[0]),
							Integer.parseInt(jj[1]));

				}

				for (int l3 = 0; l3 < traceEvents.size(); l3++) {
					String[] jj = traceEvents.get(l3).split("::");
					g33.addEdge(Integer.parseInt(jj[1]),
							Integer.parseInt(jj[0]));
				}
				eventList = g33.DFS(traceLog.get(l));				
				logalphabet.eventLists = eventList;
				
				if (tb.isFirstKey) {
					logalphabet.isFirstKey = true;
					System.out.println("Trace:"+ traceno);
					System.out.println("LogEvent:"+ traceLog.get(l));
					System.out.println("LogMapped:"+ eventList.toString());
					
				} else {
					logalphabet.isFirstKey = false;
				}
				logMap.put(traceLog.get(l), logalphabet);
			}
			
			traceEvents.clear();
			traceLog.clear();
			check = 0;
			traceno++;
		}		
	}


	private static boolean checkforRepeart(ArrayList<Integer> soucelist,ArrayList<Integer> checkingList) {
		// checkingList.remove(0);
		Collections.sort(soucelist);
		Collections.sort(checkingList);

		return soucelist.equals(checkingList) ? true : false;
	}


	public static void GetRandomSelection(XLog xlog) {
		// TODO Auto-generated method stub
		
		
		ArrayList<String> TraceIndex = new ArrayList<String>();
		for (Entry<Integer, TraceAlphabet> activity : traceMap2.entrySet()) {
			int k = activity.getKey();
			TraceAlphabet tc = activity.getValue();
			
			 Map<String, Map<String, ArrayList<Integer>>> targetedListwithIndex = tc.targetedListwithIndex;
				ArrayList<Integer> MappedIndex = new ArrayList<Integer>();
				ArrayList<String> MappedKey = new ArrayList<String>();
			 TraceIndex.clear();
			for (String key : targetedListwithIndex.keySet()) {
				Map<String, ArrayList<Integer>> abc = targetedListwithIndex
						.get(key);
				System.out.println("source " +  key + ":" + tc.eventNo +" Targets") ;
				
				for (String key2 : abc.keySet()) {
					ArrayList<Integer> ind = abc.get(key2);
					ArrayList<String> temp = new ArrayList<String>();
					ArrayList<Integer> tempind = new ArrayList<Integer>();
					temp.clear();
					tempind.clear();
					//System.out.println("Key2:" + key2);
					for (int j = 0; j < ind.size(); j++) {
						
					 TraceAlphabet tc2 =	traceMap.get(ind.get(j));
						temp.add(tc2.alphabetKey );
						tempind.add(ind.get(j));
						
						System.out.println("j " + j+":" + tc2.alphabetKey  + ":" + ind.get(j).toString());
					}

					if (!temp.isEmpty()) {
						int random = selectRandom(temp.size());
						MappedKey.add(temp.get(random));
						MappedIndex.add(tempind.get(random));
						System.out.println("Random :" + temp.get(random) + ":" + tempind.get(random));

					}else{
					System.out.println("XX: "+ key2);	
					}
				}
				tc.Mappedkeys = MappedKey;
				tc.MappedKeysIndex = MappedIndex;
			
				traceMap2.put(k, tc);
				System.out.println(""); 
			}
			//System.out.println("Stop");
		}
	
		
		
	}


	private static void ProcessLog(XLog xlog) {
	
		try {



		ArrayList<String> sourceList = new ArrayList<String>();
		ArrayList<String> targetList = new ArrayList<String>();
		ArrayList<String> traceList = new ArrayList<String>();
	//	ArrayList<Integer> targetIndex = new ArrayList<Integer>();
		
		int count =0;
		CheckforPrecendence();
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
				sourceList.add(activity.getKey());
				constrainList.add(activity.getValue().constrain);
			//	System.out.println("Constrain : " + activity.getValue().constrain);
			}			
		
		traceMap.clear();
	//	System.out.println("-----Printing log start -----");
		int eventnumber =0;
	    int traceno =0;
	    int check =0;
	    for (XTrace xtrace : xlog) {
			
			String traceName = XConceptExtension.instance().extractName(
					xtrace);
			traceList.clear();
			
			System.out.println("TraceName: " + traceName.substring(traceName.lastIndexOf(".",traceName.length())).trim());
			for (XEvent event : xtrace) {
				TraceAlphabet tc = new TraceAlphabet();
				String activityName = XConceptExtension.instance()
						.extractName(event);
				traceList.add(DeclareLogGenerator.getAlphabetKey(activityName));				
				System.out.println("Char : "+ activityName+ " Key: " +	DeclareLogGenerator.getAlphabetKey(activityName)+ " Event :" + eventnumber);					
				String newKey = DeclareLogGenerator.getAlphabetKey(activityName);
				tc.alphabetKey = newKey;
				tc.alphabetKey = DeclareLogGenerator.getAlphabetKey(activityName);
				tc.eventNo = eventnumber;
				tc.traceNo = traceno;
				tc.isFirstKey =  ((sourceList.indexOf(DeclareLogGenerator.getAlphabetKey(activityName)) > -1));
			    tc.isMapped =false;
				int indx = sourceList.indexOf(newKey);
				if (indx > -1)
				tc.constrain =  constrainList.get(indx);
				traceMap.put(eventnumber, tc);									
				eventnumber++;
			}
			check=0;
			traceno++;
		}
			
		// processing log ....
		
		ArrayList<Integer> traceIndex = new ArrayList<Integer>();
		ArrayList<String> traceName= new ArrayList<String>();
	
		ArrayList<String> isAdded = new ArrayList<String>();
	//	List<TraceAlphabet> tc = new ArrayList<TraceAlphabet>();
	//	TraceAlphabet xx = new TraceAlphabet();
//		tc.add(xx);	
		//	traceDuplicates
	//	System.out.println("...Log Processing....");
		int traceNo=0;
		traceList.clear();
		for (Entry<Integer, TraceAlphabet> activity : traceMap.entrySet()) {
			Integer k = activity.getKey();
			TraceAlphabet bb = activity.getValue();
		//	System.out.println("event: "+bb.eventNo + " key :" +  bb.alphabetKey + ":" + bb.constrain);
			if (traceNo != bb.traceNo){
				System.out.println("Trace No:  " + (traceNo));
				for (int i=0; i <traceList.size();i++ )
				{
					
					if (sourceList.indexOf(traceList.get(i)) != -1){
					targetList.clear();
					TraceAlphabet traceEvent = traceMap.get(traceIndex.get(i));
				//	Graph g = new Graph(600);
					traceEvent.traceNo = traceNo;
					traceEvent.alphabetKey  =traceList.get(i);
					traceEvent.selectedSource =traceList.get(i); 
					traceEvent.sourceIndex = traceIndex.get(i);
					traceEvent.isMapped =true; 
					traceEvent.isFirstKey =  ((sourceList.indexOf(traceList.get(i)) > -1));
					targetList=  getCorr(traceList.get(i));
					ArrayList<String> temp = new ArrayList<String>();
					ArrayList<Integer> temp2 = new ArrayList<Integer>();
					temp2.clear();temp.clear();
					temp = getmatchList(traceList,targetList, i,traceIndex,traceList.get(i),temp2);
			//		System.out.println("Source : " + traceList.get(i) + "(" +traceIndex.get(i) +")");
					if (!temp.isEmpty()) {
						traceEvent.targetList=	temp;
						/*traceEvent.targetList=	getmatchList(traceList,targetList, i,traceIndex,traceList.get(i));
						for (int w=0; w < temp.size(); w++){
							temp2.add(traceIndex.get(w));
						}*/
					traceEvent.targetListIndex = temp2;	
					
					
			 Map<String, Map<String, ArrayList<Integer>>> targetedListwithIndex =new  HashMap<String, Map<String, ArrayList<Integer>>>(); ;
			 Map<String, ArrayList<Integer>>  targetMap = new HashMap<String, ArrayList<Integer>>();
						//	if (isAdded.indexOf(traceList.get(i)) == -1) {
								isAdded.add(traceList.get(i));
								isAdded.clear();
								for (int ww = 0; ww < temp.size(); ww++) {
									String key =  temp.get(ww);
									ArrayList<Integer> traces =  new ArrayList<Integer>();
								if (isAdded.indexOf(key) == -1) {
									isAdded.add(key);
									for (int t = i + 1; t < traceList.size(); t++) {
										if (traceList.get(t).contains("_")) {
											if (key.equals(traceList.get(t))) {
												traces.add(traceIndex.get(t));
											} else {
												String keys[] = traceList.get(t).split("_");
												if (key.equals(keys[0])
														|| key.equals(keys[1])) {
													traces.add(traceIndex
															.get(t));
												}

											}
										}
										else
									/*	if (key.contains("_")) {
											String keys[] = key.split("_");
											for (int ky = 0; ky < keys.length; ky++) {
												if (keys[ky].equals(traceList
														.get(t))) {
													traces.add(traceIndex
															.get(t)); break;
												}
											}

										} else {*/

											if (key.equals(traceList.get(t))) {
												traces.add(traceIndex.get(t));
											}
									//	}
									}
									targetMap.put(key.substring(0,1), traces);
								}
							}
								/*
								for (int jk = 0; jk < temp2.size(); jk++) {
									 ArrayList<Integer> test =  new ArrayList<Integer>();
									g.addEdge(traceIndex.get(i), temp2.get(jk));
									g.addEdge(temp2.get(jk), traceIndex.get(i));
									test.add(temp2.get(jk));
									for (int lc=i; lc <traceList.size(); lc++){
										if(traceList.get(lc).equals(temp2.get(jk))){
											g.addEdge(traceIndex.get(lc), temp2.get(jk));
											g.addEdge(temp2.get(jk), traceIndex.get(lc));
											test.add(traceIndex.get(lc));
										}
									}
									targetMap.put(temp.get(jk),test);	
								}
								ArrayList<Integer> dfs = new ArrayList<Integer>();
								ArrayList<Integer> ilp = new ArrayList<Integer>();
								dfs = g.DFS(traceIndex.get(i));
								
								for (int df=0;df< dfs.size(); df++){
									int srch = traceIndex.indexOf(dfs.get(df));
									if (sourceList.indexOf(traceList.get(srch)) == -1){
										ilp.add(dfs.get(df));
									}	
									
								}*/
								
							//	CheckforRando(dfs,traceIndex,traceList);
								 targetedListwithIndex.put(traceList.get(i), targetMap);
								 traceEvent.targetedListwithIndex =targetedListwithIndex;
							//	 traceEvent.ilpList = dfs;
							//	traceEvent.ilpSelectedList = ilp;
							//	System.out.println("checking for " 	+ traceIndex.get(i));
							//	System.out.println(g.DFS(traceIndex.get(i)));
								//System.out.println(ilp.toString());
								
						//	}
						}
	//				tc.add(traceEvent);
			//	System.out.println("Duplicates: " +traceIndex.get(i) + ":" + traceList.get(i) + temp2.toString() + temp.toString());
				traceMap2.put(traceIndex.get(i), traceEvent);	
				//traceMap.put(traceIndex.get(i), traceEvent);
					}
				}
				//if (!tc.isEmpty())
			//	traceDuplicates.put(traceNo, tc);
		//		tc.clear();
				//getEventList(traceIndex,sourceList,traceList,traceNo);
			//	traceNo = k;
				traceNo = bb.traceNo;
//				targetIndex.clear();
				traceIndex.clear();
				traceList.clear();
				isAdded.clear();
				//traceNo ;
			}
//			targetIndex.add(bb.targetIndex);
			traceIndex.add(k);
			traceList.add(bb.alphabetKey);			
		}	


		for (int iX=0; iX <traceList.size();iX++ )
		{
			if (sourceList.indexOf(traceList.get(iX)) != -1){
			targetList.clear();
			TraceAlphabet traceEvent = traceMap.get(traceIndex.get(iX));
			Graph g = new Graph(600);
			//	traceEvent.traceNo = traceNo;
				traceEvent.alphabetKey  =traceList.get(iX);
				traceEvent.selectedSource =traceList.get(iX); 
				traceEvent.sourceIndex = traceIndex.get(iX);
				traceEvent.isMapped =true;
				traceEvent.isFirstKey =  ((sourceList.indexOf(traceList.get(iX)) > -1));
			targetList=  getCorr(traceList.get(iX));
			ArrayList<String> temp = new ArrayList<String>();
			ArrayList<Integer> temp2 = new ArrayList<Integer>();
			temp2.clear();temp.clear();
			temp = getmatchList(traceList,targetList, iX,traceIndex,traceList.get(iX),temp2);
	//		System.out.println("Source : " + traceList.get(iX) + "(" +traceIndex.get(iX) +")");
			if (!temp.isEmpty()) {
				traceEvent.targetList=	temp;
				traceEvent.targetListIndex = temp2;	
					Map<String, Map<String, ArrayList<Integer>>> targetedListwithIndex = new HashMap<String, Map<String, ArrayList<Integer>>>();
					;
					Map<String, ArrayList<Integer>> targetMap = new HashMap<String, ArrayList<Integer>>();
					// if (isAdded.indexOf(traceList.get(i)) == -1) {
					//isAdded.add(traceList.get(i));
					isAdded.clear();
					for (int ww = 0; ww < temp.size(); ww++) {
						String key = temp.get(ww);
						ArrayList<Integer> traces = new ArrayList<Integer>();
						if (isAdded.indexOf(key) == -1) {
							isAdded.add(key);
							for (int t = iX+1; t < traceList.size(); t++) {
								if (traceList.get(t).contains("_")) {
									if (key.equals(traceList.get(t))) {
										traces.add(traceIndex.get(t));
									} else {
										String keys[] = traceList.get(t).split("_");
										if (key.equals(keys[0])
												|| key.equals(keys[1])) {
											traces.add(traceIndex
													.get(t));
										}

									}
								}
								else
/*								if (key.contains("_")) {
									String keys[] = key.split("_");
									for (int ky = 0; ky < keys.length; ky++) {
										if (keys[ky].equals(traceList
												.get(t))) {
											traces.add(traceIndex
													.get(t)); break;
										}
									}

								} else {*/

									if (key.equals(traceList.get(t))) {
										traces.add(traceIndex.get(t));
									}
								//}

							}
							targetMap.put(key.substring(0,1), traces);
						}
					}

/*					//if (isAdded.indexOf(traceList.get(iX)) == -1) {
						isAdded.add(traceList.get(iX));
						for (int jk = 0; jk < temp2.size(); jk++) {
							g.addEdge(traceIndex.get(iX), temp2.get(jk));
							g.addEdge(temp2.get(jk), traceIndex.get(iX));
							
							for (int lc=iX; lc <traceList.size(); lc++){
								if(traceList.get(lc).equals(traceList.get(iX))){
									g.addEdge(traceIndex.get(lc), temp2.get(jk));
									g.addEdge(temp2.get(jk), traceIndex.get(lc));
								}
							}
						}
						
						ArrayList<Integer> dfs = new ArrayList<Integer>();
						ArrayList<Integer> ilp = new ArrayList<Integer>();
						dfs = g.DFS(traceIndex.get(iX));
						
						for (int df=0;df< dfs.size(); df++){
							int srch = traceIndex.indexOf(dfs.get(df));
							System.out.println(traceList.get(srch) + ":"+dfs.get(df));
							if (sourceList.indexOf(traceList.get(srch)) == -1){
								ilp.add(dfs.get(df));
							}	
							
						}
*/					//	System.out.println("checking for " + traceIndex.get(iX));
					//	System.out.println(g.DFS(traceIndex.get(iX)));
						 targetedListwithIndex.put(traceList.get(iX), targetMap);
						 traceEvent.targetedListwithIndex =targetedListwithIndex;
//						System.out.println(ilp.toString());
					}
			traceMap2.put(traceIndex.get(iX), traceEvent);
				}
			//}
		}
		} catch (Exception e){

		    // Deal with e as you please.
		    //e may be any type of exception at all.

		}
	
		
	}


	private static void CheckforRando(ArrayList<Integer> dfs,
			ArrayList<Integer> traceIndex, ArrayList<String> traceList) {
		// TODO Auto-generated method stub
		ArrayList<String> Added = new ArrayList<String>();
		ArrayList<Integer> AddedIndex = new ArrayList<Integer>();
		ArrayList<Integer> ret = new ArrayList<Integer>();

		  
		for (int i = 0; i < dfs.size(); i++) {
			int ser = traceIndex.get(dfs.get(i));
			String srch = traceList.get(ser);
			if (!Arrays.asList(Added).contains(srch)) {
				Added.add(srch);
			for (int j = i ; j < dfs.size(); j++) {				
				if (!Arrays.asList(Added).contains("c")) {
					
					AddedIndex.add(ser);
				}
			}
			

			if (Added.size() > 0) {
				int random = selectRandom(Added.size());
				ret.add(dfs.get(random));
			}
			}
		}
		
		System.out.println("" +ret);
	}


	private static void mergeLists() {
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet ret = activity.getValue();
			abMapAll.put(k, ret);
			//System.out.println(" Key " + k + " Condition : "+ ret.actCondition);
		}	
		for (Entry<String, Alphabet> activity : abMap2.entrySet()) {
			String k = activity.getKey();
			Alphabet ret = activity.getValue();
			abMapAll.put(k, ret);
		//	System.out.println(" Key " + k + " Condition : "+ ret.actCondition);
		}
		
	}


	private static void RestoreKeys(XLog xlog) {
	int count =0;
	int check =0;
	int eventnumber=0;
		for (XTrace xtrace : xlog) {
				for (XEvent event : xtrace) {				
					//event.getAttributes().
				
					XAttributeMap eventAttributes = event.getAttributes();
					String name = "?";
					int local=0;
					for (String key : eventAttributes.keySet()) {
						if (local==0){
							name= 	eventAttributes.get(key).toString();
						}
					
						/*String value = eventAttributes.get(key).toString();
						String ww = eventAttributes.get(key).getKey();
						System.out.println(" eventAttributes key: " + key + "  value: " + value + " ww: " + ww);*/
						local++;
					}
										
					String keyName = 	getAlphabetKey(name);
					String paylaodName = "Data";
					int keyValue = (int) Math.random();
					Alphabet ExeActivity = abMapAll.get(keyName);
					if (ExeActivity !=null){
						paylaodName	 = ExeActivity.payLoadName;
						keyValue= getRandomTrace(ExeActivity.minValue,ExeActivity.maxValue);
					}
					
					if (paylaodName ==null || paylaodName.isEmpty())
					{ paylaodName="Data";}
					XConceptExtension.instance().assignName(event,keyName);
					 XFactory xFactory = XFactoryRegistry.instance().currentDefault();
						XAttribute test = xFactory.createAttributeLiteral(paylaodName,Integer.toString(keyValue), null);
						XAttributeMap test2 = event.getAttributes();
						test2.put(paylaodName, test);
						event.setAttributes(test2);
						//xtrace.set(check, event);
						//check++;
					xtrace.set(check, event);
					check++;
					
					eventnumber++;	
				}
				check=0;
				
				
			}
			
	}


	private static void addCorrelationtoArray() {
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet ret = activity.getValue();

			if (ret != null){
				if(ret.correlationlist != null){
					for(int i=0; i < ret.correlationlist.length; i++){
					  String key = ret.correlationlist[i];
					  ret.isActivated = false;
					  String condition = ret.actCondition + "::" + ret.relCondition;
					  /*  System.out.println("-----------:( "+key+ " ):-----------------");
					  
					  System.out.println("actCondition: "+ ret.actCondition );
					  System.out.println("Corelation: "+ ret.relCondition );
					  System.out.println("Key: "+ key + " Codition : " + condition );*/
					  ret.actCondition = condition;
					  abMap2.put(key, ret);
				  }	
				}
			}
		}		
	}


	public static ArrayList<String> getCorr(String key){
		ArrayList<String> targetList = new ArrayList<String>();	
		
		Alphabet ret = abMapx.get(key);
		if (ret != null){
			if(ret.correlationlist != null){
				targetList.clear();
				for(int i=0; i < ret.correlationlist.length; i++){
				  targetList.add(ret.correlationlist[i]);
				/*  String alphabetname = BranchCombination.getParentLetter(ret.correlationlist[i]);
					String blist[] = ret.secondAlphabet.split("::");*/
			/*	if (blist.length>1){
					for (int ndm =0; ndm <blist.length; ndm++){
						if (!alphabetname.equals("")){
						if(!blist[ndm].equals(alphabetname)){
						//	System.out.println(ret.correlationlist[i].replace(alphabetname, blist[ndm]));
							targetList.add(ret.correlationlist[i].replace(alphabetname, blist[ndm]));							
						}			}
					}
					}*/
			  }	
			}
		}
		return  targetList;
	}
	
	public static String printme(ArrayList<String> sourceList,ArrayList<String> targetList,
 ArrayList<String> trace, String key,
			int start, int eventnumber) {
		
		String ret = "";
		ArrayList<String> trace2 = new ArrayList<String>();
		for (int kk = start; kk < trace.size(); kk++) {
			trace2.add(trace.get(kk));
		}
		int index = sourceList.indexOf(key);
		//System.out.println("Combination : Source : " + sourceList.get(index) + "("+
			//	eventnumber+ ")" );
		
	//	for (int b = 0; b < trace2.size(); b++) {
			//System.out.println("--");
			
			if (index > -1) {				
				//System.out.println("Log  A = " + sourceList.get(index));
			
				for (int bb = 0; bb < trace2.size(); bb++) {
					if (targetList.indexOf(trace2.get(bb)) > -1) {
						int ndx = trace2.indexOf(trace2.get(bb));
						TraceAlphabet tcx = traceMap.get(eventnumber +ndx);
						tcx.isMapped = true;
						tcx.isFirstKey = false;
						tcx.selectedSource = sourceList.get(index);
						tcx.selectedTarget =  trace2.get(bb);
						tcx.sourceIndex = eventnumber;
						tcx.targetIndex = (eventnumber +ndx);
						ret = trace2.get(bb);
						tcx.constrain = constrainList.get(index);
						traceMap.put(eventnumber +ndx, tcx);
					//	indexList.add((eventnumber +ndx));
					//	System.out.println("Target " + trace2.get(bb) +"("+ (eventnumber +ndx)+")" );
						break;
						//	System.out.println("Log B : " + trace.get(bb) + " : "+ bb + " : Event number :" + (eventnumber - b));
					}					
				}				
				//break;
		//	}
			//return ret;
		}
		return ret;
			
	}
	public static void twist(XLog xlog) {
		/*try {*/

			ArrayList<String> sourceList = new ArrayList<String>();
			ArrayList<String> targetList = new ArrayList<String>();
			ArrayList<String> traceList = new ArrayList<String>();
		//	ArrayList<Integer> targetIndex = new ArrayList<Integer>();
			
			int count =0;
			CheckforPrecendence();
			for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
					sourceList.add(activity.getKey());
					constrainList.add(activity.getValue().constrain);
					System.out.println("Constrain : " + activity.getValue().constrain);
				}			
			
			traceMap.clear();
			System.out.println("-----Printing log start -----");
			int eventnumber =0;
		    int traceno =0;
		    int check =0;
		    for (XTrace xtrace : xlog) {
				
				String traceName = XConceptExtension.instance().extractName(
						xtrace);
				traceList.clear();
				
				System.out.println("TraceName: " + traceName.substring(traceName.lastIndexOf(".",traceName.length())).trim());
				for (XEvent event : xtrace) {
					TraceAlphabet tc = new TraceAlphabet();
					String activityName = XConceptExtension.instance()
							.extractName(event);
					traceList.add(DeclareLogGenerator.getAlphabetKey(activityName));				
					System.out.println("Char : "+ activityName+ " Key: " +	DeclareLogGenerator.getAlphabetKey(activityName)+ " Event :" + eventnumber);					
					String newKey = DeclareLogGenerator.getAlphabetKey(activityName);
					tc.alphabetKey = newKey;
					tc.alphabetKey = DeclareLogGenerator.getAlphabetKey(activityName);
					tc.eventNo = eventnumber;
					tc.traceNo = traceno;
					tc.isFirstKey = true;
					tc.isMapped =false;
					int indx = sourceList.indexOf(newKey);
					if (indx > -1)
					tc.constrain =  constrainList.get(indx);
					traceMap.put(eventnumber, tc);									
					eventnumber++;
				}
				check=0;
				traceno++;
			}
				
			// processing log ....
			
			ArrayList<Integer> traceIndex = new ArrayList<Integer>();
			ArrayList<String> traceName= new ArrayList<String>();
			
		//	List<TraceAlphabet> tc = new ArrayList<TraceAlphabet>();
		//	TraceAlphabet xx = new TraceAlphabet();
	//		tc.add(xx);	
			//	traceDuplicates
			System.out.println("...Log Processing....");
			int traceNo=0;
			traceList.clear();
			for (Entry<Integer, TraceAlphabet> activity : traceMap.entrySet()) {
				Integer k = activity.getKey();
				TraceAlphabet bb = activity.getValue();
			//	System.out.println("event: "+bb.eventNo + " key :" +  bb.alphabetKey + ":" + bb.constrain);
				if (traceNo != bb.traceNo){
					System.out.println("Duplication in Trace:  " + (traceNo));
					for (int i=0; i <traceList.size();i++ )
					{
						if (sourceList.indexOf(traceList.get(i)) != -1){
						targetList.clear();
						TraceAlphabet eEvent = new TraceAlphabet();
						eEvent.traceNo = traceNo;
						eEvent.alphabetKey  =traceList.get(i);
						eEvent.selectedSource =traceList.get(i); 
						eEvent.sourceIndex = traceIndex.get(i);
						eEvent.isMapped =true;
						targetList=  getCorr(traceList.get(i));
						ArrayList<String> temp = new ArrayList<String>();
						ArrayList<Integer> temp2 = new ArrayList<Integer>();
						temp2.clear();temp.clear();
						temp = getmatchList(traceList,targetList, i,traceIndex,traceList.get(i),temp2);
				//		System.out.println("Source : " + traceList.get(i) + "(" +traceIndex.get(i) +")");
						if (!temp.isEmpty()) {
							eEvent.targetList=	temp;
							/*traceEvent.targetList=	getmatchList(traceList,targetList, i,traceIndex,traceList.get(i));
							for (int w=0; w < temp.size(); w++){
								temp2.add(traceIndex.get(w));
							}*/
							eEvent.targetListIndex = temp2;	
							
							
							
							
							
						}
						
		//				tc.add(traceEvent);
				//	System.out.println("Duplicates: " +traceIndex.get(i) + ":" + traceList.get(i) + temp2.toString() + temp.toString());
					traceMap2.put(traceIndex.get(i), eEvent);	
					//traceMap.put(traceIndex.get(i), traceEvent);
						}
					}
					//if (!tc.isEmpty())
				//	traceDuplicates.put(traceNo, tc);
			//		tc.clear();
					//getEventList(traceIndex,sourceList,traceList,traceNo);
				//	traceNo = k;
					traceNo = bb.traceNo;
//					targetIndex.clear();
					traceIndex.clear();
					traceList.clear();
					
					//traceNo ;
				}
	//			targetIndex.add(bb.targetIndex);
				traceIndex.add(k);
				traceList.add(bb.alphabetKey);			
			}	
			//getEventList(traceIndex,sourceList,traceList,traceNo);
		//traceNo++;
	//		System.out.println("Duplication in Last Trace:  " + (traceNo));
			//tc.clear();
			for (int iX=0; iX <traceList.size();iX++ )
			{
				if (sourceList.indexOf(traceList.get(iX)) != -1){
				targetList.clear();
				TraceAlphabet traceEvent = new TraceAlphabet();// traceMap.get(traceIndex.get(iX));
					traceEvent.traceNo = traceNo;
					traceEvent.alphabetKey  =traceList.get(iX);
					traceEvent.selectedSource =traceList.get(iX); 
					traceEvent.sourceIndex = traceIndex.get(iX);
					traceEvent.isMapped =true;
				targetList=  getCorr(traceList.get(iX));
				ArrayList<String> temp = new ArrayList<String>();
				ArrayList<Integer> temp2 = new ArrayList<Integer>();
				temp2.clear();temp.clear();
				temp = getmatchList(traceList,targetList, iX,traceIndex,traceList.get(iX),temp2);
		//		System.out.println("Source : " + traceList.get(iX) + "(" +traceIndex.get(iX) +")");
				if (!temp.isEmpty()) {
					traceEvent.targetList=	temp;
					traceEvent.targetListIndex = temp2;	
				}
				traceMap2.put(traceIndex.get(iX), traceEvent);
				}
			}
			
			
			System.out.println("....Mapped Trace...");
			for (Entry<Integer, TraceAlphabet> activity : traceMap2.entrySet()) {
				Integer k = activity.getKey();
				TraceAlphabet tx = activity.getValue();
			System.out.println("Eevent : " + k + " Map (A): " + tx.selectedSource + ":" + tx.sourceIndex);
			if ((tx.targetList != null)) {
				for (int m = 0; m < tx.targetList.size(); m++) {
					System.out.println("Trace : " +tx.traceNo + " event :" +k + " source : "
							+ tx.selectedSource + ":" + tx.sourceIndex
							+ " target:" + tx.targetList.get(m)
							+ " : Index : (" + tx.targetListIndex.get(m) + ")");
				}

				
				System.out.println("---Hello---Random: "
						+ SelectRandomName(tx.targetList));
				System.out.println("");
			}

		}

				
	/*	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		System.out.println("-----Printing log end -----");
	}
	
	public static void twistcopy(){
		System.out.println("----------index printing---------");
		
		Iterator<Entry<Integer, TraceAlphabet>> iter = traceMap.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<Integer,TraceAlphabet> entry = iter.next();
		    if((entry.getValue().selectedSource == null)&&
		    		(entry.getValue().selectedSource == null)){
		        iter.remove();
		    }
		}
		
		
		int  traceNo=-1;
			for (Entry<Integer, TraceAlphabet> activity : traceMap.entrySet()) {
				TraceAlphabet bb = activity.getValue();			
				if (bb.traceNo != traceNo){
					traceNo = bb.traceNo;
					System.out.println("----------Trace ("+traceNo+")----------" );
				
				}
				
				System.out.println(" Trace: "+ bb.traceNo + " Event:"+ bb.eventNo + "  : Source " +
				bb.selectedSource +"(" + bb.sourceIndex + ")  : Target " + bb.selectedTarget 
				+ "(" + bb.targetIndex + ")" +  bb.constrain);
				
			}	
		
		
	}
	
	public static void twistRandomSelection(){
		ArrayList<String> eventKey = new ArrayList();
		ArrayList<String> matched = new ArrayList();
		ArrayList<Integer> eventNo = new ArrayList();
		ArrayList<String> added = new ArrayList();
		ArrayList<String> targetList =  new ArrayList();
 		int  traceNo=0;
		for (Entry<Integer, TraceAlphabet> activity : traceMap.entrySet()) {
			
			TraceAlphabet bb = activity.getValue();			
			if (bb.traceNo != traceNo){
			   setRandomTrace(eventKey,eventNo,traceNo);
			   System.out.println("Sect " + secondact);
			   
			   for (int i = 0; i < secondact.size(); i++) {
				   String jx= secondact.get(i);
				    String jj[] = jx.split(":");
					
				    targetList=  getCorr(jj[0]);
				    String found = jj[jj.length -1];
				    
				    /*for (int x = i + 1; x < secondact.size(); x++) {
				    	 String jx2= secondact.get(x);
						   String jj2[] = jx.split(":");
							String found2 = jj[jj.length -1];
				    	
				    	if (found.equals(found2)) {
						String Dupli = secondact.get(i);
						if (added.indexOf(found)<= -1){
							//if (secondact.get(i) == secondact.get(j) && i != j) {
							matched.add(found);
							added.add(found);
							}	
						
					} // 
				    }// end of second if
*/				}

				traceNo = bb.traceNo;
				System.out.println("----------Trace ("+traceNo+")----------" );
				eventKey.clear();
				eventNo.clear();
				secondact.clear();
				added.clear();
				matched.clear();
			
			}
		//	eventKey.add(bb.selectedSource);
			eventKey.add(bb.selectedTarget);
			eventNo.add(activity.getKey());
			System.out.println(" Trace: "+ bb.traceNo + " Event:"+ bb.eventNo + "  : Source " +
			bb.selectedSource +"(" + bb.sourceIndex + ")  : Target " + bb.selectedTarget 
			+ "(" + bb.targetIndex + ")" +  bb.constrain);
			
		}	
		
		 setRandomTrace(eventKey,eventNo,traceNo);
	
	}
	
	public static ArrayList<String> getmatchList(ArrayList<String> trace,ArrayList<String> target,
			int start,ArrayList<Integer> indx,String source,ArrayList<Integer> eventIndex){
		boolean foundSwitch = false;  
		ArrayList<String> temp =  new ArrayList<String>();
	          //outer loop for all the elements in arrayA[i]
		for (int i = start; i < trace.size(); i++) {
			// inner loop for all the elements in arrayB[j]
			for (int j = 0; j < target.size(); j++) {
				// compare arrayA to arrayB and output results
				if (trace.get(i).equals(target.get(j))) {
					foundSwitch = true;
					temp.add(target.get(j));
					eventIndex.add(indx.get(i));
					//System.out.println("Target:(" + target.get(j)+") : " + +(indx.get(i)));
				}
			}			
			foundSwitch = false;
		}	
		
		if (!temp.isEmpty()){
			Set<Object> strSet = Arrays.stream(temp.toArray()).collect(Collectors.toSet());
    		temp.clear();
    		
    		for(Object s: strSet){
    			temp.add((String) s);
    		}
		}
		
		return temp;
	}
	
	public static int SelectRandomIndex(ArrayList<Integer> names) {
		int ret = -1;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < names.size(); i++) {
			for (int j = i + 1; j < names.size(); j++) {
				if (names.get(i).equals(names.get(j))) {
					temp.add(names.get(j));
				}				
			}
		}

		if (!temp.isEmpty()) {
			if (temp.size() > 0) {
				ret = names.get(selectRandom(names.size()));
			} else {
				ret = names.get(0);
			}
		}
		return ret;
	}

	
	public static int SelectRandomName(ArrayList<String> names) {
		int ret = -1;
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String> temp2 = new ArrayList<String>();
		ArrayList<String> added = new ArrayList<String>();
		for (int i = 0; i < names.size(); i++) {
			if (!added.contains(names.get(i))){
				added.add(names.get(i));
			
			temp.clear();
			for (int j = 0; j < names.size(); j++) {
				if (names.get(i).equals(names.get(j))) {
					temp.add(names.get(j));
				}				
			}
			
			if (!temp.isEmpty()) {
				if (temp.size() > 0) {
					ret = selectRandom(names.size());
				} else {
					ret = 0;
				}
			}
			temp2.add(names.get(i) + "." + ret);
		
			}
		}

	
		return ret;
	}
	
	public static String setRandomTrace(ArrayList<String> eventKey,ArrayList<Integer> eventNo,int traceNo){
		String ret ="";
		ArrayList<String> duplicateList = new ArrayList();	
		ArrayList<String> corrList = new ArrayList();
		System.out.println("setRandomTrace");
	
		for (int i = 0; i < eventKey.size(); i++) {
			System.out.println("i " + i + " event " + eventKey.get(i) + " key "
					+ eventNo.get(i));
			for (int j = i+1; j < eventKey.size(); j++) {
				if (eventKey.get(i) == eventKey.get(j) && i != j) {
					duplicateList.add(eventKey.get(i));
					String Dupli = eventKey.get(i)+ ":"+eventNo.get(j);
					if (secondact.indexOf(Dupli) <= -1)
					secondact.add(Dupli);
					System.out.println("Duplicate" + eventKey.get(j)
							+ ": event " + eventNo.get(j));
				}
			}
		}
				
			
		/*	
		
		for (int i = 0; i < eventKey.size(); i++)
		{
			System.out.println("duplicate event: " + eventNo.get(i));
			for (int j = i + 1 ; j < eventKey.size(); j++) 
			{ 
				if (eventKey.get(i).equals(eventKey.get(j))) { 
					System.out.println("Duplicates at" + eventKey.get(i) + " at : "+eventNo.get(i));
					//System.out.println("eventKey.get(j)" + eventKey.get(j));
				}
			}
		}
*/
			/*
		
		
		for (int i=0; i < eventNo.size(); i ++){
			int event = eventNo.get(i);
			String key = eventKey.get(i);
			corrList.clear();
			corrList= getCorr(eventKey.get(i));
			for (int k=0; k < corrList.size(); k++){
				if(corrList.get(k).equals(eventKey.get(i)))
				duplicateList.add(corrList.get(k));
			}		
			
			for (int ii=i; ii < eventNo.size(); ii ++){
				if (eventKey.get(i).equals(eventKey.get(ii)))
				{
					duplicateList.add(eventKey.get(i));	
				}
			}
		}
		int indx =0;
			
				if (duplicateList.size() > 0)
				{
					indx = selectRandom(duplicateList.size());

				int ind = eventKey.indexOf(duplicateList.get(indx));

				if (ind > -1) {
					ret = eventKey.get(ind);
				}
			
		System.out.println("Duplicates :" + ret);
		}	
*/		return ret;
	}
	
	public static void getEventList(ArrayList<Integer> traceList,ArrayList<String> sourceList,ArrayList<String> traceListName, int count ){
		ArrayList<String> targetList = new ArrayList<String>();
		ArrayList<String> targetIndex = new ArrayList<String>();
		targetIndex.clear();
		for (int i = 0; i < traceList.size(); i++) {
			targetList.clear();
			count = traceList.get(i);
			int index = sourceList.indexOf(traceListName.get(i));
		//	
			if (index > -1) {
				targetList = getCorr(sourceList.get(index));
			//	getmatchList(traceListName,targetList,null,""); 
				
				if (!targetList.isEmpty()) {
					  
					  TraceAlphabet tcx = traceMap.get(count);
				//	  System.out.println("Soucre : " + traceList.get(i) + "("+count +")");
					  tcx.isFirstKey = true;
					  tcx.isMapped = true;
					  traceMap.put(count, tcx);
					 printme(sourceList, targetList, traceListName,
							 traceListName.get(i), i,count);
					  
				} 
			} else {
				TraceAlphabet tcx = traceMap.get(count);
				tcx.isMapped = false;
				tcx.isFirstKey = false;
				traceMap.put(count, tcx);
			}
			 
			count++;
		} // end of tracelist;

	}
	
	
public static ProcessModel newStyleLog (){

	List<TaskChar> where = new ArrayList<TaskChar>();
	List<Constraint> lst = new ArrayList<Constraint>();
	List<String> mylist = new  ArrayList<String>();
	
	
	for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
		String key = activity.getKey();
		Alphabet filter = activity.getValue();
		mylist.add(key);
		
	/*	if (!filter.secondAlphabetKey.isEmpty()) {			
			String alphabetname = BranchCombination.getParentLetter(key);
			String xnam = " 1st : "+ key.replace(key,filter.secondAlphabetKey);//filter.correlationlist[j]; \
			xnam = key.replace(alphabetname,filter.secondAlphabetKey);
			mylist.add(xnam);		
		} // second key
		
		else {*/
			System.out.println("New Style : " + key + ":"+ filter.secondAlphabet);
			System.out.println(filter.secondAlphabet);
			if (filter.correlationlist != null) {
				for (int j = 0; j < filter.correlationlist.length; j++) {
					String xnam = " Correlation 1st : "
							+ filter.correlationlist[j];
					//if(mylist.indexOf(filter.correlationlist[j]) != -1){
						mylist.add(filter.correlationlist[j]);	
				//	}
					
				/*	String alphabetname = BranchCombination.getParentLetter(filter.correlationlist[j]);
					String blist[] = filter.secondAlphabet.split("::");
				if (blist.length>1){
					for (int ndm =0; ndm <blist.length; ndm++){
						if (!alphabetname.equals("")){
						if(!blist[ndm].equals(alphabetname)){
							System.out.println(filter.correlationlist[j].replace(alphabetname, blist[ndm]));
							mylist.add(filter.correlationlist[j].replace(alphabetname, blist[ndm]));							
						}			}
					}
					}*/
				}
			}

			else {
				String alphabetname = BranchCombination.getParentLetter(key);
				String xnam = " 1st : "
						+ key.replace(key, filter.secondAlphabetKey);// filter.correlationlist[j];
																		// \
				xnam = key.replace(alphabetname, filter.secondAlphabetKey);
				mylist.add(xnam);
			}
		}
	
	
	if (!mylist.isEmpty()){
		Set<Object> strSet = Arrays.stream(mylist.toArray()).collect(Collectors.toSet());
		mylist.clear();
		
		for(Object s: strSet){
			mylist.add((String) s);
		}
	}
	
	Collections.sort(mylist);
	int theEnd = 65+ mylist.size();
	for (int vchar = 65; vchar < theEnd;vchar++ ){
		char c = (char)vchar;
		System.out.println("Char: "+ c);
		TaskChar cc = new TaskChar(c);
		where.add(cc);			
	}
	
	for (int i=0; i < mylist.size(); i++){
		newLogIndex.put(where.get(i),mylist.get(i));
		System.out.println("Map:  Key: "+ where.get(i) + " Char: "+  mylist.get(i));
	}
	
	
	for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
		String key = activity.getKey();
		Alphabet filter = activity.getValue();
		
		TaskChar firstChar = getAlphabetValue(key);//  newLogIndex.get(key);
		
		ArrayList<TaskChar> list = new ArrayList<TaskChar>();
		TaskCharSet target = new TaskCharSet();
		/*if (!filter.secondAlphabetKey.isEmpty()) {			
			String alphabetname = BranchCombination.getParentLetter(key);
			String xnam =  key.replace(key,filter.secondAlphabetKey);//filter.correlationlist[j]; \
			xnam = key.replace(alphabetname,filter.secondAlphabetKey);
			testchar[0]= newLogIndex.get(xnam);	
			
		} // second key
		
		else {
		*/	list.clear();
		
			String xKey = key.substring(key.length() - 1);

			if (xKey.equals("0")) {
				if (filter.correlationlist != null){
					for(int j=0; j < filter.correlationlist.length ; j++){
						String xnam =filter.correlationlist[j];
						list.add(getAlphabetValue(filter.correlationlist[j]));
						int len = filter.correlationlist[j].length();
						if ((xnam.contains("_"))
								&& (xnam.contains(filter.secondAlphabetKey))) {
							String sx[] = xnam.split("_");
							String first = BranchCombination.getParentLetter(key);
							String last = BranchCombination.getParentLetter(sx[0]);
							first = key.replace(first, "");
							last = sx[0].replace(last, "");
							System.out.println("First" + first + " :Lst" + last);
							if (first.equals(last)) {
								list.add(getAlphabetValue(filter.correlationlist[j]));								
							}
						} 
					}
				}
				
			} // end of zero level first part
		
			
			// end of one leerl
			else {		
			if (filter.correlationlist != null){
				for(int j=0; j < filter.correlationlist.length ; j++){
					String xnam =filter.correlationlist[j];
					int len = filter.correlationlist[j].length();
					if ((xnam.contains("_"))
							&& (xnam.contains(filter.secondAlphabetKey))) {
						String sx[] = xnam.split("_");
						String first = BranchCombination.getParentLetter(key);
						String last = BranchCombination.getParentLetter(sx[0]);
						first = key.replace(first, "");
						last = sx[0].replace(last, "");
						System.out.println("First" + first + " :Lst" + last);
						//if (first.equals(last)) {
						if (filter.isRoot){	
						list.add(getAlphabetValue(filter.correlationlist[j]));
							// list.add(
							// newLogIndex.get(filter.correlationlist[j]));
						}
					} else if ((filter.correlationlist[j].length() < key
							.length() + 3)
							&& (xnam.contains(filter.secondAlphabetKey))) {
						list.add(getAlphabetValue(filter.correlationlist[j]));
						// list.add(newLogIndex.get(filter.correlationlist[j]));
					}
					}
			} else {
				String alphabetname = BranchCombination.getParentLetter(key);
				String xnam = key.replace(key, filter.secondAlphabetKey);// filter.correlationlist[j];
																			// \
				if ((xnam.length() + 3 == key.length())
						&& (xnam.contains(filter.secondAlphabetKey))) {
					xnam = key.replace(alphabetname, filter.secondAlphabetKey);
					// list.add(newLogIndex.get(xnam));
					list.add(getAlphabetValue(xnam));
				}
			}  // end of filter empy
			} // end of zero level check
			
		
			
			if (filter.constrain.equals("response")) {
				Response res = new Response(new TaskCharSet(firstChar), new TaskCharSet(list));
				lst.add(res);
			
				if (filter.correlationlist != null) {
					for (int j = 1; j < filter.correlationlist.length; j++) {
						list.clear();
						String xnam = " Correlation 1st : "	+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
						if (filter.isRoot==false){
							list.add(getAlphabetValue(filter.correlationlist[j]));
							Response res2 = new Response(new TaskCharSet(firstChar), new TaskCharSet(list));
							lst.add(res2);
						}// false
					}
					
				}
				
			} else if (filter.constrain.equals("precedence")) {
				Precedence res = new Precedence( new TaskCharSet(list),new TaskCharSet(firstChar));
				lst.add(res);
				
				if (filter.correlationlist != null) {
					for (int j = 1; j < filter.correlationlist.length; j++) {
						list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
					if (filter.isRoot==false){
						list.add(getAlphabetValue(filter.correlationlist[j]));
						//Response res2 = new Response(new TaskCharSet(firstChar), new TaskCharSet(list));
						Precedence res2 = new Precedence( new TaskCharSet(list),new TaskCharSet(firstChar));
						lst.add(res2);
					
					}// false
					}
					
				}
				
				
				//START OF RESPONCE EXIS
			} 	if (filter.constrain.equals("respondedexistence")){
				RespondedExistence res = new RespondedExistence(new TaskCharSet(firstChar), new TaskCharSet(list)); 
				//Response res = new Response(new TaskCharSet(firstChar), new TaskCharSet(list));
				lst.add(res);
			
				if (filter.correlationlist != null) {
					for (int j = 1; j < filter.correlationlist.length; j++) {
						list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
				
					if (filter.isRoot==false){
						list.add(getAlphabetValue(filter.correlationlist[j]));
						//Response res2 = new Response(new TaskCharSet(firstChar), new TaskCharSet(list));
						RespondedExistence res2 = new RespondedExistence(new TaskCharSet(firstChar), new TaskCharSet(list));
						lst.add(res2);
					
					}// false
					}
					
				}
			
			} 	if (filter.constrain.equals("alternateresponse")){
				
				AlternateResponse res = new AlternateResponse(new TaskCharSet(firstChar), new TaskCharSet(list)); 
				lst.add(res);			
				if (filter.correlationlist != null) {
					for (int j = 1; j < filter.correlationlist.length; j++) {
						list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
				
					if (filter.isRoot==false){
						list.add(getAlphabetValue(filter.correlationlist[j]));
						AlternateResponse res2 = new AlternateResponse(new TaskCharSet(firstChar), new TaskCharSet(list));
						lst.add(res2);
					
					}// false
					}
					
				}
				
			} else	if (filter.constrain.equals("notresponse")){
				
				NotSuccession res = new NotSuccession(new TaskCharSet(firstChar), new TaskCharSet(list)); 
				lst.add(res);			
				if (filter.correlationlist != null) {
					for (int j = 1; j < filter.correlationlist.length; j++) {
						list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
				
					if (filter.isRoot==false){
						list.add(getAlphabetValue(filter.correlationlist[j]));
						NotSuccession res2 = new NotSuccession(new TaskCharSet(firstChar), new TaskCharSet(list));
						lst.add(res2);
					
					}// false
					}					
				}

				} else	if (filter.constrain.equals("notchainresponse")){
				
					NotChainSuccession res = new NotChainSuccession(new TaskCharSet(firstChar), new TaskCharSet(list)); 
				lst.add(res);			
				if (filter.correlationlist != null) {
					for (int j = 1; j < filter.correlationlist.length; j++) {
						list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
				
					if (filter.isRoot==false){
						list.add(getAlphabetValue(filter.correlationlist[j]));
						NotChainSuccession res2 = new NotChainSuccession(new TaskCharSet(firstChar), new TaskCharSet(list));
						lst.add(res2);
					
					}// false
					}					
				}

			}	 else if (filter.constrain.equals("precedence")) {
				Precedence res = new Precedence( new TaskCharSet(list),new TaskCharSet(firstChar));
				lst.add(res);
				
				if (filter.correlationlist != null) {
					for (int j = 1; j < filter.correlationlist.length; j++) {
						list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
					if (filter.isRoot==false){
						list.add(getAlphabetValue(filter.correlationlist[j]));
						//Response res2 = new Response(new TaskCharSet(firstChar), new TaskCharSet(list));
						Precedence res2 = new Precedence( new TaskCharSet(list),new TaskCharSet(firstChar));
						lst.add(res2);
					
					}// false
					}
					
				}
				
				
				 
			}
			
			
			
			else if (filter.constrain.equals("notrespondedexistence")) {
				
				
				NotCoExistence res = new NotCoExistence(
						new TaskCharSet(firstChar), new TaskCharSet(list));
				lst.add(res);
				if (filter.correlationlist != null) {
					for (int j = 1; j < filter.correlationlist.length; j++) {
						list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);

						if (filter.isRoot == false) {
							list.add(getAlphabetValue(filter.correlationlist[j]));
							NotCoExistence res2 = new NotCoExistence(
									new TaskCharSet(firstChar),
									new TaskCharSet(list));
							lst.add(res2);

						}// false
					}
				}

			}

			else if (filter.constrain.equals("alternateprecedence")) {
				
				AlternatePrecedence res = new AlternatePrecedence( new TaskCharSet(list),new TaskCharSet(firstChar));
				lst.add(res);
				
				if (filter.correlationlist != null) {
					for (int j = 1; j < filter.correlationlist.length; j++) {
						list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
					if (filter.isRoot==false){
						list.add(getAlphabetValue(filter.correlationlist[j]));
						AlternatePrecedence res2 = new AlternatePrecedence( new TaskCharSet(list),new TaskCharSet(firstChar));
						lst.add(res2);					
					}// false
					}
					
				}
			}

			else if (filter.constrain.equals("existence")) {
				//list.clear();
				if (filter.correlationlist != null) {
					for (int j = 0; j < filter.correlationlist.length; j++) {
						//list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
					
						if (filter.isRoot == false) {
							list.add(getAlphabetValue(filter.correlationlist[j]));
							/*Participation res2 = new Participation(new TaskCharSet(list));
							lst.add(res2);*/
						}// false
					}

				} 			
 			
 			Participation res = new Participation(new TaskCharSet(list));
 			
			lst.add(res);
			}else if (filter.constrain.equals("chainprecedence")) {
				ChainPrecedence res = new ChainPrecedence( new TaskCharSet(list),new TaskCharSet(firstChar));
				lst.add(res);
				
				if (filter.correlationlist != null) {
					for (int j = 1; j < filter.correlationlist.length; j++) {
						list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
					if (filter.isRoot==false){
						list.add(getAlphabetValue(filter.correlationlist[j]));						
						ChainPrecedence res2 = new ChainPrecedence( new TaskCharSet(list),new TaskCharSet(firstChar));
						lst.add(res2);
					
					}// false
					}
					
				}
			}	else if (filter.constrain.equals("notprecedence")) {
				
				NotSuccession res = new NotSuccession( new TaskCharSet(list),new TaskCharSet(firstChar));
				lst.add(res);
				
				if (filter.correlationlist != null) {
					for (int j = 1; j < filter.correlationlist.length; j++) {
						list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
					if (filter.isRoot==false){
						list.add(getAlphabetValue(filter.correlationlist[j]));						
						NotSuccession res2 = new NotSuccession( new TaskCharSet(list),new TaskCharSet(firstChar));
						lst.add(res2);
					
					}// false
					}
					
				}
			}
			
			else if (filter.constrain.equals("init")) {
				//list.clear();
				if (filter.correlationlist != null) {
					for (int j = 0; j < filter.correlationlist.length; j++) {
						//list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
					
						if (filter.isRoot == false) {
							list.add(getAlphabetValue(filter.correlationlist[j]));
							/*Participation res2 = new Participation(new TaskCharSet(list));
							lst.add(res2);*/
						}// false
					}

				} 			
 			
				Init res = new Init(new TaskCharSet(list));
				lst.add(res);
			}
			
			else if (filter.constrain.equals("end")) {
				//list.clear();
				if (filter.correlationlist != null) {
					for (int j = 0; j < filter.correlationlist.length; j++) {
						//list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
					
						if (filter.isRoot == false) {
							list.add(getAlphabetValue(filter.correlationlist[j]));
							/*Participation res2 = new Participation(new TaskCharSet(list));
							lst.add(res2);*/
						}// false
					}

				} 			
 			
				End res = new End(new TaskCharSet(list));
				lst.add(res);
			}
			
			else if (filter.constrain.equals("absence")) {
				//list.clear();
				if (filter.correlationlist != null) {
					for (int j = 0; j < filter.correlationlist.length; j++) {
						//list.clear();
						String xnam = " Correlation 1st : "
								+ filter.correlationlist[j];
						mylist.add(filter.correlationlist[j]);
					
						if (filter.isRoot == false) {
							list.add(getAlphabetValue(filter.correlationlist[j]));
							/*Participation res2 = new Participation(new TaskCharSet(list));
							lst.add(res2);*/
						}// false
					}

				} 			
 			 
				AtMostOne res = new AtMostOne(new TaskCharSet(list));
				lst.add(res);
			}
		//--END OF ALTERNATE PRESE	
		

		}
	
	
	 Set<TaskChar> alphabetS = new TreeSet<TaskChar>(where);
  	int i=0;
	
	TaskCharEncoderDecoder taChEnDe = new TaskCharEncoderDecoder();
	taChEnDe.encode(alphabetS);
	TaskCharArchive taChaAr = new TaskCharArchive(taChEnDe.getTranslationMap());
	for (TaskChar tCh : alphabetS) {
		tCh.identifier = taChaAr.getTaskChar(tCh.taskClass).identifier;
	}

	ConstraintsBag bag = new ConstraintsBag(alphabetS);
	for (Constraint con : lst) {
		System.out.println(con + " :RE> " + con.getRegularExpression());
		bag.add(con.getBase(), con);
	}
	ProcessModel proMod = new ProcessModel(taChaAr, bag);
	return proMod;
	
}
	
	
public static TaskChar getAlphabetValue(String search){
	TaskChar ret = null; 
	for (Entry<TaskChar,String> entry : newLogIndex.entrySet()) {
        String key = entry.getValue();
        TaskChar t1 = entry.getKey();
		 if(key.equals(search)) {
            ret = t1;
           // System.out.println(t1 + ": "+ key + " : " + search);
            break;
           }
       }
	return ret;	
}

public static String getAlphabetKey(String search){
	String ret = null; 
	for (Entry<TaskChar,String> entry : newLogIndex.entrySet()) {
        String key = entry.getValue();
        String t1 = entry.getKey().toString();
		 if(t1.equals(search)) {
            ret = key;
           // System.out.println(t1 + ": "+ key + " : " + search);
            break;
           }
       }
	return ret;	
}

	private static void CheckforPrecendence() {
		
	LinkedHashMap<String, Alphabet> abMaptemp = new LinkedHashMap<String, Alphabet>();
		String aa ="";
		String bb = "";
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet filter = activity.getValue();
			aa="";bb="";
			if (filter.constrain.contains("precedence")){
				
				System.out.println("A :" + filter.alphabetkey);
				System.out.println("B :" + filter.secondAlphabetKey);
				
				aa = BranchCombination.getParentLetter(k);
				bb = BranchCombination.getParentLetter(filter.secondAlphabetKey);
				k = k.replace(aa, bb);
				filter.alphabetkey = filter.alphabetkey.replace(aa,bb);
				filter.alphabetname = filter.alphabetname.replace(aa,bb);
				filter.secondAlphabetKey = aa;
				//filter.secondAlphabetKey = filter.secondAlphabetKey.replace(bb,aa);
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
	/*	System.out.println("-------Precendece Checking-----------");
		
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
		System.out.println("Wait");*/
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
					if (constrain.contains("precedence"))
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
						System.out.println(firstName);
						countedList.add(firstName);
						alphabetList.add(firstName);
						//countedListB.add(firstName);
					} else {
						alphabetList.add(ad.getName());
						countedListB.add(ad.getName());
						System.out.println(ad.getName());
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
				
					
					if (constrain.contains("precedence")){
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
					if (constrain.contains("precedence")){
						
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
	
	public static void Addnew(String fname, String lname, String act, String ilpcond, boolean isActivated,
			String relcond,String FullCondition,String cons, boolean isroot)
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
		ab.isRoot = isroot;
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
						
						
						if (constrain.contains("precedence")) {

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
									Addnew(fname,lname,condition,ilpcond,true,getRelCond(FullCondition),FullCondition,constrain,true);	
								}	
								fname = fname+ (Integer.toString(ind));
								Addnew(fname,lname,condition,ilpcond,true,getRelCond(FullCondition),FullCondition,constrain,true);
								/*if (addparent){
								Addnew(fname+"_1",lname, BranchCombination.divertCondition(condition),ilpcond,true,getRelCond(condition),FullCondition,constrain);
								}*/
								Addnew(lname,"","","",false,"",FullCondition,constrain,false);			 
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
						 
							if ((constrain.equals("existence")||(constrain.equals("init")))
									||(constrain.equals("end"))||(constrain.equals("absence"))){
								ilpcond =	getCondtions(model,fname,condition);
								ind = checkIndex(fname);					
								ind = ind - 1;
								lname = fname;
							if (ind==0){
								addparent =true;
								Addnew(fname,lname,condition,ilpcond,true,getRelCond(FullCondition),FullCondition,constrain,true);	
							}	
							fname = fname+ (Integer.toString(ind));
							Addnew(fname,lname,condition,ilpcond,true,getRelCond(FullCondition),FullCondition,constrain,true);
							if (addparent){
							Addnew(fname+"_1",lname, BranchCombination.divertCondition(condition),ilpcond,true,getRelCond(condition),FullCondition,constrain,true);
							}
							Addnew(lname,"","","",false,"",FullCondition,constrain,false);
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
						Addnew(fname,lname,condition,ilpcond,true,getRelCond(FullCondition),FullCondition,constrain,true);	
					}	
					fname = fname+ (Integer.toString(ind));
					Addnew(fname,lname,condition,ilpcond,true,getRelCond(FullCondition),FullCondition,constrain,true);
					if (addparent){
					Addnew(fname+"_1",lname, BranchCombination.divertCondition(condition),ilpcond,true,getRelCond(condition),FullCondition,constrain,false);
					}
					Addnew(lname,"","","",false,"",FullCondition,constrain,false);
				System.out.println("First : " +fname + " Last : " +  lname);
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
				ab.isRoot = false;
				ab.alphabetname =  BranchCombination.getParentLetter(key);
				if (clst.length > 1) {
					ab.isSingle = true;
					ab.secondAlphabet = getB(clst);					
				}	
				ab.constrain = constrainB;
				String cor = getCoRel(clst);// = abMap.get(ab.alphabetname);
				String cond = "";
				for (int c=0; c < clst.length; c++){
					Alphabet alph = abMap.get(clst[c]);
					if (c==0){
						ab.secondAlphabetKey = alph.secondAlphabetKey;	
					}
					if (!cond.isEmpty()) cond = cond + "::";
					if (alph!=null){
						cond = cond + alph.fullCondition;
					//	ab.fullCondition = bd.fullCondition;
					}
				}
				ab.fullCondition = cond;
				
				
				
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

				if ((filter.constrain.equals("existence")) || ((filter.constrain.equals("init")))
						|| ((filter.constrain.equals("end")))|| ((filter.constrain.equals("absence")))) {
					if (corChoine.length <= 1) {
						String[] cor = getZeroCor(k, filter.alphabetname,
								filter.secondAlphabetKey, filter);
						filter.correlationlist = cor;
						filter.secondAlphabet = "";
						/*
						 * SetSecondList(filter.alphabetname,
						 * filter.secondAlphabetKey,cor);
						 */

					} else {
						filter.correlationlist = getUpperCor(k,
								filter.alphabetname, filter.secondAlphabetKey,
								filter.secondAlphabet);

					}

				} else {
				
					if (corChoine.length <= 1) {

						String[] cor = getZeroCor(k, filter.alphabetname,
								filter.secondAlphabetKey, filter);
						// filter.correlationlist = cor;
						String test = filter.secondAlphabetKey;

						System.out.println("xxx" + filter.constrain);
						if (test.isEmpty()) {
							test = "XX";
						}
						test = test + "::";

						String[] corx = test.split("::");

						filter.correlationlist = corx;
						filter.secondAlphabet = "";
						/*
						 * SetSecondList(filter.alphabetname,
						 * filter.secondAlphabetKey,cor);
						 */
					
					} else {
						// filter.correlationlist = getUpperCor(k,
						// filter.alphabetname,
						// filter.secondAlphabetKey,filter.secondAlphabet);
						filter.correlationlist = filter.secondAlphabet
								.split("::");
						String snd = filter.secondAlphabet;
						// xxx filter.secondAlphabet = snd.replaceAll(
						// filter.secondAlphabetKey, "!@#@!");
						/*
						 * System.out.println("Key: " + k + " :  Upper corre: "
						 * + getUpperCor(k,filter.alphabetname,
						 * filter.secondAlphabetKey));
						 */
					}
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
			String temp = combinedList.get(ind).replaceAll(" ", "");
			String root[] = combinedList.get(ind).split(" ");
			if (temp.contains(xkey)) {
				if (!ret.isEmpty()) {
					ret = ret + "::";
				}
				if (ind == 0) {
					ret = ret
							+ combinedList.get(ind).replaceAll(aa, b)
									.replaceAll(" ", "").trim();
				} else {
					String vtemp = combinedList.get(ind).replaceAll(aa, b)
							.replaceAll(" ", "").trim();
					if (!vtemp.equals(root[0].replaceAll(aa, b))) {
						ret = ret + /* vtemp + "::"+ */vtemp;
					} else {
						{
							ret = ret + vtemp;
						}
					}
				}
			}
		}	
		return ret.split("::");
	}
	
	public static String[] getUpperCor(String xkey, String a, String b, String sendlist) {
		String aa = BranchCombination.getParentLetter(a);
		String ret = "";
		int vsize = 0;
		String sblist[] = sendlist.split("::");
		
		for (int q=0; q < sblist.length; q++){
			b = sblist[q];
			for (int ind = 0; ind < combinedList.size(); ind++) {
				String root[] =  combinedList.get(ind).split(" ");
				String  temp = combinedList.get(ind).replaceAll(" ", "");
				if(temp.length() == xkey.length()){
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
						{
							ret = ret+ vtemp /*+ "::"+ root[0].replaceAll(aa, b) + "_"+ vtemp*/;}
						else{
							/*{ret = ret+ vtemp;}	*/
							if (sblist.length>0){
								 for(int j=0; j<sblist.length; j++)
									{if (!ret.isEmpty()) {
										ret = ret + "::";
									}
									ret = ret + vtemp.replace(aa, sblist[j]);
								}
								}
						}
					
					
						
						
						
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
				
				if(b2.constrain.contains("precedence")){
					secondkey = b2.secondAlphabetKey;
					if(constrainB.isEmpty())
						constrainB =b2.constrain;
					
				}else{
				if(constrainB.isEmpty())
					constrainB =b2.constrain;
				}
				if (!b.isEmpty()){
					b=b+"::";
				}
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
