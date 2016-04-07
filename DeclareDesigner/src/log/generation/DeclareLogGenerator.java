package log.generation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.deckfour.xes.classification.XEventAttributeClassifier;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension.StandardModel;
import org.deckfour.xes.extension.std.XOrganizationalExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.in.XMxmlParser;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;

import minerful.concept.ProcessModel;
import minerful.concept.TaskChar;
import minerful.concept.TaskCharArchive;
import minerful.concept.constraint.ConstraintsBag;
import minerful.concept.constraint.relation.Response;
import minerful.io.encdec.TaskCharEncoderDecoder;
import minerful.io.encdec.declare.DeclareEncoderDecoder;
import minerful.io.encdec.declare.DeclareTemplate;
import minerful.logmaker.MinerFulLogMaker;
import minerful.logmaker.params.LogMakerCmdParameters;
import minerful.logmaker.params.LogMakerCmdParameters.Encoding;
import minerful.logparser.StringTaskClass;



import nl.tue.declare.appl.design.gui.ParameterSettings;
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
//import nl.tue.declare.datamanagement.XMLElementFactory;
//import nl.tue.declare.datamanagement.assignment.AssignmentElementFactory;
//import nl.tue.declare.domain.model.ActivityDataDefinition;
//import nl.tue.declare.execution.AssignmentState;
import nl.tue.declare.execution.WorkItemData;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Date;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension.StandardModel;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.out.XMxmlSerializer;
import org.deckfour.xes.out.XesXmlSerializer;

public class DeclareLogGenerator {
	public static long traceLength = 0;
	static LinkedHashMap <String, Alphabet> abMap = new LinkedHashMap <String, Alphabet>();
	ArrayList<WorkItemData> data;
	static ArrayList<String> alphabets1 = new ArrayList();
	static ArrayList<String> firstact = new ArrayList();
	static ArrayList<String> firstcont = new ArrayList();
	
