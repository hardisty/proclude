package geovista.proclude;

/**
 * This class contains the initializer for the GAM.  It reads in a data file and
 * stores data about that file (min and max X and Y and point density).
 * It also creates random genes.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import geovista.common.data.DataSetForApps;
import geovista.readers.csv.CSVParser;
import java.awt.Shape;
import java.io.*;
import java.math.BigDecimal;
import java.util.Vector;
import java.lang.reflect.Array;
import com.vividsolutions.jts.geom.Point;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

public class InitGAMFile implements ActionListener {

  private double minX;
  private double maxX;
  private double minY;
  private double maxY;
  private double density;
  private Vector dataSet;
  private String[][] values;
  private DataSetForApps dsfa;
  private EventListenerList ell = new EventListenerList();

  public InitGAMFile() {
  }

  /**
   * This method reads in a .csv file, assuming the top row of values contains
   * header labels and returns a vector of DataPoints.
   * It passes on FileNotFoundExceptions and IOExceptions from the file reader.
   *
   * It also computes the minimum and maximum values for X and Y as well as the
   * point density for the data set.
   */

  public void processTextFile(File txt) throws FileNotFoundException, IOException{

    dataSet = new Vector();
    FileReader in = new FileReader(txt);
    CSVParser csvp = new CSVParser(in);
    values = csvp.getAllValues();

    //Assuming the top row of values is header labels:

    InitDialog id = new InitDialog();
    id.addActionListener(this);
    id.popUp(values[0]);

  }

  public void processTextFileWithoutDialog(File txt) throws FileNotFoundException, IOException{
    dataSet = new Vector();
    FileReader in = new FileReader(txt);
    CSVParser csvp = new CSVParser(in);
    values = csvp.getAllValues();
    processFile(0, 1, 2, 3, -1);
  }
  
