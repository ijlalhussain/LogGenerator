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
import java.util.Set;
import java.util.Map.Entry;

import org.processmining.plugins.declareminer.enumtypes.DeclareTemplate;
import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;
import org.processmining.plugins.declareminer.visualizing.AssignmentModel;
import org.processmining.plugins.declareminer.visualizing.AssignmentModelView;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.ConstraintTemplate;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;
import org.processmining.plugins.declareminer.visualizing.IItem;
import org.processmining.plugins.declareminer.visualizing.Language;
import org.processmining.plugins.declareminer.visualizing.LanguageGroup;
import org.processmining.plugins.declareminer.visualizing.Parameter;
import org.processmining.plugins.declareminer.visualizing.TemplateBroker;
import org.processmining.plugins.declareminer.visualizing.XMLBrokerFactory;

/*import ee.ut.deviance.declare.constraint.ConstraintDescription;
import ee.ut.deviance.declare.constraint.ConstraintResult;*/

public class DeclareModelGenerator {

	static Map<Integer, List<List<String>>> eventPermutations;
	static List<Integer> selectedConstraints;
	static Map<Integer, Alphabet> constraintMap;
	static Map<Integer, Alphabet> resultMapTrain;
	static Set<String> events;
	static nl.tue.declare.domain.model.AssignmentModel sourcemodel;
	static LinkedHashMap<String, Alphabet> abMapx;
	// take all the constraing info as an input

	// constraints map, events map, event permutations maps
	// example from writeSelectedConstraints method
	// Utils.writeSelectedConstraints(selectedConstraints, constraintMap, resultMapTrain, foldDir, eventPermutations);

	public DeclareModelGenerator(/* all the parameters here */) {
	}
	
	public DeclareModelGenerator(nl.tue.declare.domain.model.AssignmentModel model, LinkedHashMap<String, Alphabet> abMapx) {
		this.sourcemodel = model;
		this.abMapx = abMapx;
	}

	public DeclareModelGenerator(List<Integer> selectedConstraints,
			Map<Integer, Alphabet> constraintMap, Map<Integer, Alphabet> resultMapTrain,
			Map<Integer, List<List<String>>> eventPermutations, Set<String> events) {
		this.eventPermutations = eventPermutations;
		this.selectedConstraints = selectedConstraints;
		this.constraintMap = constraintMap;
		this.resultMapTrain = resultMapTrain;
		this.events = events;
	}

	public static AssignmentModel generateModel(/* all the parameters here */) {

		InputStream ir = ClassLoader.getSystemClassLoader().getResourceAsStream("resources/template.xml");
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

		TemplateBroker template = XMLBrokerFactory.newTemplateBroker(language.getAbsolutePath());
		List<Language> languages = template.readLanguages();
		Language lang = languages.get(0);
		AssignmentModel model = new AssignmentModel(lang);

		model.setName("new model");
		// create activitydefinition for each uniq label

		// SAVING FIRST ACTIVITIES IN THE MAP OF ACTIVITY DEFINITIONS
	//	Map<String, Integer> eventsWithIds = new HashMap<>(); // key = event na,e, value = idx
		Map<String, ActivityDefinition> activityDefinitions = new HashMap<>(); // key = event name
		int i = 0;
		int constraintNo =0;
		// activities in the model should have different Ids (i should be different for different activity definitions) if an activity with a given name is already available you have to use the existing one so please keep a list of added activities to check if an activity with a given name has already been added.
//		eventsWithIds.put(activityName, i);
		
		
		
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet filter = activity.getValue();
			String constraintName = "";
			if (!filter.constrain.isEmpty())
				constraintName = filter.constrain.toLowerCase();
			if (constraintName.equals("precedence")) {
				ActivityDefinition activitydefinition = model
						.addActivityDefinition(i);
				
				String aa = BranchCombination.getParentLetter(filter.alphabetkey);
				String bb = BranchCombination.getParentLetter(filter.secondAlphabetKey);
				if (bb.isEmpty()) bb ="B";
				String actName = k.replace(aa,bb);
				System.out.println("A: "+filter.alphabetkey);
				System.out.println("B: "+filter.secondAlphabetKey);
				System.out.println("CctL:" + actName);
				activitydefinition.setName(actName);
				activityDefinitions.put(actName, activitydefinition);
				i++;
			} else if(constraintName.equals("existence")) {
				
				ActivityDefinition activitydefinition = model
						.addActivityDefinition(i);
				String actName = k;
				activitydefinition.setName(k);
				activityDefinitions.put(actName, activitydefinition);
				i++;
			}
			
			else  {
				ActivityDefinition activitydefinition = model
						.addActivityDefinition(i);
				String actName = k;
				activitydefinition.setName(k);
				activityDefinitions.put(actName, activitydefinition);
				i++;
			}
		}
		
