import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

/**
 *
 * @author Group 8
 *
 */
class Reviews_Ratings {

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
     * Allows user to navigate different tasks pertaining to members.
     *
     * @param in
     * @param wID
     * @param userID
     * @param conn
     */
    static void reviewsMenu(BufferedReader in, int wID, Connection conn,
            int uID) throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println("Enter the single character to select option:");
            System.out.println("(n) New Review");
            System.out.println("(e) Edit Review");
            System.out.println("(d) Delete Review");
            System.out.println("(s) Search Reviews");
            System.out.println("(b) Back");
            String menuChoice = in.readLine();
            if (menuChoice.length() == 0) {
                System.out.println("Invalid Choice Selection.");
            } else {
                switch (menuChoice.charAt(0)) {
                    case 'n':
                        newReviewSetup(in, wID, conn, uID);
                        break;
                    case 'e':
                        editReviewSetup(in, uID, conn);
                        break;
                    case 'd':
                        deleteReviewSetup(in, uID, conn);
                        break;
                    case 's':
                        searchReviewSetup(in, uID, conn);
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
            System.out.println();
        }
    }

    /**
     *
     * @param in
     * @param userID
     * @param conn
     * @throws IOException
     */
    private static void searchReviewSetup(BufferedReader in, int userID,
            Connection conn) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlERS = "SELECT serialNumber, description "
                    + "FROM Ordered_Item, Add_To, Inventory, Equipment "
                    + "WHERE ordered_item.orderNumber=add_to.orderNumber "
                    + "AND add_to.inventoryId=inventory.inventoryId "
                    + "AND inventory.inventoryId=equipment.inventoryId "
                    + "AND equipment.serialNumber IN  (SELECT serialNumber "
                    + "FROM Rent WHERE userId = ? AND serialNumber "
                    + "IN (SELECT serialNumber FROM Reviews_Ratings "
                    + "WHERE userId = ?));";
            stmt = conn.prepareStatement(sqlERS);
            stmt.setInt(1, userID);
            stmt.setInt(2, userID);
            rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int col = rsmd.getColumnCount();
            if (rs.next()) {
                System.out.println("Review(s) Available to View");
                System.out.println("---------------------------");
                System.out.println(rs.getString(1) + " - " + rs.getString(2));
                while (rs.next()) {
                    int i;
                    for (i = 1; i < col; i++) {
                        String serNum = rs.getString(i);
                        i++;
                        String desc = rs.getString(i);
                        System.out.println(serNum + " - " + desc);
                    }
                }
                System.out.println();
                String serialNum = "";
                while (serialNum.length() != 6) {
                    System.out.println("Enter the associated serial number of "
                            + "review to view:");
                    serialNum = in.readLine();
                    if (serialNum.length() != 6) {
                        System.out.println("Invalid serial number length.");
                        System.out.println();
                    }
                }
                showReview(in, conn, userID, serialNum);
            } else {
                System.out.println("No reviews available to search.");
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
     *
     * @param in
     * @param conn
     * @param userID
     * @param serialNum
     * @throws IOException
     */
    private static void showReview(BufferedReader in, Connection conn,
            int userID, String serialNum) throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT dateCreated, timeCreated, rating, review "
                    + "FROM Reviews_Ratings WHERE userId = ? AND serialNumber = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userID);
            stmt.setString(2, serialNum);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                System.out
                        .println("User #" + userID + " Review of " + serialNum);
                System.out.println("--------------------------");
                System.out.println(
                        rsmd.getColumnName(1) + ": " + rs.getString(1));
                System.out.println(
                        rsmd.getColumnName(2) + ": " + rs.getString(2));
                System.out.println(rsmd.getColumnName(3) + ": " + rs.getInt(3));
                System.out.println(
                        rsmd.getColumnName(4) + ": " + rs.getString(4));
                System.out.println();
                System.out.println("Press enter when done viewing.");
                in.readLine();
                System.out.println();
            } else {
                System.out.println();
                System.out.println("ERROR 404: Review Not Found.");
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
     *
     * @param in
     * @param userID
     * @param conn
     * @throws IOException
     */
    private static void deleteReviewSetup(BufferedReader in, int userID,
            Connection conn) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlERS = "SELECT serialNumber, description "
                    + "FROM Ordered_Item, Add_To, Inventory, Equipment "
                    + "WHERE ordered_item.orderNumber=add_to.orderNumber "
                    + "AND add_to.inventoryId=inventory.inventoryId "
                    + "AND inventory.inventoryId=equipment.inventoryId "
                    + "AND equipment.serialNumber IN  (SELECT serialNumber "
                    + "FROM Rent WHERE userId = ? AND serialNumber "
                    + "IN (SELECT serialNumber FROM Reviews_Ratings "
                    + "WHERE userId = ?));";
            stmt = conn.prepareStatement(sqlERS);
            stmt.setInt(1, userID);
            stmt.setInt(2, userID);
            rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int col = rsmd.getColumnCount();
            if (rs.next()) {
                System.out.println("Review(s) Available to Delete");
                System.out.println("-----------------------------");
                System.out.println(rs.getString(1) + " - " + rs.getString(2));
                while (rs.next()) {
                    int i;
                    for (i = 1; i < col; i++) {
                        String serNum = rs.getString(i);
                        i++;
                        String desc = rs.getString(i);
                        System.out.println(serNum + " - " + desc);
                    }
                }
                System.out.println();
                String serialNum = "";
                while (serialNum.length() != 6) {
                    System.out.println("Enter the associated serial number of "
                            + "review to delete:");
                    serialNum = in.readLine();
                    if (serialNum.length() != 6) {
                        System.out.println("Invalid serial number length.");
                        System.out.println();
                    }
                }
                int rating = 0;
                String review = "";
                getPreviousDT(conn, userID, serialNum, rating, review);
            } else {
                System.out.println("No reviews available to delete.");
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
    private static void newReviewSetup(BufferedReader in, int wID,
            Connection conn, int userID) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            String sqlRS = "SELECT serialNumber, description "
                    + "FROM Ordered_Item, Add_To, Inventory, Equipment "
                    + "WHERE ordered_item.orderNumber=add_to.orderNumber "
                    + "AND add_to.inventoryId=inventory.inventoryId "
                    + "AND inventory.inventoryId=equipment.inventoryId "
                    + "AND equipment.serialNumber IN  "
                    + "(SELECT serialNumber FROM Rent WHERE userId = ? "
                    + "AND serialNumber NOT IN "
                    + "(SELECT serialNumber FROM Reviews_Ratings "
                    + "WHERE userId = ?));";
            stmt = conn.prepareStatement(sqlRS);
            stmt.setInt(1, userID);
            stmt.setInt(2, userID);
            rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int col = rsmd.getColumnCount();
            if (rs.next()) {
                System.out.println("Equipment Item(s) Available to Review");
                System.out.println("-------------------------------------");
                System.out.println(rs.getString(1) + " - " + rs.getString(2));
                while (rs.next()) {
                    int i;
                    for (i = 1; i < col; i++) {
                        String serNum = rs.getString(i);
                        i++;
                        String desc = rs.getString(i);
                        System.out.println(serNum + " - " + desc);
                    }
                }
                String serialNum = "";
                while (serialNum.length() != 6) {
                    System.out.println("Enter the associated serial number of "
                            + "the item to review:");
                    serialNum = in.readLine();
                    if (serialNum.length() != 6) {
                        System.out.println("Invalid serial number length.");
                        System.out.println();
                    }
                }
                generateNewReview(in, conn, userID, serialNum);
            } else {
                System.out.println("No items to create new review.");
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
     *
     * @param in
     * @param conn
     * @param userID
     * @param serialNum
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    private static void generateNewReview(BufferedReader in, Connection conn,
            int userID, String serialNum) throws IOException {

        Date dateCreated = new Date(System.currentTimeMillis());
        LocalTime timCreated = LocalTime.now();
        int hour = timCreated.getHour();
        int minute = timCreated.getMinute();
        int sec = timCreated.getSecond();
        Time timeCreated = new Time(hour, minute, sec);

        int rating = 0;
        boolean invalidRat = true;
        while (invalidRat) {
            System.out.println("Enter rating of " + serialNum + " from 1-5:");
            String rat = in.readLine();
            if (isInteger(rat)) {
                rating = Integer.parseInt(rat);
                if (rating >= 1 && rating <= 5) {
                    invalidRat = false;
                } else {
                    System.out.println("Error rating must be between 1-5.");
                }
            } else {
                System.out.println("Invalid rating input.");
                System.out.println();
            }

        }
        String review = "";
        while (review.length() == 0) {
            System.out.println("Enter review of " + serialNum + ":");
            review = in.readLine();
            if (review.length() == 0) {
                System.out.println("Invalid review length.");
            }
        }

        insertRevTransaction(conn, userID, serialNum, dateCreated, timeCreated,
                rating, review);

    }

    /**
     *
     * @param conn
     * @param userID
     * @param serialNum
     * @param dateCreated
     * @param timeCreated
     * @param rating
     * @param review
     */
    private static void insertRevTransaction(Connection conn, int userID,
            String serialNum, Date dateCreated, Time timeCreated, int rating,
            String review) {

        String sqlRR = "INSERT INTO Reviews_Ratings VALUES (?, ?, ?, ?, ?, ?)";

        String sqlM = "INSERT INTO Make VALUES (? ,? , ?)";

        ResultSet rs = null;
        PreparedStatement stmt = null, stmt2 = null;

        try {

            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(sqlRR);
            stmt.setString(1, dateCreated.toString());
            stmt.setString(2, timeCreated.toString());
            stmt.setInt(3, rating);
            stmt.setString(4, review);
            stmt.setInt(5, userID);
            stmt.setString(6, serialNum);
            int row = stmt.executeUpdate();

            if (row != 1) {
                conn.rollback();
            }

            stmt2 = conn.prepareStatement(sqlM);
            stmt2.setInt(1, userID);
            stmt2.setString(2, dateCreated.toString());
            stmt2.setString(3, timeCreated.toString());

            row = stmt2.executeUpdate();
            if (row != 1) {
                conn.rollback();
            } else {
                System.out.println("Successfully added review.");
            }

            conn.commit();

            conn.setAutoCommit(true);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
            } catch (SQLException es) {
                System.out.println(es.getMessage());
            }
        }

    }

    /**
     * Allows user to edit any of the attributes associated with a member.
     *
     * @param in
     * @param userID
     * @param conn
     */
    private static void editReviewSetup(BufferedReader in, int userID,
            Connection conn) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            String sqlERS = "SELECT serialNumber, description "
                    + "FROM Ordered_Item, Add_To, Inventory, Equipment "
                    + "WHERE ordered_item.orderNumber=add_to.orderNumber "
                    + "AND add_to.inventoryId=inventory.inventoryId "
                    + "AND inventory.inventoryId=equipment.inventoryId "
                    + "AND equipment.serialNumber IN  (SELECT serialNumber "
                    + "FROM Rent WHERE userId = ? AND serialNumber "
                    + "IN (SELECT serialNumber FROM Reviews_Ratings "
                    + "WHERE userId = ?));";
            stmt = conn.prepareStatement(sqlERS);
            stmt.setInt(1, userID);
            stmt.setInt(2, userID);
            rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int col = rsmd.getColumnCount();
            if (rs.next()) {
                System.out.println("Review(s) Available to Edit");
                System.out.println("---------------------------");
                System.out.println(rs.getString(1) + " - " + rs.getString(2));
                while (rs.next()) {
                    int i;
                    for (i = 1; i < col; i++) {
                        String serNum = rs.getString(i);
                        i++;
                        String desc = rs.getString(i);
                        System.out.println(serNum + " - " + desc);
                    }
                }

                String serialNum = "";
                while (serialNum.length() != 6) {
                    System.out.println("Enter the associated serial number of "
                            + "review to edit:");
                    serialNum = in.readLine();
                    if (serialNum.length() != 6) {
                        System.out.println("Invalid serial number length.");
                        System.out.println();
                    }
                }

                int rating = 0;
                boolean invalidRat = true;
                while (invalidRat) {
                    System.out.println("Enter updated rating of " + serialNum
                            + " from 1-5:");
                    String rat = in.readLine();
                    if (isInteger(rat)) {
                        rating = Integer.parseInt(rat);
                        if (rating >= 1 && rating <= 5) {
                            invalidRat = false;
                        } else {
                            System.out.println(
                                    "Error rating must be between 1-5.");
                        }
                    } else {
                        System.out.println("Invalid rating input.");
                        System.out.println();
                    }

                }
                String review = "";
                while (review.length() == 0) {
                    System.out.println(
                            "Enter updated review of " + serialNum + ":");
                    review = in.readLine();
                    if (review.length() == 0) {
                        System.out.println("Invalid review length.");
                    }
                }
                getPreviousDT(conn, userID, serialNum, rating, review);
            } else {
                System.out.println("No items to create new review.");
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
     *
     * @param conn
     * @param userID
     * @param serialNum
     * @param rating
     * @param review
     */
    private static void getPreviousDT(Connection conn, int userID,
            String serialNum, int rating, String review) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlDT = "SELECT dateCreated, timeCreated "
                    + "FROM Reviews_Ratings "
                    + "WHERE userId = ? AND serialNumber = ?;";
            stmt = conn.prepareStatement(sqlDT);
            stmt.setInt(1, userID);
            stmt.setString(2, serialNum);
            rs = stmt.executeQuery();
            String oldDate = rs.getString(1);
            String oldTime = rs.getString(2);

            if (rating != 0) {
                editReview(conn, userID, serialNum, oldDate, oldTime, rating,
                        review);
            } else {
                deleteReview(conn, userID, serialNum, oldDate, oldTime);
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
     *
     * @param conn
     * @param userID
     * @param serialNum
     * @param oldDate
     * @param oldTime
     */
    private static void deleteReview(Connection conn, int userID,
            String serialNum, String oldDate, String oldTime) {

        String sqlDR = "DELETE FROM Reviews_Ratings "
                + "WHERE userId = ? AND serialNumber = ?";

        String sqlDM = "DELETE FROM Make "
                + "WHERE userId = ? AND dateCreated = ? AND timeCreated = ?";

        ResultSet rs = null;
        PreparedStatement stmt = null, stmt2 = null;

        try {

            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(sqlDR);
            stmt.setInt(1, userID);
            stmt.setString(2, serialNum);
            int row = stmt.executeUpdate();

            if (row != 1) {
                conn.rollback();
            }

            stmt2 = conn.prepareStatement(sqlDM);
            stmt2.setInt(1, userID);
            stmt2.setString(2, oldDate);
            stmt2.setString(3, oldTime);

            row = stmt2.executeUpdate();
            if (row != 1) {
                conn.rollback();
            } else {
                System.out.println("Successfully deleted review.");
            }

            conn.commit();

            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
            } catch (SQLException es) {
                System.out.println(es.getMessage());
            }
        }

    }

    /**
     *
     * @param conn
     * @param userID
     * @param serialNum
     * @param oldDate
     * @param oldTime
     * @param rating
     * @param review
     */
    @SuppressWarnings("deprecation")
    private static void editReview(Connection conn, int userID,
            String serialNum, String oldDate, String oldTime, int rating,
            String review) {

        Date dateCreated = new Date(System.currentTimeMillis());
        LocalTime timCreated = LocalTime.now();
        int hour = timCreated.getHour();
        int minute = timCreated.getMinute();
        int sec = timCreated.getSecond();
        Time timeCreated = new Time(hour, minute, sec);

        String sqlRR = "UPDATE Reviews_Ratings SET dateCreated = ?, "
                + "timeCreated = ?, rating = ?, review = ? "
                + "WHERE userId = ? AND serialNumber = ?";

        String sqlM = "UPDATE Make SET dateCreated = ?, timeCreated = ? "
                + "WHERE userId = ? AND dateCreated = ? AND timeCreated = ?";

        ResultSet rs = null;
        PreparedStatement stmt = null, stmt2 = null;

        try {

            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(sqlRR);
            stmt.setString(1, dateCreated.toString());
            stmt.setString(2, timeCreated.toString());
            stmt.setInt(3, rating);
            stmt.setString(4, review);
            stmt.setInt(5, userID);
            stmt.setString(6, serialNum);
            int row = stmt.executeUpdate();

            if (row != 1) {
                conn.rollback();
            }

            stmt2 = conn.prepareStatement(sqlM);
            stmt2.setString(1, dateCreated.toString());
            stmt2.setString(2, timeCreated.toString());
            stmt2.setInt(3, userID);
            stmt2.setString(4, oldDate);
            stmt2.setString(5, oldTime);

            row = stmt2.executeUpdate();
            if (row != 1) {
                conn.rollback();
            } else {
                System.out.println("Successfully edited review.");
            }

            conn.commit();

            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
            } catch (SQLException es) {
                System.out.println(es.getMessage());
            }
        }

    }

    /**
     * Allows user to navigate different tasks pertaining to members.
     *
     * @param in
     * @param wID
     * @param userID
     * @param conn
     */
    static void reviewsEquip(BufferedReader in, Connection conn, int wID)
            throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println("Enter the single character to select option:");
            System.out.println("(a) Average Reviews for All Equipment");
            System.out.println("(v) View Reviews for Equipment Item");
            System.out.println("(b) Back");
            String menuChoice = in.readLine();
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Choice Selection.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case 'a':
                            String sqlA = "SELECT avg(Rating), serialNumber "
                                    + "FROM Member, Reviews_Ratings "
                                    + "WHERE Member.userId = "
                                    + "Reviews_Ratings.userId "
                                    + "AND warehouseId = ? "
                                    + "GROUP BY serialNumber "
                                    + "ORDER BY avg(Rating) DESC;";
                            stmt = conn.prepareStatement(sqlA);
                            stmt.setInt(1, wID);
                            rs = stmt.executeQuery();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int colCount = rsmd.getColumnCount();
                            if (rs.next()) {
                                System.out.println(
                                        "Average Ratings with Associated "
                                                + "Equipment Serial Number");
                                System.out.println(
                                        "-----------------------------------"
                                                + "--------------------");
                                System.out.println(
                                        rs.getString(2) + " - " + rs.getInt(1));
                                while (rs.next()) {
                                    System.out.println(rs.getString(2) + " - "
                                            + rs.getInt(1));
                                }
                            } else {
                                System.out.println("NO REVIEWS ON EQUIP");
                            }
                            System.out.println();
                            System.out.println(
                                    "Press Enter When Done Viewing...");
                            in.readLine();
                            break;
                        case 'v':
                            String serialNum = "";
                            while (serialNum.length() != 6) {
                                System.out.println(
                                        "Enter serial number of equipment "
                                                + "item to view reviews:");
                                serialNum = in.readLine();
                                if (serialNum.length() != 6) {
                                    System.out.println(
                                            "ERROR INVALID SERIAL NUMBER LENGTH.");
                                }
                            }
                            String sqlV = "SELECT rating, review FROM "
                                    + "Reviews_Ratings WHERE serialNumber = ? "
                                    + "ORDER BY rating DESC;";
                            stmt = conn.prepareStatement(sqlV);
                            stmt.setString(1, serialNum);
                            rs = stmt.executeQuery();
                            if (rs.next()) {
                                System.out.println("REVIEWS FOR " + serialNum);
                                System.out.println("-----------------------");
                                System.out.println(
                                        rs.getInt(1) + " - " + rs.getString(2));
                                while (rs.next()) {
                                    System.out.println(rs.getInt(1) + " - "
                                            + rs.getString(2));
                                }
                            } else {
                                System.out.println(
                                        "No reviews for " + serialNum + ".");
                            }
                            System.out.println();
                            System.out.println(
                                    "Press Enter When Done Viewing...");
                            in.readLine();
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
                System.out.println();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException es) {
                    System.out.println(es.getMessage());
                }
            }
        }
    }

}