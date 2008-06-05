package geovista.proclude;

/**
 * This class contains the default settings event.  It's a trivial extension of
 * EventObject.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;

public class DefaultSettingsEvent extends EventObject{

  public DefaultSettingsEvent(Object source) {
    super(source);
  }

}