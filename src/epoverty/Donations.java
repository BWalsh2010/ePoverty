package epoverty;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;

public class Donations extends JPanel
{
    private final int width = super.getWidth();
    private final int heigth = super.getHeight();
    private final int font = 20;
    private JTextField firstName_TF;
    private JTextField lastName_TF;
    private Connection conn;
    private myTable pTable = new myTable();
    private int pColCount = 2;
    private String[] pColNames = new String[pColCount];
    private JTable pJTable = new JTable(pTable);
    private JScrollPane pScrollPane = new JScrollPane(pJTable);
    private PreparedStatement pstmt;
    private ResultSet prs;
    private ResultSetMetaData pmd;
    private BackgroundPanel bottomDonationsTab;
    
    private JPanel mainPanel;
    
    GridBagConstraints gBC = new GridBagConstraints();
    
   Donations(JTabbedPane tabs)throws SQLException, ClassNotFoundException
   {
        
       mainPanel = new JPanel();
       mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
       Dimension mainDonation = new Dimension(width, heigth-15);
       mainPanel.setPreferredSize(mainDonation);
       mainPanel.setBackground(Color.DARK_GRAY);
       
       //Create the main content
       //Create top of page content
       BackgroundPanel topDonationsTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL,1.0f,0.5f);
       topDonationsTab.setLayout(new GridLayout(0, 1, 1, 1));
       Dimension topDonation = new Dimension(width, 725);
       topDonationsTab.setPreferredSize(topDonation);
       GradientPaint paintTopDonations = new GradientPaint(0, 0, Color.BLACK, 100, 100, Color.DARK_GRAY);
       topDonationsTab.setPaint(paintTopDonations);
      
       // Create bottom of page content
       bottomDonationsTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
       bottomDonationsTab.setLayout(new GridBagLayout());
       Dimension bottomDonations = new Dimension(width/3, 175);
       bottomDonationsTab.setPreferredSize(bottomDonations);
       GradientPaint paintBottomFundraiser = new GradientPaint(0, 150, Color.LIGHT_GRAY, 100, 50, Color.DARK_GRAY);
       bottomDonationsTab.setPaint(paintBottomFundraiser);
     
       //setup ui for jtable
       UIManager.put("Table.background", Color.DARK_GRAY);
      UIManager.put("Table.foreground", Color.WHITE);
      UIManager.put("Table.font", new Font("SansSerif", Font.BOLD, 14));
      UIManager.put("Table.selectionBackground", Color.WHITE);
      UIManager.put("Table.selectionForeground", Color.BLACK);
      UIManager.put("Table.gridColor", new Color(35, 35, 35));
      UIManager.put("TableHeader.background", Color.LIGHT_GRAY);
      UIManager.put("TableHeader.foreground", new Color(35, 35, 35));
      UIManager.put("TableHeader.font", new Font("SansSerif", Font.BOLD, 16));
     
       String getDonationsQuery = "SELECT d.donationDate, d.donationAmount, p.firstName, p.lastName " +
           "FROM donations d " +
           "inner join donors o on d.donationId = o.donorId " +
           "inner join persons p on o.personId = p.personId ";
     
       //Creates connection and runs query  - DB
       Connection ePoverty = DBconnection.getConnection();         
       PreparedStatement stmt = (PreparedStatement) ePoverty.prepareStatement(getDonationsQuery);
       ResultSet rs = (ResultSet) stmt.executeQuery();
       ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
             
       //Using a DefaultTableModel to pull column information and row data from query. - DB
       myTable donationModel = new myTable();
       
       int donationsColCount = md.getColumnCount();
      
       String[] columnNames = new String[donationsColCount];
       for (int i = 1; i <= donationsColCount; i++) 
       {
           columnNames[i - 1] = md.getColumnName(i);
       }
       
       donationModel.setColumnIdentifiers(columnNames);
       
       while (rs.next()) 
       {
           Object[] data = new String[donationsColCount];
           for (int i = 1; i <= donationsColCount; i++) {
               data[i - 1] = rs.getString(i);
           }        
           donationModel.addRow(data);
       }  
       
       gBC.gridx = 0;
       gBC.gridy = 0;
       gBC.anchor = GridBagConstraints.CENTER;
       
       JTable donationsTable = new JTable(donationModel);
       donationsTable.enableInputMethods(false);
       JScrollPane withdrawlScrollPane = new JScrollPane(donationsTable);
       donationsTable.setFillsViewportHeight(true);
       topDonationsTab.add(withdrawlScrollPane, gBC);
       
       //BOTTOM SECTION******
       //Bottom section 
      //Column 1 
      JLabel fromLabel = new JLabel("Donor First Name: ");
      fromLabel.setForeground(EPovertyMain.font);
      fromLabel.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.5;
      gBC.gridx = 0;
      gBC.gridy = 0;
      gBC.anchor = GridBagConstraints.EAST;      
      gBC.fill = GridBagConstraints.NONE;
      gBC.ipady = 0;
      bottomDonationsTab.add(fromLabel, gBC);
      
