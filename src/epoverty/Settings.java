/*
 * Class that allows modifications to be made to the connection
 * settings for the database.
 */
package epoverty;

import com.mysql.jdbc.Connection;
import java.awt.Color;
import java.awt.GridLayout;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Settings
{

   private final String settingsLogin = "Epoverty#Cis2770";
   private final String settingsPass = "Eliminating Poverty Settings";
   private final char[] passwordCheck = settingsPass.toCharArray();
   private Connection conn;
   private JCheckBox editable_CB;
   private JTextField url_TF;
   private JTextField driver_TF;
   private JTextField login_TF;
   private JTextField password_TF;

   public Settings() throws ClassNotFoundException
   {
      login();
   }

   private void login() throws ClassNotFoundException
   {
      // *****************************************ADJUST SETTINGS TO GO WITH L&F OF PROJECT
      UIManager.put("OptionPane.background", Color.DARK_GRAY);
      UIManager.put("OptionPane.messageForeground", Color.WHITE);
      UIManager.put("OptionPane.okButtonText", "Login");
      UIManager.put("Panel.background", Color.DARK_GRAY);
      UIManager.put("Label.foreground", Color.WHITE);

      // *****************************************CREATE AND ADD THE COMPONENTS
      JTextField loginSettings_TF = new JTextField("Epoverty#Cis2770");
      JPasswordField passwordSettings_TF = new JPasswordField("Eliminating Poverty Settings");
      JPanel panel = new JPanel(new GridLayout(0, 1));
      panel.add(new JLabel("Login:"));
      panel.add(loginSettings_TF);
      panel.add(new JLabel("Password:"));
      panel.add(passwordSettings_TF);
      int result = JOptionPane.showConfirmDialog(null, panel, "Settings Login",
         JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

      // *****************************************IF 'OK' IS HIT VERYIFY.  OTHERWISE CANCEL
      if (result == JOptionPane.OK_OPTION)
      {
         if (settingsLogin.equals(loginSettings_TF.getText()) && Arrays.equals(passwordCheck, passwordSettings_TF.getPassword()))
         {
            loginSettings();
         } else
         {
            new Warnings("Settings Login fail...");
         }
      } else
      {
         System.out.println("Cancelled");
      }
   }

   private void loginSettings() throws ClassNotFoundException
   {
      UIManager.put("OptionPane.okButtonText", "TEST CONNECTION");
      UIManager.put("CheckBox.background", Color.DARK_GRAY);
      UIManager.put("CheckBox.foreground", Color.WHITE);

      // *****************************************CREATE AND ADD THE COMPONENTS
      url_TF = new JTextField("");
      url_TF.setEditable(false);
      driver_TF = new JTextField("");
      driver_TF.setEditable(false);
      login_TF = new JTextField("");
      login_TF.setEditable(false);
      password_TF = new JTextField("");
      password_TF.setEditable(false);
      editable_CB = new JCheckBox("Edit Fields?");
      JPanel panel = new JPanel(new GridLayout(5, 2));
      panel.add(new JLabel("URL:"));
      panel.add(url_TF);
      panel.add(new JLabel("Driver:"));
      panel.add(driver_TF);
      panel.add(new JLabel("Login:"));
      panel.add(login_TF);
      panel.add(new JLabel("Password:"));
      panel.add(password_TF);
      panel.add(new JLabel(""));
      panel.add(editable_CB);

      // *****************************************ADD CHANGE LISTENER TO CHECK BOX
      editable_CB.addChangeListener(
         new ChangeListener()
         {

            public void stateChanged(ChangeEvent e)
            {
               if (editable_CB.isSelected())
               {
                  url_TF.setEditable(true);
                  driver_TF.setEditable(true);
                  login_TF.setEditable(true);
                  password_TF.setEditable(true);
               } else
               {
                  url_TF.setEditable(false);
                  driver_TF.setEditable(false);
                  login_TF.setEditable(false);
                  password_TF.setEditable(false);
               }
            }
         });

      // *****************************************INITIALIZE TF'S WITH CURRENT SETTINGS
      url_TF.setText(EPoverty.getURL());
      driver_TF.setText(EPoverty.getDriver());
      login_TF.setText(EPoverty.getLogin());
      password_TF.setText(EPoverty.getPassword());

      int result = JOptionPane.showConfirmDialog(null, panel, "Update Information",
         JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

      // *****************************************IF 'OK' IS HIT VERYIFY.  OTHERWISE CANCEL
      if (result == JOptionPane.OK_OPTION)
      {
         // **************************************TRY TO MAKE A CONNECTION, BEFORE ALLOWING A SAVE
         Class.forName(driver_TF.getText());
         try
         {
            conn = (Connection) DriverManager.getConnection(url_TF.getText(), login_TF.getText(), password_TF.getText());
            
            // **************************************SAVE CHANGES DIALOG BOX
            UIManager.put("OptionPane.okButtonText", "SAVE");
            JPanel resultPanel = new JPanel(new GridLayout(0, 2));
            resultPanel.add(new JLabel("Connection Made.\n Would you like to save these changes?"));
            int testResult = JOptionPane.showConfirmDialog(null, resultPanel, "Connection Made",
               JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // **************************************IF SAVE SETTINGS IS SELETED, SAVE THE SETTINGS
            if (testResult == JOptionPane.OK_OPTION)
            {
                UIManager.put("OptionPane.okButtonText", "OK");
               new Warnings("New Settings Saved");
            } else
            {
               System.out.println("Cancelled");
            }

         } catch (SQLException ex)
         {
             UIManager.put("OptionPane.okButtonText", "CONFIRM");
            new Warnings("Connection could not be made!");
         }
      } else
      {
         System.out.println("Cancelled");
      }
   }
}
