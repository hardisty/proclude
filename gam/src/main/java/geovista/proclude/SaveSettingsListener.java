package geovista.proclude;

/**
 * Interface for the save settings listeners.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface SaveSettingsListener extends EventListener{

  public void saveSettings(SaveSettingsEvent sse);

}