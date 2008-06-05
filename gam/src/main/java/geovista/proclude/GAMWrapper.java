package geovista.proclude;

/**
 * This class contains a wrapper class so that the GAM (regardless of type) can
 * be easily converted into a bean by Studio.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.Vector;
import javax.swing.*;
import java.util.Iterator;
import java.io.*;
import java.awt.*;
import javax.swing.event.*;

public class GAMWrapper extends Container implements RunGAMListener,
                                                     GAMSettingsListener,
                                                     Serializable{

  //This extends Component because I need to give a parent component for the dialog
  //boxes that show up in the saveResults method.

  private AbstractGam GAM;
  private Vector solution;
  private GAMSettingsEvent settings;
  private EventListenerList GAMSolutionList = new EventListenerList();
  private InitGAMFile initializer;

  public GAMWrapper() {
  }
//
//  public void setGAM(AbstractGam ag){
//    GAM = ag;
//  }

/**
 * Implementation of the GAMSaveListener interface.  It saves the results of the
 * most recent run of the GAM.
 */

//  public void saveResults(GAMSaveEvent gse){
//    JFileChooser jfc = new JFileChooser();
//    jfc.setMultiSelectionEnabled(false);
//    jfc.setFileFilter(new TextFilter());
//    if (solution == null){
//      JOptionPane.showMessageDialog(this,
//                                    "You have nothing to save!",
//                                    "Error: tried to save without solution.",
//                                    JOptionPane.ERROR_MESSAGE);
//    } else {
//      int returnVal = jfc.showSaveDialog(this);
//      if (returnVal == JFileChooser.APPROVE_OPTION){
//        File f = jfc.getSelectedFile();
//        String out = "Solution: " + '\n';
//        Iterator it = solution.iterator();
//        while (it.hasNext()){
//          Gene next = (Gene) it.next();
//          out = out + next.toCSVString();
//        }
//
//        //save out string to the file
//
//        try{
//          FileWriter outWriter = new FileWriter(f);
//          int l = out.length();
//          for (int i = 0; i < l; i++){
//            outWriter.write(out.charAt(i));
//          }
//          JOptionPane.showMessageDialog(this,
//                                        "Save was successful.  Saved file to " + f.getName(),
//                                        "Save successful",
//                                        JOptionPane.INFORMATION_MESSAGE);
//          outWriter.close();
//        }
//        catch(IOException e) {
//          JOptionPane.showMessageDialog(this,
//                                        "Save failed.  Sorry for the inconvenience." + e.getMessage(),
//                                        "Error",
//                                        JOptionPane.ERROR_MESSAGE);
//        }
//      }   //End if (returnVal == JFileChooser.APPROVE_OPTION){...
//    }     //End if (solution == null){...} else {...
//  }

  /**
   * Implementation of the RunGAMListener interface.
   */

  public void runGAM(RunGAMEvent rge){
    runGAM();
  }

  public void runGAM(){
    if (initializer == null){
      JOptionPane.showMessageDialog(this,
                                    "You need to import a data set first!",
                                    "Error: Tried to run without data",
                                    JOptionPane.ERROR_MESSAGE);
    } else {
      System.out.println("Running a " + GAM.getClass());
      solution = GAM.run();
      fireGAMSolution();
    }      
  }
  
  /**
   * Implementation of the GAMSettingsListener interface.  It indicates the
   * application of new settings.
   */

  public void setGAMSettings(GAMSettingsEvent gse){
    settings = gse;
    switch(gse.getGAMType()){
      case(0):{
        GAM = new GeneticGAM();
        ((GeneticGAM)GAM).setPopSize(gse.getPopSize());
        ((GeneticGAM)GAM).setProbMut(gse.getProbMut());
        ((GeneticGAM)GAM).setRandomGenes(gse.getRandGenes());
        ((GeneticGAM)GAM).setSelectPairs(gse.getSelectPairs());
        ((GeneticGAM)GAM).setBannedList(gse.getBannedList());
        ((GeneticGAM)GAM).setSolutionList(gse.getSolutionList());
        ((GeneticGAM)GAM).setAntiConvergence(gse.getAntiConvergence());
        ((GeneticGAM)GAM).setHaltCondition(gse.getHaltCondition());
        ((GeneticGAM)GAM).setFirstAdd(gse.getFirstAdd());
        ((GeneticGAM)GAM).setUpdateOften(gse.getUpdateOften());
        GAM.setMinPoints(gse.getMinPoints());
        GAM.setFitnessFunction(gse.getFitnessFunction());
        GAM.setMinAccepted(gse.getMinAccepted());
        GAM.setMinRadius(gse.getMinRadius());
        GAM.setMaxRadius(gse.getMaxRadius());
        ((GeneticGAM)GAM).setSelectMethod(gse.getSelectionMethod());
        ((GeneticGAM)GAM).setSurviveMethod(gse.getSurvivalMethod());
        ((GeneticGAM)GAM).setCrossoverMethod(gse.getCrossoverMethod());
        ((GeneticGAM)GAM).setMutationMethod(gse.getMutationMethod());
        ((GeneticGAM)GAM).setRelocationMethod(gse.getRelocationMethod());
        break;
      }
      case(1):{
        GAM = new RandomGam();
        GAM.setFitnessFunction(gse.getFitnessFunction());
        GAM.setMinPoints(gse.getMinPoints());
        GAM.setMinRadius(gse.getMinRadius());
        GAM.setMaxRadius(gse.getMaxRadius());
        ((RandomGam)GAM).setNumTests(gse.getPopSize() * gse.getHaltCondition().getMaxGens());
        GAM.setMinAccepted(gse.getMinAccepted());
        break;
      }
      case 2:{
        GAM = new SystematicGam();
        GAM.setFitnessFunction(gse.getFitnessFunction());
        GAM.setMinPoints(gse.getMinPoints());
        GAM.setMinAccepted(gse.getMinAccepted());
        GAM.setMinRadius(gse.getMinRadius());
        GAM.setMaxRadius(gse.getMaxRadius());
        break;
      }
      default:{
        GAM = new BesagNewellGAM();
        GAM.setFitnessFunction(gse.getFitnessFunction());
        GAM.setMinPoints(gse.getMinPoints());
        GAM.setMinAccepted(gse.getMinAccepted());
        GAM.setMinRadius(gse.getMinRadius());
        GAM.setMaxRadius(gse.getMaxRadius());
      }
    }
    initializer = gse.getInitializer();
    GAM.setInitializer(initializer);
  }

  public Vector getResults(){
      return solution;
  }
  
  public void addGAMSolutionListener(GAMSolutionListener gsl){
    GAMSolutionList.add(GAMSolutionListener.class, gsl);
  }

  public void removeGAMSolutionListener(GAMSolutionListener gsl){
    GAMSolutionList.remove(GAMSolutionListener.class, gsl);
  }

  public void fireGAMSolution(){
    Object[] listeners = GAMSolutionList.getListenerList();
    int numListeners = listeners.length;
    GAMSolutionEvent uce = new GAMSolutionEvent(this, solution);
    for (int i = 0; i < numListeners; i++){
      if (listeners[i]==GAMSolutionListener.class) {
        // pass the event to the listeners event dispatch method
        ((GAMSolutionListener)listeners[i+1]).setSolution(uce);
      }
    }
  }

}