/* Generated by Together */

package nl.tue.declare.datamanagement.organization;

import java.util.*;

import org.w3c.dom.*;
import nl.tue.declare.datamanagement.*;
import nl.tue.declare.domain.organization.*;

public class XMLOgranizationBroker
    extends XMLBroker implements OrganizationBroker {

//  private static final String TAG_ROLES = "roles";
//  private static final String TAG_USERS = "users";

  private OrganizationElementFactory factory;

  /**
   * XMLOgranizationBroker
   *
   * @param aConnectionString Document
   * @param name String
   */
  public XMLOgranizationBroker(String aConnectionString, String name) {
    super(aConnectionString, name);
    factory = new OrganizationElementFactory(this);
    readDocument();
  }


  /**
   * addUser
   *
   * @param anUser User
   */
  public void addUser(User anUser) {
    Element newUser = factory.userToElement(anUser);
    Element users = this.getUsersElement();
    users.appendChild(newUser);
    writeDocument();
  }

  /**
   * deleteUser
   *
   * @param anUser User
   * @return boolean
   */
  public boolean deleteUser(User anUser) {
    Element element = this.getUserElement(anUser);
    if (element != null) {
      //ElementFactory.deleteElement(element, this.getUsersElement());
      deleteElement(element, this.getUsersElement());
      writeDocument();
      return true;
    }
    return false;
  }

  /**
   * editUser
   *
   * @param anUser User
   * @return boolean
   */
  public boolean editUser(User anUser) {
    Element element = this.getUserElement(anUser);
    if (element != null) {
      factory.updateUserElement(anUser, element);
      writeDocument();
      return true;
    }
    return false;
  }

  /**
   * addRole
   *
   * @param aRole Role
   */
  public void addRole(Role aRole) {
    Element newRole = factory.roleToElement(aRole);
    Element roles = this.getRolesElement();
    roles.appendChild(newRole);
    writeDocument();
  }

  /**
   * deleteRole
   *
   * @param aRole Role
   * @return boolean
   */
  public boolean deleteRole(Role aRole) {
    Element element = this.getRoleElement(aRole);
    //Element element = ElementFactory.getRoleElement(aRole,this.getRolesElement(),this);
    if (element != null) {
      //ElementFactory.deleteElement(element, this.getRolesElement());
      deleteElement(element, this.getRolesElement());
      writeDocument();
      return true;
    }
    return false;
  }

  /**
   * editRole
   *
   * @param aRole Role
   * @return boolean
   */
  public boolean editRole(Role aRole) {
    Element element = this.getRoleElement(aRole);
    if (element != null) {
      factory.updateRoleElement(aRole, element);
      writeDocument();
      return true;
    }
    return false;
  }

  /**
   * getRoleElement
   *
   * @param role Role
   * @return Element
   */
  private Element getRoleElement(Role role) {
    return factory.getRoleElement(role, this.getRolesElement());
  }

  /**
   * getUserElement
   *
   * @param user User
   * @return Element
   */
  private Element getUserElement(User user) {
    return factory.getUserElement(user, this.getUsersElement());
  }

  /**
   * readRoles
   *
   * @return List
   */
  public List<Role> readRoles() {
    List<Role> list = new ArrayList<Role> ();
    Element element;
    Role role;
    NodeList roles = OrganizationElementFactory.getListRoles(this.getRolesElement());

    for (int i = 0; i < roles.getLength(); i++) {
      element = (Element) roles.item(i);
      role = factory.elementToRole(element);
      list.add(role);
    }
    return list;
  }

  /**
   * getRolesElement
   *
   * @return Element
   */
  private Element getRolesElement() {
    //return factory.getFirstElement(this.getDocumentRoot(), TAG_ROLES);
    return factory.getRolesElement(this.getDocumentRoot());
  }

  /**
   * getUsersElement
   *
   * @return Element
   */
  private Element getUsersElement() {
    //return this.getFirstElement(this.getDocumentRoot(), TAG_USERS);
    return factory.getUsersElement(this.getDocumentRoot());
  }

  /**
   * readUsers
   *
   * @return List
   */
  public List<User> readUsers() {
    List<User> list = new ArrayList<User> ();
    Element element;
    User user;
    NodeList users = OrganizationElementFactory.getListUsers(this.getUsersElement());

    for (int i = 0; i < users.getLength(); i++) {
      element = (Element) users.item(i);
      user = factory.elementToUser(element);
      list.add(user);
    }
    return list;
  }

  /**
   * assignRole
   *
   * @param anUser User
   * @param aRole Role
   */
  public void assignRole(User anUser, Role aRole) {
    Element user = this.getUserElement(anUser);
    factory.addUserRole(user, aRole);
    writeDocument();
  }

  /**
   * disassignRole
   *
   * @param anUser User
   * @param aRole Role
   */
  public void disassignRole(User anUser, Role aRole) {
    Element user = this.getUserElement(anUser);
    factory.removeUserRole(user, aRole);
    writeDocument();
  }

  /**
   * readAssignedRoles
   *
   * @param anUser User
   * @return List
   */
  public List<Role> readAssignedRoles(User anUser) {
    List<Role> list = new ArrayList<Role> ();
    Element userElement = this.getUserElement(anUser);
    Element element;
    Role role;
    NodeList roles = OrganizationElementFactory.getListRoles(userElement);

    for (int i = 0; i < roles.getLength(); i++) {
      element = (Element) roles.item(i);
      role = new Role(factory.elementToBase(element).getId());
      list.add(role);
    }
    return list;
  }
}
