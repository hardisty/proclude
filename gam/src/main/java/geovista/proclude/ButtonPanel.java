package geovista.proclude;

/**
 * This class contains the button panel at the top of the GUI.
 * Now a Studio-friendly bean.
 *
 * @author Jamison Conley
 * @version 2.0
 */


import geovista.readers.csv.CSVParser;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Vector;
import java.io.*;

public class ButtonPanel extends JPanel implements ActionListener,
                                                   Serializable{

  private EventListenerList displayModeList = new EventListenerList();
  private EventListenerList newDataSetList = new EventListenerList();
  private EventListenerList clearCreatedList = new EventListenerList();
  private EventListenerList GAMSaveList = new EventListenerList();
  private EventListenerList runGAMList = new EventListenerList();
  private EventListenerList defaultSettingsList = new EventListenerList();
  private EventListenerList loadSettingsList = new EventListenerList();
  private EventListenerList saveSettingsList = new EventListenerList();
  private EventListenerList loadResultList = new EventListenerList();
  private JButton runButton, defaultButton, clearButton, importButton,
          loadSettingsButton, saveSettingsButton, loadResultsButton;
  private JPanel radioPanel;
  private JRadioButton[] radioButtons1 = new JRadioButton[2];
  private ButtonGroup radioGroup1 = new ButtonGroup();

  public ButtonPanel() {
    addButtonWidgets();
  }

  /**
   * This method initializes the button panel (the top row of buttons with "RUN", "Compute Fitness"...
   */

  private void addButtonWidgets(){
    //Create run and compute buttons
    runButton = new JButton("RUN");
    runButton.setActionCommand("RUN");
    defaultButton = new JButton("Restore to defaults");
    defaultButton.setActionCommand("DEFAULT");
    clearButton = new JButton("Clear created ellipses");
    clearButton.setActionCommand("CLEAR");
//    saveButton = new JButton("Save Results");
//    saveButton.setActionCommand("SAVE");
    importButton = new JButton("Import data set");
    importButton.setActionCommand("IMPORT");
    loadSettingsButton = new JButton("Load Alg'm Settings");
    loadSettingsButton.setActionCommand("LOAD");
    saveSettingsButton = new JButton("Save Settings");
    saveSettingsButton.setActionCommand("SAVESET");
    loadResultsButton = new JButton("Load results");
    loadResultsButton.setActionCommand("LOADRES");

    radioPanel = new JPanel();
    radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));

    radioButtons1[0] = new JRadioButton("Draw ellipse.");
    radioButtons1[0].setActionCommand("mode1");
    radioGroup1.add(radioButtons1[0]);
    radioButtons1[0].setSelected(true);

    radioButtons1[1] = new JRadioButton("Select PROCLUDE ellipse.");
    radioButtons1[1].setActionCommand("mode2");
    radioGroup1.add(radioButtons1[1]);

    //Create appropriate layout
    setLayout(new FlowLayout());

    //Put things in the right places in that layout

    radioPanel.add(radioButtons1[0]);
    radioPanel.add(radioButtons1[1]);

    add(radioPanel);
    add(runButton);
    add(defaultButton);
    add(clearButton);
    add(importButton);
    add(loadResultsButton);
    add(loadSettingsButton);
//    add(saveButton);
    add(saveSettingsButton);

    //Listen to events from the buttons
    runButton.addActionListener(this);
    defaultButton.addActionListener(this);
    clearButton.addActionListener(this);
