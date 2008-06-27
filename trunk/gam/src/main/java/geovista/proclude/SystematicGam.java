package geovista.proclude;

import java.util.Vector;

/**
 * This class contains the Systematic GAM.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class SystematicGam extends AbstractGam{


  public SystematicGam(){
  }

  /**
   * This method runs the systematic GAM.
   *
   * The basic algorithm for the systematic GAM is:
   * For x = minimum x to maximum x
   *     For y = minimum y to maximum y
   *         For horizontal radius = 1 to 7
   *             For vertical radius = 1 to 7
   *                 Find the fitness of the gene specified by x, y, and the two radii
   *                 If that fitness is high enough and it has enough points, add its ellipse to the list
   *                 Otherwise, discard it.
   */

  public Vector run(){
//    System.out.println("Running Systematic GAM.");
    
    Vector clusters = new Vector();    
    double xScale = (initializer.getMaxX() - initializer.getMinX())/100d;
    double yScale = (initializer.getMaxY() - initializer.getMinY())/100d;
    double xVal, yVal;
    for (int x = 0; x < 100; x++){
      System.out.println("X: " + x);
      xVal = initializer.getMinX() + (x * xScale);
      for (int y = 0; y < 100; y++){
        yVal = initializer.getMinY() + (y * yScale);
        for (int r1 = 1; r1 < (double) maxRadius/minRadius; r1++){
//          for (int r2 = 1; r2 < maxRadius; r2++){
//            System.out.println("radius is " + r1 * minRadius);
            Gene hypothesis = new Gene(r1 * minRadius, r1 * minRadius, xVal, yVal, 0, fitnessFunction);
            double fitness = hypothesis.getFitness();
            if ((fitness >= minAccepted) && (hypothesis.getCount() >= minPoints)){
              clusters.add(hypothesis);
            }
//          }
        }
      }
    }
    return clusters;
  }

}

