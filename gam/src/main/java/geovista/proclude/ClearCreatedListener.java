package geovista.proclude;

/**
 * The listener interface for clear created events.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface ClearCreatedListener extends EventListener{

  /**
   * Informs the listener that the list of created ellipses should be cleared along
   * with any associated statistics or ellipses.
   */
  public void clearCreated(ClearCreatedEvent cce);

}