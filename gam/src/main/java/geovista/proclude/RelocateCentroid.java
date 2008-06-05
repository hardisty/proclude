package geovista.proclude;

import java.awt.Shape;
import java.util.Vector;
import java.util.Iterator;
import java.awt.geom.Ellipse2D;

/**
 * This class contains the centroid containment method of determining whether or
 * not an ellipse impinges on the banned area.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class RelocateCentroid extends Relocation{

  public RelocateCentroid() {
  }

  /**
   * This method determines whether or not the gene encroaches too much on the
   * banned list.  The way this is determined is by whether or not the centroid
   * of the gene g is contained within any of the ellipses in the banned list.
   */

  public boolean move(Vector bannedList, Gene g){
    boolean value = false;
    Iterator it = bannedList.iterator();
    while (it.hasNext() && !value){
      Gene nextInList = (Gene) it.next();
      Shape s = nextInList.toShape();
      value = s.contains(g.getX(), g.getY());
    }
    return value;
  }

  /**
   * This method updates the banned list by adding the gene g to the banned list.
   * With this option, there is no way for a gene to be removed from the banned
   * list.
   */

  public Vector updateBannedList(Vector bannedList, Gene g){
    bannedList.add(g);
    return bannedList;
  }

  public String toString(){
    return GAMSettingsPanel.relocations[0];
  }

}