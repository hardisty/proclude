package geovista.proclude;

/**
 * This is the load settings event, which is yet another trivial extension of
 * EventObject.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;

public class LoadSettingsEvent extends EventObject{

  public LoadSettingsEvent(Object source){
    super(source);
  }

}