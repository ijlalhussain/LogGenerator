package log.generation;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Iterator;
import java.util.List;

import minerful.concept.TaskCharArchive;
import minerful.io.encdec.TaskCharEncoderDecoder;
import minerful.io.encdec.TaskCharEncoderDecoder;
import minerful.logparser.StringTaskClass;

import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;
import org.processmining.plugins.declareminer.visualizing.AssignmentModel;
import org.processmining.plugins.declareminer.visualizing.AssignmentModelView;
import org.processmining.plugins.declareminer.visualizing.AssignmentViewBroker;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.Parameter;
import org.processmining.plugins.declareminer.visualizing.XMLBrokerFactory;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;

public class DeclareLogGenerator {
	public long traceLength = 0;
	ArrayList<String> alphabets = new ArrayList<String>();
	public  void GenerateLog(int minlength, int maxlength, long LogSize, String filename){
		
		AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker(filename);
		AssignmentModel model = broker.readAssignment();
		AssignmentModelView view = new AssignmentModelView(model);
		//broker.readAssignmentGraphical(model, view);
	//	TaskCharEncoderDecoder encdec = new TaskCharEncoderDecoder();
		
		for(ConstraintDefinition cd : model.getConstraintDefinitions()){
		for(Parameter p : cd.getParameters()){
			System.out.println(p.getName());
			alphabets.add(p.getName());
		}
		}
		
		//remove duplicates 
		 LinkedHashSet<String> lhs = new LinkedHashSet<String>();
		 lhs.addAll(alphabets);
		 alphabets.clear();
		 alphabets.addAll(lhs);
			
		
		for (int i=minlength; i<  maxlength; i++)
		{
			traceLength = CalcaulateTrace(i,maxlength);
		}
				
		traceLength = CalcaulateTrace(minlength,maxlength);		
		
		for (int trace=0; trace < traceLength; trace++)
		{		
			
		}		
	}
	
	private int CalcaulateTrace(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min) + min;		
	}

}
