package log.generation;

import minerful.concept.TaskCharArchive;
import minerful.io.encdec.TaskCharEncoderDecoder;
import minerful.logparser.StringTaskClass;

import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;
import org.processmining.plugins.declareminer.visualizing.AssignmentModel;
import org.processmining.plugins.declareminer.visualizing.AssignmentModelView;
import org.processmining.plugins.declareminer.visualizing.AssignmentViewBroker;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.Parameter;
import org.processmining.plugins.declareminer.visualizing.XMLBrokerFactory;

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
}