      JLabel lNameLabel = new JLabel("Last Name: ");
      lNameLabel.setForeground(EPovertyMain.font);
      lNameLabel.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.5;
      gBC.gridx = 0;
      gBC.gridy = 1;
      gBC.anchor = GridBagConstraints.EAST;      
      gBC.fill = GridBagConstraints.NONE;
      gBC.ipady = 0;
      bottomDonationsTab.add(lNameLabel, gBC);
      
      //Column 2 - DB
      firstName_TF = new JTextField(12);
      firstName_TF.setForeground(EPovertyMain.font);
      firstName_TF.setFont(new Font("Serif", Font.PLAIN, font));
      firstName_TF.setColumns(15);
      gBC.weightx = 0.25;
      gBC.gridx = 1;
      gBC.gridy = 0;
      gBC.anchor = GridBagConstraints.WEST; 
      gBC.fill = GridBagConstraints.HORIZONTAL;
      bottomDonationsTab.add(firstName_TF, gBC);
      
      lastName_TF = new JTextField(12);
      lastName_TF.setForeground(EPovertyMain.font);
      lastName_TF.setFont(new Font("Serif", Font.PLAIN, font));
      lastName_TF.setColumns(15);
      gBC.weightx = 0.25;
      gBC.gridx = 1;
      gBC.gridy = 1;
      gBC.anchor = GridBagConstraints.WEST;
      gBC.fill = GridBagConstraints.HORIZONTAL;
      bottomDonationsTab.add(lastName_TF, gBC);
      
      JButton search = new JButton("Search");
      search.setForeground(EPovertyMain.font);
      search.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.25;
      gBC.gridx = 1;
      gBC.gridy = 2;
      gBC.anchor = GridBagConstraints.BELOW_BASELINE_LEADING;
      bottomDonationsTab.add(search, gBC);
            

      pColNames[0] = "First Name";
      pColNames[1] = "Last Name";
      
      pTable.setColumnIdentifiers(pColNames);
      
      String query = "SELECT firstName, lastName " +
                        "FROM persons " + 
                        "WHERE persons.firstName LIKE '%%' " +
                        "AND persons.lastName LIKE '%%'";
      pstmt = (PreparedStatement) ePoverty.prepareStatement(query);
      prs = (ResultSet) pstmt.executeQuery();
      pmd = (ResultSetMetaData) prs.getMetaData();
       
      while (prs.next()) 
      {
           Object[] pdata = new String[pColCount];
           for (int i = 1; i <= pColCount; i++) {
               pdata[i - 1] = prs.getString(i);
           }        
           pTable.addRow(pdata);
      }  

      gBC.weightx=.5;
      gBC.gridheight = 10;
      gBC.gridwidth = 10;
       gBC.gridx = 3;
       gBC.gridy = 0;
       gBC.anchor = GridBagConstraints.CENTER;
       gBC.fill = GridBagConstraints.VERTICAL;
       
       pScrollPane.setPreferredSize(new Dimension(500,100));
       
       pJTable.enableInputMethods(false);
       pJTable.setFillsViewportHeight(true);
       bottomDonationsTab.add(pScrollPane, gBC);
      
      
      // ADD AN ACTION LISTENER TO THE BUTTON THAT WILL CHECK THE LOGIN INFO AND LOG INTO THE DATABASE
      search.addActionListener(
         new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               String firstName = firstName_TF.getText();
               String lastName = lastName_TF.getText();
                try {
                    int done = pTable.getRowCount();
                    for(int i = 0; i < done; i++)
                    {
                        pTable.removeRow(0);
                    }
                    
                    conn = DBconnection.getConnection();
                    String query = "SELECT firstName, lastName " +
                        "FROM persons " + 
                        "WHERE persons.firstName LIKE '" + firstName + "%' " +
                        "AND persons.lastName LIKE '"+lastName+"%'";
                    pstmt = (PreparedStatement) conn.prepareStatement(query);
                    prs = (ResultSet) pstmt.executeQuery();
                    pmd = (ResultSetMetaData) prs.getMetaData();
                                        
                    while (prs.next()) 
                    {
                        Object[] data = new String[pColCount];
                        for (int i = 1; i <= pColCount; i++) {
                            data[i - 1] = prs.getString(i);
                        }        
                        pTable.addRow(data);
                    } 
                    
                    
                    pJTable.enableInputMethods(false);
                    pJTable.setFillsViewportHeight(true);
                    bottomDonationsTab.add(pScrollPane, gBC);                  
                    
                } catch (SQLException ex) {
                    Logger.getLogger(Donations.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Donations.class.getName()).log(Level.SEVERE, null, ex);
                }                              
            }            
         });
               
       mainPanel.add(topDonationsTab);
       mainPanel.add(bottomDonationsTab);
       tabs.add("", mainPanel);
       DBconnection.closeConnection();
       
       
   }  
   
   //Prevents cells from being edited in table by overriding isCellEditable.
   public class myTable extends DefaultTableModel
   {
        @Override
        public boolean isCellEditable(int row, int col) {  
            return false;  
        } 
   }

   public void test()
   {
   
   }
}