package geovista.proclude;

/**
 * This class contains the output statistics panel of the GUI.
 * Now in a form that Studio can convert into a bean.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import geovista.common.event.DataSetListener;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.util.Iterator;
import java.io.*;
import javax.swing.event.EventListenerList;
import geovista.common.event.DataSetEvent;
import java.awt.event.*;
import com.vividsolutions.jts.geom.*;

public class OutputPanel extends JScrollPane implements NewDataSetListener,
                                                        ClearCreatedListener,
                                                        GAMSolutionListener,
                                                        GAMSettingsListener,
                                                        UpdateCreatedListener,
                                                        UpdateGAMListener,
                                                        LoadResultsListener,
                                                        ActionListener,
                                                        Serializable{

  private JScrollPane createdPanel;
  private Vector gamPanels; //Vector of JScrollPanes
  private JPanel createdLabels, entireView;
  private Vector gamLabels; //Vector of JPanels
  private Vector solutions, created;
  private int gamSet, gamIndex, createdIndex;
  private Fitness fitnessFunction;
  private EventListenerList outputList;
  private EventListenerList removeList = new EventListenerList();

  public OutputPanel() {
    entireView = new JPanel();
    addOutputWidgets();
    solutions = new Vector();
    created = new Vector();
    gamLabels = new Vector();
    createdLabels = new JPanel();
    setViewportView(entireView);
    fitnessFunction = new FitnessDensity(new Vector());
    outputList = new EventListenerList();
//    this.setMaximumSize(new Dimension(2, 100));
    gamSet = -1;
    gamIndex = -1;
  }

  /**
   * This method initializes the output panel ("Output stats" tab).
   */

  private void addOutputWidgets(){
    //Create the scroll panels

    createdPanel = new JScrollPane();
    gamPanels = new Vector();

    //Put the scroll panels in the output area

    entireView.setLayout(new FlowLayout());
    entireView.add(createdPanel);
    for (int i = 0; i < gamPanels.size(); i++){
      JScrollPane nextPane = (JScrollPane) gamPanels.get(i);
      entireView.add(nextPane);
    }
    this.add(entireView);
  }

  private double roundToHundredths(double d){
    return ((double) Math.round(d * 100))/100;
  }

  /**
   * Converts an ellipse to an array of labels containing the statistics about
   * that ellipse.
   */

  private JLabel[] geneToLabels(Gene g){
    JLabel[] labels = new JLabel[9];
    labels[0] = new JLabel("Center X: " + roundToHundredths(g.getX()));
    labels[1] = new JLabel("Center Y: " + roundToHundredths(g.getY()));
    labels[2] = new JLabel("Major Radius: " + roundToHundredths(g.getMajorAxisRadius()));
    labels[3] = new JLabel("Minor Radius: " + roundToHundredths(g.getMinorAxisRadius()));
    labels[4] = new JLabel("Orientation: " + roundToHundredths(g.getOrientation()));
    labels[5] = new JLabel("Area: " + roundToHundredths(Math.PI * g.getMajorAxisRadius() * g.getMinorAxisRadius()));
    labels[6] = new JLabel("Population: " + g.getPopulation());
    labels[7] = new JLabel("Count: " + g.getCount());
    labels[8] = new JLabel("Fitness: " + roundToHundredths(g.getFitness()));
    labels[8].setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
    labels[0].setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
    return labels;
  }

  /**
   * This method updates the statistics of the GAM ellipses in the "Output stats" panel.
   *
   * It takes the list of genes from the GAM and an integer denoting which gene is selected
   * (-1 if none is selected), and prints the results in the GAM ellipse section
   * of the "Output stats" panel
   */

  public void updateGAMLabels(Vector genes, int set, int index){
    for (int j = 0; j < gamPanels.size(); j++){
      entireView.remove((JScrollPane) gamPanels.get(j));
    }
    for (int k = 0; k < genes.size(); k++){
      Vector nextGenes = (Vector) genes.get(k);
      JScrollPane newScrollPanel = new JScrollPane();
      JButton removeButton = new JButton("Remove");
      JButton saveButton = new JButton("Save");
      JButton colorButton = new JButton("Set color");
      removeButton.setActionCommand("" + k);
      removeButton.addActionListener(this);
      saveButton.setActionCommand("SAVE" + k);
      saveButton.addActionListener(this);
      colorButton.setActionCommand("COLOR" + k);
      colorButton.addActionListener(this);
      Iterator it = nextGenes.iterator();
      Color darkGreen = Color.green.darker();
      JPanel newLabels = new JPanel();
      newLabels.setLayout(new BoxLayout(newLabels, BoxLayout.Y_AXIS));
      newLabels.add(removeButton);
      newLabels.add(saveButton);
      newLabels.add(colorButton);
      int q = 0;
      while (it.hasNext()){
        Gene next = (Gene) it.next();
        JLabel[] labels = geneToLabels(next);
        if ((q == index) && (k == set)){
          for (int x = 0; x < 9; x++){
            labels[x].setForeground(darkGreen);
          }
        }
        for (int x = 0; x < 9; x++){
          newLabels.add(labels[x]);
        }
        q++;
      }
      newScrollPanel.setViewportView(newLabels);
      gamPanels.add(newScrollPanel);
      entireView.add(newScrollPanel);
    }

//    this.setMaximumSize(new Dimension(2, 100));
    revalidate();
    repaint();
  }

  /**
   * This method updates the list of created ellipses in the "Output stats" panel.
   * It works much the same way as updateGeneLabels.
   */

  public void updateCreatedLabels(Vector genes, int i){
    createdLabels = new JPanel();
    createdLabels.setLayout(new BoxLayout(createdLabels, BoxLayout.Y_AXIS));

    for (int k = genes.size() - 1; k >= 0; k--){
      Gene next = (Gene) genes.get(k);
      JLabel[] labels = geneToLabels(next);
      if (k == i){
        for (int x = 0; x < 9; x++){
          labels[x].setForeground(Color.red);
        }
      }
      for (int x = 0; x < 9; x++){
        createdLabels.add(labels[x]);
      }
    }
    createdPanel.setViewportView(createdLabels);
    revalidate();
    repaint();
  }

  public void loadResults(LoadResultsEvent lre){
    String[][] data = lre.getFile();
    Vector genes = new Vector();
    for (int i = 1; i < data.length; i++){
      Gene newGene = new Gene(data[i], fitnessFunction);
      genes.add(newGene);
    }
    solutions.add(genes);
    updateGAMLabels(solutions, gamSet, gamIndex);
    fireDataSetChanged(assembleOutput(genes));
  }

  /**
   * Implementation of the new data set listener interface.  It clears the GAM
   * and created statistics whenever a new data set is imported (although this
   * object does not actually get the new data set).
   */

  public void setDataSet(NewDataSetEvent ndse){
    solutions.clear();
    gamIndex = -1;
    for (int i = 0; i < gamLabels.size(); i++){
      entireView.remove((JPanel) gamLabels.get(i));
    }
    created.clear();
    createdIndex = -1;
    createdLabels.removeAll();
    repaint();
  }

  /**
   * Implementation of the clear created listener interface.  It clears the created
   * statistics whenever this event is fired.
   */

  public void clearCreated(ClearCreatedEvent cce){
    createdIndex = -1;
    created.clear();
    createdLabels.removeAll();
    repaint();
  }

  /**
   * Implementation of the GAM solution listener interface.  It updates the GAM
   * statistics whenever a new solution arrives.
   */

  public void setSolution(GAMSolutionEvent gse){
    solutions.add(gse.getSolution());
    updateGAMLabels(solutions, gamSet, gamIndex);
    Object[] output = assembleOutput(gse.getSolution());
    fireDataSetChanged(output);
  }

  /**
   * Implementation of the GAM settings listener interface.  It uses the fitness
   * function to update the created ellipses' fitness statistics.
   */

  public void setGAMSettings(GAMSettingsEvent gse){
    fitnessFunction = gse.getFitnessFunction();
    for (int i = 0; i < created.size(); i++){
      ((Gene)created.get(i)).setFunction(fitnessFunction);
    }
    updateCreatedLabels(created, createdIndex);
  }

  /**
   * Implementation of the Update created listener interface.  It indicates that
   * either a new ellipse has been created, or a different created ellipse has
   * either been selected as the active one or has been moved.
   */

  public void updateCreatedLabels(UpdateCreatedEvent uce){
    Vector createdGenes = uce.getCreatedList();
    createdIndex = uce.getActiveIndex();
    created = createdGenes;
    for (int i = 0; i < created.size(); i++){
      ((Gene)created.get(i)).setFunction(fitnessFunction);
    }
    updateCreatedLabels(created, createdIndex);
  }

  /**
   * Implementation of the update GAM listener interface.  It indicates that a
   * different GAM ellipse has been selected as the active one.
   */

  public void updateGAMLabels(UpdateGAMEvent uge){
    gamIndex = uge.getActiveIndex();
    gamSet = uge.getActiveSet();
//    System.out.println("Output panel: active set = " + gamSet + ": active index = " + gamIndex);
    updateGAMLabels(solutions, gamSet, gamIndex);
  }

  private Object[] assembleOutput(Vector solution){
    Object[] output = new Object[9];
    output[0] = new String[]{"X", "Y", "Major Radius", "Minor Radius", "Orientation", "Population", "Count", "Fitness"};
    int x = 0;
    double[] xArray = new double[solution.size()];
    double[] yArray = new double[solution.size()];
    double[] majArray = new double[solution.size()];
    double[] minArray = new double[solution.size()];
    double[] orArray = new double[solution.size()];
    double[] popArray = new double[solution.size()];
    double[] ctArray = new double[solution.size()];
    double[] fitArray = new double[solution.size()];
    Iterator it = solution.iterator();
    while (it.hasNext()){
      Gene next = (Gene) it.next();
      xArray[x] = next.getX();
      yArray[x] = next.getY();
      majArray[x] = next.getMajorAxisRadius();
      minArray[x] = next.getMinorAxisRadius();
      orArray[x] = next.getOrientation();
      popArray[x] = next.getPopulation();
      ctArray[x] = next.getCount();
      fitArray[x] = next.getFitness();
      x = x + 1;
    }
    output[1] = xArray;
    output[2] = yArray;
    output[3] = majArray;
    output[4] = minArray;
    output[5] = orArray;
    output[6] = popArray;
    output[7] = ctArray;
    output[8] = fitArray;
    return output;
  }

  public void saveToFile(String fileName){
      File f = new File(fileName);
      Vector solution = (Vector) solutions.get(0);
      try{
          FileWriter outWriter = new FileWriter(f);
          Iterator it = solution.iterator();
          while (it.hasNext()){
              Gene next = (Gene) it.next();
              outWriter.write(next.toCSVString());
          }
          System.out.println("save successful for " + fileName);
          outWriter.close();
      }
      catch(IOException e) {
        System.out.println("error saving " + fileName + ": " + e.getMessage());
        e.printStackTrace();
      }          
 
  }
  
  private void saveAction(int index){
    Vector solution = (Vector) solutions.get(index);
    JFileChooser jfc = new JFileChooser();
    jfc.setMultiSelectionEnabled(false);
    jfc.setFileFilter(new TextFilter());

    int returnVal = jfc.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION){
      File f = jfc.getSelectedFile();
      String out = "Solution: " + '\n';
      Iterator it = solution.iterator();
      while (it.hasNext()){
        Gene next = (Gene) it.next();
        out = out + next.toCSVString();
      }

      //save out string to the file

      try{
        FileWriter outWriter = new FileWriter(f);
        int l = out.length();
        for (int i = 0; i < l; i++){
          outWriter.write(out.charAt(i));
        }
        JOptionPane.showMessageDialog(this,
                                      "Save was successful.  Saved file to " + f.getName(),
                                      "Save successful",
                                      JOptionPane.INFORMATION_MESSAGE);
        outWriter.close();
      }
      catch(IOException e) {
        JOptionPane.showMessageDialog(this,
                                      "Save failed.  Sorry for the inconvenience." + e.getMessage(),
                                      "Error",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }   //End if (returnVal == JFileChooser.APPROVE_OPTION){...

  }

  private void colorAction(int i){
    Color c = JColorChooser.showDialog(this,
                                       "Choose a color for set #" + i,
                                       DisplayPanel.colorRing[i%4]);
    fireChangeColor(c, i);
  }

  public void actionPerformed(ActionEvent e){
    if (e.getActionCommand().startsWith("SAVE")){
      int index = Integer.parseInt(e.getActionCommand().substring(4));
//      System.out.println("User requested to save set number " + index);
      saveAction(index);
    } else if (e.getActionCommand().startsWith("COLOR")){
      int index = Integer.parseInt(e.getActionCommand().substring(5));
//      System.out.println("User requested to change the color of set number " + index);
      colorAction(index);
    } else {
      int index = Integer.parseInt(e.getActionCommand());
//      System.out.println("User requested to remove set number " + index);
      entireView.remove((JScrollPane) gamPanels.get(index));
      solutions.remove(index);
      gamPanels.remove(index);

      fireRemoveResults(index);
    }
  }

//  public FeatureCollection getOutputFC(){
//    System.out.println("Output panel creating a feature collection.");
//    FeatureCollection fc = FeatureCollections.newCollection();
//    try{
//      com.vividsolutions.jts.geom.GeometryFactory geomFac = new com.vividsolutions.jts.geom.GeometryFactory();
//      java.util.ArrayList features = new java.util.ArrayList();
//
//      //These are all "point attributes".  I don't really want this, do I?
//      org.geotools.feature.AttributeType[] pointAttribute = new org.geotools.feature.AttributeType[9];
//      pointAttribute[0] = AttributeTypeFactory.newAttributeType("centre", com.vividsolutions.jts.geom.Point.class);
//      pointAttribute[1] = AttributeTypeFactory.newAttributeType("horiz. radius",Double.class);
//      pointAttribute[2] = AttributeTypeFactory.newAttributeType("vert. radius",Double.class);
//      pointAttribute[3] = AttributeTypeFactory.newAttributeType("x", Double.class);
//      pointAttribute[4] = AttributeTypeFactory.newAttributeType("y", Double.class);
//      pointAttribute[5] = AttributeTypeFactory.newAttributeType("orientation", Double.class);
//      pointAttribute[6] = AttributeTypeFactory.newAttributeType("population", Integer.class);
//      pointAttribute[7] = AttributeTypeFactory.newAttributeType("count", Integer.class);
//      pointAttribute[8] = AttributeTypeFactory.newAttributeType("fitness", Double.class);
//
//      org.geotools.feature.FeatureType pointType = org.geotools.feature.FeatureTypeBuilder.newFeatureType(pointAttribute,"testPoint");
//      
//      Vector mostRecent = (Vector) solutions.lastElement();
//      for(int j = 0; j < mostRecent.size(); j++){
//      // values[j][0] = x.  values[j][1] = y.  values[j][2] = population.  values[j][3] = target.
//          double x = ((Gene) mostRecent.get(j)).getX();
//          double y = ((Gene) mostRecent.get(j)).getY();
//          com.vividsolutions.jts.geom.Point point = makeSamplePoint(geomFac, x, y);
//          double r1 = ((Gene) mostRecent.get(j)).getMajorAxisRadius();
//          double r2 = ((Gene) mostRecent.get(j)).getMinorAxisRadius();
//          double o = ((Gene) mostRecent.get(j)).getOrientation();
//          int population = ((Gene) mostRecent.get(j)).getPopulation();
//          int count = ((Gene) mostRecent.get(j)).getCount();
//          double fitness = ((Gene) mostRecent.get(j)).getFitness();
//          org.geotools.feature.Feature pointFeature = pointType.create(new Object[]{point,
//                                                                       new Double(r1),
//                                                                       new Double(r2),
//                                                                       new Double(x),
//                                                                       new Double(y),
//                                                                       new Double(o),
//                                                                       new Integer(population),
//                                                                       new Integer(count),
//                                                                       new Double(fitness)},""+j);
//          features.add(pointFeature);
////      System.out.println(pointFeature);
//      }
//      fc.addAll(features);
//    }
//    //Do something with these exceptions!!!!!
//    catch (SchemaException se){
//          JOptionPane.showMessageDialog(this,
//                                        se.getMessage(),
//                                        "Schema Exception",
//                                        JOptionPane.ERROR_MESSAGE);
//    }
//    catch (IllegalAttributeException iae){
//          JOptionPane.showMessageDialog(this,
//                                        iae.getMessage(),
//                                        "Illegal Argument Exception",
//                                        JOptionPane.ERROR_MESSAGE);        
//    }
//
//    return fc;
//  }
  
      
  private com.vividsolutions.jts.geom.Point makeSamplePoint(final com.vividsolutions.jts.geom.GeometryFactory geomFac, double x, double y) {
    com.vividsolutions.jts.geom.Coordinate c = new com.vividsolutions.jts.geom.Coordinate(x,y);
    com.vividsolutions.jts.geom.Point point = geomFac.createPoint(c);
    return point;
  }
  
  public void addDataSetListener(DataSetListener ol){
    outputList.add(DataSetListener.class, ol);
  }

  public void removeDataSetListener(DataSetListener ol){
    outputList.remove(DataSetListener.class, ol);
  }

  public void fireDataSetChanged(Object[] output){
    System.out.println("Output panel firing data.");
    Object[] listeners = outputList.getListenerList();
    int numListeners = listeners.length;
    DataSetEvent dse = new DataSetEvent(this, output);
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==DataSetListener.class) {
        // pass the event to the listeners event dispatch method
        ((DataSetListener)listeners[i+1]).dataSetChanged(dse);
      }
    }
  }

  public void addRemoveResultsListener(RemoveResultsListener rrl){
    removeList.add(RemoveResultsListener.class, rrl);
  }

  public void removeRemoveResultsListener(RemoveResultsListener rrl){
    removeList.remove(RemoveResultsListener.class, rrl);
  }

  public void fireRemoveResults(int index){
    Object[] listeners = removeList.getListenerList();
    int numListeners = listeners.length;
    RemoveResultsEvent rre = new RemoveResultsEvent(this, index);
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==RemoveResultsListener.class) {
        // pass the event to the listeners event dispatch method
        ((RemoveResultsListener)listeners[i+1]).removeResults(rre);
      }
    }
  }

  public void addChangeColorListener(ChangeColorListener ccl){
    removeList.add(ChangeColorListener.class, ccl);
  }

  public void removeChangeColorListener(ChangeColorListener ccl){
    removeList.remove(ChangeColorListener.class, ccl);
  }

  public void fireChangeColor(Color color, int index){
    Object[] listeners = removeList.getListenerList();
    int numListeners = listeners.length;
    ChangeColorEvent cce = new ChangeColorEvent(this, color, index);
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==ChangeColorListener.class) {
        // pass the event to the listeners event dispatch method
        ((ChangeColorListener)listeners[i+1]).setColor(cce);
      }
    }
  }

}



