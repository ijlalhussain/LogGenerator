package log.generation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import minerful.logmaker.params.LogMakerCmdParameters;
import nl.tue.declare.domain.model.AssignmentModel;

import org.deckfour.xes.classification.XEventNameClassifier;
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
import org.deckfour.xes.out.XesXmlSerializer;

public class LogService {
	
	public static void PrintMyLog(AssignmentModel model,
			LinkedHashMap<String, Alphabet> abMapx, long LogSize,
			ArrayList<String> combinedList, int minlength, int maxlength ) {
		
		for (int trace = 0; trace < LogSize; trace++) {
			String firedTransition = null;
			int localMax = 0;
			int xselect = selectRandom(abMapx.size());
			if ((xselect >= 0) && (xselect <= abMapx.size())) {
				Alphabet ExeActivity = abMapx.get(combinedList.get(xselect)
						.trim().replaceAll(" ", ""));
				firedTransition = ExeActivity.alphabetkey;// alphabets.get(xselect)
				System.out.println("1st Alphabet : " + firedTransition); // ;
				if (ExeActivity.isRetaiveTrue) {
					String[] corrLiost = ExeActivity.correlationlist;
					int randx = selectRandom(corrLiost.length);
					firedTransition = corrLiost[randx];// ExeActivity.secondAlphabet;
					System.out.println("2st Alphabet with : " + firedTransition);
				}

				if ((ExeActivity.isRetaiveTrue==false)&&(!ExeActivity.secondAlphabet.isEmpty())) {
					// if (1==1){
					String[] b = ExeActivity.secondAlphabet.split("::");
					for (int ndx = 0; ndx < b.length; ndx++) {
						if (!b[ndx].equals("!@#@!")) {
							firedTransition = b[ndx];// ExeActivity.secondAlphabet;
							System.out.println("2st Alphabet : "
									+ firedTransition);

						}
					}

				} // if not !=null and isretiefalse

				if (localMax < maxlength) {

					for (int k = localMax; k < maxlength; k++) {
						int xxd = selectRandom(abMapx.size());
						Alphabet TestChar = abMapx.get(combinedList.get(xxd)
								.trim().replaceAll(" ", ""));
						firedTransition = TestChar.alphabetkey; // ExeActivity.secondAlphabet;
			//			System.out.println("Looping Max Events : "
			//					+ firedTransition);
					}
				}
			}
		}
	}
	
	
	public static XLog GenerateLog(AssignmentModel model,
			LinkedHashMap<String, Alphabet> abMapx, long LogSize,
			ArrayList<String> combinedList, int minlength, int maxlength ) {
		// c.getConstraintCondition();
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
		int padder = (int) (Math.ceil(Math.log10(LogSize)));
		String traceNameTemplate = "Multi-proespective trace no. "
				+ (padder < 1 ? "" : "%0" + padder) + "d";

		for (int trace = 0; trace < LogSize; trace++) {
			String firedTransition = null;
			xTrace = xFactory.createTrace();
			concExtino.assignName(xTrace,String.format(traceNameTemplate, (trace)));
			int localMax = 0;
			int xselect = selectRandom(abMapx.size());
			if ((xselect >= 0) && (xselect <= abMapx.size())) {
				Alphabet ExeActivity = abMapx.get(combinedList.get(xselect)
						.trim().replaceAll(" ", ""));
				firedTransition =  ExeActivity.alphabetkey ;// alphabets.get(xselect)
														// ;
				currentDate = LogService
						.generateRandomDateTimeForLogEvent(currentDate);
				int xval = ExeActivity.randomValue;
				if (xval == 0) {
					xval = ExeActivity.payLoadValue;
				}
				
				xval = getRandomTrace(ExeActivity.minValue, ExeActivity.maxValue);
				//xval = getRandomTrace(1,100000);//xselect;
				XAttribute test = xFactory.createAttributeLiteral("X",
						Integer.toString(xval), null);
				XAttributeMap test2 = xFactory.createAttributeMap();
				test2.put("X", test);
				xEvent = LogService.makeXEvent(xFactory, concExtino,
						lifeExtension, timeExtension, firedTransition,
						currentDate, test2);

				XTimeExtension.instance().assignTimestamp(xEvent, currentDate);
				XConceptExtension.instance()
						.assignName(xEvent, firedTransition);
				xTrace.add(xEvent);
				if (ExeActivity.isRetaiveTrue){
				 String[] corrLiost = ExeActivity.correlationlist;
				 int randx= selectRandom(corrLiost.length);
					firedTransition = corrLiost[randx];// ExeActivity.secondAlphabet;
					XEvent xEventb = xFactory.createEvent();
					XAttribute testb = xFactory
							.createAttributeLiteral("X", Integer
									.toString(getRandomTrace(1000, 40000)),
									null);
					XAttributeMap test2b = xFactory.createAttributeMap();
					test2b.put("X", testb);
					xEventb = LogService.makeXEvent(xFactory, concExtino,
							lifeExtension, timeExtension, firedTransition,
							currentDate, test2b);
					XTimeExtension.instance().assignTimestamp(xEventb,
							currentDate);
					XConceptExtension.instance().assignName(xEventb,
							firedTransition);
					localMax++;
					xTrace.add(xEventb);
				} 
				
				
				if (!ExeActivity.secondAlphabet.isEmpty()) {
					// if (1==1){
					String[] b = ExeActivity.secondAlphabet.split("::");
					for (int ndx=0; ndx < b.length; ndx++)
					{
					if (!b[ndx].equals("!@#@!"))
					{
					firedTransition = b[ndx];// ExeActivity.secondAlphabet;
					XEvent xEventb = xFactory.createEvent();
					XAttribute testb = xFactory.createAttributeLiteral("X", Integer.toString(getRandomTrace(1000, 40000)),
									null);
					XAttributeMap test2b = xFactory.createAttributeMap();
					test2b.put("X", testb);
					xEventb = LogService.makeXEvent(xFactory, concExtino,
							lifeExtension, timeExtension, firedTransition,
							currentDate, test2b);
					XTimeExtension.instance().assignTimestamp(xEventb,
							currentDate);
					XConceptExtension.instance().assignName(xEventb,
							firedTransition);
					localMax++;
					xTrace.add(xEventb);
					}
				}
					
				} // if not !=null and isretiefalse
				
				if (localMax < maxlength ){
					
					for (int k=localMax; k < maxlength; k++){
						int xxd = selectRandom(abMapx.size());
						Alphabet TestChar = abMapx.get(combinedList.get(xxd)
								.trim().replaceAll(" ", ""));
						firedTransition = TestChar.alphabetkey; // ExeActivity.secondAlphabet;
						XEvent xEventb = xFactory.createEvent();
						XAttribute testb = xFactory.createAttributeLiteral("X", Integer.toString(getRandomTrace(TestChar.minValue, TestChar.maxValue)),
										null);
						XAttributeMap test2b = xFactory.createAttributeMap();
						test2b.put("X", testb);
						xEventb = LogService.makeXEvent(xFactory, concExtino,
								lifeExtension, timeExtension, firedTransition,
								currentDate, test2b);
						XTimeExtension.instance().assignTimestamp(xEventb,
								currentDate);
						XConceptExtension.instance().assignName(xEventb,
								firedTransition);
						localMax++;
						xTrace.add(xEventb);
						
					}
				}

			}
			xlog.add(xTrace);

		}
		return xlog;
	}