	HashMap<String,String> attributes;
	private LogMakerCmdParameters parameters; 
	public static Encoding OUTPUT_ENCODING = Encoding.xes;
	public static final File OUTPUT_LOG = new File("");
	
	
	public  void GenerateLog(int minlength, int maxlength, long LogSize, String filename, String destitionfile){
		AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker(filename);
		AssignmentModel model = broker.readAssignment();
		getAlphabets(model);
		checkActivation(model);		
		
	//	addCondtion(model);
		
		
		
	//	---------------
//		for (int k=0; k < lst.size(); k++)
//		{ 
//			DataElement d = lst.get(k);
//			d.getInitial();
//			System.out.println(d.getName() + ":-> " + d.getType().getName());			
//		}
		
		
//		for (Entry<String, Alphabet> activity : abMap.entrySet()) {
//	    String k = entry.getKey();
//	    Alphabet dd2 =entry.getValue();
//		System.out.print("key,val: ");
//	    System.out.println(k + "," + dd2.actCondition + dd2.relCondition);
//	}
				
//		ConstraintConditions c = ConstraintConditions.build(conditions.get(0));
		// if (c.getActivationCondition().toString() != "")
		 {
	//		 c.getConstraintCondition();
			 XFactory xFactory = XFactoryRegistry.instance().currentDefault();
			 XLog xlog = xFactory.createLog();
			 XTrace xTrace = null;
			 XEvent xEvent = xFactory.createEvent();
			    XConceptExtension concExtino = XConceptExtension.instance();
			   	XLifecycleExtension lifeExtension = XLifecycleExtension.instance();
				XTimeExtension timeExtension = XTimeExtension.instance();
				xlog.getExtensions().add(concExtino);
				xlog.getExtensions().add(lifeExtension);
				xlog.getExtensions().add(timeExtension);
				xlog.getClassifiers().add(new XEventNameClassifier());
				
				concExtino.assignName(xEvent, "Tartu Uni Log: " + model.getName());
				Date currentDate = null;
				int padder = (int)(Math.ceil(Math.log10(LogSize)));
				String traceNameTemplate = "Multi-proespective trace no. " + (padder < 1 ? "" : "%0" + padder) + "d";
	    
			    for (int trace=0; trace < LogSize; trace++){
			    	String firedTransition = null;
			    	int xselect = selectRandom(abMap.size());
			    	System.out.println(xselect + ":-> " + trace);
			    	xTrace = xFactory.createTrace();
					concExtino.assignName(
							xTrace,
							String.format(traceNameTemplate, (trace))
						);
					
					 if ((xselect >= 0)&&(xselect <= abMap.size()))
			    	{
						 Alphabet ExeActivity = (new ArrayList<Alphabet>(abMap.values())).get(xselect);
						 firedTransition = ExeActivity.alphabetname;//  alphabets.get(xselect) ;
			    	    currentDate = generateRandomDateTimeForLogEvent(currentDate);
			    	    int xval = ExeActivity.randomValue;
			    	    if(xval == 0)
			    	    {
			    	    	xval = ExeActivity.payLoadValue;
			    	    }
			    	    
			    	    XAttribute test = xFactory.createAttributeLiteral("X", Integer.toString(xval) , null);
			    	    XAttributeMap test2 = xFactory.createAttributeMap();
			    	    test2.put("X",test);			    	    
						xEvent = makeXEvent(xFactory, concExtino, lifeExtension, timeExtension, firedTransition, currentDate, test2);
											
						XTimeExtension.instance().assignTimestamp(xEvent, currentDate);	
						XConceptExtension.instance().assignName(xEvent, firedTransition);
						//XOrganizationalExtension.instance().assignResource(xEvent, condmap.get(firedTransition));
						xTrace.add(xEvent);
			    	}
			       xlog.add(xTrace);			    			
			    } // end of for loop
			    
			    lifeExtension.assignModel(xlog, "Tartu Testing Model");
			    printLog(xlog);
				LogMakerCmdParameters logMakParameters = new LogMakerCmdParameters(minlength, maxlength, LogSize);
				OUTPUT_ENCODING = Encoding.xes;	
				logMakParameters.outputEncoding = OUTPUT_ENCODING;
				
				File OUTPUT_LOG = new File(destitionfile);
				logMakParameters.outputLogFile = OUTPUT_LOG;
				try {
					storeLog(xlog,logMakParameters);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    System.out.println(lifeExtension.getName());	
			    
		 }
				
	}
	
	
	public static void printLog(XLog xlog)
	{
		try {	
			
			for(XTrace xtrace:xlog){
				String traceName = XConceptExtension.instance().extractName(xtrace);
				System.out.println("TraceName: "+traceName);
				XAttributeMap caseAttributes = xtrace.getAttributes();
				for(XEvent event : xtrace){
					String activityName = XConceptExtension.instance().extractName(event);
					System.out.println("ActivityName: "+activityName);
					Date timestamp = XTimeExtension.instance().extractTimestamp(event);
					System.out.println("Timestamp: "+timestamp);
					String eventType = XLifecycleExtension.instance().extractTransition(event);
					System.out.println("EventType: "+eventType);
					XAttributeMap eventAttributes = event.getAttributes();
					
					for(String key :eventAttributes.keySet()){
						String value = eventAttributes.get(key).toString(); 
						System.out.println("key: "+key+"  value: "+value);
					}
					for(String key :caseAttributes.keySet()){
						String value = caseAttributes.get(key).toString();
						System.out.println("key: "+key+"  value: "+value);
					}					
				}			
			}		
			System.out.println(xlog);
			System.out.println(XConceptExtension.instance().extractName(xlog.get(0)));
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	public static File storeLog(XLog log, LogMakerCmdParameters parameters) throws IOException {
		checkParametersForLogEncoding(log, parameters);
		if (parameters.outputLogFile == null)
			throw new IllegalStateException("Output file not specified in given parameters");
		
		File outFile = parameters.outputLogFile;
		OutputStream outStream = new FileOutputStream(outFile);
	//	this.printEncodedLogInStream(outStream);
		new XesXmlSerializer().serialize(log, outStream);
		outStream.flush();
		outStream.close();
		return outFile;
	}
	
	private static void checkParametersForLogEncoding(XLog log, LogMakerCmdParameters parameters) {
		if (log == null)
			throw new IllegalStateException("Log not yet generated");
		if (parameters.outputEncoding == null)
			throw new IllegalStateException("Output encoding not specified in given parameters");
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
		String s =values[1]; 
		return s;
	}

	public static String getPayloadValue(String c) {
		String[] values = null;
		values = c.split(" ");
		String s = values[2];
		return values[2].substring(0, values[2].length() - 1);
	}

	private static Date generateRandomDateTimeForLogEvent(Date laterThan) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		if (laterThan == null) {
			cal.add(GregorianCalendar.YEAR, -1);
			cal.add(GregorianCalendar.MONTH,
					(int) (Math.round(Math.random() * 12)) * -1);
			cal.add(GregorianCalendar.WEEK_OF_MONTH,
					(int) (Math.round(Math.random() * 4)) * -1);
			cal.add(GregorianCalendar.DAY_OF_WEEK,
					(int) (Math.round(Math.random() * 7)) * -1);
			laterThan = cal.getTime();
		}

		long randomAdditionalTime = (long) (Math.round(Math.random()
				* TimeUnit.DAYS.toMillis(1)));
		cal.setTimeInMillis(laterThan.getTime() + randomAdditionalTime);
		return cal.getTime();
	}

	private static XEvent makeXEvent(XFactory xFactory,
			XConceptExtension concExtino, XLifecycleExtension lifeExtension,
			XTimeExtension timeExtension, String firedTransition,
			Date currentDate, XAttributeMap xmap) {
		XEvent xEvent = xFactory.createEvent();
		xEvent.setAttributes(xmap);
		concExtino.assignName(xEvent, firedTransition.toString());		
		lifeExtension.assignStandardTransition(xEvent,
				XLifecycleExtension.StandardModel.COMPLETE);
		timeExtension.assignTimestamp(xEvent, currentDate);
		
		return xEvent;
	}

	public static void getAlphabets(AssignmentModel model) {
		Iterable<nl.tue.declare.domain.model.ActivityDefinition> dd = model
				.getActivityDefinitions();
		for (nl.tue.declare.domain.model.ActivityDefinition d : dd) {
			System.out.println(d.getName());
		//	alphabets.add(d.getName());
		}
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

	public static void checkActivation(AssignmentModel model) {
		String condef;
//		alphabets.clear();
		alphabets1.clear();
//		conditions.clear();
		firstact.clear();
		firstcont.clear();
		condef = model.getConstraintDefinitions().toString();
		System.out.println(condef);
//		System.out.println("_________________________________");
//		System.out.println(model.getActivityDefinitions().iterator());
//		
//		
//		condef = "";
//		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model.getConstraintDefinitions()) {
//			ConstraintConditions c = ConstraintConditions.build(cd.getCondition().toString());
//			System.out.println("a = " + c.getActivationCondition());
//			if (condef == "")
//				condef =  c.getActivationCondition() +",";
//				else
//					condef = condef + "," + c.getActivationCondition(); 
//			
//		}
//		System.out.println(condef);
//		
//		activities = model.getActivityDefinitions();
//		activities.spliterator()
//		 Iterator<nl.tue.declare.domain.model.ActivityDefinition> ia = activities.iterator();// .iterator();
//	      while (ia.hasNext()) {
//	    	  System.out.println(ia.toString());
//	    	  ia.next();
//	      }
		
		
		
		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model.getConstraintDefinitions()) {
			for (Parameter p : cd.getParameters()) {
				if (p.getName().equals("A")){
					firstact.add(cd.getFirstBranch(p).toString());
					firstcont.add(getLeft(cd.getCondition().toString()));	
					
			}			
		}}
		
//	
//		condmap.clear();
		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model.getConstraintDefinitions()) {
			String constrain = cd.getName().replace("-", "").replace(" ", "").toLowerCase();
			int chk = 0;			
	//	conditions.add(cd.getCondition().toString());
			for (Parameter p : cd.getParameters()) {
				for (nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)) {
					if (p.getName().equals("A")){
						addFirst(model, ad.getName());			
					} else {					
						Alphabet ab = new Alphabet();
						ab.alphabetname = ad.getName();
						ab.actCondition = "";
						ab.isActivationTrue = false;
						int val = Integer.parseInt(getPayloadValue(getLeft(cd.getCondition().toString())));
						ab.payLoadValue = val;
						ab.randomValue = val;
						ab.payLoadILPValue = val;
						abMap.put(ad.getName(), ab);
		//				alphabets.add(ad.getName());
		//				condmap.put(alphabets.get(alphabets.size()-1), "");
					}
					chk += 1;
				}
			}
		}
	//	System.out.println(alphabets);
	}
	