//    saveButton.addActionListener(this);
    radioButtons1[0].addActionListener(this);
    radioButtons1[1].addActionListener(this);
    importButton.addActionListener(this);
    loadSettingsButton.addActionListener(this);
    saveSettingsButton.addActionListener(this);
    loadResultsButton.addActionListener(this);
  }

  /**
   * Determines what to do when a button is pressed.  This ends in
   * firing an event, although some (like the IMPORT action) require some work to
   * be done before the event is fired.
   */

  public void actionPerformed(ActionEvent event){
    if ("mode1".equals(event.getActionCommand())){
      fireDisplayMode(1);
    } else if ("mode2".equals(event.getActionCommand())){
      fireDisplayMode(2);
    } else if ("IMPORT".equals(event.getActionCommand())){
      importFileAction();
//    } else if ("SAVE".equals(event.getActionCommand())){
//      fireGAMSave();
    } else if ("DEFAULT".equals(event.getActionCommand())){
      fireDefaultSettings();
    } else if ("CLEAR".equals(event.getActionCommand())){
      fireClearCreated();
    } else if ("LOAD".equals(event.getActionCommand())){
      fireLoadSettings();
    } else if ("SAVESET".equals(event.getActionCommand())){
      fireSaveSettings();
    } else if ("LOADRES".equals(event.getActionCommand())){
      loadResultsAction();
    } else if ("RUN".equals(event.getActionCommand())){    
      fireRunGAM();
    } else {    //action command is "NEWDATA"
        System.out.println("changing the dataset.");
        InitGAMFile initializer = (InitGAMFile) event.getSource();
        Vector dataSet = initializer.getDataSet();
        double minX = initializer.getMinX();
        double minY = initializer.getMinY();
        double maxX = initializer.getMaxX();
        double maxY = initializer.getMaxY();
        double density = initializer.getDensity();
        fireNewDataSet(dataSet, minX, minY, maxX, maxY, density, initializer);       
    }
  }

  /**
   * Imports the file selected in the file chooser, and fires a new data set event.
   */
  private void importFileAction(){
    JFileChooser jfc = new JFileChooser();
    jfc.setMultiSelectionEnabled(false);
    jfc.setFileFilter(new TextFilter());
    try{
      int returnVal = jfc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION){
        File f = jfc.getSelectedFile();
        InitGAMFile initializer = new InitGAMFile();
        initializer.addActionListener(this);
        initializer.processTextFile(f);
      }
    }
    catch(FileNotFoundException fnfe){
      JOptionPane.showMessageDialog(this,
                                    fnfe.getMessage(),
                                    "File Not Found Exception",
                                    JOptionPane.ERROR_MESSAGE);
    }
    catch(IOException ioe){
      JOptionPane.showMessageDialog(this,
                                    ioe.getMessage(),
                                    "I/O Exception",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  private void loadResultsAction(){
    JFileChooser jfc = new JFileChooser();
    jfc.setMultiSelectionEnabled(false);
    jfc.setFileFilter(new TextFilter());
    try{
      int returnVal = jfc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION){
        File f = jfc.getSelectedFile();
        FileReader in = new FileReader(f);
        CSVParser csvp = new CSVParser(in);
        String[][] values = csvp.getAllValues();
        fireLoadResults(values);
      }
    }
    catch(FileNotFoundException fnfe){
      JOptionPane.showMessageDialog(this,
                                    fnfe.getMessage(),
                                    "File Not Found Exception",
                                    JOptionPane.ERROR_MESSAGE);
    }
    catch(IOException ioe){
      JOptionPane.showMessageDialog(this,
                                    ioe.getMessage(),
                                    "I/O Exception",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  public void addDisplayModeListener(DisplayModeListener dml){
    displayModeList.add(DisplayModeListener.class, dml);
  }

  public void removeDisplayModeListener(DisplayModeListener dml){
    displayModeList.remove(DisplayModeListener.class, dml);
  }

  public void fireDisplayMode(int mode){
    Object[] listeners = displayModeList.getListenerList();
    int numListeners = listeners.length;
    DisplayModeEvent dme = new DisplayModeEvent(this, mode);
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==DisplayModeListener.class) {
        // pass the event to the listeners event dispatch method
        ((DisplayModeListener)listeners[i+1]).setDisplayMode(dme);
      }
    }
  }

  public void addNewDataSetListener(NewDataSetListener gsl){
    newDataSetList.add(NewDataSetListener.class, gsl);
  }

  public void removeNewDataSetListener(NewDataSetListener gsl){
    newDataSetList.remove(NewDataSetListener.class, gsl);
  }

  public void fireNewDataSet(Vector dataSet, double minX, double minY, double maxX,
                             double maxY, double density, InitGAMFile init){
    Object[] listeners = newDataSetList.getListenerList();
    int numListeners = listeners.length;
    NewDataSetEvent ndse = new NewDataSetEvent(this, dataSet, minX, minY, maxX, maxY, density, init);
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==NewDataSetListener.class) {
        // pass the event to the listeners event dispatch method
        ((NewDataSetListener)listeners[i+1]).setDataSet(ndse);
      }
    }
  }

  public void addClearCreatedListener(ClearCreatedListener ccl){
    clearCreatedList.add(ClearCreatedListener.class, ccl);
  }

  public void removeClearCreatedListener(ClearCreatedListener ccl){
    clearCreatedList.remove(ClearCreatedListener.class, ccl);
  }

  public void fireClearCreated(){
    Object[] listeners = clearCreatedList.getListenerList();
    int numListeners = listeners.length;
    ClearCreatedEvent cce = new ClearCreatedEvent(this);
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==ClearCreatedListener.class) {
        // pass the event to the listeners event dispatch method
        ((ClearCreatedListener)listeners[i+1]).clearCreated(cce);
      }
    }
  }

