package geovista.proclude;

/**
 * The interface for the new data set listener.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventListener;

public interface NewDataSetListener extends EventListener{

  public void setDataSet(NewDataSetEvent ndse);

}