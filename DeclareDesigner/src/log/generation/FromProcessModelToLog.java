package log.generation;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import minerful.concept.ProcessModel;
import minerful.concept.TaskChar;
import minerful.concept.TaskCharArchive;
import minerful.concept.TaskCharSet;
import minerful.concept.constraint.Constraint;
import minerful.concept.constraint.ConstraintsBag;
import minerful.concept.constraint.existence.*;
import minerful.concept.constraint.relation.*;
import minerful.io.encdec.TaskCharEncoderDecoder;
//import minerful.io.encdec.declaremap.DeclareMapEncoderDecoder;
import minerful.logmaker.MinerFulLogMaker;
import minerful.logmaker.params.LogMakerCmdParameters;
import minerful.logmaker.params.LogMakerCmdParameters.Encoding;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.out.XesXmlSerializer;

public class FromProcessModelToLog {
	
	//public static Set<TaskChar> alphabet = new TreeSet<TaskChar>(Arrays.asList(tchs));


	public static Integer minEventsPerTrace = 5;
	public static Integer maxEventsPerTrace = 5;
	public static Long tracesInLog = (long)10;
	public static File outputLog = new File("oooout.xes");

	
	public static void main(String[] args) throws IOException {
		
		HashMap<String,TaskChar> test = new HashMap<String,TaskChar>();
		List<TaskChar> where = new ArrayList<TaskChar>();
		List<Constraint> lst = new ArrayList<Constraint>();
		
		List<String> alp = new  ArrayList<String>();
		alp.add("AO");
		alp.add("AOA1");
		alp.add("AOA2");
		alp.add("AOA3");
		alp.add("BOB1");
		alp.add("BOB2");
		alp.add("BOB3");
		

		int theEnd = 65+ alp.size();
		for (int vchar = 65; vchar < theEnd;vchar++ ){
			char c = (char)vchar;
			System.out.println("Char: "+ c);
			TaskChar cc = new TaskChar(c);
			where.add(cc);			
		}
		
		for (int i=0; i < alp.size(); i++){
			test.put(alp.get(i), where.get(i));
			System.out.println("Mapr:  Key: "+ alp.get(i) + " Char: "+ where.get(i));
		}
		
		TaskChar z= test.get("AOA1");
	System.out.println("SS"+	test.get("AOA1"));
		/*for (int j=0; j <3; j++)
		{
			*/
		
		   System.out.println("1st: "+where.get(0));
			System.out.println("2st: "+where.get(1));
			System.out.println("3st: "+where.get(2));
			Precedence rss= 	new Precedence(new TaskCharSet(where.get(0), where.get(1)), new TaskCharSet(where.get(2)));
			//Response tss= 	new Response(new TaskCharSet(where.get(3);
		   // Response rs = new Response(new TaskCharSet(where),new TaskCharSet(where));
		lst.add(rss);
	//	}
			
	
	//	where.add(arg0)
		
	/*	TaskChar a = new TaskChar('a');
		test.put("AO", a);
		TaskChar b = new TaskChar('b');
		test.put("BOB1", b);
		char c = 'c';
		TaskChar cs = new TaskChar(c);
		test.put("BOB1", b)*/;
	/*	c= new TaskChar('c');
		test.put("BOA2", c);
		d= new TaskChar('d');
		test.put("A1", d);*/
		
	/*	where.add(a);
		where.add(b);*/
		
		/*for(int i=0; i < where.size(); i++){
			System.out.print(where[i]);
		}*/
		
	//	System.out.println(where);
		
		/*TaskChar[] tchs =
				new TaskChar[]{
					a, b, c, d
				};	*/
		
	   TaskChar[] testchar;
	   Set<TaskChar> alphabetS = new TreeSet<TaskChar>(where);
	  	int i=0;
		
		
		for (Map.Entry<String, TaskChar> entry : test.entrySet()) {
		    String key = entry.getKey();
		   // tchs[i] = entry.getValue();
		    System.out.println("Key:" + key);
		    System.out.println(entry.getValue());
		    // ...
		}
		
		
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
		
		LogMakerCmdParameters logMakParameters =
				new LogMakerCmdParameters(
						minEventsPerTrace, maxEventsPerTrace, tracesInLog);
		
		MinerFulLogMaker logMak = new MinerFulLogMaker(logMakParameters);

		XLog log = logMak.createLog(proMod);

		logMakParameters.outputEncoding = Encoding.xes;
		logMakParameters.outputLogFile = outputLog;
		logMak.storeLog();
	}
}
