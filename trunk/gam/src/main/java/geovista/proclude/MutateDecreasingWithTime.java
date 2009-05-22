package geovista.proclude;

import java.util.Random;

/**
 * This class contains a mutation method in which the potential size of the mutation
 * decreases with each mutation.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class MutateDecreasingWithTime extends Mutation{

  private double rateOfDecrease;    //AS IN THE PERCENT DECREASE PER MUTATION.  PREFERABLY VERY SMALL.
  private double mutationAmt;
  private int genCount;

  /**
   * Constructor throws an illegal argument exception if the rate of decrease
   * (second parameter) is less than zero or greater than one.
   */

  public MutateDecreasingWithTime(double d1, double d2) {
    if ((d2 < 0) || (d2 > 1)){
      throw new IllegalArgumentException("Rate of decrease must not be less than zero or greater than one.");
    }
    mutationAmt = d1;
    rateOfDecrease = d2;
    genCount = 0;
  }

  /**
   * Takes a gene and alters one of the four attributes (centroid X, centroid Y,
   * horizontal and vertical radii) by an amount less than or equal to the maximum
   * amount.
   *
   * This maximum amount decreases slightly after each mutation.  This is to
   * reasonably approximate simulated annealing.  The formula for calculating
   * the next mutation's maximum possible size is:
   *      Next mutation amount = Current mutation amount * (1 - rate of decrease)
   *
   * Therefore, if the rate of decrease is 0.01, the maximum size of each mutation
   * decreases by 1% for each subsequent mutation.
   */

  public Gene run(Gene g){
    Random r = new Random();
    double newValue;
    switch(r.nextInt(5)){
      case 0:
      {
        if (r.nextDouble() < 0.5)
          newValue = g.getMajorAxisRadius() + r.nextDouble() * mutationAmt;
        else
          newValue = Math.max(mutationAmt/5, g.getMajorAxisRadius() - r.nextDouble() * mutationAmt);
        g.setMajorAxisRadius(newValue);
      }
        break;
      case 1:
      {
        if (r.nextDouble() < 0.5)
          newValue = g.getMinorAxisRadius() + r.nextDouble() * mutationAmt;
        else
          newValue = Math.max(mutationAmt/5, g.getMinorAxisRadius() - r.nextDouble() * mutationAmt);
        g.setMinorAxisRadius(newValue);
      }
        break;
      case 2:
      {
        if (r.nextDouble() < 0.5)
          newValue = g.getX() + r.nextDouble() * mutationAmt;
        else
          newValue = g.getX() - r.nextDouble() * mutationAmt;
        g.setX(newValue);
      }
        break;
      case 3:
      {
        if (r.nextDouble() < 0.5)
          newValue = g.getY() + r.nextDouble() * mutationAmt;
        else
          newValue = g.getY() - r.nextDouble() * mutationAmt;
        g.setY(newValue);
      }
        break;
      case 4:
      {
        if (r.nextDouble() < 0.5)
          newValue = g.getOrientation() + r.nextDouble() * mutationAmt;
        else
          newValue = g.getOrientation() - r.nextDouble() * mutationAmt;
        if (newValue < 0){
            newValue = newValue + 360;
        }
        if (newValue > 360){
            newValue = newValue - 360;
        }
        g.setOrientation(newValue);
      }
        break;        
      default:
        ;
    }
    mutationAmt = mutationAmt * (1 - rateOfDecrease);
    if (g.getMajorAxisRadius() < g.getMinorAxisRadius()){
        double tempMajor = g.getMinorAxisRadius();
        double tempMinor = g.getMajorAxisRadius();
        double tempOrient = g.getOrientation() - 90;
        if (tempOrient > 0){
            tempOrient = tempOrient + 180;
        }
        g.setMajorAxisRadius(tempMajor);
        g.setMinorAxisRadius(tempMinor);
        g.setOrientation(tempOrient);
    }
    return g;
  }

  public String toString(){
    return GAMSettingsPanel.mutations[2] + '\n' +
           GAMSettingsPanel.MUTSIZE + ',' + mutationAmt + '\n' +
           GAMSettingsPanel.DECREASE + ',' + rateOfDecrease;
  }

}
