/*
 * BesagNewellGAM.java
 *
 * Created on October 7, 2003, 11:22 AM
 */

package geovista.proclude;

/**
 *
 * @author  jfc173
 */

import java.util.Vector;
import java.util.Iterator;

public class BesagNewellGAM extends AbstractGam{
    
    /** Creates a new instance of BesagNewellGAM */
    public BesagNewellGAM() {
    }
    
    public Vector run(){
        Vector returned = new Vector();
        double increment = (maxRadius - minRadius)/20; 
        Vector data = initializer.getDataSet();
        Iterator it = data.iterator();
        Gene g;
        //For each case
        while(it.hasNext()){
            DataPoint next = (DataPoint) it.next();
            if (next.getTarget() > 0){
                        
        //Put a circle there of minimum radius
                g = new Gene(minRadius, minRadius, next.getLocation().x, next.getLocation().y, 0, fitnessFunction);
                
        //Expand it outward until minPoints is met or maxRadius is reached
                boolean satisfied = false;
                while ((g.getMajorAxisRadius() <= maxRadius) && (g.getCount() < minPoints)){
                       g.setMajorAxisRadius(g.getMajorAxisRadius() + increment);
                       g.setMinorAxisRadius(g.getMinorAxisRadius() + increment);
                }  
               
        //Get fitness of that circle            
        //If it is above minAccepted, keep it.
                if (g.getFitness() >= minAccepted){
                     returned.add(g);
                }
            }
        }
                 
        return returned;
    }
    
}
