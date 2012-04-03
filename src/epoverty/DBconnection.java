/*
 * This class will handle all connections to the DB, by checking if
 * the connection is still valid first, and if needed reconnecting 
 * before returning the Connection.  You can get the connection by calling 
 * the getConnection() method.  Make sure to Close the method using the 
 * closeConnection() method.
 * 
 * All of the static fields will be initialized when called by EPoverty.java
 * This allows the user to still adjust settings to make the initial connection 
 * that verifies if they are an admin in the system!
 */
package epoverty;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.ResultSetMetaData;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.imageio.ImageIO;

public class DBconnection
{
   //*********************************************STATIC VARIABLES USED FOR CONNECTION
   private static String url;
   private static String driver;
   private static String dbLogin;
   private static String dbPassword;
   private static Connection conn;
   private static PreparedStatement stmt;
   private static ResultSet rs;

   public DBconnection(String url, String driver, String dbLogin, String dbPassword) throws ClassNotFoundException, SQLException
   {
      //******************************************INITIALIZE STATIC VARAIBLES FROM ABOVE
      DBconnection.url = url;
      DBconnection.driver = driver;
      DBconnection.dbLogin = dbLogin;
      DBconnection.dbPassword = dbPassword;

      // Open and close the connection for a test
      Class.forName(driver);
      conn = (Connection) DriverManager.getConnection(url, dbLogin, dbPassword);
      conn.close();
   }

   //*********************************************CHECKS CONNECTION AND RETURNS IT
   public static Connection getConnection() throws SQLException, ClassNotFoundException
   {
      if (conn.isClosed())
      {
         //***************************************RE-ESTABLISH CONNECTION
         Class.forName(driver);
         conn = (Connection) DriverManager.getConnection(url, dbLogin, dbPassword);
         return conn;
      }
      else
      {
         return conn;
      }
   }

   //*********************************************CLOSES THE CONNECTION FOR SECURITY REASONS THIS SHOULD ALWAYS BE CALLED
   public static void closeConnection() throws SQLException
   {
      conn.close();
   }

   //*********************************************CLOSES THE STATEMENT SHOULD ALWAYS BE CALLED
   public static void closeStatement() throws SQLException
   {
      stmt.close();
   }

   //*********************************************CLOSES THE RESULT SET SHOULD ALWAYS BE CALLED
   public static void closeResultSet() throws SQLException
   {
      rs.close();
   }

   // ********************************************RETURNS AN ArrayList OF ALL TRIP INFORMATION
   public static ArrayList getTrips(int tripID) throws SQLException, ClassNotFoundException
   {
      ArrayList allTripData = new ArrayList();
      String getTripsQuery = "SELECT * FROM expeditions WHERE expeditionId = ?";

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(getTripsQuery);
      stmt.setInt(1, tripID);
      rs = (ResultSet) stmt.executeQuery();
      ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
      int columns = md.getColumnCount();

      // GET FUNDRAISERS AND STORE THEM IN AN ARRAYLIST
      while (rs.next())
      {
         ArrayList row = new ArrayList(columns);

         for (int i = 1; i <= columns; i++)
         {
            row.add(rs.getObject(i));
         }
         allTripData.add(row);
      }

      return allTripData;
   }

   //*********************************************RETURNS AN ArrayList OF ALL TRIPS IN DATABASE
   public static ArrayList getTrips() throws SQLException, ClassNotFoundException
   {
      ArrayList possibleTrips = new ArrayList();
      String getTripsQuery = "SELECT name, expeditionId FROM expeditions";

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(getTripsQuery);
      rs = (ResultSet) stmt.executeQuery();
      ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
      int columns = md.getColumnCount();

      // GET FUNDRAISERS AND STORE THEM IN AN ARRAYLIST
      while (rs.next())
      {
         ArrayList row = new ArrayList(columns);

         for (int i = 1; i <= columns; i++)
         {
            row.add(rs.getObject(i));
         }
         possibleTrips.add(row);
      }

      return possibleTrips;
   }

