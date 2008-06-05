package geovista.proclude;

import java.util.Vector;

/**
 * This class contains a density-based fitness function that caps the fitness at
 * a user-specified value.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class FitnessMaxAtN extends Fitness {

  private double maxFitness;

  public FitnessMaxAtN(Vector v, double d) {
    dataSet = v;
    maxFitness = d;
  }

  /**
   * This function works identically to the density fitness function except that
   * it cannot return a value greater than the maximum fitness.
   */
  public double run(Gene g){
    int count = count(g);
    return Math.min(maxFitness, 10 * (count)/(Math.max(1, population)));
  }

  /**
   * returns the maximum fitness allowed by the function.
   */
  public double getMaxFitness(){
    return maxFitness;
  }

  public String toString(){
    return GAMSettingsPanel.fitnesses[2] + '\n' + GAMSettingsPanel.MAXFIT + ',' + maxFitness;
  }
  
}