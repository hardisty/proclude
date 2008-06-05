package geovista.proclude;

/**
 * This class contains the halting condition that stops after a specified number
 * of generations.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class StopAtNGens extends StopType {

  public StopAtNGens(int i) {
    maxGens = i;
  }

  /**
   * This method does absolutely nothing, but is required by the StopType superclass.
   */

  public void init(){}

  /**
   * This method determines whether or not the genetic GAM should stop based on
   * how many generations have passed.  It does not take either the best or median
   * fitness into account.
   */

  public boolean run(double[] fitnessArray, int generationCount){
    return (generationCount >= maxGens);
  }

  public String toString(){
    return GAMSettingsPanel.halts[2] + '\n' +
           GAMSettingsPanel.MAXGENS + ',' + maxGens;
  }

}