		//i=0;
		String constraintName = "";
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet filter = activity.getValue();
			
			

		//			System.out.println(eventsWithIds);

		//		
		//		Time required for step
		//		
		//		Map<String, Integer> eventsWithIds = new HashMap<>();
		//		
		//		
		//int constraintNo =0;
		//for (int constraintNo : selectedConstraints) {
		//for(nl.tue.declare.domain.model.ConstraintDefinition cd : sourcemodel.getConstraintDefinitions()){
			//String constraintName = cd.getName().replace("-", "").replace(" ", "");
			
			constraintName = "";
			if (!filter.constrain.isEmpty())
				constraintName = filter.constrain.toLowerCase();
			if (constraintName.isEmpty()){
				constraintName = "existence";;
			}
			
			
			if (constraintName.equals("existence")){				
				if (BranchCombination.getParentLetter(k).equals(k))
				{
					continue;
				}
				if (filter.isSingle){
					continue;
				}
			}
			
			//	String constraintName = "take the constraint name from the original model";
			//	int numParams = constraintMap.get(constraintNo).getNumParams();
			/*	int eventsIdx = constraintMap.get(constraintNo).getEventsIdx();
			List<String> constraintParams = eventPermutations.get(numParams).get(eventsIdx);
			 */
			HashMap<String, DeclareTemplate> templateNameStringDeclareTemplateMap;
			Map<DeclareTemplate, ConstraintTemplate> map;
			ConstraintDefinition constraintdefinition = null;
			
			switch (constraintName) {
			case "absence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("absence", DeclareTemplate.Absence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Absence));
				break;
			case "absence2" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("absence2", DeclareTemplate.Absence2);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Absence2));
				break;
			case "absence3" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("absence3", DeclareTemplate.Absence3);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Absence3));
				break;
			case "alternate_precedence" : case "alternate precedence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("alternate precedence",
						DeclareTemplate.Alternate_Precedence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Alternate_Precedence));
				break;
			case "alternate_response" : case "alternate response" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("alternate response", DeclareTemplate.Alternate_Response);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Alternate_Response));
				break;
			case "alternate_succession" : case "alternate succession" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("alternate succession",
						DeclareTemplate.Alternate_Succession);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Alternate_Succession));
				break;
			case "chain_precedence" :case "chain precedence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();
				templateNameStringDeclareTemplateMap.put("chain precedence", DeclareTemplate.Chain_Precedence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Chain_Precedence));
				break;
			case "chain_response" : case "chain response" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("chain response", DeclareTemplate.Chain_Response);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Chain_Response));
				break;
			case "chain_succession" : case "chain succession" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("chain succession", DeclareTemplate.Chain_Succession);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Chain_Succession));
				break;
			case "choice" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("choice", DeclareTemplate.Choice); //// ???? of
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Choice));
				break;
			case "coexistence" : case "co-existence":
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("co-existence", DeclareTemplate.CoExistence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.CoExistence));
				break;
			case "exactly1" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("exactly1", DeclareTemplate.Exactly1);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Exactly1));
				break;
			case "exactly2" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("exactly2", DeclareTemplate.Exactly2);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Exactly2));
				break;
			case "exclusive_choice" : case "exclusive choice" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("exclusive choice", DeclareTemplate.Exclusive_Choice);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Exclusive_Choice));
				break;
			case "existence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("existence", DeclareTemplate.Existence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Existence));
				break;
			case "existence2" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("existence2", DeclareTemplate.Existence2);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Existence2));
				break;
			case "existence3" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("existence3", DeclareTemplate.Existence3);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Existence3));
				break;
			case "init" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("init", DeclareTemplate.Init);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model, map.get(DeclareTemplate.Init));
				break;
			case "not_chain_succession" : case "not chain succession" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("not chain succession",
						DeclareTemplate.Not_Chain_Succession);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Not_Chain_Succession));
				break;
			case "not_coexistence" : case "not coexistence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("not co-existence", DeclareTemplate.Not_CoExistence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Not_CoExistence));
				break;
			case "not_succession" : case "not succession" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("not succession", DeclareTemplate.Not_Succession);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Not_Succession));
				break;
			case "precedence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("precedence", DeclareTemplate.Precedence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Precedence));
				break;
			case "response" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("response", DeclareTemplate.Response);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Response));
				break;
			case "responded_existence": case "responded existence" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("responded existence",
						DeclareTemplate.Responded_Existence);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Responded_Existence));
				break;
			case "succession" :
				templateNameStringDeclareTemplateMap = new HashMap<String, DeclareTemplate>();

				templateNameStringDeclareTemplateMap.put("succession", DeclareTemplate.Succession);
				map = DeclareModelGenerator.readConstraintTemplates(templateNameStringDeclareTemplateMap);
				constraintdefinition = new ConstraintDefinition(constraintNo, model,
						map.get(DeclareTemplate.Succession));
				}
			
			if (constraintName.equals("existence")){
				if (!filter.isSingle){
				String aa = BranchCombination.getParentLetter(filter.alphabetname);
				String bb = BranchCombination.getParentLetter(filter.secondAlphabetKey);
				for (Parameter p : constraintdefinition.getParameters()) {
					if (p.getName().equals("A")) {
						String actName = filter.alphabetkey;
						System.out.println("existence 1st : " + actName);
						constraintdefinition.addBranch(p,
								activityDefinitions.get(actName));
					//	constraintdefinition.getBranches(p);
						//constraintdefinition.getBranches(p);
						
						if (filter.correlationlist != null){
							String actNamew = filter.correlationlist[1];
							ActivityDefinition activitydefinition = model.addActivityDefinition(i);
							constraintdefinition.addBranch(p,activityDefinitions.get(actNamew));
							activitydefinition.setName(actNamew);							
							System.out.println("existence cor : " + actNamew);
							activityDefinitions.put(actNamew, activitydefinition);							
						}
						
					
					} 
				}
				}
			} // end of precedence
			
