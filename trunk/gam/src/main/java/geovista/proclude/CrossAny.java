package geovista.proclude;

/**
 * This class contains the Cross Any method of crossover.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.Random;

public class CrossAny extends Crossover{

  private Random r;

  public CrossAny() {
    r = new Random();
  }

  /**
   * This type of crossover takes the two parents and randomly assigns each of their
   * genes to the two children as such:
   *
   * For each value contained by the gene (major, minor, x, y, o), one child gets the
   * value from parentOne and the other gets the value from parentTwo.
   * There is no relationship between which child gets which parent's values,
   * and all of the genetic material contained in the parents is in the children.
   */
  public Gene[] children(Gene parentOne, Gene parentTwo){
    Gene[] c = new Gene[2];
    double firstMajor, firstMinor, firstX, firstY, firstOrient, secondMajor, secondMinor, secondX, secondY, secondOrient;
    if (r.nextDouble() < 0.5){
      firstMajor = parentOne.getMajorAxisRadius();
      secondMajor = parentTwo.getMajorAxisRadius();
    } else {
      firstMajor = parentTwo.getMajorAxisRadius();
      secondMajor = parentOne.getMajorAxisRadius();
    }
    if (r.nextDouble() < 0.5){
      firstMinor = parentOne.getMinorAxisRadius();
      secondMinor = parentTwo.getMinorAxisRadius();
    } else {
      firstMinor = parentTwo.getMinorAxisRadius();
      secondMinor = parentOne.getMinorAxisRadius();
    }
    if (r.nextDouble() < 0.5){
      firstX = parentOne.getX();
      secondX = parentTwo.getX();
    } else {
      firstX = parentTwo.getX();
      secondX = parentOne.getX();
    }
    if (r.nextDouble() < 0.5){
      firstY = parentOne.getY();
      secondY = parentTwo.getY();
    } else {
      firstY = parentTwo.getY();
      secondY = parentOne.getY();
    }
    if (r.nextDouble() < 0.5){
      firstOrient = parentOne.getOrientation();
      secondOrient = parentTwo.getOrientation();
    } else {
      firstOrient = parentTwo.getOrientation();
      secondOrient = parentOne.getOrientation();
    }    
    c[0] = new Gene(firstMajor, firstMinor, firstX, firstY, firstOrient, parentOne.getFunction());
    c[1] = new Gene(secondMajor, secondMinor, secondX, secondY, secondOrient, parentOne.getFunction());
    return c;
  }

  public String toString(){
    return GAMSettingsPanel.crossovers[0];
  }

}