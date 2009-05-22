package geovista.proclude;

/**
 * This class contains the Genetic GAM.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.Vector;
import java.util.Arrays;

public class GeneticGAM extends AbstractGam {

  private int populationSize;
  private int randGenes;
  private double mutationProb;
  private Selection selectType;
  private Survive surviveType;
  private Crossover crossType;
  private Mutation mutationType;
  private Relocation relocationType;
  private StopType whenToHalt;
  private Vector population;
  private double[] fitnessArray;
  private int generationCount;
  private boolean selectPairs;
  private boolean useList;
  private boolean returnAllSolutions;
  private Vector solutionList;
  private Vector bannedList;
  private int solutionCounter;
  private int countNum;
  private int firstAdd;
  private boolean antiConvergence;

  public GeneticGAM(){
  }

  /**
   * Sets the population size.
   *
   * Throws an illegal argument exception if the population size is less than 2
   * or if the population size is less than the number of random genes per generation.
   */

  public void setPopSize(int i){
    if (i < 2){
      throw new IllegalArgumentException("The population size must be at least 2.");
    }
    if (i < randGenes){
      throw new IllegalArgumentException("The population size must be greater than the number of random genes per generation.");
    }
    populationSize = i;
  }

  /**
   * Sets the probability of mutation
   *
   * Throws an illegal argument exception if the probability of mutation is less than
   * zero or greater than one.
   */

  public void setProbMut(double d){
    if ((d < 0) || (d > 1)){
      throw new IllegalArgumentException("Mutation probability cannot be less than 0 or greater than 1.");
    }
    mutationProb = d;
  }

  /**
   * Sets the number of random genes per generation
   *
   * Throws an illegal argument exception if the number of random genes per generation
   * is greater than the population size.
   */

  public void setRandomGenes(int i){
    if (i > populationSize){
      throw new IllegalArgumentException("The number of random genes per generation must not be greater than the population size");
    }
    randGenes = i;
  }

  /**
   * Sets the option to select pairs of parents based on closeness.
   */

  public void setSelectPairs(boolean b){
    selectPairs = b;
  }

  /**
   * Sets the option to keep a list of banned areas, and not permit later ellipses
   * to encroach upon places in the banned list.
   */

  public void setBannedList(boolean b){
    useList = b;
  }

  /**
   * Sets the option to keep a solution list separate from the list of banned areas.
   */

  public void setSolutionList(boolean b){
    returnAllSolutions = b;
  }

  /**
   * Sets the option to restart with a new set of randomly created genes if the
   * GAM converges
   */

  public void setAntiConvergence(boolean b){
    antiConvergence = b;
  }

  /**
   * Sets the generation when the first addition to the banned/solution lists is done.
   *
   * Throws an illegal argument exception if the first addition is greater than
   * the maximum number of generations.
   */

  public void setFirstAdd(int i){
    if (i > whenToHalt.getMaxGens()){
      throw new IllegalArgumentException("First addition to the banned/solution lists must be before the maximum number of generations.");
    }
    firstAdd = i;
  }

  /**
   * Sets the frequency of how often the banned/solution lists are updated.
   */

  public void setUpdateOften(int i){
    countNum = i;
  }

  /**
   * Sets the selection method.
   */

  public void setSelectMethod(Selection s){
    selectType = s;
  }

  /**
   * Sets the survival method.
   */

  public void setSurviveMethod(Survive s){
    surviveType = s;
  }

  /**
   * Sets the crossover method.
   */

  public void setCrossoverMethod(Crossover c){
    crossType = c;
  }

  /**
   * Sets the mutation method.
   */

  public void setMutationMethod(Mutation m){
    mutationType = m;
  }

  /**
   * Sets the relocation method.
   */

  public void setRelocationMethod(Relocation r){
    relocationType = r;
  }

  /**
   * Sets the halting condition.
   */

  public void setHaltCondition(StopType s){
    whenToHalt = s;
  }


  private boolean closeEnough(Gene p1, Gene p2){

    double dist = Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) +
                            (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
    double prob = 1/Math.sqrt(dist);
    return (prob > Math.random());
  }

  /**
   * Steps through one generation, and returns a Vector of the new population
   * after that generation.
   */

  private Vector step(){
//    System.out.println();
//    System.out.println(generationCount);
//    System.out.println("The Population is:");
//    System.out.println(population);
    
    //DETERMINE WHO SURVIVES INTO NEXT GENERATION
    Vector nextGeneration = surviveType.run(population, fitnessArray);

    //DO THE CROSSOVER PROCESS
    //WHILE THE NEXT GENERATION ISN'T FULL
    while (nextGeneration.size() < (populationSize - randGenes)){
      //FIND PARENTS
      Gene parentOne, parentTwo;
      do {
        Gene[] parents = selectType.run(population, fitnessArray);
        parentOne = parents[0];
        parentTwo = parents[1];
      } while (selectPairs && (! closeEnough(parentOne, parentTwo)));
      //ADD CHILDREN
      Gene[] kids = crossType.children(parentOne, parentTwo);

      nextGeneration.add(kids[0]);
      if (nextGeneration.size() < (populationSize - randGenes)){
        nextGeneration.add(kids[1]);
      }
    }
    //ADD RANDOM GENES TO THE POPULATION
    while (nextGeneration.size() < populationSize){
      nextGeneration.add(initializer.createRandomGene(fitnessFunction, minRadius, maxRadius));
    }
    //MUTATE THE NEXT GENERATION
    for (int j = 0; j < populationSize; j++){
      if (Math.random() < mutationProb){
       nextGeneration.set(j, mutationType.run((Gene) nextGeneration.get(j)));
      }
    }

    //COMPUTE FITNESSES AND RELOCATE IF NECESSARY
    Gene bestGene = (Gene) nextGeneration.get(0);
    int bestX = 0;
    for (int x = 0; x < populationSize; x++){
      Gene next = (Gene) nextGeneration.get(x);
      while (relocationType.move(bannedList, next)){
        Gene newGene = initializer.createRandomGene(fitnessFunction, minRadius, maxRadius);
        next = newGene;
        nextGeneration.set(x, next);
      }
      fitnessArray[x] = next.getFitness();

      //FOR THE PURPOSES OF UPDATING THE BANNED LIST
      if (next.getFitness() > bestGene.getFitness()){
        bestGene = next;
        bestX = x;
      }
    }   //End for (int x = 0;...

    Arrays.sort(fitnessArray);

    //UPDATE THE BANNED LIST BY ADDING THE BEST GENE IN THE NEXT GENERATION IF IT'S ABOVE minAccepted AND CONTAINS MORE THAN minPoints POINTS
    if (useList){
      if ((generationCount >= firstAdd) &&
          (solutionCounter >= countNum) &&
          (bestGene.getFitness() >= minAccepted) &&
          (bestGene.getCount() >= minPoints)){
        solutionCounter = 0;
//        System.out.println(bestGene);
        Gene bestClone = new Gene (bestGene.getMajorAxisRadius(),                 //THIS KLUDGE IS DONE B/C .clone() IS
                                   bestGene.getMinorAxisRadius(),                 //PROTECTED, AND I CAN'T UNPROTECT IT.
                                   bestGene.getX(),                  //I USE A CLONED GENE TO PREVENT A MUTATION
                                   bestGene.getY(),                  //IN THE NEXT GENERATION FROM ALTERING bannedList
                                   bestGene.getOrientation(),
                                   bestGene.getFunction());
        bannedList = relocationType.updateBannedList(bannedList, bestClone);


        //IF NECESSARY, UPDATE THE SOLUTION LIST BY ADDING ALL CLUSTERS WITH MORE THAN minPoints POINTS AND A FITNESS OF AT LEAST minAccepted
        if (returnAllSolutions){
          for (int i = 0; i < populationSize; i++){
            Gene next = (Gene) nextGeneration.get(i);
            if ((next.getFitness() >= minAccepted) && (next.getCount() >= minPoints)){
              solutionList.add(next);
            }
          }//End for (int i = 0...
        } else {
          solutionList = bannedList;
        }  //End if (returnAllSolutions){...}else{
      }    //End if (generationCount > 4...
    }      //End if (useList){

    generationCount = generationCount + 1;
    solutionCounter = solutionCounter + 1;

    //IF AVOIDING CONVERGENCE, AND IT HAS CONVERGED, START OVER WITH RANDOM GENES

    double bestFitness = bestGene.getFitness();
    double medianFitness = roundToHundredths((double) fitnessArray[(int) Math.floor(populationSize / 2)]);

//    System.out.println(bestFitness);
//    System.out.println(medianFitness);

    if ((antiConvergence) &&
        ((bestFitness - medianFitness) < (0.01 * bestFitness)) &&
        (generationCount > firstAdd)){
      nextGeneration = initializer.getGenes(populationSize, fitnessFunction, minRadius, maxRadius);
//      System.out.println("EXPLODED CONVERGENCE!");
      for (int x = 0; x < populationSize; x++){
        Gene next = (Gene) nextGeneration.get(x);
        fitnessArray[x] = next.getFitness();
      }
      Arrays.sort(fitnessArray);
    }

    return nextGeneration;
  }

  private double roundToHundredths(double d){
    return ((double) Math.round(d * 100))/100;
  }

  /**
   * runs the Genetic GAM, and returns a Vector of the solution.  The solution is
   * the solution list, if both banned and solution lists are kept; the banned list,
   * if a banned list is kept, but not a solution list; or the population of the final
   * generation, if a banned list is not kept.
   */

  public Vector run(){
    generationCount = 0;
    solutionList = new Vector();
    bannedList = new Vector();
    solutionCounter = 0;
    whenToHalt.init();
    //INITIALIZE THE POPULATION

    population = initializer.getGenes(populationSize, fitnessFunction, minRadius, maxRadius);
    fitnessArray = new double[populationSize];
    //COMPUTE FITNESSES
    for (int x = 0; x < populationSize; x++){
      Gene next = (Gene) population.get(x);
      fitnessArray[x] = next.getFitness();
    }

    Arrays.sort(fitnessArray);

    while (! stop()){
      //RUN THROUGH THE GENERATIONS
      population = step();
    }

    if (useList){
      return solutionList;
    } else {
      return population;
    }
  }

  /**
   * returns the number of generations that the Genetic GAM stepped through
   * (This might not be used any more.)
   */

  public int getGenCount(){
    return generationCount;
  }

  private boolean stop(){
    return whenToHalt.run(fitnessArray, generationCount);
  }

}