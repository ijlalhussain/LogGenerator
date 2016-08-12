
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
import minerful.concept.constraint.relation.ChainResponse;
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

/*
 *Main Class of Log Generator

*/
public class DeclareLogGenerator {
	static LinkedHashMap<String, Alphabet> alphabetMap = new LinkedHashMap<String, Alphabet>(); // Activation Activities
	static LinkedHashMap<String, Alphabet> acitivityConstraintList = new LinkedHashMap<String, Alphabet>(); //Constraints activities
	static LinkedHashMap<String, Alphabet> abMap2 = new LinkedHashMap<String, Alphabet>(); // List to Compare
	static LinkedHashMap<String, Alphabet> abMapAll = new LinkedHashMap<String, Alphabet>(); // Merger all List
	static LinkedHashMap<String, Alphabet> alphabetMapx = new LinkedHashMap<String, Alphabet>(); // List for Log Generation
	static LinkedHashMap<String, Alphabet> corrlationList = new LinkedHashMap<String, Alphabet>();	// Correlation Condition List
	static LinkedHashMap<String, Alphabet> abMapdata = new LinkedHashMap<String, Alphabet>(); // Data List
	static ArrayList<String> alphabets1 = new ArrayList(); // Name of the Alphabets to check the duplicates
	static ArrayList<String> firstactivationList = new ArrayList(); // First aplhabet in the event
	static ArrayList<String> secondActivationList = new ArrayList(); // second alphabet in the event
	static ArrayList<String> firstConditionList = new ArrayList(); // activation conditions
	static ArrayList<String> CorrelationContList = new ArrayList(); // correlation conditions
	static ArrayList<String> addedList = new ArrayList(); // activites added in the log lists
	static ArrayList<String> countedList = new ArrayList(); // count number of combinations
	static ArrayList<String> countedListB = new ArrayList(); // combination second list
	static ArrayList<String> countedListA = new ArrayList();
	static ArrayList<String> alphabetList = new ArrayList();
	static ArrayList<String> repeatList = new ArrayList();
	static ArrayList<String> combinedList = new ArrayList(); // List if akk constraitns
	static ArrayList<String> constrainList = new ArrayList<String>();
	static 	TreeMap<TaskChar,String> newLogIndex = new TreeMap<TaskChar,String>();
	static 	TreeMap<Integer,TraceAlphabet> traceMap = new TreeMap<Integer,TraceAlphabet>();
	static 	TreeMap<Integer,LogAlphabet> logMap = new TreeMap<Integer,LogAlphabet>();	
	static TreeMap<Integer, TraceAlphabet> traceMap2 = new TreeMap<Integer,TraceAlphabet>();
	static String secondkey ="";
	static String actcond ="";
	static String constrainB ="";
	public static Encoding OUTPUT_ENCODING = Encoding.xes;
	public static final File OUTPUT_LOG = new File("");

