package epoverty;

import com.mysql.jdbc.ResultSet;
import java.awt.Color;
import java.awt.Dimension;
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
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class EPovertyMain extends JFrame
{
   private JPanel mainPanel;
   private JTabbedPane tabs;
   public static JLabel messages;
   
   // OBJECT ARAYS FOR GRAPHING THE CHARTS and VARIABLE FOR HOW MANY MONTHS TO DO
   private final int NUMBER_OF_MONTHS_TO_GRAPH = 3;
   Object[] months;
   Object[] donationsData;
   Object[] withdrawalsData;
   
   //********************************************* ALL TABBED IMAGES
   private BufferedImage homeTAB = ImageIO.read(new File("homeTAB.jpg"));
   private BufferedImage homeTABselected = ImageIO.read(new File("homeTABselected.jpg"));
   private BufferedImage fundraisersTAB = ImageIO.read(new File("fundraisersTAB.jpg"));
   private BufferedImage fundraisersTABselected = ImageIO.read(new File("fundraisersTABselected.jpg"));
   private BufferedImage donorsTAB = ImageIO.read(new File("donorsTAB.jpg"));
   private BufferedImage donorsTABselected = ImageIO.read(new File("donorsTABselected.jpg"));
   private BufferedImage donationsTAB = ImageIO.read(new File("donationsTAB.jpg"));
   private BufferedImage donationsTABselected = ImageIO.read(new File("donationsTABselected.jpg"));
   private BufferedImage accountsTAB = ImageIO.read(new File("accountsTAB.jpg"));
   private BufferedImage accountsTABselected = ImageIO.read(new File("accountsTABselected.jpg"));
   private BufferedImage transactionsTAB = ImageIO.read(new File("transactionsTAB.jpg"));
   private BufferedImage transactionsTABselected = ImageIO.read(new File("transactionsTABselected.jpg"));
   private BufferedImage tripsTAB = ImageIO.read(new File("tripsTAB.jpg"));
   private BufferedImage tripsTABselected = ImageIO.read(new File("tripsTABselected.jpg"));
   private BufferedImage charity$TAB = ImageIO.read(new File("charity$TAB.jpg"));
   private BufferedImage charity$TABselected = ImageIO.read(new File("charity$TABselected.jpg"));
   private BufferedImage overhead$TAB = ImageIO.read(new File("overhead$TAB.jpg"));
   private BufferedImage overhead$TABselected = ImageIO.read(new File("overhead$TABselected.jpg"));
   private BufferedImage trips$TAB = ImageIO.read(new File("trips$TAB.jpg"));
   private BufferedImage trips$TABselected = ImageIO.read(new File("trips$TABselected.jpg"));
   //*********************************************Colors used in the program that are not found in the Colors class
   public static final Color darkest = new Color(35, 35, 35);
   public static final Color font = new Color(195, 195, 195);

   //*********************************************CONSTRUCTOR METHOD
   public EPovertyMain() throws IOException, ClassNotFoundException, SQLException
   {      
      //CREATE MAIN PANEL
      setTitle("|||E-Poverty Main || V-0.1 (SKELETON)|||");
      setSize(1300, 720);
      setLocation(0, 0);
      setVisible(true);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      mainPanel = new JPanel();
      setContentPane(mainPanel);
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
      mainPanel.setBackground(darkest);

      //******************************************CREATE THE TOP PANEL
      BackgroundPanel topPanel = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      topPanel.setLayout(new GridLayout(1, 2, 0, 0));
      Dimension top = new Dimension(super.getWidth(), super.getHeight() - 15);
      topPanel.setPreferredSize(top);

      //CREATE THE BOTTOM 'STATUS' PANEL AND ADD A LABEL TO IT FOR MESSAGES
      BackgroundPanel bottomPanel = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      Dimension bottom = new Dimension(5000, 15);
      bottomPanel.setPreferredSize(bottom);
      bottomPanel.setMaximumSize(bottom);
      GradientPaint paintBottom = new GradientPaint(0, 0, Color.BLACK, 200, 0, Color.WHITE);
      bottomPanel.setPaint(paintBottom);
      bottomPanel.setLayout(new GridBagLayout());
      GridBagConstraints gBC = new GridBagConstraints();
      gBC.fill = GridBagConstraints.NONE;
      messages = new JLabel("");
      gBC.insets = new Insets(0, 0, 0, 5);
      gBC.anchor = GridBagConstraints.EAST;
      gBC.weightx = 0.5;
      gBC.gridx = 10;
      gBC.gridy = 0;
      gBC.gridwidth = 1;
      messages.setForeground(Color.BLACK);
      bottomPanel.add(messages, gBC);

      /* 
       * HOME TAB 
       */
      BackgroundPanel homeTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 50.0f, 10.5f);
      homeTab.setLayout(new GridLayout(2, 2, 0, 0));
      Dimension access = new Dimension(super.getWidth(), super.getHeight() - 15);
      homeTab.setPreferredSize(access);
      GradientPaint paintAccess = new GradientPaint(0, 0, Color.BLACK, 100, 100, Color.BLACK);
      homeTab.setPaint(paintAccess);

      //******************************************SET UP THE GRAPHS AND ADD THEM TO HOMETAB
      months = DBconnection.getMonths(NUMBER_OF_MONTHS_TO_GRAPH);
      donationsData = DBconnection.getMonthlyDonations(months);
      withdrawalsData = DBconnection.getMonthlyWithdrawals(months);
      BarGraph donationsGraph = new BarGraph("Monthly Donations", months, donationsData);
      BarGraph withdrawalsGraph = new BarGraph("Monthly Withdrawals", months, withdrawalsData);
      
      homeTab.add(donationsGraph);
      homeTab.add(new JLabel(""));
      homeTab.add(new JLabel(""));
      homeTab.add(withdrawalsGraph);

      //******************************************CREATE TABS and EDIT THEIR APPEARANCE
      tabs = new JTabbedPane();
      UIManager.put("TabbedPane.selected", Color.DARK_GRAY);
      tabs.setTabPlacement(JTabbedPane.LEFT);
      tabs.setBackground(Color.DARK_GRAY);
      tabs.updateUI();
      mainPanel.add(tabs);

      //******************************************CALL EACH TABS CLASS (SENDING IT THE JTABBEDPANE OBJECT)
      tabs.add("", homeTab);
      Fundraisers fundraisers = new Fundraisers(tabs);
      Donors donors = new Donors(tabs);
      Donations donations = new Donations(tabs);
      Withdrawals transactions = new Withdrawals(tabs);
      AccountsSummary accountsSum = new AccountsSummary(tabs);
      Trips trips = new Trips(tabs);
      CharityFunds charity = new CharityFunds(tabs);
      OverheadFunds overhead = new OverheadFunds(tabs);
      IndividualTripFunds individual = new IndividualTripFunds(tabs);

      //******************************************ADD ICONS TO TABS (Changing Tab location above will NOT change their image)
      tabs.setIconAt(0, new ImageIcon(homeTABselected));
      tabs.setIconAt(1, new ImageIcon(fundraisersTAB));
      tabs.setIconAt(2, new ImageIcon(donorsTAB));
      tabs.setIconAt(3, new ImageIcon(donationsTAB));
      tabs.setIconAt(4, new ImageIcon(transactionsTAB));
      tabs.setIconAt(5, new ImageIcon(accountsTAB));
      tabs.setIconAt(6, new ImageIcon(tripsTAB));
      tabs.setIconAt(7, new ImageIcon(charity$TAB));
      tabs.setIconAt(8, new ImageIcon(overhead$TAB));
      tabs.setIconAt(9, new ImageIcon(trips$TAB));

      //******************************************LISTENER FOR THE TABS (TO CHANGE ICONS WHEN SELECTED)
      tabs.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent e)
         {
            // RESET ALL TABS TO THEIR 'UNSELECTED' FORM
            tabs.setIconAt(0, new ImageIcon(homeTAB));
            tabs.setIconAt(1, new ImageIcon(fundraisersTAB));
            tabs.setIconAt(2, new ImageIcon(donorsTAB));
            tabs.setIconAt(3, new ImageIcon(donationsTAB));
            tabs.setIconAt(4, new ImageIcon(transactionsTAB));
            tabs.setIconAt(5, new ImageIcon(accountsTAB));
            tabs.setIconAt(6, new ImageIcon(tripsTAB));
            tabs.setIconAt(7, new ImageIcon(charity$TAB));
            tabs.setIconAt(8, new ImageIcon(overhead$TAB));
            tabs.setIconAt(9, new ImageIcon(trips$TAB));

            // GET SELECTED TAB
            int tabSelected = tabs.getSelectedIndex();
            switch (tabSelected)
            {
               case 0:
                  tabs.setIconAt(0, new ImageIcon(homeTABselected));
                  break;
               case 1:
                  tabs.setIconAt(1, new ImageIcon(fundraisersTABselected));
                  break;
               case 2:
                  tabs.setIconAt(2, new ImageIcon(donorsTABselected));
                  break;
               case 3:
                  tabs.setIconAt(3, new ImageIcon(donationsTABselected));
                  break;
               case 4:
                  tabs.setIconAt(4, new ImageIcon(transactionsTABselected));
                  break;
               case 5:
                  tabs.setIconAt(5, new ImageIcon(accountsTABselected));
                  break;
               case 6:
                  tabs.setIconAt(6, new ImageIcon(tripsTABselected));
                  break;
               case 7:
                  tabs.setIconAt(7, new ImageIcon(charity$TABselected));
                  break;
               case 8:
                  tabs.setIconAt(8, new ImageIcon(overhead$TABselected));
                  break;
               case 9:
                  tabs.setIconAt(9, new ImageIcon(trips$TABselected));
                  break;
               default:
                  break;
            }

         }
      });

      mainPanel.add(bottomPanel);

      //******************************************SET UP MENU BAR 
      JMenuBar menuBar = new JMenuBar();
      menuBar.setBackground(darkest);
      setJMenuBar(menuBar);

      JMenu option1 = new JMenu("Accounts");
      option1.setForeground(Color.WHITE);
      option1.setMnemonic('1');
      menuBar.add(option1);
      JMenuItem subOption1_1 = new JMenuItem("Add Donation");
      JMenuItem subOption2_1 = new JMenuItem("Add Withdrawal");
      JMenuItem subOption3_1 = new JMenuItem("Add Account??");
      option1.add(subOption1_1);
      option1.add(subOption2_1);
      option1.add(subOption3_1);

      JMenu option2 = new JMenu("Trips");
      option2.setForeground(Color.WHITE);
      option2.setMnemonic('2');
      menuBar.add(option2);
      JMenuItem addTripJMI = new JMenuItem("Add Trip");
      JMenuItem editTripJMI = new JMenuItem("Edit Trip");
      JMenuItem subOption3_2 = new JMenuItem("Archive Trip");
      option2.add(addTripJMI);
      option2.add(editTripJMI);
      option2.add(subOption3_2);

      JMenu option3 = new JMenu("Persons");
      option3.setForeground(Color.WHITE);
      option3.setMnemonic('3');
      menuBar.add(option3);
      JMenuItem addPersonJMI = new JMenuItem("Add Person");
      JMenuItem changeRolesJMI = new JMenuItem("Change Persons Roles");
      JMenuItem subOption3_3 = new JMenuItem("Update Person??");
      option3.add(addPersonJMI);
      option3.add(changeRolesJMI);
      option3.add(subOption3_3);

      /*
       * 
       * AFTER THIS POINT THERE ARE THE LISTENERS FOR THE JMENUITEMS
       * AT THE VERY END OF THE CLASS THERE IS A METHOD TO UPDATE
       * THE STATUS BAR AT THE BOTTOM.  NOTHING IS UPDATING THIS AS 
       * OF NOW THOUGH. 
       * 
       */
      // *****************************************LISTENER FOR THE EDIT TRIPS FUNCTION
      editTripJMI.addActionListener(
         new ActionListener()
         {
            JTextField searchTF, nameTF, costTF, defaultRaiseGoalTF, departDateTF, returnDateTF, cutoffDateTF;
            JTextArea descriptionTF;
            JButton departBTN, returnBTN, cutoffBTN;
            JComboBox tripList;
            ArrayList trips;

            @Override
            public void actionPerformed(ActionEvent e)
            {
               UIManager.put("OptionPane.okButtonText", "UPDATE TRIP");
               UIManager.put("TextArea.background", Color.GRAY);
               UIManager.put("TextArea.foreground", Color.WHITE);
               nameTF = new JTextField(12);
               searchTF = new JTextField(12);
               descriptionTF = new JTextArea(4, 30);
               descriptionTF.setBorder(BorderFactory.createLineBorder(Color.WHITE));
               costTF = new JTextField(12);
               defaultRaiseGoalTF = new JTextField(12);
               departDateTF = new JTextField(12);
               returnDateTF = new JTextField(12);
               cutoffDateTF = new JTextField(12);
               departBTN = new JButton("PICK DATE");
               returnBTN = new JButton("PICK DATE");
               cutoffBTN = new JButton("PICK DATE");
               UIManager.put("TextArea.font", searchTF.getFont());
               try
               {
                  trips = DBconnection.getTrips();
               }
               catch (SQLException ex)
               {
                  Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
               }
               catch (ClassNotFoundException ex)
               {
                  Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
               }
               tripList = new JComboBox(trips.toArray());

               final JPanel panel = new JPanel(new GridBagLayout());
               GridBagConstraints gBC = new GridBagConstraints();
               gBC.anchor = GridBagConstraints.WEST;

               // COLUMN 1
               gBC.weightx = 1;
               gBC.weighty = 1;
               gBC.gridx = 0;
               gBC.gridy = 0;
               panel.add(new JLabel("Name:"), gBC);

               gBC.gridy = 1;
               panel.add(new JLabel("Cost:"), gBC);

               gBC.gridy = 2;
               panel.add(new JLabel("Raise Goal:"), gBC);

               gBC.gridy = 3;
               panel.add(new JLabel("Depart Date:"), gBC);

               gBC.gridy = 4;
               panel.add(new JLabel("Return Date:"), gBC);

               gBC.gridy = 5;
               panel.add(new JLabel("Cutoff Date:"), gBC);

               gBC.gridy = 6;
               panel.add(new JLabel("Description:"), gBC);

               gBC.gridy = 7;
               panel.add(new JLabel("SEARCH:"), gBC);

               // COLUMN 2
               gBC.gridx = 1;
               gBC.gridy = 0;
               panel.add(nameTF, gBC);

               gBC.gridy = 1;
               panel.add(costTF, gBC);

               gBC.gridy = 2;
               panel.add(defaultRaiseGoalTF, gBC);

               gBC.gridy = 3;
               panel.add(departDateTF, gBC);

               gBC.gridy = 4;
               panel.add(returnDateTF, gBC);

               gBC.gridy = 5;
               panel.add(cutoffDateTF, gBC);

               gBC.gridy = 6;
               gBC.gridwidth = 2;
               panel.add(descriptionTF, gBC);

               gBC.gridy = 7;
               gBC.gridwidth = 1;
               panel.add(searchTF, gBC);

               // COLUMN 3
               gBC.gridx = 2;
               gBC.gridy = 3;
               gBC.anchor = GridBagConstraints.WEST;
               panel.add(departBTN, gBC);

               gBC.gridy = 4;
               panel.add(returnBTN, gBC);

               gBC.gridy = 5;
               panel.add(cutoffBTN, gBC);

               gBC.gridy = 7;
               panel.add(tripList, gBC);

               final JFrame f = new JFrame();
               f.getContentPane().add(panel);
               f.pack();

               // LISTENERS FOR DATE PICKER BUTTONS
               departBTN.addActionListener(new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent ae)
                  {
                     departDateTF.setText(new DatePicker(f).setPickedDate());
                  }
               });
               returnBTN.addActionListener(new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent ae)
                  {
                     returnDateTF.setText(new DatePicker(f).setPickedDate());
                  }
               });
               cutoffBTN.addActionListener(new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent ae)
                  {
                     cutoffDateTF.setText(new DatePicker(f).setPickedDate());
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

                     // **************************************UPDATES THE COMBO BOX TO REFLECT ALL FUNDRAISERS MATCHING SEARCH CRITERIA
                     private void updateComboBox() throws SQLException, ClassNotFoundException
                     {
                        Object[] tripsNames = trips.toArray();

                        Pattern p = Pattern.compile(searchTF.getText(), Pattern.CASE_INSENSITIVE);
                        tripList.removeAllItems();
                        for (int i = 0; i < tripsNames.length; i++)
                        {
                           String elem = tripsNames[i].toString();
                           Matcher m = p.matcher(elem);
                           if (m.find())
                           {
                              tripList.addItem(tripsNames[i]);
                           }
                        }
                     }
                  });

               // ITEM LISTENER FOR COMBO BOX
               tripList.addItemListener(
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
                           String personSelected = tripList.getSelectedItem().toString();
                           String tripID = personSelected.substring((personSelected.lastIndexOf(',') + 2), (personSelected.length() - 1));
                           try
                           {
                              // UPDATE TRIP INFO
                              ArrayList allTripInfo = DBconnection.getTrips(Integer.parseInt(tripID));
                              String allTrip = allTripInfo.toString();
                              String[] results = allTrip.split(",");
                              nameTF.setText(results[1].substring(1));
                              descriptionTF.setText(results[2].substring(1));
                              costTF.setText(results[3].substring(1));
                              defaultRaiseGoalTF.setText(results[4].substring(1));
                              departDateTF.setText(results[5].substring(1));
                              returnDateTF.setText(results[6].substring(1));
                              cutoffDateTF.setText(results[7].substring(1, results[7].length() - 2));
                           }
                           catch (SQLException ex)
                           {
                              Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                           }
                           catch (ClassNotFoundException ex)
                           {
                              Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                           }
                        }
                     }
                  });

               // DISPLAY selected persons information               
               int result = JOptionPane.showConfirmDialog(null, panel, "UPDATE TRIP",
                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

               // *****************************************IF 'OK' IS HIT VERYIFY.  OTHERWISE CANCEL
               if (result == JOptionPane.OK_OPTION)
               {
                  // CALL TO UPDATE DATABASE
               }
            }
         });

      // *****************************************LISTENER FOR THE ADD TRIPS FUNCTION
      addTripJMI.addActionListener(
         new ActionListener()
         {
            JTextField nameTF;
            JTextArea descriptionTF;
            JTextField costTF;
            JTextField defaultRaiseGoalTF;
            JTextField departDateTF;
            JTextField returnDateTF;
            JTextField cutoffDateTF;
            JButton departBTN;
            JButton returnBTN;
            JButton cutoffBTN;

            @Override
            public void actionPerformed(ActionEvent e)
            {
               UIManager.put("OptionPane.okButtonText", "ADD TRIP");
               UIManager.put("TextArea.background", Color.GRAY);
               UIManager.put("TextArea.foreground", Color.WHITE);
               nameTF = new JTextField(12);
               descriptionTF = new JTextArea(4, 30);
               descriptionTF.setBorder(BorderFactory.createLineBorder(Color.WHITE));
               costTF = new JTextField(12);
               defaultRaiseGoalTF = new JTextField(12);
               departDateTF = new JTextField(12);
               returnDateTF = new JTextField(12);
               cutoffDateTF = new JTextField(12);
               departBTN = new JButton("PICK DATE");
               returnBTN = new JButton("PICK DATE");
               cutoffBTN = new JButton("PICK DATE");
               UIManager.put("TextArea.font", nameTF.getFont());

               final JPanel panel = new JPanel(new GridBagLayout());
               GridBagConstraints gBC = new GridBagConstraints();
               gBC.anchor = GridBagConstraints.WEST;

               // COLUMN 1
               gBC.weightx = 1;
               gBC.weighty = 1;
               gBC.gridx = 0;
               gBC.gridy = 0;
               panel.add(new JLabel("Name:"), gBC);

               gBC.gridy = 1;
               panel.add(new JLabel("Cost:"), gBC);

               gBC.gridy = 2;
               panel.add(new JLabel("Raise Goal:"), gBC);

               gBC.gridy = 3;
               panel.add(new JLabel("Depart Date:"), gBC);

               gBC.gridy = 4;
               panel.add(new JLabel("Return Date:"), gBC);

               gBC.gridy = 5;
               panel.add(new JLabel("Cutoff Date:"), gBC);

               gBC.gridy = 6;
               panel.add(new JLabel("Description:"), gBC);

               // COLUMN 2
               gBC.gridx = 1;
               gBC.gridy = 0;
               panel.add(nameTF, gBC);

               gBC.gridy = 1;
               panel.add(costTF, gBC);

               gBC.gridy = 2;
               panel.add(defaultRaiseGoalTF, gBC);

               gBC.gridy = 3;
               panel.add(departDateTF, gBC);

               gBC.gridy = 4;
               panel.add(returnDateTF, gBC);

               gBC.gridy = 5;
               panel.add(cutoffDateTF, gBC);

               gBC.gridy = 6;
               gBC.gridwidth = 2;
               panel.add(descriptionTF, gBC);

               // COLUMN 3
               gBC.gridx = 2;
               gBC.gridy = 3;
               gBC.gridwidth = 1;
               gBC.anchor = GridBagConstraints.WEST;
               panel.add(departBTN, gBC);

               gBC.gridy = 4;
               panel.add(returnBTN, gBC);

               gBC.gridy = 5;
               panel.add(cutoffBTN, gBC);

               final JFrame f = new JFrame();
               f.getContentPane().add(panel);
               f.pack();

               // LISTENERS FOR DATE PICKER BUTTONS
               departBTN.addActionListener(new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent ae)
                  {
                     departDateTF.setText(new DatePicker(f).setPickedDate());
                  }
               });
               returnBTN.addActionListener(new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent ae)
                  {
                     returnDateTF.setText(new DatePicker(f).setPickedDate());
                  }
               });
               cutoffBTN.addActionListener(new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent ae)
                  {
                     cutoffDateTF.setText(new DatePicker(f).setPickedDate());
                  }
               });

               // DISPLAY selected persons information               
               int result = JOptionPane.showConfirmDialog(null, panel, "ADD NEW TRIP",
                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

               // *****************************************IF 'OK' IS HIT VERYIFY.  OTHERWISE CANCEL
               if (result == JOptionPane.OK_OPTION)
               {
                  try
                  {
                     // CALL TO ADD TRIP TO DB
                     DBconnection.addExpedition(nameTF.getText(), descriptionTF.getText(), costTF.getText(),
                        defaultRaiseGoalTF.getText(), departDateTF.getText(), returnDateTF.getText(),
                        cutoffDateTF.getText());
                  }
                  catch (ParseException ex)
                  {
                     Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  catch (SQLException ex)
                  {
                     Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  catch (ClassNotFoundException ex)
                  {
                     Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                  }
               }
            }
         });
      //******************************************END OF ADD TRIP JMENU LISTENER

      // *****************************************LISTENER FOR THE ADD PERSON FUNCTION
      addPersonJMI.addActionListener(
         new ActionListener()
         {
            JTextField firstNameTF;
            JTextField middleNameTF;
            JTextField lastNameTF;
            JTextField phoneTF;
            JTextField emailTF;
            JTextField streetTF;
            JTextField raiseGoalTF;
            JTextField cityTF;
            JTextField stateTF;
            JTextField zipTF;
            JFileChooser photoFC;
            JCheckBox donor;
            JCheckBox fundraiser;
            JCheckBox admin;
            FileFilter photoFilter = new FileNameExtensionFilter("Image Files", "jpg", "png");
            JTextField photoTF;
            JButton browseBTN;
            JTextField passwordTF;
            JComboBox expeditionsList;
            File photoFile;
            ResultSet rs;
            BufferedImage photoImage;
            ArrayList tripsList;

            @Override
            public void actionPerformed(ActionEvent e)
            {
               UIManager.put("OptionPane.okButtonText", "ADD PERSON");
               firstNameTF = new JTextField(12);
               middleNameTF = new JTextField(12);
               lastNameTF = new JTextField(12);
               phoneTF = new JTextField(12);
               emailTF = new JTextField(12);
               streetTF = new JTextField(12);
               raiseGoalTF = new JTextField(12);
               cityTF = new JTextField(12);
               stateTF = new JTextField(2);
               zipTF = new JTextField(5);
               photoFC = new JFileChooser();
               photoFC.setFileFilter(photoFilter);
               photoTF = new JTextField(12);
               browseBTN = new JButton("BROWSE");
               passwordTF = new JPasswordField(12);
               donor = new JCheckBox("Donor?");
               fundraiser = new JCheckBox("Fundraiser?");
               admin = new JCheckBox("Admin?");
               try
               {
                  tripsList = DBconnection.getTrips();
               }
               catch (SQLException ex)
               {
                  Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
               }
               catch (ClassNotFoundException ex)
               {
                  Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
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

               gBC.gridy = 6;
               panel.add(fundraiser, gBC);

               gBC.gridy = 7;
               panel.add(donor, gBC);

               gBC.gridy = 8;
               panel.add(admin, gBC);

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

               // DISPLAY selected persons information
               int result = JOptionPane.showConfirmDialog(null, panel, "ADD NEW PERSONS",
                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

               // *****************************************IF 'OK' IS HIT VERYIFY.  OTHERWISE CANCEL
               if (result == JOptionPane.OK_OPTION)
               {
                  // GET THE PICTURE AND MAKE IT A BUFFEREDIMAGE
                  //UPDATE PICTURE
                  if (photoFile != null)
                  {
                     try
                     {
                        photoImage = ImageIO.read(photoFile);
                        photoImage = Util.resizeImage(photoImage, 200, 200);
                     }
                     catch (IOException ex)
                     {
                        Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                     }
                  }
                  else
                  {
                     try
                     {
                        photoImage = Util.resizeImage(ImageIO.read(new File("default.jpg")), 200, 200);
                     }
                     catch (IOException ex)
                     {
                        Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                     }
                  }

                  // CALL TO UPDATE THE DATABASE
                  try
                  {
                     DBconnection.addPerson(firstNameTF.getText(), middleNameTF.getText(),
                        lastNameTF.getText(), phoneTF.getText(), emailTF.getText(),
                        streetTF.getText(), cityTF.getText(), stateTF.getText(), zipTF.getText(),
                        photoImage, SimpleSHA1.SHA1(passwordTF.getText()));

                     // CHECK IF PERSON NEEDS TO BE ADDED TO DONORS TABLE
                     if (donor.isSelected())
                     {
                        DBconnection.insertPersonIntoDonors(emailTF.getText());
                     }

                     // CHECK IF PERSON NEEDS TO BE ADDED TO FUNDRAISERS TABLE
                     if (fundraiser.isSelected())
                     {
                        String expeditionID = expeditionsList.getSelectedItem().toString();
                        expeditionID = expeditionID.substring((expeditionID.lastIndexOf(',') + 2), (expeditionID.length() - 1));
                        DBconnection.insertPersonIntoFundraisers(emailTF.getText(), raiseGoalTF.getText(), Integer.parseInt(expeditionID));
                     }

                     // CHECK IF PERSON NEEDS TO BE ADDED TO ADMIN TABLE
                     if (admin.isSelected())
                     {
                        DBconnection.insertPersonIntoAdmins(emailTF.getText());
                     }

                     DBconnection.closeConnection();
                  }
                  catch (IOException ex)
                  {
                     Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  catch (NoSuchAlgorithmException ex)
                  {
                     Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  catch (SQLException ex)
                  {
                     Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  catch (ClassNotFoundException ex)
                  {
                     Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                  }

                  // RESET THE PHOTO TO NULL FOR FUTURE UPDATES
                  photoFile = null;
               }
            }
         });
      //******************************************END OF ADD PERSON JMENU LISTENER

      // *****************************************LISTENER FOR THE CHANGE ROLES FUNCTION
      changeRolesJMI.addActionListener(
         new ActionListener()
         {
            JTextField searchTF;
            JComboBox personsList;
            JCheckBox fundraiserCB;
            JCheckBox donorCB;
            JCheckBox adminCB;
            ArrayList peopleList;
            boolean adminStatus;
            boolean fundraiserStatus;
            boolean donorStatus;
            String personID;

            @Override
            public void actionPerformed(ActionEvent e)
            {

               UIManager.put("OptionPane.okButtonText", "UPDATE");
               //CALL TO GET ALL PEOPLE, RETURN ARRAY LIST AND SET THAT AS COMBOBOX ARRAY
               try
               {
                  peopleList = DBconnection.getPeople();
                  personsList = new JComboBox(peopleList.toArray());
               }
               catch (SQLException ex)
               {
                  Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
               }
               catch (ClassNotFoundException ex)
               {
                  Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
               }
               searchTF = new JTextField(12);
               fundraiserCB = new JCheckBox("Fundraiser?");
               donorCB = new JCheckBox("Donor?");
               adminCB = new JCheckBox("Admin?");
               adminStatus = false;
               fundraiserStatus = false;
               donorStatus = false;

               final JPanel panel = new JPanel(new GridBagLayout());
               GridBagConstraints gBC = new GridBagConstraints();
               gBC.anchor = GridBagConstraints.WEST;

               // COLUMN 1
               gBC.weightx = 1;
               gBC.weighty = 1;
               gBC.gridx = 0;
               gBC.gridy = 0;
               panel.add(new JLabel("SEARCH:"), gBC);

               gBC.gridy = 1;
               panel.add(searchTF, gBC);

               gBC.gridy = 2;
               panel.add(personsList, gBC);

               // COLUMN 2
               gBC.gridx = 2;
               gBC.gridy = 0;
               panel.add(fundraiserCB, gBC);

               gBC.gridy = 1;
               panel.add(donorCB, gBC);

               gBC.gridy = 2;
               panel.add(adminCB, gBC);

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

                     // **************************************UPDATES THE COMBO BOX TO REFLECT ALL FUNDRAISERS MATCHING SEARCH CRITERIA
                     private void updateComboBox() throws SQLException, ClassNotFoundException
                     {
                        Object[] personsNames = peopleList.toArray();

                        Pattern p = Pattern.compile(searchTF.getText(), Pattern.CASE_INSENSITIVE);
                        personsList.removeAllItems();
                        for (int i = 0; i < personsNames.length; i++)
                        {
                           String elem = personsNames[i].toString();
                           Matcher m = p.matcher(elem);
                           if (m.find())
                           {
                              personsList.addItem(personsNames[i]);
                           }
                        }
                     }
                  });

               // ITEM LISTENER FOR COMBO BOX
               personsList.addItemListener(
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
                           String personSelected = personsList.getSelectedItem() + "";
                           personID = personSelected.substring((personSelected.lastIndexOf(',') + 2), (personSelected.length() - 1));
                           try
                           {
                              // UPDATE CHECK BOXES
                              if (DBconnection.isAdmin(Integer.parseInt(personID)))
                              {
                                 adminCB.setSelected(true);
                                 adminStatus = true;
                              }
                              else
                              {
                                 adminCB.setSelected(false);
                                 adminStatus = false;
                              }
                              if (DBconnection.isFundraiser(Integer.parseInt(personID)))
                              {
                                 fundraiserCB.setSelected(true);
                                 fundraiserStatus = true;
                              }
                              else
                              {
                                 fundraiserCB.setSelected(false);
                                 fundraiserStatus = false;
                              }
                              if (DBconnection.isDonor(Integer.parseInt(personID)))
                              {
                                 donorCB.setSelected(true);
                                 donorStatus = true;
                              }
                              else
                              {
                                 donorCB.setSelected(false);
                                 donorStatus = false;
                              }
                           }
                           catch (SQLException ex)
                           {
                              Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                           }
                           catch (ClassNotFoundException ex)
                           {
                              Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                           }

                        }
                     }
                  });
               // END OF ITEM LISTENER FOR COMBO BOX 

               // DISPLAY selected persons information               
               int result = JOptionPane.showConfirmDialog(null, panel, "UPDATE ROLES",
                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

               // *****************************************IF 'OK' IS HIT VERYIFY.  OTHERWISE CANCEL
               if (result == JOptionPane.OK_OPTION)
               {
                  // CALLS TO UPDATE DB TABLES
                  try
                  {
                     // IF ADMIN WAS NOT INITIALLY SELECTED BUT IS NOW, ADD TO ADMINS TABLE
                     if (adminStatus == false && adminCB.isSelected())
                     {
                        DBconnection.insertPersonIntoAdmins(Integer.parseInt(personID));
                        // NOW PERSON IS IN ADMINS SO..
                        adminStatus = true;
                     }
                     // IF FUNDRAISER WAS NOT INITIALLY SELECTED BUT IS NOW, ADD TO FUNRAISERS TABLE
                     if (fundraiserStatus == false && fundraiserCB.isSelected())
                     {
                        //NEED TO GET MORE INFORMATION (expeditionId and raiseGoal)
                        final JPanel panelMore = new JPanel(new GridBagLayout());
                        JTextField raiseGoalMore = new JTextField(12);
                        ArrayList tripsList = DBconnection.getTrips();
                        JComboBox expeditionsList = new JComboBox(tripsList.toArray());
                        gBC.anchor = GridBagConstraints.WEST;

                        // COLUMN 1
                        gBC.weightx = 1;
                        gBC.weighty = 1;
                        gBC.gridx = 0;
                        gBC.gridy = 0;
                        panelMore.add(new JLabel("Expedition:"), gBC);

                        gBC.gridy = 1;
                        panelMore.add(new JLabel("Raise Goal:"), gBC);

                        // COLUMN 2
                        gBC.gridx = 1;
                        gBC.gridy = 0;
                        panelMore.add(expeditionsList, gBC);

                        gBC.gridy = 1;
                        panelMore.add(raiseGoalMore, gBC);

                        int moreInfo = JOptionPane.showConfirmDialog(null, panelMore, "UPDATE ROLES",
                           JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        // *****************************************IF 'OK' IS HIT VERYIFY.  OTHERWISE CANCEL
                        if (moreInfo == JOptionPane.OK_OPTION)
                        {
                           String expeditionID = expeditionsList.getSelectedItem().toString();
                           expeditionID = expeditionID.substring((expeditionID.lastIndexOf(',') + 2), (expeditionID.length() - 1));
                           DBconnection.insertPersonIntoFundraisers(Integer.parseInt(personID), raiseGoalMore.getText(), Integer.parseInt(expeditionID));
                           // NOW PERSON IS FUNDRAISER SO..
                           fundraiserStatus = true;
                        }
                     }
                     // IF DONOR WAS NOT INITIALLY SELECTED BUT IS NOW, ADD TO DONORS TABLE
                     if (donorStatus == false && donorCB.isSelected())
                     {
                        DBconnection.insertPersonIntoDonors(Integer.parseInt(personID));
                        // NOW PERSON IS DONOR SO..
                        donorStatus = true;
                     }
                  }
                  catch (SQLException ex)
                  {
                     Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  catch (ClassNotFoundException ex)
                  {
                     Logger.getLogger(EPovertyMain.class.getName()).log(Level.SEVERE, null, ex);
                  }
               }
            }
         });
      //******************************************END OF CHANGE ROLE JMENU LISTENER

      //******************************************This just makes sure that everything is drawn correctly on the screen
      this.validate();
   }
   //*********************************************END OF CONSTRUCTOR

   /*
    * This method is so that all of our tab classes can change the status message on the bottom of the screen
    * We may want to add a timer to this so that it will erase it after some point? Just an idea.
    */
   public static void updateStatus(String message)
   {
      messages.setText(message);
   }
}
