package geovista.proclude;

import java.util.Vector;

/**
 * Abstract superclass for relocation methods.
 *
 * Currently known subclasses are RelocateCentroid and RelocateDifference
 *
 * @author Jamison Conley
 * @version 2.0
 */

public abstract class Relocation {

  public Relocation() {
  }

  /**
   * Determines whether or not g must be moved based on the contents of the banned
   * list.
   */

  public abstract boolean move(Vector bannedList, Gene g);

  /**
   * Updates the banned list by incorporating g into the banned areas.
   */

  public abstract Vector updateBannedList(Vector bannedList, Gene g);

}