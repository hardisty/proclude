package geovista.proclude;

import java.util.Vector;
import java.util.Random;

/**
 * This class contains the random elite method of selecting parents.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class SelectRandomElite extends Selection{

  private double pctSurvive;
  private Random r;

  /**
   * The constructor throws an illegal argument exception if the percent in the
   * elite is less than or equal to zero or greater than one.
   */

  public SelectRandomElite(double d) {
    if ((d <= 0) || (d > 1)){
      throw new IllegalArgumentException("Percent in the elite must be greater than zero and less than or equal to one.");
    }
    pctSurvive = d;
    r = new Random();
  }

  /**
   * This method returns two randomly selected genes from the elite, where the
   * elite is the top x% where x is the argument to the constructor.
   */

  public Gene[] run(Vector population, double[] sortedFitnessArray){
    int populationSize = population.size();
    double fitLevel = sortedFitnessArray[populationSize - Math.max(1, (int) Math.floor(populationSize *  pctSurvive))];
    Gene[] parents = new Gene[2];

    do {
      int firstParent = r.nextInt(populationSize);
        parents[0] = (Gene) population.get(firstParent);
    } while (parents[0].getFitness() < fitLevel);
    do {
      int secondParent = r.nextInt(populationSize);
      parents[1] = (Gene) population.get(secondParent);
    } while (parents[1].getFitness() < fitLevel);

    return parents;
  }

  public String toString(){
    return GAMSettingsPanel.selections[2] + '\n' + GAMSettingsPanel.ELITE + ',' + pctSurvive;
  }

}