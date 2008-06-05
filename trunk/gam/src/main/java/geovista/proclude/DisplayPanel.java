package geovista.proclude;

/**
 * This class contains the display panel part of the GUI.
 * Now a Studio-friendly bean
 *
 * Scrollable implementation code borrowed from ScrollablePicture.java contained
 * in the Java tutorial.  See the "How to Use Scroll Panes" lesson in the tutorial
 * for more detail.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Vector;
import java.util.Iterator;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.*;
import java.awt.geom.AffineTransform;

public class DisplayPanel extends JPanel implements DisplaySettingsListener,
                                                    DisplayModeListener,
                                                    NewDataSetListener,
                                                    ClearCreatedListener,
                                                    GAMSolutionListener,
                                                    GAMSettingsListener,
                                                    LoadResultsListener,
                                                    RemoveResultsListener,
                                                    ChangeColorListener,
//                                                    RunGAMListener,
                                                    Serializable{
    
    private Vector dataSet;
    private Vector solutions, colors;
    private Vector created = new Vector();
    private Vector createdGenes = new Vector();
    private Shape activeCreated;
    private Gene activeGenetic;
    private Gene activeCreatedGene;
    private int mode, borderWidth, dpSize, gsWidth, guWidth, csWidth, cuWidth;
    private double scalingFactor, xOffset, yOffset;
    private double minX, minY, maxX, maxY;
    private boolean move, drawBorder;
    private Fitness f;
    private Color bgColor, borderColor, dpColor, gsColor, csColor, cuColor;
    private Shape nullShape = new Ellipse2D.Double(0, 0, 0, 0);
    private Gene nullGene = new Gene(0, 0, 0, 0, 0, new FitnessDensity(new Vector()));
    private EventListenerList updateCreatedList = new EventListenerList();
    private EventListenerList updateGAMList = new EventListenerList();
    private Dimension area = new Dimension(0, 0);
    private double tick;
    private int active, gamSet;
    private AffineTransform scaling, offset;
    static Color[] colorRing = {Color.blue, Color.orange, Color.green, Color.yellow.darker()};
    
    public DisplayPanel() {
        dataSet = new Vector();
        solutions = new Vector();
        colors = new Vector();
        colors.add(0, colorRing[0]);
        colors.add(1, colorRing[1]);
        colors.add(2, colorRing[2]);
        colors.add(3, colorRing[3]);
        maxX = 0;
        maxY = 0;
        minX = 0;
        minY = 0;
        tick = 1;
        mode = 1;
        active = -1;
        move = false;
        drawBorder = true;
        f = new FitnessDensity(dataSet);
        activeCreated = nullShape;
        activeGenetic = nullGene;
        activeCreatedGene = nullGene;
        GAMMouseListener GML = new GAMMouseListener();
        addMouseListener(GML);
        addMouseMotionListener(GML);
        bgColor = Color.white;
        borderColor = Color.black;
        dpColor = Color.black;
        gsColor = Color.green.darker();
        csColor = Color.red;
        cuColor = Color.red;
        scalingFactor = 4;
        xOffset = 10;
        yOffset = 10;
        borderWidth = 1;
        dpSize = 4;
        gsWidth = 2;
        guWidth = 1;
        csWidth = 2;
        cuWidth = 1;
        offset = AffineTransform.getTranslateInstance(xOffset, yOffset);
        scaling = AffineTransform.getScaleInstance(scalingFactor, scalingFactor);
        setFocusable(true);

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UP");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DOWN");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LEFT");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RIGHT");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "COUNTER");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "CLOCK");
        this.getActionMap().put("UP", new ArrowKeyAction("y", "plus"));
        this.getActionMap().put("DOWN", new ArrowKeyAction("y", "minus"));
        this.getActionMap().put("LEFT", new ArrowKeyAction("x", "minus"));
        this.getActionMap().put("RIGHT", new ArrowKeyAction("x", "plus"));
        this.getActionMap().put("COUNTER", new RotateKeyAction("minus"));
        this.getActionMap().put("CLOCK", new RotateKeyAction("plus"));
    }
    
    /**
     * This inner class is a mouse listener that listens for events in the display
     * panel and updates the display appropriately.  Adapted from the MyListener
     * inner class in the SelectionDemo from the Java Tutorial.
     *
     * @author Jamison Conley
     * @version 1.0
     */
    class GAMMouseListener extends MouseInputAdapter{
        
        Point2D.Double initPoint = new Point2D.Double();
        Point2D.Double closePoint = new Point2D.Double();
        
        public GAMMouseListener() {
        }
        
        public void mousePressed(MouseEvent e) {
            requestFocusInWindow();
            int x = e.getX();
            int y = e.getY();
            initPoint = new Point2D.Double(x, y);
            switch (mode){
                case (1):{
                    int i = 0;
                    while (!move && (i < created.size())){
                        Shape next = (Shape) created.get(i);
                        if (next.contains(initPoint)){
                            move = true;
                            activeCreated = next;
                            activeCreatedGene = (Gene) createdGenes.get(i);
                        } else {
                            activeCreated = new Ellipse2D.Double(x, y, 0, 0);
                            activeCreatedGene = ellipseToGene((Ellipse2D.Double) activeCreated, f);
                        }
                        i++;
                    }
                    repaint();
                    break;
                }
                case (2):{
                    active = -1;
                    gamSet = -1;
                    Gene newGene = null;
                    //          System.out.println("There are " + solutions.size() + " sets to check.");
                    for(int k = 0; k < solutions.size(); k++){
                        Vector solution = (Vector) solutions.get(k);
                        int i = 0;
                        while (i < solution.size()){
                            Gene next = (Gene) solution.get(i);
//                            Ellipse2D.Double nextEllipse = (new Ellipse2D.Double(scalingFactor * (next.getX() - next.getR1()) + xOffset,
//                            scalingFactor * (next.getY() - next.getR2()) + yOffset,
//                            next.getR1() * 2 * scalingFactor,
//                            next.getR2() * 2 * scalingFactor));
                            Shape nextShape = next.toShape(); 
                            nextShape = offset.createTransformedShape(scaling.createTransformedShape(nextShape));
                            //              System.out.println("Checking ellipse set " + k + ": index " + i);
                            if (nextShape.contains(initPoint)){
                                newGene = next;
                                active = i;
                                gamSet = k;
                            }
                            i++;
                        }
                        //            System.out.println("Set index is now " + k);
                    }
                    if (newGene != null){
                        activeGenetic = newGene;
                    } else {
                        activeGenetic = nullGene;
                    }
                    //          System.out.println("Display panel: active set = " + set + ": active index = " + j);
                    fireUpdateGAM(gamSet, active);
                    repaint();
                    break;
                }
            }
        }
        
        public void mouseDragged(MouseEvent e) {
            if (mode == 1){
                if (move)
                    moveGene(e);
                else
                    updateSize(e);
            }
        }
        
        public void mouseReleased(MouseEvent e) {
            if (mode == 1){
                if (move){
                    moveGene(e);
                    move = false;
                } else {
                    updateSize(e);
                }
            }
        }
        
  /*
   * Update the location of the current ellipse
   * and call repaint.
   */
        
        void moveGene(MouseEvent e){
            double oldMinX = activeCreated.getBounds2D().getMinX();
            double oldMinY = activeCreated.getBounds2D().getMinY();
            double oldMaxX = activeCreated.getBounds2D().getMaxX();
            double oldMaxY = activeCreated.getBounds2D().getMaxY();
            int x = e.getX();
            int y = e.getY();
            double deltaX = x - initPoint.getX();
            double deltaY = y - initPoint.getY();
            initPoint = new Point2D.Double(x, y);
            AffineTransform shift = AffineTransform.getTranslateInstance(deltaX, deltaY);
            Shape newCreated = shift.createTransformedShape(activeCreated);
            int tou = created.indexOf(activeCreated);
            created.set(tou, newCreated);
            activeCreated = newCreated;
            double worldDeltaX = deltaX / scalingFactor;
            double worldDeltaY = deltaY / scalingFactor;
            activeCreatedGene = (Gene) createdGenes.get(tou);
            activeCreatedGene.setX(activeCreatedGene.getX() + worldDeltaX);
            activeCreatedGene.setY(activeCreatedGene.getY() + worldDeltaY);
            repaint();
            //      repaint(Math.min((int) Math.floor(oldMinX), (int) Math.floor(activeCreated.getMinX())) - csWidth,
            //              Math.min((int) Math.floor(oldMinY), (int) Math.floor(activeCreated.getMinY())) - csWidth,
            //              (int) Math.ceil(Math.max(oldMaxX, activeCreated.getMaxX()) - Math.min(oldMinX, activeCreated.getMinX())) + csWidth * 2,
            //              (int) Math.ceil(Math.max(oldMaxY, activeCreated.getMaxY()) - Math.min(oldMinY, activeCreated.getMinY())) + csWidth * 2);
            
//            createdGenes = new Vector();
//            for(int i = 0; i < created.size(); i++){              
//                createdGenes.add(i, ellipseToGene((Ellipse2D.Double) created.get(i), f));
//            }
            fireUpdateCreated(tou);
        }
        
        //This method is only called when the mouse is dragging the size of the ellipse inward or
        //outward.  This only happens before the ellipse gets orientated, so I can use Ellipse2Ds 
        //instead of the more generic Shapes.
        void updateSize(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            closePoint = new Point2D.Double(x, y);
            double minX = Math.min(initPoint.getX(), closePoint.getX());
            double minY = Math.min(initPoint.getY(), closePoint.getY());
            double maxX = Math.max(initPoint.getX(), closePoint.getX());
            double maxY = Math.max(initPoint.getY(), closePoint.getY());
            double newWidth = maxX - minX;
            double newHeight = maxY - minY;           
            Ellipse2D.Double newCreated = new Ellipse2D.Double(minX, minY, newWidth, newHeight);
            int tou = 0;
            try{
                tou = created.indexOf(activeCreated);
                created.set(tou, newCreated);                
                activeCreatedGene = ellipseToGene(newCreated, f);
                createdGenes.set(tou, activeCreatedGene);
            }
            catch(IndexOutOfBoundsException ie){
                created.add(newCreated);                
                activeCreatedGene = ellipseToGene(newCreated, f);
                createdGenes.add(activeCreatedGene);                
            }
            activeCreated = newCreated;
            repaint();
            //      repaint((int) Math.floor(minX) - csWidth,
            //              (int) Math.floor(minY) - csWidth,
            //              (int) Math.ceil(newWidth) + csWidth * 2,
            //              (int) Math.ceil(newHeight) + csWidth * 2);
//            createdGenes = new Vector();
//            for(int i = 0; i < created.size(); i++){
//                createdGenes.add(i, ellipseToGene((Ellipse2D.Double) created.get(i), f));
//            }
            fireUpdateCreated(tou);
        }
    }  //END class GAMMouseListener
    
    class ArrowKeyAction extends AbstractAction {
        
        String radius;
        String direction;
        
        public ArrowKeyAction(String xOrY, String plusOrMinus){
            radius = xOrY;
            direction = plusOrMinus;
        }
        
        public void actionPerformed(ActionEvent e){
            int index = created.indexOf(activeCreated);
            activeCreatedGene = (Gene) createdGenes.get(index);
            if ((radius == "y") && (direction == "plus")){
                if (activeCreatedGene.isVertical()){
                    activeCreatedGene.setMajorAxisRadius(activeCreatedGene.getMajorAxisRadius() + (tick / scalingFactor));                    
                } else {
                    activeCreatedGene.setMinorAxisRadius(activeCreatedGene.getMinorAxisRadius() + (tick / scalingFactor));                    
                }
                if (activeCreatedGene.getMinorAxisRadius() > activeCreatedGene.getMajorAxisRadius()){
                    activeCreatedGene.setMajorAxisRadius(activeCreatedGene.getMinorAxisRadius());
                    activeCreatedGene.setMinorAxisRadius(activeCreatedGene.getMajorAxisRadius());
                    activeCreatedGene.setOrientation(activeCreatedGene.getOrientation() + 90);
                    if (activeCreatedGene.getOrientation() > 360){
                        activeCreatedGene.setOrientation(activeCreatedGene.getOrientation() - 360);
                    }
                }
            }
            if ((radius == "y") && (direction == "minus")){
                if (activeCreatedGene.isVertical()){
                    activeCreatedGene.setMajorAxisRadius(activeCreatedGene.getMajorAxisRadius() - (tick / scalingFactor));                    
                } else {
                    activeCreatedGene.setMinorAxisRadius(activeCreatedGene.getMinorAxisRadius() - (tick / scalingFactor));                    
                }
                if (activeCreatedGene.getMinorAxisRadius() > activeCreatedGene.getMajorAxisRadius()){
                    activeCreatedGene.setMajorAxisRadius(activeCreatedGene.getMinorAxisRadius());
                    activeCreatedGene.setMinorAxisRadius(activeCreatedGene.getMajorAxisRadius());
                    activeCreatedGene.setOrientation(activeCreatedGene.getOrientation() + 90);
                    if (activeCreatedGene.getOrientation() > 360){
                        activeCreatedGene.setOrientation(activeCreatedGene.getOrientation() - 360);
                    }
                }
            }
            if ((radius == "x") && (direction == "plus")){
                if (activeCreatedGene.isVertical()){
                    activeCreatedGene.setMinorAxisRadius(activeCreatedGene.getMinorAxisRadius() + (tick / scalingFactor));                    
                } else {
                    activeCreatedGene.setMajorAxisRadius(activeCreatedGene.getMajorAxisRadius() + (tick / scalingFactor));                    
                }
                if (activeCreatedGene.getMinorAxisRadius() > activeCreatedGene.getMajorAxisRadius()){
                    activeCreatedGene.setMajorAxisRadius(activeCreatedGene.getMinorAxisRadius());
                    activeCreatedGene.setMinorAxisRadius(activeCreatedGene.getMajorAxisRadius());
                    activeCreatedGene.setOrientation(activeCreatedGene.getOrientation() + 90);
                    if (activeCreatedGene.getOrientation() > 360){
                        activeCreatedGene.setOrientation(activeCreatedGene.getOrientation() - 360);
                    }
                }
            }
            if ((radius == "x") && (direction == "minus")){
                if (activeCreatedGene.isVertical()){
                    activeCreatedGene.setMinorAxisRadius(activeCreatedGene.getMinorAxisRadius() - (tick / scalingFactor));                    
                } else {
                    activeCreatedGene.setMajorAxisRadius(activeCreatedGene.getMajorAxisRadius() - (tick / scalingFactor));                    
                }
                if (activeCreatedGene.getMinorAxisRadius() > activeCreatedGene.getMajorAxisRadius()){
                    activeCreatedGene.setMajorAxisRadius(activeCreatedGene.getMinorAxisRadius());
                    activeCreatedGene.setMinorAxisRadius(activeCreatedGene.getMajorAxisRadius());
                    activeCreatedGene.setOrientation(activeCreatedGene.getOrientation() + 90);
                    if (activeCreatedGene.getOrientation() > 360){
                        activeCreatedGene.setOrientation(activeCreatedGene.getOrientation() - 360);
                    }
                }
            }
            activeCreated = geneToShape(activeCreatedGene);
            repaint();
            created.set(index, activeCreated);
            fireUpdateCreated(index);
        }
    } //End class ArrowKeyAction
    
    class RotateKeyAction extends AbstractAction{
        
        String direction;
        
        public RotateKeyAction(String plusOrMinus){
            direction = plusOrMinus;
        }
        
        public void actionPerformed(ActionEvent e){
            int index = created.indexOf(activeCreated);
            activeCreatedGene = (Gene) createdGenes.get(index);
            double orientation = activeCreatedGene.getOrientation();
            if (direction == "plus"){
                orientation = orientation + tick;
                if (orientation > 360){
                    orientation = orientation - 360;
                }
            } else {
                orientation = orientation - tick;
                if (orientation < 0){
                    orientation = orientation + 360;
                }
            }
            activeCreatedGene.setOrientation(orientation);
            activeCreated = geneToShape(activeCreatedGene);
            repaint();
            created.set(index, activeCreated);
            fireUpdateCreated(index);
        }
    } //End class RotateKeyAction
    
    /**
     * This method updates the settings contained in a DisplaySettingsEvent.  It also
     * updates the display accordingly.
     */
    public void setSettings(DisplaySettingsEvent dse){
        if (dse.getScalingFactor() != scalingFactor){
            double change = (double) dse.getScalingFactor() / (double) scalingFactor;
            AffineTransform changeSize = AffineTransform.getScaleInstance(change, change);
            for (int i = 0; i < created.size(); i++){
                Shape next = (Shape) created.get(i);
                Shape updated = changeSize.createTransformedShape(next);
                created.set(i, updated);
            }
            Shape newActive = changeSize.createTransformedShape(activeCreated);
            activeCreated = newActive;
        }
        bgColor = dse.getbgColor();
        borderColor = dse.getBorderColor();
        dpColor = dse.getdpColor();
        gsColor = dse.getgsColor();
        csColor = dse.getcsColor();
        cuColor = dse.getcuColor();
        scalingFactor = dse.getScalingFactor();
        xOffset = 2 * scalingFactor - scalingFactor * minX;
        yOffset = 2 * scalingFactor - scalingFactor * minY;
        borderWidth = dse.getBorderWidth();
        dpSize = dse.getdpSize();
        gsWidth = dse.getgsWidth();
        guWidth = dse.getguWidth();
        csWidth = dse.getcsWidth();
        cuWidth = dse.getcuWidth();
        drawBorder = dse.getDrawBorder();
        area.width = (int) Math.round(maxX * scalingFactor + xOffset + dpSize * 3);
        area.height = (int) Math.round(maxY * scalingFactor + yOffset + dpSize * 3);
        setPreferredSize(area);
        revalidate();
        repaint();
    }
    
    /**
     * Accepts the mode (select a GAM ellipse or create your own ellipse)
     */
    public void setMode(int i){
        mode = i;
    }
    
    /**
     * Accepts the data set (a Vector of DataPoints).
     */
    public void setDataSet(Vector v){
        dataSet = v;
    }
    
    /**
     * Accepts the solution (a Vector of Genes).
     */
    public void setSolution(Vector v){
        solutions.add(v);
    }
    
    /**
     * Accepts the fitness function and updates the statistics on the created ellipses
     * to reflect this change.  This is needed so that the display panel can
     * turn the created ellipses into Genes.  It also is used by GamGUI's computeAction
     * to change the fitness function used to calculate the fitness of the created
     * ellipses.
     */
    public void setFitness(Fitness fit){
        f = fit;
        activeCreatedGene.setFunction(fit);
        int j = -1;
        for (int i = 0; i < created.size(); i++){
            ((Gene) createdGenes.get(i)).setFunction(fit);
            if (activeCreated.equals(created.get(i))){
                j = i;
            }
        }
        fireUpdateCreated(j);
    }
    
    /**
     * returns the active created ellipse in Gene form.
     */
    public Gene getActiveCreated(Fitness f){
        return activeCreatedGene;
    }
    
    /**
     * Accepts the minimum x value.
     */
    public void setMinX(double d){
        minX = d;
    }
    
    /**
     * Accepts the minimum y value.
     */
    public void setMinY(double d){
        minY = d;
    }
    
    /**
     * Accepts the maximum x value.
     */
    public void setMaxX(double d){
        maxX = d;
    }
    
    /**
     * Accepts the maximum y value.
     */
    public void setMaxY(double d){
        maxY = d;
    }
    
    private Gene ellipseToGene(Ellipse2D.Double e, Fitness f){
        if (e.getWidth() > e.getHeight()){
            return new Gene(e.getWidth()/(scalingFactor * 2),
                            e.getHeight()/(scalingFactor * 2),
                            (e.getX() - xOffset + e.getWidth()/2)/scalingFactor,
                            (e.getY() - yOffset + e.getHeight()/2)/scalingFactor,
                            0, 
                            f);
        } else {
            return new Gene(e.getHeight()/(scalingFactor * 2),
                            e.getWidth()/(scalingFactor * 2),
                            (e.getX() - xOffset + e.getWidth()/2)/scalingFactor,
                            (e.getY() - yOffset + e.getHeight()/2)/scalingFactor,
                            90, 
                            f);
        }
    }
    
    private Shape geneToShape(Gene g){
        offset = AffineTransform.getTranslateInstance(xOffset, yOffset);
        scaling = AffineTransform.getScaleInstance(scalingFactor, scalingFactor);
        return offset.createTransformedShape(scaling.createTransformedShape(g.toShape()));
    }
    
    /**
     * returns the active (selected) GAM gene.
     */
    public Gene getActiveGenetic(){
        return activeGenetic;
    }
    
    /**
     * Clears the list of created ellipses.
     */
    public void clearCreated(){
        created.clear();
        activeCreated = nullShape;
        repaint();
    }
    
    /**
     * Clears the list of GAM ellipses.
     */
    public void clearGenetic(){
        activeGenetic = nullGene;
        solutions.clear();
        repaint();
    }
    
    public void loadResults(LoadResultsEvent lre){
        String[][] data = lre.getFile();
        Vector results = new Vector();
        for (int i = 1; i < data.length; i++){
            Gene newGene = new Gene(data[i], f);
            results.add(newGene);
        }
        solutions.add(results);
        //    System.out.println("There are " + solutions.size() + " data sets loaded.");
        repaint();
    }
    
    /**
     * Does all the graphical work through the repaint() method.
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        setBackground(bgColor);
        setForeground(Color.black);
        setOpaque(true);
        Graphics2D g2 = (Graphics2D) g;
        
        scaling = AffineTransform.getScaleInstance(scalingFactor, scalingFactor);
        offset = AffineTransform.getTranslateInstance(xOffset, yOffset);
        
        //Draw the data points.
        BasicStroke stroke = new BasicStroke(1.0f);
        g2.setColor(dpColor);
        g2.setStroke(stroke);
        Iterator it = dataSet.iterator();
        int drawnSize = dpSize;
        while (it.hasNext()){
            DataPoint next = (DataPoint) it.next();
            if (next.getTarget() == 0){
                g2.setColor(dpColor);
                drawnSize = dpSize/2;
                //      System.out.println("Drawing data point at x: " + (scalingFactor * next.getLocation().getX() + xOffset));
                //      System.out.println("                      y: " + (scalingFactor * next.getLocation().getY() + yOffset));
                if (next.getPopulation() > 0){
                    g2.fill(new Ellipse2D.Double(scalingFactor * next.getLocation().getX() + xOffset - drawnSize/2,
                                                 scalingFactor * next.getLocation().getY() + yOffset - drawnSize/2,
                                                 drawnSize,
                                                 drawnSize));   
                }
            } 
        }
        
        Iterator itAgain = dataSet.iterator();
        while (itAgain.hasNext()){
            DataPoint next = (DataPoint) itAgain.next();
            if (next.getTarget() > 0){
                g2.setColor(new Color(200, 0, 200, 50));
                drawnSize = dpSize * Math.max(1, ((int) Math.floor(next.getTarget()/50)));
            
                //      System.out.println("Drawing data point at x: " + (scalingFactor * next.getLocation().getX() + xOffset));
                //      System.out.println("                      y: " + (scalingFactor * next.getLocation().getY() + yOffset));
                g2.fill(new Ellipse2D.Double(scalingFactor * next.getLocation().getX() + xOffset - drawnSize/2,
                                             scalingFactor * next.getLocation().getY() + yOffset - drawnSize/2,
                                             drawnSize,
                                             drawnSize));
            }
        }
        
        //Draw the genetic ellipses (inactive);
        stroke = new BasicStroke(guWidth);
        g2.setStroke(stroke);
        for (int k = 0; k < solutions.size(); k ++){
            if (k < colors.size()){
                g2.setColor((Color) colors.get(k));
            } else {
                g2.setColor(colorRing[k%4]);
            }
            Vector solution = (Vector) solutions.get(k);
            
            Iterator geneIt = solution.iterator();
            while (geneIt.hasNext()){
                Gene next = (Gene) geneIt.next();
                Shape drawn = offset.createTransformedShape(scaling.createTransformedShape(next.toShape()));
                g2.draw(drawn);
            }
        }
        //(re)Draw the active genetic ellipse.
        if (activeGenetic != nullGene){
            stroke = new BasicStroke(gsWidth);
            g2.setStroke(stroke);
            g2.setColor(gsColor);
            g2.draw(offset.createTransformedShape(scaling.createTransformedShape(activeGenetic.toShape())));
            int[] gpoints = activeGenetic.getContainedPoints();
            for (int i = 0; i < gpoints.length; i++){
                if (gpoints[i] != -1){
                    DataPoint next = (DataPoint) dataSet.get(gpoints[i]);
                    g2.fill(new Ellipse2D.Double(scalingFactor * next.getLocation().getX() + xOffset - dpSize/2,
                    scalingFactor * next.getLocation().getY() + yOffset - dpSize/2,
                    dpSize,
                    dpSize));
                }
            }
        }
        
        //Draw the created ellipses (inactive).
        stroke = new BasicStroke(cuWidth);
        g2.setStroke(stroke);
        g2.setColor(cuColor);
        for (int i = 0; i < created.size(); i++){
            g2.draw((Shape) created.get(i));
        }
        
        //(re)Draw the active created ellipse.
        if (activeCreated != nullShape){
            stroke = new BasicStroke(csWidth);
            g2.setStroke(stroke);
            g2.setColor(csColor);
            g2.draw(activeCreated);
            int[] cpoints = activeCreatedGene.getContainedPoints();
            for (int i = 0; i < cpoints.length; i++){
                if (cpoints[i] != -1){
                    DataPoint next = (DataPoint) dataSet.get(cpoints[i]);
                    g2.fill(new Ellipse2D.Double(scalingFactor * next.getLocation().getX() + xOffset - dpSize/2,
                    scalingFactor * next.getLocation().getY() + yOffset - dpSize/2,
                    dpSize,
                    dpSize));
                }
            }
        }
        
        //Draw the border.
        if (drawBorder){
            stroke = new BasicStroke(borderWidth);
            g2.setStroke(stroke);
            g2.setColor(borderColor);
            g2.drawRect((int) Math.max(0, (minX - dpSize - 1) * scalingFactor + xOffset),
                        (int) Math.max(0, (minY - dpSize - 1) * scalingFactor + yOffset),
                        (int) Math.round((maxX - minX) * scalingFactor + dpSize * 4),
                        (int) Math.round((maxY - minY ) * scalingFactor + dpSize * 4));
        }
    }
    
    /**
     * Implementation of the DisplayModeListener interface.  It sets the mouse mode
     * to the specification in the event.
     */
    
    public void setDisplayMode(DisplayModeEvent dme){
        setMode(dme.getMode());
    }
    
    /**
     * Implementation of the NewDataSetListener.  When a new data set is imported,
     * it clears all ellipses and adjusts the display to the new data set.
     */
    
    public void setDataSet(NewDataSetEvent ndse){
        dataSet = ndse.getDataSet();
        clearGenetic();
        clearCreated();
        minX = ndse.getMinX();
        maxX = ndse.getMaxX();
        minY = ndse.getMinY();
        maxY = ndse.getMaxY();
        //calibrate scaling factor and offsets
        double initScalingFactor = 400 / (Math.max(maxY - minY, maxX - minX));
        if (initScalingFactor > 1){
            scalingFactor = (int) Math.round(initScalingFactor);
        } else {
            scalingFactor = (double) Math.max(0.01, (Math.round(initScalingFactor * 100) / 100));
        }
        xOffset = 2 * scalingFactor - scalingFactor * minX;
        yOffset = 2 * scalingFactor - scalingFactor * minY;
        System.out.println("Scaling factor is: " + scalingFactor);
        System.out.println("X offset is: " + xOffset);
        System.out.println("Y offset is: " + yOffset);
        area.width = (int) Math.round(maxX * scalingFactor + xOffset);
        area.height = (int) Math.round(maxY * scalingFactor + yOffset);
        setPreferredSize(area);
        revalidate();
    }
    
    /**
     * Implementation of the ClearCreatedListener interface.  Appropriately enough,
     * it clears all the created ellipses from the display panel.
     */
    
    public void clearCreated(ClearCreatedEvent cce){
        clearCreated();
    }
    
    /**
     * Implementation of the GAMSolutionListener interface.  It changes the display
     * to show new solutions when they arrive.
     */
    
    public void setSolution(GAMSolutionEvent gse){
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        solutions.add(gse.getSolution());
        activeGenetic = nullGene;
        repaint();
    }
    
    /**
     * Implementation of the GAMSettingsListener interface.  It gets the fitness
     * function from the settings, and this fitness function is needed when created
     * ellipses are converted into genes.
     */
    
    public void setGAMSettings(GAMSettingsEvent gse){
        f = gse.getFitnessFunction();
    }
    
    public void removeResults(RemoveResultsEvent rre){
        int index = rre.getIndex();
        System.out.println("Display panel removing set number " + index);
        solutions.remove(index);
        Color c = (Color) colors.get(index);
        colors.remove(index);
        colors.add(c);
        if (index == gamSet){
            gamSet = -1;
            active = -1;
            activeGenetic = nullGene;
        }
        if (index > gamSet){
            gamSet = gamSet - 1;
        }
        repaint();
        fireUpdateGAM(gamSet, active);
    }
    
    public void setColor(ChangeColorEvent cce){
        int i = cce.getIndex();
        if (i < colors.size()){
            colors.set(cce.getIndex(), cce.getColor());
        } else {
            colors.add(cce.getIndex(), cce.getColor());
        }
        repaint();
    }
    
    public void addUpdateCreatedListener(UpdateCreatedListener ucl){
        updateCreatedList.add(UpdateCreatedListener.class, ucl);
    }
    
    public void removeUpdateCreatedListener(UpdateCreatedListener ucl){
        updateCreatedList.remove(UpdateCreatedListener.class, ucl);
    }
    
    public void addUpdateGAMListener(UpdateGAMListener ugl){
        updateGAMList.add(UpdateGAMListener.class, ugl);
    }
    
    public void removeUpdateGAMListener(UpdateGAMListener ugl){
        updateGAMList.remove(UpdateGAMListener.class, ugl);
    }
    
    public void fireUpdateCreated(int active){
        Object[] listeners = updateCreatedList.getListenerList();
        int numListeners = listeners.length;
        UpdateCreatedEvent uce = new UpdateCreatedEvent(this, createdGenes, active);
        for (int i = 0; i < numListeners; i++){
            if (listeners[i]==UpdateCreatedListener.class) {
                // pass the event to the listeners event dispatch method
                ((UpdateCreatedListener)listeners[i+1]).updateCreatedLabels(uce);
            }
        }
    }
    
    public void fireUpdateGAM(int set, int active){
        Object[] listeners = updateGAMList.getListenerList();
        int numListeners = listeners.length;
        UpdateGAMEvent uge = new UpdateGAMEvent(this, set, active);
        for (int i = 0; i < numListeners; i++){
            if (listeners[i]==UpdateGAMListener.class) {
                // pass the event to the listeners event dispatch method
                ((UpdateGAMListener)listeners[i+1]).updateGAMLabels(uge);
            }
        }
    }
    
}


