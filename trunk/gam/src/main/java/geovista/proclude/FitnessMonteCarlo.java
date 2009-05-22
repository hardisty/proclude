/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geovista.proclude;

import java.awt.Shape;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author jconley
 */
public class FitnessMonteCarlo extends Fitness{

    int totalPop = -1;
    int totalCases = -1;
    int[][] monteCarloTargets;
    int[] counts;
    
    public FitnessMonteCarlo(Vector v, double d){
        dataSet = v;
        minPts = d;
        monteCarloTargets = new int[99][v.size()];
        System.out.println("creating random datasets");
        for (int i = 0; i < monteCarloTargets.length; i++){
            monteCarloTargets[i] = generateRandomDataset(dataSet);
            System.out.println("finished #" + i);
        }
        counts = new int[monteCarloTargets.length + 1];
    }
    
    @Override
    public double run(Gene g) {
        if (g.getPopulation() == 0){
            return -1;
        } else if (count(g) < minPts) {
            return -0.5;
        } else {        
            double ret = 0;
            int myCount = count(g);
            counts[0] = myCount;
            //compare count(g) with the same ellipse in the other datasets
            for (int i = 1; i < counts.length; i++){
                counts[i] = mcCount(g, monteCarloTargets[i-1]);
            }
            Arrays.sort(counts);

            //find the index for the actual count        
            ret = (double) Arrays.binarySearch(counts, myCount) / (double) counts.length;

            return ret;
        }
    }
    
    public String toString(){
        return GAMSettingsPanel.fitnesses[8];
    }    

    private int[] generateRandomDataset(Vector v){
        if (totalPop == -1){
            Iterator it1 = v.iterator();            
            totalPop = 0;
            totalCases = 0;
            while (it1.hasNext()){
                DataPoint next = (DataPoint) it1.next();
                totalPop = totalPop + next.getPopulation();
                totalCases = totalCases + next.getTarget();
            }
        }
        
        int[] ret = new int[v.size()]; 
        Random r = new Random();
        for (int i = 0; i < totalCases; i++){
            boolean assigned = false;
            while (!(assigned)){
                int rand = r.nextInt(ret.length);                
                double rand2 = r.nextDouble();
                int thisPop = ((DataPoint) v.get(rand)).getPopulation();
                if ((rand2 < ((double) thisPop / (double) totalPop)) &&
                    (ret[rand] < thisPop)){
                    assigned = true;
                    ret[rand]++;
                }
            }            
        }
        
        return ret;
    }
  
    private int mcCount(Gene g, int[] mcData){
        int target = 0;
        if (useContainedPoints){
            int[] contained = getContainedPoints();
            for (int i = 0; i < contained.length; i++){
                target = target + mcData[contained[i]];
            }
        } else {
            Shape s = g.toShape();            
            for (int index = 0; index < mcData.length; index++){
                DataPoint nextInList = (DataPoint) dataSet.get(index);
                if (s.contains(nextInList.getLocation())){
                    target = target + mcData[index];
                }
            }
        }
        return target;        
    }
    
}
