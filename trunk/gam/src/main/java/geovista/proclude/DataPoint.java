package geovista.proclude;

import java.awt.geom.Point2D;

/**
 * This class contains the representation of data points within the GAM.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public class DataPoint extends Object{

  private Point2D.Double location;
  private int population, target;
  private double expected;

  /**
   * The constructors accept x, y, a population and a target count.  The x and y are stored
   * as a location in a Point2D.Double object.  The population and target values
   * are used to enable the algorithm to find areas with a high ratio of target to
   * population (as in regions of abnormally high rates of a rare disease).
   */
  public DataPoint(int x, int y, int p, int c){
    location = new Point2D.Double(x, y);
    population = p;
    target = c;
    expected = -1;
  }

  public DataPoint(double x, double y, int p, int c){
    location = new Point2D.Double(x, y);
    population = p;
    target = c;
    expected = -1;
  }

  public DataPoint(int x, int y, int c, double e){
    location = new Point2D.Double(x, y);
    expected = e;
    target = c; 
    population = -1;
  }

  public DataPoint(double x, double y, int c, double e){
    location = new Point2D.Double(x, y);
    expected = e;
    target = c;  
    population = -1;
  }  
  
  public void setDensity(double d){
      if (population != -1){
        expected = population * d;
      }
  }
  
  /**
   * Returns the location as represented by a Point2D.Double object.  The use of
   * this object type enables easy determination of whether or not a data point
   * falls within an ellipse.
   */
  public Point2D.Double getLocation(){
    return location;
  }

  /**
   * Returns the population of the data point.
   */
  public int getPopulation(){
    return population;
  }

  /**
   * Returns the target count of the data point.
   */

  public int getTarget(){
    return target;
  }

  public double getExpected(){
      return expected;
  }
  
  public void setExpected(double d){
      expected = d;
  }
  
  /**
   * Returns a string representation of the data point
   */
  public String toString(){
    return ("" + location.getX() + "," + location.getY() + "," + population
           + "," + target + '\n');
  }

}