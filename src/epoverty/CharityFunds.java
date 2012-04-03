package epoverty;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CharityFunds extends JPanel
{
   private final int width = super.getWidth();
   private final int height = super.getHeight();
   private final int font = 20;
   //private BufferedImage profilePic = ImageIO.read(new File("Cloud.jpg"));
   private JPanel mainPanel;
   // CREATE AND ADD COMPONENTS TO THE TOP OF TAB
   GridBagConstraints gBC = new GridBagConstraints();

   CharityFunds(JTabbedPane tabs) throws SQLException, ClassNotFoundException
   {
      mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
      Dimension mainFundraiser = new Dimension(width, height - 15);
      mainPanel.setPreferredSize(mainFundraiser);
      mainPanel.setBackground(Color.DARK_GRAY);

      // CREATE TOP OF THE TAB
      BackgroundPanel topCharityTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      topCharityTab.setLayout(new GridLayout(0, 1, 1, 1));
      Dimension topFundraiser = new Dimension(width, 725);
      topCharityTab.setPreferredSize(topFundraiser);
      GradientPaint paintTopFundraiser = new GradientPaint(0, 0, Color.BLACK, 100, 100, Color.DARK_GRAY);
      topCharityTab.setPaint(paintTopFundraiser);

      // CREATE BOTTOM OF THE TAB
      BackgroundPanel bottomCharityTab = new BackgroundPanel(null, BackgroundPanel.ACTUAL, 1.0f, 0.5f);
      bottomCharityTab.setLayout(new GridBagLayout());
      Dimension bottomFundraiser = new Dimension(width, 175);
      bottomCharityTab.setPreferredSize(bottomFundraiser);
      GradientPaint paintBottomFundraiser = new GradientPaint(0, 150, Color.LIGHT_GRAY, 100, 50, Color.DARK_GRAY);
      bottomCharityTab.setPaint(paintBottomFundraiser);

//      // CREATE AND ADD COMPONENTS TO THE TOP OF TAB
//      GridBagConstraints gBC = new GridBagConstraints();
      gBC.fill = GridBagConstraints.NONE;

      //Set up UI for JTable
      UIManager.put("Table.background", Color.DARK_GRAY);
      UIManager.put("Table.foreground", Color.WHITE);
      UIManager.put("Table.font", new Font("SansSerif", Font.BOLD, 14));
      UIManager.put("Table.selectionBackground", Color.WHITE);
      UIManager.put("Table.selectionForeground", Color.BLACK);
      UIManager.put("Table.gridColor", new Color(35, 35, 35));
      UIManager.put("TableHeader.background", Color.LIGHT_GRAY);
      UIManager.put("TableHeader.foreground", new Color(35, 35, 35));


      //Query for account deposits - DB
      String getDepositsQuery = ("select d.donationID,o.donorid,o.fundraiserid,DATE_FORMAT(o.donationdate,'%m-%d-%Y') as Date,d.amount "
                                 + "from deposits d "
                                 + "inner join donations o on d.donationid = o.donationid "
                                 + "where accountid = 3");

      //Creates connection and runs query  - DB
      Connection ePoverty = DBconnection.getConnection();
      PreparedStatement stmt = (PreparedStatement) ePoverty.prepareStatement(getDepositsQuery);
      ResultSet rs = (ResultSet) stmt.executeQuery();
      ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();

      //Using a DefaultTableModel to pull column information and row data from query. - DB
      myTable depositModel = new myTable();

      int depositColCount = md.getColumnCount();
      String[] columnNames = new String[depositColCount];
      for (int i = 1; i <= depositColCount; i++)
      {
         columnNames[i - 1] = md.getColumnName(i);
      }
      depositModel.setColumnIdentifiers(columnNames);

      while (rs.next())
      {
         Object[] data = new String[depositColCount];
         for (int i = 1; i <= depositColCount; i++)
         {
            data[i - 1] = rs.getString(i);
         }
         depositModel.addRow(data);
      }


      gBC.gridx = 0;
      gBC.gridy = 0;
      gBC.anchor = GridBagConstraints.CENTER;
      JTable depositsTable = new JTable(depositModel);
      depositsTable.enableInputMethods(false);
      JScrollPane withdrawlScrollPane = new JScrollPane(depositsTable);
      depositsTable.setFillsViewportHeight(true);
      topCharityTab.add(withdrawlScrollPane, gBC);

      //Query for withdrawls for the account - DB
      String getWithdrwalsQuery = ("select * from withdrawals "
                                   + "where accountid = 3");

      stmt = (PreparedStatement) ePoverty.prepareStatement(getWithdrwalsQuery);
      rs = (ResultSet) stmt.executeQuery();
      md = (ResultSetMetaData) rs.getMetaData();

      myTable withdrwalModel = new myTable();

      int withdrawlColCount = md.getColumnCount();
      columnNames = new String[withdrawlColCount];
      for (int i = 1; i <= withdrawlColCount; i++)
      {
         columnNames[i - 1] = md.getColumnName(i);
      }
      withdrwalModel.setColumnIdentifiers(columnNames);

      while (rs.next())
      {
         Object[] data = new String[withdrawlColCount];
         for (int i = 1; i <= withdrawlColCount; i++)
         {
            data[i - 1] = rs.getString(i);
         }
         withdrwalModel.addRow(data);
      }
      //Query for account balance - DB
      String getAcctBalanceQuery = ("select balance from accounts where accountid = 3");
      stmt = (PreparedStatement) ePoverty.prepareStatement(getAcctBalanceQuery);
      rs = (ResultSet) stmt.executeQuery();

      String acctBalance = "";
      while (rs.next())
      {
         acctBalance = rs.getString(1);
      }

      DBconnection.closeConnection();

      gBC.gridx = 0;
      gBC.gridy = 1;
      gBC.anchor = GridBagConstraints.CENTER;
      JTable withdrawlTable = new JTable(withdrwalModel);
      withdrawlTable.enableInputMethods(false);
      JScrollPane depositScrollPane = new JScrollPane(withdrawlTable);
      withdrawlTable.setFillsViewportHeight(true);
      topCharityTab.add(depositScrollPane, gBC);

      //Bottom section - DB
      //Column 1 - DB
      JLabel fromLabel = new JLabel("From: ");
      fromLabel.setForeground(EPovertyMain.font);
      fromLabel.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.5;
      gBC.gridx = 0;
      gBC.gridy = 0;
      gBC.anchor = GridBagConstraints.EAST;
      gBC.fill = GridBagConstraints.NONE;
      gBC.ipady = 0;
      bottomCharityTab.add(fromLabel, gBC);

      JLabel toLabel = new JLabel("To: ");
      toLabel.setForeground(EPovertyMain.font);
      toLabel.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.5;
      gBC.gridx = 0;
      gBC.gridy = 1;
      gBC.anchor = GridBagConstraints.EAST;
      bottomCharityTab.add(toLabel, gBC);

      //Column 2 - DB
      JTextField fromDate = new JTextField();
      fromDate.setForeground(EPovertyMain.font);
      fromDate.setFont(new Font("Serif", Font.PLAIN, font));
      fromDate.setColumns(15);
      gBC.weightx = 0.5;
      gBC.gridx = 1;
      gBC.gridy = 0;
      gBC.anchor = GridBagConstraints.WEST;
      bottomCharityTab.add(fromDate, gBC);

      JTextField toDate = new JTextField();
      toDate.setForeground(EPovertyMain.font);
      toDate.setFont(new Font("Serif", Font.PLAIN, font));
      toDate.setColumns(15);
      gBC.weightx = 0.5;
      gBC.gridx = 1;
      gBC.gridy = 1;
      gBC.anchor = GridBagConstraints.WEST;
      bottomCharityTab.add(toDate, gBC);

      JButton search = new JButton("Search");
      search.setForeground(EPovertyMain.font);
      search.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.5;
      gBC.gridx = 1;
      gBC.gridy = 2;
      gBC.anchor = GridBagConstraints.BELOW_BASELINE_LEADING;
      bottomCharityTab.add(search, gBC);

      //Column 3 - DB
      JLabel column3 = new JLabel("");
      column3.setForeground(EPovertyMain.font);
      column3.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.5;
      gBC.gridx = 2;
      gBC.gridy = 0;
      gBC.anchor = GridBagConstraints.FIRST_LINE_START;
      bottomCharityTab.add(column3, gBC);

      //Column 4 - DB
      JLabel accountBalanceLabel = new JLabel("Account Balance:");
      accountBalanceLabel.setForeground(EPovertyMain.font);
      accountBalanceLabel.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.5;
      gBC.gridx = 3;
      gBC.gridy = 0;
      gBC.anchor = GridBagConstraints.EAST;
      gBC.ipadx = 10;
      bottomCharityTab.add(accountBalanceLabel, gBC);

      JLabel depositsLabel = new JLabel("Deposits:");
      depositsLabel.setForeground(EPovertyMain.font);
      depositsLabel.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.5;
      gBC.gridx = 3;
      gBC.gridy = 1;
      gBC.anchor = GridBagConstraints.EAST;
      bottomCharityTab.add(depositsLabel, gBC);

      JLabel withdrawlsLabel = new JLabel("Withdrawls:");
      withdrawlsLabel.setForeground(EPovertyMain.font);
      withdrawlsLabel.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.5;
      gBC.gridx = 3;
      gBC.gridy = 2;
      gBC.anchor = GridBagConstraints.EAST;
      bottomCharityTab.add(withdrawlsLabel, gBC);

      //Column 5 - DB
      JLabel balance = new JLabel("$" + acctBalance);
      balance.setForeground(EPovertyMain.font);
      balance.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.5;
      gBC.gridx = 4;
      gBC.gridy = 0;
      gBC.anchor = GridBagConstraints.WEST;
      bottomCharityTab.add(balance, gBC);

      JLabel deposits = new JLabel("");
      deposits.setForeground(EPovertyMain.font);
      deposits.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.5;
      gBC.gridx = 4;
      gBC.gridy = 1;
      gBC.anchor = GridBagConstraints.WEST;
      bottomCharityTab.add(deposits, gBC);

      JLabel withdrawls = new JLabel("");
      withdrawls.setForeground(EPovertyMain.font);
      withdrawls.setFont(new Font("Serif", Font.PLAIN, font));
      gBC.weightx = 0.5;
      gBC.gridx = 4;
      gBC.gridy = 2;
      gBC.anchor = GridBagConstraints.WEST;
      bottomCharityTab.add(withdrawls, gBC);

      //Adds up all of the deposits in the account and sets total for deposit text.
      double depositAmount = 0;
      String acctDeposits;
      for (int i = 0; i < depositModel.getRowCount(); i++)
      {
         acctDeposits = depositModel.getValueAt(i, depositColCount - 1).toString();
         depositAmount = depositAmount + Double.parseDouble(acctDeposits);
      }

      deposits.setText("$" + depositAmount);

      //Adds up all of the withdrawls in the account and sets the total for the withdrawl text.
      double withdrawlAmount = 0;
      String acctWithdrawls;
      for (int i = 0; i < withdrwalModel.getRowCount(); i++)
      {
         acctWithdrawls = withdrwalModel.getValueAt(i, 2).toString();
         withdrawlAmount = withdrawlAmount + Double.parseDouble(acctWithdrawls);
      }

      withdrawls.setText("$" + withdrawlAmount);

      // ADD TOP AND BOTTOM TO THE MAIN AND PUT THEM INTO A TAB
      mainPanel.add(topCharityTab);
      mainPanel.add(bottomCharityTab);
      tabs.add("", mainPanel);
      tabs.update(null);
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