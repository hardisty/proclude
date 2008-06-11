/*
 * MultiPoint.java
 *
 * Created on June 11, 2008, 11:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geovista.proclude;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author jfc173
 */
public class MultiPoint implements Shape{
    
    Point2D.Double[] points;
    
    /** Creates a new instance of MultiPoint */
    public MultiPoint() {
    }
    
    public MultiPoint(Point2D.Double[] array){
        points = array;
    }

    public Rectangle getBounds() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (int i = 0; i < points.length; i++){
            if (points[i].getX() < minX){
                minX = (int) Math.round(points[i].getX());
            }
            if (points[i].getX() > maxX){
                maxX = (int) Math.round(points[i].getX());
            }
            if (points[i].getY() < minY){
                minY = (int) Math.round(points[i].getY());
            }
            if (points[i].getY() > maxY){
                maxY = (int) Math.round(points[i].getY());
            }            
        }        
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    public Rectangle2D getBounds2D() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        for (int i = 0; i < points.length; i++){
            if (points[i].getX() < minX){
                minX = points[i].getX();
            }
            if (points[i].getX() > maxX){
                maxX = points[i].getX();
            }
            if (points[i].getY() < minY){
                minY = points[i].getY();
            }
            if (points[i].getY() > maxY){
                maxY = points[i].getY();
            }            
        }        
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);        
    }

    public boolean contains(double x, double y) {
        boolean ret = false;
        for (int i = 0; i < points.length; i++){
            if ((points[i].getX() == x) && (points[i].getY() == y)){
                ret = true;
            }
        }
        return ret;
    }

    public boolean contains(Point2D p) {
        boolean ret = false;
        for (int i = 0; i < points.length; i++){
            if ((points[i].getX() == p.getX()) && (points[i].getY() == p.getY())){
                ret = true;
            }
        }
        return ret;        
    }

    public boolean intersects(double x, double y, double w, double h) {
        return intersects(new Rectangle2D.Double(x, y, w, h));
    }

    public boolean intersects(Rectangle2D r) {
        boolean ret = false;
        for (int i = 0; i < points.length; i++){
            if (r.contains(points[i])){
                ret = true;
            }
        }
        return ret;
    }

    public boolean contains(double x, double y, double w, double h) {
        return contains(new Rectangle2D.Double(x, y, w, h));
    }

    public boolean contains(Rectangle2D r) {
        boolean ret = true;
        for (int i = 0; i < points.length; i++){
            if (!(r.contains(points[i]))){
                ret = false;
            }
        }
        return ret;        
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return new InternalPathIterator(at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new InternalPathIterator(at);
    }
    
    class InternalPathIterator implements PathIterator{
        AffineTransform transform;
        int currentIndex;
        
        public InternalPathIterator(AffineTransform at){
            transform = at;
        }        
        
        public int getWindingRule() {
            return this.WIND_NON_ZERO;
        }

        public boolean isDone() {
            return currentIndex == points.length;
        }

        public void next() {
            if (isDone()){
                return; 
            } else {
                currentIndex++;
            }
        }

        public int currentSegment(float[] coords) {
            coords[0] = (float) points[currentIndex].getX();
            coords[1] = (float) points[currentIndex].getY();
            if (transform != null){
                transform.transform(coords, 0, coords, 0, 1);
            }
            return this.SEG_MOVETO;
        }

        public int currentSegment(double[] coords) {
            coords[0] = points[currentIndex].getX();
            coords[1] = points[currentIndex].getY();
            if (transform != null){
                transform.transform(coords, 0, coords, 0, 1);
            }
            return this.SEG_MOVETO;
        }
        
    } 
    
    
}
