package geovista.proclude;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.util.EventObject;

public class RemoveResultsEvent extends EventObject{

  private int index;

  public RemoveResultsEvent(Object source, int i) {
    super(source);
    index = i;
  }

  public int getIndex(){
    return index;
  }
}