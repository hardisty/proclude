package geovista.proclude;

/**
 * Abstract superclass for mutation methods.
 *
 * Currently known subclasses are MutateDecreasingWithTime and
 * MutateLinearAmount
 *
 * @author Jamison Conley
 * @version 2.0
 */

public abstract class Mutation {

  public Mutation() {
  }

  /**
   * Takes a gene, and returns that gene with a mutation.
   */

  public abstract Gene run(Gene g);

}