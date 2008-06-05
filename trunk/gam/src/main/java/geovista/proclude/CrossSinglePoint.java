package geovista.proclude;

/**
 * This class contains the CrossSinglePoint method of crossover.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.Random;

public class CrossSinglePoint extends Crossover {

  private Random r;

  public CrossSinglePoint() {
    r = new Random();
  }

  /**
   * This type of crossover works in the following way:
   *
   * Assume the two parents are R1 R2 X Y and r1 r2 x y.
   *
   * Child one will be R1 r2 x y, R1 R2 x y, or R1 R2 X y; and
   * child two will be r1 R2 X Y, r1 r2 X Y, or r1 r2 x Y respectively
   */
  public Gene[] children(Gene parentOne, Gene parentTwo){
    Gene[] c = new Gene[2];

    switch(r.nextInt(4)){
      case 0:{
        c[0] = new Gene(parentOne.getMajorAxisRadius(),
                        parentTwo.getMinorAxisRadius(),
                        parentTwo.getX(),
                        parentTwo.getY(),
                        parentTwo.getOrientation(),
                        parentOne.getFunction());
        c[1] = new Gene(parentTwo.getMajorAxisRadius(),
                        parentOne.getMinorAxisRadius(),
                        parentOne.getX(),
                        parentOne.getY(),
                        parentOne.getOrientation(),
                        parentOne.getFunction());
      }
        break;
      case 1:{
        c[0] = new Gene(parentOne.getMajorAxisRadius(),
                        parentOne.getMinorAxisRadius(),
                        parentTwo.getX(),
                        parentTwo.getY(),
                        parentTwo.getOrientation(), 
                        parentOne.getFunction());
        c[1] = new Gene(parentTwo.getMajorAxisRadius(),
                        parentTwo.getMinorAxisRadius(),
                        parentOne.getX(),
                        parentOne.getY(),
                        parentOne.getOrientation(),
                        parentOne.getFunction());
      }
        break;
      case 2:{
        c[0] = new Gene(parentOne.getMajorAxisRadius(),
                        parentOne.getMinorAxisRadius(),
                        parentOne.getX(),
                        parentTwo.getY(),                        
                        parentTwo.getOrientation(), 
                        parentOne.getFunction());
        c[1] = new Gene(parentTwo.getMajorAxisRadius(),
                        parentTwo.getMinorAxisRadius(),
                        parentTwo.getX(),
                        parentOne.getY(),
                        parentOne.getOrientation(),
                        parentOne.getFunction());
      }
        break;
      case 3:{
        c[0] = new Gene(parentOne.getMajorAxisRadius(),
                        parentOne.getMinorAxisRadius(),
                        parentOne.getX(),
                        parentOne.getY(),                        
                        parentTwo.getOrientation(), 
                        parentOne.getFunction());
        c[1] = new Gene(parentTwo.getMajorAxisRadius(),
                        parentTwo.getMinorAxisRadius(),
                        parentTwo.getX(),
                        parentTwo.getY(),
                        parentOne.getOrientation(),
                        parentOne.getFunction());
      }
        break;        
      default:{
        c[0] = parentOne;
        c[1] = parentTwo;
      }
    }
    return c;
  }

  public String toString(){
    return GAMSettingsPanel.crossovers[5];
  }

}
