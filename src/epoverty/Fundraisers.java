package epoverty;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.ResultSetMetaData;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class Fundraisers extends JPanel
{
   private final int width = super.getWidth();
   private final int height = super.getHeight();
   private File photoFile;
   private BufferedImage defaultProfilePic = Util.resizeImage(ImageIO.read(new File("default.jpg")), 200, 200);
   private JPanel mainPanel;
   private myTable fundraisersTable;
   private JTextField searchTF, raiseGoalTF, firstNameTF, middleNameTF, lastNameTF, phoneTF, emailTF,
      streetTF, cityTF, stateTF, zipTF, photoTF, passwordTF;
   private JComboBox fundraiserList, expeditionsList;
   private JLabel name, phone, email, street, cityState, tripName, status, goalRaised, picture;
   private JButton updateBTN, browseBTN;
   private ArrayList possibleNames;
   private JCheckBox editableCB;
   private JFileChooser photoFC;
   private JProgressBar goalProgress;
   private Connection conn;
   private String personSelected, personID;

   Fundraisers(JTabbedPane tabs) throws IOException, ClassNotFoundException, SQLException
   {
      //******************************************Set up UI
      UIManager.put("Table.background", Color.DARK_GRAY);
      UIManager.put("Table.foreground", Color.WHITE);
      UIManager.put("Table.font", new Font("SansSerif", Font.BOLD, 14));
      UIManager.put("Table.selectionBackground", Color.GREEN);
      UIManager.put("Table.selectionForeground", Color.BLACK);
      UIManager.put("Table.gridColor", Color.BLACK);
      UIManager.put("TableHeader.background", Color.GRAY);
      UIManager.put("TableHeader.foreground", Color.GREEN);
      UIManager.put("TableHeader.font", new Font("SansSerif", Font.BOLD, 16));
      UIManager.put("OptionPane.background", Color.DARK_GRAY);
      UIManager.put("OptionPane.messageForeground", Color.WHITE);
      UIManager.put("OptionPane.okButtonText", "Login");
      UIManager.put("Panel.background", Color.DARK_GRAY);
      UIManager.put("Label.foreground", Color.WHITE);
      UIManager.put("Label.foreground", Color.WHITE);
      UIManager.put("Label.font", new Font("Serif", Font.PLAIN, 18));
      UIManager.put("TextField.font", new Font("Serif", Font.PLAIN, 18));
      UIManager.put("TextField.foreground", Color.BLACK);
      UIManager.put("Button.background", EPovertyMain.darkest);
      UIManager.put("Button.foreground", Color.WHITE);
      UIManager.put("TextField.background", new Color(100, 100, 100));
      UIManager.put("TextField.foreground", Color.WHITE);
      UIManager.put("PasswordField.background", new Color(100, 100, 100));
      UIManager.put("PasswordField.foreground", Color.WHITE);
      UIManager.put("CheckBox.background", Color.DARK_GRAY);
      UIManager.put("CheckBox.foreground", Color.WHITE);

      GridBagConstraints gBC = new GridBagConstraints();
      Border compoundProgress;
      Border raisedbevel = BorderFactory.createRaisedBevelBorder();
      Border loweredbevel = BorderFactory.createLoweredBevelBorder();
      Border border = BorderFactory.createLineBorder(Color.GREEN);
      compoundProgress = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
      compoundProgress = BorderFactory.createCompoundBorder(border, compoundProgress);

      // *****************************************MAIN PANEL
      mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
      Dimension mainFundraiser = new Dimension(width, height - 15);
      mainPanel.setPreferredSize(mainFundraiser);
      mainPanel.setBackground(Color.DARK_GRAY);

      // *****************************************CREATE TOP OF TAB
      BackgroundPanel topFundraisersTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      topFundraisersTab.setBorder(compoundProgress);
      topFundraisersTab.setLayout(new GridLayout(0, 1, 1, 1));
      Dimension topFundraiser = new Dimension(width, 520);
      topFundraisersTab.setPreferredSize(topFundraiser);
      GradientPaint paintTopFundraiser = new GradientPaint(0, 0, Color.BLACK, 100, 100, Color.DARK_GRAY);
      topFundraisersTab.setPaint(paintTopFundraiser);

      // *****************************************CREATE MIDDLE OF THE TAB
      BackgroundPanel middleFundraisersTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      middleFundraisersTab.setBorder(compoundProgress);
      middleFundraisersTab.setLayout(new GridBagLayout());
      middleFundraisersTab.setBorder(BorderFactory.createLineBorder(Color.WHITE));
      Dimension middleFundraiser = new Dimension(width, 50);
      middleFundraisersTab.setPreferredSize(middleFundraiser);
      GradientPaint paintmiddleFundraiser = new GradientPaint(0, 0, Color.BLACK, 100, 100, Color.DARK_GRAY);
      middleFundraisersTab.setPaint(paintmiddleFundraiser);

      // *****************************************CREATE BOTTOM OF THE TAB
      BackgroundPanel bottomFundraisersTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      bottomFundraisersTab.setBorder(compoundProgress);
      bottomFundraisersTab.setLayout(new GridBagLayout());
      Dimension bottomFundraiser = new Dimension(width, 150);
      bottomFundraisersTab.setPreferredSize(bottomFundraiser);
      GradientPaint paintBottomFundraiser = new GradientPaint(0, 150, Color.LIGHT_GRAY, 100, 50, Color.DARK_GRAY);
      bottomFundraisersTab.setPaint(paintBottomFundraiser);

      // *****************************************CREATE AND ADD TABLE TO THE TOP OF TAB
      fundraisersTable = new myTable();
      JTable dataTable = new JTable(fundraisersTable);
      JScrollPane scrollPane = new JScrollPane(dataTable);
      dataTable.setFillsViewportHeight(true);
      topFundraisersTab.add(scrollPane);

      // *****************************************CREATE AND ADD COMPONENTS TO THE MIDDLE OF TAB  
      // COLUMN 1      
      Dimension componentsSize = new Dimension(450, 20);
      JLabel trip = new JLabel("TRIP:");
      gBC.fill = GridBagConstraints.NONE;
      gBC.anchor = GridBagConstraints.WEST;
      gBC.weightx = 0.5;
      gBC.gridx = 0;
      gBC.gridy = 0;
      middleFundraisersTab.add(trip, gBC);
      trip.setMaximumSize(componentsSize);
      trip.setMinimumSize(componentsSize);

      tripName = new JLabel(" ");
      gBC.gridx = 1;
      middleFundraisersTab.add(tripName, gBC);
      tripName.setMinimumSize(componentsSize);
      tripName.setMaximumSize(componentsSize);

      JLabel amountRaised = new JLabel("AMOUNT RAISED:");
      gBC.gridx = 2;
      middleFundraisersTab.add(amountRaised, gBC);
      amountRaised.setMaximumSize(componentsSize);
      amountRaised.setMinimumSize(componentsSize);

      goalRaised = new JLabel(" ");
      gBC.gridx = 3;
      middleFundraisersTab.add(goalRaised, gBC);
      goalRaised.setMaximumSize(componentsSize);
      goalRaised.setMinimumSize(componentsSize);

      JLabel statusLabel = new JLabel("STATUS:");
      gBC.gridx = 4;
      middleFundraisersTab.add(statusLabel, gBC);
      statusLabel.setMaximumSize(componentsSize);
      statusLabel.setMinimumSize(componentsSize);

      status = new JLabel(" ");
      gBC.gridx = 5;
      middleFundraisersTab.add(status, gBC);
      status.setMaximumSize(componentsSize);
      status.setMinimumSize(componentsSize);

      goalProgress = new JProgressBar();
      Border redline = BorderFactory.createLineBorder(Color.BLACK);
      compoundProgress = BorderFactory.createTitledBorder(compoundProgress, "PROGRESS", TitledBorder.CENTER, TitledBorder.TOP, null, Color.WHITE);
      goalProgress.setBorder(redline);
      gBC.gridx = 0;
      gBC.gridy = 1;
      gBC.weighty = 1;
      gBC.weightx = 1;
      gBC.ipadx = 1000;
      gBC.gridwidth = 6;
      gBC.gridheight = 2;
      middleFundraisersTab.add(goalProgress, gBC);
      Dimension progressBarDim = new Dimension(1800, 10);
      goalProgress.setMaximumSize(progressBarDim);
      goalProgress.setMinimumSize(progressBarDim);

      // *****************************************CREATE AND ADD COMPONENTS TO THE BOTTOM OF TAB
      // COLUMN 1
      componentsSize = new Dimension(250, 30);
      JLabel nameLabel = new JLabel("Name: ");
      nameLabel.setForeground(EPovertyMain.font);
      gBC.ipadx = 0;
      gBC.ipady = 0;
      gBC.insets = new Insets(0, 0, 0, 0);
      gBC.gridwidth = 1;
      gBC.gridheight = 1;
      gBC.anchor = GridBagConstraints.EAST;
      gBC.gridx = 0;
      gBC.gridy = 0;
      bottomFundraisersTab.add(nameLabel, gBC);

      JLabel phoneLabel = new JLabel("Phone Number: ");
      phoneLabel.setForeground(EPovertyMain.font);
      gBC.gridy = 1;
      bottomFundraisersTab.add(phoneLabel, gBC);

      JLabel emailLabel = new JLabel("E-Mail: ");
      emailLabel.setForeground(EPovertyMain.font);
      gBC.gridy = 2;
      bottomFundraisersTab.add(emailLabel, gBC);

      searchTF = new JTextField("", 75);
      Border compoundSearch;
      compoundSearch = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
      compoundSearch = BorderFactory.createCompoundBorder(redline, compoundSearch);
      compoundSearch = BorderFactory.createTitledBorder(compoundSearch, "SEARCH", TitledBorder.CENTER, TitledBorder.TOP, null, Color.WHITE);
      searchTF.setHorizontalAlignment(JTextField.RIGHT);
      searchTF.setBorder(compoundSearch);
      gBC.fill = GridBagConstraints.HORIZONTAL;
      gBC.gridy = 3;
      bottomFundraisersTab.add(searchTF, gBC);

      // COLUMN 2 
      name = new JLabel("");
      gBC.fill = GridBagConstraints.NONE;
      gBC.anchor = GridBagConstraints.WEST;
      gBC.gridx = 1;
      gBC.gridy = 0;
      bottomFundraisersTab.add(name, gBC);
      name.setMinimumSize(componentsSize);
      name.setMaximumSize(componentsSize);

      phone = new JLabel("");
      gBC.gridy = 1;
      bottomFundraisersTab.add(phone, gBC);
      phone.setMinimumSize(componentsSize);
      phone.setMaximumSize(componentsSize);

      email = new JLabel("");
      gBC.gridy = 2;
      bottomFundraisersTab.add(email, gBC);
      email.setMinimumSize(componentsSize);
      email.setMaximumSize(componentsSize);

      possibleNames = DBconnection.getFundraisers();
      DBconnection.closeConnection();
      fundraiserList = new JComboBox(possibleNames.toArray());
      fundraiserList.setSelectedIndex(0);
      fundraiserList.setForeground(Color.BLACK);
      fundraiserList.setBackground(Color.WHITE);
      gBC.gridy = 3;
      bottomFundraisersTab.add(fundraiserList, gBC);
      fundraiserList.setMinimumSize(componentsSize);
      fundraiserList.setMaximumSize(componentsSize);

      // COLUMN 3       
      JLabel addressLabel = new JLabel("Address: ");
      addressLabel.setForeground(EPovertyMain.font);
      gBC.gridx = 2;
      gBC.gridy = 1;
      bottomFundraisersTab.add(addressLabel, gBC);

      // COLUMN 4
      street = new JLabel(" ");
      gBC.gridx = 3;
      bottomFundraisersTab.add(street, gBC);
      street.setMinimumSize(componentsSize);
      street.setMaximumSize(componentsSize);

      cityState = new JLabel(" ");
      gBC.gridy = 2;
      bottomFundraisersTab.add(cityState, gBC);
      cityState.setMinimumSize(componentsSize);
      cityState.setMaximumSize(componentsSize);

      // COLUMN 5
      picture = new JLabel(new ImageIcon(defaultProfilePic));
      gBC.anchor = GridBagConstraints.WEST;
      gBC.gridx = 4;
      gBC.gridy = 0;
      gBC.gridheight = 4;
      bottomFundraisersTab.add(picture, gBC);
      Dimension photoSize = new Dimension(200, 200);
      picture.setMaximumSize(photoSize);
      picture.setMinimumSize(photoSize);

      updateBTN = new JButton("UPDATE");
      gBC.gridheight = 1;
      gBC.gridx = 2;
      gBC.gridy = 3;
      bottomFundraisersTab.add(updateBTN, gBC);
      updateBTN.setOpaque(true);

      // CALL UPDATES TO INITIALIZE THE TABLE AND CONTACT INFO 
      personSelected = fundraiserList.getSelectedItem() + "";
      personID = personSelected.substring((personSelected.lastIndexOf(',') + 2), (personSelected.length() - 1));
      updateContactInfo(Integer.parseInt(personID));
      updateTable(Integer.parseInt(personID));

      // ADD TOP AND BOTTOM TO THE MAIN AND PUT THEM INTO A TAB
      mainPanel.add(topFundraisersTab);
      mainPanel.add(middleFundraisersTab);
      mainPanel.add(bottomFundraisersTab);
      mainPanel.revalidate();
      tabs.add("", mainPanel);

      //*********************************************ACTION LISTENER FOR UPDATE BUTTON
      updateBTN.addActionListener(
         new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               ArrayList tripsList = new ArrayList();
               UIManager.put("OptionPane.okButtonText", "SAVE CHANGES");
               // *****************************************CREATE AND ADD THE COMPONENTS
               firstNameTF = new JTextField(12);
               firstNameTF.setEditable(false);
               middleNameTF = new JTextField(12);
               middleNameTF.setEditable(false);
               lastNameTF = new JTextField(12);
               lastNameTF.setEditable(false);
               phoneTF = new JTextField(12);
               phoneTF.setEditable(false);
               emailTF = new JTextField(12);
               emailTF.setEditable(false);
               streetTF = new JTextField(12);
               streetTF.setEditable(false);
               raiseGoalTF = new JTextField(12);
               raiseGoalTF.setEditable(false);
               cityTF = new JTextField(12);
               cityTF.setEditable(false);
               stateTF = new JTextField(2);
               stateTF.setEditable(false);
               zipTF = new JTextField(5);
               zipTF.setEditable(false);
               photoFC = new JFileChooser();
               FileFilter photoFilter = new FileNameExtensionFilter("Image Files", "jpg", "png");
               photoFC.setFileFilter(photoFilter);
               photoTF = new JTextField(12);
               photoTF.setEditable(false);
               browseBTN = new JButton("BROWSE");
               passwordTF = new JPasswordField(12);
               passwordTF.setEditable(false);
               editableCB = new JCheckBox("Edit Fields?");
               try
               {
                  tripsList = DBconnection.getTrips();
               }
               catch (SQLException ex)
               {
                  Logger.getLogger(Fundraisers.class.getName()).log(Level.SEVERE, null, ex);
               }
               catch (ClassNotFoundException ex)
               {
                  Logger.getLogger(Fundraisers.class.getName()).log(Level.SEVERE, null, ex);
               }
               expeditionsList = new JComboBox(tripsList.toArray());

               JPanel panel = new JPanel(new GridBagLayout());
               GridBagConstraints gBC = new GridBagConstraints();
               gBC.anchor = GridBagConstraints.WEST;

               // COLUMN 1
               gBC.weightx = 1;
               gBC.weighty = 1;
               gBC.gridx = 0;
               gBC.gridy = 0;
               panel.add(new JLabel("First:"), gBC);

               gBC.gridy = 1;
               panel.add(new JLabel("Phone:"), gBC);

               gBC.gridy = 2;
               panel.add(new JLabel("E-Mail:"), gBC);

               gBC.gridy = 3;
               panel.add(new JLabel("Raise Goal:"), gBC);

               gBC.gridy = 4;
               panel.add(new JLabel("Expedition:"), gBC);

               // COLUMN 2
               gBC.gridx = 1;
               gBC.gridy = 0;
               panel.add(firstNameTF, gBC);

               gBC.gridy = 1;
               panel.add(phoneTF, gBC);

               gBC.gridy = 2;
               panel.add(emailTF, gBC);

               gBC.gridy = 3;
               panel.add(raiseGoalTF, gBC);

               gBC.gridy = 4;
               gBC.fill = GridBagConstraints.HORIZONTAL;
               panel.add(expeditionsList, gBC);

               // COLUMN 3
               gBC.gridx = 2;
               gBC.gridy = 0;
               gBC.fill = GridBagConstraints.NONE;
               panel.add(new JLabel("Middle:"), gBC);

               gBC.gridy = 1;
               panel.add(new JLabel("Address:"), gBC);

               gBC.gridy = 2;
               panel.add(new JLabel("City:"), gBC);

               gBC.gridy = 4;
               panel.add(new JLabel("Picture:"), gBC);

               gBC.gridy = 5;
               panel.add(new JLabel("Password:"), gBC);

               // COLUMN 4
               gBC.gridx = 3;
               gBC.gridy = 0;
               panel.add(middleNameTF, gBC);

               gBC.gridy = 1;
               panel.add(streetTF, gBC);

               gBC.gridy = 2;
               panel.add(cityTF, gBC);

               gBC.gridy = 4;
               panel.add(photoTF, gBC);

               gBC.gridy = 5;
               panel.add(passwordTF, gBC);

               gBC.gridy = 6;
               panel.add(editableCB, gBC);

               // COLUMN 5
               gBC.gridx = 4;
               gBC.gridy = 0;
               panel.add(new JLabel("Last:"), gBC);

               gBC.gridy = 2;
               panel.add(new JLabel("State:"), gBC);

               gBC.gridy = 3;
               panel.add(new JLabel("Zip:"), gBC);

               // COLUMN 6
               gBC.gridx = 5;
               gBC.gridy = 0;
               panel.add(lastNameTF, gBC);

               gBC.gridy = 2;
               panel.add(stateTF, gBC);

               gBC.gridy = 3;
               panel.add(zipTF, gBC);

               gBC.gridy = 4;
               panel.add(browseBTN, gBC);

               // *****************************************ADD LISTENER TO BROWSE BUTTON
               browseBTN.addActionListener(
                  new ActionListener()
                  {
                     @Override
                     public void actionPerformed(ActionEvent ae)
                     {
                        // GETS THE APPROVED PICTURE AND STORES IT AS THE photoFILE TO BE UPLOADED
                        if (photoFC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                        {
                           photoFile = photoFC.getSelectedFile();
                           photoTF.setText(photoFile.getName());
                        }
                     }
                  });

               // *****************************************ADD CHANGE LISTENER TO CHECK BOX
               editableCB.addChangeListener(
                  new ChangeListener()
                  {
                     @Override
                     public void stateChanged(ChangeEvent e)
                     {
                        if (editableCB.isSelected())
                        {
                           firstNameTF.setEditable(true);
                           middleNameTF.setEditable(true);
                           lastNameTF.setEditable(true);
                           phoneTF.setEditable(true);
                           emailTF.setEditable(true);
                           streetTF.setEditable(true);
                           cityTF.setEditable(true);
                           stateTF.setEditable(true);
                           zipTF.setEditable(true);
                           photoFC.setEnabled(true);
                           passwordTF.setEditable(true);
                           raiseGoalTF.setEditable(true);
                        }
                        else
                        {
                           firstNameTF.setEditable(false);
                           middleNameTF.setEditable(false);
                           lastNameTF.setEditable(false);
                           phoneTF.setEditable(false);
                           emailTF.setEditable(false);
                           streetTF.setEditable(false);
                           cityTF.setEditable(false);
                           stateTF.setEditable(false);
                           zipTF.setEditable(false);
                           photoFC.setEnabled(false);
                           passwordTF.setEditable(false);
                           raiseGoalTF.setEditable(false);
                        }
                     }
                  });
               try
               {
                  // GET PERSON ID AND USE IT TO GET INFO FOR THE UPDATE PAGE
                  String personSelected = fundraiserList.getSelectedItem() + "";
                  // CHECK TO MAKE SURE THAT THERE IS SOMEONE SELECTED IN COMBO BOX
                  if (!personSelected.contains("ul"))
                  {
                     // GET personId from the combobox
                     String personID = personSelected.substring((personSelected.lastIndexOf(',') + 2), (personSelected.length() - 1));
                     // GET all the info for the update by calling getInfoForUpdate method
                     getInfoForUpdate(Integer.parseInt(personID));
                     // DISPLAY selected persons information
                     int result = JOptionPane.showConfirmDialog(null, panel, "Update Fundraiser Information",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                     // *****************************************IF 'OK' IS HIT VERYIFY.  OTHERWISE CANCEL
                     if (result == JOptionPane.OK_OPTION)
                     {


                        try
                        {
                           // CALL TO UPDATE THE DATABASE
                           updateDatabase(Integer.parseInt(personID));
                        }
                        catch (IOException ex)
                        {
                           Logger.getLogger(Fundraisers.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        catch (NoSuchAlgorithmException ex)
                        {
                           Logger.getLogger(Fundraisers.class.getName()).log(Level.SEVERE, null, ex);
                        }

                     }

                  }
                  else
                  {
                     new Warnings("No Person Selected\n Please select someone first.");
                  }
               }
               catch (SQLException ex)
               {
                  Logger.getLogger(Fundraisers.class.getName()).log(Level.SEVERE, null, ex);
               }
               catch (ClassNotFoundException ex)
               {
                  Logger.getLogger(Fundraisers.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
         });

      searchTF.addKeyListener(
         new KeyListener()
         {
            @Override
            public void keyTyped(KeyEvent e)
            {
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
               JTextField textField = (JTextField) e.getSource();
               String text = textField.getText();
               textField.setText(text);
               try
               {
                  updateComboBox();
               }
               catch (SQLException ex)
               {
                  Logger.getLogger(Fundraisers.class.getName()).log(Level.SEVERE, null, ex);
               }
               catch (ClassNotFoundException ex)
               {
                  Logger.getLogger(Fundraisers.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
         });

      // ITEM LISTENER FOR COMBO BOX
      fundraiserList.addItemListener(
         new ItemListener()
         {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
               // WHEN SOMEONE IS SELECTED FROM THE COMBOBOX GET THEIR INFORMATION
               if (e.getStateChange() == ItemEvent.DESELECTED)
               {
               }
               if (e.getStateChange() == ItemEvent.SELECTED)
               {
                  personSelected = fundraiserList.getSelectedItem() + "";
                  personID = personSelected.substring((personSelected.lastIndexOf(',') + 2), (personSelected.length() - 1));
                  // UPDATE CONTACT INFO AND TABLE
                  try
                  {
                     updateContactInfo(Integer.parseInt(personID));
                     updateTable(Integer.parseInt(personID));

                  }
                  catch (IOException ex)
                  {
                     Logger.getLogger(Fundraisers.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  catch (ClassNotFoundException ex)
                  {
                     Logger.getLogger(Fundraisers.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  catch (SQLException ex)
                  {
                     Logger.getLogger(Fundraisers.class.getName()).log(Level.SEVERE, null, ex);
                  }
               }
            }
         });
      // END OF ITEM LISTENER FOR COMBO BOX      
   }

   // ********************************************METHOD TO UPDATE CONTACT INFO BASED ON PERSON SELECTED
   private void updateContactInfo(int personID) throws SQLException, ClassNotFoundException, IOException
   {
      String getInfoQuery = ("SELECT firstName, middleName, lastName, phoneNumber, emailAddress, addressStreet, addressCity, "
                             + "addressState, addressZip"
                             + " FROM persons WHERE personID =" + personID);
      String allInfo = "";

      conn = DBconnection.getConnection();
      PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(getInfoQuery);
      ResultSet rs = (ResultSet) stmt.executeQuery();
      ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
      int columns = md.getColumnCount();

      // GET ALL THE DATA FOR THE PERSON
      if (rs.next())
      {
         for (int i = 1; i <= columns; i++)
         {
            if (rs.getObject(i) != null)
            {
               allInfo = allInfo + rs.getObject(i);
            }
            else
            {
               allInfo = allInfo + " ";
            }
            allInfo = allInfo + ",";
         }
      }

      rs.close();
      stmt.close();

      // PUT THE DATA INTO AN ARRAY AND USE THAT TO RECREATE INFO ON SCREEN
      String[] results = allInfo.split(",");
      BufferedImage profilePicture = DBconnection.getPhoto(personID);
      if (profilePicture != null)
      {
         profilePicture = Util.resizeImage(profilePicture, 400, 400);
         picture.setIcon(new ImageIcon(profilePicture));
      }
      else
      {
         picture.setIcon(new ImageIcon(defaultProfilePic));
      }
      name.setText(results[2] + ", " + results[0] + " " + results[1]);
      phone.setText(results[3]);
      email.setText(results[4]);
      street.setText(results[5]);
      cityState.setText(results[6] + ", " + results[7] + " " + results[8]);
      DBconnection.closeConnection();
   }

   // ********************************************METHOD TO UPDATE CONTACT INFO BASED ON PERSON SELECTED
   private void getInfoForUpdate(int personID) throws SQLException, ClassNotFoundException
   {
      String getInfoQuery = "SELECT * FROM persons, fundraisers WHERE persons.personId =" + personID
         + " AND fundraisers.personId = " + personID;
      String allInfo = "";

      conn = DBconnection.getConnection();
      PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(getInfoQuery);
      ResultSet rs = (ResultSet) stmt.executeQuery();
      ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
      int columns = md.getColumnCount();

      // GET ALL THE DATA FROM THE PERSONS AND FUNDRAISERS TABLES
      if (rs.next())
      {
         for (int i = 1; i <= columns; i++)
         {
            if (rs.getObject(i) != null)
            {
               allInfo = allInfo + rs.getObject(i);
            }
            else
            {
               allInfo = allInfo + " ";
            }
            allInfo = allInfo + ",";
         }
      }

      // GET ALL THE EXPEDITION NAMES
      rs.close();
      stmt.close();
      DBconnection.closeConnection();

      // PUT THE DATA INTO AN ARRAY AND USE THAT TO RECREATE INFO ON SCREEN
      String[] results = allInfo.split(",");
      firstNameTF.setText(results[1]);
      middleNameTF.setText(results[2]);
      lastNameTF.setText(results[3]);
      phoneTF.setText(results[4]);
      emailTF.setText(results[5]);
      streetTF.setText(results[6]);
      cityTF.setText(results[7]);
      stateTF.setText(results[8]);
      zipTF.setText(results[9]);
      passwordTF.setText(results[11]);
      raiseGoalTF.setText(results[15]);
      //expeditionsList.setSelectedIndex(Integer.parseInt(results[14]));
   }

   // ********************************************METHOD TO UPDATE THE TABLE
   private void updateTable(int personId) throws SQLException, ClassNotFoundException
   {
      // CLEAR THE TABLE
      fundraisersTable.getDataVector().removeAllElements();

      String donationsMadeQuery = "SELECT p.firstName, d.donationAmount, d.donationDate "
         + "FROM persons AS p, donations AS d, fundraisers AS f, donors AS dnr "
         + "WHERE p.personId = dnr.personId "
         + "AND dnr.donorId = d.donorId "
         + "AND d.fundraiserId = f.fundraiserId "
         + "AND f.personId = " + personId;

      String tripStatusQuery = "SELECT e.name, e.cutoffDate, f.raiseGoal, f.raised "
         + "FROM expeditions AS e, fundraisers AS f "
         + "WHERE f.personId = " + personId + " "
         + "AND f.expeditionId = e.expeditionId";

      // GET CONNECTION AND RUN QUERY FOR TABLE
      Connection ePoverty = DBconnection.getConnection();
      PreparedStatement tableStmt = (PreparedStatement) ePoverty.prepareStatement(donationsMadeQuery);
      ResultSet tableRS = (ResultSet) tableStmt.executeQuery();
      ResultSetMetaData tableMD = (ResultSetMetaData) tableRS.getMetaData();


      // PUT THE META DATA INTO THE TABLE
      int fundraiserColCount = tableMD.getColumnCount();
      String[] columnNames =
      {
         "DONOR", "AMOUNT", "DATE"
      };
      fundraisersTable.setColumnIdentifiers(columnNames);

      while (tableRS.next())
      {
         Object[] data = new String[fundraiserColCount];
         for (int i = 1; i <= fundraiserColCount; i++)
         {
            data[i - 1] = tableRS.getString(i);
         }
         fundraisersTable.addRow(data);
      }
      fundraisersTable.fireTableDataChanged();

      // CLOSE STATEMENT and RESULT SET
      tableRS.close();
      tableStmt.close();

      // RUN QUERY FOR TRIP STATUS BAR
      PreparedStatement statusStmt = (PreparedStatement) ePoverty.prepareStatement(tripStatusQuery);
      ResultSet statusRS = (ResultSet) statusStmt.executeQuery();
      ResultSetMetaData statusMD = (ResultSetMetaData) statusRS.getMetaData();
      String allInfo = "";
      int columns = statusMD.getColumnCount();
      if (statusRS.next())
      {
         for (int i = 1; i <= columns; i++)
         {
            if (statusRS.getObject(i) != null)
            {
               allInfo = allInfo + statusRS.getObject(i);
            }
            else
            {
               allInfo = allInfo + " ";
            }
            allInfo = allInfo + ",";
         }
      }

      // PUT DATA INTO AN ARRAY AND USE FOR STATUS UPDATE
      String[] results = allInfo.split(",");
      double raisedAmount = Double.parseDouble(results[3]);
      double tripCost = Double.parseDouble(results[2]);
      int ra = (int) raisedAmount;
      int tc = (int) tripCost;
      if (ra >= tc)
      {
         status.setText("ELIGIBLE");
         status.setForeground(Color.GREEN);
         goalRaised.setText("$" + ra + "/$" + tc);
         goalRaised.setForeground(Color.GREEN);
         tripName.setText(results[0]);
         tripName.setForeground(Color.GREEN);
         goalProgress.setMaximum(tc);
         goalProgress.setValue(ra);
         goalProgress.setForeground(Color.GREEN);
      }
      else
      {
         status.setText("INELIGIBLE");
         status.setForeground(new Color(244, 38, 44));
         goalRaised.setText("$" + ra + "/$" + tc);
         goalRaised.setForeground(new Color(244, 38, 44));
         tripName.setText(results[0]);
         tripName.setForeground(new Color(244, 38, 44));
         goalProgress.setMaximum(tc);
         goalProgress.setValue(ra);
         goalProgress.setForeground(new Color(244, 38, 44));
      }

      //CLOSE CONNECTIONS
      statusRS.close();
      statusStmt.close();
      DBconnection.closeConnection();
   }

   // ********************************************METHOD TO UPDATE DATABASE
   private void updateDatabase(int personId) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException, IOException
   {
      String expeditionID = expeditionsList.getSelectedItem().toString();
      expeditionID = expeditionID.substring((expeditionID.lastIndexOf(',') + 2), (expeditionID.length() - 1));
      String updatePersonsQuery;
      String passwordCheckQuery = "SELECT password FROM persons WHERE personId = " + personId;
      String updateFundraisersQuery = "UPDATE fundraisers SET raiseGoal = \"" + raiseGoalTF.getText() + "\" "
         + ", expeditionId = " + expeditionID
         + " WHERE fundraisers.personId = " + personId;
      conn = DBconnection.getConnection();
      PreparedStatement checkPW = (PreparedStatement) conn.prepareStatement(passwordCheckQuery);
      ResultSet passRS = (ResultSet) checkPW.executeQuery();
      passRS.next();
      // CHECK IF PASSWORD ENTERED IS THE SAME AS THAT STORED IN THE DB (if it is don't rehash it and store again)!!
      if (passwordTF.getText().equals(passRS.getObject(1)))
      {
         updatePersonsQuery = "UPDATE persons SET firstName = \"" + firstNameTF.getText() + "\""
            + ", middleName = \"" + middleNameTF.getText() + "\""
            + ", lastName = \"" + lastNameTF.getText() + "\""
            + ", phoneNumber = \"" + phoneTF.getText() + "\""
            + ", emailAddress = \"" + emailTF.getText() + "\""
            + ", addressStreet = \"" + streetTF.getText() + "\""
            + ", addressCity = \"" + cityTF.getText() + "\""
            + ", addressState = \"" + stateTF.getText() + "\""
            + ", addressZip = \"" + zipTF.getText() + "\""
            + " WHERE persons.personId = " + personId;
      }
      else
      {
         updatePersonsQuery = "UPDATE persons SET firstName = \"" + firstNameTF.getText() + "\""
            + ", middleName = \"" + middleNameTF.getText() + "\""
            + ", lastName = \"" + lastNameTF.getText() + "\""
            + ", phoneNumber = \"" + phoneTF.getText() + "\""
            + ", emailAddress = \"" + emailTF.getText() + "\""
            + ", addressStreet = \"" + streetTF.getText() + "\""
            + ", addressCity = \"" + cityTF.getText() + "\""
            + ", addressState = \"" + stateTF.getText() + "\""
            + ", addressZip = \"" + zipTF.getText() + "\""
            + ", password = \"" + SimpleSHA1.SHA1(passwordTF.getText()) + "\""
            + " WHERE persons.personId = " + personId;
      }
      //RUN THE UPDATE QUERIES         
      PreparedStatement tableStmt = (PreparedStatement) conn.prepareStatement(updatePersonsQuery);
      tableStmt.executeUpdate();
      tableStmt = (PreparedStatement) conn.prepareStatement(updateFundraisersQuery);
      tableStmt.executeUpdate();

      //UPDATE PICTURE
      if (photoFile != null)
      {
         BufferedImage photoImage = ImageIO.read(photoFile);
         photoImage = Util.resizeImage(photoImage, 200, 200);
         DBconnection.updatePhoto(personId, photoImage);
         //SET photoFile to null SO IT DOESN'T UPDATE FOR FUTURE UPDATES
         photoFile = null;
      }

      tableStmt.close();
      DBconnection.closeConnection();

      // UPDATE CONTACT INFO TO DISPLAY CHANGES
      updateContactInfo(personId);
      updateTable(personId);
   }

   // **************************************UPDATES THE COMBO BOX TO REFLECT ALL FUNDRAISERS MATCHING SEARCH CRITERIA
   private void updateComboBox() throws SQLException, ClassNotFoundException
   {
      Object[] fundraiserNames = possibleNames.toArray();

      Pattern p = Pattern.compile(searchTF.getText(), Pattern.CASE_INSENSITIVE);
      fundraiserList.removeAllItems();
      for (int i = 0; i < fundraiserNames.length; i++)
      {
         String elem = fundraiserNames[i].toString();
         Matcher m = p.matcher(elem);
         if (m.find())
         {
            fundraiserList.addItem(fundraiserNames[i]);
         }
      }
   }

   //Prevents cells from being edited in table by overriding isCellEditable.
   public class myTable extends DefaultTableModel
   {
      @Override
      public boolean isCellEditable(int row, int col)
      {
         return false;
      }
   }
}
