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
public class Repair {

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
     *
     * @param input
     * @return boolean
     */
    private static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static void repairMenu(BufferedReader in, Connection conn)
            throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println("Enter the single character to select option:");
            System.out.println("(n) New Repair");
            System.out.println("(e) Edit Repair");
            System.out.println("(d) Delete Repair");
            System.out.println("(s) Search Repair");
            System.out.println("(l) Repair List");
            System.out.println("(b) Back");
            String menuChoice = in.readLine();
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Choice Selection.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case 'n':
                            newRepair(in, conn);
                            break;
                        case 'e':
                            System.out.println("Enter company name:");
                            String cName = in.readLine();
                            System.out.println("Enter inventory Id:");
                            int invId = Integer.parseInt(in.readLine());
                            System.out.println(
                                    "Enter start date (ex: 2077-01-22):");
                            String date = in.readLine();
                            String sqlE = "SELECT * FROM Repair WHERE cName = ? AND "
                                    + " inventoryId = ? AND startDate = ?;";
                            stmt = conn.prepareStatement(sqlE);
                            stmt.setString(1, cName);
                            stmt.setInt(2, invId);
                            stmt.setString(3, date);
                            rs = stmt.executeQuery();
                            if (rs.next()) {
                                editRepair(in, rs, cName, invId, date, conn);
                            } else {
                                System.out.println();
                                System.out.println(
                                        "ERROR 404: Repair Not Found.");
                            }
                            break;
                        case 'd':
                            System.out.println("Enter company name:");
                            cName = in.readLine();
                            System.out.println("Enter inventory Id:");
                            invId = Integer.parseInt(in.readLine());
                            System.out.println(
                                    "Enter start date (ex: 2077-01-22):");
                            date = in.readLine();

                            deleteRepair(cName, invId, date, conn);

                            break;
                        case 's':
                            System.out.println("Enter company name:");
                            cName = in.readLine();
                            System.out.println("Enter inventory Id:");
                            invId = Integer.parseInt(in.readLine());
                            System.out.println(
                                    "Enter start date (ex: 2077-01-22):");
                            date = in.readLine();
                            searchRepair(in, cName, invId, date, conn);
                            break;
                        case 'l':
                            String sqlML = "SELECT * FROM REPAIR;";
                            stmt = conn.prepareStatement(sqlML);
                            rs = stmt.executeQuery();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int columnCount = rsmd.getColumnCount();
                            System.out.println("REPAIR LIST");
                            System.out.println("-------------------------");
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
                            System.out.println();
                            System.out
                                    .println("Press enter when done viewing.");
                            in.readLine();
                            System.out.println();
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

    private static void newRepair(BufferedReader in, Connection conn)
            throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rs3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        try {
            String sqlC = "SELECT companyName From SERVICE_REPAIRER WHERE companyName = ?;";
            stmt = conn.prepareStatement(sqlC);
            System.out.println();
            String cName = "";
            boolean invalidCName = true;
            while (invalidCName) {
                System.out.println(
                        "Enter Existing Company Name (ex: Hane LLC): ");
                cName = in.readLine();
                stmt.setString(1, cName);
                rs = stmt.executeQuery();
                if (cName.length() == 0) {
                    System.out.println("Invalid city length.  Try again.");

                } else if (!rs.next()) {
                    System.out.println(
                            "Company name does not exist. Please try again or "
                                    + "make new service repairer.");
                } else {
                    invalidCName = false;
                }
            }
            int invId = 0;
            boolean invalidinvId = true;
            String sqlI = "SELECT inventoryId From Inventory WHERE inventoryId = ?;";
            stmt = conn.prepareStatement(sqlI);

            while (invalidinvId) {
                System.out.println("Enter InventoryId (ex:1): ");
                String input = in.readLine();
                if (isInteger(input)) {
                    invId = Integer.parseInt(input);
                    stmt.setInt(1, invId);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        invalidinvId = false;
                        String sqlD = "SELECT status, location "
                                + "FROM DRONES, Inventory "
                                + "WHERE drones.inventoryId = inventory.inventoryId AND drones.inventoryId = ?;";
                        stmt3 = conn.prepareStatement(sqlD);
                        stmt3.setInt(1, invId);
                        rs3 = stmt3.executeQuery();
                        if (rs3.next()) {
                            String sqlU = "UPDATE DRONES "
                                    + "SET Status = 'Inactive' "
                                    + "WHERE inventoryid = ?;";
                            stmt4 = conn.prepareStatement(sqlU);
                            stmt4.setInt(1, invId);
                            stmt4.executeUpdate();
                            String sqlU2 = "UPDATE INVENTORY "
                                    + "SET LOCATION = 'Out' "
                                    + "WHERE inventoryid = ?;";
                            stmt5 = conn.prepareStatement(sqlU2);
                            stmt5.setInt(1, invId);
                            stmt5.executeUpdate();
                        }

                    } else {
                        System.out.println(
                                "Inventory id does not exist. Try again.");
                    }
                } else {
                    System.out.println("Invalid inventory id.  Try again.");
                }
            }

