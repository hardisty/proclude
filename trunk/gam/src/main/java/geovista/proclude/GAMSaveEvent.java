package geovista.proclude;

/**
 * The GAMSaveEvent class, which is a trivial extension of EventObject.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;

public class GAMSaveEvent extends EventObject {

  public GAMSaveEvent(Object source) {
    super(source);
  }

}