package geovista.proclude;

/**
 * Event Object for the save settings events.  Once again, it is a trivial
 * extension of EventObject
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;

public class SaveSettingsEvent extends EventObject{

  public SaveSettingsEvent(Object source) {
    super(source);
  }

}