   //*********************************************ADD PERSON TO DONORS VIA EMAIL
   public static void insertPersonIntoDonors(String email) throws SQLException, ClassNotFoundException
   {
      String addDonor = "INSERT into donors (personId) "
         + "SELECT p.personId FROM persons AS p "
         + "WHERE p.emailAddress = '" + email + "'";

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(addDonor);
      stmt.executeUpdate();
   }

   //*********************************************ADD PERSON TO DONORS VIA EMAIL
   public static void insertPersonIntoDonors(int personID) throws SQLException, ClassNotFoundException
   {
      String addDonor = "INSERT into donors (personId) values (?)";

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(addDonor);
      stmt.setInt(1, personID);
      stmt.executeUpdate();

   }

   //*********************************************ADD PERSON TO FUNDRAISERS VIA EMAIL
   public static void insertPersonIntoFundraisers(String email, String raiseGoal, int expeditionId) throws SQLException, ClassNotFoundException
   {
      System.out.println("in fundraisers insert from email");
      String addDonor = "INSERT into fundraisers (personId, expeditionId, raiseGoal) "
         + "SELECT persons.personId,  '" + expeditionId + "', '" + raiseGoal + "' " + "FROM persons "
         + "WHERE persons.emailAddress = '" + email + "'";

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(addDonor);
      stmt.executeUpdate();
   }

   //*********************************************ADD PERSON TO FUNDRAISERS VIA PERSONID
   public static void insertPersonIntoFundraisers(int personID, String raiseGoal, int expeditionID) throws SQLException, ClassNotFoundException
   {
      System.out.println("in fundraisers insert from personID");
      String addDonor = "INSERT into fundraisers (personId, raiseGoal, expeditionId) values (?, ?, ?)";

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(addDonor);
      stmt.setInt(1, personID);
      stmt.setString(2, raiseGoal);
      stmt.setInt(3, expeditionID);
      System.out.println("Statement " + stmt);
      stmt.executeUpdate();
   }

   //*********************************************ADD PERSON TO ADMINS VIA EMAIL
   public static void insertPersonIntoAdmins(String email) throws SQLException, ClassNotFoundException
   {
      String addDonor = "INSERT into admins (personId) "
         + "SELECT persons.personId FROM persons "
         + "WHERE persons.emailAddress = '" + email + "'";

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(addDonor);
      stmt.executeUpdate();
   }

   //*********************************************ADD PERSON TO ADMINS VIA PERSONID
   public static void insertPersonIntoAdmins(int personID) throws SQLException, ClassNotFoundException
   {
      String addDonor = "INSERT into admins (personId) values (?)";

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(addDonor);
      stmt.setInt(1, personID);
      stmt.executeUpdate();
   }

   //*********************************************ADDS A PERSON INTO THE PERSONS TABLE
   public static void addPerson(String fn, String mn, String ln, String pn, String ea, String ast, String ac, String as, String az, BufferedImage photo, String pw) throws SQLException, ClassNotFoundException, IOException
   {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(photo, "jpg", baos);
      baos.flush();
      byte[] photoBytes = baos.toByteArray();
      String addPerson = "INSERT into persons (firstName, middleName, lastName, phoneNumber, emailAddress, addressStreet, "
         + "addressCity, addressState, addressZip, photo, password) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(addPerson);
      stmt.setString(1, fn);
      stmt.setString(2, mn);
      stmt.setString(3, ln);
      stmt.setString(4, pn);
      stmt.setString(5, ea);
      stmt.setString(6, ast);
      stmt.setString(7, ac);
      stmt.setString(8, as);
      stmt.setString(9, az);
      stmt.setBytes(10, photoBytes);
      stmt.setString(11, pw);
      stmt.executeUpdate();
   }

