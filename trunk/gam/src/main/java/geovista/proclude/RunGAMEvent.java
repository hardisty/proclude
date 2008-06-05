package geovista.proclude;

/**
 * Object for the Run GAM event.  This is a trivial extension of EventObject.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;

public class RunGAMEvent extends EventObject{

  public RunGAMEvent(Object source) {
    super(source);
  }

}