            int year = 1900;
            int month = 1;
            int day = 1;
            boolean invalidSDate = true;
            String date = "";
            while (invalidSDate) {
                System.out.println("Enter Start Date: (ex: 2077-01-22)");
                date = in.readLine();
                if (isInteger(date.substring(0, 4))
                        && isInteger(date.substring(5, 7))
                        && isInteger(date.substring(8, 10))) {
                    year = Integer.parseInt(date.substring(0, 4));
                    month = Integer.parseInt(date.substring(5, 7));
                    day = Integer.parseInt(date.substring(8, 10));
                    if (year > 1900 && month >= 1 && month <= 12 && day >= 1
                            && day <= 31) {
                        year = year - 1900;
                        month = month - 1;
                        invalidSDate = false;
                    } else {
                        System.out.println("Invalid date entered. Try again.");
                    }
                } else {
                    System.out.println("Invalid date structure. Try again.");
                }
            }
            Date sDate = new Date(year, month, day);

            year = 1900;
            month = 1;
            day = 1;
            boolean invalidExDate = true;
            date = "";
            while (invalidExDate) {
                System.out.println(
                        "Enter Expected Return Date: (ex: 2077-01-22)");
                date = in.readLine();
                if (isInteger(date.substring(0, 4))
                        && isInteger(date.substring(5, 7))
                        && isInteger(date.substring(8, 10))) {
                    year = Integer.parseInt(date.substring(0, 4));
                    month = Integer.parseInt(date.substring(5, 7));
                    day = Integer.parseInt(date.substring(8, 10));
                    if (year > 1900 && month >= 1 && month <= 12 && day >= 1
                            && day <= 31) {
                        year = year - 1900;
                        month = month - 1;
                        invalidExDate = false;
                    } else {
                        System.out.println("Invalid date entered. Try again.");
                    }
                } else {
                    System.out.println("Invalid date structure. Try again.");
                }
            }
            Date exDate = new Date(year, month, day);

            Date rDate = null;

            String sql2 = "INSERT INTO REPAIR (cName, inventoryId, "
                    + "cost, startDate, expRetDate, retDate) " + "VALUES ('"
                    + cName + "', '" + invId + "', '" + "NULL" + "', '" + sDate
                    + "', '" + exDate + "', '" + rDate + "');";
            stmt2 = conn.prepareStatement(sql2);
            int rowChange = stmt2.executeUpdate();

