package geovista.proclude;

/**
 * The listener for load settings events.  It indicates that the user has
 * pre-saved settings to be loaded.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface LoadSettingsListener extends EventListener{

  public void loadSettings(LoadSettingsEvent lse);

}