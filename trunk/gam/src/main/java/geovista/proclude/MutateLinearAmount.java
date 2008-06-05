package geovista.proclude;

import java.util.Random;

/**
 * This class contains a simple mutation method that changes the value of one of
 * the gene attributes by no more than a specified value.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class MutateLinearAmount extends Mutation{

  private double mutationAmt;

  public MutateLinearAmount(double d) {
    mutationAmt = d;
  }

  /**
   * Takes a gene and changes one of the four attributes (X, Y, horizontal radius
   * and vertical radius) by an amount not exceeding the maximum fitness size.
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
          newValue = Math.max(1, g.getMajorAxisRadius() - r.nextDouble() * mutationAmt);
        g.setMajorAxisRadius(newValue);
      }
        break;
      case 1:
      {
        if (r.nextDouble() < 0.5)
          newValue = g.getMinorAxisRadius() + r.nextDouble() * mutationAmt;
        else
          newValue = Math.max(1, g.getMinorAxisRadius() - r.nextDouble() * mutationAmt);
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
    return GAMSettingsPanel.mutations[1] + '\n' +
           GAMSettingsPanel.MUTSIZE + ',' + mutationAmt;
  }

}
