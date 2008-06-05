package geovista.proclude;

/**
 * This class contains the GUI for the Genetic GAM.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.EventListenerList;
//import org.geotools.feature.FeatureCollection;

public class GamGUI extends JPanel implements DataSetListener, Serializable{

  JPanel mainPanel, aboutPanel;
  JLabel label1, label2, label3, label4, label5;
  DisplayPanel displayPanel;
  DisplaySettingsPanel displaySettingsPanel;
  GAMSettingsPanel gsPanel;
  ButtonPanel buttonPanel;
  GAMWrapper gw;
  OutputPanel outputPanel;
  JTabbedPane tabbedSettingsPanel;
  JScrollPane settingsScrollPanel, displaySettingsScrollPanel, displayScrollPanel;
//  FeatureCollection fc;
  
  private EventListenerList ell = new EventListenerList();

  public GamGUI(){
    init();
  }

  /**
   * This method performs the initialization of the GUI.
   */

  private void init() {

    //Create main panel
    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    //Create display panel
    displayPanel = new DisplayPanel();
    displayScrollPanel = new JScrollPane(displayPanel);
    displayScrollPanel.setBorder(BorderFactory.createRaisedBevelBorder());
    
    //Create the button panel
    buttonPanel = new ButtonPanel();

    //Create settings panel
    tabbedSettingsPanel = new JTabbedPane();

    settingsScrollPanel = new JScrollPane();
    gsPanel = new GAMSettingsPanel();
    gsPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));

    //Create the output panel
    outputPanel = new OutputPanel();
    outputPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
    outputPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    
    //Create the display settings panel
    displaySettingsPanel = new DisplaySettingsPanel();

    //Create the about panel
    aboutPanel = new JPanel();
    label1 = new JLabel("PROCLUDE written by Jamison Conley");
    label2 = new JLabel("with advice from Mark Gahegan and James MacGill");
    label3 = new JLabel("GeoVISTA Center");
    label4 = new JLabel("The Pennsylvania State University");
    label5 = new JLabel("Department of Geography");
    label3.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
    aboutPanel.add(label1);
    aboutPanel.add(label2);
    aboutPanel.add(label3);
    aboutPanel.add(label4);
    aboutPanel.add(label5);
    aboutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    //Put the settings panel into a scroll panel
    settingsScrollPanel.setViewportView(gsPanel);
    //the scroll bar policies are added for use in Studio, which somehow messes up the _AS_NEEDED type to make it think it never needs scroll bars.
    settingsScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    
    //Put the display settings panel into a scroll panel
    displaySettingsScrollPanel = new JScrollPane();
    displaySettingsScrollPanel.setViewportView(displaySettingsPanel);
    displaySettingsScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    
    //Assemble the tabbed panel
    tabbedSettingsPanel.addTab("Algorithm Settings", settingsScrollPanel);
    tabbedSettingsPanel.addTab("Display Settings", displaySettingsScrollPanel);
    tabbedSettingsPanel.addTab("Output stats", outputPanel);
    tabbedSettingsPanel.addTab("About", aboutPanel);

    //Put the display, button, and settings panels into the main panel
    mainPanel.add(displayScrollPanel, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.NORTH);
    mainPanel.add(tabbedSettingsPanel, BorderLayout.EAST);

    this.add(mainPanel);

    //Assemble the network of listeners.
    gw = new GAMWrapper();

    displaySettingsPanel.addDisplaySettingsListener(displayPanel);
    displayPanel.addUpdateCreatedListener(outputPanel);
    displayPanel.addUpdateGAMListener(outputPanel);
    buttonPanel.addDisplayModeListener(displayPanel);
    buttonPanel.addNewDataSetListener(displayPanel);
    buttonPanel.addClearCreatedListener(displayPanel);
//    buttonPanel.addGAMSaveListener(gw);
//    buttonPanel.addRunGAMListener(displayPanel);
    buttonPanel.addRunGAMListener(gw);
    buttonPanel.addNewDataSetListener(outputPanel);
    buttonPanel.addNewDataSetListener(displaySettingsPanel);
    buttonPanel.addClearCreatedListener(outputPanel);
    buttonPanel.addDefaultSettingsListener(gsPanel);
    buttonPanel.addNewDataSetListener(gsPanel);
    buttonPanel.addLoadSettingsListener(gsPanel);
    buttonPanel.addSaveSettingsListener(gsPanel);
    buttonPanel.addLoadResultsListener(displayPanel);
    buttonPanel.addLoadResultsListener(outputPanel);
    gw.addGAMSolutionListener(outputPanel);
    gw.addGAMSolutionListener(displayPanel);
    gsPanel.addGAMSettingsListener(outputPanel);
    gsPanel.addGAMSettingsListener(gw);
    gsPanel.addGAMSettingsListener(displayPanel);
    outputPanel.addRemoveResultsListener(displayPanel);
    outputPanel.addChangeColorListener(displayPanel);
    outputPanel.addDataSetListener(this);
  }

  /**
   * Sets the file if the file is imported from outside (as in a SimpleFileChooser
   * bean).
   */

  public void setFile(File f){
    buttonPanel.setFile(f);
  }

  public void setVectorData(Vector v){
      buttonPanel.setVectorData(v);
  }
  
//  public void setFeatureData(FeatureCollection fc){
//    System.out.println("GamGUI received the data set collection.");
//    buttonPanel.setFeatureData(fc);
//  }
  
  public void runGAM(){
      buttonPanel.removeLoadResultsListener(displayPanel);
      buttonPanel.removeNewDataSetListener(displayPanel);
      gw.removeGAMSolutionListener(displayPanel);
      gsPanel.removeGAMSettingsListener(displayPanel);
      buttonPanel.fireRunGAM();
  }
  
  public void saveToFile(String fileName){
      outputPanel.saveToFile(fileName);
  }
  
  public void dataSetChanged(DataSetEvent e) {
//      fc = outputPanel.getOutputFC();
//      System.out.println("GamGUI got the results feature collection: ");
//      fireAction();
  }
  
//  public FeatureCollection getFeatureCollection(){
//      return fc;
//  }
  
  
    public void addActionListener(ActionListener sl){
        ell.add(ActionListener.class, sl);
    }

    public void removeActionListener(ActionListener sl){
        ell.remove(ActionListener.class, sl);
    }
    
    public void fireAction(){
        Object[] listeners = ell.getListenerList();
        int numListeners = listeners.length;
        ActionEvent se = new ActionEvent(this, 42, "a string");
        for (int i = 0; i < numListeners; i++){
          if (listeners[i]==ActionListener.class){
        // pass the event to the listeners event dispatch method
            ((ActionListener)listeners[i+1]).actionPerformed(se);
          }
        }
    }
  
}