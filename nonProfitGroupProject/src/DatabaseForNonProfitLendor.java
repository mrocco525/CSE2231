import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Database for non-profit tool lending organization.
 *
 * @author Joshua Baum, Craig Cirino, Mason Rocco, Channing Thornton
 *
 */
public final class DatabaseForNonProfitLendor {

    /**
     *
     */
    private static String mDATABASE = "C:\\Users\\mrocc\\Downloads\\FinalDB.db";

    /**
     * Constructor.
     */
    private DatabaseForNonProfitLendor() {
    }

    /**
     * Returns boolean on whether string can be interpreted as an int.
     *
     * @param input
     * @return boolean
     */
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Prompt allows for users to select certain actions pertaining to
     * warehouses or to quit the program.
     *
     * @param in
     * @param conn
     * @throws SQLException
     */
    private static void warehousePrompt(BufferedReader in, Connection conn)
            throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println(
                    "Enter the single character to choose WAREHOUSE interaction:");
            System.out.println("(v) View Warehouse Data");
            System.out.println("(n) New Warehouse");
            System.out.println("(e) Edit Warehouse");
            System.out.println("(d) Delete Warehouse");
            System.out.println("(s) Search");
            System.out.println("(w) Warehouse List");
            System.out.println("(q) Quit Program");
            String warehouseChoice = in.readLine();
            System.out.println();
            String warehouseID;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                if (warehouseChoice.length() == 0) {
                    System.out.println("Invalid Choice Selection.");
                } else {
                    switch (warehouseChoice.charAt(0)) {
                        case 'v':
                            System.out.println("Enter Warehouse ID:");
                            warehouseID = in.readLine();
                            if (isInteger(warehouseID)) {
                                int wID = Integer.parseInt(warehouseID);
                                enterWarehouseDatabase(in, wID, conn);
                            } else {
                                System.out.println("Invalid WarehouseID.");
                                System.out.println();
                            }
                            break;
                        case 'n':
                            newWarehouse(in, conn);
                            break;
                        case 'e':
                            System.out.println("Enter Warehouse ID:");
                            warehouseID = in.readLine();
                            if (isInteger(warehouseID)) {
                                int wID = Integer.parseInt(warehouseID);
                                String sqlE = "SELECT * FROM warehouse WHERE "
                                        + "warehouseId = ?;";
                                stmt = conn.prepareStatement(sqlE);
                                stmt.setInt(1, wID);
                                rs = stmt.executeQuery();
                                if (rs.next()) {
                                    editWarehouse(in, rs, wID, conn);
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "ERROR 404: Warehouse Not Found.");
                                }
                            } else {
                                System.out.println("Invalid WarehouseID.");
                                System.out.println();
                            }
                            break;
                        case 'd':
                            System.out.println("Enter Warehouse ID:");
                            warehouseID = in.readLine();
                            if (isInteger(warehouseID)) {
                                int wID = Integer.parseInt(warehouseID);
                                deleteWarehouse(wID, conn);
                            } else {
                                System.out.println("Invalid WarehouseID.");
                            }
                            break;
                        case 's':
                            System.out.println("Enter Warehouse ID:");
                            warehouseID = in.readLine();
                            if (isInteger(warehouseID)) {
                                int wID = Integer.parseInt(warehouseID);
                                showWarehouseInformation(wID, in, conn);
                            } else {
                                System.out.println("Invalid WarehouseID.");
                            }
                            break;
                        case 'w':
                            String sqlWL = "SELECT warehouseId, city "
                                    + "FROM Warehouse "
                                    + "ORDER BY warehouseId;";
                            stmt = conn.prepareStatement(sqlWL);
                            rs = stmt.executeQuery();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int columnCount = rsmd.getColumnCount();
                            System.out.println("WAREHOUSE LIST");
                            System.out.println("--------------");
                            while (rs.next()) {
                                int i;
                                for (i = 1; i <= columnCount; i++) {
                                    String id = rs.getString(i);
                                    i++;
                                    String city = rs.getString(i);
                                    System.out.println(id + " - " + city);
                                }
                            }
                            System.out.println();
                            System.out
                                    .println("Press enter when done viewing.");
                            in.readLine();
                            System.out.println();
                            break;
                        case 'q':
                            notDone = false;
                            break;
                        default:
                            System.out.println("Invalid Choice Selection.");
                            break;
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            System.out.println();
        }
    }

    /**
     * Deletes warehouse from database.
     *
     * @param wID
     * @param conn
     */
    private static void deleteWarehouse(int wID, Connection conn) {
        PreparedStatement stmt = null;
        try {
            String sqlD = "DELETE FROM Warehouse WHERE warehouseId = ?";
            stmt = conn.prepareStatement(sqlD);
            stmt.setInt(1, wID);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Successfully Deleted Warehouse.");
            } else {
                System.out.println("ERROR DELETING DATABASE");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Method allows user to input associated data values of warehouse and
     * assigns this information a warehouse ID.
     *
     * @param in
     * @param conn
     * @throws IOException
     * @throws SQLException
     */
    private static void newWarehouse(BufferedReader in, Connection conn)
            throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        try {
            String sql = "SELECT max(warehouseId) From warehouse;";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            String count = rs.getString(1);
            int warehouseID = 1 + Integer.parseInt(count);
            System.out.println();
            String phoneNum = "";
            boolean invalidPNum = true;
            while (invalidPNum) {
                System.out.println(
                        "Enter Warehouse Phone Number: (ex: 1234567890)");
                phoneNum = in.readLine();
                if (phoneNum.length() != 10) {
                    System.out.println(
                            "Invalid phone number length.  Try again.");
                } else {
                    invalidPNum = false;
                }
            }
            int storageCapacity = 0;
            boolean invalidStorCap = true;
            while (invalidStorCap) {
                System.out.println(
                        "Enter Warehouse Storage Capacity: (ex: 15000)");
                String storCapacity = in.readLine();
                if (isInteger(storCapacity)) {
                    storageCapacity = Integer.parseInt(storCapacity);
                    invalidStorCap = false;
                } else {
                    System.out.println("Invalid input.  Try again.");
                }
            }
            boolean invalidCity = true;
            String city = "";
            while (invalidCity) {
                System.out.println("Enter Warehouse City: (ex: Boston)");
                city = in.readLine();
                if (city.length() > 0) {
                    invalidCity = false;
                } else {
                    System.out.println("Invalid city name.  Try again.");
                }
            }
            boolean invalidAddress = true;
            String address = "";
            while (invalidAddress) {
                System.out.println(
                        "Enter Warehouse Address: (ex: 142 Chittenden Ave)");
                address = in.readLine();
                if (address.length() == 0) {
                    System.out.println("Invalid address. Try again.");
                } else {
                    invalidAddress = false;
                }
            }
            boolean invalidName = true;
            String managerName = "";
            while (invalidName) {
                System.out.println(
                        "Enter Warehouse Manager Name: (ex: John Smith)");
                managerName = in.readLine();
                if (managerName.length() == 0) {
                    System.out.println("Invalid name. Try again.");
                } else {
                    invalidName = false;
                }
            }
            boolean invalidDroneCap = true;
            int droneCapacity = 0;
            while (invalidDroneCap) {
                System.out.println("Enter Warehouse Drone Capacity: (ex: 450)");
                String droneCap = in.readLine();
                if (isInteger(droneCap)) {
                    droneCapacity = Integer.parseInt(droneCap);
                    invalidDroneCap = false;
                } else {
                    System.out.println("Invalid input.  Try again.");
                }
            }

            String sql2 = "INSERT INTO warehouse (warehouseId, phoneNumber, "
                    + "storageCapacity, city, address, managerName, droneCapacity) "
                    + "VALUES ('" + warehouseID + "', '" + phoneNum + "', '"
                    + storageCapacity + "', '" + city + "', '" + address
                    + "', '" + managerName + "', '" + droneCapacity + "');";
            stmt2 = conn.prepareStatement(sql2);
            int rowChange = stmt2.executeUpdate();
            if (rowChange > 0) {
                System.out.println("Success! You managed to add a warehouse!");
            } else {
                System.out
                        .println("Warehouse information unable to be stored.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (rs2 != null) {
                    rs2.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (stmt2 != null) {
                    stmt2.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Allows user to view certain information related to that particular
     * warehouse.
     *
     * @param in
     * @param wID
     * @param conn
     *
     */
    private static void enterWarehouseDatabase(BufferedReader in, int wID,
            Connection conn) throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println("Enter the single character to select option:");
            System.out.println("(w) Warehouse Information");
            System.out.println("(m) Members");
            System.out.println("(r) Rentals");
            System.out.println("(i) Inventory");
            System.out.println("(s) Service Repairer");
            System.out.println("(n) Manufacturer");
            System.out.println("(u) Useful Reports");
            System.out.println("(p) Repairs");
            System.out.println("(b) Back");
            String menuChoice = in.readLine();
            if (menuChoice.length() == 0) {
                System.out.println("Invalid Choice Selection.");
            } else {
                switch (menuChoice.charAt(0)) {
                    case 'w':
                        showWarehouseInformation(wID, in, conn);
                        break;
                    case 'm':
                        Member.memberMenu(in, wID, conn);
                        break;
                    case 'r':
                        Rent.rentMenu(in, conn);
                        break;
                    case 'i':
                        Inventory.inventoryMenu(in, wID, conn);
                        break;
                    case 's':
                        ServiceRepairer.serviceMenu(in, wID, conn);
                        break;
                    case 'n':
                        Manufacturer.manMenu(in, conn);
                        break;
                    case 'u':
                        UsefulReports.usefulReportMenu(in, conn);
                        break;
                    case 'p':
                        Repair.repairMenu(in, conn);
                        break;
                    case 'b':
                        notDone = false;
                        break;
                    default:
                        System.out.println();
                        System.out.println("Invalid Choice Selection.");
                        break;
                }
            }
        }
    }

    /**
     * Allows user to edit any erronous information stored in warehouse data.
     *
     * @param in
     * @param rs
     * @param wID
     * @param conn
     *
     */
    private static void editWarehouse(BufferedReader in, ResultSet rs, int wID,
            Connection conn) throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println(
                    "Enter the single character to select edit category:");
            System.out.println("(p) Phone Number");
            System.out.println("(s) Storage Capacity");
            System.out.println("(c) City");
            System.out.println("(a) Address");
            System.out.println("(m) Manager Name");
            System.out.println("(d) Drone Capacity");
            System.out.println("(b) Back");
            String menuChoice = in.readLine();
            System.out.println();
            int row;

            PreparedStatement stmt2 = null;
            ResultSet rs2 = null;

            try {
                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Menu Choice.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case 'p':
                            System.out.println(
                                    "Input new phone number value: ex:(1234567890)");
                            System.out.println(
                                    "Current phone number: " + rs.getString(2));
                            String pNum = in.readLine();
                            if (pNum.length() == 10) {
                                String sqlP = "UPDATE warehouse SET "
                                        + "phoneNumber = ? WHERE "
                                        + "warehouseId = ?";
                                stmt2 = conn.prepareStatement(sqlP);
                                stmt2.setString(1, pNum);
                                stmt2.setInt(2, wID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed phone "
                                                    + "number of warehouse "
                                                    + wID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating phone number.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid Phone Number Length.");
                            }
                            break;
                        case 's':
                            System.out.println(
                                    "Input new storage capacity value: ex:(15000)");
                            System.out.println("Current storage capacity: "
                                    + rs.getString(3));
                            String storCap = in.readLine();
                            if (isInteger(storCap)) {
                                int storageCapacity = Integer.parseInt(storCap);
                                String sqlS = "UPDATE warehouse SET "
                                        + "storageCapacity = ? WHERE "
                                        + "warehouseId = ?";
                                stmt2 = conn.prepareStatement(sqlS);
                                stmt2.setInt(1, storageCapacity);
                                stmt2.setInt(2, wID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed storage "
                                                    + "capacity of warehouse "
                                                    + wID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating storage capacity.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid input for storage capacity.");
                            }
                            break;
                        case 'c':
                            System.out.println(
                                    "Input new city value: ex:(Boston)");
                            System.out.println(
                                    "Current city: " + rs.getString(4));
                            String city = in.readLine();
                            if (city.length() > 0) {
                                String sqlC = "UPDATE warehouse SET city = ? "
                                        + "WHERE warehouseId = ?";
                                stmt2 = conn.prepareStatement(sqlC);
                                stmt2.setString(1, city);
                                stmt2.setInt(2, wID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed city of warehouse "
                                                    + wID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println("Error updating city.");
                                }
                            } else {
                                System.out.println("Invalid input for city.");
                            }
                            break;
                        case 'a':
                            System.out.println(
                                    "Input new address value: ex:(111 Main Str)");
                            System.out.println(
                                    "Current address: " + rs.getString(5));
                            String addy = in.readLine();
                            if (addy.length() > 0) {
                                String sqlA = "UPDATE warehouse SET address = "
                                        + "? WHERE warehouseId = ?";
                                stmt2 = conn.prepareStatement(sqlA);
                                stmt2.setString(1, addy);
                                stmt2.setInt(2, wID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed address of warehouse "
                                                    + wID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out
                                            .println("Error updating address.");
                                }
                            } else {
                                System.out
                                        .println("Invalid input for address.");
                            }
                            break;
                        case 'm':
                            System.out.println(
                                    "Input new manager name value: ex:(Jane Doe)");
                            System.out.println(
                                    "Current manager name: " + rs.getString(6));
                            String mName = in.readLine();
                            if (mName.length() > 0) {
                                String sqlM = "UPDATE warehouse SET managerName"
                                        + " = ? WHERE warehouseId = ?";
                                stmt2 = conn.prepareStatement(sqlM);
                                stmt2.setString(1, mName);
                                stmt2.setInt(2, wID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed manager "
                                                    + "name of warehouse " + wID
                                                    + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating manager name.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid input for manager name.");
                            }
                            break;
                        case 'd':
                            System.out.println(
                                    "Input new drone capacity value: ex:(500)");
                            System.out.println("Current drone capacity: "
                                    + rs.getString(7));
                            String droneCap = in.readLine();
                            if (isInteger(droneCap)) {
                                int droneCapacity = Integer.parseInt(droneCap);
                                String sqlD = "UPDATE warehouse SET "
                                        + "droneCapacity = ? WHERE warehouseId "
                                        + "= ?";
                                stmt2 = conn.prepareStatement(sqlD);
                                stmt2.setInt(1, droneCapacity);
                                stmt2.setInt(2, wID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed drone "
                                                    + "capacity of warehouse "
                                                    + wID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating drone capacity.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid input for drone capacity.");
                            }
                            break;
                        case 'b':
                            notDone = false;
                            break;
                        default:
                            System.out.println("Invalid choice selection.");
                            break;
                    }
                }
                System.out.println();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (rs2 != null) {
                        rs2.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                try {
                    if (stmt2 != null) {
                        stmt2.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }

    /**
     * Prints the provided warehouse's information to the screen.
     *
     * @param wID
     * @param in
     * @param conn
     */
    private static void showWarehouseInformation(int wID, BufferedReader in,
            Connection conn) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM warehouse WHERE warehouseId = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, wID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                System.out.println("WAREHOUSE #" + wID);
                System.out.println("------------------");
                for (int i = 1; i <= columnCount; i++) {
                    String value = rsmd.getColumnName(i);
                    String columnValue = rs.getString(i);
                    System.out.println(value + ": " + columnValue);
                }
                System.out.println();
                System.out.println("Press enter when done viewing.");
                in.readLine();
                System.out.println();
            } else {
                System.out.println();
                System.out.println("ERROR 404: Warehouse Not Found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Connects to the database if it exists, creates it if it does not, and
     * returns the connection object.
     *
     * @param databaseFileName
     *            the database file name
     * @return a connection object to the designated database
     */
    public static Connection initializeDB(String databaseFileName) {
        String url = "jdbc:sqlite:" + databaseFileName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out
                        .println("The driver name is " + meta.getDriverName());
                System.out.println(
                        "The connection to the database was successful.");
            } else {
                System.out.println("Null Connection");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out
                    .println("There was a problem connecting to the database.");
        }
        return conn;
    }

    /**
     *
     * @param conn
     */
    private static void foreignKeysOn(Connection conn) {

        PreparedStatement stmt = null;

        try {
            String sqlFK = "PRAGMA foreign_keys = on;";
            stmt = conn.prepareStatement(sqlFK);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, SQLException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in));

        Connection conn = initializeDB(mDATABASE);

        foreignKeysOn(conn);

        //Prompts user about warehouse information.
        warehousePrompt(in, conn);
        /*
         * Close input and output streams
         */
        System.out.println("Goodbye!");
        in.close();
    }

}
