package geovista.proclude;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.util.EventListener;

public interface RemoveResultsListener extends EventListener {

  public void removeResults(RemoveResultsEvent rre);

}