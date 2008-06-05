package geovista.proclude;

import java.util.Vector;

/**
 * This class contains a fitness function based on an optimal area given by the
 * user.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class FitnessOptimalArea extends Fitness {

  private double optimalArea;

  public FitnessOptimalArea(Vector v, double d) {
    dataSet = v;
    optimalArea = d;
  }

  /**
   * This function returns the density fitness with a penalty based on deviance
   * from a user-supplied optimum area.
   */
  public double run(Gene g){
    int count = count(g);
    double area = Math.PI * g.getMajorAxisRadius() * g.getMinorAxisRadius();
    return 10 * (count)/(Math.max(1, population)) - 0.2 * Math.abs(optimalArea - area);
  }

  public String toString(){
    return GAMSettingsPanel.fitnesses[3] + '\n' + GAMSettingsPanel.OPTIMUM + ',' + optimalArea;
  }

}
