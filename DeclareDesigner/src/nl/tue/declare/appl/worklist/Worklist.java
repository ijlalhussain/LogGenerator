package nl.tue.declare.appl.worklist;

import nl.tue.declare.appl.*;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author Maja Pesic
 * @version 1.0
 */
public class Worklist {
  public static void main(String[] args) {
    Project.ini(args);
    try {
      new WorklistCoordinator();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
