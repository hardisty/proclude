package geovista.proclude;

/**
 * This class contains the clear created event.  This is a trivial extension of
 * EventObject.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;

public class ClearCreatedEvent extends EventObject{

  public ClearCreatedEvent(Object source){
    super(source);
  }
}