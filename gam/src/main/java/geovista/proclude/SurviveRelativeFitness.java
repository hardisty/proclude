package geovista.proclude;

import java.util.Vector;
import java.util.Random;

/**
 * This class contains the relative fitness survival method.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class SurviveRelativeFitness extends Survive {

  private double pctSurvive;
  private Random r;

  /**
   * The constructor throws an illegal argument exception if the percent survival
   * rate is less than zero or greater than one.
   */

  public SurviveRelativeFitness(double d) {
    if ((d < 0) || (d > 1)){
      throw new IllegalArgumentException("Percent survival rate must not be less than zero or greater than one.");
    }
    pctSurvive = d;
    r = new Random();
  }

  /**
   * This method determines which genes survive into the next generation by comparing
   * their fitness with the total fitness of the population.  The probability that
   * a gene will survive into the next generation is proportional to the ratio of
   * its fitness to the total population's fitness.
   */

  public Vector run(Vector population, double[] sortedFitnessArray){
    int populationSize = population.size();
    int max = (int) Math.floor(populationSize * pctSurvive);
    Vector newGenes = new Vector();
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
    while (k < max){
      Gene g1 = (Gene) population.get(r.nextInt(populationSize));
      double offsetFitness = g1.getFitness() + offset;
      if (((offsetFitness * offsetFitness)/totalSquaredFitness) > (r.nextDouble() / x)){
        //This is divided by 4 to speed up the run--essentially reduce the range of r.nextDouble from 0-1 to 0-1/x.
        //Since offsetFitness^2 will likely not be > totalSquaredFitness/x, this shouldn't affect performance.
        newGenes.add(g1);
        k++;
      }
    }
    return newGenes;
  }

  public String toString(){
  return GAMSettingsPanel.survives[2] + '\n' +
         GAMSettingsPanel.PCTSURV + ',' + pctSurvive;
  }

}