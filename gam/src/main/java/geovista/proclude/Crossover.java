package geovista.proclude;

/**
 * This class contains the abstract supertype for crossover methods.
 *
 * Currently known subtypes are CrossAny, CrossMidLine,
 * CrossMidPoint, and CrossSinglePoint.
 *
 * @author Jamison Conley
 * @version 2.0
 */

public abstract class Crossover {

  public Crossover() {
  }

  /**
   * The children method for each type of crossover accepts two Genes, the parents,
   * and returns an array of Genes containing two values, the two children.
   */
  public abstract Gene[] children(Gene parentOne, Gene parentTwo);

}