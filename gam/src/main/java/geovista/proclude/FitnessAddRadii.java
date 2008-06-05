package geovista.proclude;

import java.util.Vector;

/**
 * This class contains the optimal sum of radii fitness function.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class FitnessAddRadii extends Fitness {

  private double optimalSum;

  public FitnessAddRadii(Vector v, double d) {
    dataSet = v;
    optimalSum = d;
  }

  /**
   * This fitness function calculates the density of target cases within the ellipse
   * and subtracts a penalty based on how far away the sum of the radii is from
   * the user-defined optimal radii sum.
   */
  
  public double run(Gene g) {
      int c = count(g);
      return 10 * c/(Math.max(1, population)) - 0.2 * Math.abs(optimalSum - g.getMajorAxisRadius() - g.getMinorAxisRadius());
  }
 
  public String toString(){
    return GAMSettingsPanel.fitnesses[0] + '\n' + GAMSettingsPanel.OPTIMUM + ',' + optimalSum;
  }
  
}