// end of existen
			else	if (constraintName.equals("precedence")){
				String aa = BranchCombination.getParentLetter(filter.alphabetname);
				String bb = BranchCombination.getParentLetter(filter.secondAlphabetKey);
				for (Parameter p : constraintdefinition.getParameters()) {
					if (p.getName().equals("B")) {
						String aaa = BranchCombination.getParentLetter(filter.alphabetkey);
						String bbb = BranchCombination.getParentLetter(filter.secondAlphabetKey);
						String actName = k.replace(aaa,bbb);
						System.out.println("precedence 1st : " + actName);
						//String actName = filter.alphabetkey;
					//	System.out.print("B: " + actName);
						constraintdefinition.addBranch(p,
								activityDefinitions.get(actName));
						//constraintdefinition.getBranches(p);
					} else {
								if (filter.correlationlist != null){
						for(int j=0; j < filter.correlationlist.length ; j++){
							ActivityDefinition activitydefinition = model.addActivityDefinition(i);
							String actName = "A: "+ filter.correlationlist[j].replace(bb, aa); 
							activitydefinition.setName(actName);
							System.out.println("precedence 2nd : " + actName);
							activityDefinitions.put(actName, activitydefinition);
						
							constraintdefinition.addBranch(p, activityDefinitions.get(actName));
						//	constraintdefinition.getBranches(p);
						
						}}
					}
				}
				
			} // end of precedence
			else {
				for (Parameter p : constraintdefinition.getParameters()) {
					if (p.getName().equals("A")) {
						String actName = filter.alphabetkey;
						System.out.println("Response 1st: " + actName);
						constraintdefinition.addBranch(p,
								activityDefinitions.get(actName));
					//	constraintdefinition.getBranches(p);
					} else {
						
						if (k.equals("b")){
							ActivityDefinition activitydefinition = model
									.addActivityDefinition(i);
							
							String actName = "2nd: "
									+ filter.secondAlphabetKey;
							activitydefinition.setName(actName);
							activityDefinitions.put(actName, activitydefinition);
							System.out.println("Response 2nd: " + actName);
							constraintdefinition.addBranch(p,activityDefinitions.get(actName));
						
						}
						else {			
								
							if (filter.correlationlist != null){
						for (int j = 0; j < filter.correlationlist.length; j++) {
							ActivityDefinition activitydefinition = model.addActivityDefinition(i);
							String actName =  filter.correlationlist[j];
							activitydefinition.setName(actName);
							activityDefinitions.put(actName, activitydefinition);
							constraintdefinition.addBranch(p,activityDefinitions.get(actName));
							//i++;
							//constraintdefinition.getBranches(p);
						}}
						}
					}
				}
			}
			constraintNo ++;
			i++;
			model.addConstraintDefiniton(constraintdefinition);

		}

		//AssignmentModelView view = new AssignmentModelView(model);
	//	DeclareMap outputMap = new DeclareMap(model, null, view, null, null, null);
	//	return outputMap;
		
		System.out.println("Modle print_________________");
		for(ConstraintDefinition cd : model.getConstraintDefinitions()){
			  String s = cd.getCondition().toString();
			System.out.println("Constrains : " +  cd.getName());
			
				System.out.println(cd.getCondition() );
			//	checkCondtion(cd.getCondition().toString());
				for(Parameter p : cd.getParameters()){
					
					for(ActivityDefinition ad : cd.getBranches(p)){
						if (p.getName().equals("A")) {
							System.out.println("1st: "+ ad.getName());
						} else
						{
							System.out.println("2nd: "+ ad.getName());
						}
					}
				}
				System.out.println("_________________________________");	
			}
		
		
