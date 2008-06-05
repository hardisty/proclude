package geovista.proclude;

import java.util.Vector;

/**
 * The abstract superclass for survival methods.
 *
 * Known subclasses are SurviveCompareTwo, SurviveEliteN, and SurviveRelativeFitness
 *
 * @author Jamison Conley
 * @version 2.0
 */

public abstract class Survive {

  public Survive() {
  }

  /**
   * Returns a subset of the population that survived into the next generation.
   */

  public abstract Vector run(Vector population, double[] sortedFitnessArray);

}