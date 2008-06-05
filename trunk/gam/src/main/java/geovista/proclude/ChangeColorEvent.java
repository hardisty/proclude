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
import java.awt.Color;

public class ChangeColorEvent extends EventObject{

  Color color;
  int index;

  public ChangeColorEvent(Object source, Color c, int i){
    super(source);
    color = c;
    index = i;
  }

  public Color getColor(){
    return color;
  }

  public int getIndex(){
    return index;
  }
}