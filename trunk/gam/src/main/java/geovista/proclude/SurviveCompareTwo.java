package geovista.proclude;

import java.util.Vector;
import java.util.Random;

/**
 * This class contains the compare two survival method.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class SurviveCompareTwo extends Survive {

  private double pctSurvive;
  private Random r;

  /**
   * The constructor throws an illegal argument exception if the percent survival
   * rate is less than zero or greater than one.
   */

  public SurviveCompareTwo(double d) {
    if ((d < 0) || (d > 1)){
      throw new IllegalArgumentException("Percent survival rate must not be less than zero or greater than one.");
    }
    pctSurvive = d;
    r = new Random();
  }

  /**
   * This method determines which genes survive into the next generation by choosing
   * two genes at random, comparing their fitnesses, and placing the better fit gene
   * into the next generation.
   */

  public Vector run(Vector population, double[] sortedFitnessArray){
    int populationSize = population.size();
    int max = (int) Math.floor(populationSize *  pctSurvive);
    Vector newGenes = new Vector();
    for (int k = 0; k < max; k++){
      Gene g1 = (Gene) population.get(r.nextInt(populationSize));
      Gene g2 = (Gene) population.get(r.nextInt(populationSize));
      if (g1.getFitness() >= g2.getFitness())
        newGenes.add(g1);
      else
        newGenes.add(g2);
    }
    return newGenes;
  }

  public String toString(){
  return GAMSettingsPanel.survives[0] + '\n' +
         GAMSettingsPanel.PCTSURV + ',' + pctSurvive;
  }

}