package geovista.proclude;

import java.util.Vector;

/**
 * Abstract superclass for selection methods.
 *
 * Currently known subclasses are SelectPairs, SelectProbabilistic, and SelectRandomElite,
 *
 * @author Jamison Conley
 * @version 2.0
 */

public abstract class Selection {

  public Selection() {
  }

  /**
   * Takes a population of genes and a sorted array of that population's fitness
   * values, and returns two genes that are selected to be parents.  These two
   * genes are returned as an array of genes.
   */

  public abstract Gene[] run(Vector population, double[] sortedFitnessArray);  //WILL RETURN ARRAY OF TWO GENES

}