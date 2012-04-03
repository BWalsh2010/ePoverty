package epoverty;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Trips extends JPanel
{
   private JLabel tripName;
   private JPanel mainPanel;
   private JLabel tripDate;
   //private JTable tripsTable;
   private myTable tripsTable;
   private final int width = super.getWidth();
   private final int height = super.getHeight();
   private final int font = 20;
   private JButton addBTN;
   private JTextField tripNameTF, tripDescTF, tripDateTF, tripCostTF;
   private JComboBox expeditionsList;
   private JCheckBox editableCB;
   GridBagConstraints gBC = new GridBagConstraints();

   Trips(JTabbedPane tabs) throws SQLException, ClassNotFoundException
   {
      super();
      gBC.fill = GridBagConstraints.NONE;
      //******************************************Set up UI
       UIManager.put("Table.background", Color.DARK_GRAY);
      UIManager.put("Table.foreground", Color.WHITE);
      UIManager.put("Table.font", new Font("SansSerif", Font.BOLD, 14));
      UIManager.put("Table.selectionBackground", Color.WHITE);
      UIManager.put("Table.selectionForeground", Color.BLACK);
      UIManager.put("Table.gridColor", new Color(35, 35, 35));
      UIManager.put("TableHeader.background", Color.LIGHT_GRAY);
      UIManager.put("TableHeader.foreground", new Color(35, 35, 35));


      //GridBagConstraints gBC = new GridBagConstraints();
      BackgroundPanel tripsTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      Dimension access = new Dimension(super.getWidth(), super.getHeight() - 15);
      tripsTab.setPreferredSize(access);
      GradientPaint paintAccess = new GradientPaint(0, 0, Color.CYAN, 100, 100, Color.BLACK);
      tripsTab.setPaint(paintAccess);

      //tabs.add("", tripsTab);     


      mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
      Dimension mainTrips = new Dimension(width, height - 15);
      mainPanel.setPreferredSize(mainTrips);
      mainPanel.setBackground(Color.DARK_GRAY);


      // CREATE TOP OF THE TAB
      BackgroundPanel topTripsTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      topTripsTab.setLayout(new GridLayout(0, 1, 1, 1));
      Dimension topFundraiser = new Dimension(width, 725);
      topTripsTab.setPreferredSize(topFundraiser);
      GradientPaint paintTopFundraiser = new GradientPaint(0, 0, Color.BLACK, 100, 100, Color.DARK_GRAY);
      topTripsTab.setPaint(paintTopFundraiser);

      BackgroundPanel middleTripsTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      middleTripsTab.setLayout(new GridBagLayout());
      middleTripsTab.setBorder(BorderFactory.createLineBorder(Color.WHITE));
      Dimension middleFundraiser = new Dimension(width, 50);
      middleTripsTab.setPreferredSize(middleFundraiser);
      GradientPaint paintmiddleFundraiser = new GradientPaint(0, 0, Color.BLACK, 100, 100, Color.DARK_GRAY);
      middleTripsTab.setPaint(paintmiddleFundraiser);

      // CREATE BOTTOM OF THE TAB
      BackgroundPanel bottomTripsTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      bottomTripsTab.setLayout(new GridBagLayout());
      Dimension bottomFundraiser = new Dimension(width, 175);
      bottomTripsTab.setPreferredSize(bottomFundraiser);
      GradientPaint paintBottomFundraiser = new GradientPaint(0, 150, Color.LIGHT_GRAY, 100, 50, Color.DARK_GRAY);
      bottomTripsTab.setPaint(paintBottomFundraiser);

      // CREATE AND ADD TABLE TO THE TOP OF TAB

      String getTripsQuery = "SELECT e.departDate, e.returnDate, e.cost, e.name, e.expeditionDescription "
         + "FROM expeditions e "; //+ 
      //  "inner join donors o on d.donationId = o.donorId " +
      //  "inner join persons p on o.personId = p.personId ";

      //Creates connection and runs query  - DB
      Connection ePoverty = DBconnection.getConnection();
      PreparedStatement stmt = (PreparedStatement) ePoverty.prepareStatement(getTripsQuery);
      ResultSet rs = (ResultSet) stmt.executeQuery();
      ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();

      tripsTable = new myTable();

      int tripsColCount = md.getColumnCount();

      String[] columnNames = new String[tripsColCount];
      for (int i = 1; i <= tripsColCount; i++)
      {
         columnNames[i - 1] = md.getColumnName(i);
      }

      tripsTable.setColumnIdentifiers(columnNames);

      while (rs.next())
      {
         Object[] data = new String[tripsColCount];
         for (int i = 1; i <= tripsColCount; i++)
         {
            data[i - 1] = rs.getString(i);
         }
         tripsTable.addRow(data);
      }

      gBC.gridx = 0;
      gBC.gridy = 0;
      gBC.anchor = GridBagConstraints.CENTER;

      JTable dataTable = new JTable(tripsTable);
      dataTable.enableInputMethods(false);
      JScrollPane scrollPane = new JScrollPane(dataTable);
      dataTable.setFillsViewportHeight(true);
      topTripsTab.add(scrollPane, gBC);

      mainPanel.add(topTripsTab);
      mainPanel.add(bottomTripsTab);
      tabs.add("", mainPanel);

      /*Dimension componentsSize = new Dimension(450, 20);
      JLabel trip = new JLabel("TRIP:");
      gBC.fill = GridBagConstraints.NONE;
      gBC.anchor = GridBagConstraints.WEST;
      gBC.weightx = 0.5;
      gBC.gridx = 0;
      gBC.gridy = 0;
      topTripsTab.add(trip, gBC);
      trip.setMaximumSize(componentsSize);
      trip.setMinimumSize(componentsSize);
      
       */
      addBTN = new JButton("Add");
      gBC.gridheight = 3;
      gBC.gridx = 3;
      gBC.gridy = 3;
      bottomTripsTab.add(addBTN, gBC);
      addBTN.setOpaque(true);


      //*********************************************ACTION LISTENER FOR Add BUTTON
      addBTN.addActionListener(
         new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               UIManager.put("OptionPane.okButtonText", "SAVE CHANGES");
               // *****************************************CREATE AND ADD THE COMPONENTS
               tripNameTF = new JTextField(12);
               tripNameTF.setEditable(true);
               tripDescTF = new JTextField(12);
               tripDescTF.setEditable(true);
               tripDateTF = new JTextField(12);
               tripDateTF.setEditable(true);
               tripCostTF = new JTextField(12);
               tripCostTF.setEditable(true);
               //  editableCB = new JCheckBox("Edit Fields?");
               //  expeditionsList = new JComboBox();

               JPanel panel = new JPanel(new GridBagLayout());
               GridBagConstraints gBC = new GridBagConstraints();
               gBC.anchor = GridBagConstraints.WEST;

               // COLUMN 1
               gBC.weightx = 1;
               gBC.weighty = 1;
               gBC.gridx = 0;
               gBC.gridy = 0;
               panel.add(new JLabel("Trip Name:"), gBC);

               gBC.gridy = 1;
               panel.add(new JLabel("Trip Description:"), gBC);

               gBC.gridy = 2;
               panel.add(new JLabel("Trip Date:"), gBC);

               gBC.gridy = 3;
               panel.add(new JLabel("Trip Cost:"), gBC);

               // COLUMN 2
               gBC.gridx = 1;
               gBC.gridy = 0;
               panel.add(tripNameTF, gBC);

               gBC.gridy = 1;
               panel.add(tripDescTF, gBC);

               gBC.gridy = 2;
               panel.add(tripDateTF, gBC);

               gBC.gridy = 3;
               panel.add(tripCostTF, gBC);

               /*gBC.gridy = 4;
               gBC.fill = GridBagConstraints.HORIZONTAL;
               panel.add(expeditionsList, gBC);
               
               gBC.gridy = 5;
               panel.add(editableCB, gBC);*/

               int result = JOptionPane.showConfirmDialog(null, panel, "Add A Trip",
                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            }
         });
   }

   public class myTable extends DefaultTableModel
   {
      @Override
      public boolean isCellEditable(int row, int col)
      {
         return false;
      }
   }
}
