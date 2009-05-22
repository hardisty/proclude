package geovista.proclude;

import java.util.Vector;

/**
 * This class contains a fitness function based on density with an optimal radius.
 *
 * @author unascribed
 * @version 2.0
 */

public class FitnessOptimalRadius extends Fitness {

  private double optimalRadius;

  public FitnessOptimalRadius(Vector v, double d, double d2) {
    dataSet = v;
    optimalRadius = d;
    minPts = d2;
  }

  /**
   * This function returns a density based fitness with a penalty based on deviance
   * from the optimal radius size.
   */
  public double run(Gene g){
    int count = count(g);
    if (count < minPts){
        return -0.5;
    } else {
        return 10 * (count)/(Math.max(1, population)) - 0.2 * (Math.max(1, Math.abs(optimalRadius - g.getMajorAxisRadius())) * Math.max(1, Math.abs(optimalRadius - g.getMinorAxisRadius())));
        //I used Math.max(1, |optimalRadius - major|) because without the max statement, if major = optimalRadius and the other is, say, 20, since
        //optimalRadius - major = 0, and thus the penalty for minor = 20 is negated since 0 * anything = 0.  Thus, even a
        //radius of optimalRadius incurs a small penalty.
    }
  }

  public String toString(){
    return GAMSettingsPanel.fitnesses[4] + '\n' + GAMSettingsPanel.OPTIMUM + ',' + optimalRadius;
  }

}