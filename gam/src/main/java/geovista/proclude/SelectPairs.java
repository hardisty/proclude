package geovista.proclude;

import java.util.Vector;
import java.util.Random;

/**
 * This class contains the method of selecting pairs of parents based on how close
 * they are to each other; pairs of parents that are closer to each other are more
 * likely to be chosen than pairs of parents that are not near each other.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class SelectPairs extends Selection{

  private double pctSurvive;
  private Random r;

  /**
   * The constructor throws an illegal argument exception if the percent in the
   * elite is less than or equal to zero or greater than one.
   */

  public SelectPairs(double d) {
    if ((d <= 0) || (d > 1)){
      throw new IllegalArgumentException("Percent in the elite must be greater than zero and less than or equal to one.");
    }
    pctSurvive = d;
    r = new Random();
  }

  private boolean CloseEnough(Gene p1, Gene p2){
      double dist = Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) +
                              (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
      double prob = 1/Math.sqrt(dist);
      return (prob > r.nextDouble());
  }

  /**
   * This method selects two parents that are both in the elite and favors the
   * selection of pairs that are near each other, although pairs that are far
   * from each other are still possible.
   *
   * Since the same effect can be achieved through setting the selection method
   * to 'select random elite' and setting 'select pairs' to 'true', this class is
   * redundant, but I haven't bothered to remove it.
   */

  public Gene[] run(Vector population, double[] sortedFitnessArray){
    int populationSize = population.size();
    double fitLevel = sortedFitnessArray[populationSize - (int) Math.floor(populationSize *  pctSurvive)];

    //THERE HAS TO BE A QUICKER WAY TO ACHIEVE THE SAME ENDS!!!!!  TWO RANDOMLY CHOSEN
    //GENES ABOVE THE fitLevel THRESHOLD.

    Gene[] parents = new Gene[2];
    do {

      do {
        int firstParent = r.nextInt(populationSize);
        parents[0] = (Gene) population.get(firstParent);
      } while (parents[0].getFitness() < fitLevel);
      do {
        int secondParent = r.nextInt(populationSize);
        parents[1] = (Gene) population.get(secondParent);
      } while (parents[1].getFitness() < fitLevel);

    } while (! CloseEnough(parents[0], parents[1]));
    return parents;
  }

  public String toString(){
    return GAMSettingsPanel.selections[0] + '\n' + GAMSettingsPanel.ELITE + ',' + pctSurvive;
  }

}