            if (rowChange > 0) {
                System.out.println("Success! You managed to add a repair!");
            } else {
                System.out.println("Repair information unable to be stored.");
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
            try {
                if (stmt3 != null) {
                    stmt3.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (rs3 != null) {
                    rs3.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (stmt4 != null) {
                    stmt4.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (stmt5 != null) {
                    stmt5.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    private static void editRepair(BufferedReader in, ResultSet rs,
            String cName, int invId, String sDate, Connection conn)
            throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println(
                    "Enter the single character to select edit category:");
            System.out.println("(n) Company Name");
            System.out.println("(i) Inventory Id");
            System.out.println("(c) Cost");
            System.out.println("(s) Start Date");
            System.out.println("(e) Expected Return Date");
            System.out.println("(r) Return Date");
            System.out.println("(b) Back");
            String menuChoice = in.readLine();
            System.out.println();
            int row;

            PreparedStatement stmt2 = null;
            ResultSet rs2 = null;
            PreparedStatement stmt3 = null;
            try {
                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Menu Choice.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case 'n':
                            System.out.println(
                                    "Input new company name: ex:(Hale LLC)");
                            System.out.println(
                                    "Current company Name: " + rs.getString(1));
                            String newCName = in.readLine();
                            if (newCName.length() != 0) {
                                String sqlA = "UPDATE REPAIR SET cname = ? WHERE cName = ? AND "
                                        + " inventoryId = ? AND startDate = ?;";
                                stmt2 = conn.prepareStatement(sqlA);
                                stmt2.setString(1, newCName);
                                stmt2.setString(2, cName);
                                stmt2.setInt(3, invId);
                                stmt2.setString(4, sDate);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed company name.");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println("Error company name.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid Company Name Length.");
                            }
                            break;
                        case 'i':
                            System.out
                                    .println("Input new inventory id: ex:(3)");
                            System.out.println(
                                    "Current inventory id: " + rs.getString(2));
                            String input = in.readLine();
                            if (isInteger(input)) {
                                int newInvId = Integer.parseInt(input);
                                String sqlA = "UPDATE REPAIR SET inventoryId = ? WHERE cName = ? AND "
                                        + " inventoryId = ? AND startDate = ?;";
                                stmt2 = conn.prepareStatement(sqlA);
                                stmt2.setInt(1, newInvId);
                                stmt2.setString(2, cName);
                                stmt2.setInt(3, invId);
                                stmt2.setString(4, sDate);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed inventory id.");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating inventory id.");
                                }
                            } else {
                                System.out.println("Invalid inventory id.");
                            }
                            break;
                        case 'c':
                            System.out.println("Input new cost id: ex:(5.00)");
                            System.out.println(
                                    "Current cost: " + rs.getString(3));
                            input = in.readLine();
                            if (isDouble(input)) {
                                double newCost = Double.parseDouble(input);
                                String sqlA = "UPDATE REPAIR SET cost = ? WHERE cName = ? AND "
                                        + " inventoryId = ? AND startDate = ?;";
                                stmt2 = conn.prepareStatement(sqlA);
                                stmt2.setDouble(1, newCost);
                                stmt2.setString(2, cName);
                                stmt2.setInt(3, invId);
                                stmt2.setString(4, sDate);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed cost.");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println("Error updating cost.");
                                }
                            } else {
                                System.out.println("Invalid cost.");
                            }
                            break;
                        case 's':
                            System.out.println(
                                    "Input new repair start date: ex:(2077-01-22)");
                            System.out.println(
                                    "Current start date: " + rs.getString(4));
                            String start = in.readLine();
                            if (isInteger(start.substring(0, 4))
                                    && isInteger(start.substring(5, 7))
                                    && isInteger(start.substring(8, 10))) {
                                String sqlA = "UPDATE REPAIR SET startDate = ? WHERE cName = ? AND "
                                        + " inventoryId = ? AND startDate = ?;";
                                stmt2 = conn.prepareStatement(sqlA);
                                stmt2.setString(1, start);
                                stmt2.setString(2, cName);
                                stmt2.setInt(3, invId);
                                stmt2.setString(4, sDate);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed start date of reapir.");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating start date.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid date structure. Try again.");
                            }
                            break;
                        case 'e':
                            System.out.println(
                                    "Input new repair expected return date: ex:(2077-01-22)");
                            System.out.println("Current expected return date: "
                                    + rs.getString(5));
                            String ex = in.readLine();
                            if (isInteger(ex.substring(0, 4))
                                    && isInteger(ex.substring(5, 7))
                                    && isInteger(ex.substring(8, 10))) {
                                String sqlA = "UPDATE REPAIR SET expRetDate = ? WHERE cName = ? AND "
                                        + " inventoryId = ? AND startDate = ?;";
                                stmt2 = conn.prepareStatement(sqlA);
                                stmt2.setString(1, ex);
                                stmt2.setString(2, cName);
                                stmt2.setInt(3, invId);
                                stmt2.setString(4, sDate);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed start date of reapir.");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating expected return date.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid date structure. Try again.");
                            }
                            break;
                        case 'r':
                            System.out.println(
                                    "Input new repair return date: ex:(2077-01-22)");
                            System.out.println(
                                    "Current return date: " + rs.getString(6));
                            String ret = in.readLine();
                            if (isInteger(ret.substring(0, 4))
                                    && isInteger(ret.substring(5, 7))
                                    && isInteger(ret.substring(8, 10))) {
                                String sqlA = "UPDATE REPAIR SET retDate = ? WHERE cName = ? AND "
                                        + " inventoryId = ? AND startDate = ?;";
                                stmt2 = conn.prepareStatement(sqlA);
                                stmt2.setString(1, ret);
                                stmt2.setString(2, cName);
                                stmt2.setInt(3, invId);
                                stmt2.setString(4, sDate);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed return date of reapir.");
                                    String sqlU = "UPDATE DRONES "
                                            + "SET Status = 'Inactive' "
                                            + "WHERE inventoryid = ?;";
                                    stmt3 = conn.prepareStatement(sqlU);
                                    stmt3.setInt(1, invId);
                                    stmt3.executeUpdate();
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating return date.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid date structure. Try again.");
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
                try {
                    if (stmt3 != null) {
                        stmt3.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            System.out.println();
        }
    }

    private static void deleteRepair(String cName, int invId, String sDate,
            Connection conn) {
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
            String sql = "DELETE FROM Repair WHERE cName = ? AND "
                    + " inventoryId = ? AND startDate = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cName);
            stmt.setInt(2, invId);
            stmt.setString(3, sDate);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Successfully deleted repair from " + cName
                        + " with inventory Id: " + invId + ".");
                String sqlU = "UPDATE DRONES " + "SET Status = 'Active' "
                        + "WHERE inventoryid = ?;";
                stmt2 = conn.prepareStatement(sqlU);
                stmt2.setInt(1, invId);
                stmt2.executeUpdate();
            } else {
                System.out.println("ERROR DELETING REPAIR");
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

    private static void searchRepair(BufferedReader in, String cName, int invId,
            String sDate, Connection conn) throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Repair WHERE cName = ? AND "
                    + " inventoryId = ? AND startDate = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cName);
            stmt.setInt(2, invId);
            stmt.setString(3, sDate);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                System.out.println("Company Name: " + cName);
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
                System.out.println("ERROR 404: Repair Not Found.");
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

}
