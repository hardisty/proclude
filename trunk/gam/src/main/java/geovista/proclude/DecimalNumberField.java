package geovista.proclude;

/**
 * This class contains an extension of the JTextField that only accepts non-negative
 * decimal numbers.  It is a modification of the WholeNumberField accessible through
 * the Java Tutorial.
 *
 * @author Jamison Conley
 * @version 2.0
 */


import javax.swing.*;
import javax.swing.text.*;
import java.awt.Toolkit;
import java.text.ParseException;
import java.text.DecimalFormat;

public class DecimalNumberField extends JTextField{

  private Toolkit toolkit;
  private DecimalFormat decimalFormatter;
  private boolean acceptDecimal;

  public DecimalNumberField(double value, int columns) {
    super(columns);
    toolkit = Toolkit.getDefaultToolkit();
//    decimalFormatter = DecimalFormat.getNumberInstance();
    decimalFormatter = new DecimalFormat();
    decimalFormatter.setParseIntegerOnly(false);
    setValue(value);
  }

  public double getValue() {
    double retVal = 0;
        try {
            retVal = decimalFormatter.parse(getText()).doubleValue();
        } catch (ParseException e) {
            // This should never happen because insertString allows
            // only properly formatted data to get in the field.
            toolkit.beep();
        }
        return retVal;
  }

  public void setValue(double value) {
    setText(decimalFormatter.format(value));
  }

  protected Document createDefaultModel() {
    acceptDecimal = true;
    return new DecimalNumberDocument();
  }

  protected class DecimalNumberDocument extends PlainDocument {

    //New method that is used to determine whether or not the text already contains
    //a decimal point.
    private boolean contains(String s, char c){
      return (s.lastIndexOf(c) != -1);
    }

    public void insertString(int offs,
                             String str,
                             AttributeSet a)
        throws BadLocationException {

      char[] source = str.toCharArray();
      char[] result = new char[source.length];
      int j = 0;
      acceptDecimal = !(contains(super.getText(0, super.getLength()), '.'));  //New line that uses the contains method above.
      for (int i = 0; i < result.length; i++) {
        if (Character.isDigit(source[i])){
          result[j++] = source[i];
        } else if ((acceptDecimal) && (source[i] == '.')){    //New lines to accept the first decimal point and no subsequent points.
          result[j++] = source[i];
        } else {
          toolkit.beep();
//          System.err.println("insertString: " + source[i]);
        }
      }
      super.insertString(offs, new String(result, 0, j), a);
    }
  }

}