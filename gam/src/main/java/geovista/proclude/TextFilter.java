package geovista.proclude;

/**
 * This class extends FileFilter to only allow the user to look at .csv files.
 *
 * This is a modified version of an extension of the FileFilter class found
 * somewhere in the java tutorial.
 *
 * @author Jamison Conley
 * @version 2.0
 */

import javax.swing.filechooser.*;
import java.io.File;

public class TextFilter extends FileFilter {

  public TextFilter() {
  }

  /**
   * Returns the extension of the file name associated with the File f.
   */

  public static String getExtension(File f) {
      String ext = null;
      String s = f.getName();
      int i = s.lastIndexOf('.');

      if (i > 0 &&  i < s.length() - 1) {
          ext = s.substring(i+1).toLowerCase();
      }
      return ext;
  }

  /**
   * Determines whether or not to accept a given file or directory.
   */

  public boolean accept(File f) {
    if (f.isDirectory()) {
        return true;
    }
    String extension = getExtension(f);
    if (extension != null) {
        return (extension.equals("csv"));
    }
    return false;
  }

  /**
   * Returns the description of the files it allows.
   */

  public String getDescription(){
    return ".csv files";
  }

}