//  public void addGAMSaveListener(GAMSaveListener gsl){
//    GAMSaveList.add(GAMSaveListener.class, gsl);
//  }
//
//  public void removeGAMSaveListener(GAMSaveListener gsl){
//    GAMSaveList.remove(GAMSaveListener.class, gsl);
//  }
//
//  public void fireGAMSave(){
//    Object[] listeners = GAMSaveList.getListenerList();
//    int numListeners = listeners.length;
//    GAMSaveEvent gse = new GAMSaveEvent(this);
//    for (int i = 0; i < numListeners; i++){
//      if (listeners[i]==GAMSaveListener.class) {
//        // pass the event to the listeners event dispatch method
//        ((GAMSaveListener)listeners[i+1]).saveResults(gse);
//      }
//    }
//  }

  public void addRunGAMListener(RunGAMListener gsl){
    runGAMList.add(RunGAMListener.class, gsl);
  }

  public void removeRunGAMListener(RunGAMListener gsl){
    runGAMList.remove(RunGAMListener.class, gsl);
  }

  public void fireRunGAM(){
    Object[] listeners = runGAMList.getListenerList();
    int numListeners = listeners.length;
    RunGAMEvent rge = new RunGAMEvent(this);
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==RunGAMListener.class) {
        // pass the event to the listeners event dispatch method
        ((RunGAMListener)listeners[i+1]).runGAM(rge);
      }
    }
  }

  public void addDefaultSettingsListener(DefaultSettingsListener gsl){
    defaultSettingsList.add(DefaultSettingsListener.class, gsl);
  }

  public void removeDefaultSettingsListener(DefaultSettingsListener gsl){
    defaultSettingsList.remove(DefaultSettingsListener.class, gsl);
  }

  public void fireDefaultSettings(){
    Object[] listeners = defaultSettingsList.getListenerList();
    int numListeners = listeners.length;
    DefaultSettingsEvent dse = new DefaultSettingsEvent(this);
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==DefaultSettingsListener.class) {
        // pass the event to the listeners event dispatch method
        ((DefaultSettingsListener)listeners[i+1]).setToDefaults(dse);
      }
    }
  }

  public void addLoadSettingsListener(LoadSettingsListener lsl){
    loadSettingsList.add(LoadSettingsListener.class, lsl);
  }

  public void removeLoadSettingsListener(LoadSettingsListener lsl){
    loadSettingsList.remove(LoadSettingsListener.class, lsl);
  }

  public void fireLoadSettings(){
    Object[] listeners = loadSettingsList.getListenerList();
    int numListeners = listeners.length;
    LoadSettingsEvent lse = new LoadSettingsEvent(this);
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==LoadSettingsListener.class) {
        // pass the event to the listeners event dispatch method
        ((LoadSettingsListener)listeners[i+1]).loadSettings(lse);
      }
    }
  }

  public void addSaveSettingsListener(SaveSettingsListener ssl){
    saveSettingsList.add(SaveSettingsListener.class, ssl);
  }

  public void removeSaveSettingsListener(SaveSettingsListener ssl){
    saveSettingsList.remove(SaveSettingsListener.class, ssl);
  }

  public void fireSaveSettings(){
    Object[] listeners = saveSettingsList.getListenerList();
    int numListeners = listeners.length;
    SaveSettingsEvent sse = new SaveSettingsEvent(this);
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==SaveSettingsListener.class) {
        // pass the event to the listeners event dispatch method
        ((SaveSettingsListener)listeners[i+1]).saveSettings(sse);
      }
    }
  }

  public void addLoadResultsListener(LoadResultsListener lrl){
    loadResultList.add(LoadResultsListener.class, lrl);
  }

  public void removeLoadResultsListener(LoadResultsListener lrl){
    loadResultList.remove(LoadResultsListener.class, lrl);
  }

  public void fireLoadResults(String[][] data){
    Object[] listeners = loadResultList.getListenerList();
    int numListeners = listeners.length;
    LoadResultsEvent ndse = new LoadResultsEvent(this, data);
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==LoadResultsListener.class) {
        // pass the event to the listeners event dispatch method
        ((LoadResultsListener)listeners[i+1]).loadResults(ndse);
      }
    }
  }

  /**
   * Accepts a file from an outside source (such as a SimpleFileChooser bean)
   * rather than using the file chooser to get the file itself.
   */

  public void setFile(File data){
    InitGAMFile initializer = new InitGAMFile();
    try{
      initializer.processTextFile(data);  
      initializer.addActionListener(this);
    }
    catch (IOException ioe){
      JOptionPane.showMessageDialog(this,
                                    ioe.getMessage(),
                                    "I/O Exception",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  public void setVectorData(Vector v){
      InitGAMFile initializer = new InitGAMFile();
      initializer.setDataVector(v);
      double minX = initializer.getMinX();
      double minY = initializer.getMinY();
      double maxX = initializer.getMaxX();
      double maxY = initializer.getMaxY();
      double density = initializer.getDensity();
      fireNewDataSet(v, minX, minY, maxX, maxY, density, initializer);
  }
  
//      public void setFeatureData(FeatureCollection fc){
//          InitGAMFile initializer = new InitGAMFile();
//          initializer.addActionListener(this);
//          initializer.setFeatureCollection(fc);      
//      }
  
}
