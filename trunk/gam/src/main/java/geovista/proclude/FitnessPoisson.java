package geovista.proclude;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.util.Vector;

public class FitnessPoisson extends Fitness{

  private double expectedDensity;

  public FitnessPoisson(Vector v, double d) {
    dataSet = v;
    expectedDensity = d;
  }

  public double run(Gene g){
    int c = count(g);    
//    System.out.println("Actual count = " + c);
//    System.out.println("Expected count = " + expectedCount);
    double top = ((Math.pow(Math.E,(0 - totalExpected)) * (Math.pow(totalExpected ,c))));
    double bottom = fact(c);
    double poissonProb = top/bottom;
//    System.out.println("Top of the Poisson fraction is " + top);
//    System.out.println("Bottom of the Poisson fraction is " + bottom);
//    System.out.println("Probability (assuming Poisson distribution) of this happening is " + poissonProb);
//    System.out.println("____________________________________________");
    if ((c != 0) && (totalExpected != 0)){
      return(1 - poissonProb);
    } else {
      return 0;
    }
  }

  private static double iterFact(int i, int f){
    if ((i == 0) || (i == 1)){
      return f;
    } else {
      return iterFact(i-1, i*f);
    }
  }

  public static double fact(int i){
    return iterFact(i, 1);
  }

  public String toString(){
    return GAMSettingsPanel.fitnesses[7];
  }

}