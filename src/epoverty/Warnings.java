/*
 * Just displays a simple 'warning' message to the user.
 */
package epoverty;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Warnings
{
   //*********************************************CONSTRUCTOR MUST BE SENT THE MESSAGE TO POP UP
   Warnings(String message)
   {
      // *****************************************SET L&F TO MATCH PROJECT
      UIManager.put("OptionPane.background", Color.DARK_GRAY);
      UIManager.put("OptionPane.messageForeground", Color.WHITE);
      UIManager.put("Panel.background", Color.DARK_GRAY);
      
      JFrame mainPanel = new JFrame();
      JOptionPane.showMessageDialog(mainPanel, message);
   }
}
