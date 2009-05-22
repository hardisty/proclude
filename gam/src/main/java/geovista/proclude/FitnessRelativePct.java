package geovista.proclude;

import java.util.Vector;

/**
 * This class contains a fitness function based on the ratio of the expected count
 * and the actual count.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class FitnessRelativePct extends Fitness {

  public FitnessRelativePct(Vector v, double d) {
    dataSet = v;
    minPts = d;
  }

  /**
   * This function first calculates the expected target count based on the ellipse's
   * population.  Then it returns the ratio of the actual count/the expected count.
   */
  public double run(Gene g){
    int c = count(g);
    if (population == 0){
      return -1;
    } else if (c < minPts){
        return -0.5;
    } else {
      return c/totalExpected;
    }
  }

  public String toString(){
    return GAMSettingsPanel.fitnesses[6];
  }

}