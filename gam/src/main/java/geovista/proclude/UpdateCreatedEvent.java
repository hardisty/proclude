package geovista.proclude;

/**
 * The object for update created events.  It contains a Vector of all created
 * ellipses and an index indicating which one (if any) is active.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;
import java.util.Vector;

public class UpdateCreatedEvent extends EventObject{

  private Vector createdList;
  private int activeIndex;

  public UpdateCreatedEvent(Object source, Vector ellipses, int index) {
    super(source);
    createdList = ellipses;
    activeIndex = index;
  }

  /**
   * Returns the list of created ellipses.
   */

  public Vector getCreatedList(){
    return createdList;
  }

  /**
   * Returns the index of the active ellipse in the list of created ellipses (or
   * -1 if there is no active created ellipse.
   */

  public int getActiveIndex(){
    return activeIndex;
  }

}