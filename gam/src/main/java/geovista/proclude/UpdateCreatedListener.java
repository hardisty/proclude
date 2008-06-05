package geovista.proclude;

/**
 * Interface for the update created listener.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface UpdateCreatedListener extends EventListener{

  public void updateCreatedLabels(UpdateCreatedEvent uce);

}