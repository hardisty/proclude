package geovista.proclude;

/**
 * This is the display mode event.  It tells the display panel which mode to use
 * for mouse clicks: draw and move created ellipses or select GAM ellipses.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;

public class DisplayModeEvent extends EventObject{

  private int mode;

  public DisplayModeEvent(Object source, int i) {
    super(source);
    mode = i;
  }

  public int getMode(){
    return mode;
  }
}