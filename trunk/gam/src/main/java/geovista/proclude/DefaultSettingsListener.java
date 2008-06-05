package geovista.proclude;

/**
 * The interface for default settings listeners.  They listen for the default
 * settings events, which indicate that the user wants the GAM settings returned
 * to their defaults.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface DefaultSettingsListener extends EventListener{

  public void setToDefaults(DefaultSettingsEvent dse);

}