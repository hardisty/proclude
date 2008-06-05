package geovista.proclude;

/**
 * This class contains the CrossMidPoint method of crossover.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class CrossMidpoint extends Crossover {

  public CrossMidpoint() {
  }

  /**
   * This type of crossover averages the parents' x and y values to determine the
   * children's locations.  One child has the average radii and orientation of the parents, and
   * the other child has the radii values and orientation of the parent with the better fitness.
   */
  public Gene[] children(Gene parentOne, Gene parentTwo){
    Gene[] c = new Gene[2];
    Gene childOne = new Gene((parentOne.getMajorAxisRadius() + parentTwo.getMajorAxisRadius()) / 2,
                             (parentOne.getMinorAxisRadius() + parentTwo.getMinorAxisRadius()) / 2,
                             (parentOne.getX() + parentTwo.getX()) / 2,
                             (parentOne.getY() + parentTwo.getY()) / 2,
                             calculateOrientation(parentOne.getOrientation(), parentTwo.getOrientation()),
                             parentOne.getFunction());
    Gene betterParent;
    if (parentOne.getFitness() > parentTwo.getFitness())
      betterParent = parentOne;
    else
      betterParent = parentTwo;
    Gene childTwo = new Gene(betterParent.getMajorAxisRadius(),
                             betterParent.getMinorAxisRadius(),
                             (parentOne.getX() + parentTwo.getX()) / 2,
                             (parentOne.getY() + parentTwo.getY()) / 2,
                             betterParent.getOrientation(),
                             parentOne.getFunction());
    c[0] = childOne;
    c[1] = childTwo;
    return c;
  }

    private double calculateOrientation(double p1o, double p2o){
      double ret;
      if (Math.abs(p2o - p1o) > 90){
          if (p2o > p1o){
              p2o = p2o - 180;
          } else {
              p1o = p1o - 180;
          }
      }
      ret = (p1o + p2o) / 2;
      if (ret < 0){
          ret = ret + 360;
      }
      return ret;
  }
  
  public String toString(){
    return GAMSettingsPanel.crossovers[3];
  }

}