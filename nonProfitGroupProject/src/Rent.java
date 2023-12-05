import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Group 8
 *
 */
class Rent {

    /**
     *
     * @param input
     * @return boolean
     */
    private static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Prompts user to select from several different rental options.
     *
     * @param in
     * @param conn
     * @throws IOException
     */
    static void rentMenu(BufferedReader in, Connection conn)
            throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println("Enter the single character to select option:");
            System.out.println("(v) View all current Rentals");
            System.out.println("(a) Add a new Rental");
            System.out.println("(r) Return a Rental");
            System.out.println("(e) Edit Rental Information");
            System.out.println("(d) Delete Rental");
            System.out.println("(s) Search Rentals by userID");
            System.out.println("(i) Search Rentals by start date");
            System.out
                    .println("(n) Search Rentals by equipment's serial number");
            System.out.println("(b) Back");
            String menuChoice = in.readLine();

            if (menuChoice.length() == 0) {
                System.out.println("Invalid Choice Selection.");
            } else {
                switch (menuChoice.charAt(0)) {
                    case 'a':
                        addRentalForUser(in, conn);
                        break;
                    case 'r':
                        returnRentalForUser(in, conn);
                        break;
                    case 'v':
                        viewRentals(conn);
                        break;
                    case 'e':
                        updateRentalForUser(in, conn);
                        break;
                    case 'd':
                        deleteRental(in, conn);
                        break;
                    case 's':
                        searchRentalsByUser(in, conn);
                        break;
                    case 'i':
                        searchRentalsByStartDate(in, conn);
                        break;
                    case 'n':
                        searchRentalsBySerialNumber(in, conn);
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
     *
     * @param conn
     * @throws IOException
     */
    private static void viewRentals(Connection conn) throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        try {
            String sqlRent = "SELECT * FROM Rent;";
            stmt = conn.prepareStatement(sqlRent);
            rs = stmt.executeQuery();
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String value = rsmd.getColumnName(i);
                System.out.print(value);
                if (i < columnCount) {
                    System.out.print(",  ");
                }
            }
            System.out.print("\n");
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue);
                    if (i < columnCount) {
                        System.out.print(",  ");
                    }
                }
                System.out.print("\n");
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
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }

        }
    }

    /**
     *
     * @param in
     * @param conn
     * @throws IOException
     */
    private static void deleteRental(BufferedReader in, Connection conn)
            throws IOException {

        System.out.println("Enter the userID of the Rentee:");
        String uID = in.readLine();
        int userID = 0;
        while (userID == 0) {
            if (isInteger(uID)) {
                userID = Integer.parseInt(uID);
            } else {
                System.out.println("INVALID USER ID.");
                System.out.println();
            }
        }
        PreparedStatement stmt = null;

        System.out
                .println("Enter the starting date of the rental: (YYYY-MM-DD)");
        String startDate = in.readLine();
        boolean dateFormat = true;
        while (dateFormat) {
            if (startDate.length() != 10 || startDate.charAt(4) != '-'
                    || startDate.charAt(7) != '-') {
                System.out.println(
                        "INVALID DATE. Date should be in format (YYYY-MM-DD). Enter a correct date: ");
                startDate = in.readLine();
            } else {
                dateFormat = false;
            }
        }

        int serialNumLength = 6;
        String serialNumber = null;
        while (serialNumber == null) {
            System.out.println(
                    "Enter the serial number of the Rental you wish to delete: ");
            serialNumber = in.readLine();
            if (serialNumber.length() != serialNumLength) {
                System.out.println("INVALID SERIAL NUMBER.");
                System.out.println();
                serialNumber = null;
            }
        }
        try {
            foreignKeysOff(conn);
            String sqlRentalDelete = "DELETE FROM Rent WHERE userID = " + userID
                    + " AND startDate = '" + startDate
                    + "' AND serialNumber = '" + serialNumber + "';";
            stmt = conn.prepareStatement(sqlRentalDelete);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Successfully Deleted Rental.");
            } else {
                System.out.println("ERROR DELETING RENTAL");
            }
            foreignKeysOn(conn);
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

        int invID = getInventoryID(conn, serialNumber);
        inventoryToIn(conn, invID);
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
     *
     * @param conn
     */
    private static void foreignKeysOff(Connection conn) {
        PreparedStatement stmt = null;

        try {
            String sqlFK = "PRAGMA foreign_keys = off;";
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
     *
     * @param conn
     * @param in
     */
    private static void searchRentalsByUser(BufferedReader in, Connection conn)
            throws IOException {
        System.out.println("Enter the userID of the Rentee:");
        String uID = in.readLine();
        int userID = 0;
        while (userID == 0) {
            if (isInteger(uID)) {
                userID = Integer.parseInt(uID);
            } else {
                System.out.println("INVALID USER ID.");
                System.out.println();
            }
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        try {
            String sqlRent = "SELECT * FROM Rent WHERE userId = ?;";
            stmt = conn.prepareStatement(sqlRent);
            stmt.setInt(1, userID);
            rs = stmt.executeQuery();
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String value = rsmd.getColumnName(i);
                System.out.print(value);
                if (i < columnCount) {
                    System.out.print(",  ");
                }
            }
            System.out.print("\n");
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue);
                    if (i < columnCount) {
                        System.out.print(",  ");
                    }
                }
                System.out.print("\n");
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
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }

        }
    }

    /**
     *
     * @param conn
     * @param in
     */
    private static void searchRentalsByStartDate(BufferedReader in,
            Connection conn) throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        System.out.println("Enter today's date: (YYYY-MM-DD)");
        String startDate = in.readLine();
        boolean dateFormat = true;
        while (dateFormat) {
            if (startDate.length() != 10 || startDate.charAt(4) != '-'
                    || startDate.charAt(7) != '-') {
                System.out.println(
                        "Error. Date should be in format (YYYY-MM-DD). Enter a correct date: ");
                startDate = in.readLine();
            } else {
                dateFormat = false;
            }
        }
        try {
            String sqlRent = "SELECT * FROM Rent WHERE startDate = '"
                    + startDate + "';";
            stmt = conn.prepareStatement(sqlRent);
            rs = stmt.executeQuery();
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String value = rsmd.getColumnName(i);
                System.out.print(value);
                if (i < columnCount) {
                    System.out.print(",  ");
                }
            }
            System.out.print("\n");
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue);
                    if (i < columnCount) {
                        System.out.print(",  ");
                    }
                }
                System.out.print("\n");
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
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }

        }
    }

    /**
     *
     * @param conn
     * @param in
     */
    private static void searchRentalsBySerialNumber(BufferedReader in,
            Connection conn) throws IOException {
        System.out.println("Enter the serial number of the Rented equipment:");
        String serialNum = in.readLine();
        int serialNumLen = 6;
        while (serialNum.length() != serialNumLen) {
            System.out.println("INVALID SERIAL NUMBER.");
            System.out.println();
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        try {
            String sqlRent = "SELECT * FROM Rent WHERE serialNumber = ?;";
            stmt = conn.prepareStatement(sqlRent);
            stmt.setString(1, serialNum);
            rs = stmt.executeQuery();
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String value = rsmd.getColumnName(i);
                System.out.print(value);
                if (i < columnCount) {
                    System.out.print(",  ");
                }
            }
            System.out.print("\n");
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue);
                    if (i < columnCount) {
                        System.out.print(",  ");
                    }
                }
                System.out.print("\n");
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
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }

        }
    }

    /**
     *
     * @param conn
     * @param in
     */
    private static void addRentalForUser(BufferedReader in, Connection conn)
            throws IOException {
        PreparedStatement stmt = null;
        PreparedStatement daysRentedCalc = null;
        ResultSet rs = null;

        int userID = 0;
        int daysRented = 0;
        String startDate = null;
        String returnDate = "";
        String serialNumber = null;

        while (userID == 0) {
            System.out.println("Enter the userID of the Rentee:");
            String uID = in.readLine();
            if (isInteger(uID)) {
                userID = Integer.parseInt(uID);
            } else {
                System.out.println("INVALID USER ID.");
                System.out.println();
            }
        }

        int serialNumLength = 6;
        while (serialNumber == null) {
            System.out.println("Enter the serial number of Rented Eqiupment: ");
            serialNumber = in.readLine();
            if (serialNumber.length() != serialNumLength) {
                System.out.println("INVALID SERIAL NUMBER.");
                System.out.println();
                serialNumber = null;
            }
        }
        String droneSerialNumber = null;
        while (droneSerialNumber == null) {
            System.out.println("Enter the drone serial number : ");
            droneSerialNumber = in.readLine();
            if (droneSerialNumber.length() != serialNumLength) {
                System.out.println("INVALID DRONE SERIAL NUMBER.");
                System.out.println();
                droneSerialNumber = null;
            }
        }
        System.out.println("Is this a brand new rental? (y/n)");
        String ans = in.readLine();
        char answer = ans.charAt(0);
        boolean dateFormat = true;
        if (answer == 'n') {
            System.out.println(
                    "Enter the starting date of the rental: (YYYY-MM-DD)");
            startDate = in.readLine();

            while (dateFormat) {
                if (startDate.length() != 10 || startDate.charAt(4) != '-'
                        || startDate.charAt(7) != '-') {
                    System.out.println(
                            "Error. Date should be in format (YYYY-MM-DD). Enter a correct date: ");
                    startDate = in.readLine();
                } else {
                    dateFormat = false;
                }
            }
            System.out.println("Has it been returned?(y/n)");
            ans = in.readLine();
            answer = ans.charAt(0);
            if (answer == 'y') {
                System.out.println("When was it returned?: (YYYY-MM-DD)");
                returnDate = in.readLine();
                dateFormat = true;
                while (dateFormat) {
                    if (startDate.length() != 10 || returnDate.charAt(4) != '-'
                            || returnDate.charAt(7) != '-') {
                        System.out.println(
                                "Error. Date should be in format (YYYY-MM-DD). Enter a correct date: ");
                        returnDate = in.readLine();
                    } else {
                        dateFormat = false;
                    }
                }
                int invID = getInventoryID(conn, serialNumber);
                inventoryToIn(conn, invID);
            } else if (answer == 'n') {
                System.out.println("How many days has it been out for rent?");
                String days = in.readLine();
                daysRented = Integer.parseInt(days);
                int invID = getInventoryID(conn, serialNumber);
                inventoryToOut(conn, invID);
            }
        } else if (answer == 'y') {
            System.out.println("Enter today's date: (YYYY-MM-DD)");
            startDate = in.readLine();
            dateFormat = true;
            while (dateFormat) {
                if (startDate.length() != 10 || startDate.charAt(4) != '-'
                        || startDate.charAt(7) != '-') {
                    System.out.println(
                            "Error. Date should be in format (YYYY-MM-DD). Enter a correct date: ");
                    startDate = in.readLine();
                } else {
                    dateFormat = false;
                }
            }
            addTransport(conn, droneSerialNumber, serialNumber, "delivery");
            int invID = getInventoryID(conn, serialNumber);
            inventoryToOut(conn, invID);
        }

        try {

            String sqlInsertRent = "INSERT INTO rent (userId, startDate, returnDate, daysRented, serialNumber) "
                    + "VALUES (" + userID + ", '" + startDate + "', '"
                    + returnDate + "', " + daysRented + ", '" + serialNumber
                    + "');";
            stmt = conn.prepareStatement(sqlInsertRent);
            int rowChange = stmt.executeUpdate();
            if (rowChange > 0) {
                System.out.println("Success! You managed to add a Rental!");
            } else {
                System.out.println("Rental information unable to be stored.");
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
        try {

            String daysRentedString = "SELECT Cast (("
                    + "JulianDay(returnDate) - JulianDay(startDate)"
                    + ") As Integer) AS days, daysRented FROM RENT WHERE userId = "
                    + userID + " AND startDate = '" + startDate
                    + "' AND serialNumber = '" + serialNumber + "';";
            stmt = conn.prepareStatement(daysRentedString);
            rs = stmt.executeQuery();
            daysRented = rs.getInt(1);
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
        try {
            String daysRentedUpdate = "UPDATE Rent " + "SET daysRented = "
                    + daysRented + " " + "WHERE userId = " + userID
                    + " AND startDate = '" + startDate
                    + "' AND serialNumber = '" + serialNumber + "';";
            daysRentedCalc = conn.prepareStatement(daysRentedUpdate);
            int rows = daysRentedCalc.executeUpdate();
            if (rows < 0) {
                System.out.println("Error inputing days rented.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (daysRentedCalc != null) {
                    daysRentedCalc.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     *
     * @param conn
     * @param in
     */
    private static void updateRentalForUser(BufferedReader in, Connection conn)
            throws IOException {
        int userID = 0;
        String startDate = null;
        String serialNumber = null;

        while (userID == 0) {
            System.out.println("Enter the userID of the Rental to update:");
            String uID = in.readLine();
            if (isInteger(uID)) {
                userID = Integer.parseInt(uID);
            } else {
                System.out.println("INVALID USER ID.");
                System.out.println();
            }
        }

        int serialNumLength = 6;
        while (serialNumber == null) {
            System.out.println("Enter the serial number of Rental to update: ");
            serialNumber = in.readLine();
            if (serialNumber.length() != serialNumLength) {
                System.out.println("INVALID SERIAL NUMBER.");
                System.out.println();
                serialNumber = null;
            }
        }
        boolean dateFormat = true;
        System.out.println(
                "Enter the starting date of the rental to update: (YYYY-MM-DD)");
        startDate = in.readLine();
        while (dateFormat) {
            if (startDate.length() != 10 || startDate.charAt(4) != '-'
                    || startDate.charAt(7) != '-') {
                System.out.println(
                        "Error. Date should be in format (YYYY-MM-DD). Enter a correct date: ");
                startDate = in.readLine();
            } else {
                dateFormat = false;
            }
        }

        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println(
                    "Which attribute would you like to update? Enter single character for choice.");
            System.out.println("(u) User ID");
            System.out.println("(r) Return Date");
            System.out.println("(s) Serial Number");
            System.out.println("(b) Back");
            String menuChoice = in.readLine();
            if (menuChoice.length() == 0) {
                System.out.println("Invalid Choice Selection.");
            } else {
                switch (menuChoice.charAt(0)) {
                    case 'u':
                        userID = updateUserID(in, conn, userID, startDate,
                                serialNumber);
                        break;
                    case 'r':
                        updateReturnDate(in, conn, userID, startDate,
                                serialNumber);
                        break;
                    case 's':
                        serialNumber = updateSerialNumber(in, conn, userID,
                                startDate, serialNumber);
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
     *
     * @param conn
     * @param in
     * @param userID
     * @param startDate
     * @param serialNumber
     */
    private static int updateUserID(BufferedReader in, Connection conn,
            int userID, String startDate, String serialNumber)
            throws IOException {
        PreparedStatement stmt = null;
        int newUserID = 0;
        String uID = null;
        System.out.println("Enter the new user ID: ");
        uID = in.readLine();
        while (newUserID == 0) {
            if (isInteger(uID)) {
                newUserID = Integer.parseInt(uID);
            } else {
                System.out
                        .println("INVALID USER ID. Enter a correct user ID: ");
                uID = in.readLine();
            }
        }
        try {
            String userIDUpdate = "UPDATE Rent " + "SET userId = ? "
                    + "WHERE userId = " + userID + " AND startDate = '"
                    + startDate + "' AND serialNumber = '" + serialNumber
                    + "';";
            stmt = conn.prepareStatement(userIDUpdate);
            stmt.setInt(1, newUserID);
            int rows = stmt.executeUpdate();
            if (rows < 0) {
                System.out.println("Error updating userId.");
            } else {
                System.out.println("Success updating userId!");
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
        return newUserID;
    }

    /**
     *
     * @param conn
     * @param in
     * @param userID
     * @param startDate
     * @param serialNumber
     */
    private static void updateReturnDate(BufferedReader in, Connection conn,
            int userID, String startDate, String serialNumber)
            throws IOException {
        PreparedStatement stmt = null;
        PreparedStatement daysRentedCalc = null;
        ResultSet rs = null;
        int daysRented = 0;
        Date newReturn = new Date(System.currentTimeMillis());
        String newReturnDate = newReturn.toString();
        try {
            String startDateUpdate = "UPDATE Rent " + "SET returnDate = '"
                    + newReturnDate + "' " + "WHERE userId = " + userID
                    + " AND startDate = '" + startDate
                    + "' AND serialNumber = '" + serialNumber + "';";
            stmt = conn.prepareStatement(startDateUpdate);
            int rows = stmt.executeUpdate();
            if (rows < 0) {
                System.out
                        .println("Error updating return date to today's date.");
            } else {
                System.out.println(
                        "Success updating return date to today's date!");
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
        try {

            String daysRentedString = "SELECT Cast (("
                    + "JulianDay(returnDate) - JulianDay(startDate)"
                    + ") As Integer) AS days, daysRented FROM RENT WHERE userId = "
                    + userID + " AND startDate = '" + startDate
                    + "' AND serialNumber = '" + serialNumber + "';";
            stmt = conn.prepareStatement(daysRentedString);
            rs = stmt.executeQuery();
            daysRented = rs.getInt(1);
            if (daysRented < 0) {
                System.out.println(
                        "INVALID RETURN DATE: Return date must be after start date. Enter a valid return date: ");
                newReturnDate = in.readLine();
                try {
                    String startDateUpdate = "UPDATE Rent "
                            + "SET returnDate = '" + newReturnDate + "' "
                            + "WHERE userId = " + userID + " AND startDate = '"
                            + startDate + "' AND serialNumber = '"
                            + serialNumber + "';";
                    stmt = conn.prepareStatement(startDateUpdate);
                    int rows = stmt.executeUpdate();
                    if (rows < 0) {
                        System.out.println("Error updating return date.");
                    } else {
                        System.out.println("Success updating return date!");
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
        try {
            String daysRentedUpdate = "UPDATE Rent " + "SET daysRented = "
                    + daysRented + " " + "WHERE userId = " + userID
                    + " AND startDate = '" + startDate
                    + "' AND serialNumber = '" + serialNumber + "';";
            daysRentedCalc = conn.prepareStatement(daysRentedUpdate);
            int rows = daysRentedCalc.executeUpdate();
            if (rows < 0) {
                System.out.println("Error inputting days rented.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (daysRentedCalc != null) {
                    daysRentedCalc.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     *
     * @param conn
     * @param in
     * @param userID
     * @param startDate
     * @param serialNumber
     */
    private static String updateSerialNumber(BufferedReader in, Connection conn,
            int userID, String startDate, String serialNumber)
            throws IOException {
        PreparedStatement stmt = null;
        int serialNumLen = 6;
        boolean correctSerialNum = false;
        String newSerialNum = null;
        System.out.println("Enter the new serial number: ");
        newSerialNum = in.readLine();
        while (!correctSerialNum) {
            if (newSerialNum.length() != serialNumLen) {
                System.out.println(
                        "INVALID USER SERIAL NUMBER. Enter a correct serial number: ");
                newSerialNum = in.readLine();

            } else {
                correctSerialNum = true;
            }
        }
        try {
            String daysRentedUpdate = "UPDATE Rent " + "SET serialNumber = ? "
                    + "WHERE userId = " + userID + " AND startDate = '"
                    + startDate + "' AND serialNumber = '" + serialNumber
                    + "';";
            stmt = conn.prepareStatement(daysRentedUpdate);
            stmt.setString(1, newSerialNum);
            int rows = stmt.executeUpdate();
            if (rows < 0) {
                System.out.println("Error updating serial number.");
            } else {
                System.out.println("Success updating serial number!");
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
        int invID = getInventoryID(conn, serialNumber);
        inventoryToIn(conn, invID);
        invID = getInventoryID(conn, newSerialNum);
        inventoryToOut(conn, invID);
        return newSerialNum;
    }

    /**
     *
     * @param conn
     * @param in
     */
    private static void returnRentalForUser(BufferedReader in, Connection conn)
            throws IOException {
        PreparedStatement stmt = null;
        PreparedStatement daysRentedCalc = null;
        ResultSet rs = null;
        int daysRented = 0;
        Date newReturn = new Date(System.currentTimeMillis());
        String newReturnDate = newReturn.toString();
        System.out.println("Enter the userID of the Rentee:");
        String uID = in.readLine();
        int userID = 0;
        while (userID == 0) {
            if (isInteger(uID)) {
                userID = Integer.parseInt(uID);
            } else {
                System.out.println("INVALID USER ID.");
                System.out.println();
            }
        }

        System.out
                .println("Enter the starting date of the rental: (YYYY-MM-DD)");
        String startDate = in.readLine();
        boolean dateFormat = true;
        while (dateFormat) {
            if (startDate.length() != 10 || startDate.charAt(4) != '-'
                    || startDate.charAt(7) != '-') {
                System.out.println(
                        "INVALID DATE. Date should be in format (YYYY-MM-DD). Enter a correct date: ");
                startDate = in.readLine();
            } else {
                dateFormat = false;
            }
        }

        int serialNumLength = 6;
        String serialNumber = null;
        while (serialNumber == null) {
            System.out.println(
                    "Enter the serial number of the Rental you wish to return: ");
            serialNumber = in.readLine();
            if (serialNumber.length() != serialNumLength) {
                System.out.println("INVALID SERIAL NUMBER.");
                System.out.println();
                serialNumber = null;
            }
        }
        String droneSerialNumber = null;
        while (droneSerialNumber == null) {
            System.out.println("Enter the drone serial number : ");
            droneSerialNumber = in.readLine();
            if (droneSerialNumber.length() != serialNumLength) {
                System.out.println("INVALID DRONE SERIAL NUMBER.");
                System.out.println();
                droneSerialNumber = null;
            }
        }
        try {

            String updateReturnDate = "UPDATE Rent " + "SET returnDate = '"
                    + newReturnDate + "' WHERE userId = " + userID
                    + " AND startDate = '" + startDate
                    + "' AND serialNumber = '" + serialNumber + "';";
            stmt = conn.prepareStatement(updateReturnDate);
            rs = stmt.executeQuery();

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
        try {

            String daysRentedString = "SELECT Cast (("
                    + "JulianDay(returnDate) - JulianDay(startDate)"
                    + ") As Integer) AS days, daysRented FROM RENT WHERE userId = "
                    + userID + " AND startDate = '" + startDate
                    + "' AND serialNumber = '" + serialNumber + "';";
            stmt = conn.prepareStatement(daysRentedString);
            rs = stmt.executeQuery();
            daysRented = rs.getInt(1);

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
        try {
            String daysRentedUpdate = "UPDATE Rent " + "SET daysRented = "
                    + daysRented + " " + "WHERE userId = " + userID
                    + " AND startDate = '" + startDate
                    + "' AND serialNumber = '" + serialNumber + "';";
            daysRentedCalc = conn.prepareStatement(daysRentedUpdate);
            int rows = daysRentedCalc.executeUpdate();
            if (rows < 0) {
                System.out.println("Error inputting days rented.");
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
                if (daysRentedCalc != null) {
                    daysRentedCalc.close();
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
        addTransport(conn, droneSerialNumber, serialNumber, "return");
        int invID = getInventoryID(conn, serialNumber);
        inventoryToIn(conn, invID);
    }

    /**
     *
     * @param conn
     * @param iId
     * @param modelNumber
     */
    private static void addTransport(Connection conn, String dSerialNum,
            String eSerialNum, String type) {
        PreparedStatement stmt = null;
        Date currDate = new Date(System.currentTimeMillis());
        try {
            String sqlAT = "INSERT INTO Transport VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlAT);
            stmt.setString(1, dSerialNum);
            stmt.setString(2, eSerialNum);
            stmt.setString(3, currDate.toString());
            stmt.setString(4, type);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Successfully added transport.");
                System.out.println();
            } else {
                System.out.println("ERROR ADDING TRANSPORT");
                System.out.println();
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
     *
     * @param conn
     * @param iId
     * @param modelNumber
     */
    private static void inventoryToIn(Connection conn, int iId) {
        PreparedStatement stmt = null;

        try {
            String sqlITI = "UPDATE Inventory SET location = \"In\" WHERE "
                    + "inventoryId = ?;";
            stmt = conn.prepareStatement(sqlITI);
            stmt.setInt(1, iId);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Successfully updated inventory location.");
                System.out.println();
            } else {
                System.out.println("ERROR EDITING INVENTORY");
                System.out.println();
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
     *
     * @param conn
     * @param iId
     * @param modelNumber
     */
    private static void inventoryToOut(Connection conn, int iId) {

        PreparedStatement stmt = null;

        try {

            String sqlITI = "UPDATE Inventory SET location = \"Out\" WHERE "
                    + "inventoryId = ?;";
            stmt = conn.prepareStatement(sqlITI);
            stmt.setInt(1, iId);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Successfully updated inventory location.");
                System.out.println();
            } else {
                System.out.println("ERROR EDITING INVENTORY");
                System.out.println();
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
     *
     * @param conn
     * @param iId
     * @param modelNumber
     */
    private static int getInventoryID(Connection conn, String serialNum) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int iId = 0;

        try {

            String sqlGID = "SELECT Inventory.inventoryId FROM Inventory, Equipment "
                    + "WHERE serialNumber = ? and Inventory.inventoryId = Equipment.inventoryId;";
            stmt = conn.prepareStatement(sqlGID);
            stmt.setString(1, serialNum);
            rs = stmt.executeQuery();
            if (rs.next()) {
                iId = rs.getInt(1);
            } else {
                System.out.println("ERROR EDITING INVENTORY");
                System.out.println();
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
        return iId;

    }

}