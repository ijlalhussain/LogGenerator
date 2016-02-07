package log.generation;

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



public class LogGeneration {
		
	public boolean checkforCondition(String declareMapFilePath){
		
		boolean ret =false;
		
		
	
		
		AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker(declareMapFilePath);
		AssignmentModel model = broker.readAssignment();
		AssignmentModelView view = new AssignmentModelView(model);
		broker.readAssignmentGraphical(model, view);
		TaskCharEncoderDecoder encdec = new TaskCharEncoderDecoder();
	
		for(ConstraintDefinition cd : model.getConstraintDefinitions()){
			System.out.println(cd.getCondition());
			checkCondtion(cd.getCondition().toString());
			for(Parameter p : cd.getParameters()){
				for(ActivityDefinition ad : cd.getBranches(p)){
					encdec.encode(new StringTaskClass(ad.getName()));
					System.out.println(ad.getName());
					ret =true;
				}
			}
		}		
		return ret;
		
	}
	
	public void checkCondtion(String s){
		
		
			

		VariableStoreExample sx = new VariableStoreExample();
		sx.CheckLinear(s);
		Integer test = 5;
		try {
			Evaluator mEvaluator = new Evaluator();	
			String r1 = mEvaluator.evaluate("1/10000"); // Works, r1 == "1.0E-4"
			String r3 = mEvaluator.evaluate("1.0E4");   // Works, r3 == "1.0E4"
			String r2 = mEvaluator.evaluate(s); // Works, r1 == "1.0E-4"
			System.out.println(r1 +":"+ ":"+ r3 + "::condtionvalue:" + r2);
			sx.CheckExample(s);
		
		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  // Doesn't work, exception is thrown

		
	}	
}