	public static void AddFirstLetter(AssignmentModel model, String activity){
	
		
		
		
		
	}
	
	public static void addFirst(AssignmentModel model, String s){
		String condition = "";
		String condition1 = "";
		int indx = newactivity(s);
		mainloop:
		for (nl.tue.declare.domain.model.ConstraintDefinition cd : model.getConstraintDefinitions()) {
			for (Parameter p : cd.getParameters()) {
						
				for (nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)) {
					if (p.getName().equals("A") && (ad.getName().toString() == s)){
						Alphabet ab = new Alphabet();
						String key = "";
						ab.alphabetname = ad.getName().toString();
						ab.fullCondition = cd.getCondition().toString();
						condition = getLeft(cd.getCondition().toString());
						if (condition != "")
						{
							ab.actCondition(condition);
							ab.isActivationTrue =true;
						} else {
							ab.isActivationTrue = false;
							ab.actCondition = "";
						}
					int val = 0;	
					if (indx == 0)
					{
						condition = filterArray(0,s);
						condition1 = filterArray(1,s);
						int a0 = Integer.parseInt(getPayloadValue(condition));//x > 2 
						int a1 = Integer.parseInt(getPayloadValue(condition1));// x < 6
						int ilp = IlpSolver.getILPValue(condition,condition1);
						ab.payLoadValue = a0;
						ab.leftvVlue = a0;
						ab.rightValue = a1;
						ab.logGenerate = true;
						key = ad.getName();
						ab.payLoadILPValue = ilp;
						ab.randomValue = ilp;
						if ((ilp > 0) && (ilp < 4))// A.x > 0   &&   !  (A.x < 4 )
						{
							key = key+"0";
						} else
						{
							key = key+"01";
						}
						abMap.put(key, ab);
//						if (ilp == a0){
//							
//							ilp = getRandomTrace(0,100);
//							ab.randomValue = ilp;
//							 if ((a0 > ilp) && (!(a1 < ilp))) {
//								 key = ad.getName()+0;
//		//						 condmap.put(key, ilp+"");
//			//					 alphabets.add(key+0);
//							 } else  if ((a0 > ilp) && (a1 < ilp)) {
//								 key = ad.getName()+01;
//				//				 condmap.put(key, ilp+"");
//					//			 alphabets.add(key);
//							 } else  if (!(a0 > ilp) && (a1 < ilp)) {
//								 key = ad.getName()+1;
//						//		 condmap.put(key, ilp+"");
//							//	 alphabets.add(key);
//							 } else {
//								// condmap.put(key, ilp+"");
//								// alphabets.add(key); 
//							 }
//							
//							 abMap.put(key, ab);
//					} else 
//					{ 
//						 ab.payLoadILPValue = ilp;
//						 ab.randomValue = ilp;
//						// condmap.put(key, ilp+"");
//						// alphabets.add(key);
//						 abMap.put(key, ab);
//					}
						 break mainloop;
					}
					else if (indx == 1)
					{
						key= ad.getName();
						condition = filterArray(0,s);
						condition1 = filterArray(1,s);
						int a0 = Integer.parseInt(getPayloadValue(condition));
						int a1 = Integer.parseInt(getPayloadValue(condition1));
						int ilp = IlpSolver.getILPValue(condition, condition1);
						ab.payLoadValue = a1;
						ab.leftvVlue = a1;
						ab.fullCondition = condition + "&&" + condition1;
						ab.rightValue = a0;
						ab.logGenerate = true;
						ab.payLoadILPValue = ilp;
						ab.alphabetname = key;
						ab.randomValue = ilp;
						if ((ilp < 0) && (ilp < 4))//! ( A.x > 0)   &&  A.x < 4
						{
							key = key+"1";
						} else
						{
							key = key+"011";
						}
						abMap.put(key, ab);
											
//						
//						if (ilp == a0){
//							ilp = getRandomTrace(0,100);
//							ab.randomValue = ilp;
//							 if ((!(a0 > ilp)) && (a1 < ilp)) {
//								 key = key + 1;
//							//	 condmap.put(key, ilp+"");
//								// alphabets.add(key);
//							 } else  if ((a0 > ilp) && (a1 < ilp)) {
//								 key= key+"01";
//								 //condmap.put(key, ilp+"");
//								 //alphabets.add(key);
//							 } else  if (!(a0 > ilp) && (a1 < ilp)) {
//								 key = key + "0";
//								 //condmap.put(ad.getName()+"1", ilp+"");
//								 //alphabets.add(ad.getName()+"1");
//							 } else {
//							//	 condmap.put(ad.getName(), ilp+"");
//								// alphabets.add(ad.getName()); 
//							 }
//							 abMap.put(key, ab);
//					} else 
//					{ 
//						abMap.put(ad.getName(), ab); 
//					//	condmap.put(ad.getName(), ilp+"");
//						// alphabets.add(ad.getName()); 
//					}
						 break mainloop;
					} else
					{
						break mainloop;
					}
					
					
					}}}}
				
	}
	
	public static String filterArray(int start, String s){
		String val = "";
		String val2 = "";
		
		ArrayList<String> lst = new ArrayList<String> ();
		for (int i=0; i< firstact.size(); i++ )
		{			
			val = firstact.get(i);
			val2 = firstcont.get(i);
			if (val == s)
			{
				lst.add(val2);
			}
		}		
		if (lst.isEmpty())
		{
			return "";
		} else {
		return lst.get(start);
	}
}
	
	public static int newactivity(String s)
	{
		int ret =0;		
		if (alphabets1.isEmpty())
		{
			alphabets1.add(s);
			return 0;
		} 
		else {
			alphabets1.add(s);
			return 1;	
		}
			
		
	}


	
	public static String getLeft(String s)
	{
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
	
	public static int getXvalue(String s){
		int ret =0;
		String regex = "\\d+";
		if(s.matches(regex)) {		    
		    ret = Integer.parseInt(s);
		} else
		{
			String[] sp = s.split("\\.");
			if(sp[1].matches(regex)) 	
				{
					ret = Integer.parseInt(sp[1]);
					}
				else
				{
				ret = getRandomTrace(0,40000);
			}
		}
		return ret;
	}

}