return model;
	}

	public static Map<DeclareTemplate, ConstraintTemplate> readConstraintTemplates(
			Map<String, DeclareTemplate> templateNameStringDeclareTemplateMap) {
		InputStream templateInputStream = ClassLoader.getSystemClassLoader()
				.getResourceAsStream("resources/template.xml");
		File languageFile = null;
		try {
			languageFile = File.createTempFile("template", ".xml");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(templateInputStream));
			String line = bufferedReader.readLine();
			PrintStream out = new PrintStream(languageFile);
			while (line != null) {
				out.println(line);
				line = bufferedReader.readLine();
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		TemplateBroker templateBroker = XMLBrokerFactory.newTemplateBroker(languageFile.getAbsolutePath());
		List<Language> languagesList = templateBroker.readLanguages();

		//the first language in the list is the condec language, which is what we need
		Language condecLanguage = languagesList.get(0);
		List<IItem> templateList = new ArrayList<IItem>();
		List<IItem> condecLanguageChildrenList = condecLanguage.getChildren();
		for (IItem condecLanguageChild : condecLanguageChildrenList) {
			if (condecLanguageChild instanceof LanguageGroup) {
				templateList.addAll(visit(condecLanguageChild));
			} else {
				templateList.add(condecLanguageChild);
			}
		}

		Map<DeclareTemplate, ConstraintTemplate> declareTemplateConstraintTemplateMap = new HashMap<DeclareTemplate, ConstraintTemplate>();

		for (IItem item : templateList) {
			if (item instanceof ConstraintTemplate) {
				ConstraintTemplate constraintTemplate = (ConstraintTemplate) item;
				//				System.out.println(constraintTemplate.getName()+" @ "+constraintTemplate.getDescription()+" @ "+constraintTemplate.getText());
				if (templateNameStringDeclareTemplateMap.containsKey(constraintTemplate.getName().toLowerCase())) {
					declareTemplateConstraintTemplateMap.put(
							templateNameStringDeclareTemplateMap.get(constraintTemplate.getName().toLowerCase()),
							constraintTemplate);
					System.out.println(constraintTemplate.getName() + " @ " + templateNameStringDeclareTemplateMap
							.get(constraintTemplate.getName().replaceAll("-", "").toLowerCase()));
				}
			}
		}

		return declareTemplateConstraintTemplateMap;
	}

	private static List<IItem> visit(IItem item) {
		List<IItem> templateList = new ArrayList<IItem>();
		if (item instanceof LanguageGroup) {
			LanguageGroup languageGroup = (LanguageGroup) item;
			List<IItem> childrenList = languageGroup.getChildren();
			for (IItem child : childrenList) {
				if (child instanceof LanguageGroup) {
					templateList.addAll(visit(child));
				} else {
					templateList.add(child);
				}
			}
		}
		return templateList;
	}

}
