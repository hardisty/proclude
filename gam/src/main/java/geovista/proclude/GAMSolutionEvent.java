package geovista.proclude;

/**
 * This class contains the GAM Solution event.  It indicates that the GAM has
 * finished, and contains the solution GAM ended with.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;
import java.util.Vector;

public class GAMSolutionEvent extends EventObject{

  private Vector solution;

  public GAMSolutionEvent(Object source, Vector genes) {
    super(source);
    solution = genes;
  }

  /**
   * Returns the solution.
   */

  public Vector getSolution(){
    return solution;
  }
}