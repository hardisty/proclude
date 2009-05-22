package geovista.proclude;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Vector;
import java.util.Iterator;
import java.awt.geom.Ellipse2D;

/**
 * This class contains the genetic difference method of determining whether or not
 * a gene is intruding on the banned list.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class RelocateDifference extends Relocation{

  private double expectedDensity;
  private int minPoints;
  private double minFitness;

  public RelocateDifference(int i, double d) {
    minPoints = i;
    minFitness = d;
  }

  /**
   * Sets the expected density.
   */

  public void setExpectedDensity(double d){
    expectedDensity = d;
  }

  /**
   * Determines whether or not the gene g must be relocated because it impinges
   * on the banned areas.
   *
   * Note that the intersection and containment tests check the bounding box, not the shape itself.
   * While not preferred, this simplifies the programming.
   *
   * The method for determining this is:
   * For each gene in the banned list,
   *   If the gene in the list does not intersect g at all, do not move g.
   *   If the gene in the list totally contains g,
   *     If the area of g is less than half that of the gene in the list, do not move g.
   *        Otherwise, move g.
   *   If g contains the gene in the list,
   *     If the area of g is more than twice the area of the gene in the list, do not move g.
   *        Otherwise, move g.
   *   If the gene in the list intersects g,
   *     If the area in common is more than half the area of g and more than half
   *            the area of the gene in the list, move g.
   *        Otherwise, do not move g.
   */

  public boolean move(Vector bannedList, Gene g){
    Iterator it = bannedList.iterator();
    boolean value = false;
    while (it.hasNext() && !value){
      Gene nextInList = (Gene) it.next();
      Shape nextInListShape = nextInList.toShape();
      Shape gShape = g.toShape();

      //IF nextInList CONTAINS g
      if (nextInListShape.contains(gShape.getBounds2D())){
        // IF g IS MORE THAN HALF THE AREA OF nextInList, RELOCATE g
        double gArea = (Math.PI * g.getMajorAxisRadius() * g.getMinorAxisRadius());
        double nilArea = (Math.PI * nextInList.getMajorAxisRadius() * nextInList.getMinorAxisRadius());
        value = (gArea > nilArea/2);

        // IF g CONTAINS nextInList
      } else if (gShape.contains(nextInListShape.getBounds2D())){
        // IF nextInList IS MORE THAN HALF THE AREA OF g, RELOCATE g
        double gArea = (Math.PI * g.getMajorAxisRadius() * g.getMinorAxisRadius());
        double nilArea = (Math.PI * nextInList.getMajorAxisRadius() * nextInList.getMinorAxisRadius());
        value = (nilArea > gArea/2);

        // IF nextInList INTERSECTS g
      } else if (nextInListShape.intersects(gShape.getBounds2D())){
        // IF intersection IS MORE THAN HALF THE SIZE OF BOTH g AND nextInList, RELOCATE g
        double isectWidth = Math.min(nextInListShape.getBounds2D().getMaxX(),
                                     gShape.getBounds2D().getMaxX()) - 
                            Math.max(nextInListShape.getBounds2D().getMinX(),
                                     gShape.getBounds2D().getMinX());
        double isectHeight = Math.min(nextInListShape.getBounds2D().getMaxY(),
                                      gShape.getBounds2D().getMaxY()) - 
                             Math.max(nextInListShape.getBounds2D().getMinY(),
                                      gShape.getBounds2D().getMinY());
        double isectArea = (Math.PI * isectWidth * isectHeight);
        double gArea = (Math.PI * g.getMajorAxisRadius() * g.getMinorAxisRadius());
        double nilArea = (Math.PI * nextInList.getMajorAxisRadius() * nextInList.getMinorAxisRadius());
        value = ((isectArea > gArea/2) &&
                 (isectArea > nilArea/2));
      } else {
        value = false;
      }
    }
    return value;
  }

  private Gene createIsect(Gene first, Gene second){
      double innerLeft = Math.max(first.toShape().getBounds2D().getMinX(),
                                  second.toShape().getBounds2D().getMinX());
      double innerRight = Math.min(first.toShape().getBounds2D().getMaxX(),
                                   second.toShape().getBounds2D().getMaxX());
      double innerBottom = Math.max(first.toShape().getBounds2D().getMinY(),
                                    second.toShape().getBounds2D().getMinY());
      double innerTop = Math.min(first.toShape().getBounds2D().getMaxY(),
                                 second.toShape().getBounds2D().getMaxY());
      double origHeight = (innerTop - innerBottom) / 2;
      double origWidth = (innerRight - innerLeft) / 2;
      double centerX = innerLeft + origWidth;
      double centerY = innerBottom + origHeight;
      //create an ellipse that fills the bounding box
      Ellipse2D.Double nonRotated = new Ellipse2D.Double(innerLeft, 
                                                         innerTop,
                                                         innerRight - innerLeft,
                                                         innerTop - innerBottom);
      //rotate this ellipse
      double rotation, orientation;
      if ((first.getMajorAxisRadius() * first.getMinorAxisRadius()) < 
          (second.getMajorAxisRadius() * second.getMinorAxisRadius())){
          orientation = first.getOrientation();
          
          //May need to offset orientation by 90 because it's based on the major axis while the rotation
          //transform is not.  In other words, I may need to turn an extra 90 degrees to get the major axis
          //lined up correctly with the orientation value.
          if (first.toShape().getBounds2D().getHeight() > first.toShape().getBounds2D().getWidth()){
              rotation = orientation;
          } else {
              rotation = orientation - 90;
          }
      } else {
          orientation = second.getOrientation();
          if (second.toShape().getBounds2D().getHeight() > second.toShape().getBounds2D().getWidth()){
              rotation = orientation;
          } else {
              rotation = orientation - 90;
          }
      }
      AffineTransform rotationT = AffineTransform.getRotateInstance(Math.toRadians(rotation),
                                                                    centerX,
                                                                    centerY);
      Shape rotated = rotationT.createTransformedShape(nonRotated);
      //shrink it to fit it back into the box.
      double rotatedWidth = rotated.getBounds2D().getMaxX() - rotated.getBounds2D().getMinX();
      double rotatedHeight = rotated.getBounds2D().getMaxY() - rotated.getBounds2D().getMinY();
      double shrinkFactor = Math.min(origWidth / rotatedWidth,
                                     origHeight / rotatedHeight);
      double major, minor;
      if (origWidth > origHeight){
           major = origWidth * shrinkFactor;
           minor = origHeight * shrinkFactor;
      } else {
          major = origHeight * shrinkFactor;
          minor = origWidth * shrinkFactor;
      }
      
      return new Gene (major, minor, centerX, centerY, orientation, first.getFunction());      
  }
  
  private Gene createUnion(Gene first, Gene second){
      double outerLeft = Math.min(first.toShape().getBounds2D().getMinX(),
                                  second.toShape().getBounds2D().getMinX());
      double outerRight = Math.max(first.toShape().getBounds2D().getMaxX(),
                                   second.toShape().getBounds2D().getMaxX());
      double outerBottom = Math.min(first.toShape().getBounds2D().getMinY(),
                                    second.toShape().getBounds2D().getMinY());
      double outerTop = Math.max(first.toShape().getBounds2D().getMaxY(),
                                 second.toShape().getBounds2D().getMaxY());
      double origHeight = (outerTop - outerBottom) / 2;
      double origWidth = (outerRight - outerLeft) / 2;
      double centerX = outerLeft + origWidth;
      double centerY = outerBottom + origHeight;
      //create an ellipse that fills the bounding box
      Ellipse2D.Double nonRotated = new Ellipse2D.Double(outerLeft, 
                                                         outerTop,
                                                         outerRight - outerLeft,
                                                         outerTop - outerBottom);
      //rotate this ellipse
      double rotation, orientation;
      if ((first.getMajorAxisRadius() * first.getMinorAxisRadius()) > 
          (second.getMajorAxisRadius() * second.getMinorAxisRadius())){
          orientation = first.getOrientation();
          
          //May need to offset orientation by 90 because it's based on the major axis while the rotation
          //transform is not.  In other words, I may need to turn an extra 90 degrees to get the major axis
          //lined up correctly with the orientation value.
          if (first.toShape().getBounds2D().getHeight() > first.toShape().getBounds2D().getWidth()){
              rotation = orientation;
          } else {
              rotation = orientation - 90;
          }
      } else {
          orientation = second.getOrientation();
          if (second.toShape().getBounds2D().getHeight() > second.toShape().getBounds2D().getWidth()){
              rotation = orientation;
          } else {
              rotation = orientation - 90;
          }
      }
      AffineTransform rotationT = AffineTransform.getRotateInstance(Math.toRadians(rotation),
                                                                    centerX,
                                                                    centerY);
      Shape rotated = rotationT.createTransformedShape(nonRotated);
      //shrink it to fit it back into the box.
      double rotatedWidth = rotated.getBounds2D().getMaxX() - rotated.getBounds2D().getMinX();
      double rotatedHeight = rotated.getBounds2D().getMaxY() - rotated.getBounds2D().getMinY();
      double shrinkFactor = Math.min(origWidth / rotatedWidth,
                                     origHeight / rotatedHeight);
      double major, minor;
      if (origWidth > origHeight){
           major = origWidth * shrinkFactor;
           minor = origHeight * shrinkFactor;
      } else {
          major = origHeight * shrinkFactor;
          minor = origWidth * shrinkFactor;
      }
      
      return new Gene (major, minor, centerX, centerY, orientation, first.getFunction());        
  }
  
  //returns true when I want to keep first

  private boolean isectCompare(Fitness f, Gene first, Gene isect){
    boolean toBeReturned;
    if ((f instanceof FitnessAddRadii) ||
        (f instanceof FitnessOptimalArea) ||
        (f instanceof FitnessOptimalRadius) ||
        (f instanceof FitnessMonteCarlo)){
      double firstFitness = f.run(first);
      double isectFitness = f.run(isect);
      toBeReturned = (firstFitness > isectFitness);
    } else if ((f instanceof FitnessDensity) ||
               (f instanceof FitnessMaxAtN)){
      double d;
      if ((first.getPopulation() - isect.getPopulation()) == 0){
        d = -1;
      } else {
        d = (double) (first.getCount() - isect.getCount()) / (first.getPopulation() - isect.getPopulation());
      }
      toBeReturned = ((isect.getCount() < minPoints) ||
                      (d > expectedDensity * minFitness) ||
                      (d == -1));
    } else if (f instanceof FitnessRelative){
      double d;
      if (first.getPopulation() == isect.getPopulation()){
        d = -1;
      } else {
        int popDiff = first.getPopulation() - isect.getPopulation();
        int tarDiff = first.getCount() - isect.getCount();
        double expDiff = expectedDensity * popDiff;
        d = (tarDiff - expDiff) - (.0002 * popDiff);
      }
      toBeReturned = ((isect.getCount() < minPoints) ||
                      (d > minFitness) ||
                      (d == -1));
    } else if (f instanceof FitnessRelativePct){
      double d;
       if (first.getPopulation() == isect.getPopulation()){
         d = -1;
       } else {
         int popDiff = first.getPopulation() - isect.getPopulation();
         int tarDiff = first.getCount() - isect.getCount();
         double expDiff = expectedDensity * popDiff;
         d = tarDiff/expDiff;
       }
       toBeReturned = ((isect.getCount() < minPoints) ||
                       (d > minFitness) ||
                       (d == -1));
    } else {  //f is an instance of FitnessPoisson
      int popDiff = first.getPopulation() - isect.getPopulation();
      int tarDiff = first.getCount() - isect.getCount();
      double expDiff = expectedDensity * popDiff;
      double top = ((Math.pow(Math.E,(0 - expDiff)) * (Math.pow(expDiff,tarDiff))));
      double bottom = FitnessPoisson.fact(tarDiff);
      double poissonProb = top/bottom;
      toBeReturned = ((1 - poissonProb) > minFitness);
    } 
    return toBeReturned;
  }

  // returns true when I want to keep first and second

  private boolean unionCompare(Fitness f, Gene first, Gene second, Gene union, Gene isect){
    boolean toBeReturned;
    if ((f instanceof FitnessAddRadii) ||
        (f instanceof FitnessOptimalArea) ||
        (f instanceof FitnessOptimalRadius) ||
        (f instanceof FitnessMonteCarlo)){
      double firstFitness = f.run(first);
      double secondFitness = f.run(second);
      double unionFitness = f.run(union);
      toBeReturned = ((firstFitness > unionFitness) &&
                      (secondFitness > unionFitness));
      } else if ((f instanceof FitnessDensity) ||
                 (f instanceof FitnessMaxAtN)){
        double d3;
        if ((union.getPopulation() - first.getPopulation() - second.getPopulation() + isect.getPopulation()) == 0){
          d3 = -1;
        } else {
          d3 = (double) ((union.getCount() - first.getCount() - second.getCount() + isect.getCount()) /
                         (union.getPopulation() - first.getPopulation() - second.getPopulation() + isect.getPopulation()));
        }
        toBeReturned = ((d3 > expectedDensity * minFitness) ||
                        (d3 == -1));
      } else if (f instanceof FitnessRelative){
        double d;
        if (first.getPopulation() == isect.getPopulation()){
          d = -1;
        } else {
          int popDiff = (union.getPopulation() - first.getPopulation() - second.getPopulation() + isect.getPopulation());
          int tarDiff = (union.getCount() - first.getCount() - second.getCount() + isect.getCount());
          double expDiff = expectedDensity * popDiff;
          d = (tarDiff - expDiff) - (.0002 * popDiff);
        }
        toBeReturned = ((d > minFitness)
                        || (d == -1));
      } else if (f instanceof FitnessRelativePct){
        double d;
        if (first.getPopulation() == isect.getPopulation()){
          d = -1;
        } else {
          int popDiff = (union.getPopulation() - first.getPopulation() - second.getPopulation() + isect.getPopulation());
          int tarDiff = (union.getCount() - first.getCount() - second.getCount() + isect.getCount());
          double expDiff = expectedDensity * popDiff;
          d = tarDiff / expDiff;
        }
        toBeReturned = ((d > minFitness)
                        || (d == -1));
      } else {  //f is an instance of FitnessPoisson
        int popDiff = (union.getPopulation() - first.getPopulation() - second.getPopulation() + isect.getPopulation());
        int tarDiff = (union.getCount() - first.getCount() - second.getCount() + isect.getCount());
        double expDiff = expectedDensity * popDiff;
        double top = ((Math.pow(Math.E,(0 - expDiff)) * (Math.pow(expDiff,tarDiff))));
        double bottom = FitnessPoisson.fact(tarDiff);
        double poissonProb = top/bottom;
        toBeReturned = ((1 - poissonProb) > minFitness);
      }
      return toBeReturned;
  }

  /**
   * This method determines how to update the banned list by adding g.
   *
   * For each gene in the banned list,
   *   If the gene in the list does not intersect g at all, add g and do not remove the gene in the list.
   *   If the gene in the list totally contains g,
   *     If the area in the gene in the list, but not in g, has a point density of three times
   *             the expected rate, keep the gene in the list and do not add g.
   *        Otherwise, add g and remove the gene in the list.
   *   If g contains the gene in the list,
   *     If the area in g, but not in the gene in the list, has a point density of three times
   *             the expected rate, add g and remove the gene in the list.
   *        Otherwise, keep the gene in the list and do not add g.
   *   If the gene in the list intersects g,
   *     Compare the gene in the list with the intersection in the same manner as above.
   *     Compare g with the intersection in the same manner as above.
   *     If the intersection is better than both the gene in the list and g,
   *        Add the intersection, and remove the gene in the list.
   *     If the gene in the list is better than the intersection and the intersection is better than g,
   *        Keep the gene in the list and do not add g.
   *     If g is better than the intersection and the intersection is better than the gene in the list,
   *        Add g and remove the gene in the list.
   *     If both the gene in the list and g are better than the intersection,
   *        Compare the gene in the list and g with an ellipse representing their union.
   *        If the union is better than g and the gene in the list,
   *           Add the union and remove the gene in the list.
   *           Otherwise, add g and do not remove the gene in the list.
   */

  public Vector updateBannedList(Vector bannedList, Gene g){
    Vector removeList = new Vector();
    Vector addList = new Vector();
    addList.add(g);
    Iterator it = bannedList.iterator();
    while (it.hasNext()){
      Gene nextInList = (Gene) it.next();
      Shape nextInListShape = nextInList.toShape();
      Shape gShape = g.toShape();
      //IF nextInList CONTAINS g
      if (nextInListShape.contains(gShape.getBounds2D())){
        double d;
        try{
            d = (nextInList.getCount() - g.getCount()) / (nextInList.getPopulation() - g.getPopulation());
        } catch (ArithmeticException ae){ //divide by zero error
            d = 0;
        }
                   
        // IF AREA IN nextInList BUT NOT IN g DOES NOT SATISFY DEFINITION OF CLUSTER (minFitness*expectedRate)
        // REPLACE nextInList WITH g
//        System.out.print("nextInList: " + nextInList);
//        System.out.print("contains g: " + g);
        if (d < expectedDensity * minFitness){
          removeList.add(nextInList);
//          System.out.println("Replaced nextInList with g.");
        } else {        // IF AREA IN nextInList BUT NOT IN add SATISFIES DEFINITION OF CLUSTER, DO NOTHING
//          System.out.println("Kept nextInList and didn't add g.");
          addList.remove(g);
        }

        // IF g CONTAINS nextInList
      } else if (gShape.contains(nextInListShape.getBounds2D())){
        double d = 0;
        if (g.getPopulation() - nextInList.getPopulation() != 0){
          d = (g.getCount() - nextInList.getCount()) /
              (g.getPopulation() - nextInList.getPopulation());
        }
        // IF AREA IN g BUT NOT IN nextInList SATISFIES DEFINITION OF CLUSTER (minFitness*expectedRate)
        // REPLACE nextInList WITH g
//        System.out.print("g: " + g);
//        System.out.print("contains nextInList: " + nextInList);
        if (d > expectedDensity * minFitness){
          removeList.add(nextInList);
//          System.out.println("Replaced nextInList with g.");
        } else {        // IF AREA IN g BUT NOT IN nextInList DOES NOT SATISFY DEFINITION OF CLUSTER, DO NOTHING
//          System.out.println("Kept nextInList and did not add g.");
          addList.remove(g);
        }

        // IF nextInList INTERSECTS g
      } else if (nextInListShape.intersects(gShape.getBounds2D())){
        String s1;
        String s2;
        // CREATE GENE APPROXIMATING INTERSECTION OF g AND nextInList
        Gene isect = createIsect(nextInList, g);
        // CREATE GENE APPROXIMATING UNION OF g AND nextInList
        Gene union = createUnion(nextInList, g);

//        System.out.println("Intersection");
//        System.out.print("New gene: " + g);
//        System.out.print("Next in list: " + nextInList);
//        System.out.print("Intersection: " + isect);
//        System.out.print("Union: " + union);
//        System.out.println("Expected Density: " + expectedDensity);
//        System.out.println("Minimum accepted fitness: " + minFitness);

        // COMPARE g WITH isect
        if (isectCompare(g.getFunction(), g, isect)){
          s1 = "g";
        } else {
          s1 = "isect";
        }
        // COMPARE nextInList WITH isect
        double d2;
        if (isectCompare(nextInList.getFunction(), nextInList, isect)){
          s2 = "nextInList";
        } else {
          s2 = "isect";
        }

        if ((s1.equals("g")) && (s2.equals("isect"))){   //g is better than isect, and isect is better than nextInList
                             //replace nextInList with g
          removeList.add(nextInList);
//          System.out.print("Replaced nextInList with new gene");
        }
        if ((s1.equals("isect")) && (s2.equals("nextInList"))){  //nextInList is better than isect, and isect is better than g
         //do nothing
//          System.out.print("Kept nextInList, and didn't add new gene.");
          addList.remove(g);
        }
        if ((s1.equals("isect")) && (s2.equals("isect"))){    //isect is better than both nextInList and g
          addList.remove(g);
          addList.add(isect);                       //replace nextInList with isect
          removeList.add(nextInList);
//          System.out.print("Replaced nextInList with intersection.");
        }
        if ((s1.equals("g")) && (s2.equals("nextInList"))){      //nextInList and g are both better than isect
          // COMPARE BOTH g AND nextInList WITH union
          if (!(unionCompare(g.getFunction(), g, nextInList, union, isect))){
            addList.remove(g);
            addList.add(union);               //union is better than nextInList and g
            removeList.add(nextInList);       //replace nextInList with union
//            System.out.println("Replaced nextInList with union.");
          } else {                           //nextInList and g are better than union
            //add g without removing nextInList
//            System.out.print("Kept nextInList, and added new gene.");
          }
        }
      } else {
        // nextInList AND g ARE TOTALLY DISJOINT
      }
    }
    Iterator addIt = addList.iterator();
    while(addIt.hasNext()){
      Gene next = (Gene) addIt.next();
      bannedList.add(next);
    }
    Iterator removeIt = removeList.iterator();
    while (removeIt.hasNext()){
      Gene next = (Gene) removeIt.next();
      bannedList.remove(next);
    }
//    System.out.println();
//    System.out.println(bannedList);
    return bannedList;
  }

  public String toString(){
    return GAMSettingsPanel.relocations[1];
  }

}