   // ********************************************GETS A PHOTO FROM THE DB AND CONVERTS IT TO A BUFFEREDIMAGE
   public static BufferedImage getPhoto(int personId) throws SQLException, IOException, ClassNotFoundException
   {
      String sql = "select * from persons where personId = " + personId;

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(sql);
      rs = (ResultSet) stmt.executeQuery();

      if (rs.next())
      {
         byte[] photoBytes = rs.getBytes("photo");//gets the person's photo as an array of bytes
         if (photoBytes != null)//check to see if there is a photo on file for this person
         {
            ByteArrayInputStream bis = new ByteArrayInputStream(photoBytes);//create a ByteArrayInputStream from our array of bytes
            BufferedImage photo = ImageIO.read(bis);//use Java's native ImageIO class, and static read method to read from our bytes, and create a BufferedImage
            return photo;
         }
         else
         {
            return null;
         }
      }
      else
      {
         return null;
      }
   }

   // ********************************************USED TO UPDATE A PERSONS PHOTO
   public static boolean updatePhoto(int personId, BufferedImage photo) throws SQLException, IOException, ClassNotFoundException
   {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(photo, "jpg", baos);
      baos.flush();
      byte[] photoBytes = baos.toByteArray();

      String sql = "update persons set photo = ? where personId = ?";
      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(sql);

      stmt.setBytes(1, photoBytes);
      stmt.setInt(2, personId);

      int status = stmt.executeUpdate();

      return (status > 0);
   }

   //*********************************************GET ALL PEOPLE
   public static ArrayList getPeople() throws SQLException, ClassNotFoundException
   {
      String peoplesQuery = "SELECT p.lastName, p.firstName, p.personId From persons AS p";

      ArrayList possibleNames = new ArrayList();

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(peoplesQuery);
      rs = (ResultSet) stmt.executeQuery();
      ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
      int columns = md.getColumnCount();

      // GET PEOPLE AND STORE THEM IN AN ARRAYLIST
      while (rs.next())
      {
         ArrayList row = new ArrayList(columns);

         for (int i = 1; i <= columns; i++)
         {
            row.add(rs.getObject(i));
         }
         possibleNames.add(row);
      }

      return possibleNames;
   }

   //*********************************************GET ALL FUNDRAISERS
   public static ArrayList getFundraisers() throws SQLException, ClassNotFoundException
   {
      String fundraisersQuery = "SELECT DISTINCT p.lastName, p.firstName, p.personId FROM persons AS p, fundraisers AS f "
         + "WHERE f.personId = p.personId";

      ArrayList possibleNames = new ArrayList();

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(fundraisersQuery);
      rs = (ResultSet) stmt.executeQuery();
      ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
      int columns = md.getColumnCount();

      // GET FUNDRAISERS AND STORE THEM IN AN ARRAYLIST
      while (rs.next())
      {
         ArrayList row = new ArrayList(columns);

         for (int i = 1; i <= columns; i++)
         {
            row.add(rs.getObject(i));
         }
         possibleNames.add(row);
      }

      return possibleNames;
   }

   //*********************************************GET ALL FUNDRAISERS
   public static ArrayList getDonors() throws SQLException, ClassNotFoundException
   {
      String fundraisersQuery = "SELECT DISTINCT p.lastName, p.firstName, p.personId FROM persons AS p, donors AS d "
         + "WHERE d.personId = p.personId";

      ArrayList possibleNames = new ArrayList();

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(fundraisersQuery);
      rs = (ResultSet) stmt.executeQuery();
      ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
      int columns = md.getColumnCount();

      // GET FUNDRAISERS AND STORE THEM IN AN ARRAYLIST
      while (rs.next())
      {
         ArrayList row = new ArrayList(columns);

         for (int i = 1; i <= columns; i++)
         {
            row.add(rs.getObject(i));
         }
         possibleNames.add(row);
      }

      return possibleNames;
   }