	public static boolean GenerateLog(int minlength, int maxlength, long LogSize,
	/* 
	 * Read the Declare Model  
	 */
			
			String filename, String destitionfile) {
		AssignmentViewBroker broker = XMLBrokerFactory
				.newAssignmentBroker(filename);
		AssignmentModel model = broker.readAssignment();				

		/* 
		 * Parse the model and add in the List 
		 */
				
		getAlphabets(model); // add first aphabets and condition
		
		 
		checkActivation(model); // add activation conditions
		ParameterSettings2.jProgressBar1.setValue(3);
		AddFirstLetter(model); // add into abMap
		/* 
		 * splitting the activations
		 */
		
		generateCombination(); // count alphabets
		
		/* 
		 * ILP Check 
		 */
		
		alphabetMapx.clear();
		SetCombinationCondtion(); // combination without rep.
		ILPSolverUtil.CheckIlpConditions(alphabetMapx);
		ParameterSettings2.jProgressBar1.setValue(4);

		ILPSolverUtil.purifyLog(combinedList, alphabetMapx);

		if (alphabetMapx.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Unable to Generate Log!");
			return false;
		}
		addCorrelatedConditions();
	//	CheckforPrecendence();

		LogMakerCmdParameters logMakParameters = new LogMakerCmdParameters(
				minlength, maxlength, LogSize);
		OUTPUT_ENCODING = Encoding.xes;
		logMakParameters.outputEncoding = OUTPUT_ENCODING;

		File OUTPUT_LOG = new File(destitionfile);
		logMakParameters.outputLogFile = OUTPUT_LOG;
		
		/* 
		 * Log Process start 
		 */
		MinerFulLogMaker logMak = new MinerFulLogMaker(logMakParameters);		
		ProcessModel proMod = null;  
		//CheckforPrecendence();
		proMod = createDataModel();//newStyleLog();
		XLog xlog = logMak.createLog(proMod);
		// CreateBranches(xlog); //twist
		ProcessLog(xlog);
		/*
		 * GetRandomSelection(xlog); CreateGraphofLog(xlog);
		 * CombineSelectedLog();
		 */
		addCorrelationtoArray();
		mergeLists();
		addEventDataRestoreKeys(xlog);

		
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
			Alphabet traceAlphabet = activity.getValue();
			
			if (traceAlphabet.isSingle){
				System.out.println("Key..:" +activity.getKey() + " Condition: "+
						traceAlphabet.fullCondition + " : mapped:" + traceAlphabet.secondAlphabet);
			}else
			{
				System.out.println("Key:" +activity.getKey() + " Condition: "+
						traceAlphabet.fullCondition + " : mapped:" + traceAlphabet.secondAlphabetKey  );
			}
	
		}
		
	}

	/* 
	 * Combination of correlation activities
	 * based on Graph 
	 */
	private static void CombineSelectedLog() {
		System.out.println("--------------Log Graph Combinations------------------------");
		int count = 0;
		int loopcheck = 0;
		for (Entry<Integer, LogAlphabet> activity : logMap.entrySet()) {
			int k = activity.getKey();
			LogAlphabet traceAlphabet = activity.getValue();
			//System.out.println("Trace:" + traceAlphabet.traceNo + " event : " + k);
			if (traceAlphabet.isFirstKey == true) {
				for (int i = 0; i < traceAlphabet.eventLists.size(); i++) {
					if (traceAlphabet.eventLists.get(i) > k) {
							TraceAlphabet tc2 = traceMap.get(traceAlphabet.eventLists.get(i));
							if (tc2.isFirstKey) {
								traceAlphabet.eventLists.remove(traceAlphabet.eventLists.get(i));
							}
						} else {
							traceAlphabet.eventLists.remove(traceAlphabet.eventLists.get(i));
						}
					

				}
				logMap.put(k, traceAlphabet);

			}
		}
		// remove duplications
		for (Entry<Integer, LogAlphabet> activity : logMap.entrySet()) {
			int k = activity.getKey();
			LogAlphabet LogAlphabet = activity.getValue();
			TraceAlphabet key = traceMap.get(k);
			if (LogAlphabet.isFirstKey == true) {
				ArrayList<Integer> dfsList = new ArrayList<Integer>();
				dfsList.clear();
				for (int i = 0; i < LogAlphabet.eventLists.size(); i++) {
					if (LogAlphabet.eventLists.get(i) > k) {
						TraceAlphabet tc2 = traceMap.get(LogAlphabet.eventLists.get(i));
						dfsList.add(LogAlphabet.eventLists.get(i));
					}
				}
				LogAlphabet.eventLists = dfsList;
				logMap.put(k, LogAlphabet);
			}
		}
		
		//print combinations

	}
	
	
	public static ArrayList<Integer> GetMapppedList(int envetNo){
		ArrayList<Integer> dfsList = new ArrayList<Integer>();
		dfsList.clear();
		LogAlphabet LogAlphabet = logMap.get(envetNo);
		if (LogAlphabet == null) {
			// System.out.println(" No Connected Events: "+ envetNo );
			return dfsList;
		}
		TraceAlphabet key = traceMap.get(envetNo);

		if (LogAlphabet.isFirstKey == true) {
			System.out.println("key" + key.alphabetKey + " event : " + envetNo
					+ ":Trace:" + LogAlphabet.traceNo);

			String ilpCondition = "";
			for (int i = 0; i < LogAlphabet.eventLists.size(); i++) {
				// if (LogAlphabet.eventLists.get(i) > k) {
				if (!ilpCondition.isEmpty()) {
					ilpCondition = ilpCondition + "::";
				}
				TraceAlphabet tc2 = traceMap.get(LogAlphabet.eventLists.get(i));

				// dfsList.add(LogAlphabet.eventLists.get(i));
				if (tc2.isFirstKey) {

					// LogAlphabet.eventLists.remove(LogAlphabet.eventLists.get(i));
					System.out.println("Related Key: " + tc2.alphabetKey + ":"
							+ LogAlphabet.eventLists.get(i));
				} else {
					dfsList.add(LogAlphabet.eventLists.get(i));
					System.out.println("Connected Events: " + tc2.alphabetKey
							+ ":" + LogAlphabet.eventLists.get(i));
				}
			}
		}
		return dfsList;
	}
	
	
	private static void CheckForSameList(XLog xlog) {
		int eventnumber = 0;
		int traceno = 0;
		int check = 0;
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

				// if (tc2.isFirstKey) {
				mappedList = GetMapppedList(event);
				if (!mappedList.isEmpty()) {
					for (int i2 = i + 1; i2 < traceLog.size(); i2++) {
						// System.out.println("Mappedex:" + tc2.alphabetKey +
						// ":"+tc2.Mappedkeys);
						TraceAlphabet tc = traceMap.get(event);
						if (tc.Mappedkeys != null) {
							ArrayList<Integer> mappedList2 = new ArrayList<Integer>();
							int event2 = traceLog.get(i2);
							mappedList2 = GetMapppedList(event2);
							if (!mappedList2.isEmpty()
									&& checkforRepeart(mappedList, mappedList2)) {
								sameEvents.add(event + "::" + event2);

							}

						}

						// }

					}

				}

			}

			check = 0;
			traceno++;
		}

		for (int i = 0; i < sameEvents.size(); i++) {
			System.out.println("Similar Events with:" + sameEvents.get(i));
			String[] ex = sameEvents.get(i).split("::");

			for (int count = 0; count < ex.length; count++) {
				TraceAlphabet tc = traceMap2.get(Integer.parseInt(ex[count]));
				System.out.println(" souce:" + ex[count]);
				if (tc != null) {
					if (tc.ilpSelectedList != null) {
						for (int t = 0; t < tc.ilpSelectedList.size(); t++) {
							System.out.println("xxx"
									+ tc.ilpSelectedList.get(t));
						}
					}
				}
			}

		}
	}

	private static void CreateGraphofLog(XLog xlog) {
			
		int trace = 0;
		int check = 0;
		for (Entry<Integer, TraceAlphabet> activity : traceMap2.entrySet()) {
			int k = activity.getKey();
			TraceAlphabet tc = activity.getValue();
			Map<Integer, List<Integer>> ilpIndex = new HashMap<Integer, List<Integer>>();
			// System.out.println("Source:" + tc.alphabetKey);
			if (tc.Mappedkeys != null) {
				Graph graph2 = new Graph(600);
				for (int i = 0; i < tc.Mappedkeys.size(); i++) {
					graph2.addEdge(k, tc.MappedKeysIndex.get(i));					
				}

				// System.out.println(" DFS: "+ graph2.DFS(k));
				ArrayList<Integer> dfsList = new ArrayList<Integer>();
				dfsList = graph2.DFS(k);
				ilpIndex.put(k, dfsList);
				// TraceAlphabet tc2 = new TraceAlphabet();
				tc.ilpIndex = ilpIndex;
				dfsList.remove(0);
				tc.ilpSelectedList = dfsList;
				tc.ilpList = dfsList;
				traceMap2.put(k, tc);
			}
		}
		// - trace action

		int eventnumber = 0;
		int traceno = 0;
		check = 0;

		for (XTrace xtrace : xlog) {
			ArrayList<Integer> traceLog = new ArrayList<Integer>();
			ArrayList<String> traceEvents = new ArrayList<String>();
			for (XEvent event : xtrace) {
				TraceAlphabet tc = traceMap2.get(eventnumber);
				if (tc != null) {
					if (tc.ilpSelectedList != null) {
						traceLog.add(eventnumber);
						for (int i = 0; i < tc.ilpSelectedList.size(); i++) {
							traceEvents.add(eventnumber + "::"
									+ tc.ilpSelectedList.get(i));
						}
					}
				}
				eventnumber++;
			}

			Set<Object> strSet = Arrays.stream(traceLog.toArray()).collect(
					Collectors.toSet());
			traceLog.clear();

			for (Object s : strSet) {
				traceLog.add((Integer) s);
			}
			for (int l = 0; l < traceLog.size(); l++) {
				TraceAlphabet tb = traceMap.get(traceLog.get(l));
				// if (tb.alphabetKey.substring(0,1).equals("A")){
				LogAlphabet logalphabet = new LogAlphabet();
				logalphabet.eventNo = tb.eventNo;
				logalphabet.traceNo = tb.traceNo;
				Graph g33 = new Graph(600);
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
					System.out.println("Trace:" + traceno);
					System.out.println("LogEvent:" + traceLog.get(l));
					System.out.println("LogMapped:" + eventList.toString());

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

	/* 
	 * Selection of random correlation from multiple  activities
	 * based on Graph  - not useful for activations only
	 */
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

	/* 
	 * Selection of random correlation from multiple  activities
	 * based on Graph  - not useful for activations only
	 */
	private static void ProcessLog(XLog xlog) {
	
		try {
			ArrayList<String> sourceList = new ArrayList<String>();
			ArrayList<String> targetList = new ArrayList<String>();
			ArrayList<String> traceList = new ArrayList<String>();
			// ArrayList<Integer> targetIndex = new ArrayList<Integer>();

			int count = 0;
			CheckforPrecendence();
			for (Entry<String, Alphabet> activity : alphabetMapx.entrySet()) {
				sourceList.add(activity.getKey());
				constrainList.add(activity.getValue().constrain);
				// System.out.println("Constrain : " +
				// activity.getValue().constrain);
			}

			traceMap.clear();
			// System.out.println("-----Printing log start -----");
			int eventnumber = 0;
			int traceno = 0;
			int check = 0;
			for (XTrace xtrace : xlog) {

				String traceName = XConceptExtension.instance().extractName(
						xtrace);
				traceList.clear();

				System.out.println("TraceName: "
						+ traceName.substring(
								traceName.lastIndexOf(".", traceName.length()))
								.trim());
				for (XEvent event : xtrace) {
					TraceAlphabet tc = new TraceAlphabet();
					String activityName = XConceptExtension.instance()
							.extractName(event);
					traceList.add(DeclareLogGenerator
							.getAlphabetKey(activityName));
					System.out.println("Char : " + activityName + " Key: "
							+ DeclareLogGenerator.getAlphabetKey(activityName)
							+ " Event :" + eventnumber);
					String newKey = DeclareLogGenerator
							.getAlphabetKey(activityName);
					tc.alphabetKey = newKey;
					tc.alphabetKey = DeclareLogGenerator
							.getAlphabetKey(activityName);
					tc.eventNo = eventnumber;
					tc.traceNo = traceno;
					tc.isFirstKey = ((sourceList.indexOf(DeclareLogGenerator
							.getAlphabetKey(activityName)) > -1));
					tc.isMapped = false;
					int indx = sourceList.indexOf(newKey);
					if (indx > -1)
						tc.constrain = constrainList.get(indx);
					traceMap.put(eventnumber, tc);
					eventnumber++;
				}
				check = 0;
				traceno++;
			}

			// processing log ....

			ArrayList<Integer> traceIndex = new ArrayList<Integer>();
			ArrayList<String> traceName = new ArrayList<String>();
			ArrayList<String> isAdded = new ArrayList<String>();

			int traceNo = 0;
			traceList.clear();
			for (Entry<Integer, TraceAlphabet> activity : traceMap.entrySet()) {
				Integer k = activity.getKey();
				TraceAlphabet bb = activity.getValue();
				// System.out.println("event: "+bb.eventNo + " key :" +
				// bb.alphabetKey + ":" + bb.constrain);
				if (traceNo != bb.traceNo) {
					System.out.println("Trace No:  " + (traceNo));
					for (int i = 0; i < traceList.size(); i++) {

						if (sourceList.indexOf(traceList.get(i)) != -1) {
							targetList.clear();
							TraceAlphabet traceEvent = traceMap.get(traceIndex
									.get(i));
							// Graph g = new Graph(600);
							traceEvent.traceNo = traceNo;
							traceEvent.alphabetKey = traceList.get(i);
							traceEvent.selectedSource = traceList.get(i);
							traceEvent.sourceIndex = traceIndex.get(i);
							traceEvent.isMapped = true;
							traceEvent.isFirstKey = ((sourceList
									.indexOf(traceList.get(i)) > -1));
							targetList = getCorrelationList(traceList.get(i));
							ArrayList<String> temp = new ArrayList<String>();
							ArrayList<Integer> temp2 = new ArrayList<Integer>();
							temp2.clear();
							temp.clear();
							temp = getmatchList(traceList, targetList, i,
									traceIndex, traceList.get(i), temp2);
							// System.out.println("Source : " + traceList.get(i)
							// + "(" +traceIndex.get(i) +")");
							if (!temp.isEmpty()) {
								traceEvent.targetList = temp;
								traceEvent.targetListIndex = temp2;
								Map<String, Map<String, ArrayList<Integer>>> targetedListwithIndex = new HashMap<String, Map<String, ArrayList<Integer>>>();
								Map<String, ArrayList<Integer>> targetMap = new HashMap<String, ArrayList<Integer>>();
								isAdded.add(traceList.get(i));
								isAdded.clear();
								for (int ww = 0; ww < temp.size(); ww++) {
									String key = temp.get(ww);
									ArrayList<Integer> traces = new ArrayList<Integer>();
									if (isAdded.indexOf(key) == -1) {
										isAdded.add(key);
										for (int t = i + 1; t < traceList
												.size(); t++) {
											if (traceList.get(t).contains("_")) {
												if (key.equals(traceList.get(t))) {
													traces.add(traceIndex
															.get(t));
												} else {
													String keys[] = traceList
															.get(t).split("_");
													if (key.equals(keys[0])
															|| key.equals(keys[1])) {
														traces.add(traceIndex
																.get(t));
													}

												}
											} else

											if (key.equals(traceList.get(t))) {
												traces.add(traceIndex.get(t));
											}

										}
										targetMap.put(key.substring(0, 1),
												traces);
									}
								}

								//Not required after branch issue
							//	targetedListwithIndex.put(traceList.get(i),
							//			targetMap);
							//	traceEvent.targetedListwithIndex = targetedListwithIndex;

							}

							//traceMap2.put(traceIndex.get(i), traceEvent);

						}
					}

					traceNo = bb.traceNo;
					// targetIndex.clear();
					traceIndex.clear();
					traceList.clear();
					isAdded.clear();
					// traceNo ;
				}
				// targetIndex.add(bb.targetIndex);
				traceIndex.add(k);
				traceList.add(bb.alphabetKey);
			}

			for (int iX = 0; iX < traceList.size(); iX++) {
				if (sourceList.indexOf(traceList.get(iX)) != -1) {
					targetList.clear();
					TraceAlphabet traceEvent = traceMap.get(traceIndex.get(iX));
					Graph g = new Graph(600);
					// traceEvent.traceNo = traceNo;
					traceEvent.alphabetKey = traceList.get(iX);
					traceEvent.selectedSource = traceList.get(iX);
					traceEvent.sourceIndex = traceIndex.get(iX);
					traceEvent.isMapped = true;
					traceEvent.isFirstKey = ((sourceList.indexOf(traceList
							.get(iX)) > -1));
					targetList = getCorrelationList(traceList.get(iX));
					ArrayList<String> temp = new ArrayList<String>();
					ArrayList<Integer> temp2 = new ArrayList<Integer>();
					temp2.clear();
					temp.clear();
					temp = getmatchList(traceList, targetList, iX, traceIndex,
							traceList.get(iX), temp2);
					// System.out.println("Source : " + traceList.get(iX) + "("
					// +traceIndex.get(iX) +")");
					if (!temp.isEmpty()) {
						traceEvent.targetList = temp;
						traceEvent.targetListIndex = temp2;
						Map<String, Map<String, ArrayList<Integer>>> targetedListwithIndex = new HashMap<String, Map<String, ArrayList<Integer>>>();
						Map<String, ArrayList<Integer>> targetMap = new HashMap<String, ArrayList<Integer>>();
						isAdded.clear();
						for (int ww = 0; ww < temp.size(); ww++) {
							String key = temp.get(ww);
							ArrayList<Integer> traces = new ArrayList<Integer>();
							if (isAdded.indexOf(key) == -1) {
								isAdded.add(key);
								for (int t = iX + 1; t < traceList.size(); t++) {
									if (traceList.get(t).contains("_")) {
										if (key.equals(traceList.get(t))) {
											traces.add(traceIndex.get(t));
										} else {
											String keys[] = traceList.get(t)
													.split("_");
											if (key.equals(keys[0])
													|| key.equals(keys[1])) {
												traces.add(traceIndex.get(t));
											}

										}
									} else if (key.equals(traceList.get(t))) {
										traces.add(traceIndex.get(t));
									}
									// }

								}
								targetMap.put(key.substring(0, 1), traces);
							}
						}
					//not required after branch issue.
						//targetedListwithIndex.put(traceList.get(iX), targetMap);
					//	traceEvent.targetedListwithIndex = targetedListwithIndex;
						// System.out.println(ilp.toString());
					}
					//traceMap2.put(traceIndex.get(iX), traceEvent);
				}
				// }
			}
		} catch (Exception e) {

			// Deal with e as you please.
			// e may be any type of exception at all.

		}
	
		
	}

	/* 
	 * check and test of random correlation from multiple  activities
	 * based on Graph  - not useful for activations only
	 */

	private static void CheckforRandomSelection(ArrayList<Integer> dfs,ArrayList<Integer> traceIndex, ArrayList<String> traceList) {
		
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

	/* 
	 * Merge both list .. without any change in the original list
	 */
	private static void mergeLists() {
		for (Entry<String, Alphabet> activity : alphabetMapx.entrySet()) {
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

	/* 
	 * Restore the original activity name by replacing TaskChar Values
	 */
	private static void addEventDataRestoreKeys(XLog xlog) {
		int count = 0;
		int check = 0;
		int eventnumber = 0;
		for (XTrace xtrace : xlog) {
			for (XEvent event : xtrace) {
				// event.getAttributes().

				XAttributeMap eventAttributes = event.getAttributes();
				String name = "?";
				int local = 0;
				for (String key : eventAttributes.keySet()) {
					if (local == 0) {
						name = eventAttributes.get(key).toString();
					}
					local++;
				}

				String keyName = getAlphabetKey(name);
				String paylaodName = "Data:";
				int keyValue = (int) Math.random();
				Alphabet ExeActivity = abMapAll.get(keyName);
				if (ExeActivity != null) {
					paylaodName = paylaodName+ ExeActivity.payLoadName;
					keyValue = getRandomTrace(ExeActivity.minValue,
							ExeActivity.maxValue);
				}

				if (paylaodName == null || paylaodName.isEmpty()) {
					paylaodName = "Data";
				}
				XConceptExtension.instance().assignName(event, keyName);
				XFactory xFactory = XFactoryRegistry.instance()
						.currentDefault();
				XAttribute test = xFactory.createAttributeLiteral(paylaodName,
						Integer.toString(keyValue), null);
				XAttributeMap test2 = event.getAttributes();
				test2.put(paylaodName, test);
				event.setAttributes(test2);
				xtrace.set(check, event);
				check++;

				eventnumber++;
			}
			check = 0;

		}
			
	}

	/* 
	 * Correlation Array List
	 */
	private static void addCorrelationtoArray() {
		for (Entry<String, Alphabet> activity : alphabetMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet ret = activity.getValue();

			if (ret != null) {
				if (ret.correlationlist != null) {
					for (int i = 0; i < ret.correlationlist.length; i++) {
						String key = ret.correlationlist[i];
						ret.isActivated = false;
						String condition = ret.actCondition + "::"
								+ ret.relCondition;
						ret.actCondition = condition;
						abMap2.put(key, ret);
					}
				}
			}
		}	
	}


	public static ArrayList<String> getCorrelationList(String key) {
		ArrayList<String> targetList = new ArrayList<String>();

		Alphabet ret = alphabetMapx.get(key);
		if (ret != null) {
			if (ret.correlationlist != null) {
				targetList.clear();
				for (int i = 0; i < ret.correlationlist.length; i++) {
					targetList.add(ret.correlationlist[i]);
				}
			}
		}

		return targetList;
	}
	
	/* 
	 * Print the log with the correlation condition - not in use for Activations
	 */
	
	public static String printme(ArrayList<String> sourceList,ArrayList<String> targetList,
 ArrayList<String> trace, String key,
			int start, int eventnumber) {
		
		String ret = "";
		ArrayList<String> trace2 = new ArrayList<String>();
		for (int kk = start; kk < trace.size(); kk++) {
			trace2.add(trace.get(kk));
		}
		int index = sourceList.indexOf(key);
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
	
	/* 
	 * Create Branches of the activation
	 */
	public static void CreateBranches(XLog xlog) {
		/*try {*/

		ArrayList<String> sourceList = new ArrayList<String>();
		ArrayList<String> targetList = new ArrayList<String>();
		ArrayList<String> traceList = new ArrayList<String>();
		// ArrayList<Integer> targetIndex = new ArrayList<Integer>();

		int count = 0;
		CheckforPrecendence();
		for (Entry<String, Alphabet> activity : alphabetMapx.entrySet()) {
			sourceList.add(activity.getKey());
			constrainList.add(activity.getValue().constrain);
			System.out.println("Constrain : " + activity.getValue().constrain);
		}

		traceMap.clear();
		System.out.println("-----Printing log start -----");
		int eventnumber = 0;
		int traceno = 0;
		int check = 0;
		for (XTrace xtrace : xlog) {

			String traceName = XConceptExtension.instance().extractName(xtrace);
			traceList.clear();

			System.out.println("TraceName: "
					+ traceName.substring(
							traceName.lastIndexOf(".", traceName.length()))
							.trim());
			for (XEvent event : xtrace) {
				TraceAlphabet traceAlphabet = new TraceAlphabet();
				String activityName = XConceptExtension.instance().extractName(
						event);
				traceList.add(DeclareLogGenerator.getAlphabetKey(activityName));
				System.out.println("Char : " + activityName + " Key: "
						+ DeclareLogGenerator.getAlphabetKey(activityName)
						+ " Event :" + eventnumber);
				String newKey = DeclareLogGenerator
						.getAlphabetKey(activityName);
				traceAlphabet.alphabetKey = newKey;
				traceAlphabet.alphabetKey = DeclareLogGenerator
						.getAlphabetKey(activityName);
				traceAlphabet.eventNo = eventnumber;
				traceAlphabet.traceNo = traceno;
				traceAlphabet.isFirstKey = true;
				traceAlphabet.isMapped = false;
				int indx = sourceList.indexOf(newKey);
				if (indx > -1)
					traceAlphabet.constrain = constrainList.get(indx);
				traceMap.put(eventnumber, traceAlphabet);
				eventnumber++;
			}
			check = 0;
			traceno++;
		}

		// processing log ....

		ArrayList<Integer> traceIndex = new ArrayList<Integer>();
		ArrayList<String> traceName = new ArrayList<String>();
		System.out.println("...Log Processing....");
		int traceNo = 0;
		traceList.clear();
		for (Entry<Integer, TraceAlphabet> activity : traceMap.entrySet()) {
			Integer k = activity.getKey();
			TraceAlphabet bb = activity.getValue();
			if (traceNo != bb.traceNo) {
				System.out.println("Duplication in Trace:  " + (traceNo));
				for (int i = 0; i < traceList.size(); i++) {
					if (sourceList.indexOf(traceList.get(i)) != -1) {
						targetList.clear();
						TraceAlphabet eEvent = new TraceAlphabet();
						eEvent.traceNo = traceNo;
						eEvent.alphabetKey = traceList.get(i);
						eEvent.selectedSource = traceList.get(i);
						eEvent.sourceIndex = traceIndex.get(i);
						eEvent.isMapped = true;
						targetList = getCorrelationList(traceList.get(i));
						ArrayList<String> temp = new ArrayList<String>();
						ArrayList<Integer> temp2 = new ArrayList<Integer>();
						temp2.clear();
						temp.clear();
						temp = getmatchList(traceList, targetList, i,
								traceIndex, traceList.get(i), temp2);						
						if (!temp.isEmpty()) {
							eEvent.targetList = temp;
							eEvent.targetListIndex = temp2;
						}
						traceMap2.put(traceIndex.get(i), eEvent);
						
					}
				}

				traceNo = bb.traceNo;
				// targetIndex.clear();
				traceIndex.clear();
				traceList.clear();

				// traceNo ;
			}
			// targetIndex.add(bb.targetIndex);
			traceIndex.add(k);
			traceList.add(bb.alphabetKey);
		}

		for (int iX = 0; iX < traceList.size(); iX++) {
			if (sourceList.indexOf(traceList.get(iX)) != -1) {
				targetList.clear();
				TraceAlphabet traceEvent = new TraceAlphabet();// traceMap.get(traceIndex.get(iX));
				traceEvent.traceNo = traceNo;
				traceEvent.alphabetKey = traceList.get(iX);
				traceEvent.selectedSource = traceList.get(iX);
				traceEvent.sourceIndex = traceIndex.get(iX);
				traceEvent.isMapped = true;
				targetList = getCorrelationList(traceList.get(iX));
				ArrayList<String> temp = new ArrayList<String>();
				ArrayList<Integer> temp2 = new ArrayList<Integer>();
				temp2.clear();
				temp.clear();
				temp = getmatchList(traceList, targetList, iX, traceIndex,
						traceList.get(iX), temp2);
				// System.out.println("Source : " + traceList.get(iX) + "("
				// +traceIndex.get(iX) +")");
				if (!temp.isEmpty()) {
					traceEvent.targetList = temp;
					traceEvent.targetListIndex = temp2;
				}
				traceMap2.put(traceIndex.get(iX), traceEvent);
			}
		}

		System.out.println("....Mapped Trace...");
		for (Entry<Integer, TraceAlphabet> activity : traceMap2.entrySet()) {
			Integer k = activity.getKey();
			TraceAlphabet tx = activity.getValue();
			System.out.println("Eevent : " + k + " Map (A): "
					+ tx.selectedSource + ":" + tx.sourceIndex);
			if ((tx.targetList != null)) {
				for (int m = 0; m < tx.targetList.size(); m++) {
					System.out.println("Trace : " + tx.traceNo + " event :" + k
							+ " source : " + tx.selectedSource + ":"
							+ tx.sourceIndex + " target:"
							+ tx.targetList.get(m) + " : Index : ("
							+ tx.targetListIndex.get(m) + ")");
				}

				System.out.println("---Hello---Random: "
						+ SelectRandomName(tx.targetList));
				System.out.println("");
			}

		}

		System.out.println("-----Printing log end -----");
	}
	

	/* 
	 * Not is use for Actications Only.
	 */
	public static void removeEventswithoutSouce(){
		System.out.println("----------index printing---------");
		
		Iterator<Entry<Integer, TraceAlphabet>> iter = traceMap.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, TraceAlphabet> entry = iter.next();
			if ((entry.getValue().selectedSource == null)
					&& (entry.getValue().selectedSource == null)) {
				iter.remove();
			}
		}
		int traceNo = -1;
		for (Entry<Integer, TraceAlphabet> activity : traceMap.entrySet()) {
			TraceAlphabet bb = activity.getValue();
			if (bb.traceNo != traceNo) {
				traceNo = bb.traceNo;
				System.out.println("----------Trace (" + traceNo+ ")----------");

			}

			System.out.println(" Trace: " + bb.traceNo + " Event:" + bb.eventNo
					+ "  : Source " + bb.selectedSource + "(" + bb.sourceIndex
					+ ")  : Target " + bb.selectedTarget + "(" + bb.targetIndex
					+ ")" + bb.constrain);

		}		
	}
	
	
	/* 
	 * Not is use for Actications Only.
	 */
	public static void selectRandomEvents(){
		ArrayList<String> eventKey = new ArrayList();
		ArrayList<String> matched = new ArrayList();
		ArrayList<Integer> eventNo = new ArrayList();
		ArrayList<String> added = new ArrayList();
		ArrayList<String> targetList = new ArrayList();
		int traceNo = 0;
		for (Entry<Integer, TraceAlphabet> activity : traceMap.entrySet()) {

			TraceAlphabet bb = activity.getValue();
			if (bb.traceNo != traceNo) {
				setRandomTrace(eventKey, eventNo, traceNo);
				System.out.println("Sect " + secondActivationList);

				for (int i = 0; i < secondActivationList.size(); i++) {
					String jx = secondActivationList.get(i);
					String jj[] = jx.split(":");

					targetList = getCorrelationList(jj[0]);
					String found = jj[jj.length - 1];
				}

				traceNo = bb.traceNo;
				System.out.println("----------Trace (" + traceNo+ ")----------");
				eventKey.clear();
				eventNo.clear();
				secondActivationList.clear();
				added.clear();
				matched.clear();

			}
			// eventKey.add(bb.selectedSource);
			eventKey.add(bb.selectedTarget);
			eventNo.add(activity.getKey());
			System.out.println(" Trace: " + bb.traceNo + " Event:" + bb.eventNo
					+ "  : Source " + bb.selectedSource + "(" + bb.sourceIndex
					+ ")  : Target " + bb.selectedTarget + "(" + bb.targetIndex
					+ ")" + bb.constrain);

		}

		setRandomTrace(eventKey, eventNo, traceNo);
	
	}
	
	
	/* 
	 * Not is use for Activations Only.
	 */
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
	
	/* 
	 * Select a random event from trace.
	 */
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

	/* 
	 * Select a random event name from trace.
	 */	
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
	
	/* 
	 * Set a random trace.
	 */
	public static String setRandomTrace(ArrayList<String> eventKey,ArrayList<Integer> eventNo,int traceNo){
		String ret = "";
		ArrayList<String> duplicateList = new ArrayList();
		ArrayList<String> corrList = new ArrayList();
		System.out.println("setRandomTrace");

		for (int i = 0; i < eventKey.size(); i++) {
			System.out.println("i " + i + " event " + eventKey.get(i) + " key "
					+ eventNo.get(i));
			for (int j = i + 1; j < eventKey.size(); j++) {
				if (eventKey.get(i) == eventKey.get(j) && i != j) {
					duplicateList.add(eventKey.get(i));
					String Dupli = eventKey.get(i) + ":" + eventNo.get(j);
					if (secondActivationList.indexOf(Dupli) <= -1)
						secondActivationList.add(Dupli);
					System.out.println("Duplicate" + eventKey.get(j)
							+ ": event " + eventNo.get(j));
				}
			}
		}

		return ret;
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
				targetList = getCorrelationList(sourceList.get(index));
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
	
	/* 
	 * Generating Input Model for MINERFul.
	 */	
	
public static ProcessModel createDataModel (){

	List<TaskChar> addtaskCharList = new ArrayList<TaskChar>();
	List<Constraint> constraintList = new ArrayList<Constraint>();
	List<String> targetList = new  ArrayList<String>();
	
	/* 
	 * Get All activites .
	 */	
		for (Entry<String, Alphabet> activity : alphabetMapx.entrySet()) {
			String key = activity.getKey();
			Alphabet alphabet = activity.getValue();
			targetList.add(key);
			if (alphabet.correlationlist != null) {
				for (int j = 0; j < alphabet.correlationlist.length; j++) {
					targetList.add(alphabet.correlationlist[j]);
				}
			}

			else {
				String alphabetname = BranchCombination.getParentLetter(key);
				String xnam = " 1st : " + key.replace(key, alphabet.secondAlphabetKey);// alphabet.correlationlist[j];
																		// \
				xnam = key.replace(alphabetname, alphabet.secondAlphabetKey);
				targetList.add(xnam);
			}
		}
	
		/* 
		 * Sort the list by Converting it into collections
		 */	
		if (!targetList.isEmpty()) {
			Set<Object> strSet = Arrays.stream(targetList.toArray()).collect(
					Collectors.toSet());
			targetList.clear();

			for (Object s : strSet) {
				targetList.add((String) s);
			}
		}

		Collections.sort(targetList);
	
		
		/* 
		 * Generate TaskChar for Model .. 65 == a
		 */	
		int theEnd = 65 + targetList.size();
		for (int vchar = 65; vchar < theEnd; vchar++) {
			char c = (char) vchar;
			System.out.println("Char: " + c);
			TaskChar cc = new TaskChar(c);
			addtaskCharList.add(cc);
		}
	
		for (int i = 0; i < targetList.size(); i++) {
			newLogIndex.put(addtaskCharList.get(i), targetList.get(i));
			System.out.println("Map:  Key: " + addtaskCharList.get(i) + " Char: "
					+ targetList.get(i));
		}
	
	
		/* 
		 * Mapped or create a Constraints
		 */	
	for (Entry<String, Alphabet> activity : alphabetMapx.entrySet()) {
			String key = activity.getKey();
			Alphabet alphabet = activity.getValue();

			TaskChar firstChar = getAlphabetValue(key);// newLogIndex.get(key);

			ArrayList<TaskChar> taskCharList = new ArrayList<TaskChar>();
			TaskCharSet target = new TaskCharSet();
			taskCharList.clear();

			if (alphabet.constrain.equals("existence")) {
			
				/* 
				 * if not activation continue
				 */	
				if (alphabet.isSingle) {
					continue;
				}
			}

			/* 
			 * special case for absence
			 */	
			if (alphabet.constrain.equals("absence")) {
				if (alphabet.isSingle) {
					continue;
				}
			}
		
		
			String xKey = key.substring(key.length() - 1);

			if (xKey.equals("0")) {
				if (alphabet.correlationlist != null){
					for(int j=0; j < alphabet.correlationlist.length ; j++){
						String xnam =alphabet.correlationlist[j];
						taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
						int len = alphabet.correlationlist[j].length();
						if ((xnam.contains("_"))
								&& (xnam.contains(alphabet.secondAlphabetKey))) {
							String sx[] = xnam.split("_");
							String first = BranchCombination.getParentLetter(key);
							String last = BranchCombination.getParentLetter(sx[0]);
							first = key.replace(first, "");
							last = sx[0].replace(last, "");
							System.out.println("First" + first + " :Lst" + last);
							if (first.equals(last)) {
								taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));								
							}
						} 
					}
				}
				
			} // end of zero level first part
		
			
			// end of one leerl
			else {		
			if (alphabet.correlationlist != null){
				for(int j=0; j < alphabet.correlationlist.length ; j++){
					String xnam =alphabet.correlationlist[j];
					int len = alphabet.correlationlist[j].length();
						if ((xnam.contains("_"))
								&& (xnam.contains(alphabet.secondAlphabetKey))) {
							String sx[] = xnam.split("_");
							String first = BranchCombination
									.getParentLetter(key);
							String last = BranchCombination
									.getParentLetter(sx[0]);
							first = key.replace(first, "");
							last = sx[0].replace(last, "");
							System.out
									.println("First" + first + " :Lst" + last);
						
							if (alphabet.isRoot) {
								taskCharList
										.add(getAlphabetValue(alphabet.correlationlist[j]));

							}
						} else if ((alphabet.correlationlist[j].length() < key
								.length() + 3)
								&& (xnam.contains(alphabet.secondAlphabetKey))) {
							taskCharList
									.add(getAlphabetValue(alphabet.correlationlist[j]));

						}
					}
				
				//**********************Chain Constraint Start*****************
				// check for Chain Constraints if it is not a single activity - un-comment below two lines
				/*if(!alphabet.isSingle)
				checkforChainConstraint(taskCharList,addtaskCharList,alphabet);*/
				//**********************Chain Constraint End*****************
				
				
		
			} else {
				String alphabetname = BranchCombination.getParentLetter(key);
				String xnam = key.replace(key, alphabet.secondAlphabetKey);// alphabet.correlationlist[j];
																			// \
				if ((xnam.length() + 3 == key.length())
						&& (xnam.contains(alphabet.secondAlphabetKey))) {
					xnam = key.replace(alphabetname, alphabet.secondAlphabetKey);
					
					taskCharList.add(getAlphabetValue(xnam));
				}
			}  // end of alphabet empy
			} // end of zero level check
			
		
			// create new model for log generation..
		     switch (alphabet.constrain) {
	            case "response": 
				Response response = new Response(new TaskCharSet(firstChar),
						new TaskCharSet(taskCharList));
				constraintList.add(response);
				if (alphabet.correlationlist != null) {
					for (int j = 1; j < alphabet.correlationlist.length; j++) {
						taskCharList.clear();
						targetList.add(alphabet.correlationlist[j]);
						if (alphabet.isRoot == false) {
							taskCharList
									.add(getAlphabetValue(alphabet.correlationlist[j]));

							if (!addConstraints(alphabet.correlationlist[j], firstChar, taskCharList,
									constraintList)) {
								Response response2 = new Response(
										new TaskCharSet(firstChar),
										new TaskCharSet(taskCharList));
								constraintList.add(response2);
							}

						}
					}

				}
						break;
						
	            case "alternateresponse" :
						AlternateResponse res = new AlternateResponse(new TaskCharSet(
								firstChar), new TaskCharSet(taskCharList));
						constraintList.add(res);
						if (alphabet.correlationlist != null) {
							for (int j = 1; j < alphabet.correlationlist.length; j++) {
								taskCharList.clear();
								targetList.add(alphabet.correlationlist[j]);
								if (alphabet.isRoot == false) {
									taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
								/*	*/
									
									if (!addConstraints(alphabet.correlationlist[j],firstChar,taskCharList, constraintList)){
										AlternateResponse res2 = new AlternateResponse(
												new TaskCharSet(firstChar),
												new TaskCharSet(taskCharList));
										constraintList.add(res2);
									}
		
								}
							}
		
						}
						break;
						
	            case "notresponse" :
					NotSuccession notresponse = new NotSuccession(new TaskCharSet(
							firstChar), new TaskCharSet(taskCharList));
					constraintList.add(notresponse);
					if (alphabet.correlationlist != null) {
						for (int j = 1; j < alphabet.correlationlist.length; j++) {
							taskCharList.clear();
							targetList.add(alphabet.correlationlist[j]);
	
							if (alphabet.isRoot == false) {
								taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
								/**/
								
								if (!addConstraints(alphabet.correlationlist[j],firstChar,taskCharList, constraintList)){
									NotSuccession notresponse2 = new NotSuccession(
											new TaskCharSet(firstChar),
											new TaskCharSet(taskCharList));
									constraintList.add(notresponse2);
								}
	
							}
						}
					}
					break;
	         
	            case "respondedexistence" :
						RespondedExistence respondedexistence = new RespondedExistence(
								new TaskCharSet(firstChar), new TaskCharSet(taskCharList));
						constraintList.add(respondedexistence);
						if (alphabet.correlationlist != null) {
							for (int j = 1; j < alphabet.correlationlist.length; j++) {
								taskCharList.clear();
								targetList.add(alphabet.correlationlist[j]);
		
								if (alphabet.isRoot == false) {
									taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
									/**/
									if (!addConstraints(alphabet.correlationlist[j],firstChar,taskCharList, constraintList)){
										RespondedExistence respondedexistence2 = new RespondedExistence(
												new TaskCharSet(firstChar),
												new TaskCharSet(taskCharList));
										constraintList.add(respondedexistence2);
									}
								}
							}
		
						}
						break;
	        
	            case "notchainresponse" :
					NotChainSuccession notchainresponse = new NotChainSuccession(
							new TaskCharSet(firstChar), new TaskCharSet(taskCharList));
					constraintList.add(notchainresponse);
					if (alphabet.correlationlist != null) {
						for (int j = 1; j < alphabet.correlationlist.length; j++) {
							taskCharList.clear();
							targetList.add(alphabet.correlationlist[j]);
	
							if (alphabet.isRoot == false) {
								taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
								
								if (!addConstraints(alphabet.correlationlist[j],firstChar,taskCharList, constraintList)){
									NotChainSuccession notchainresponse2 = new NotChainSuccession(
											new TaskCharSet(firstChar),
											new TaskCharSet(taskCharList));
									constraintList.add(notchainresponse2);
								} 
	
							}
						}
					}
					break;
					
	            case "chainresponse":	            	
						ChainResponse chainresponse = new ChainResponse(
								new TaskCharSet(firstChar), new TaskCharSet(taskCharList));
						constraintList.add(chainresponse);
						if (alphabet.correlationlist != null) {
							for (int j = 1; j < alphabet.correlationlist.length; j++) {
								taskCharList.clear();
								targetList.add(alphabet.correlationlist[j]);
								if (alphabet.isRoot == false) {
									taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
									/*ChainResponse chainresponse2 = new ChainResponse(
											new TaskCharSet(firstChar),
											new TaskCharSet(taskCharList));
									constraintList.add(chainresponse2);*/
									
									if (!addConstraints(alphabet.correlationlist[j],firstChar,taskCharList, constraintList)){
										ChainResponse chainresponse2 = new ChainResponse(
												new TaskCharSet(firstChar),
												new TaskCharSet(taskCharList));
										constraintList.add(chainresponse2);
									} 
								}
							}
		
						}		
						break;
					
	            case "notrespondedexistence":
						NotCoExistence notrespondedexistence = new NotCoExistence(new TaskCharSet(
								firstChar), new TaskCharSet(taskCharList));
						constraintList.add(notrespondedexistence);
						if (alphabet.correlationlist != null) {
							for (int j = 1; j < alphabet.correlationlist.length; j++) {
								taskCharList.clear();
								targetList.add(alphabet.correlationlist[j]);
		
								if (alphabet.isRoot == false) {
									taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
																
									if (!addConstraints(alphabet.correlationlist[j],firstChar,taskCharList, constraintList)){
										NotCoExistence notrespondedexistence2 = new NotCoExistence(
												new TaskCharSet(firstChar),
												new TaskCharSet(taskCharList));
										constraintList.add(notrespondedexistence2);
									} 
		
								}// false
							}
						}
						break;
	            
	            case "precedence":
					Precedence precedence = new Precedence(new TaskCharSet(taskCharList),
							new TaskCharSet(firstChar));
					constraintList.add(precedence);
					if (alphabet.correlationlist != null) {
						for (int j = 1; j < alphabet.correlationlist.length; j++) {
							taskCharList.clear();
							targetList.add(alphabet.correlationlist[j]);
							if (alphabet.isRoot == false) {
								taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
								
								
								if (!addConstraints(alphabet.correlationlist[j],firstChar,taskCharList, constraintList)){
									Precedence precedence2 = new Precedence(
											new TaskCharSet(taskCharList), new TaskCharSet(
													firstChar));
									constraintList.add(precedence2);
								} 
	
	
							}
						}
					}
					break;
					
	            case "alternateprecedence" :
						AlternatePrecedence alternateprecedence = new AlternatePrecedence(
								new TaskCharSet(taskCharList), new TaskCharSet(firstChar));
						constraintList.add(alternateprecedence);
	
						if (alphabet.correlationlist != null) {
							for (int j = 1; j < alphabet.correlationlist.length; j++) {
								taskCharList.clear();
								targetList.add(alphabet.correlationlist[j]);
								if (alphabet.isRoot == false) {
									taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
									
									
									if (!addConstraints(alphabet.correlationlist[j],firstChar,taskCharList, constraintList)){
										AlternatePrecedence alternateprecedence2 = new AlternatePrecedence(
												new TaskCharSet(taskCharList), new TaskCharSet(
														firstChar));
										constraintList.add(alternateprecedence2);
									}
								}
							}
	
						}				
						break;
	            	
	            case "chainprecedence" :
							ChainPrecedence chainprecedence = new ChainPrecedence(
									new TaskCharSet(taskCharList), new TaskCharSet(firstChar));
							constraintList.add(chainprecedence);
			
							if (alphabet.correlationlist != null) {
								for (int j = 1; j < alphabet.correlationlist.length; j++) {
									taskCharList.clear();
									targetList.add(alphabet.correlationlist[j]);
									if (alphabet.isRoot == false) {
										taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
										
										
										if (!addConstraints(alphabet.correlationlist[j],firstChar,taskCharList, constraintList)){
											ChainPrecedence chainprecedence2 = new ChainPrecedence(
													new TaskCharSet(taskCharList), new TaskCharSet(
															firstChar));
											constraintList.add(chainprecedence2);
										}
			
									}
								}
			
							}
		            	break;
		            	
		            case "notprecedence":
							NotSuccession notprecedence = new NotSuccession(
									new TaskCharSet(taskCharList), new TaskCharSet(firstChar));
							constraintList.add(notprecedence);
			
							if (alphabet.correlationlist != null) {
								for (int j = 1; j < alphabet.correlationlist.length; j++) {
									taskCharList.clear();
									targetList.add(alphabet.correlationlist[j]);
									if (alphabet.isRoot == false) {
										taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
										
										
										if (!addConstraints(alphabet.correlationlist[j],firstChar,taskCharList, constraintList)){
											NotSuccession notprecedence2 = new NotSuccession(
													new TaskCharSet(taskCharList), new TaskCharSet(
															firstChar));
											constraintList.add(notprecedence2);
										}
			
									}
								}
			
							}
		            	break;
		            	
		            case "notchainprecedence" :		            	
		            	NotChainSuccession notchainprecedence = new NotChainSuccession(new TaskCharSet(taskCharList),
								new TaskCharSet(firstChar));
						constraintList.add(notchainprecedence);
						if (alphabet.correlationlist != null) {
							for (int j = 1; j < alphabet.correlationlist.length; j++) {
								taskCharList.clear();
								targetList.add(alphabet.correlationlist[j]);
								if (alphabet.isRoot == false) {
									taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
									
									
									if (!addConstraints(alphabet.correlationlist[j],firstChar,taskCharList, constraintList)){
										NotChainSuccession notchainprecedence2 = new NotChainSuccession(
												new TaskCharSet(taskCharList), new TaskCharSet(
														firstChar));
										constraintList.add(notchainprecedence2);
									}
		
								}
							}
						}		
		           	break;	            	
	            	
	            case "existence": 
			            	if (alphabet.correlationlist != null) {
								for (int j = 0; j < alphabet.correlationlist.length; j++) {							
									targetList.add(alphabet.correlationlist[j]);
								
									if (alphabet.isRoot == false) {
										taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));								
									}
								}
		
							} 			
						
						Participation existence = new Participation(new TaskCharSet(taskCharList)); 			
						constraintList.add(existence);
			        break;
			            	
		            	
	            case "init" :
						if (alphabet.correlationlist != null) {
							for (int j = 0; j < alphabet.correlationlist.length; j++) {
								targetList.add(alphabet.correlationlist[j]);
		
								if (alphabet.isRoot == false) {
									taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
								}// false
							}
		
						}
		
						Init init = new Init(new TaskCharSet(taskCharList));
						constraintList.add(init);
	            	break;
	            	
	            case "last":
						if (alphabet.correlationlist != null) {
							for (int j = 0; j < alphabet.correlationlist.length; j++) {		
								targetList.add(alphabet.correlationlist[j]);		
								if (alphabet.isRoot == false) {
									taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
		
								}
							}
		
						}		
						End last = new End(new TaskCharSet(taskCharList));
						constraintList.add(last);		
					break;
					
	            case "absence":	            	
				/*	if (alphabet.correlationlist != null) {
						for (int j = 0; j < alphabet.correlationlist.length; j++) {
							targetList.add(alphabet.correlationlist[j]);
						
							if (alphabet.isRoot == false) {
								taskCharList.add(getAlphabetValue(alphabet.correlationlist[j]));
								
							}// false
						}

					} 	*/		
	 			 // Speical case for absense
	            	taskCharList.clear();
	            	taskCharList.add(firstChar);
					AtMostOne absence = new AtMostOne(new TaskCharSet(taskCharList));
					constraintList.add(absence);   	
	            	break;
	           
	            default: 
	            	break;
	        }		     
	} 
		     
		  
		Set<TaskChar> alphabetS = new TreeSet<TaskChar>(addtaskCharList);
		int i = 0;

		TaskCharEncoderDecoder taChEnDe = new TaskCharEncoderDecoder();
		taChEnDe.encode(alphabetS);
		TaskCharArchive taChaAr = new TaskCharArchive(
				taChEnDe.getTranslationMap());
		for (TaskChar tCh : alphabetS) {
			tCh.identifier = taChaAr.getTaskChar(tCh.taskClass).identifier;
		}

		ConstraintsBag bag = new ConstraintsBag(alphabetS);
		for (Constraint con : constraintList) {
			System.out.println(con + " :RE> " + con.getRegularExpression());
			bag.add(con.getBase(), con);
		}
		ProcessModel proMod = new ProcessModel(taChaAr, bag);
		return proMod;

}
	
/* 
 * Function for Adding Chain Constraints
 *  */	
	private static void checkforChainConstraint(
			ArrayList<TaskChar> taskCharList, List<TaskChar> addtaskCharList,
			Alphabet alphabet) {
		if (alphabet.correlationlist != null) {
			for (int j = 0; j < alphabet.correlationlist.length; j++) {
				for (Entry<String, Alphabet> activity : alphabetMapx.entrySet()) {
					if (!activity.getKey().equals(alphabet.alphabetkey)) {
						if (BranchCombination
								.getParentLetter(activity.getKey()).equals(
										alphabet.correlationlist[j])) {
							TaskChar tk = getAlphabetValue(activity.getKey());
							if (!(tk == null)) {
								taskCharList.add(tk);
							}
						}
					}
				}

			}

		}

	}

	/* 
	 * add Constraints in activation based on the given condition
	 *  */	
	
	public static boolean addConstraints(String key, TaskChar firstChar,
			List<TaskChar> taskCharList, List<Constraint> constraintList) {
		
				
		Alphabet activityConstraint = acitivityConstraintList.get(key);
		if (activityConstraint == null) {
			return false;
		}

		

		switch (activityConstraint.constrain) {
		case "response":
			Response response2 = new Response(new TaskCharSet(firstChar),
					new TaskCharSet(taskCharList));
			constraintList.add(response2);
			break;
		case "chainresponse":
			ChainResponse chainresponse2 = new ChainResponse(new TaskCharSet(
					firstChar), new TaskCharSet(taskCharList));
			constraintList.add(chainresponse2);
			break;
		case "notresponse":
			NotSuccession notresponse3 = new NotSuccession(new TaskCharSet(
					firstChar), new TaskCharSet(taskCharList));
			constraintList.add(notresponse3);
			break;
		case "alternateresponse":
			AlternateResponse res2 = new AlternateResponse(new TaskCharSet(
					firstChar), new TaskCharSet(taskCharList));
			constraintList.add(res2);
			break;
		case "respondedexistence":
			RespondedExistence respondedexistence2 = new RespondedExistence(
					new TaskCharSet(firstChar), new TaskCharSet(taskCharList));
			constraintList.add(respondedexistence2);
			break;
			
		case "notchainresponse" : 
			NotChainSuccession notchainresponse2 = new NotChainSuccession(
					new TaskCharSet(firstChar),
					new TaskCharSet(taskCharList));
			constraintList.add(notchainresponse2);
			break;
			
		case "notrespondedexistence" :
			NotCoExistence notrespondedexistence2 = new NotCoExistence(
					new TaskCharSet(firstChar),
					new TaskCharSet(taskCharList));
			constraintList.add(notrespondedexistence2);
			break;
			
		case "precedence": 
			Precedence precedence2 = new Precedence(
					new TaskCharSet(taskCharList), new TaskCharSet(
							firstChar));
			constraintList.add(precedence2);
			break;
			
			
		case "alternateprecedence": 
			AlternatePrecedence alternateprecedence2 = new AlternatePrecedence(
					new TaskCharSet(taskCharList), new TaskCharSet(
							firstChar));
			constraintList.add(alternateprecedence2);
			break;
			
			
		case "chainprecedence": 
			ChainPrecedence chainprecedence2 = new ChainPrecedence(
					new TaskCharSet(taskCharList), new TaskCharSet(
							firstChar));
			constraintList.add(chainprecedence2);
			break;
			
		case "notprecedence": 
			NotSuccession notprecedence2 = new NotSuccession(
					new TaskCharSet(taskCharList), new TaskCharSet(
							firstChar));
			constraintList.add(notprecedence2);
			break;
			
		case "notchainprecedence" : 
			NotChainSuccession notchainprecedence2 = new NotChainSuccession(
					new TaskCharSet(taskCharList), new TaskCharSet(
							firstChar));
			constraintList.add(notchainprecedence2);
			break;
			
			
		default:
			return false;

		}

		return true;
	}

	/* 
	 *Get Value of TaskChar from the Generated List
	 *  */	
	
public static TaskChar getAlphabetValue(String search){
	TaskChar ret = null; 
		for (Entry<TaskChar, String> entry : newLogIndex.entrySet()) {
			String key = entry.getValue();
			TaskChar t1 = entry.getKey();
			if (key.equals(search)) {
				ret = t1;
				// System.out.println(t1 + ": "+ key + " : " + search);
				break;
			}
		}
		return ret;
}

public static String getAlphabetKey(String search){
	String ret = null; 
		for (Entry<TaskChar, String> entry : newLogIndex.entrySet()) {
			String key = entry.getValue();
			String t1 = entry.getKey().toString();
			if (t1.equals(search)) {
				ret = key;
				// System.out.println(t1 + ": "+ key + " : " + search);
				break;
			}
		}
		return ret;
}




/* 
 *Special conversion of activites for precedence
 *for example opposite of Response.
 *  */	
private static void CheckforPrecendence() {
		
	LinkedHashMap<String, Alphabet> abMaptemp = new LinkedHashMap<String, Alphabet>();
		String aa ="";
		String bb = "";
		for (Entry<String, Alphabet> activity : alphabetMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet alphabet = activity.getValue();
			aa="";bb="";
			if (alphabet.constrain.contains("precedence")){
				
				System.out.println("A :" + alphabet.alphabetkey);
				System.out.println("B :" + alphabet.secondAlphabetKey);
				
				aa = BranchCombination.getParentLetter(k);
				bb = BranchCombination.getParentLetter(alphabet.secondAlphabetKey);
				k = k.replace(aa, bb);
				alphabet.alphabetkey = alphabet.alphabetkey.replace(aa,bb);
				alphabet.alphabetname = alphabet.alphabetname.replace(aa,bb);
				alphabet.secondAlphabetKey = aa;
				//alphabet.secondAlphabetKey = alphabet.secondAlphabetKey.replace(bb,aa);
				String corr [] = alphabet.correlationlist;
				if (corr!=null)
				{
					for (int i=0; i < corr.length; i++){						
					corr[i] = corr[i].replace(bb, aa);	
					}
				alphabet.correlationlist = corr;	
				}
				
			}
			abMaptemp.put(k,alphabet);
		}
		alphabetMapx.clear();
		alphabetMapx=abMaptemp;	
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
			int ind = alphabetList.indexOf(lst[0]);
			if (max > 1) {
				BranchCombination.GetCombination(lst[0], max, combinedList);
				repeatList.add(combinedList.get(combinedList.size() - 1));
			} else {
				combinedList.add(lst[0]);
			}
		}
		System.out.println("");
	}
	
	public static void SetFirstLetters() {
		for (int k = 0; k < combinedList.size(); k++) {
			String[] kk = combinedList.get(k).toString().split(" ");
			String name = combinedList.get(k);
			System.out.println(name);
			if (kk.length <= 1) {
				getRootCondition(name.replaceAll(" ", ""));
			} else {
				// String name=
				// combinedList.get(k).toString().replaceAll(" ","");
				getUpperLevelCondition(name);
			}
		}
	}

	public static String getAcitvationCondition(String a) {
		for (int i = 0; i < firstactivationList.size(); i++) {
			if (firstactivationList.get(i).equals(a)) {
				return firstConditionList.get(i);
			}
		}
		return "Error:ActivationCondition";
	}
	
	public static String getUpperLevelCondition(String alphabets) {
		String cond = "";
		String temp = "";
		String found = "";
		String result = "";
		String[] alphabetsList = alphabets.split(" ");
		for (int rep = 0; rep < repeatList.size(); rep++) {
			for (int j = 0; j < alphabetsList.length; j++) {
				if (repeatList.get(rep).contains(alphabetsList[j])) {
					found = repeatList.get(rep).toString();
					break;
				}
			}
		}

		String[] sList = found.split(" ");
		for (int upper = 0; upper < sList.length; upper++) {
			temp = "";
			for (int ind = 0; ind < alphabetsList.length; ind++) {
				if (alphabetsList[ind].equals(sList[upper])) {
					temp = alphabetsList[ind];
					result = getAcitvationCondition(temp);
					break;
				}
			}
			if (temp.isEmpty()) {
				temp = sList[upper];
				result = BranchCombination
						.divertCondition(getAcitvationCondition(temp));
			}

			if (!cond.isEmpty()) {
				cond = cond + "::";
			}
			cond = cond + result;// BranchCombination.divertCondition(temp);

		}
		return cond;
	}// if contains ss
	
	
	public static String getRootCondition(String alphabet) {
		String allCond = "";
		String cond = "";
		String temp = "";
		for (int rep = 0; rep < repeatList.size(); rep++) {
			if (repeatList.get(rep).contains(alphabet)) {
				allCond = repeatList.get(rep).toString();
				String[] conditionlist = allCond.split(" ");
				for (int ind = 0; ind < conditionlist.length; ind++) {
					temp = getAcitvationCondition(conditionlist[ind]);
					if (!cond.isEmpty())
						cond = cond + "::";
					if (conditionlist[ind].equals(alphabet)) {
						cond = cond + temp;
					} else {
						cond = cond + BranchCombination.divertCondition(temp);
					}
				}// end of sst loop
				break;
			}// if contains ss
		}
		return cond;
	}
	public static void getAlphabets(AssignmentModel model) {
		
		countedList.clear();
		String firstName = "";
		String constrain = "";
		String lastname = "";
	//First Step
		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model
				.getConstraintDefinitions()) {
			constrain = cd.getName().replace("-", "").replace(" ", "")
					.toLowerCase();
			firstName = "";
			lastname = "";
			for (Parameter p : cd.getParameters()) {

				for (nl.tue.declare.domain.model.ActivityDefinition ad : cd
						.getBranches(p)) {
					// System.out.println(ad.getName());
					if (constrain.contains("precedence")) {
						if (p.getName().equals("B")) {
							firstName = ad.getName().toString();
							firstName = firstName.replace(" ", "");
							countedList.add(firstName);
							alphabetList.add(lastname);
							alphabetList.add(firstName);
							countedListB.add(firstName);
							// countedListB.add(firstName);
						} else {
							// alphabetList.add(ad.getName());
							lastname = ad.getName();
							lastname = lastname.replace(" ", "");
						}
					} else {
						if (p.getName().equals("A")) {
							firstName = ad.getName().toString();
							System.out.println(firstName);
							firstName = firstName.replace(" ", "");
							countedList.add(firstName);
							alphabetList.add(firstName);
							// countedListB.add(firstName);
						} else {
							String temp = ad.getName().replace(" ", "");
							alphabetList.add(temp);
							countedListB.add(temp);
							lastname = temp;
						//	System.out.println(ad.getName());
						}
					}
					
					
					if (!lastname.isEmpty()) {
						Alphabet constraints = new Alphabet();
						constraints.constrain = constrain;
						constraints.alphabetkey = lastname;
						constraints.alphabetname = lastname;
						acitivityConstraintList.put(lastname, constraints);
					}
					//System.out.println("Last Name: " + lastname + ": Const: "+ constrain);
				}
			}
		}
		BranchCombination.countAlphabets(countedList);
		
		
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
		for (int i = 0; i < alphabets1.size(); i++) {
			if (alphabets1.get(i).toString() == alph) {
				ret++;
			}
		}
		return ret;
	}
	

	public static void checkActivation(AssignmentModel model) {
		//String condef;
		// alphabets.clear();
		alphabets1.clear();
		// conditions.clear();
		firstactivationList.clear();
		firstConditionList.clear();
		abMapdata.clear();
		String vkey = "";
		int check = 0;
		
		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model
				.getConstraintDefinitions()) {
			String constrain = cd.getName().replace("-", "").replace(" ", "")
					.toLowerCase();
			Alphabet alphabet = new Alphabet();
			alphabet.constrain = constrain;
			alphabet.actCondition = getActivationCondition(cd.getCondition().toString());
			alphabet.relCondition = getcorrelationCondition(cd.getCondition().toString());
			check = 0;
			for (Parameter p : cd.getParameters()) {
				for (nl.tue.declare.domain.model.ActivityDefinition ad : cd
						.getBranches(p)) {

					if (constrain.contains("precedence")) {
						if (p.getName().equals("B")) {
							String sname = ad.getName();
							sname = sname.replace(" ", "");
							firstactivationList.add(sname + checkList(sname));
							alphabets1.add(sname);
							if (!cd.getCondition().toString().isEmpty()) {
								firstConditionList.add(getActivationCondition(cd.getCondition()
										.toString()));
								CorrelationContList.add(getcorrelationCondition(cd
										.getCondition().toString()));
							} else {
								firstConditionList.add("");
								CorrelationContList.add("");
							}
						} // / end of A

					} else {

						// Start of A
						if (p.getName().equals("A")) {
							String sname = ad.getName();
							sname = sname.replace(" ", "");
							firstactivationList.add(sname + checkList(sname));
							alphabets1.add(sname);
							if (!cd.getCondition().toString().isEmpty()) {
								firstConditionList.add(getActivationCondition(cd.getCondition()
										.toString()));
								CorrelationContList.add(getcorrelationCondition(cd
										.getCondition().toString()));
							} else {
								firstConditionList.add("");
								CorrelationContList.add("");
							}
						} // / end of A
					}

					abMapdata.put(vkey, alphabet);

				}
			}
		}
		System.out.println(firstactivationList);
		
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
							if (getActivationCondition(
									cd1.getCondition()
											.toString())
									.equals(condition)) {
								if (!Fullcondition.isEmpty()) {
									Fullcondition = Fullcondition
											+ "::";
								}
								Fullcondition = Fullcondition
										+ BranchCombination.divertCondition(getActivationCondition(cd1
												.getCondition()
												.toString()));
							}
						}	
					}
					
					//start
					if (p1.getName().equals("A")
							&& (ad1.getName()
									.equals(activity))) {
						if (getActivationCondition(
								cd1.getCondition()
										.toString())
								.equals(condition)) {
							if (!Fullcondition.isEmpty()) {
								Fullcondition = Fullcondition
										+ "::";
							}
							Fullcondition = Fullcondition
									+ BranchCombination.divertCondition(getActivationCondition(cd1
											.getCondition()
											.toString()));
						}
					} // end of resp
				}
			}
		}
		
		return Fullcondition;
		
	}
	

	
	/* 
	 Add all activites in the List.
	 *  */	
	public static void Addnew(String fname, String lname, String act, String ilpcond, boolean isActivated,
			String relcond,String FullCondition,String cons, boolean isroot)
	{
		
		fname = fname.replace(" ", "");
		lname = lname.replace(" ", "");
		Alphabet alphabet = new Alphabet();
		alphabet.alphabetname = fname;
	
		int val = 0;
		if (!act.isEmpty()) {
			val = Integer.parseInt(getPayloadValue(act));
			alphabet.payLoadValue = val;
			alphabet.randomValue = val;
			alphabet.payLoadILPValue = val;
			alphabet.randomValue = val;
			alphabet.actCondition = act;
			alphabet.payLoadName = getPayload(act);
		} else {
			alphabet.actCondition = "";
			alphabet.payLoadValue = val;
			alphabet.randomValue = val;
			alphabet.payLoadILPValue = val;
			alphabet.randomValue = val;
		}
		
		alphabet.alphabetkey = fname;
		alphabet.ilpCondition = ilpcond;
		alphabet.isActivated = isActivated;
		if (relcond.isEmpty()) {
			alphabet.isRetaiveTrue = false;
			alphabet.relCondition = "";
		} else {
			alphabet.relCondition = relcond;
			alphabet.isRetaiveTrue = true;
		}
		alphabet.secondAlphabet = lname;
		alphabet.secondAlphabetKey = lname;
		alphabet.constrain = cons;
		alphabet.fullCondition = FullCondition;
		alphabet.isRoot = isroot;
		
	
		alphabetMap.put(fname, alphabet);
		alphabetMapx.put(fname, alphabet);
		
	}
	
	public static void AddFirstLetter(AssignmentModel model) {
		String fname = "";
		String FullCondition = "";
		String lname = "";
		String ilpcond = "";
		String condition = "";
		String constrain = "";
		int ind = 0;

		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model
				.getConstraintDefinitions()) {
			FullCondition = cd.getCondition().toString();
			condition = getActivationCondition(FullCondition);
			for (Parameter p : cd.getParameters()) {
				for (nl.tue.declare.domain.model.ActivityDefinition ad : cd
						.getBranches(p)) {
					constrain = cd.getName().replace("-", "").replace(" ", "")
							.toLowerCase();

					if (constrain.contains("precedence")) {

						if (p.getName().equals("B")) {
							fname = ad.getName().toString();
							fname = fname.replace(" ", "");
							lname = lname.replace(" ", "");
							addedList.add(fname);
							
							ilpcond = getCondtions(model, lname, condition);
							ind = checkIndex(fname);
							ind = ind - 1;

							if (ind == 0) {

								Addnew(fname, lname, condition, ilpcond, true,
										getcorrelationCondition(FullCondition),
										FullCondition, constrain, true);
							}
							fname = fname + (Integer.toString(ind));
							Addnew(fname, lname, condition, ilpcond, true,
									getcorrelationCondition(FullCondition), FullCondition,
									constrain, true);

							Addnew(lname, "", "", "", false, "", FullCondition,
									constrain, false);
						} else {
							lname = ad.getName().toString();
						}
					} // end of precedence

					// start of response and ...
					else {
						if (p.getName().equals("A")) {
							fname = ad.getName().toString();
							//fname = ad.getName().toString();
							fname = fname.replace(" ", "");
							lname = lname.replace(" ", "");
							addedList.add(fname);
						//	addedList.add(fname.replace(" ", ""));

							if ((constrain.equals("existence") || (constrain
									.equals("init")))
									|| (constrain.equals("last"))
									|| (constrain.equals("absence"))) {
								ilpcond = getCondtions(model, fname, condition);
								ind = checkIndex(fname);
								ind = ind - 1;
								lname = fname;
								if (ind == 0) {

									Addnew(fname, lname, condition, ilpcond,
											true, getcorrelationCondition(FullCondition),
											FullCondition, constrain, true);
								}
								fname = fname + (Integer.toString(ind));
								Addnew(fname, lname, condition, ilpcond, true,
										getcorrelationCondition(FullCondition),
										FullCondition, constrain, true);

								Addnew(lname, "", "", "", false, "",
										FullCondition, constrain, false);
							}

						} else {
							lname = ad.getName().toString();
						}
					}
					// start b
					if (p.getName().equals("B")) {
						fname = fname.replace(" ", "");
						lname = lname.replace(" ", "");
						ilpcond = getCondtions(model, fname, condition);
						ind = checkIndex(fname);
						ind = ind - 1;

						if (ind == 0) {

							Addnew(fname, lname, condition, ilpcond, true,
									getcorrelationCondition(FullCondition), FullCondition,
									constrain, true);
						}
						fname = fname + (Integer.toString(ind));
						Addnew(fname, lname, condition, ilpcond, true,
								getcorrelationCondition(FullCondition), FullCondition,
								constrain, true);

						Addnew(lname, "", "", "", false, "", FullCondition,
								constrain, false);
						System.out.println("First : " + fname + " Last : "
								+ lname);
					}
					// end of b response
				}
			}
		}

		System.out.println(alphabetMap);
	}

	public static void SetCombinationCondtion() {
		
		String key = "";
		String codition = "";
		String tempp = "";
		String send = "";
		String[] correlationList;
		String b = "";
		String combinationCond = "";
		String constain = "";
		// Alphabet alphabet = new Alphabet();

		alphabetMapx.clear();
		for (int i = 0; i < combinedList.size(); i++) {
			correlationList = combinedList.get(i).trim().split(" ");
			key = combinedList.get(i).toString().trim().replaceAll(" ", "");
			codition = "";
			System.out.println("key" + key + " : " + correlationList.length);
			if (correlationList.length <= 1) {
				codition = getRootCondition(correlationList[0]);
			} else {
				codition = getUpperLevelCondition(combinedList.get(i));
			}
			combinationCond = codition;
			Alphabet Existingalphabet = alphabetMap.get(key);
			if (Existingalphabet == null) {
				Alphabet alphabet = new Alphabet();
				alphabet.ilpCondition = "";
				if (!codition.isEmpty())
					alphabet.ilpCondition = codition;
				alphabet.conditionlist = correlationList;
				alphabet.alphabetkey = key;
				alphabet.isSingle = false;
				alphabet.isRoot = false;
				alphabet.alphabetname = BranchCombination.getParentLetter(key);
				if (correlationList.length > 1) {
					alphabet.isSingle = true;
					alphabet.secondAlphabet = getSecondAlphabet(correlationList);
				}
				alphabet.constrain = constrainB;
				String cor = getCorrelationCondition(correlationList);// = abMap.get(alphabet.alphabetname);
				String cond = "";
				for (int c = 0; c < correlationList.length; c++) {
					Alphabet alph = alphabetMap.get(correlationList[c]);
					if (c == 0) {
						alphabet.secondAlphabetKey = alph.secondAlphabetKey;
						alphabet.payLoadName = alph.payLoadName;
					}
					if (!cond.isEmpty())
						cond = cond + "::";
					if (alph != null) {
						cond = cond + alph.fullCondition;
						// alphabet.fullCondition = bd.fullCondition;
					}
				}
				alphabet.fullCondition = cond;

				alphabet.actCondition = actcond;
				if (cor != null) {
					alphabet.relCondition = cor;
					alphabet.isRetaiveTrue = true;
				} else {
					alphabet.relCondition = "";
					alphabet.isRetaiveTrue = false;
				}
				alphabetMapx.put(key, alphabet);
			} else {
				Existingalphabet.isSingle = false;
				if (correlationList.length > 1) {
					Existingalphabet.secondAlphabet = getSecondAlphabet(correlationList);
					Existingalphabet.isSingle = true;
				}

				if (!codition.isEmpty())
					Existingalphabet.ilpCondition = codition;
				Existingalphabet.conditionlist = correlationList;
				Existingalphabet.alphabetkey = key;

				alphabetMapx.put(key, Existingalphabet);
			}

		}
	}
	
	

	public static void addCorrelatedConditions() {
		String corCondtion = "";
		for (Entry<String, Alphabet> activity : alphabetMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet alphabet = activity.getValue();
			if (alphabet.isRetaiveTrue) {
				corCondtion = getCorrlationAlphabet(k);
				String[] corChoine = corCondtion.split(" ");

				if ((alphabet.constrain.equals("existence"))
						|| ((alphabet.constrain.equals("init")))
						|| ((alphabet.constrain.equals("last")))
						|| ((alphabet.constrain.equals("absence")))) {
					if (corChoine.length <= 1) {
						String[] cor = getZeroCor(k, alphabet.alphabetname,
								alphabet.secondAlphabetKey, alphabet);
						alphabet.correlationlist = cor;
						alphabet.secondAlphabet = "";
					} else {
						alphabet.correlationlist = getUpperCor(k,
								alphabet.alphabetname, alphabet.secondAlphabetKey,
								alphabet.secondAlphabet);

					}

				} else {

					if (corChoine.length <= 1) {

						String[] cor = getZeroCor(k, alphabet.alphabetname,
								alphabet.secondAlphabetKey, alphabet);
						String test = alphabet.secondAlphabetKey;
						System.out.println("xxx" + alphabet.constrain);
						if (test.isEmpty()) {
							test = "";
						}
						test = test + "::";

						String[] corx = test.split("::");

						alphabet.correlationlist = corx;
						alphabet.secondAlphabet = "";

					} else {

						alphabet.correlationlist = alphabet.secondAlphabet
								.split("::");
						String snd = alphabet.secondAlphabet;

					}
				}
				alphabetMapx.put(k, alphabet);
			}
		}

		for (Entry<String, Alphabet> activity : alphabetMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet alphabet = activity.getValue();
			System.out.println("___________________________");
			System.out.println("Key: " + k);
			System.out.println("actCondition: " + alphabet.actCondition);
			System.out.println("Corelation: " + alphabet.relCondition);
			System.out.println("fullCondition: " + alphabet.fullCondition);
			System.out.println("ILP: " + alphabet.ilpCondition);
			System.out
					.println("secondAlphabetKey: " + alphabet.secondAlphabetKey);
			System.out.println("Constrains : " + alphabet.constrain);
			System.out.println("IsSingle : " + alphabet.isSingle);

			if (alphabet.correlationlist != null) {
				for (int i = 0; i < alphabet.correlationlist.length; i++) {
					System.out
					.println("Cor : " + alphabet.correlationlist[i]);
				}
			}

		}
		System.out.println("Wait");
	}

	public static void SetSecondList(String a, String b, String[] lst) {
		String aa = BranchCombination.getParentLetter(a);
		corrlationList.put(a, null);
	}
	
	public static String[] getZeroCor(String xkey, String a, String b, Alphabet alphabet) {
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

		for (int q = 0; q < sblist.length; q++) {
			b = sblist[q];
			for (int ind = 0; ind < combinedList.size(); ind++) {
				String root[] = combinedList.get(ind).split(" ");
				String temp = combinedList.get(ind).replaceAll(" ", "");
				if (temp.length() == xkey.length()) {
					if (temp.contains(xkey)) {
						if (!ret.isEmpty()) {
							ret = ret + "::";
						}
						if (root.length == 0) {
							ret = ret
									+ combinedList.get(ind).replaceAll(aa, b)
											.replaceAll(" ", "").trim();
						} else {
							String vtemp = combinedList.get(ind)
									.replaceAll(aa, b).replaceAll(" ", "")
									.trim();
							if (!vtemp.equals(root[0].replaceAll(aa, b))) {
								ret = ret + vtemp /*
												 * + "::"+
												 * root[0].replaceAll(aa, b) +
												 * "_"+ vtemp
												 */;
							} else {
								/* {ret = ret+ vtemp;} */
								if (sblist.length > 0) {
									for (int j = 0; j < sblist.length; j++) {
										if (!ret.isEmpty()) {
											ret = ret + "::";
										}
										ret = ret
												+ vtemp.replace(aa, sblist[j]);
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
	
	public static String getSecondAlphabet(String[] correlationList){
		String b = "";
		constrainB = "";
		secondkey = "";
		for (int i2 = 0; i2 < correlationList.length; i2++) {
			Alphabet b2 = alphabetMap.get(correlationList[i2]);
			if (b2 != null) {

				if (b2.constrain.contains("precedence")) {
					secondkey = b2.secondAlphabetKey;
					if (constrainB.isEmpty())
						constrainB = b2.constrain;

				} else {
					if (constrainB.isEmpty())
						constrainB = b2.constrain;
				}
				if (!b.isEmpty()) {
					b = b + "::";
				}
				b = b + b2.secondAlphabet;
			}
		}
		return b;
	}
	
	
	public static String getCorrelationCondition(String[] correlationList){
		String b="";
		String act = "";
		secondkey = "";
		constrainB = "";
		for (int i2 = 0; i2 < correlationList.length; i2++) {
			Alphabet b2 = alphabetMap.get(correlationList[i2]);
			if (b2 != null) {
				if (!b.isEmpty()) {
					b = b + "::";
				}
				if (!act.isEmpty()) {
					act = act + "::";
				}
				b = b + b2.relCondition;
				act = act + b2.actCondition;
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
	
	public static String getActivationCondition(String fullcondition) {
		if (fullcondition.isEmpty())
			return "";
		ConstraintConditions c = ConstraintConditions.build(fullcondition);
		return c.getActivationCondition();
	}

	public static String getcorrelationCondition(String fullcondition) {
		if (fullcondition.isEmpty())
			return "";
		ConstraintConditions c = ConstraintConditions.build(fullcondition);
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
