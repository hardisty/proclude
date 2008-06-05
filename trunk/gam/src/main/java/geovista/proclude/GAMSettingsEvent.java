package geovista.proclude;

/**
 * This class contains the GAM Settings Event, which is also used (probably
 * improperly) as a container for all the GAM settings, even when the use isn't
 * explicitly for event firing purposes.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;

public class GAMSettingsEvent extends EventObject {

  private int type, populationSize, randomGenes, firstAddition, updateHowOften,
              minimumPoints;
  private double probMut, minimumRadius, maximumRadius, minimumAccepted;
  private boolean pairs, banned, solution, anti;
  private Fitness function;
  private Selection selection;
  private Survive survival;
  private Crossover crossover;
  private Relocation relocation;
  private Mutation mutation;
  private StopType halting;
  private InitGAMFile initializer;

  public final static int GENETIC_TYPE = 0;
  public final static int RANDOM_TYPE = 1;
  public final static int SYSTEMATIC_TYPE = 2;
  public final static int BESAG_TYPE = 3;
  
  public GAMSettingsEvent(Object source, int GAMType, int popSize, double mutProb, int randGenes,
                          boolean selectPairs, boolean bannedList, boolean solutionList,
                          boolean antiConvergence, StopType haltCondition, int firstAdd,
                          int updateOften, int minPoints, Fitness fitnessFunction,
                          double minAccepted, double minRadius, double maxRadius, Selection selectMethod,
                          Survive survivalMethod, Crossover crossMethod,
                          Relocation relocateMethod, Mutation mutateMethod,
                          InitGAMFile init){
    super(source);
    type = GAMType;
    populationSize = popSize;
    probMut = mutProb;
    randomGenes = randGenes;
    pairs = selectPairs;
    banned = bannedList;
    solution = solutionList;
    anti = antiConvergence;
    halting = haltCondition;
    firstAddition = firstAdd;
    updateHowOften = updateOften;
    minimumPoints = minPoints;
    function = fitnessFunction;
    minimumAccepted = minAccepted;
    minimumRadius = minRadius;
    maximumRadius = maxRadius;
    selection = selectMethod;
    survival = survivalMethod;
    crossover = crossMethod;
    relocation = relocateMethod;
    mutation = mutateMethod;
    initializer  = init;
  }

  /**
   * Returns the initializer (an object containing such information as max X, max Y, and
   * density).
   */

  public InitGAMFile getInitializer(){
    return initializer;
  }

  /**
   * Returns the type of GAM (Genetic, Random, or Systematic).
   */

  public int getGAMType(){
    return type;
  }

  /**
   * Returns the population size.
   */

  public int getPopSize(){
    return populationSize;
  }

  /**
   * Returns the probability of mutation.
   */

  public double getProbMut(){
    return probMut;
  }

  /**
   * Returns the number of random genes per generation.
   */

  public int getRandGenes(){
    return randomGenes;
  }

  /**
   * Returns whether or not parents are being selected as pairs with preference
   * given to pairs of parents that are near each other.
   */

  public boolean getSelectPairs(){
    return pairs;
  }

  /**
   * Returns whether or not a list of banned areas is being kept.
   */

  public boolean getBannedList(){
    return banned;
  }

  /**
   * Returns whether or not a solution list is being kept.
   */

  public boolean getSolutionList(){
    return solution;
  }

  /**
   * Returns whether or not anti-convergence is being used.
   */

  public boolean getAntiConvergence(){
    return anti;
  }

  /**
   * Returns the halting condition.
   */

  public StopType getHaltCondition(){
    return halting;
  }

  /**
   * Returns the generation of the first addition to the banned and solution lists.
   */

  public int getFirstAdd(){
    return firstAddition;
  }

  /**
   * Returns the number of generations between additions to the banned and solution
   * lists.
   */

  public int getUpdateOften(){
    return updateHowOften;
  }

  /**
   * Returns the minimum number of points needed for a valid cluster.
   */

  public int getMinPoints(){
    return minimumPoints;
  }

  /**
   * Returns the fitness function.
   */

  public Fitness getFitnessFunction(){
    return function;
  }

  /**
   * Returns the minimum accepted fitness.
   */

  public double getMinAccepted(){
    return minimumAccepted;
  }

  /**
   * Returns the minimum radius.
   */

  public double getMinRadius(){
    return minimumRadius;
  }
  
  /**
   * Returns the maximum radius.
   */
  public double getMaxRadius(){
    return maximumRadius;
  }
  
  /**
   * Returns the selection method.
   */

  public Selection getSelectionMethod(){
    return selection;
  }

  /**
   * Returns the survival method.
   */

  public Survive getSurvivalMethod(){
    return survival;
  }

  /**
   * Returns the relocation method.
   */

  public Relocation getRelocationMethod(){
    return relocation;
  }

  /**
   * Returns the mutation method.
   */

  public Mutation getMutationMethod(){
    return mutation;
  }

  /**
   * Returns the crossover method.
   * @return
   */

  public Crossover getCrossoverMethod(){
    return crossover;
  }

  /**
   * Returns a string form of the settings that is formatted for .csv files.
   */

  public String toString(){
    String out;
    switch(type){
      case GENETIC_TYPE:{
        out = GAMSettingsPanel.TYPE + ',' + "Genetic" + '\n' +
              GAMSettingsPanel.POPSIZE + ',' + populationSize + '\n' +
              GAMSettingsPanel.PROBMUT + ',' + probMut + '\n' +
              GAMSettingsPanel.RANDOM + ',' + randomGenes + '\n' +
              GAMSettingsPanel.PAIRS + ',' + pairs + '\n' +
              GAMSettingsPanel.BANNED + ',' + banned + '\n' +
              GAMSettingsPanel.SOLUTION + ',' + solution + '\n' +
              GAMSettingsPanel.ANTI + ',' + anti + '\n' +
              GAMSettingsPanel.START + ',' + firstAddition + '\n' +
              GAMSettingsPanel.OFTEN + ',' + updateHowOften + '\n' +
              GAMSettingsPanel.MINPTS + ',' + minimumPoints + '\n' +
              GAMSettingsPanel.MINRAD + ',' + minimumRadius + '\n' +
              GAMSettingsPanel.MAXRAD + ',' + maximumRadius + '\n' +
              GAMSettingsPanel.MINFIT + ',' + minimumAccepted + '\n' +
              GAMSettingsPanel.FUNCTION + ',' + function.toString() + '\n' +
              GAMSettingsPanel.SELECT + ',' + selection.toString() + '\n' +
              GAMSettingsPanel.SURVIVE + ',' + survival.toString() + '\n' +
              GAMSettingsPanel.CROSS + ',' + crossover.toString() + '\n' +
              GAMSettingsPanel.MUTATE + ',' + mutation.toString() + '\n' +
              GAMSettingsPanel.HALT + ',' + halting.toString() + '\n' +
              GAMSettingsPanel.RELOCATE + ',' + relocation.toString() + '\n';
        break;
      }
      case RANDOM_TYPE:{
        out = GAMSettingsPanel.TYPE + ',' + "Random" + '\n' +
              GAMSettingsPanel.POPSIZE + ',' + populationSize + '\n' +
              GAMSettingsPanel.MAXGENS + ',' + halting.getMaxGens() + '\n' +
              GAMSettingsPanel.MINPTS + ',' + minimumPoints + '\n' +
              GAMSettingsPanel.MINRAD + ',' + minimumRadius + '\n' +
              GAMSettingsPanel.MAXRAD + ',' + maximumRadius + '\n' +
              GAMSettingsPanel.MINFIT + ',' + minimumAccepted + '\n' +
              GAMSettingsPanel.FUNCTION + ',' + function.toString() + '\n';
        break;
      }

      case SYSTEMATIC_TYPE:{
        out = GAMSettingsPanel.TYPE + ',' + "Systematic" + '\n' +
              GAMSettingsPanel.MINPTS + ',' + minimumPoints + '\n' +
              GAMSettingsPanel.MINRAD + ',' + minimumRadius + '\n' +
              GAMSettingsPanel.MAXRAD + ',' + maximumRadius + '\n' +
              GAMSettingsPanel.MINFIT + ',' + minimumAccepted + '\n' +
              GAMSettingsPanel.FUNCTION + ',' + function.toString() + '\n';
        break;
      }
      default:{
        out = GAMSettingsPanel.TYPE + ',' + "Besag-Newell" + '\n' +
              GAMSettingsPanel.MINPTS + ',' + minimumPoints + '\n' +
              GAMSettingsPanel.MINRAD + ',' + minimumRadius + '\n' +
              GAMSettingsPanel.MAXRAD + ',' + maximumRadius + '\n' +
              GAMSettingsPanel.MINFIT + ',' + minimumAccepted + '\n' +
              GAMSettingsPanel.FUNCTION + ',' + function.toString() + '\n';
        break;
      }
    }
    return out;
  }

}