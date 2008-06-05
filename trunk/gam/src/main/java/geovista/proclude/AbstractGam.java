package geovista.proclude;

/**
 * This class is an abstract supertype for Geographical Analysis
 * Machines.  Currently known subtypes are GeneticGAM, which uses a
 * genetic algorithm; RandomGam, which tests randomly generated hypotheses;
 * SystematicGam, which is the exhaustive search mechanism originally
 * developed by Stan Openshaw; and BesagNewellGAM, which is from Besag and 
 * Newell (1991).  One modification to bear in mind
 * is that this program uses ellipses instead of circles.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.Vector;

public abstract class AbstractGam{

  int minPoints;
  double minAccepted;
  double minRadius;
  double maxRadius;
  Fitness fitnessFunction;
  InitGAMFile initializer;
  Vector solution;

  public AbstractGam() {
  }

  /**
   * Accepts the initializer.  The initializer generates random ellipses and keeps
   * the data set.
   */
  public void setInitializer(InitGAMFile igf){
    initializer = igf;
  }

  /**
   * Accepts the minimum permitted fitness value for reporting an ellipse as a
   * worthwhile solution.
   *
   * Throws IllegalArgumentException if the minimum acceptable fitness is greater
   * than the maximum fitness value if the algorithm is using the FitnessMaxAtN
   * fitness function.
   */
  public void setMinAccepted(double d){
    if (fitnessFunction instanceof FitnessMaxAtN){
       if (((FitnessMaxAtN) fitnessFunction).getMaxFitness() < d){
         throw new IllegalArgumentException("The maximum computable fitness cannot be less than the minimum acceptable fitness.");
       }
    }
    minAccepted = d;
  }

  /**
   * Accepts the minimum number of points needed for an ellipse to be considered a
   * cluster.
   */
  public void setMinPoints(int i){
    minPoints = i;
  }
  
  /**
   * Accepts the fitness function that the GAM will use.
   *
   * Throws IllegalArgumentEception if the fitness function is of type FitnessMaxAtN
   * and the maximum fitness is less than the minimum acceptable fitness.
   */
  public void setFitnessFunction(Fitness f){
    if (f instanceof FitnessMaxAtN){
      if (((FitnessMaxAtN) f).getMaxFitness() < minAccepted){
        throw new IllegalArgumentException("The maximum computable fitness cannot be less than the minimum acceptable fitness.");
      }
    }
    fitnessFunction = f;
  }

  /**
   * returns the fitness function
   */

  public Fitness getFitnessFunction(){
    return fitnessFunction;
  }

  /**
   * Accepts the minimum radius size for a GAM ellipse.
   */
  public void setMinRadius(double d){
    minRadius = d;
  }

  public void setMaxRadius(double d){
    maxRadius = d;
  }
  
  /**
   * Abstract run method.  It returns a vector, which is the list of solutions.
   *
   * Note: this will probably need to be changed to return DataSetForApps to be
   * Studio-compliant.  Either that or (preferably) I'll need to write a method
   * that converts my Vector representation of solution lists into a DataSetForApps
   * and vice versa.
   */
  public abstract Vector run();

}