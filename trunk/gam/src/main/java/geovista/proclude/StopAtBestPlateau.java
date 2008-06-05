package geovista.proclude;

import java.util.Arrays;
import java.lang.reflect.Array;


/**
 * This class contains the stop at best plateau halting condition.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class StopAtBestPlateau extends StopType{

  private int maxStagnantGens;
  private int stagnantGens;   //will count how many generations have gone past without improvements in median fitness
  private double previousBest;

  /**
   * The constructor throws an illegal argument exception if the maximum number
   * of stagnant generations (i2) is greater than the maximum number of generations
   * (i1).
   */

  public StopAtBestPlateau(int i1, int i2) {
    if (i2 > i1){
      throw new IllegalArgumentException("Max stagnant generations cannot exceed maximum total generations.");
    }
    maxGens = i1;
    maxStagnantGens = i2;
    init();
  }

  public void init(){
    previousBest = -100;
    stagnantGens = 0;
  }

  /**
   * This method determines whether or not the genetic GAM should stop by checking
   * to see if the best gene in the population's fitness has not improved for the
   * past n generations where n is the maximum stagnant generations value given to
   * the constructor.
   */

  public boolean run(double[] fitnessArray, int generationCount){
    Arrays.sort(fitnessArray);
    double currentBest = fitnessArray[Array.getLength(fitnessArray) - 1];
    if (currentBest <= (previousBest + 0.01))
      stagnantGens = stagnantGens + 1;
    else
      stagnantGens = 0;
    previousBest = currentBest;
    return (generationCount >= maxGens) || (stagnantGens >= maxStagnantGens);
  }

  public String toString(){
    return GAMSettingsPanel.halts[0] + '\n' +
           GAMSettingsPanel.MAXGENS + ',' + maxGens + '\n' +
           GAMSettingsPanel.STAGNANT + ',' + maxStagnantGens;
  }

}