package geovista.proclude;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

/**
 * This class contains the gene object.  It represents the ellipses for the GAM.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class Gene{

  private double major;
  private double minor;
  private double x;
  private double y;
  private double fitness;
  private double orientation;
  private char[] bits;
  private boolean bitsChanged;
  private Fitness function;
  private int count, population;
  private int[] containedPoints;

  public Gene(double d1, double d2, double d3, double d4, double d5, Fitness f){
    major = d1;
    minor = d2;
    x = d3;
    y = d4;
    orientation = d5;
    bits = new char[28];
    bitsChanged = true;
    function = f;
    count = function.count(this);
    population = function.getPopulation();
    fitness = function.run(this);
    containedPoints = function.getContainedPoints();
  }

//  public Gene(char[] ca, Fitness f){
//    bits = ca;
//    major = Integer.parseInt(new String(ca, 0, 7), 2);
//    minor = Integer.parseInt(new String(ca, 7, 7), 2);
//    x = Integer.parseInt(new String(ca, 14, 7), 2);
//    y = Integer.parseInt(new String(ca, 21, 7), 2);
//    bitsChanged = false;
//    function = f;
//    count = function.count(major, minor, x, y);
//    population = function.getPopulation();
//    fitness = function.run(major, minor, x, y);
//    containedPoints = function.getContainedPoints();
//  }

  public Gene(String[] data, Fitness f){
    x = Double.parseDouble(data[1]);
    y = Double.parseDouble(data[3]);
    major = Double.parseDouble(data[5]);
    minor = Double.parseDouble(data[7]);
    orientation = Double.parseDouble(data[9]);
    function = f;
    count = function.count(this);
    population = function.getPopulation();
    fitness = function.run(this);
    containedPoints = function.getContainedPoints();
  }

  /**
   * returns the fitness function associated with this gene.
   */

  public Fitness getFunction(){
    return function;
  }

  /**
   * sets the fitness function
   */

  public void setFunction(Fitness f){
    function = f;
    count = function.count(this);
    population = function.getPopulation();
    fitness = function.run(this);
    containedPoints = function.getContainedPoints();
  }

  /**
   * returns the horizontal radius.
   */

  public double getMajorAxisRadius(){
    return major;
  }

  /**
   * sets the horizontal radius
   */

  public void setMajorAxisRadius(double d){
    major = d;
    count = function.count(this);
    population = function.getPopulation();
    fitness = function.run(this);
    containedPoints = function.getContainedPoints();
    bitsChanged = true;
  }

  /**
   * gets the vertical radius
   */

  public double getMinorAxisRadius(){
    return minor;
  }

  /**
   * sets the vertical radius
   */

  public void setMinorAxisRadius(double d){
    minor = d;
    count = function.count(this);
    population = function.getPopulation();
    fitness = function.run(this);
    containedPoints = function.getContainedPoints();
    bitsChanged = true;
  }

  /**
   * gets the X value of the centroid
   */

  public double getX(){
    return x;
  }

  /**
   * sets the X value of the centroid
   */

  public void setX(double d){
    x = d;
    count = function.count(this);
    population = function.getPopulation();
    fitness = function.run(this);
    containedPoints = function.getContainedPoints();
    bitsChanged = true;
  }

  /**
   * gets the Y value of the centroid
   */

  public double getY(){
    return y;
  }

  /**
   * sets the Y value of the centroid
   */

  public void setY(double d){
    y = d;
    count = function.count(this);
    population = function.getPopulation();
    fitness = function.run(this);
    containedPoints = function.getContainedPoints();
    bitsChanged = true;
  }

  /**
   * returns the fitness value of the gene
   */

  public double getFitness(){
    return fitness;
  }

  /**
   * sets the fitness value of the gene
   * (Is this ever used?)
   */

  public void setFitness(double d){
    fitness = d;
  }

  /**
   *returns the orientation of major axis of the gene
   */
  
  public double getOrientation(){
      return orientation;
  }
  
   /**
   *sets the orientation of major axis of the gene
   */
  
  public void setOrientation(double d){
      orientation = d;
      count = function.count(this);
      population = function.getPopulation();
      fitness = function.run(this);
      containedPoints = function.getContainedPoints();
      bitsChanged = true;
  }
  
  public boolean isVertical(){
      return ((Math.abs(270 - orientation) < 45) || (Math.abs(90 - orientation) < 45));
  }
  
  /**
   * Returns the population size contained within the gene.
   */

  public int getPopulation(){
    return population;
  }

  /**
   * returns the number of target cases contained within the gene
   */

  public int getCount(){
    return count;
  }

  /**
   * returns a vector of the points contained within the gene
   */

  public int[] getContainedPoints(){
    return containedPoints;
  }

  /**
   * Converts the centroid X, centroid Y, and both radii into a bit string.
   * Needs to be updated to account for the fact that the maximum value of X and Y
   * is not necessarily 100 (and thus 7 bits per value is not necessarily a good
   * idea).
   *
   * The gene is represented as a bit string of length 28, the first 7 bits encoding
   * the ceiling of the horizontal radius, the next 7 bits encoding the
   * ceiling of the vertical radius, the next 7 bits encoding the ceiling
   * of the X value, and the last 7 bits encoding the ceiling of the Y value.
   *
   * There's no guarantee that any of the code that uses bit strings works.  I had
   * better success with a double representation than a bit string representation,
   * and so abandoned work with the bit strings.
   */

