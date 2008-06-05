package geovista.proclude;

/**
 * The event object for importing a new data set.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;
import java.util.Vector;

public class NewDataSetEvent extends EventObject{

  private Vector dataSet;
  private double minX, minY, maxX, maxY, density;
  private InitGAMFile initializer;

  public NewDataSetEvent(Object source, Vector data, double xMin, double yMin,
                         double xMax, double yMax, double dens, InitGAMFile init) {
    super(source);
    dataSet = data;
    minX = xMin;
    minY = yMin;
    maxX = xMax;
    maxY = yMax;
    density = dens;
    initializer = init;
  }

  /**
   * Returns the initializer, which keeps track of information such as the minimum X,
   * minimum Y, and average density of target cases/population as well as the generator
   * of random genes.
   */

  public InitGAMFile getInitializer(){
    return initializer;
  }

  /**
   * Returns the new data set.
   */

  public Vector getDataSet(){
    return dataSet;
  }

  /**
   * Returns the minimum value of X.
   */

  public double getMinX(){
    return minX;
  }

  /**
   * Returns the maximum value of X.
   */

  public double getMaxX(){
    return maxX;
  }

  /**
   * Returns the minimum value of Y.
   */

  public double getMinY(){
    return minY;
  }

  /**
   * Returns the maximum value of Y.
   */

  public double getMaxY(){
    return maxY;
  }

  /**
   * Returns the ratio of target cases to population.
   */

  public double getDensity(){
    return density;
  }
}