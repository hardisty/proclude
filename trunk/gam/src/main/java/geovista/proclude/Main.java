package geovista.proclude;

/**
 * This is the main class for the GAM program.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import javax.swing.*;

public class Main{

  public static void main(String[] args) {
    // create a new instance of GamGUI
    GamGUI gg = new GamGUI();

    // Create a frame and container for the panels.
    JFrame GAMFrame = new JFrame("PROCLUDE");

    // Set the look and feel.
    try {
      UIManager.setLookAndFeel(
          UIManager.getCrossPlatformLookAndFeelClassName());
      } catch(Exception e) {}

      GAMFrame.setContentPane(gg.mainPanel);

      // Exit when the window is closed.
      GAMFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Show the whole thing.
      GAMFrame.pack();
      GAMFrame.setVisible(true);
  }

}