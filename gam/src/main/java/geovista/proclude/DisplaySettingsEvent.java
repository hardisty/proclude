package geovista.proclude;

/**
 * This class contains the event object that transmits the display settings from
 * the display settings panel to the display panel.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import java.util.EventObject;
import java.awt.Color;

public class DisplaySettingsEvent extends EventObject{

  private Color background, border, dataPoint, genSel, createSel,
                createUnsel;
  private int borderWidth, dpSize, gsWidth, guWidth, csWidth, cuWidth;
  private double scale;
  private boolean drawBorder;

  public DisplaySettingsEvent(Object source, Color bgColor, Color borderColor,
                              Color dpColor, Color gsColor,
                              Color csColor, Color cuColor, double scaling,
                              int bw, int dps, int gsw,
                              int guw, int csw, int cuw, boolean db) {
    super(source);
    background = bgColor;
    border = borderColor;
    dataPoint = dpColor;
    genSel = gsColor;
    createSel = csColor;
    createUnsel = cuColor;
    scale = scaling;
    borderWidth = bw;
    dpSize = dps;
    gsWidth = gsw;
    guWidth = guw;
    csWidth = csw;
    cuWidth = cuw;
    drawBorder = db;
  }

  /**
   * returns whether or not to draw the border.
   */
  public boolean getDrawBorder(){
      return drawBorder;
  }
  
  /**
   * returns the background color.
   */
  public Color getbgColor(){
    return background;
  }

  /**
   * returns the border color.
   */
  public Color getBorderColor(){
    return border;
  }

  /**
   * returns the color for data points.
   */
  public Color getdpColor(){
    return dataPoint;
  }

  /**
   * returns the color for the selected GAM ellipse.
   */
  public Color getgsColor(){
    return genSel;
  }

  /**
   * returns the color for the active created ellipse.
   */
  public Color getcsColor(){
    return createSel;
  }

  /**
   * returns the color for the inactive created ellipses.
   */
  public Color getcuColor(){
    return createUnsel;
  }

  /**
   * returns the scaling factor for the display.
   */
  public double getScalingFactor(){
    return scale;
  }

  /**
   * returns the width of the border.
   */
  public int getBorderWidth(){
    return borderWidth;
  }

  /**
   * returns the size of the data points.
   */
  public int getdpSize(){
    return dpSize;
  }

  /**
   * returns the width of the selected GAM ellipse.
   */
  public int getgsWidth(){
    return gsWidth;
  }

  /**
   * returns the width of the unselected GAM ellipses.
   */
  public int getguWidth(){
    return guWidth;
  }

  /**
   * returns the width of the active created ellipse.
   */
  public int getcsWidth(){
    return csWidth;
  }

  /**
   * returns the width of the inactive created ellipses.
   */
  public int getcuWidth(){
    return cuWidth;
  }

}