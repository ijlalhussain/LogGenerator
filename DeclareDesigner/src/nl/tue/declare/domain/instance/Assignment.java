/* Generated by Together */

package nl.tue.declare.domain.instance;

import java.util.ArrayList;

import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.organization.*;

public class Assignment
    extends AssignmentModel {

  private int id = 0;
  private Team team;
  private State state;
  private ArrayList<Event> log;

  /**
   * Assignment
   *
   * @param anCase Case
   * @param anAssignmentModel AssignmentModel
   */
  public Assignment(int ID, AssignmentModel anAssignmentModel) {
    super(anAssignmentModel);
    id = ID;
    team = new Team(this);
    log = new ArrayList<Event>(50);
  }

 private Assignment(Assignment assignment) {
     super(assignment);
     id = assignment.getId();
     team = (Team) assignment.getTeam().clone();
     log = new ArrayList<Event>();
     for (Event event: assignment.log){
    	 log.add((Event) event.clone());
     }
  }
 
 public Object clone() {
    return new Assignment(this);
  }

 public Assignment clone(int ID) {
     Assignment clone = new Assignment(ID, this);
     clone.team = (Team) team.clone();
     return clone;
   }
 
 public void clearEventLog(){
	 log.clear();
 }
 
 public Iterable<Event> getEventLog(){
	 return log;
 }
 
 public void addEvent(Event event){
	 log.add(event);
 }
 

 public boolean equals(Object obj) {
    boolean equals = false;
    if (obj instanceof Assignment) {
      Assignment a = (Assignment) obj;
      equals = this.id == a.getId();
    }
    return equals;
  }

  /**
   * addactivityDefinition
   *
   * @return activityDefinition
   * @param activityDefinition ActivityDefinition
   */
  protected boolean addActivityDefinition(ActivityDefinition activityDefinition) {
    return super.addActivityDefinition(new Activity( (ActivityDefinition)
        activityDefinition.clone(), this));
  }

  public boolean addConstraintDefiniton(ConstraintDefinition
                                        constraintDefinition) {
    return super.addConstraintDefiniton(new Constraint(constraintDefinition, this));
  }

  /**
   * jobExists
   *
   * @param activity Job
   * @return boolean
   */
  public boolean activityExists(Activity activity) {
    return super.ActivityDefinitionExists(activity);
  }

  /**
   * activityDefinitionAt
   *
   * @param anIndex int
   * @return Job
   */
  public Activity activityAt(int anIndex) {
    ActivityDefinition definition = super.activityDefinitionAt(anIndex);
    return toActivity(definition);
  }

  private Activity toActivity(ActivityDefinition definition) {
    Activity activity = null;
    if (definition != null) {
      activity = (Activity) definition;
    }
    return activity;
  }

  /**
   *
   * @param name int
   * @return Activity
   */
  public Activity activityWithName(String name) {
    ActivityDefinition definition = super.activityDefinitionWithName(name);
    return this.toActivity(definition);
  }

  /**
   * jobsCount
   *
   * @return int
   */
  public int activitiesCount() {
    return super.activityDefinitionsCount();
  }

  /**
   * add
   *
   * @param element DataElement
   * @return boolean
   */
  public boolean addData(DataElement element) {
    DataField field = new DataField(element);
    if (element instanceof DataField) {
      field.push( ( (DataField) element).getValue());
    }
    return super.addData(field);
  }

  public int dataCount() {
    return super.getDataCount();
  }

  public DataField dataAt(int index) {
    DataField field = null;
    if (index < this.dataCount()) {
      field = (DataField)super.dataAt(index);
    }
    return field;
  }

  public Team getTeam() {
    return this.team;
  }

  public DataField getDataFied(String name, String type) {
    int i = 0;
    boolean found = false;
    DataField dataField = null;
    while (i < this.dataCount() && !found) {
      dataField = this.dataAt(i++);
      found = dataField.maps(name, type);
    }
    return found ? dataField : null;
  }

  public DataField getDataFiedWithName(String name) {
    int i = 0;
    boolean found = false;
    DataField dataField = null;
    while (i < this.dataCount() && !found) {
      dataField = this.dataAt(i++);
      found = dataField.getName().equals(name);
    }
    return found ? dataField : null;
  }


 public String toString() {
     return  Integer.toString(id) + ": " + this.getName();
  }

  public int getId() {
    return id;
  }


  /**
   * activityDefinitionAt
   *
   * @param anIndex int
   * @return Job
   */
  public Constraint constraintAt(int anIndex) {
    return (Constraint)super.constraintDefinitionAt(anIndex);
  }

  /**
   *
   * @param id int
   * @return Constraint
   */
  public Constraint constraintWithId(int id) {
    return (Constraint)super.constraintWithId(id);
  }

  /**
   * isTrue()
   *
   * @return boolean
   */
  public boolean isTrue() {
    boolean ok = true;
    int i = 0;
    while ( (i < constraintDefinitionsCount()) && ok) {
      Constraint constraint = (Constraint)super.constraintDefinitionAt(i++);
      ok = constraint.getState().equals(State.SATISFIED);
    }
    return ok;
  }

  /**
   * isTrue()
   *
   * @return DefaultState
   */
  public State getState() {
    return state;
  }

  public void setState(State state) {
    if (state != null) {
      this.state = state;
    }
  }

   /**
    *
    * @return int
    */
   public int hashCode() {
     int hash = 7;
     int var_code = this.getId();
     hash = 31 * hash + var_code;
     return hash;
  }

  /**
   *
   * @param definition ActivityDefinition
   * @return Activity
   */
  public Activity getActivityForDefinition(ActivityDefinition definition) {
    return this.toActivity(definition);
  }

  /**
   *
   * @param activity Activity
   * @param user User
   * @return boolean
   */
  public boolean authorized(ActivityDefinition activity, User user) {
    boolean auth = this.team.isMember(user); // first check it the user is a memebre of the team
    if (auth) { // if the user belongs to the team
      Authorization autorization = activity.
          getAuthorization(); // get the authorization of the activity
      auth = team.authorized(autorization, user); // check if the user is authorized to execute the activity
    }
    return auth;
  }

  /**
   * event
   */
  public void event() {
    for (int i = 0; i < this.constraintDefinitionsCount(); i++) {
      this.constraintAt(i).resetStatus();
    }
  }

  public void printData() {
    for (int i = 0; i < dataCount(); i++) {
      dataAt(i).print();
    }
  }
}
