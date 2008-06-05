package geovista.proclude;

import java.util.Vector;

/**
 * This class contains the random GAM.  The random GAM works by generating random
 * ellipses, and testing their fitness.  If an ellipse's fitness is above a specified
 * value, it keeps that ellipse in the solution list, otherwise, it discards it.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class RandomGam extends AbstractGam{

  private double averageFitness;
  private int numTests;

  public RandomGam(){
    averageFitness = 0;
  }

  /**
   * Sets the number of ellipses that the random GAM will test.  This is found in
   * the program by multiplying the maximum number of generations by the population
   * size.
   *
   * This throws an error if the number of tests is less than one.
   */

  public void setNumTests(int i){
    if (i <= 0){
      throw new ArithmeticException("Number of random GAM tests must be at least 1.");
    }

    numTests = i;
  }

  /**
   * Returns the average fitness of all ellipses that it tested.
   */

  public double getFitness(){
    return averageFitness;
  }

  /**
   * Runs the random GAM, returning a vector of genes.  The basic algorithm is:
   * For q = 1 to number of tests,
   *     Create a random gene.
   *     Get its fitness.
   *     If that fitness is above a threshold, keep it.
   *     Otherwise, discard it.
   */

  public Vector run(){
//    System.out.println("Minimum radius is: " + minRadius);
//    System.out.println("Maximum radius is: " + maxRadius);
    Vector clusters = new Vector();
    double fitnessSum = 0;
    for (int q = 0; q < numTests; q++){
      Gene hypothesis = initializer.createRandomGene(fitnessFunction, minRadius, maxRadius);
      //This turns it into a circle.
      if (Math.random() > 0.5){ 
        hypothesis.setMajorAxisRadius(hypothesis.getMinorAxisRadius());
      } else {          
        hypothesis.setMinorAxisRadius(hypothesis.getMajorAxisRadius());
      }
//      System.out.println(hypothesis);
      double fitness = hypothesis.getFitness();
      fitnessSum = fitnessSum + fitness;
      if ((hypothesis.getCount() >= minPoints) && (fitness >= minAccepted)){
        clusters.add(hypothesis);
      }
    }
    averageFitness = fitnessSum/numTests;
    return clusters;
  }

}

