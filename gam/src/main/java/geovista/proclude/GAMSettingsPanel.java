package geovista.proclude;

/**
 * This class contains the GAM settings panel for the GUI.
 *
 * @author Jamison Conley
 * @version 2.0
 */


import geovista.readers.csv.CSVParser;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.io.*;

public class GAMSettingsPanel extends JPanel implements ActionListener,
                                                        DefaultSettingsListener,
                                                        NewDataSetListener,
                                                        LoadSettingsListener,
                                                        SaveSettingsListener,
                                                        Serializable{

  JButton applyButton;
  JLabel popSizeLabel, probMutLabel, randomGenesLabel, selectPairsLabel, bannedListLabel,
         solutionListLabel, antiConvergenceLabel, firstAddLabel, updateOftenLabel, minPointsLabel,
         fitFunLabel, selectionLabel, surviveLabel, crossoverLabel, mutationLabel, haltLabel,
         relocationLabel, optimumLabel, maxFitnessLabel, pctEliteLabel, pctSurviveLabel, mutSizeLabel,
         decreaseRateLabel, maxGensLabel, maxStagnantLabel, gamTypeLabel, minAcceptedLabel, minRadiusLabel, maxRadiusLabel;
  DecimalNumberField probMutField, optimumField, maxFitnessField, pctEliteField, pctSurviveField, mutSizeField,
                     decreaseRateField, minAcceptedField, minRadiusField, maxRadiusField;
  JCheckBox selectPairsBox, bannedListBox, solutionListBox, antiConvergenceBox;
  JComboBox gamTypeMenu, fitFunMenu, selectionMenu, surviveMenu, crossoverMenu, mutationMenu, haltMenu, relocationMenu;
  WholeNumberField popSizeField, randomGenesField, firstAddField, updateOftenField, minPointsField,
                   maxGensField, maxStagnantField;
  JPanel bottomPanel, aboutPanel;
  JTextArea aboutArea;
  private EventListenerList GAMSettingsList = new EventListenerList();
  private GAMSettingsEvent settings;
  private Vector dataSet;
  private double density;
  private InitGAMFile init;
  private JFileChooser jfc;
  final String[] geneFeatures = {"Center X: ", "Center Y: ", "Horiz. Radius: ", "Vertical Radius: ", "Area: ", "Count: ", "Fitness: "};
  public static final String[] fitnesses = {"Optimal Radii Sum", "Density", "Max Fitness", "Optimal Area",
                                            "Optimal Radius", "Obs - Exp", "Obs/Exp Ratio", "Poisson", "Monte Carlo"};
  public static final String[] selections = {"Pairs of neighbors", "Probabilistic", "Random Elite"};
  public static final String[] survives = {"Compare two", "Elite N", "Relative Fitness"};
  public static final String[] crossovers = {"Any Mix", "Midline", "Midpoint", "Single Point"};
  public static final String[] mutations = {"Constant Linear", "Decreasing with Time"};
  public static final String[] halts = {"Best Plateau", "Median Plateau", "N Generations"};
  public static final String[] relocations = {"Centroid Containment", "Genetic Difference"};
  final String[] gamTypes = {"Genetic", "Random", "Systematic", "Besag-Newell"};
  public static final String TYPE = "Algorithm type:";
  public static final String POPSIZE = "Population size:";
  public static final String PROBMUT = "Probability of mutation:";
  public static final String RANDOM = "No. of random genes/generation:";
  public static final String PAIRS = "Select neighboring pairs of parents:";
  public static final String BANNED = "Keep a list of banned areas:";
  public static final String SOLUTION = "Keep a separate solution list:";
  public static final String ANTI = "Use anti-convergence:";
  public static final String START = "When to start the banned/sol'n list:";
  public static final String OFTEN = "How often to update the list(s):";
  public static final String MINRAD = "Minimum radius size:";
  public static final String MAXRAD = "Maximum radius size:";
  public static final String MINPTS = "Minimum points in a cluster:";
  public static final String MINFIT = "Minimum acceptable fitness:";
  public static final String FUNCTION = "Fitness function:";
  public static final String OPTIMUM = "Optimum:";
  public static final String MAXFIT = "Maximum fitness:";
  public static final String SELECT = "Parent selection method:";
  public static final String ELITE = "Percent in elite:";
  public static final String SURVIVE = "Survival method:";
  public static final String PCTSURV = "Percent that survive:";
  public static final String CROSS = "Crossover method:";
  public static final String MUTATE = "Mutation method:";
  public static final String MUTSIZE = "Mutation size:";
  public static final String DECREASE = "Rate of size decrease:";
  public static final String HALT = "Halting condition:";
  public static final String MAXGENS = "Maximum no. of generations:";
  public static final String STAGNANT = "Maximum no. of stagnant generations:";
  public static final String RELOCATE = "Relocation method:";
  private FitnessMonteCarlo fmc = null;



  public GAMSettingsPanel() {
    addGAMSettingsWidgets();
  }

  /**
   * This method initializes the GAM settings panel ("GAM Settings" tab)
   * Called by init()
   */

  private void addGAMSettingsWidgets(){

    jfc = new JFileChooser();
    jfc.setMultiSelectionEnabled(false);
    jfc.setFileFilter(new TextFilter());

    applyButton = new JButton("Apply Settings:");
    applyButton.setActionCommand("APPLY");
    applyButton.addActionListener(this);

    //Create 29 labels
    gamTypeLabel = new JLabel(TYPE);
    popSizeLabel = new JLabel(POPSIZE);
    probMutLabel = new JLabel(PROBMUT);
    randomGenesLabel = new JLabel(RANDOM);
    selectPairsLabel = new JLabel(PAIRS);
    bannedListLabel = new JLabel(BANNED);
    solutionListLabel = new JLabel(SOLUTION);
    antiConvergenceLabel = new JLabel(ANTI);
    firstAddLabel = new JLabel(START);
    updateOftenLabel = new JLabel(OFTEN);
    minPointsLabel = new JLabel(MINPTS);
    fitFunLabel = new JLabel(FUNCTION);
    selectionLabel = new JLabel(SELECT);
    surviveLabel = new JLabel(SURVIVE);
    crossoverLabel = new JLabel(CROSS);
    mutationLabel = new JLabel(MUTATE);
    haltLabel = new JLabel(HALT);
    relocationLabel = new JLabel(RELOCATE);
    optimumLabel = new JLabel(OPTIMUM);
    maxFitnessLabel = new JLabel(MAXFIT);
    pctEliteLabel = new JLabel(ELITE);
    pctSurviveLabel = new JLabel(PCTSURV);
    mutSizeLabel = new JLabel(MUTSIZE);
    decreaseRateLabel = new JLabel(DECREASE);
    maxGensLabel = new JLabel(MAXGENS);
    maxStagnantLabel = new JLabel(STAGNANT);
    minAcceptedLabel = new JLabel(MINFIT);
    minRadiusLabel = new JLabel(MINRAD);
    maxRadiusLabel = new JLabel (MAXRAD);

    //Create 16 text fields
    popSizeField = new WholeNumberField(100, 1);
    probMutField = new DecimalNumberField(0.05, 1);
    randomGenesField = new WholeNumberField(0, 1);
    firstAddField = new WholeNumberField(4, 1);
    updateOftenField = new WholeNumberField(2, 1);
    minPointsField = new WholeNumberField(3, 1);
    optimumField = new DecimalNumberField(25.0, 1);
    maxFitnessField = new DecimalNumberField(3.0, 1);
    pctEliteField = new DecimalNumberField(0.4, 1);
    pctSurviveField = new DecimalNumberField(0.4, 1);
    mutSizeField = new DecimalNumberField(5.0, 1);
    decreaseRateField = new DecimalNumberField(0.01, 1);
    maxGensField = new WholeNumberField(75, 1);
    maxStagnantField = new WholeNumberField(5, 1);
    minAcceptedField = new DecimalNumberField(3.0, 1);
    minRadiusField = new DecimalNumberField(0.5, 1);
    maxRadiusField = new DecimalNumberField(8.0, 1);

    //Create 4 check boxes
    selectPairsBox = new JCheckBox();
    bannedListBox = new JCheckBox();
    solutionListBox = new JCheckBox();
    antiConvergenceBox = new JCheckBox();

    //Create 8 drop menus (JComboBox-es)
    gamTypeMenu = new JComboBox(gamTypes);
    fitFunMenu = new JComboBox(fitnesses);
    selectionMenu = new JComboBox(selections);
    surviveMenu = new JComboBox(survives);
    crossoverMenu = new JComboBox(crossovers);
    mutationMenu = new JComboBox(mutations);
    haltMenu = new JComboBox(halts);
    relocationMenu = new JComboBox(relocations);

    aboutPanel = new JPanel();
    aboutArea = new JTextArea("Default settings.", 1, 30);
    aboutArea.setLineWrap(true);
    aboutArea.setWrapStyleWord(true);
    aboutPanel.add(aboutArea);
    aboutPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5),
                                                            BorderFactory.createEtchedBorder()));

    setToDefaults();

    bottomPanel = new JPanel();

    //Create appropriate (grid) layout
    bottomPanel.setLayout(new GridLayout(0,2,5,0));
    //Put everything into its place
    bottomPanel.add(gamTypeLabel);
    bottomPanel.add(gamTypeMenu);

    bottomPanel.add(popSizeLabel);
    bottomPanel.add(popSizeField);

    bottomPanel.add(probMutLabel);
    bottomPanel.add(probMutField);

    bottomPanel.add(randomGenesLabel);
    bottomPanel.add(randomGenesField);

    bottomPanel.add(selectPairsLabel);
    bottomPanel.add(selectPairsBox);

    bottomPanel.add(bannedListLabel);
    bottomPanel.add(bannedListBox);

    bottomPanel.add(solutionListLabel);
    bottomPanel.add(solutionListBox);

    bottomPanel.add(antiConvergenceLabel);
    bottomPanel.add(antiConvergenceBox);

    bottomPanel.add(firstAddLabel);
    bottomPanel.add(firstAddField);

    bottomPanel.add(updateOftenLabel);
    bottomPanel.add(updateOftenField);

    bottomPanel.add(minRadiusLabel);
    bottomPanel.add(minRadiusField);

    bottomPanel.add(maxRadiusLabel);
    bottomPanel.add(maxRadiusField);
    
    bottomPanel.add(minPointsLabel);
    bottomPanel.add(minPointsField);

    bottomPanel.add(minAcceptedLabel);
    bottomPanel.add(minAcceptedField);

    bottomPanel.add(fitFunLabel);
    bottomPanel.add(fitFunMenu);
    bottomPanel.add(optimumLabel);
    bottomPanel.add(optimumField);
    bottomPanel.add(maxFitnessLabel);
    bottomPanel.add(maxFitnessField);

    bottomPanel.add(selectionLabel);
    bottomPanel.add(selectionMenu);
    bottomPanel.add(pctEliteLabel);
    bottomPanel.add(pctEliteField);

    bottomPanel.add(surviveLabel);
    bottomPanel.add(surviveMenu);
    bottomPanel.add(pctSurviveLabel);
    bottomPanel.add(pctSurviveField);

    bottomPanel.add(crossoverLabel);
    bottomPanel.add(crossoverMenu);

    bottomPanel.add(mutationLabel);
    bottomPanel.add(mutationMenu);
    bottomPanel.add(mutSizeLabel);
    bottomPanel.add(mutSizeField);
    bottomPanel.add(decreaseRateLabel);
    bottomPanel.add(decreaseRateField);

    bottomPanel.add(haltLabel);
    bottomPanel.add(haltMenu);
    bottomPanel.add(maxGensLabel);
    bottomPanel.add(maxGensField);
    bottomPanel.add(maxStagnantLabel);
    bottomPanel.add(maxStagnantField);

    bottomPanel.add(relocationLabel);
    bottomPanel.add(relocationMenu);

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    add(aboutPanel);
    add(applyButton);
    add(bottomPanel);
  }

  private void setToDefaults(){
    popSizeField.setValue(100);
    probMutField.setValue(0.05);
    randomGenesField.setValue(0);
    firstAddField.setValue(4);
    updateOftenField.setValue(2);
    minRadiusField.setValue(0.5);
    maxRadiusField.setValue(8.0);
    minPointsField.setValue(30);
    minAcceptedField.setValue(30.0);
    optimumField.setValue(75.0);
    maxFitnessField.setValue(3.0);
    pctEliteField.setValue(0.2);
    pctSurviveField.setValue(0.2);
    mutSizeField.setValue(5.0);
    decreaseRateField.setValue(0.01);
    maxGensField.setValue(75);
    maxStagnantField.setValue(5);
    selectPairsBox.setSelected(false);
    bannedListBox.setSelected(true);
    solutionListBox.setSelected(false);
    antiConvergenceBox.setSelected(true);
    gamTypeMenu.setSelectedIndex(2);
    fitFunMenu.setSelectedIndex(5);
    selectionMenu.setSelectedIndex(2);
    surviveMenu.setSelectedIndex(1);
    crossoverMenu.setSelectedIndex(1);
    mutationMenu.setSelectedIndex(0);
    haltMenu.setSelectedIndex(2);
    relocationMenu.setSelectedIndex(1);
    aboutArea.setText("Default settings.");
  }

  /**
   * What to do (fire a GAM Settings Event) whenever the 'Apply Settings' button
   * is pressed.
   */

  public void actionPerformed(ActionEvent e){
    if ("APPLY".equals(e.getActionCommand())){
      fireGAMSettings();
    }
  }

  /**
   * Implementation of the default settings listener; restore the settings to their
   * defaults and fire a new GAM Settings event.
   */

  public void setToDefaults(DefaultSettingsEvent dse){
    setToDefaults();
    fireGAMSettings();
  }

  /**
   * Implementation of the NewDataSetListener interface; accept the new data set,
   * change the fitness function accordingly, and fire a GAMSettingsEvent.
   * @param ndse
   */

  public void setDataSet(NewDataSetEvent ndse){
    dataSet = ndse.getDataSet();
    density = ndse.getDensity();
    init = ndse.getInitializer();
    double xDim = init.getMaxX() - init.getMinX();
    double yDim = init.getMaxY() - init.getMinY();
    double minRad = 0.005*Math.min(xDim, yDim);
    double maxRad = 0.2*Math.max(xDim, yDim);
    minRadiusField.setValue(minRad);
    maxRadiusField.setValue(maxRad);
    mutSizeField.setValue((maxRad + minRad) / 2);
    fmc = null;
    fireGAMSettings();
  }

  /**
   * Implementation of the LoadSettingsListener interface.  It reads a .csv file
   * and extracts the settings from that file.
   */

  public void loadSettings(LoadSettingsEvent lse){
    try{
      int returnVal = jfc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION){
        File f = jfc.getSelectedFile();
        FileReader in = new FileReader(f);
        CSVParser csvp = new CSVParser(in);
        String[][] values = csvp.getAllValues();
        stringArrayToSettings(values);
        fireGAMSettings();
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

  /**
   * Does the dirty work of the loadSettings method
   */

  private void stringArrayToSettings(String[][] values){
    //Convert the double array of strings into settings by altering the values
    //of the settings fields and menus appropriately.
    if (values[0][1].equals("Genetic")){
      gamTypeMenu.setSelectedIndex(0);
    } else if (values[0][1].equals("Random")){
      gamTypeMenu.setSelectedIndex(1);
    } else if (values[0][1].equals("Systematic")){
      gamTypeMenu.setSelectedIndex(2);
    } else {
      gamTypeMenu.setSelectedIndex(3);
    }
    int index = 0;
    switch(gamTypeMenu.getSelectedIndex()){
      case 0:{
        popSizeField.setValue(Integer.parseInt(values[1][1]));
        probMutField.setValue(Double.parseDouble(values[2][1]));
        randomGenesField.setValue(Integer.parseInt(values[3][1]));
        selectPairsBox.setSelected(Boolean.valueOf(values[4][1]).booleanValue());
        bannedListBox.setSelected(Boolean.valueOf(values[5][1]).booleanValue());
        solutionListBox.setSelected(Boolean.valueOf(values[6][1]).booleanValue());
        antiConvergenceBox.setSelected(Boolean.valueOf(values[7][1]).booleanValue());
        firstAddField.setValue(Integer.parseInt(values[8][1]));
        updateOftenField.setValue(Integer.parseInt(values[9][1]));
        minPointsField.setValue(Integer.parseInt(values[10][1]));
        minRadiusField.setValue(Double.parseDouble(values[11][1]));
        maxRadiusField.setValue(Double.parseDouble(values[12][1]));
        minAcceptedField.setValue(Double.parseDouble(values[13][1]));
        index = 14;
        if (values[index][1].equals(fitnesses[0])){
          fitFunMenu.setSelectedIndex(0);
          index++;
          optimumField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(fitnesses[1])){
          fitFunMenu.setSelectedIndex(1);
          index++;
        } else if (values[index][1].equals(fitnesses[2])){
          fitFunMenu.setSelectedIndex(2);
          index++;
          maxFitnessField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(fitnesses[3])){
          fitFunMenu.setSelectedIndex(3);
          index++;
          optimumField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(fitnesses[4])){
          fitFunMenu.setSelectedIndex(4);
          index++;
          optimumField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(fitnesses[5])){
          fitFunMenu.setSelectedIndex(5);
          index++;
        } else if (values[index][1].equals(fitnesses[6])){
          fitFunMenu.setSelectedIndex(6);
          index++;
        } else if (values[index][1].equals(fitnesses[7])){
          fitFunMenu.setSelectedIndex(7);
          index++;
        } else {
          fitFunMenu.setSelectedIndex(8);
          index++;
        } //End fitness function assignment.  Begin selection assignment.
        if (values[index][1].equals(selections[0])){
          selectionMenu.setSelectedIndex(0);
          index++;
          pctEliteField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(selections[1])){
          selectionMenu.setSelectedIndex(1);
          index++;
        } else {
          selectionMenu.setSelectedIndex(2);
          index++;
          pctEliteField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } //End selection assignment.  Begin survival assignment.
        if (values[index][1].equals(survives[0])){
          surviveMenu.setSelectedIndex(0);
          index++;
          pctSurviveField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(survives[1])){
          surviveMenu.setSelectedIndex(1);
          index++;
          pctSurviveField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else {
          surviveMenu.setSelectedIndex(2);
          index++;
          pctSurviveField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } //End survival assignment.  Begin crossover assignment.
        if (values[index][1].equals(crossovers[0])){
          crossoverMenu.setSelectedIndex(0);
          index++;
        } else if (values[index][1].equals(crossovers[1])){
          crossoverMenu.setSelectedIndex(1);
          index++;
        } else if (values[index][1].equals(crossovers[2])){
          crossoverMenu.setSelectedIndex(2);
          index++;
        } else if (values[index][1].equals(crossovers[3])){
          crossoverMenu.setSelectedIndex(3);
          index++;
        } //End crossover assignment.  Begin mutation assignment.
        if (values[index][1].equals(mutations[0])){
          mutationMenu.setSelectedIndex(0);
          index++;
          mutSizeField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(mutations[1])){
          mutationMenu.setSelectedIndex(1);
          index++;
          mutSizeField.setValue(Double.parseDouble(values[index][1]));
          index++;
          decreaseRateField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } //End mutation assignment.  Begin halting condition assignment.
        if (values[index][1].equals(halts[0])){
          haltMenu.setSelectedIndex(0);
          index++;
          maxGensField.setValue(Integer.parseInt(values[index][1]));
          index++;
          maxStagnantField.setValue(Integer.parseInt(values[index][1]));
          index++;
        } else if (values[index][1].equals(halts[1])){
          haltMenu.setSelectedIndex(1);
          index++;
          maxGensField.setValue(Integer.parseInt(values[index][1]));
          index++;
          maxStagnantField.setValue(Integer.parseInt(values[index][1]));
          index++;
        } else if (values[index][1].equals(halts[2])){
          haltMenu.setSelectedIndex(2);
          index++;
          maxGensField.setValue(Integer.parseInt(values[index][1]));
          index++;
        } //End halting condition assignment.  Begin relocation assignment.
        if (values[index][1].equals(relocations[0])){
          relocationMenu.setSelectedIndex(0);
          index++;
        } else if (values[index][1].equals(relocations[1])){
          relocationMenu.setSelectedIndex(1);
          index++;
        } //End relocation assignment.
        break;
      }
      case 1:{
        popSizeField.setValue(Integer.parseInt(values[1][1]));
        maxGensField.setValue(Integer.parseInt(values[2][1]));
        minPointsField.setValue(Integer.parseInt(values[3][1]));
        minRadiusField.setValue(Double.parseDouble(values[4][1]));
        maxRadiusField.setValue(Double.parseDouble(values[5][1]));
        minAcceptedField.setValue(Double.parseDouble(values[6][1]));
        index = 7;
        if (values[index][1].equals(fitnesses[0])){
          fitFunMenu.setSelectedIndex(0);
          index++;
          optimumField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(fitnesses[1])){
          fitFunMenu.setSelectedIndex(1);
          index++;
        } else if (values[index][1].equals(fitnesses[2])){
          fitFunMenu.setSelectedIndex(2);
          index++;
          maxFitnessField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(fitnesses[3])){
          fitFunMenu.setSelectedIndex(3);
          index++;
          optimumField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(fitnesses[4])){
          fitFunMenu.setSelectedIndex(4);
          index++;
          optimumField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(fitnesses[5])){
          fitFunMenu.setSelectedIndex(5);
          index++;
        } else if (values[index][1].equals(fitnesses[6])){
          fitFunMenu.setSelectedIndex(6);
          index++;
        } else {
          fitFunMenu.setSelectedIndex(7);
          index++;
        }
        break;
      }
      case 2: case 3:{
        minPointsField.setValue(Integer.parseInt(values[1][1]));
        minRadiusField.setValue(Double.parseDouble(values[2][1]));
        maxRadiusField.setValue(Double.parseDouble(values[3][1]));
        minAcceptedField.setValue(Double.parseDouble(values[4][1]));
        index = 5;
        if (values[index][1].equals(fitnesses[0])){
          fitFunMenu.setSelectedIndex(0);
          index++;
          optimumField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(fitnesses[1])){
          fitFunMenu.setSelectedIndex(1);
          index++;
        } else if (values[index][1].equals(fitnesses[2])){
          fitFunMenu.setSelectedIndex(2);
          index++;
          maxFitnessField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(fitnesses[3])){
          fitFunMenu.setSelectedIndex(3);
          index++;
          optimumField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(fitnesses[4])){
          fitFunMenu.setSelectedIndex(4);
          index++;
          optimumField.setValue(Double.parseDouble(values[index][1]));
          index++;
        } else if (values[index][1].equals(fitnesses[5])){
          fitFunMenu.setSelectedIndex(5);
          index++;
        } else if (values[index][1].equals(fitnesses[6])){
          fitFunMenu.setSelectedIndex(6);
          index++;
        } else {
          fitFunMenu.setSelectedIndex(7);
          index++;
        }
        break;
      }
    }
    String about = new String();
    for (int i = index; i < values.length; i++){
      for (int j = 0; j < values[i].length; j++){
        if (j == 0)
          about = about + values[i][j];
        else
          about = about + ", " + values[i][j];
      }
      if (i != values.length - 1){
        about = about + '\n';
      }
    }
    aboutArea.setText(about);
  }

  /**
   * Implementation of the save settings listener interface.  It converts the
   * settings into a .csv file, and then writes them out to a file.
   */

  public void saveSettings(SaveSettingsEvent sse){
    int returnVal = jfc.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION){
      File f = jfc.getSelectedFile();
      String out = settings.toString();
      out = out + aboutArea.getText();

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
    }
  }

  public void addGAMSettingsListener(GAMSettingsListener gsl){
    GAMSettingsList.add(GAMSettingsListener.class, gsl);
  }

  public void removeGAMSettingsListener(GAMSettingsListener gsl){
    GAMSettingsList.remove(GAMSettingsListener.class, gsl);
  }

  public void fireGAMSettings(){
    try{
      Object[] listeners = GAMSettingsList.getListenerList();
      int numListeners = listeners.length;

      int gamType = gamTypeMenu.getSelectedIndex();
      int popSize = popSizeField.getValue();
      double probMut = probMutField.getValue();
      int randomGenes = randomGenesField.getValue();
      boolean selectPairs = selectPairsBox.isSelected();
      boolean bannedList = bannedListBox.isSelected();
      boolean solutionList = solutionListBox.isSelected();
      boolean antiConvergence = antiConvergenceBox.isSelected();
      int firstAdd = firstAddField.getValue();
      int updateOften = updateOftenField.getValue();
      double minRadius = minRadiusField.getValue();
      double maxRadius = maxRadiusField.getValue();
      int minPoints = minPointsField.getValue();
      double minAccepted = minAcceptedField.getValue();
      double pctElite = pctEliteField.getValue();
      double pctSurvive = pctSurviveField.getValue();
      double mutSize = mutSizeField.getValue();
      double decreaseRate = decreaseRateField.getValue();
      int maxGens = maxGensField.getValue();
      int maxStagnant = maxStagnantField.getValue();

      double optimum = optimumField.getValue();
      double maxFitness = maxFitnessField.getValue();
      int fitnessIndex = fitFunMenu.getSelectedIndex();
      Fitness fitnessFunction = null;
      switch (fitnessIndex){
        case 0:
          fitnessFunction = new FitnessAddRadii(dataSet, optimum, minPoints);
          break;
        case 1:
          fitnessFunction = new FitnessDensity(dataSet, minPoints);
          break;
        case 2:
          fitnessFunction = new FitnessMaxAtN(dataSet, maxFitness, minPoints);
          break;
        case 3:
          fitnessFunction = new FitnessOptimalArea(dataSet, optimum, minPoints);
          break;
        case 4:
          fitnessFunction = new FitnessOptimalRadius(dataSet, optimum, minPoints);
          break;
        case 5:
          fitnessFunction = new FitnessRelative(dataSet, minPoints);
          break;
        case 6:
          fitnessFunction = new FitnessRelativePct(dataSet, minPoints);
          break;
        case 7:
          fitnessFunction = new FitnessPoisson(dataSet, density, minPoints);
          break;
        case 8:
          if (fmc == null){
            fmc = new FitnessMonteCarlo(dataSet, minPoints);
          } 
          fitnessFunction = fmc;
          break;
      }

      int selectionIndex = selectionMenu.getSelectedIndex();
      Selection selectMethod = null;
      switch (selectionIndex){
        case 0:
          selectMethod = new SelectPairs(pctElite);
          break;
        case 1:
          selectMethod = new SelectProbabilistic();
          break;
        case 2:
          selectMethod = new SelectRandomElite(pctElite);
          break;
      }

      int surviveIndex = surviveMenu.getSelectedIndex();
      Survive surviveMethod = null;
      switch (surviveIndex){
        case 0:
          surviveMethod = new SurviveCompareTwo(pctSurvive);
          break;
        case 1:
          surviveMethod = new SurviveEliteN(pctSurvive);
          break;
        case 2:
          surviveMethod = new SurviveRelativeFitness(pctSurvive);
          break;
      }

      int crossoverIndex = crossoverMenu.getSelectedIndex();
      Crossover crossoverMethod = null;
      switch (crossoverIndex){
        case 0:
          crossoverMethod = new CrossAny();
          break;
        case 1:
          crossoverMethod = new CrossMidLine();
          break;
        case 2:
          crossoverMethod = new CrossMidpoint();
          break;
        case 3:
          crossoverMethod = new CrossSinglePoint();
          break;
      }

      int mutationIndex = mutationMenu.getSelectedIndex();
      Mutation mutationMethod = null;
      switch (mutationIndex){
        case 0:
          mutationMethod = new MutateLinearAmount(mutSize);
          break;
        case 1:
          mutationMethod = new MutateDecreasingWithTime(mutSize, decreaseRate);
          break;
      }

      int haltIndex = haltMenu.getSelectedIndex();
      StopType haltCondition = null;
      switch(haltIndex){
        case 0:
          haltCondition = new StopAtBestPlateau(maxGens, maxStagnant);
          break;
        case 1:
          haltCondition = new StopAtMedianPlateau(maxGens, maxStagnant);
          break;
        case 2:
          haltCondition = new StopAtNGens(maxGens);
          break;
      }

      int relocationIndex = relocationMenu.getSelectedIndex();
      Relocation relocationMethod = null;
      switch(relocationIndex){
        case 0:
          relocationMethod = new RelocateCentroid();
          break;
        case 1:
          relocationMethod = new RelocateDifference(minPoints, minAccepted);
          ((RelocateDifference)relocationMethod).setExpectedDensity(density);
          break;
      }

      settings = new GAMSettingsEvent(this, gamType, popSize, probMut, randomGenes,
                                      selectPairs, bannedList, solutionList,
                                      antiConvergence, haltCondition, firstAdd,
                                      updateOften, minPoints, fitnessFunction,
                                      minAccepted, minRadius, maxRadius, selectMethod,
                                      surviveMethod, crossoverMethod,
                                      relocationMethod, mutationMethod, init);

      for (int i = 0; i < numListeners; i++){
        if (listeners[i]==GAMSettingsListener.class) {
          // pass the event to the listeners event dispatch method
          ((GAMSettingsListener)listeners[i+1]).setGAMSettings(settings);
        }
      }

    }
    catch (IllegalArgumentException iae){
      JOptionPane.showMessageDialog(this,
                                    iae.getMessage(),
                                    "Illegal argument.",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

}