//  private void processFeatureData(InitDialog id){
//    FeatureIterator it = features.features();
//    String pName = id.getBackName();
//    String cName = id.getCaseName();
//    String xName = "";
//    String yName = "";
//    if (id.getUseDefaultGeom()){
//        xName = id.getXName();
//        yName = id.getYName();
//    }
//    dataSet = new Vector();
//    int pop = 0;
//    int count = 0;        
//    if (it.hasNext()){
//        Feature first = it.next();
//        double xValue, yValue;
//        if (id.getUseDefaultGeom()){
//            xValue = first.getDefaultGeometry().getCentroid().getX();
//            yValue = first.getDefaultGeometry().getCentroid().getY();                
//        } else {
//            xValue = ((Number) first.getAttribute(xName)).doubleValue();
//            yValue = ((Number) first.getAttribute(yName)).doubleValue();
//        }
//        int pValue = ((Number) first.getAttribute(pName)).intValue();
//        int cValue = ((Integer) first.getAttribute(cName)).intValue();
//        
//        dataSet.add(new DataPoint(xValue, yValue, pValue, cValue));
//        pop = pop + pValue;
//        count = count + cValue;
//
//        minX = xValue;
//        maxX = xValue;
//        minY = yValue;
//        maxY = yValue;        
//    }
//
//    while(it.hasNext()){
//        Feature next = it.next();
//        double xValue, yValue;
//        if (id.getUseDefaultGeom()){
//            xValue = next.getDefaultGeometry().getCentroid().getX();
//            yValue = next.getDefaultGeometry().getCentroid().getY();                
//        } else {
//            xValue = ((Number) next.getAttribute(xName)).doubleValue();
//            yValue = ((Number) next.getAttribute(yName)).doubleValue();
//        }
//        int pValue = ((Number) next.getAttribute(pName)).intValue();
//        int cValue = ((Number) next.getAttribute(cName)).intValue();
//
//        dataSet.add(new DataPoint(xValue, yValue, pValue, cValue));
//        pop = pop + pValue;
//        count = count + cValue;
//
//        if (xValue < minX)
//            minX = xValue;
//        if (xValue > maxX)
//            maxX = xValue;
//        if (yValue < minY)
//            minY = yValue;
//        if (yValue > maxY)
//            maxY = yValue;
//    }
//
//    density = (double) count / pop;   
//    Iterator dataIt = dataSet.iterator();
//    while (dataIt.hasNext()){
//        DataPoint next = (DataPoint) dataIt.next();
//        next.setDensity(density);
//    }
//    fireAction();          
//      
//  }
  
  private void processDSFA(InitDialog id){
      int backIndex = id.getBackIndex();
      int xIndex = id.getXIndex();
      int yIndex = id.getYIndex();
      int caseIndex = id.getCaseIndex();
      Object[] numericAttributes = dsfa.getDataSetNumeric();
      Shape[] shapes = dsfa.getShapeData();
      dataSet = new Vector();
      
      maxX = Integer.MIN_VALUE;
      minX = Integer.MAX_VALUE;
      maxY = Integer.MIN_VALUE;
      minY = Integer.MAX_VALUE;
      int size;
      try{
          size = ((int[]) numericAttributes[xIndex]).length;
      } catch (ClassCastException cce){
          size = ((double[]) numericAttributes[xIndex]).length;
      }
      double pop = 0;
      double count = 0;
      
      for (int i = 0; i < size; i++){
          double xValue, yValue;
          if (id.getUseDefaultGeom()){
              //use the centroid of the bounding box because there's no easy way to get the centroid of a Shape.
              java.awt.geom.Rectangle2D rect = shapes[i].getBounds2D();
              xValue = (rect.getMaxX() + rect.getMinX()) / 2;
              yValue = (rect.getMaxY() + rect.getMinY()) / 2;              
          } else {          
              try{
                  xValue = ((double[]) numericAttributes[xIndex])[i];
              } catch (ClassCastException cce){
                  xValue = ((int[]) numericAttributes[xIndex])[i];
              }
              try{
                  yValue = ((double[]) numericAttributes[yIndex])[i];
              } catch (ClassCastException cce){
                  yValue = ((int[]) numericAttributes[yIndex])[i];
              }
          }
          int cValue, pValue;
          try{
              cValue = ((int[]) numericAttributes[caseIndex])[i];
          } catch (ClassCastException cce){
              cValue = (int) Math.round(100*((double[]) numericAttributes[caseIndex])[i]);  //100* b/c this is usually a double if there are percentages
          }
          try{
              pValue = ((int[]) numericAttributes[backIndex])[i];
          } catch (ClassCastException cce){
              pValue = (int) Math.round(100*((double[]) numericAttributes[backIndex])[i]);  //100* b/c this is usually a double if there are percentages
          }
          
          pop = pop + pValue;
          count = count + cValue;
          
          if (xValue < minX)
              minX = xValue;
          if (xValue > maxX)
              maxX = xValue;
          if (yValue < minY)
              minY = yValue;
          if (yValue > maxY)
              maxY = yValue;
          
          dataSet.add(new DataPoint(xValue, yValue, pValue, cValue));           
      }
      
      density = (double) count / pop;
    
      Iterator it = dataSet.iterator();
      while (it.hasNext()){
          DataPoint next = (DataPoint) it.next();
          next.setDensity(density);
      }
      fireAction();       
  }
  
  private void processFile(int xIndex, int yIndex, int backIndex, int caseIndex, int expIndex){
    minX = Double.parseDouble(values[1][xIndex]);
    maxX = Double.parseDouble(values[1][xIndex]);
    minY = Double.parseDouble(values[1][yIndex]);
    maxY = Double.parseDouble(values[1][yIndex]);

    int size = values.length;
    long pop = 0;
    long count = 0;
    double totalExp = 0;
    
    for (int i = 1; i < size; i++){
      double xValue = Double.parseDouble(values[i][xIndex]);
      double yValue = Double.parseDouble(values[i][yIndex]);
      int cValue = Integer.parseInt(values[i][caseIndex]);
      if (backIndex == -1){
        double eValue = Double.parseDouble(values[i][expIndex]); 
        dataSet.add(i - 1, new DataPoint(xValue, yValue, cValue, eValue));  
        totalExp = totalExp + eValue;
      } else {
        int pValue = Integer.parseInt(values[i][backIndex]); 
        dataSet.add(i - 1, new DataPoint(xValue, yValue, pValue, cValue));  
        pop = pop + pValue;
      }

      count = count + cValue;
      if (xValue < minX)
        minX = xValue;
      if (xValue > maxX)
        maxX = xValue;
      if (yValue < minY)
        minY = yValue;
      if (yValue > maxY)
        maxY = yValue;
    }
    
    if (backIndex == -1){
        
    } else {
      density = (double) count / pop;
    }
    
    Iterator it = dataSet.iterator();
    while (it.hasNext()){
        DataPoint next = (DataPoint) it.next();
        next.setDensity(density);
    }
//    System.out.println("Population: " + pop);
//    System.out.println("Count: " + count);
//    System.out.println("Density: " + density); 
    fireAction();    
  }
  
  public void actionPerformed(ActionEvent e) {
    System.out.println("InitGAMFile received an event.");
    InitDialog id = (InitDialog) e.getSource();
    if (e.getActionCommand().equalsIgnoreCase("dsfa")){
        processDSFA(id);
    } else {
        int xIndex = id.getXIndex();
        int yIndex = id.getYIndex();
        int backIndex = id.getBackIndex();
        int caseIndex = id.getCaseIndex();
        int expIndex = id.getExpIndex();

        System.out.println("xIndex: " + xIndex);
        System.out.println("yIndex: " + yIndex);
        System.out.println("backIndex: " + backIndex);
        System.out.println("caseIndex: " + caseIndex);
        System.out.println("expIndex: " + expIndex);

        processFile(xIndex, yIndex, backIndex, caseIndex, expIndex);
    }
  }  
  
      public void addActionListener(ActionListener ssl){
          ell.add(ActionListener.class, ssl);
      }

      public void removeActionListener(ActionListener ssl){
          ell.remove(ActionListener.class, ssl);
      }

      public void fireAction(){
          System.out.println("InitGAMFile is firing action.");
          Object[] listeners = ell.getListenerList();
          int numListeners = listeners.length;
          ActionEvent sse = new ActionEvent(this, 42, "NEWDATA");
          for (int i = 0; i < numListeners; i++){
              if (listeners[i]==ActionListener.class) {
                 // pass the event to the listeners event dispatch method
                 ((ActionListener)listeners[i+1]).actionPerformed(sse);
              }              
          }
      }  
      
