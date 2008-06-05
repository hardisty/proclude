package geovista.proclude;

/**
 * The interface for GAM save listeners.  The GAM save event indicates that the
 * user has requested that the GAM solution be saved out to a file.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface GAMSaveListener extends EventListener {

  public void saveResults(GAMSaveEvent gse);

}