//  private char[] toBits(){
//    int intR1 = (int) Math.floor(r1) + 1;
//    int intR2 = (int) Math.floor(r2) + 1;
//    int intX = (int) Math.floor(x) + 1;
//    int intY = (int) Math.floor(y) + 1;
//
//    String r1String = Integer.toBinaryString(intR1);
//    String r2String = Integer.toBinaryString(intR2);
//    String xString = Integer.toBinaryString(intX);
//    String yString = Integer.toBinaryString(intY);
//
//    while (r1String.length() < 7){
//      r1String = "0" + r1String;
//    }
//    while (r2String.length() < 7){
//      r2String = "0" + r2String;
//    }
//    while (xString.length() < 7){
//      xString = "0" + xString;
//    }
//    while (yString.length() < 7){
//      yString = "0" + yString;
//    }
//    String bitString = r1String + r2String + xString + yString;
//    bits = bitString.toCharArray();
//    return bits;
//  }

  /**
   * Returns a bit string representation of the gene.
   *
   * See disclaimer in toBits().
   */

//  public char[] getBits(){
//    if (bitsChanged){
//      bitsChanged = false;
//      return toBits();
//    } else {
//      return bits;
//    }
//  }

  /**
   * Takes in a character array, and updates the gene values accordingly
   *
   * See disclaimer in toBits().
   */

//  public void setBits(char[] c){
//    bits = c;
//    major = Integer.parseInt(new String(bits, 0, 7), 2);
//    minor = Integer.parseInt(new String(bits, 7, 7), 2);
//    x = Integer.parseInt(new String(bits, 14, 7), 2);
//    y = Integer.parseInt(new String(bits, 21, 7), 2);
//    count = function.count(major, minor, x, y, orientation);
//    population = function.getPopulation();
//    fitness = function.run(major, minor, x, y, orientation);
//    containedPoints = function.getContainedPoints();
//    bitsChanged = false;
//  }

  private double roundToHundredths(double d){
    return ((double) Math.round(d * 100))/100;
  }

  /**
   *returns a shape representation of the gene.  Currently an orientated ellipse.
   */

  public Shape toShape(){
    Ellipse2D.Double e = new Ellipse2D.Double(x - major, y - minor, major * 2, minor * 2);
    double theta = Math.toRadians(orientation);
    AffineTransform at = AffineTransform.getRotateInstance(theta, x, y);
    return at.createTransformedShape(e);
  }  
  
  /**
   * returns a string of the gene attributes where each attribute name and attribute
   * value is separated by a tab character.
   */
  
  public String toString(){
    String output = "X: " + roundToHundredths(x) + '\t' +
                    "Y: " + roundToHundredths(y) + '\t' +
                    "Horiz. radius: " + roundToHundredths(major) + '\t' +
                    "Vertical radius: " + roundToHundredths(minor) + '\t' +
                    "Orientation: " + roundToHundredths(orientation) + '\t' + 
                    "Area: " + roundToHundredths(Math.PI * major * minor) + '\t' +
                    "Population: " + population + '\t' +
                    "Target count: " + count + '\t' +
                    "Expected count: " + function.getTotalExpected() + '\t' +
                    "Fitness: " + roundToHundredths(fitness) + "\n";
    return output;
  }

  /**
   * returns a string of the gene attributes where each attribute name and value
   * is separated by a comma.  It is suitable for putting genes into a .csv file.
   */

  public String toCSVString(){
    String output = "X: " + ',' + roundToHundredths(x) + ',' +
                    "Y: " +',' + roundToHundredths(y) + ',' +
                    "Major axis radius: " + ',' + roundToHundredths(major) + ',' +
                    "Minor axis radius: " + ',' + roundToHundredths(minor) + ',' +
                    "Orientation: " + ',' + roundToHundredths(orientation) + ',' +
                    "Area: " + ',' + roundToHundredths(Math.PI * major * minor) + ',' +
                    "Population: " + ',' + population + ',' +
                    "Target count: " + ',' + count + ',' +
                    "Fitness: " + ',' + roundToHundredths(fitness) + "\n";
    return output;
  }

}
