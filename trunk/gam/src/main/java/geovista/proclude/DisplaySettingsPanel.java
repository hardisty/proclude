package geovista.proclude;

/**
 * This class contains the display settings panel
 *
 * @author Jamison Conley
 * @version 2.0
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Vector;
import java.awt.geom.Ellipse2D;
import javax.swing.event.EventListenerList;
import java.io.*;

public class DisplaySettingsPanel extends JPanel implements ActionListener,
                                                            NewDataSetListener,
                                                            Serializable{

private JPanel backgroundPanel, dataPointPanel, geneticSelectedPanel, geneticUnselectedPanel,
               createdSelectedPanel, createdUnselectedPanel, buttonPanel, bottomPanel,
               topPanel, mainPanel, bwPanel, sfPanel, dpsPanel, gswPanel, guwPanel, cswPanel, cuwPanel;
private JButton defaultButton, applyButton,
        bgColorButton, dpColorButton, gsColorButton, cuColorButton, csColorButton, borderColorButton;
private JLabel bgScalingLabel, borderWidthLabel, dpSizeLabel,
               guWidthLabel, gsWidthLabel,
               cuWidthLabel, csWidthLabel;
private JCheckBox borderCheck;
private WholeNumberField borderWidthField, dpSizeField, guWidthField, gsWidthField, cuWidthField, csWidthField;
private DecimalNumberField bgScalingField;
private Color bgColor, borderColor, gsColor, csColor, cuColor, dpColor;
private EventListenerList ell = new EventListenerList();

  public DisplaySettingsPanel() {
    init();
  }

  /**
   * This method initializes the panel.
   */
  public void init(){
    //Create the top panel
    topPanel = new JPanel();
    addTopWidgets();

    //Create the button panel
    buttonPanel = new JPanel();
    bottomPanel = new JPanel();
    addButtonWidgets();

    bottomPanel.setLayout(new BorderLayout());

    //Set to defaults
    setToDefaults();

    //Assemble into the main panel
    bottomPanel.add(buttonPanel, BorderLayout.NORTH);

    mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayout(0, 1, 0, 0));
    mainPanel.add(topPanel);
    mainPanel.add(bottomPanel);