   //*********************************************INSERT NEW TRIP INTO EXPEDITIONS TABLE
   public static void addExpedition(String name, String description, String cost, String raiseGoal,
                                    String depart, String retrn, String cutoff) throws SQLException, ClassNotFoundException, ParseException
   {
      // CONVERT THE String to util.Date to an sql.Date
      DateFormat formatter;
      java.util.Date departDate;
      java.util.Date returnDate;
      java.util.Date cutoffDate;
      formatter = new SimpleDateFormat("yyyy-MM-dd");
      departDate = (java.util.Date) formatter.parse(depart);
      returnDate = (java.util.Date) formatter.parse(retrn);
      cutoffDate = (java.util.Date) formatter.parse(cutoff);
      java.sql.Date sqlDepart = new java.sql.Date(departDate.getTime());
      java.sql.Date sqlReturn = new java.sql.Date(returnDate.getTime());
      java.sql.Date sqlCutoff = new java.sql.Date(cutoffDate.getTime());

      String sql = "INSERT into expeditions (name, expeditionDescription, cost, defaultRaiseGoal, departDate, returnDate, cutoffDate)"
         + " values (?, ?, ?, ?, ?, ?, ?)";
      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(sql);

      stmt.setString(1, name);
      stmt.setString(2, description);
      stmt.setString(3, cost);
      stmt.setString(4, raiseGoal);
      stmt.setDate(5, sqlDepart);
      stmt.setDate(6, sqlReturn);
      stmt.setDate(7, sqlCutoff);

      stmt.executeUpdate();
   }

   //*********************************************CHECKS IF PERSON IS ADMIN
   public static boolean isAdmin(int personId) throws SQLException, ClassNotFoundException
   {
      String fundraisersQuery = "SELECT a.adminId FROM admins AS a, persons AS p "
         + "WHERE p.personId = a.personId "
         + "AND p.personId = '" + personId + "'";

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(fundraisersQuery);
      rs = (ResultSet) stmt.executeQuery();

      // CHECK IF RESULT SET IS EMPTY OR NOT
      if (rs.next())
      {
         return true;
      }
      else
      {
         return false;
      }
   }

   //*********************************************CHECKS IF PERSON IS ADMIN
   public static boolean isFundraiser(int personId) throws SQLException, ClassNotFoundException
   {
      String fundraisersQuery = "SELECT f.fundraiserId FROM fundraisers AS f, persons AS p "
         + "WHERE p.personId = f.personId "
         + "AND p.personId = '" + personId + "'";

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(fundraisersQuery);
      rs = (ResultSet) stmt.executeQuery();

      // CHECK IF RESULT SET IS EMPTY OR NOT
      if (rs.next())
      {
         return true;
      }
      else
      {
         return false;
      }
   }

   //*********************************************CHECKS IF PERSON IS ADMIN
   public static boolean isDonor(int personId) throws SQLException, ClassNotFoundException
   {
      String fundraisersQuery = "SELECT d.donorId FROM donors AS d, persons AS p "
         + "WHERE p.personId = d.personId "
         + "AND p.personId = '" + personId + "'";

      conn = DBconnection.getConnection();
      stmt = (PreparedStatement) conn.prepareStatement(fundraisersQuery);
      rs = (ResultSet) stmt.executeQuery();

      // CHECK IF RESULT SET IS EMPTY OR NOT
      if (rs.next())
      {
         return true;
      }
      else
      {
         return false;
      }
   }

   //*********************************************GET THE SPECIFIED NUMBER OF MONTHS
   public static Object[] getMonths(int numberOfMonths)
   {
      Object[] months = new Object[numberOfMonths];

      DateFormat dateFormat = new SimpleDateFormat("MM");
      DateFormatSymbols dfs = new DateFormatSymbols();
      String[] monthsByName = dfs.getMonths();

      Calendar cal = Calendar.getInstance();

      for (int x = 0; x < numberOfMonths; x++)
      {
         months[x] = monthsByName[Integer.parseInt(dateFormat.format(cal.getTime())) - 1];
         cal.add(Calendar.MONTH, -1);
      }

      return months;
   }

