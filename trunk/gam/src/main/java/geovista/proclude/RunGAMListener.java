package geovista.proclude;

/**
 * The interface for the run GAM listeners.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface RunGAMListener extends EventListener{

  public void runGAM(RunGAMEvent rge);

}