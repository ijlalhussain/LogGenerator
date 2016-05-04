package log.generation;



import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.deckfour.xes.in.XesXmlGZIPParser;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.out.XesXmlGZIPSerializer;

public class RandomLogPicker {
	public static void main(String[] args) throws Exception {
		/*if (args.length < 3) {
			System.err.println("Usage: java " + RandomLogPicker.class.getName() + " <xes-file-in> <number-of-traces> <xes-file-out>");
		}*/

		File xesFileIn = new File("D:\\MainProj\\synthetic-log-generator\\DeclareDesigner\\abcd.xml");
		int numberOfTraces = 10;//Integer.valueOf(args[1]);
		File xesFileOut = new File("D:\\MainProj\\synthetic-log-generator\\DeclareDesigner\\ddxxdd.xml");
		
		XesXmlParser parser = new XesXmlParser();
        if (!parser.canParse(xesFileIn)) {
        	parser = new XesXmlGZIPParser();
        	if (!parser.canParse(xesFileIn)) {
        		throw new IllegalArgumentException("Unparsable log file: " + xesFileIn.getAbsolutePath());
        	}
        }
        List<XLog> xLogs = parser.parse(xesFileIn);
        XLog xLog = xLogs.remove(0);
        while (xLogs.size() > 0) {
        	xLog.addAll(xLogs.remove(0));
        }
        
        int remainingTraces = xLog.size();
        while (remainingTraces > numberOfTraces && remainingTraces > 0) {
        	Math.floor(Math.random() * remainingTraces);
        	xLog.remove((int) Math.floor(Math.random() * remainingTraces));
        	remainingTraces--;
        }
        
        OutputStream outStream = new FileOutputStream(xesFileOut);
		new XesXmlGZIPSerializer().serialize(xLog, outStream);
	}
}