//  public void setFeatureCollection(FeatureCollection fc){
//      features = fc;
//
//      //need to populate the array names
//      AttributeType[] atts = fc.features().next().getFeatureType().getAttributeTypes();
//      Vector names = new Vector();
//      for (int i = 0; i < atts.length ; i++){
//          if ((atts[i].getType() == Number.class) || (atts[i].getType() == Integer.class) || (atts[i].getType() == Double.class) || (atts[i].getType() == BigDecimal.class)){
//              names.add(atts[i].getName());
//          }
//      }
//      String[] namesArray = new String[names.size()];
//      int index = 0;
//      Iterator it = names.iterator();
//      while (it.hasNext()){
//          namesArray[index] = (String) it.next();
//          index++;
//      }
//      
//      //pop up dialog
//      InitDialog id = new InitDialog();
//      id.addActionListener(this);
//      id.setFromFeatureCollection(true);
//      id.popUp(namesArray);     
//  }
//  
//  public Vector initForInfoVis(FeatureCollection fc, String code){
//      dataSet = new Vector();
//      int pop = 0;
//      int count = 0;
//      FeatureIterator it = fc.features();
//
//      //Will assume there are attributes "centre", "population", and "target" of types com.vivid....Point, Integer, Integer
//      
////      System.out.println("Creating the data set from the feature collection now.");
//      
//      if (it.hasNext()){
//        Feature first = it.next();  
//        
//        double xValue = ((Point) first.getDefaultGeometry()).getX();
//        double yValue = ((Point) first.getDefaultGeometry()).getY();
//        
////        double xValue = ((Point) first.getAttribute("centre")).getX();
////        double yValue = ((Point) first.getAttribute("centre")).getY();
//        
//        int pValue = ((Number) first.getAttribute("Bkgd")).intValue();
//        int cValue = ((Number) first.getAttribute(code)).intValue();
//        
//        dataSet.add(new DataPoint(xValue, yValue, pValue, cValue));
//        pop = pop + pValue;
//        count = count + cValue;
//        
//        minX = xValue;
//        maxX = xValue;
//        minY = yValue;
//        maxY = yValue;
//      }
//      
//      while(it.hasNext()){
//        Feature next = it.next();
//                
//        double xValue = ((Point) next.getDefaultGeometry()).getX();
//        double yValue = ((Point) next.getDefaultGeometry()).getY();
//        
////        double xValue = ((Point) next.getAttribute("centre")).getX();
////        double yValue = ((Point) next.getAttribute("centre")).getY();
//        
//        int pValue = ((Number) next.getAttribute("Bkgd")).intValue();
//        int cValue = ((Number) next.getAttribute(code)).intValue();
//        
//        dataSet.add(new DataPoint(xValue, yValue, pValue, cValue));
//        pop = pop + pValue;
//        count = count + cValue;
//      
//        if (xValue < minX)
//            minX = xValue;
//        if (xValue > maxX)
//            maxX = xValue;
//        if (yValue < minY)
//            minY = yValue;
//        if (yValue > maxY)
//            maxY = yValue;
//      }
//      
//      density = (double) count / pop;
//      return dataSet;        
//  }
  
  public void setDataVector(Vector v){
    int pop = 0;
    int count = 0;  
    minX = ((DataPoint) v.get((0))).getLocation().getX();
    maxX = ((DataPoint) v.get((0))).getLocation().getX();
    minY = ((DataPoint) v.get((0))).getLocation().getY();
    maxY = ((DataPoint) v.get((0))).getLocation().getY();
    dataSet = v;
    Iterator it = v.iterator();
    while (it.hasNext()){
        DataPoint next = (DataPoint) it.next();
        double xValue = next.getLocation().getX();
        double yValue = next.getLocation().getY();
        int popValue = next.getPopulation();
        int caseValue = next.getTarget();
        pop = pop + popValue;
        count = count + caseValue;
        if (xValue < minX)
            minX = xValue;
        if (xValue > maxX)
            maxX = xValue;
        if (yValue < minY)
            minY = yValue;
        if (yValue > maxY)
            maxY = yValue;                
    }
    density = (double) count / pop;        
  }
  
  /**
   * Processes an Object[] in the format used by DataSetForApps
   */

  public void processDataSetForApps(DataSetForApps data){
      dsfa = data;
      String[] names = dsfa.getAttributeNamesNumeric();
      InitDialog id = new InitDialog();
      id.addActionListener(this);
      id.setFromDataSetForApps(true);
      id.popUp(names);
  }

  public Vector processDoubleArray(double[][] data){
      //this assumes there are n rows by four columns: x, y, pop, and count, in that order.
      dataSet = new Vector();
      int size = data.length;
      int pop = 0;
      int count = 0;      
      for (int i = 0; i < size; i++){          
          double xValue = data[i][0];
          double yValue = data[i][1];
          int pValue = (int) Math.round(data[i][2]);
          int cValue = (int) Math.round(data[i][3]);
          dataSet.add(i, new DataPoint(xValue, yValue, pValue, cValue));
          pop = pop + pValue;
          count = count + cValue;
          if (xValue < minX)
            minX = xValue;
          if (xValue > maxX)
            maxX = xValue;
          if (yValue < minY)
            minY = yValue;
          if (yValue > maxY)
            maxY = yValue;
      }
      density = (double) count / pop;
      
      Iterator it = dataSet.iterator();
      while (it.hasNext()){
          DataPoint next = (DataPoint) it.next();
          next.setDensity(density);
      }      
      
      return dataSet;      
  }
  
  /**
   * This method creates a random gene, given a fitness function to use and the
   * minimum permitted radius size.  The maximum radius size is set at 1/8 the
   * difference between maximum and minimum X or Y values (X for horizontal radius
   * and Y for vertical radius).
   */

  public Gene createRandomGene(Fitness ff, double minRadius, double maxRadius){
    double r1 = (Math.random() * (maxRadius - minRadius)) + minRadius;
    double r2 = (Math.random() * (maxRadius - minRadius)) + minRadius;
    double major = Math.max(r1, r2);
    double minor = Math.min(r1, r2);
    double orientation = Math.random() * 360;
    double x = (Math.random() * (maxX - minX)) + minX;
    double y = (Math.random() * (maxY - minY)) + minY;
    return new Gene(major, minor, x, y, orientation, ff);
  }

  /**
   * Returns a population of random genes.
   */

  public Vector getGenes(int popSize, Fitness ff, double minRadius, double maxRadius){

    Vector geneSet = new Vector();
    for (int q = 0; q < popSize; q++){		//Initialize population
      geneSet.add(q, createRandomGene(ff, minRadius, maxRadius));
    }

    return geneSet;
  }

  /**
   * Returns the data set.
   */

  public Vector getDataSet(){
    return dataSet;
  }

  /**
   * Returns the minimum value of X.
   */

  public double getMinX(){
    return minX;
  }

  /**
   * Returns the maximum value of X.
   */

  public double getMaxX(){
    return maxX;
  }

  /**
   * Returns the minimum value of Y.
   */

  public double getMinY(){
    return minY;
  }

  /**
   * Returns the maximum value of Y.
   */

  public double getMaxY(){
    return maxY;
  }

  /**
   * Returns the point density of the data set.
   */

  public double getDensity(){
    return density;
  }
  
  private class InitDialog extends JFrame implements ActionListener{
      
      private int xIndex, yIndex, caseIndex;
      private String xName, yName, caseName, expName, backName;
      private int expIndex = -1;
      private int backIndex = -1;
      private JComboBox xBox, yBox, backBox, caseBox, expBox;
      private JCheckBox check, geom;
      private EventListenerList ell = new EventListenerList();
      private boolean fromDataSetForApps = false;
      
      public InitDialog(){          
      }
      
      /**
       * Pop up a dialog that asks the user for the x, y, background rate, and case rate variables (expressions?)
       */      
      public void popUp(String[] names){
          xBox = new JComboBox(names);
          yBox = new JComboBox(names);
          backBox = new JComboBox(names);
          caseBox = new JComboBox(names);          
          expBox = new JComboBox(names);   
          expBox.setEnabled(false);
          JLabel xLabel = new JLabel("X (or latitude) value");
          JLabel yLabel = new JLabel("Y (or longitude) value");
          JLabel backLabel = new JLabel("Background population");
          JLabel caseLabel = new JLabel ("Actual number of cases");
          JLabel expLabel = new JLabel("Expected number of cases");
          check = new JCheckBox("Use expected rate (untested)");
          check.setEnabled(false);
          geom = new JCheckBox("Use default geometry");
          if (fromDataSetForApps){
              geom.setEnabled(true);
          } else {
              geom.setEnabled(false);
          }
          JButton ready = new JButton("Done");
          this.getContentPane().setLayout(new GridLayout(0, 2, 5, 0));
          this.getContentPane().add(xLabel);
          this.getContentPane().add(xBox);
          this.getContentPane().add(yLabel);
          this.getContentPane().add(yBox);
          this.getContentPane().add(backLabel);
          this.getContentPane().add(backBox);
          this.getContentPane().add(caseLabel);
          this.getContentPane().add(caseBox);
          this.getContentPane().add(expLabel);
          this.getContentPane().add(expBox);
          this.getContentPane().add(check);
          this.getContentPane().add(geom);
          this.getContentPane().add(ready);
          ready.addActionListener(this);
          ready.setActionCommand("DONE");
          check.addActionListener(this);
          check.setActionCommand("CHECK");
          geom.addActionListener(this);
          geom.setActionCommand("GEOM");
          this.pack();
          this.setVisible(true);
      }
      
      public void setFromDataSetForApps(boolean b){
          fromDataSetForApps = b;
      }
      
      public int getXIndex(){
          return xIndex;
      }
      
      public String getXName(){
          return xName;
      }
      
      public int getYIndex(){
          return yIndex;
      }
      
      public String getYName(){
          return yName;
      }
      
      public int getBackIndex(){
          return backIndex;
      }
      
      public String getBackName(){
          return backName;
      }
      
      public int getCaseIndex(){
          return caseIndex;
      }
      
      public String getCaseName(){
          return caseName;
      }
      
      public int getExpIndex(){
          return expIndex;
      }
      
      public String getExpName(){
          return expName;
      }
      
      public boolean getUseDefaultGeom(){
          return geom.isSelected();
      }
      
      public void addActionListener(ActionListener ssl){
          ell.add(ActionListener.class, ssl);
      }

      public void removeActionListener(ActionListener ssl){
          ell.remove(ActionListener.class, ssl);
      }

      public void fireAction(){
          Object[] listeners = ell.getListenerList();
          int numListeners = listeners.length;
          String com;
          if (fromDataSetForApps){
              com = "dsfa";
          } else {
              com = "non_dsfa";
          }
          ActionEvent sse = new ActionEvent(this, 42, com);
          for (int i = 0; i < numListeners; i++){
              if (listeners[i]==ActionListener.class) {
                 // pass the event to the listeners event dispatch method
                 ((ActionListener)listeners[i+1]).actionPerformed(sse);
              }              
          }
      }
      
      public void actionPerformed(ActionEvent e) {
          if (e.getActionCommand().equals("DONE")){
              xIndex = xBox.getSelectedIndex();
              xName = xBox.getSelectedItem().toString();
              yIndex = yBox.getSelectedIndex();
              yName = yBox.getSelectedItem().toString();
              caseIndex = caseBox.getSelectedIndex();
              caseName = caseBox.getSelectedItem().toString();
              if (check.isSelected()){
                  expIndex = expBox.getSelectedIndex();
                  expName = expBox.getSelectedItem().toString();                  
                  backIndex = -1;
                  backName = "not used";
              } else {
                  expIndex = -1;
                  expName = "not used";
                  backIndex = backBox.getSelectedIndex();
                  backName = backBox.getSelectedItem().toString();
              }
              this.fireAction();
              this.setVisible(false);
              this.dispose();
          } else if (e.getActionCommand().equals("CHECK")){  
              if (check.isSelected()){
                  expBox.setEnabled(true);
                  backBox.setEnabled(false);
              } else {
                  expBox.setEnabled(false);
                  backBox.setEnabled(true);
              }
          } else {  //action command = "GEOM"
              if (geom.isSelected()){
                  xBox.setEnabled(false);
                  yBox.setEnabled(false);
              } else {
                  xBox.setEnabled(true);
                  yBox.setEnabled(true);
              }
          }
      }
      
  }
  
  
}