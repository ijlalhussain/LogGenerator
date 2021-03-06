/* Generated by Together */

package nl.tue.declare.domain.template;

import java.util.*;
import java.util.Map.Entry;

import nl.tue.declare.domain.*;
import nl.tue.declare.domain.instance.State;

public class ConstraintTemplate
    extends Base implements IItem {

  private Language language = null;

  private String description = "";;
  private String display = "";;
  private String text = "";;
  private String name = "";;
  
  private HashMap<State, String> stateMessages = new HashMap<State, String> ();

  protected BaseCollection<Parameter> parameters;

  /**
   * ConstraintTemplate
   *
   * @param id int
   * @param lang int
   */
  public ConstraintTemplate(int id, Language lang) {
	  super(id);
	  parameters = new BaseCollection<Parameter>();
	  language = lang;
	  for (State s: State.values()){
		  this.stateMessages.put(s, s.name() +" undefined");
	  }
  }

  public ConstraintTemplate(int id, ConstraintTemplate t) {
	  this(id, t.language);
	  setDescription(t.getDescription());
	  setDisplay(t.getDisplay());
	  t.cloneAttributes(this);
	  for (State s: State.values()){
		  this.stateMessages.put(s, t.getStateMessage(s));
	  }
  }


  public String getDescription() {
    return description;
  }

  public String getDisplay() {
    return display;
  }

  /**
   * isUnary
   *
   * @return boolean
   */
  public boolean isUnary() {
    return this.parameterCount() == 1;
  }

  /**
   * isBinary
   *
   * @return boolean
   */
  public boolean isBinary() {
    return this.parameterCount() == 2;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  /**
   *
   * @return Object
   */
  public Object clone() {
    ConstraintTemplate myClone = new ConstraintTemplate(getId(), language);
    myClone.setDescription(this.getDescription());
    myClone.setDisplay(this.getDisplay());
    cloneAttributes(myClone);
	for (State s: State.values()){
	  myClone.stateMessages.put(s, this.getStateMessage(s));
	}
    return myClone;
  }

  public Language getLanguage() {
    return this.language;
  }

  /**
   *
   * @return String
   */
  public String getName() {
    return name;
  }

  /**
   *
   * @return String
   */
  public String getText() {
    return text;
  }

  /**
   *
   * @param name String
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   *
   * @param text String
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   *
   * @param parameter FormalParameter
   */
   public void addParameter(Parameter parameter) {
    this.parameters.add(parameter);
  }

  /**
   *
   * @param name FormalParameter
   * @param type ParameterType
   * @return FormalParameter
   */
   public Parameter addParameter(String name) {
    Parameter parameter = new Parameter(parameters.nextId(), name);
    addParameter(parameter);
    return parameter;
  }
  
  public Parameter getFirstParameter(){
	    if (parameters.isEmpty()){
	    	return null;
	    }
	    return parameters.get(0);
  }
  
  public Parameter getParameterWithId(int id){
	  return parameters.getItemWithId(id);
  }

  /**
   * attributeCount
   *
   * @return int
   */
  public int parameterCount() {
    return this.parameters.size();
  }

  /**
   * attributeExists
   * @param param FormalParameter
   * @return boolean
   */
  public boolean parameterExists(Parameter param) {
    return parameters.contains(param);
  }

  /**
   * deleteAttribute
   *
   * @param param LTLFormalParameter
   * @return boolean
   */
  protected void deleteParameter(Parameter param) {
    parameters.remove(new Integer(param.getId()));    	
  }
  
	public Collection<Parameter> getParameters(){
		return parameters;
	}


  public String toString() {
    String parameters = "";
    for (Parameter p: this.parameters) {
      if (!parameters.equals("")) {
        parameters += ", ";
      }
      parameters += p.toString();
    }
    return new String(name) + "(" + parameters + ")";
  }

  /**
   *
   * @param clone LTLFormula
   */
  private void cloneAttributes(ConstraintTemplate clone) {
    for (Parameter p: parameters) {
      Object parameter = p.clone();
      if (parameter instanceof Parameter) {
        clone.addParameter( (Parameter) parameter);
      }
    }
    clone.setName(this.getName());
    clone.setText(this.getText());
  }

  public int getMaxId() {
    return this.getId();
  }

  public IItem withId(int id) {
    return (getId() == id) ? this : null;
  }

  public boolean exists(IItem item) {
    return item.equals(this);
  }
  
  public Set<Entry<State, String>> getStateMessages(){
	  return this.stateMessages.entrySet();
  }
  
  public String getStateMessage(State state){
	  String msg = this.stateMessages.get(state);
	  return (msg != null)?msg:"undefined";
  }
  
  public void setStateMessage(State state, String message){
	  this.stateMessages.put(state, message);
  }

}