   //*********************************************GET THE AMOUNT OF DONATIONS FOR THE REVEIVED MONTHS
   public static Object[] getMonthlyDonations(Object[] months) throws SQLException, ClassNotFoundException
   {
      // GET THE NUMBER OF MONTHS TO ADD UP
      int numberOfMonths = months.length;

      // DOUBLE FOR DONATION TOTAL FOR EACH MONTH
      double totalDonations = 0;

      // WHERE THE DATA WILL BE STORED AND EVENTUALLY RETURNED
      Object[] donations = new Object[numberOfMonths];

      // GET A CONNECTION
      conn = DBconnection.getConnection();

      // STRING FOR QUERY
      String donationsQuery = "SELECT donationAmount FROM donations WHERE donationDate "
         + "BETWEEN ? AND ?";

      // DATE FORMAT AND CALENDAR OBJECTS
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
      Calendar cal = Calendar.getInstance();

      // RUN THROUGH EACH MONTH, QUERYING THE DB AND ADDING UP THE DONATIONS FOR THAT MONTH
      for (int x = 0; x < numberOfMonths; x++)
      {
         // GET THE YEAR AND MONTH AND THE MAX DAYS FOR THE MONTH
         String yearMonth = dateFormat.format(cal.getTime());
         int daysMax = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
         String firstOfMonth = yearMonth +"-1";
         String lastOfMonth = yearMonth +"-" +daysMax;

         // PREPARE THE STATEMENT AND EXECUTE IT
         stmt = (PreparedStatement) conn.prepareStatement(donationsQuery);
         stmt.setString(1, firstOfMonth);
         stmt.setString(2, lastOfMonth);
         rs = (ResultSet) stmt.executeQuery();

         // RUN THROUGH RESULTS AND ADD THEM UP
         while (rs.next())
         {
            totalDonations += Double.parseDouble(rs.getString(1));
         }
         
         // ADD THE DATA TO THE OBJECT ARRAY, RESET totalDonations, and SUBTRACT A MONTH
         donations[x] = totalDonations;
         totalDonations = 0;
         cal.add(Calendar.MONTH, -1);         
      }      
      return donations;
   }
   
   //*********************************************GET THE AMOUNT OF DONATIONS FOR THE REVEIVED MONTHS
   public static Object[] getMonthlyWithdrawals(Object[] months) throws SQLException, ClassNotFoundException
   {
      // GET THE NUMBER OF MONTHS TO ADD UP
      int numberOfMonths = months.length;

      // DOUBLE FOR WITHDRAWAL TOTAL FOR EACH MONTH
      double totalWithdrawals = 0;

      // WHERE THE DATA WILL BE STORED AND EVENTUALLY RETURNED
      Object[] withdrawals = new Object[numberOfMonths];

      // GET A CONNECTION
      conn = DBconnection.getConnection();

      // STRING FOR QUERY
      String donationsQuery = "SELECT amount FROM withdrawals WHERE withdrawalDate "
         + "BETWEEN ? AND ?";

      // DATE FORMAT AND CALENDAR OBJECTS
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
      Calendar cal = Calendar.getInstance();

      // RUN THROUGH EACH MONTH, QUERYING THE DB AND ADDING UP THE DONATIONS FOR THAT MONTH
      for (int x = 0; x < numberOfMonths; x++)
      {
         // GET THE YEAR AND MONTH AND THE MAX DAYS FOR THE MONTH
         String yearMonth = dateFormat.format(cal.getTime());
         int daysMax = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
         String firstOfMonth = yearMonth +"-1";
         String lastOfMonth = yearMonth +"-" +daysMax;

         // PREPARE THE STATEMENT AND EXECUTE IT
         stmt = (PreparedStatement) conn.prepareStatement(donationsQuery);
         stmt.setString(1, firstOfMonth);
         stmt.setString(2, lastOfMonth);
         rs = (ResultSet) stmt.executeQuery();

         // RUN THROUGH RESULTS AND ADD THEM UP
         while (rs.next())
         {
            totalWithdrawals += Double.parseDouble(rs.getString(1));
         }
         
         // ADD THE DATA TO THE OBJECT ARRAY, RESET totalDonations, and SUBTRACT A MONTH
         withdrawals[x] = totalWithdrawals;
         totalWithdrawals = 0;
         cal.add(Calendar.MONTH, -1);         
      }      
      return withdrawals;
   }
}
