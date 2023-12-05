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
class Member {

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

    /**
     * Allows user to navigate different tasks pertaining to members.
     *
     * @param in
     * @param wID
     * @param conn
     */
    static void memberMenu(BufferedReader in, int wID, Connection conn)
            throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println("Enter the single character to select option:");
            System.out.println("(n) New Member");
            System.out.println("(e) Edit Member");
            System.out.println("(d) Delete Member");
            System.out.println("(s) Search Members");
            System.out.println("(r) Reviews");
            System.out.println("(m) Member List");
            System.out.println("(b) Back");
            String menuChoice = in.readLine();
            String userID;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Choice Selection.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case 'n':
                            newMemberSetup(in, wID, conn);
                            break;
                        case 'e':
                            printUserList(conn, wID);
                            System.out.println("Enter UserID:");
                            userID = in.readLine();
                            if (isInteger(userID)) {
                                int uID = Integer.parseInt(userID);
                                String sqlE = "SELECT * FROM member WHERE userId = ?;";
                                stmt = conn.prepareStatement(sqlE);
                                stmt.setInt(1, uID);
                                rs = stmt.executeQuery();
                                if (rs.next()) {
                                    editMember(in, rs, uID, conn);
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "ERROR 404: Member Not Found.");
                                }
                            } else {
                                System.out.println("Invalid UserID.");
                                System.out.println();
                            }
                            break;
                        case 'd':
                            printUserList(conn, wID);
                            System.out.println("Enter UserID:");
                            userID = in.readLine();
                            if (isInteger(userID)) {
                                int uID = Integer.parseInt(userID);
                                deleteMember(uID, conn, wID);
                            } else {
                                System.out.println("Invalid UserID.");
                            }
                            break;
                        case 's':
                            printUserList(conn, wID);
                            System.out.println("Enter UserID:");
                            userID = in.readLine();
                            if (isInteger(userID)) {
                                int uID = Integer.parseInt(userID);
                                showMemberInfo(in, uID, conn, wID);
                            } else {
                                System.out.println("Invalid UserID.");
                            }
                            break;
                        case 'r':
                            printUserList(conn, wID);
                            System.out.println("Enter UserID:");
                            userID = in.readLine();
                            if (isInteger(userID)) {
                                int uID = Integer.parseInt(userID);
                                Reviews_Ratings.reviewsMenu(in, wID, conn, uID);
                            } else {
                                System.out.println("Invalid UserID.");
                            }
                            break;
                        case 'm':
                            String sqlML = "SELECT userId, fName, lName "
                                    + "FROM member "
                                    + "WHERE member.warehouseId = ? "
                                    + "ORDER BY userId;";
                            stmt = conn.prepareStatement(sqlML);
                            stmt.setInt(1, wID);
                            rs = stmt.executeQuery();
                            System.out.println(
                                    "WAREHOUSE #" + wID + " MEMBER LIST");
                            System.out.println("-------------------------");
                            while (rs.next()) {
                                int i = 1;
                                String id = rs.getString(i);
                                i++;
                                String fName = rs.getString(i);
                                i++;
                                String lName = rs.getString(i);
                                System.out.println(
                                        id + " - " + lName + ", " + fName);
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

    /**
     *
     * @param conn
     * @param wID
     */
    private static void printUserList(Connection conn, int wID) {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sqlU = "SELECT userId, lName, fName FROM Member WHERE warehouseId = ?";
            stmt = conn.prepareStatement(sqlU);
            stmt.setInt(1, wID);
            rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            System.out.println("USER LIST FOR WAREHOUSE " + wID);
            System.out.println("---------------------------");
            while (rs.next()) {
                int i;
                for (i = 1; i < colCount; i++) {
                    int uID = rs.getInt(i);
                    i++;
                    String lname = rs.getString(i);
                    i++;
                    String fname = rs.getString(i);
                    System.out.println(uID + " - " + lname + ", " + fname);
                }
            }
            System.out.println();
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
     *
     * @param uID
     * @param conn
     * @param wID
     */
    private static void deleteMember(int uID, Connection conn, int wID) {
        PreparedStatement stmt = null;
        try {
            String sqlD = "DELETE FROM member WHERE userId = ? AND warehouseId = ?";
            stmt = conn.prepareStatement(sqlD);
            stmt.setInt(1, uID);
            stmt.setInt(2, wID);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Successfully Deleted User " + uID + ".");
            } else {
                System.out.println("ERROR DELETING USER");
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
     * Prints the member information of a particular member associated to the
     * warehouse.
     *
     * @param userID
     * @param conn
     * @param wID
     * @param in
     * @throws IOException
     */
    private static void showMemberInfo(BufferedReader in, int userID,
            Connection conn, int wID) throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM member WHERE userId = ? AND warehouseId = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userID);
            stmt.setInt(2, wID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                System.out.println("User #" + userID);
                System.out.println("------------------");
                for (int i = 2; i <= columnCount; i++) {
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
                System.out.println("ERROR 404: Member Not Found.");
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
     * Allows user to input data pertaining to the member of a warehouse.
     *
     * @param in
     * @param wID
     * @param conn
     */
    private static void newMemberSetup(BufferedReader in, int wID,
            Connection conn) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        try {
            String sqlNM = "SELECT max(userId) From Member;";
            stmt = conn.prepareStatement(sqlNM);
            rs = stmt.executeQuery();
            String count = rs.getString(1);
            int userID = 1 + Integer.parseInt(count);
            System.out.println();
            String fName = "";
            boolean invalidName = true;
            while (invalidName) {
                System.out.println("Enter Member First Name: (ex: Karen)");
                fName = in.readLine();
                if (fName.length() == 0) {
                    System.out.println("Invalid name length.  Try again.");
                } else {
                    invalidName = false;
                }
            }
            String lName = "";
            invalidName = true;
            while (invalidName) {
                System.out.println("Enter Member Last Name: (ex: Smith)");
                lName = in.readLine();
                if (lName.length() == 0) {
                    System.out.println("Invalid name length.  Try again.");
                } else {
                    invalidName = false;
                }
            }
            boolean invalidAddress = true;
            String address = "";
            while (invalidAddress) {
                System.out.println("Enter Member Address: (ex: 101 Main Str)");
                address = in.readLine();
                if (address.length() == 0) {
                    System.out.println("Invalid address. Try again.");
                } else {
                    invalidAddress = false;
                }
            }
            String phoneNum = "";
            boolean invalidPNum = true;
            while (invalidPNum) {
                System.out
                        .println("Enter Member Phone Number: (ex: 1234567890)");
                phoneNum = in.readLine();
                if (phoneNum.length() != 10) {
                    System.out.println(
                            "Invalid phone number length.  Try again.");
                } else {
                    invalidPNum = false;
                }
            }
            Date currDate = new Date(System.currentTimeMillis());
            boolean invalidEmail = true;
            String email = "";
            while (invalidEmail) {
                System.out.println("Enter Member Email: (ex: adele@gmail.com)");
                email = in.readLine();
                if (email.length() > 0) {
                    invalidEmail = false;
                } else {
                    System.out.println("Invalid email name. Try again.");
                }
            }
            boolean invalidDist = true;
            double distanceToWarehouse = 0.0;
            String dist = "";
            while (invalidDist) {
                System.out.println("Enter Member Distance to Warehouse: (3.0)");
                dist = in.readLine();
                if (isDouble(dist)) {
                    distanceToWarehouse = Double.parseDouble(dist);
                    invalidDist = false;
                } else {
                    System.out.println(
                            "Invalid distance measurement. Try again.");
                }
            }

            String sql2 = "INSERT INTO member (userId, fName, "
                    + "lName, address, phoneNumber, startDate, balance, "
                    + "amountPaid, email, distanceToWarehouse, warehouseId) "
                    + "VALUES ('" + userID + "', '" + fName + "', '" + lName
                    + "', '" + address + "', '" + phoneNum + "', '" + currDate
                    + "', '" + email + "', '" + distanceToWarehouse + "', '"
                    + wID + "');";
            stmt2 = conn.prepareStatement(sql2);
            int rowChange = stmt2.executeUpdate();
            if (rowChange > 0) {
                System.out.println("Success! You managed to add a member!");
            } else {
                System.out.println("Member information unable to be stored.");
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
     * Allows user to edit any of the attributes associated with a member.
     *
     * @param in
     * @param rs
     * @param userID
     * @param conn
     */
    private static void editMember(BufferedReader in, ResultSet rs, int userID,
            Connection conn) throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println(
                    "Enter the single character to select edit category:");
            System.out.println("(f) First Name");
            System.out.println("(l) Last Name");
            System.out.println("(a) Address");
            System.out.println("(p) Phone Number");
            System.out.println("(e) Email");
            System.out.println("(d) Warehouse Distance");
            System.out.println("(w) Warehouse ID");
            System.out.println("(q) Back");
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
                        case 'f':
                            System.out
                                    .println("Input new first name: ex:(John)");
                            System.out.println(
                                    "Current first name: " + rs.getString(2));
                            String fName = in.readLine();
                            if (fName.length() != 0) {
                                String sqlF = "UPDATE Member SET fName "
                                        + "= ? WHERE userId = ?";
                                stmt2 = conn.prepareStatement(sqlF);
                                stmt2.setString(1, fName);
                                stmt2.setInt(2, userID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed first name of user "
                                                    + userID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating first name.");
                                }
                            } else {
                                System.out
                                        .println("Invalid First Name Length.");
                            }
                            break;
                        case 'l':
                            System.out
                                    .println("Input new last name: ex:(Smith)");
                            System.out.println(
                                    "Current first name: " + rs.getString(3));
                            String lName = in.readLine();
                            if (lName.length() != 0) {
                                String sqlL = "UPDATE Member SET lName = ? "
                                        + "WHERE userId = ?";
                                stmt2 = conn.prepareStatement(sqlL);
                                stmt2.setString(1, lName);
                                stmt2.setInt(2, userID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed last name of user "
                                                    + userID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating last name.");
                                }
                            } else {
                                System.out.println("Invalid Last Name Length.");
                            }
                            break;
                        case 'a':
                            System.out.println(
                                    "Input new address: ex:(111 Main Str)");
                            System.out.println(
                                    "Current address: " + rs.getString(4));
                            String addy = in.readLine();
                            if (addy.length() != 0) {
                                String sqlA = "UPDATE Member SET address = ? "
                                        + "WHERE userId = ?";
                                stmt2 = conn.prepareStatement(sqlA);
                                stmt2.setString(1, addy);
                                stmt2.setInt(2, userID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed address of user "
                                                    + userID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out
                                            .println("Error updating address.");
                                }
                            } else {
                                System.out.println("Invalid Address Length.");
                            }
                            break;
                        case 'p':
                            System.out.println(
                                    "Input new phone number: ex:(0123456789)");
                            System.out.println(
                                    "Current address: " + rs.getString(6));
                            String phoneNum = in.readLine();
                            if (phoneNum.length() == 10) {
                                String sqlP = "UPDATE Member SET phoneNumber "
                                        + "= ? WHERE userId = ?";
                                stmt2 = conn.prepareStatement(sqlP);
                                stmt2.setString(1, phoneNum);
                                stmt2.setInt(2, userID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed phone number of user "
                                                    + userID + ".");
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
                        case 'b':
                            System.out.println(
                                    "Input new member balance: ex:(100)");
                            System.out.println(
                                    "Current balance: " + rs.getString(7));
                            String bal = in.readLine();
                            if (isInteger(bal)) {
                                int balance = Integer.parseInt(bal);
                                String sqlB = "UPDATE warehouse SET "
                                        + "balance = ? WHERE userId " + "= ?";
                                stmt2 = conn.prepareStatement(sqlB);
                                stmt2.setInt(1, balance);
                                stmt2.setInt(2, userID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed balance of user "
                                                    + userID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out
                                            .println("Error updating balance.");
                                }
                            } else {
                                System.out
                                        .println("Invalid input for balance.");
                            }
                            break;
                        case 'm':
                            System.out.println(
                                    "Input new member amount paid: ex:(100)");
                            System.out.println(
                                    "Current amount paid: " + rs.getString(8));
                            String amPaid = in.readLine();
                            if (isInteger(amPaid)) {
                                int amountPaid = Integer.parseInt(amPaid);
                                String sqlAP = "UPDATE warehouse SET "
                                        + "amountPaid = ? WHERE userId "
                                        + "= ?";
                                stmt2 = conn.prepareStatement(sqlAP);
                                stmt2.setInt(1, amountPaid);
                                stmt2.setInt(2, userID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed amount paid of user "
                                                    + userID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating amount paid.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid input for amount paid.");
                            }
                            break;
                        case 'e':
                            System.out.println(
                                    "Input new email: ex:(adele@gmail.com)");
                            System.out.println(
                                    "Current address: " + rs.getString(9));
                            String email = in.readLine();
                            if (email.length() != 0) {
                                String sqlE = "UPDATE Member SET address = ? "
                                        + "WHERE userId = ?";
                                stmt2 = conn.prepareStatement(sqlE);
                                stmt2.setString(1, email);
                                stmt2.setInt(2, userID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed email of user "
                                                    + userID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println("Error updating email.");
                                }
                                notDone = false;
                            } else {
                                System.out.println("Invalid Email Length.");
                            }
                            break;
                        case 'd':
                            System.out.println(
                                    "Input new member distance to warehouse: ex:(1.1)");
                            System.out.println("Current distance to warehouse: "
                                    + rs.getString(10));
                            String dist = in.readLine();
                            if (isDouble(dist)) {
                                double distToWarehouse = Double
                                        .parseDouble(dist);
                                String sqlB = "UPDATE member SET "
                                        + "distanceToWarehouse = ? WHERE userId "
                                        + "= ?";
                                stmt2 = conn.prepareStatement(sqlB);
                                stmt2.setDouble(1, distToWarehouse);
                                stmt2.setInt(2, userID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed distance to "
                                                    + "warehouse of user "
                                                    + userID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating distance to warehouse.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid input for distance to warehouse.");
                            }
                            break;
                        case 'w':
                            System.out.println(
                                    "Input new member's warehouse ID: ex:(1)");
                            System.out.println("Current warehouse ID: "
                                    + rs.getString(11));
                            String wareID = in.readLine();
                            if (isInteger(wareID)) {
                                int warehouseID = Integer.parseInt(wareID);
                                String sqlB = "UPDATE member SET "
                                        + "warehouseId = ? WHERE userId "
                                        + "= ?";
                                stmt2 = conn.prepareStatement(sqlB);
                                stmt2.setInt(1, warehouseID);
                                stmt2.setInt(2, userID);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed warehouseID of user "
                                                    + userID + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating warehouseID.");
                                }
                            } else {
                                System.out
                                        .println("Invalid input for balance.");
                            }
                            break;
                        case 'q':
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
            }
            System.out.println();
        }
    }

}