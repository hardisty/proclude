package geovista.proclude;

import java.util.Random;
import java.util.Vector;

/**
 * This class contains a probabilistic method of selecting pairs of genes to be
 * parents.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class SelectProbabilistic extends Selection {

  private Random r;

  public SelectProbabilistic() {
    r = new Random();
  }

  /**
   * This method selects genes to be parents based on their fitness relative to
   * the rest of the population.  The total fitness of the population is computed,
   * and the probability of a gene being selected as a parent is proportional to
   * the ratio of its fitness to the population's total fitness.
   */

  public Gene[] run(Vector population, double[] sortedFitnessArray){
    Gene[] parents = new Gene[2];
    int populationSize = population.size();
    double totalSquaredFitness = 0;
    double offset = Math.max(0, 0 - sortedFitnessArray[0]);
    //The offset is because negative fitnesses are allowed, and negative fitnesses would screw up the
    //calculations.  Therefore, set the lowest fitness to be 0 if it is negative and increase all others
    //accordingly.
    //This could, if the lowest is -60 or something ridiculous like that, make there be not much difference
    //between, say, 1 and 5, which is now 61 and 65.  Therefore, I chose to square the fitnesses for the
    //purposes of the calculations to more easily differentiate between high fitness levels.
    for (int x = 0; x < populationSize; x++){
      totalSquaredFitness = totalSquaredFitness +
                            ((sortedFitnessArray[x] + offset) * (sortedFitnessArray[x] + offset));
    }
    int k = 0;
    int x = (int) Math.floor(populationSize / 5);
    while (k < 2){
      Gene g1 = (Gene) population.get(r.nextInt(populationSize));
      double offsetFitness = (g1.getFitness() + offset) * (g1.getFitness() + offset);
      if ((offsetFitness/totalSquaredFitness) > (r.nextDouble() / x)){
        //This is divided by x to speed up the run--essentially reduce the range of r.nextDouble from 0-1 to 0-1/x.
        //Since offsetFitness will likely not be > totalSquaredFitness/x, this shouldn't affect performance.
        parents[k] = g1;
        k++;
      }
    }
    return parents;
  }

  public String toString(){
    return GAMSettingsPanel.selections[1];
  }

}