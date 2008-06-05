package geovista.proclude;

import java.util.Vector;

/**
 * This class contains the elite n survival method.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class SurviveEliteN extends Survive {

  private double pctSurvive;

  /**
   * The constructor throws an illegal argument exception if the percent survival
   * rate is less than zero or greater than one.
   */

  public SurviveEliteN(double d) {
    if ((d < 0) || (d > 1)){
      throw new IllegalArgumentException("Percent survival rate must not be less than zero or greater than one.");
    }
    pctSurvive = d;
  }

  /**
   * This method determines takes the top n percent of the population and places
   * it in the next generation where n is the percent survival rate given to the
   * constructor.
   */

  public Vector run(Vector population, double[] sortedFitnessArray){
    int populationSize = population.size();
    double fitLevel = sortedFitnessArray[populationSize - Math.max(1, (int) Math.floor(populationSize *  pctSurvive))];
    Vector newGenes = new Vector();
    for (int k = 0; k < populationSize; k++){
      Gene hypothesis = (Gene) population.get(k);
      if (hypothesis.getFitness() >= fitLevel){
        newGenes.add(hypothesis);
      }
    }
    return newGenes;
  }

  public String toString(){
    return GAMSettingsPanel.survives[1] + '\n' +
           GAMSettingsPanel.PCTSURV + ',' + pctSurvive;
  }

}