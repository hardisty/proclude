package geovista.proclude;

import java.util.Vector;

/**
 * This class contains the fitness function based wholly on point density
 *
 * @author Jamison Conley
 * @version 1.0
 */

public class FitnessDensity extends Fitness {

  public FitnessDensity(Vector v) {
    dataSet = v;
  }

  /**
   * Calculates the fitness of an ellipse based on the ratio of target cases to
   * the total population.
   */
  public double run(Gene g){
    return 10 * (count(g))/(Math.max(1, population));
  } 

  public String toString(){
    return GAMSettingsPanel.fitnesses[1];
  }
  
}