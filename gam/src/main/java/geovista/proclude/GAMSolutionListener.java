package geovista.proclude;

/**
 * Interface for GAM solution listeners
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface GAMSolutionListener extends EventListener {

  public void setSolution(GAMSolutionEvent gse);

}