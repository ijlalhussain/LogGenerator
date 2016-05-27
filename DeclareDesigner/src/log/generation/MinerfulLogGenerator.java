package log.generation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.processmining.plugins.declareminer.enumtypes.DeclareTemplate;
import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;
import org.processmining.plugins.declareminer.visualizing.AssignmentModel;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.ConstraintTemplate;
import org.processmining.plugins.declareminer.visualizing.Language;
import org.processmining.plugins.declareminer.visualizing.Parameter;
import org.processmining.plugins.declareminer.visualizing.TemplateBroker;
import org.processmining.plugins.declareminer.visualizing.XMLBrokerFactory;

public class MinerfulLogGenerator {
	
	
	public static void createmodel(AssignmentModel sourcemodel,LinkedHashMap<String, Alphabet> abMapx) {
	/*	InputStream ir = ClassLoader.getSystemClassLoader().getResourceAsStream("resources/template.xml");
		File language = null;
		try {
			language = File.createTempFile("template", ".xml");
			BufferedReader br = new BufferedReader(new InputStreamReader(ir));
			String line = br.readLine();
			PrintStream out = new PrintStream(language);
			while (line != null) {
				out.println(line);
				line = br.readLine();
			}
			out.flush();
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		TemplateBroker templates = XMLBrokerFactory.newTemplateBroker(language.getAbsolutePath());
		List<Language> languages = templates.readLanguages();
		Language lang = languages.get(0);
		AssignmentModel targetmodel = new AssignmentModel(lang);

		targetmodel.setName("new model");
		
		Map<String, Integer> eventsWithIds = new HashMap<>(); // key = event na,e, value = idx
		Map<String, ActivityDefinition> activityDefinitions = new HashMap<>(); // key = event name
		int repeater=0;
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String key = activity.getKey();
			Alphabet filter = activity.getValue();
				if (!eventsWithIds.containsKey(key)) {
					eventsWithIds.put(key, repeater);
					ActivityDefinition activitydefinition = targetmodel.addActivityDefinition(repeater);
					activitydefinition.setName(key);
					activityDefinitions.put(key, activitydefinition);
					repeater++;
				}
				
				if (!filter.secondAlphabetKey.isEmpty()) {
					
					String alphabetname = BranchCombination.getParentLetter(key);
				
					ActivityDefinition activitydefinition = targetmodel.addActivityDefinition(repeater);
				//String xnam = " 1st : "+ k.replace(k,filter.secondAlphabetKey);//filter.correlationlist[j]; 
					System.out.println(key+alphabetname + filter.secondAlphabetKey);
					System.out.println("K "+ key.replace(alphabetname,filter.secondAlphabetKey));
				System.out.println("RR"+  key.replace(key,filter.secondAlphabetKey));
			//	System.out.println( k.replace(k,filter.secondAlphabetKey));
				String xnam = " 1st : "+ key.replace(key,filter.secondAlphabetKey);//filter.correlationlist[j]; \
				xnam = k.replace(alphabetname,filter.secondAlphabetKey);
				activitydefinition.setName(xnam);
				System.out.println("precedence 2nd : " + xnam);
				activityDefinitions.put(xnam, activitydefinition);							
				constraintdefinition.addBranch(p, activityDefinitions.get(xnam));
					
				} // second key
				
				else {
				
				if (filter.correlationlist != null){
					for(int j=0; j < filter.correlationlist.length ; j++){
						if((filter.correlationlist[j].length() == k.length()+3)
							&&(filter.correlationlist[j].contains(b2[ndxx]))){
							String alphabetname = BranchCombination.getParentLetter(filter.correlationlist[j]);
							if (filter.correlationlist[j].contains(k)){
							ActivityDefinition activitydefinition = model.addActivityDefinition(i);
						String xnam = " Error 1st : "+ filter.correlationlist[j]; 
						activitydefinition.setName(xnam);
						System.out.println("precedence 2nd : " + xnam);
						activityDefinitions.put(xnam, activitydefinition);							
						constraintdefinition.addBranch(p, activityDefinitions.get(xnam));
						i++;
					//	constraintdefinition.getBranches(p);
						}}
					}
					
				}
				}
		}
		
		
		TaskCharEncoderDecoder encdec = new TaskCharEncoderDecoder();
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet filter = activity.getValue();
		
		}
		
		  int constraintNo=0;
			HashMap<String, DeclareTemplate> templateNameStringDeclareTemplateMap;
			Map<DeclareTemplate, ConstraintTemplate> map;
			ConstraintDefinition constraintdefinition = null;
			String constraintName="";
			switch (constraintName) {
			case "absence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("absence", DeclareTemplate.Absence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Absence));
				break;
			case "absence2" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("absence2", DeclareTemplate.Absence2);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Absence2));
				break;
			case "absence3" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("absence3", DeclareTemplate.Absence3);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Absence3));
				break;
			case "alternate_precedence" : case "alternate precedence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("alternate precedence",
						DeclareTemplate.Alternate_Precedence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Alternate_Precedence));
				break;
			case "alternate_response" : case "alternate response" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("alternate response", DeclareTemplate.Alternate_Response);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Alternate_Response));
				break;
			case "alternate_succession" : case "alternate succession" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("alternate succession",
						DeclareTemplate.Alternate_Succession);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Alternate_Succession));
				break;
			case "chain_precedence" :case "chain precedence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("chain precedence", DeclareTemplate.Chain_Precedence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Chain_Precedence));
				break;
			case "chain_response" : case "chain response" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("chain response", DeclareTemplate.Chain_Response);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Chain_Response));
				break;
			case "chain_succession" : case "chain succession" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("chain succession", DeclareTemplate.Chain_Succession);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Chain_Succession));
				break;
			case "choice" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("choice", DeclareTemplate.Choice); //// ???? of
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Choice));
				break;
			case "coexistence" : case "co-existence":
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("co-existence", DeclareTemplate.CoExistence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.CoExistence));
				break;
			case "exactly1" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("exactly1", DeclareTemplate.Exactly1);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Exactly1));
				break;
			case "exactly2" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("exactly2", DeclareTemplate.Exactly2);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Exactly2));
				break;
			case "exclusive_choice" : case "exclusive choice" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("exclusive choice", DeclareTemplate.Exclusive_Choice);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Exclusive_Choice));
				break;
			case "existence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("existence", DeclareTemplate.Existence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Existence));
				break;
			case "existence2" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("existence2", DeclareTemplate.Existence2);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Existence2));
				break;
			case "existence3" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("existence3", DeclareTemplate.Existence3);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Existence3));
				break;
			case "init" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("init", DeclareTemplate.Init);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel, map.get(DeclareTemplate.Init));
				break;
			case "not_chain_succession" : case "not chain succession" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("not chain succession",
						DeclareTemplate.Not_Chain_Succession);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Not_Chain_Succession));
				break;
			case "not_coexistence" : case "not coexistence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("not co-existence", DeclareTemplate.Not_CoExistence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Not_CoExistence));
				break;
			case "not_succession" : case "not succession" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("not succession", DeclareTemplate.Not_Succession);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Not_Succession));
				break;
			case "precedence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("precedence", DeclareTemplate.Precedence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Precedence));
				break;
			case "response" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("response", DeclareTemplate.Response);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Response));
				break;
			case "responded_existence": case "responded existence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("responded existence",
						DeclareTemplate.Responded_Existence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Responded_Existence));
				break;
			case "succession" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("succession", DeclareTemplate.Succession);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, targetmodel,
						map.get(DeclareTemplate.Succession));
				}
			
			constraintNo++;
		
		
			int j = 0;
			for (Parameter p : constraintdefinition.getParameters()) {
				String eventName;
		    	constraintdefinition.addBranch(p, activityDefinitions.get(eventName));
				}

				j++;
			}

			model.addConstraintDefiniton(constraintdefinition);*/
		
	}
	
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
				encdec.encode(new StringTaskClass( filter.alphabetkey));
				if (!filter.alphabetkey.isEmpty())
				{
					String [] jj = filter.secondAlphabet.split("::");
					if(jj.length>=1){
					for (int i=0; i <jj.length; i++ ){
						System.out.println(jj[i]);
						encdec.encode(new StringTaskClass( jj[i]));
						System.out.println("Hello: "+filter.alphabetkey + "Second: " + jj[i] );	
					}}
				} else {
					encdec.encode(new StringTaskClass(k+"test"));
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
