/*    This Class throws up a login screen and will try to establish a connection to 
 *    the database based on the information entered by the user.  A successful connection will
 *    close this screen, and open a new EPovertyMain.  An unsuccessful connection will popup a window
 *    explaining the error, and allow the user to try to login again.
 */
package epoverty;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public final class EPoverty extends JFrame
{
   //private static String url = "jdbc:mysql://50.63.244.60:3306/epoverty";
   private static String url = "jdbc:mysql://localhost:3306/epoverty";
   private static String driver = "com.mysql.jdbc.Driver";
   //private static String dbLogin = "epoverty";
   private static String dbLogin = "root";
   //private static String dbPassword = "Cis2770#";
   private static String dbPassword = "Tyrel171";
   private Connection conn;
   private BufferedImage background = ImageIO.read(new File("EPoverty2.jpg"));
   private BufferedImage login = ImageIO.read(new File("login.jpg"));
   private BufferedImage settings = ImageIO.read(new File("settingsIcon.jpg"));
   private JTextField login_TF;
   private JTextField password_TF;
   private JPanel mainPanel;

   //*********************************************CONSTRUCTOR METHOD
   public EPoverty() throws IOException
   {
      UIManager.put("Button.select", Color.BLACK);

      //******************************************CREATE MAIN PANEL
      setTitle("|||E-Poverty || V-0.1 (SKELETON)|||");
      setSize(500, 275);
      setLocation(500, 200);
      setVisible(true);
      setResizable(false);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      mainPanel = new JPanel();
      setContentPane(mainPanel);
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
      //******************************************END OF CREATING MAIN PANEL

      /* CREATE THE TOP AND BOTTOM PANELS, ADDING BACKGROUND IMAGES
       *
       * BackgroundPanel is an extension to JPanel that makes it easy to 
       * paint images as a background on the panel, or add a gradient
       * fill as the background (this is what you currently see on the 
       * back of all the tabs.  The constructors can be seen in the 
       * BackgroundPanel.java class if you need or want to use them.
       */
      BackgroundPanel topPanel = new BackgroundPanel(background);
      Dimension top = new Dimension(500, 125);
      topPanel.setPreferredSize(top);
      GradientPaint paintTop = new GradientPaint(0, 0, Color.DARK_GRAY, 100, 100, Color.BLACK);
      topPanel.setPaint(paintTop);

      BackgroundPanel bottomPanel = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      Dimension bottom = new Dimension(500, 75);
      bottomPanel.setPreferredSize(bottom);
      GradientPaint paintBottom = new GradientPaint(50, 200, Color.WHITE, 100, 50, Color.BLACK);
      bottomPanel.setPaint(paintBottom);

      //*******************************************ADD PANELS TO THE MAIN
      mainPanel.add(topPanel);
      mainPanel.add(bottomPanel);

      //******************************************ADD COMPONENTS TO THE BOTTOM PANEL
      bottomPanel.setLayout(new GridBagLayout());
      GridBagConstraints gBC = new GridBagConstraints();
      gBC.fill = GridBagConstraints.HORIZONTAL;

      // *****************************************FIRST ROW (BLANK)
      JLabel blank = new JLabel("");
      gBC.weightx = 1;
      gBC.weighty = 1;
      gBC.gridx = 0;
      gBC.gridy = 0;
      bottomPanel.add(blank, gBC);

      blank = new JLabel("");
      gBC.gridx = 1;
      gBC.gridy = 0;
      bottomPanel.add(blank, gBC);

      blank = new JLabel("");
      gBC.gridx = 2;
      gBC.gridy = 0;
      bottomPanel.add(blank, gBC);

      blank = new JLabel("");
      gBC.gridx = 3;
      gBC.gridy = 0;
      bottomPanel.add(blank, gBC);

      blank = new JLabel("");
      gBC.gridx = 4;
      gBC.gridy = 0;
      bottomPanel.add(blank, gBC);

      blank = new JLabel("");
      gBC.gridx = 5;
      gBC.gridy = 0;
      bottomPanel.add(blank, gBC);

      // *****************************************SECOND ROW (LOGIN)
      blank = new JLabel("LOGIN");
      gBC.gridx = 1;
      gBC.gridy = 1;
      gBC.gridheight = 2;
      gBC.gridwidth = 2;
      blank.setForeground(Color.WHITE);
      blank.setFont(new Font("Serif", Font.PLAIN, 16));
      bottomPanel.add(blank, gBC);
      blank.setHorizontalTextPosition(SwingConstants.RIGHT);

      login_TF = new JTextField("email@email.com");
      gBC.gridx = 3;
      gBC.gridy = 1;
      login_TF.setEditable(true);
      login_TF.setBackground(Color.WHITE);
      login_TF.setFont(new Font("Serif", Font.PLAIN, 16));
      login_TF.setForeground(Color.LIGHT_GRAY);
      bottomPanel.add(login_TF, gBC);
      login_TF.requestFocusInWindow();

      //******************************************THIRD ROW (PASSWORD)
      blank = new JLabel("PASSWORD");
      gBC.gridx = 1;
      gBC.gridy = 3;
      blank.setForeground(Color.WHITE);
      blank.setFont(new Font("Serif", Font.PLAIN, 16));
      blank.setHorizontalTextPosition(JLabel.RIGHT);
      bottomPanel.add(blank, gBC);

      password_TF = new JPasswordField("password");
      gBC.gridx = 3;
      gBC.gridy = 3;
      password_TF.setEditable(true);
      password_TF.setForeground(Color.WHITE);
      password_TF.setFont(new Font("Serif", Font.PLAIN, 16));
      bottomPanel.add(password_TF, gBC);

      // *****************************************FOURTH ROW (BLANK)
      blank = new JLabel("");
      gBC.gridx = 1;
      gBC.gridy = 3;
      gBC.gridheight = 1;
      bottomPanel.add(blank, gBC);

      // *****************************************FIFTH ROW (BUTTONS)
      JButton login_BTN = new JButton("");
      gBC.gridx = 3;
      gBC.gridy = 5;
      gBC.gridwidth = 1;
      login_BTN.setIcon(new ImageIcon(login));
      login_BTN.setMargin(new Insets(0, 0, 0, 0));
      login_BTN.setBackground(Color.LIGHT_GRAY);
      login_BTN.setRolloverEnabled(false);
      login_BTN.setFocusPainted(false);
      login_BTN.setContentAreaFilled(false);
      bottomPanel.add(login_BTN, gBC);

      JButton settings_BTN = new JButton("Settings");
      gBC.gridx = 5;
      gBC.gridy = 5;
      gBC.gridheight = 3;
      settings_BTN.setIcon(new ImageIcon(settings));
      settings_BTN.setMargin(new Insets(0, 0, 0, 0));
      settings_BTN.setHorizontalAlignment(SwingConstants.RIGHT);
      settings_BTN.setVerticalTextPosition(SwingConstants.CENTER);
      settings_BTN.setHorizontalTextPosition(SwingConstants.LEFT);
      settings_BTN.setBackground(Color.LIGHT_GRAY);
      settings_BTN.setBorderPainted(false);
      settings_BTN.setRolloverEnabled(false);
      settings_BTN.setFocusPainted(false);
      settings_BTN.setContentAreaFilled(false);
      bottomPanel.add(settings_BTN, gBC);

      // *****************************************SIXTH ROW (BLANK)
      blank = new JLabel("");
      gBC.gridx = 1;
      gBC.gridy = 5;
      bottomPanel.add(blank, gBC);

      // This makes sure all components are painted on the screen correctly
      mainPanel.validate();

      // ADD AN ACTION LISTENER TO THE BUTTON THAT WILL CHECK THE LOGIN INFO AND LOG INTO THE DATABASE
      login_BTN.addActionListener(
         new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               String passwordHash = "";
               // Hash the password for comparison
               try
               {
                  passwordHash = SimpleSHA1.SHA1(password_TF.getText());
               }
               catch (NoSuchAlgorithmException ex)
               {
                  Logger.getLogger(EPoverty.class.getName()).log(Level.SEVERE, null, ex);
               }
               catch (UnsupportedEncodingException ex)
               {
                  Logger.getLogger(EPoverty.class.getName()).log(Level.SEVERE, null, ex);
               }

               String checkAdminSQL = "SELECT p.password FROM persons AS p, admins AS a "
                  + "WHERE p.personId = a.personId "
                  + "AND p.emailAddress = \"" + login_TF.getText() + "\" ";

               try
               {
                  // *********************************CHECK LOGIN AND PASSWORD
                  new DBconnection(url, driver, dbLogin, dbPassword);
                  conn = DBconnection.getConnection();
                  PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(checkAdminSQL);
                  ResultSet rs = (ResultSet) stmt.executeQuery();

                  // IF THERE IS NO NEXT RECORD, E-MAIL IS NOT THAT OF AN ADMIN
                  if (!rs.next())
                  {
                     new Warnings("You do not have login rights.");
                  }
                  else
                  {
                     String dbPass = rs.getObject(1).toString();

                     // CHECK VALUE OF RETURNED PASSWORD TO VALUE ENTERED                    
                     if (dbPass.equals(passwordHash))
                     {
                        try
                        {
                           new EPovertyMain();
                           EPovertyMain.updateStatus("Logged in");
                           dispose();

                        }
                        catch (IOException ex)
                        {
                           Logger.getLogger(EPoverty.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     }
                     else
                     {
                        new Warnings("Incorrect Password.");
                     }
                  }

                  rs.close();
                  stmt.close();
                  DBconnection.closeConnection();


               }
               catch (ClassNotFoundException ex)
               {
                  Logger.getLogger(EPoverty.class.getName()).log(Level.SEVERE, null, ex);
               }
               catch (SQLException ex)
               {
                  Logger.getLogger(EPoverty.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
         }); // END OF LOGIN BUTTON LISTENER

      settings_BTN.addActionListener(
         new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               try
               {
                  new Settings();
               }
               catch (ClassNotFoundException ex)
               {
                  Logger.getLogger(EPoverty.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
         }); // END OF SETTINGS BUTTON LISTENER
   }
   //*********************************************END OF CONSTRUCTOR

   //*********************************************GET METHODS FOR CONNECTION SETTINGS
   protected static String getURL()
   {
      return url;
   }

   protected static String getDriver()
   {
      return driver;
   }

   protected static String getLogin()
   {
      return dbLogin;
   }

   protected static String getPassword()
   {
      return dbPassword;
   }
   //*********************************************END OF GET METHODS

   //*********************************************MAIN
   public static void main(String[] args) throws IOException
   {
      new EPoverty();
   }
   //*********************************************END OF MAIN
}
