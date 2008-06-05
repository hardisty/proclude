package geovista.proclude;

/**
 * Interface for the update GAM listeners.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface UpdateGAMListener extends EventListener{

  public void updateGAMLabels(UpdateGAMEvent uge);

}