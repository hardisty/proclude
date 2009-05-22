package geovista.proclude;

import java.util.Vector;

/**
 * This class contains a fitness function based on the number of points greater
 * than the expected number of points.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class FitnessRelative extends Fitness {

  public FitnessRelative(Vector v, double d) {
    dataSet = v;
    minPts = d;
  }

  /**
   * This function first computes the expected number of target case based on the
   * population of the ellipse.  Then it returns the difference between the expected count
   * and the actual count.
   */
  public double run(Gene g){
    int c = count(g);
    if (c < minPts){
        return -0.5;
    } else {
        return (c - totalExpected);
    }
  }

  public String toString(){
    return GAMSettingsPanel.fitnesses[5];
  }
  
}