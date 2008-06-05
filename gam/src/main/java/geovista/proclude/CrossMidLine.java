package geovista.proclude;

/**
 * This class contains the CrossMidLine method of crossover.  It is the currently
 * set default method.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class CrossMidLine extends Crossover {

  public CrossMidLine() {
  }

  /**
   * This method of crossover generates a random number between 0 and 1 for each
   * child, and scales the children between the parents as such:
   *
   * For each value (major, minor, x, y, o), the child's value is partway between the parents'
   * values, and the random number(alpha) for that child determines how close it is to
   * either parent.  The formula is:
   *
   * childValue = parentOneValue + alpha * (parentTwoValue - parentOneValue);
   */
  public Gene[] children(Gene parentOne, Gene parentTwo){
    Gene[] c = new Gene[2];
    double d1 = Math.random();
    double d2 = Math.random();

    //DO I REALLY WANT TO CHANGE THE RADII AS WELL AS THE LOCATION?

    Gene childOne = new Gene(parentOne.getMajorAxisRadius() + (parentTwo.getMajorAxisRadius() - parentOne.getMajorAxisRadius()) * d1,
                             parentOne.getMinorAxisRadius() + (parentTwo.getMinorAxisRadius() - parentOne.getMinorAxisRadius()) * d1,
                             parentOne.getX() + (parentTwo.getX() - parentOne.getX()) * d1,
                             parentOne.getY() + (parentTwo.getY() - parentOne.getY()) * d1,
                             calculateOrientation(parentOne.getOrientation(), parentTwo.getOrientation(), d1), 
                             parentOne.getFunction());
    Gene childTwo = new Gene(parentOne.getMajorAxisRadius() + (parentTwo.getMajorAxisRadius() - parentOne.getMajorAxisRadius()) * d2,
                             parentOne.getMinorAxisRadius() + (parentTwo.getMinorAxisRadius() - parentOne.getMinorAxisRadius()) * d2,
                             parentOne.getX() + (parentTwo.getX() - parentOne.getX()) * d2,
                             parentOne.getY() + (parentTwo.getY() - parentOne.getY()) * d2,
                             calculateOrientation(parentOne.getOrientation(), parentTwo.getOrientation(), d2),
                             parentOne.getFunction());
    c[0] = childOne;
    c[1] = childTwo;
    return c;
  }

  private double calculateOrientation(double p1o, double p2o, double d){
      double ret;
      if (Math.abs(p2o - p1o) > 90){
          if (p2o > p1o){
              p2o = p2o - 180;
          } else {
              p1o = p1o - 180;
          }
      }
      ret = p1o + (p2o - p1o) * d;
      if (ret < 0){
          ret = ret + 360;
      }
      return ret;
  }
  
  public String toString(){
    return GAMSettingsPanel.crossovers[2];
  }

}