//    mainPanel.add(miniDisplayPanel);
    add(mainPanel);
  }

  private void addTopWidgets(){
    //Create and assemble the background panel
    backgroundPanel = new JPanel();
    bgColorButton = new JButton("Background color");
    bgColorButton.setActionCommand("BGCOLOR");

    borderColorButton = new JButton("Border color");
    borderColorButton.setActionCommand("BORDERCOLOR");
    borderWidthLabel = new JLabel("Border width: ");
    borderWidthField = new WholeNumberField(1, 5);

    borderCheck = new JCheckBox("Draw border");
    borderCheck.setSelected(false);
    
    bwPanel = new JPanel();
    bwPanel.setLayout(new FlowLayout());
    bwPanel.add(borderWidthLabel);
    bwPanel.add(borderWidthField);
    
    backgroundPanel.setBorder(BorderFactory.createTitledBorder("Background"));
    backgroundPanel.setLayout(new GridLayout(0, 1, 5, 5));
    backgroundPanel.add(bwPanel);
    backgroundPanel.add(borderCheck);
    backgroundPanel.add(borderColorButton);
    backgroundPanel.add(bgColorButton);

    bgColorButton.addActionListener(this);
    borderColorButton.addActionListener(this);

    //Create and assemble the data point panel

    dataPointPanel = new JPanel();
    dpColorButton = new JButton("Choose color");
    dpColorButton.setActionCommand("DPCOLOR");
    dpSizeLabel = new JLabel("Point size: ");
    dpSizeField = new WholeNumberField(4, 5);

    bgScalingLabel = new JLabel("Scaling factor: ");
    bgScalingField = new DecimalNumberField(4, 5);

    sfPanel = new JPanel();
    sfPanel.setLayout(new FlowLayout());
    sfPanel.add(bgScalingLabel);
    sfPanel.add(bgScalingField);

    dpsPanel = new JPanel();
    dpsPanel.setLayout(new FlowLayout());
    dpsPanel.add(dpSizeLabel);
    dpsPanel.add(dpSizeField);

    dataPointPanel.setBorder(BorderFactory.createTitledBorder("Data Points"));
    dataPointPanel.setLayout(new GridLayout(0, 1, 5, 5));
    dataPointPanel.add(sfPanel);
    dataPointPanel.add(dpsPanel);
    dataPointPanel.add(dpColorButton);

    dpColorButton.addActionListener(this);

    //Create and assemble the active genetic panel

    geneticSelectedPanel = new JPanel();
    gsColorButton = new JButton("Choose color");
    gsColorButton.setActionCommand("GSCOLOR");
    gsWidthLabel = new JLabel("Line width: ");
    gsWidthField = new WholeNumberField(2, 5);

    gswPanel = new JPanel();
    gswPanel.setLayout(new FlowLayout());
    gswPanel.add(gsWidthLabel);
    gswPanel.add(gsWidthField);

    geneticSelectedPanel.setBorder(BorderFactory.createTitledBorder("Active Discovered"));
    geneticSelectedPanel.setLayout(new GridLayout(0, 1, 5, 5));
    geneticSelectedPanel.add(gswPanel);
    geneticSelectedPanel.add(gsColorButton);

    gsColorButton.addActionListener(this);

    //Create and assemble the inactive genetic panel

    geneticUnselectedPanel = new JPanel();
    guWidthLabel = new JLabel("Line width: ");
    guWidthField = new WholeNumberField(1, 5);

    guwPanel = new JPanel();
    guwPanel.setLayout(new FlowLayout());
    guwPanel.add(guWidthLabel);
    guwPanel.add(guWidthField);

    geneticUnselectedPanel.setBorder(BorderFactory.createTitledBorder("Inactive Discovered"));
    geneticUnselectedPanel.setLayout(new GridLayout(0, 1, 5, 5));
    geneticUnselectedPanel.add(guwPanel);

    //Create and assemble the active selected panel

    createdSelectedPanel = new JPanel();
    csColorButton = new JButton("Choose color");
    csColorButton.setActionCommand("CSCOLOR");
    csWidthLabel = new JLabel("Line width: ");
    csWidthField = new WholeNumberField(2, 5);

    cswPanel = new JPanel();
    cswPanel.setLayout(new FlowLayout());
    cswPanel.add(csWidthLabel);
    cswPanel.add(csWidthField);

    createdSelectedPanel.setBorder(BorderFactory.createTitledBorder("Active Created"));
    createdSelectedPanel.setLayout(new GridLayout(0, 1, 5, 5));
    createdSelectedPanel.add(cswPanel);
    createdSelectedPanel.add(csColorButton);

    csColorButton.addActionListener(this);

    //Create and assemble the inactive selected panel

    createdUnselectedPanel = new JPanel();
    cuColorButton = new JButton("Choose color");
    cuColorButton.setActionCommand("CUCOLOR");
    cuWidthLabel = new JLabel("Line width: ");
    cuWidthField = new WholeNumberField(1, 5);

    cuwPanel = new JPanel();
    cuwPanel.setLayout(new FlowLayout());
    cuwPanel.add(cuWidthLabel);
    cuwPanel.add(cuWidthField);

    createdUnselectedPanel.setBorder(BorderFactory.createTitledBorder("Inactive Created"));
    createdUnselectedPanel.setLayout(new GridLayout(0, 1, 5, 5));
    createdUnselectedPanel.add(cuwPanel);
    createdUnselectedPanel.add(cuColorButton);

    cuColorButton.addActionListener(this);

    //Assemble into the top panel

    topPanel.setLayout(new GridLayout(0, 2, 5, 5));
    topPanel.add(backgroundPanel);
    topPanel.add(dataPointPanel);
    topPanel.add(geneticUnselectedPanel);
    topPanel.add(geneticSelectedPanel);
    topPanel.add(createdUnselectedPanel);
    topPanel.add(createdSelectedPanel);

  }

  private void addButtonWidgets(){
    //Create the buttons
    defaultButton = new JButton("DEFAULT");
    defaultButton.setActionCommand("DEFAULT");
    applyButton = new JButton("APPLY");
    applyButton.setActionCommand("APPLY");

    //Set the layout
    buttonPanel.setLayout(new FlowLayout());

    //Insert the buttons into the layout
    buttonPanel.add(defaultButton);
    buttonPanel.add(applyButton);

    //Add the listeners
    defaultButton.addActionListener(this);
    applyButton.addActionListener(this);
  }

  private void setToDefaults(){
    bgColor = Color.white;
    borderColor = Color.black;
    dpColor = Color.LIGHT_GRAY;
    gsColor = Color.green.darker();
    cuColor = Color.red;
    csColor = Color.red;
    borderWidthField.setValue(1);
    borderCheck.setSelected(false);
    bgScalingField.setValue(4);
    dpSizeField.setValue(4);
    guWidthField.setValue(1);
    gsWidthField.setValue(2);
    cuWidthField.setValue(1);
    csWidthField.setValue(2);
    fireDisplaySettings();
  }

  public void setDataSet(NewDataSetEvent ndse){   
    double minX = ndse.getMinX();
    double maxX = ndse.getMaxX();
    double minY = ndse.getMinY();
    double maxY = ndse.getMaxY();
    double initScalingFactor = 400 / (Math.max(maxY - minY, maxX - minX));
    if (initScalingFactor > 1){
        bgScalingField.setValue((int) Math.round(initScalingFactor));
    } else {
        bgScalingField.setValue((double) (Math.round(initScalingFactor * 100) / 100));
    } 
  }
  
  public void addDisplaySettingsListener(DisplaySettingsListener dsl){
    ell.add(DisplaySettingsListener.class, dsl);
  }

  public void removeDisplaySettingsListener(DisplaySettingsListener dsl){
    ell.remove(DisplaySettingsListener.class, dsl);
  }

  public void fireDisplaySettings(){
    Object[] listeners = ell.getListenerList();
    int numListeners = listeners.length;
    DisplaySettingsEvent dse = new DisplaySettingsEvent(this, bgColor, borderColor, dpColor, gsColor,
                                                        csColor, cuColor, bgScalingField.getValue(),
                                                        borderWidthField.getValue(), dpSizeField.getValue(),
                                                        gsWidthField.getValue(), guWidthField.getValue(),
                                                        csWidthField.getValue(), cuWidthField.getValue(),
                                                        borderCheck.isSelected());
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==DisplaySettingsListener.class) {
        // pass the event to the listeners event dispatch method
        ((DisplaySettingsListener)listeners[i+1]).setSettings(dse);
      }
    }
  }

  /**
   * Action commands and their actions:
   *
   * Default --> Restore the display settings to the defaults.  This does not
   * cause the main display panel to update, though.
   *
   * Test --> See what the display settings look like in the mini display test
   * panel at the bottom of the display settings panel.
   *
   * Apply --> Apply the new settings the both the mini display test panel and the
   * main display panel.
   *
   * xxColor --> Pop up a color chooser for the user to select the color for the
   * xx variable (background, border color, etc.)
   */
  public void actionPerformed(ActionEvent e){
    if ("DEFAULT".equals(e.getActionCommand())){
      setToDefaults();
      fireDisplaySettings();
    } else if ("APPLY".equals(e.getActionCommand())){
      fireDisplaySettings();
    } else if ("BGCOLOR".equals(e.getActionCommand())){
      Color newColor = JColorChooser.showDialog(this,
                                                "Choose Background Color",
                                                bgColor);
      if (newColor != null) {
        bgColor = newColor;
      }
    } else if ("BORDERCOLOR".equals(e.getActionCommand())){
      Color newColor = JColorChooser.showDialog(this,
                                                "Choose Border Color",
                                                borderColor);
      if (newColor != null) {
        borderColor = newColor;
      }
    } else if ("DPCOLOR".equals(e.getActionCommand())){
      Color newColor = JColorChooser.showDialog(this,
                                                "Choose Data Point Color",
                                                dpColor);
      if (newColor != null) {
        dpColor = newColor;
      }
    } else if ("GSCOLOR".equals(e.getActionCommand())){
      Color newColor = JColorChooser.showDialog(this,
                                                "Choose Active Genetic Color",
                                                gsColor);
      if (newColor != null) {
        gsColor = newColor;
      }
    } else if ("CSCOLOR".equals(e.getActionCommand())){
      Color newColor = JColorChooser.showDialog(this,
                                                "Choose Active Created Color",
                                                csColor);
      if (newColor != null) {
        csColor = newColor;
      }
    } else if ("CUCOLOR".equals(e.getActionCommand())){
      Color newColor = JColorChooser.showDialog(this,
                                                "Choose Inactive Created Color",
                                                cuColor);
      if (newColor != null) {
        cuColor = newColor;
      }
    }
  }   //End actionPerformed(ActionEvent e).....

}