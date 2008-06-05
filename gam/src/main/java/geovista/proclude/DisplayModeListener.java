package geovista.proclude;

/**
 * The interface for display mode listeners.  The display mode events tell what
 * mouse mode the display panel is to use.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface DisplayModeListener extends EventListener{

  public void setDisplayMode(DisplayModeEvent dme);

}