package geovista.proclude;

/**
 * Object for the update GAM events.  It contains the index of the active
 * GAM ellipse (if any).
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;

public class UpdateGAMEvent extends EventObject{

  private int activeIndex;
  private int activeSet;

  public UpdateGAMEvent(Object source, int solutionSet, int index) {
    super(source);
    activeSet = solutionSet;
    activeIndex = index;
  }

  /**
   * Returns the index of the active GAM ellipse in the solution Vector or -1 if
   * there is no active GAM ellipse.
   */

  public int getActiveIndex(){
    return activeIndex;
  }

  public int getActiveSet(){
    return activeSet;
  }

}