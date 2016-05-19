package log.generation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import minerful.concept.ProcessModel;
import minerful.concept.TaskCharArchive;
import minerful.concept.constraint.Constraint;
import minerful.concept.constraint.ConstraintsBag;
import minerful.concept.constraint.MetaConstraintUtils;
import minerful.concept.constraint.existence.AtMostOne;
import minerful.concept.constraint.existence.Init;
import minerful.concept.constraint.existence.Participation;
import minerful.concept.constraint.relation.AlternatePrecedence;
import minerful.concept.constraint.relation.AlternateResponse;
import minerful.concept.constraint.relation.AlternateSuccession;
import minerful.concept.constraint.relation.ChainPrecedence;
import minerful.concept.constraint.relation.ChainResponse;
import minerful.concept.constraint.relation.ChainSuccession;
import minerful.concept.constraint.relation.CoExistence;
import minerful.concept.constraint.relation.NotChainSuccession;
import minerful.concept.constraint.relation.NotCoExistence;
import minerful.concept.constraint.relation.NotSuccession;
import minerful.concept.constraint.relation.Precedence;
import minerful.concept.constraint.relation.RespondedExistence;
import minerful.concept.constraint.relation.Response;
import minerful.concept.constraint.relation.Succession;
import minerful.io.encdec.TaskCharEncoderDecoder;
import minerful.io.encdec.declare.DeclareEncoderDecoder;
import minerful.logparser.StringTaskClass;

import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;
import org.processmining.plugins.declareminer.visualizing.AssignmentModel;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.Parameter;

public class MinerfulLogGenerator {
	
	
	
	public static ProcessModel fromDeclareMapToMinerfulProcessModel(nl.tue.declare.domain.model.AssignmentModel model, 
			TaskCharArchive taskCharArchive,ArrayList<String> combinlist,
			LinkedHashMap<String, Alphabet> abMapx) {
		ArrayList<String> params = new ArrayList<String>();
		ArrayList<Constraint> minerFulConstraints = new ArrayList<Constraint>();

		if (taskCharArchive == null) {
			TaskCharEncoderDecoder encdec = new TaskCharEncoderDecoder();

	/*	for(nl.tue.declare.domain.model.ConstraintDefinition cd : model.getConstraintDefinitions()){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						System.out.println("Upper: "+ad.getName());
						encdec.encode(new StringTaskClass(ad.getName()));
					}
				}
			}*/
			
			for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
				String k = activity.getKey();
				Alphabet filter = activity.getValue();
				if (!filter.alphabetkey.isEmpty())
				{
					String [] jj = filter.secondAlphabet.split("::");
					for (int i=0; i <jj.length; i++ ){
						encdec.encode(new StringTaskClass( filter.alphabetkey));						
						encdec.encode(new StringTaskClass( jj[i]));
						System.out.println("Middle: "+filter.alphabetkey + "Second: " + jj[i] );	
					}
				}
				
			}
			
			for(nl.tue.declare.domain.model.ActivityDefinition ad : model.getActivityDefinitions()){
				encdec.encode(new StringTaskClass(ad.getName()));
				System.out.println("Lower: "+ad.getName());
			}
			
		
			
			/*for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
				String k = activity.getKey();
				Alphabet filter = activity.getValue();
				if (!filter.alphabetkey.isEmpty())
				encdec.encode(new StringTaskClass( filter.alphabetkey));
			}*/
			
			taskCharArchive = new TaskCharArchive(encdec.getTranslationMap());
		}

		for(nl.tue.declare.domain.model.ConstraintDefinition cd : model.getConstraintDefinitions()){
			String template = cd.getName().replace("-", "").replace(" ", "").toLowerCase();
			params = new ArrayList<String>();

			Pattern
				supPattern = Pattern.compile(DeclareEncoderDecoder.SUPPORT_EXTRACTION_REG_EXP),
				confiPattern = Pattern.compile(DeclareEncoderDecoder.CONFIDENCE_EXTRACTION_REG_EXP),
				inteFaPattern = Pattern.compile(DeclareEncoderDecoder.IF_EXTRACTION_REG_EXP);
			Matcher
				supMatcher = supPattern.matcher(cd.getText().trim()),
				confiMatcher = confiPattern.matcher(cd.getText().trim()),
				inteFaMatcher = inteFaPattern.matcher(cd.getText().trim());

			Double
				support = (supMatcher.matches() && supMatcher.groupCount() > 0 ? Double.valueOf(supMatcher.group(1)) : Constraint.DEFAULT_SUPPORT),
				confidence = (confiMatcher.matches() && confiMatcher.groupCount() > 0 ? Double.valueOf(confiMatcher.group(1)) : Constraint.DEFAULT_CONFIDENCE),
				interestFact = (inteFaMatcher.matches() && inteFaMatcher.groupCount() > 0 ? Double.valueOf(inteFaMatcher.group(1)): Constraint.DEFAULT_INTEREST_FACTOR);

//			Double support = new Double (cd.getText().split("|")[0].split(";")[1]);
//			Double confidence = new Double (cd.getText().split("|")[1].split(";")[1]);
//			Double interestFact = new Double (cd.getText().split("|")[2].split(";")[1]);
			if(template.equals("alternateprecedence")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				AlternatePrecedence minerConstr = new AlternatePrecedence(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("alternateresponse")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				AlternateResponse minerConstr = new AlternateResponse(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);

				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("alternatesuccession")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				AlternateSuccession minerConstr = new AlternateSuccession(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("chainprecedence")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				ChainPrecedence minerConstr = new ChainPrecedence(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("chainresponse")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				ChainResponse minerConstr = new ChainResponse(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("chainsuccession")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				ChainSuccession minerConstr = new ChainSuccession(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("coexistence")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				CoExistence minerConstr = new CoExistence(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("notchainsuccession")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				NotChainSuccession minerConstr = new NotChainSuccession(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("notcoexistence")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				NotCoExistence minerConstr = new NotCoExistence(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("notsuccession")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				NotSuccession minerConstr = new NotSuccession(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("precedence")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				Precedence minerConstr = new Precedence(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("response")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				Response minerConstr = new Response(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("succession")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				Succession minerConstr = new Succession(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("respondedexistence")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				RespondedExistence minerConstr = new RespondedExistence(taskCharArchive.getTaskChar(params.get(0)),taskCharArchive.getTaskChar(params.get(1)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("init")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				Init minerConstr = new Init(taskCharArchive.getTaskChar(params.get(0)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("existence")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				Participation minerConstr = new Participation(taskCharArchive.getTaskChar(params.get(0)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}else if(template.equals("absence2")){
				for(nl.tue.declare.domain.template.Parameter p : cd.getParameters()){
					for(nl.tue.declare.domain.model.ActivityDefinition ad : cd.getBranches(p)){
						params.add(ad.getName());
					}
				}
				AtMostOne minerConstr = new AtMostOne(taskCharArchive.getTaskChar(params.get(0)),support);
				minerConstr.confidence = confidence;
				minerConstr.interestFactor = interestFact;
				minerFulConstraints.add(minerConstr);
			}
		}
		MetaConstraintUtils.createHierarchicalLinks(new TreeSet<Constraint>(minerFulConstraints));
		
		ConstraintsBag constraintsBag = new ConstraintsBag(taskCharArchive.getTaskChars(), minerFulConstraints);
		String processModelName = model.getName();
		
		return new ProcessModel(taskCharArchive, constraintsBag, processModelName);
	}

}
