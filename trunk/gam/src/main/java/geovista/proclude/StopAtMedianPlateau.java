package geovista.proclude;

import java.util.Arrays;
import java.lang.reflect.Array;


/**
 * This class contains the halting condition that checks to see of the median fitness
 * has not improved.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class StopAtMedianPlateau extends StopType{

  private int maxStagnantGens;
  private int stagnantGens;
  private double previousMedian;

  /**
   * The constructor throws an illegal argument exception if the maximum number
   * of stagnant generations (i2) is greater than the maximum number of generations
   * (i1).
   */

  public StopAtMedianPlateau(int i1, int i2) {
    if (i2 > i1){
      throw new IllegalArgumentException("Max stagnant generations cannot exceed maximum total generations.");
    }
    maxGens = i1;
    maxStagnantGens = i2;
    init();
  }

  public void init(){
    previousMedian = -100;
    stagnantGens = 0;
  }

  /**
   * This method determines whether or not the genetic GAM should stop by checking
   * to see if the median fitness of the population has not improved for the
   * past n generations where n is the maximum stagnant generations value given to
   * the constructor.
   */

  public boolean run(double[] fitnessArray, int generationCount){
    Arrays.sort(fitnessArray);
    double currentMedian = fitnessArray[Array.getLength(fitnessArray)/2];
    if (currentMedian <= (previousMedian + 0.01))
      stagnantGens = stagnantGens + 1;
    else
      stagnantGens = 0;
    previousMedian = currentMedian;
    return (generationCount >= maxGens) || (stagnantGens >= maxStagnantGens);
  }

  public String toString(){
    return GAMSettingsPanel.halts[1] + '\n' +
           GAMSettingsPanel.MAXGENS + ',' + maxGens + '\n' +
           GAMSettingsPanel.STAGNANT + ',' + maxStagnantGens;
  }

}