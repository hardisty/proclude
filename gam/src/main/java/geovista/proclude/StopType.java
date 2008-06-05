package geovista.proclude;

/**
 * The abstract supertype for halting conditions.
 *
 * Known subtypes are StopAtBestPlateau, StopAtMedianPlateau, and StopAtNGens
 *
 * @author Jamison Conley
 * @version 2.0
 */

public abstract class StopType {

int maxGens;

  public StopType() {
  }

  /**
   * Returns the maximum generation count.  This is used in error checking to
   * determine if the first addition to the banned/solution lists happen after
   * the maximum number of generations has expired.
   */

  public int getMaxGens(){
    return maxGens;
  }

  /**
   * This method determines whether or not the genetic GAM should halt.
   */

  public abstract boolean run(double[] fitnessArray, int generationCount);

  public abstract void init();

}