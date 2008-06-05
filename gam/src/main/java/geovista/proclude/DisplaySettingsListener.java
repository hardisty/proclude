package geovista.proclude;

/**
 * This interface listens for display settings events.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface DisplaySettingsListener extends EventListener{

  /**
   * This is how the listener retrieves the settings from the display settings event.
   */
  public void setSettings(DisplaySettingsEvent dse);

}


