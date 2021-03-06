package geovista.proclude;

import java.awt.Shape;
/**
 * Abstract superclass for fitness functions.
 *
 * Currently known subtypes are: FitnessAddRadii, FitnessDensity, FitnessMaxAtN,
 * FitnessOptimalArea, FitnessOptimalRadius, FitnessRelative, and FitnessRelativePct.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import java.util.Vector;

public abstract class Fitness {

  Vector dataSet = new Vector();
  protected int[] containedPoints;
  protected boolean useContainedPoints = true;
  int population;
  double totalExpected, minPts;

  public Fitness() {
  }

  /**
   * return the fitness of an ellipse given the gene.
   */
  public abstract double run(Gene g);

  /**
   * Return the number of target points contained by the ellipse represented by
   * centroid x and y and horizontal and vertical radii.
   */
  public int count(Gene g){
    Shape s = g.toShape();
    population = 0; 
    totalExpected = 0;
    int target = 0;
    int counter = 0;
    int size = 20;
    if (useContainedPoints){
        containedPoints = new int[size];
        for (int k = 0; k < size; k++){
          containedPoints[k] = -1;
        }
    }
    for (int index = 0; index < dataSet.size(); index++){
      DataPoint nextInList = (DataPoint) dataSet.get(index);
      if (s.contains(nextInList.getLocation())){
          if (useContainedPoints){
            if (counter == containedPoints.length){
              doubleArraySize();
            }
            containedPoints[counter] = index;
          }
        counter++;
        population = population + nextInList.getPopulation();
        totalExpected = totalExpected + nextInList.getExpected();
        target = target + nextInList.getTarget();
      }
    }
    return target;
  }

  /**
   * Returns the population of the gene.
   */

  public int getPopulation(){
    return population;
  }

  public double getTotalExpected(){
      return totalExpected;
  }
  
  private void doubleArraySize(){
    int arraySize = containedPoints.length;
    int[] newArray = new int[arraySize * 2];
    for (int index = 0; index < (arraySize); index++){
      newArray[index] = containedPoints[index];
    }
    for (int q = arraySize; q < arraySize * 2; q++){
      newArray[q] = -1;
    }
    containedPoints = newArray;
  }

  /**
   * Return a list of all data points contained by the ellipse.
   */
  public int[] getContainedPoints(){
    if (useContainedPoints){
      //this is for trimming the array down to size.
      int n = 0;
      for (int i = 0; i < containedPoints.length; i++){
          if (containedPoints[i] != -1){
              n++;
          }
      }
      int[] ret = new int[n];
      for (int j = 0; j < n; j++){
          ret[j] = containedPoints[j];
      }
      return ret;
    } else {
      return new int[]{0};
    }
  }

  /**
   * Gives the fitness function the current data set.
   */

  public void setDataSet(Vector v){
    dataSet = v;
  }

}