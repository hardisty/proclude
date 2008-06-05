package geovista.proclude;

/**
 * Interface for GAM Settings listeners.  The GAM Settings events are received
 * whenever the user clicks the Apply Settings button from the GAM Settings Panel.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface GAMSettingsListener extends EventListener {

  public void setGAMSettings (GAMSettingsEvent gse);

}