	public static int  getRandomTrace(int min, int max) {
		try {
			Random random = new Random();
			//return random.nextInt(max - min) + min;
			return  (int) (min + (random.nextDouble() * (max - min)));
		} catch (IllegalArgumentException e) {
			return max;
		}

	}
	
	public static int selectRandom(int max) {
		Random rand = new Random();
		return rand.nextInt(max);
	}
	
		
	public static Date generateRandomDateTimeForLogEvent(Date laterThan) {
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
	
	
	public static void printLog(XLog xlog) {
		try {

			System.out.println("-----Printing log start -----");
			int eventnumber =0;
			for (XTrace xtrace : xlog) {
				String traceName = XConceptExtension.instance().extractName(
						xtrace);
				System.out.println("TraceName: " + traceName.substring(traceName.lastIndexOf(".",traceName.length())).trim());
				XAttributeMap caseAttributes = xtrace.getAttributes();
				for (XEvent event : xtrace) {
					String activityName = XConceptExtension.instance()
							.extractName(event);
				
					//System.out.println("Event ID : " + event.getID());
					System.out.println("Char : "+ activityName+ " Key: " +	DeclareLogGenerator.getAlphabetKey(activityName)
							+ " Event :" + eventnumber);
					eventnumber++;
				//	Date timestamp = XTimeExtension.instance()
						//	.extractTimestamp(event);
				/*	System.out.println("Timestamp: " + timestamp);
					String eventType = XLifecycleExtension.instance()
							.extractTransition(event);
					System.out.println("EventType: " + eventType);
					XAttributeMap eventAttributes = event.getAttributes();

					for (String key : eventAttributes.keySet()) {
						String value = eventAttributes.get(key).toString();
						String ww = eventAttributes.get(key).getKey();
						System.out.println(" eventAttributes key: " + key + "  value: " + value + " ww: " + ww);
					}
					for (String key : caseAttributes.keySet()) {
						String value = caseAttributes.get(key).toString();
						System.out.println(" caseAttributes key: " + key + "  value: " + value);
					}*/
				}
			}
		//	System.out.println(xlog);
			System.out.println(XConceptExtension.instance().extractName(
					xlog.get(0)));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("-----Printing log end -----");
	}

	public static File storeLog(XLog log, LogMakerCmdParameters parameters)
			throws IOException {
		checkParametersForLogEncoding(log, parameters);
		if (parameters.outputLogFile == null)
			throw new IllegalStateException(
					"Output file not specified in given parameters");

		File outFile = parameters.outputLogFile;
		OutputStream outStream = new FileOutputStream(outFile);
		// this.printEncodedLogInStream(outStream);
		new XesXmlSerializer().serialize(log, outStream);
		outStream.flush();
		outStream.close();
		return outFile;
	}

	private static void checkParametersForLogEncoding(XLog log,
			LogMakerCmdParameters parameters) {
		if (log == null)
			throw new IllegalStateException("Log not yet generated");
		if (parameters.outputEncoding == null)
			throw new IllegalStateException(
					"Output encoding not specified in given parameters");
	}



	public static XEvent makeXEvent(XFactory xFactory,
			XConceptExtension concExtino, XLifecycleExtension lifeExtension,
			XTimeExtension timeExtension, String firedTransition,
			Date currentDate, XAttributeMap xmap) {
	XEvent xEvent = xFactory.createEvent();
	xEvent.setAttributes(xmap);
	concExtino.assignName(xEvent, firedTransition.toString());
	timeExtension.assignTimestamp(xEvent, currentDate);
	lifeExtension.assignStandardTransition(xEvent,XLifecycleExtension.StandardModel.COMPLETE);
	return xEvent;
	}

}
