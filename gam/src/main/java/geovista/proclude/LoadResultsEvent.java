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
import java.io.File;

public class LoadResultsEvent extends EventObject{

  String[][] data;

  public LoadResultsEvent(Object source, String[][] s) {
    super(source);
    data = s;
  }

  public String[][] getFile(){
    return data;
  }
}