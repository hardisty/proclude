/*
 * SelectionGene.java
 *
 * Created on June 11, 2008, 11:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geovista.proclude;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.Vector;

/**
 *
 * @author jfc173
 */
public class SelectionGene extends Gene{
    
    /** Creates a new instance of SelectionGene */
    public SelectionGene(int[] ints, Fitness f) {
        super(0, 0, 0, 0, 0, f);
        count = function.count(this);
        population = function.getPopulation();
        fitness = function.run(this);
        containedPoints = function.getContainedPoints();        
    }
    
    public Shape toShape(){
        Vector data = function.dataSet;
        //TODO: turn the Vector into an array of Point2D.Doubles and create a MultiPoint with it.
        Point2D.Double[] points = new Point2D.Double[data.size()];
        for (int i = 0; i < data.size(); i++){
            points[i] = ((DataPoint) data.get(i)).getLocation();
        }
        return new MultiPoint(points);
    }
    
}
