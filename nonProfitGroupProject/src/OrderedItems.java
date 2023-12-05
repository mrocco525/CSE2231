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
class OrderedItems {

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
    static void orderedItemMenu(BufferedReader in, int wID, Connection conn)
            throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println("Enter the single character to select option:");
            System.out.println("(n) New Order");
            System.out.println("(e) Edit Order");
            System.out.println("(d) Delete Order");
            System.out.println("(s) Search Orders");
            System.out.println("(c) Current Orders");
            System.out.println("(p) Past Orders");
            System.out.println("(b) Back");
            String menuChoice = in.readLine();
            String ordNum;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Choice Selection.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case 'n':
                            newOrderedItem(in, wID, conn);
                            break;
                        case 'e':
                            System.out.println("Enter Order Number:");
                            ordNum = in.readLine();
                            if (isInteger(ordNum)) {
                                int orderNumber = Integer.parseInt(ordNum);
                                String sqlOI = "SELECT * FROM Ordered_Item "
                                        + "WHERE orderNumber = ?;";
                                stmt = conn.prepareStatement(sqlOI);
                                stmt.setInt(1, orderNumber);
                                rs = stmt.executeQuery();
                                if (rs.next()) {
                                    editOrder(in, rs, orderNumber, conn);
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "ERROR 404: Order Not Found.");
                                }
                            } else {
                                System.out.println("Invalid order number.");
                                System.out.println();
                            }
                            break;
                        case 'd':
                            System.out.println("Enter Order Number:");
                            ordNum = in.readLine();
                            if (isInteger(ordNum)) {
                                int orderNumber = Integer.parseInt(ordNum);
                                deleteOrder(orderNumber, conn);
                            } else {
                                System.out.println("Invalid order number.");
                            }
                            break;
                        case 's':
                            System.out.println("Enter Order Number:");
                            ordNum = in.readLine();
                            if (isInteger(ordNum)) {
                                int orderNumber = Integer.parseInt(ordNum);
                                showOrderInfo(in, orderNumber, conn, wID);
                            } else {
                                System.out.println("Invalid UserID.");
                            }
                            break;
                        case 'c':
                            String sqlC = "SELECT orderNumber, description, "
                                    + "numberOfItems, ETA "
                                    + "FROM Ordered_Item "
                                    + "WHERE status = \"IT\" AND wId = ? "
                                    + "ORDER BY orderNumber;";
                            stmt = conn.prepareStatement(sqlC);
                            stmt.setInt(1, wID);
                            rs = stmt.executeQuery();
                            System.out.println("CURRENT ORDER LIST");
                            System.out.println(
                                    "---------------------------------------------");
                            System.out.println(
                                    "orderNumber, description, numberOfItems, ETA");
                            System.out.println(
                                    "---------------------------------------------");
                            while (rs.next()) {
                                System.out.print(rs.getInt(1) + ", "
                                        + rs.getString(2) + ", " + rs.getInt(3)
                                        + ", " + rs.getString(4));
                                System.out.println();
                            }
                            System.out.println();
                            System.out
                                    .println("Press enter when done viewing.");
                            in.readLine();
                            System.out.println();
                            break;
                        case 'p':
                            String sqlP = "SELECT orderNumber, description, "
                                    + "numberOfItems, arrival "
                                    + "FROM Ordered_Item "
                                    + "WHERE status = \"AW\" AND wId = ?"
                                    + "ORDER BY orderNumber;";
                            stmt = conn.prepareStatement(sqlP);
                            stmt.setInt(1, wID);
                            rs = stmt.executeQuery();
                            System.out.println("PREVIOUS ORDER LIST");
                            System.out.println(
                                    "------------------------------------------------");
                            System.out.println(
                                    "orderNumber, description, numberOfItems, arrival");
                            System.out.println(
                                    "------------------------------------------------");
                            while (rs.next()) {
                                System.out.print(rs.getInt(1) + ", "
                                        + rs.getString(2) + ", " + rs.getInt(3)
                                        + ", " + rs.getString(4));
                                System.out.println();
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
     * @param orderNumber
     * @param conn
     */
    private static void deleteOrder(int orderNumber, Connection conn) {
        PreparedStatement stmt = null;
        try {
            String sqlD = "DELETE FROM Ordered_Item WHERE orderNumber = ?";
            stmt = conn.prepareStatement(sqlD);
            stmt.setInt(1, orderNumber);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println(
                        "Successfully Deleted Order " + orderNumber + ".");
            } else {
                System.out.println("ERROR DELETING ORDER");
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
     * @param m
     * @param in
     * @throws IOException
     */
    private static void showOrderInfo(BufferedReader in, int orderNumber,
            Connection conn, int wID) throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sqlSOI = "SELECT * FROM Ordered_Item WHERE orderNumber = ? "
                    + "AND wId = ?;";
            stmt = conn.prepareStatement(sqlSOI);
            stmt.setInt(1, orderNumber);
            stmt.setInt(2, wID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                System.out.println("Order #" + orderNumber);
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
                System.out.println("ERROR 404: Order Not Found.");
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
     * @param wID
     * @param conn
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    private static void newOrderedItem(BufferedReader in, int wID,
            Connection conn) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        try {
            String sqlOI = "SELECT max(orderNumber) From ordered_item;";
            stmt = conn.prepareStatement(sqlOI);
            rs = stmt.executeQuery();
            String count = rs.getString(1);
            int orderNumber = 1 + Integer.parseInt(count);
            System.out.println();
            String description = "";
            boolean invalidDesc = true;
            while (invalidDesc) {
                System.out.println("Enter Order Description: (ex: Hammer)      "
                        + "(if drone input \"Drone\")");
                description = in.readLine();
                if (description.length() == 0) {
                    System.out
                            .println("Invalid description length.  Try again.");
                } else {
                    invalidDesc = false;
                }
            }
            String elementType = "";
            boolean invalidET = true;
            while (invalidET) {
                System.out.println(
                        "Enter Order Element Type: ( Equipment / Drone )");
                elementType = in.readLine();
                if (elementType.equals("Equipment")
                        || elementType.equals("Drone")) {
                    invalidET = false;
                } else {
                    System.out.println("Invalid element type selection. "
                            + "Make sure to capitalize your choice.");
                }
            }
            boolean invalidNumItems = true;
            int nItems = 0;
            while (invalidNumItems) {
                System.out.println("Enter Order Quantity: (ex: 5)");
                String numItems = in.readLine();
                if (isInteger(numItems)) {
                    nItems = Integer.parseInt(numItems);
                    invalidNumItems = false;
                } else {
                    System.out.println("Invalid number input.  Try again.");
                }
            }
            double value = 0.0;
            boolean invalidCost = true;
            while (invalidCost) {
                System.out.println("Enter Order Cost: (ex: 1010.50)");
                String cost = in.readLine();
                if (isDouble(cost)) {
                    value = Double.parseDouble(cost);
                    invalidCost = false;
                } else {
                    System.out.println("Invalid cost input. Try again.");
                }
            }

            int year = 1900;
            int month = 1;
            int day = 1;
            boolean invalidDate = true;
            String date = "";
            while (invalidDate) {
                System.out.println(
                        "Enter Order Estimated Date of Arrival: (ex: 2077-01-22)");
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
                        invalidDate = false;
                    } else {
                        System.out.println("Invalid date entered. Try again.");
                    }
                } else {
                    System.out.println("Invalid date structure. Try again.");
                }
            }
            Date ETA = new Date(year, month, day);
            Date arrivalDate = null;
            String status = "IT";

            int mID = 0;
            while (mID == 0) {
                mID = selectManufacturerMenu(wID, conn, in, elementType);
            }

            String sql2 = "INSERT INTO Ordered_Item (orderNumber, description, "
                    + "elementType, numberOfItems, value, ETA, arrival, status"
                    + ", wId, mId) " + "VALUES ('" + orderNumber + "', '"
                    + description + "', '" + elementType + "', '" + nItems
                    + "', '" + value + "', '" + ETA + "', '" + arrivalDate
                    + "', '" + status + "', '" + wID + "', '" + mID + "');";
            stmt2 = conn.prepareStatement(sql2);
            int rowChange = stmt2.executeUpdate();
            if (rowChange > 0) {
                System.out.println(
                        "Success! You managed to add an ordered item(s)!");
            } else {
                System.out.println(
                        "Ordered item(s) information unable to be stored.");
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
     *
     * @param wID
     * @param conn
     * @param in
     * @return int
     * @throws IOException
     */
    private static int selectManufacturerMenu(int wID, Connection conn,
            BufferedReader in, String equip_Drone) throws IOException {
        int manID = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String mQuery = "SELECT manufacturerId, manufacturerName FROM "
                    + "Manufacturer WHERE type = ?";
            stmt = conn.prepareStatement(mQuery);
            stmt.setString(1, equip_Drone);
            rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            System.out.println(equip_Drone + " MANUFACTURER LIST");
            System.out.println("---------------------------");
            while (rs.next()) {
                int i;
                for (i = 1; i <= columnCount; i++) {
                    String mID = rs.getString(i);
                    i++;
                    String mName = rs.getString(i);
                    System.out.println(mID + " : " + mName);
                }
            }
            System.out.println();
            System.out.println("Enter Manufacturer ID From List:");
            String val = in.readLine();
            if (val == "help") {
                System.out.println("Call to create manufacturer needed!");
            } else if (isInteger(val)) {
                manID = Integer.parseInt(val);
            } else {
                System.out
                        .println("Invalid manufacturer selection. Try again.");
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
        return manID;
    }

    /**
     *
     * @param in
     * @param rs
     * @param orderNumber
     * @param conn
     * @throws IOException
     */
    @SuppressWarnings({ "deprecation", "resource" })
    private static void editOrder(BufferedReader in, ResultSet rs,
            int orderNumber, Connection conn) throws IOException {

        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        try {

            boolean notDone = true;
            while (notDone) {
                System.out.println();
                System.out.println(
                        "Enter the single character to select edit category:");
                System.out.println("(d) Description");
                System.out.println("(e) Element Type");
                System.out.println("(n) Number Of Items");
                System.out.println("(c) Order Cost");
                System.out.println("(t) ETA");
                System.out.println("(a) Arrival Date");
                System.out.println("(s) Status");
                System.out.println("(w) Warehouse ID");
                System.out.println("(m) Manufacturer ID");
                System.out.println("(q) Back");
                String menuChoice = in.readLine();
                System.out.println();
                int row;

                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Menu Choice.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case 'd':
                            System.out.println(
                                    "Input new description: ex:(Hose)");
                            System.out.println(
                                    "Current description: " + rs.getString(2));
                            String description = in.readLine();
                            if (description.length() != 0) {
                                String sqlF = "UPDATE Ordered_Item SET "
                                        + "description = ? WHERE orderNumber = ?";
                                stmt2 = conn.prepareStatement(sqlF);
                                stmt2.setString(1, description);
                                stmt2.setInt(2, orderNumber);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed description of order "
                                                    + orderNumber + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating description.");
                                }
                            } else {
                                System.out
                                        .println("Invalid Description Length.");
                            }
                            break;
                        case 'e':
                            String elementType;
                            boolean invalidET = true;
                            while (invalidET) {
                                System.out.println("Input new element type: "
                                        + "ex:( Equipment / Drone )");
                                System.out.println("Current element type: "
                                        + rs.getString(3));
                                elementType = in.readLine();
                                if (elementType == "Equipment"
                                        || elementType == "Drone") {
                                    String sqlL = "UPDATE Ordered_Item "
                                            + "SET elementType = ? "
                                            + "WHERE orderNumber = ?";
                                    stmt2 = conn.prepareStatement(sqlL);
                                    stmt2.setString(1, elementType);
                                    stmt2.setInt(2, orderNumber);
                                    row = stmt2.executeUpdate();
                                    if (row > 0) {
                                        System.out.println();
                                        System.out.println(
                                                "Successfully changed element "
                                                        + "type of order number "
                                                        + orderNumber + ".");
                                        notDone = false;
                                        invalidET = false;
                                    } else {
                                        System.out.println();
                                        System.out.println(
                                                "Error updating element type.");
                                    }
                                } else {
                                    System.out.println(
                                            "Invalid element type selection. "
                                                    + "Make sure to capitalize "
                                                    + "your choice.");
                                }
                            }
                            break;
                        case 'n':
                            System.out.println(
                                    "Input new number of items: ex:(5)");
                            System.out.println("Current number of items: "
                                    + rs.getString(4));
                            String numItems = in.readLine();
                            if (isInteger(numItems)) {
                                int nItems = Integer.parseInt(numItems);
                                String sqlA = "UPDATE Ordered_Item "
                                        + "SET numberOfItems = ? "
                                        + "WHERE orderNumber = ?";
                                stmt2 = conn.prepareStatement(sqlA);
                                stmt2.setInt(1, nItems);
                                stmt2.setInt(2, orderNumber);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed number of "
                                                    + "items in order "
                                                    + orderNumber + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating number of items.");
                                }
                            } else {
                                System.out.println("Invalid number input.");
                            }
                            break;
                        case 'c':
                            System.out.println(
                                    "Input new order cost: ex:(10.95)");
                            System.out.println(
                                    "Current order cost: " + rs.getString(5));
                            String orderCost = in.readLine();
                            if (isDouble(orderCost)) {
                                double cost = Double.parseDouble(orderCost);
                                String sqlP = "UPDATE Ordered_Item "
                                        + "SET value = ? WHERE orderNumber = ?";
                                stmt2 = conn.prepareStatement(sqlP);
                                stmt2.setDouble(1, cost);
                                stmt2.setInt(2, orderNumber);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed cost of order "
                                                    + orderNumber + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating order cost.");
                                }
                            } else {
                                System.out.println("Invalid cost input.");
                            }
                            break;
                        case 't':
                            System.out.println(
                                    "Input new order ETA: ex:(2077-01-22)");
                            System.out
                                    .println("Current ETA: " + rs.getString(6));
                            String date = in.readLine();
                            if (isInteger(date.substring(0, 4))
                                    && isInteger(date.substring(5, 7))
                                    && isInteger(date.substring(8, 10))) {
                                int year = Integer
                                        .parseInt(date.substring(0, 4));
                                int month = Integer
                                        .parseInt(date.substring(5, 7));
                                int day = Integer
                                        .parseInt(date.substring(8, 10));
                                if (year > 1900 && month >= 1 && month <= 12
                                        && day >= 1 && day <= 31) {
                                    year = year - 1900;
                                    month = month - 1;
                                    Date ETA = new Date(year, month, day);
                                    String sqlT = "UPDATE Ordered_Item SET "
                                            + "ETA = ? WHERE orderNumber = ?";
                                    stmt2 = conn.prepareStatement(sqlT);
                                    stmt2.setDate(1, ETA);
                                    stmt2.setInt(2, orderNumber);
                                    row = stmt2.executeUpdate();
                                    if (row > 0) {
                                        System.out.println();
                                        System.out.println(
                                                "Successfully changed ETA of order"
                                                        + orderNumber + ".");
                                        notDone = false;
                                    } else {
                                        System.out.println();
                                        System.out
                                                .println("Error updating ETA.");
                                    }
                                } else {
                                    System.out.println(
                                            "Invalid date entered. Try again.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid date structure. Try again.");
                            }
                            break;
                        case 'a':
                            System.out.println(
                                    "Input new order arrival date: ex:(2077-01-22)");
                            System.out.println(
                                    "Current arrival date: " + rs.getString(7));
                            String arrival = in.readLine();
                            if (isInteger(arrival.substring(0, 4))
                                    && isInteger(arrival.substring(5, 7))
                                    && isInteger(arrival.substring(8, 10))) {
                                int year = Integer
                                        .parseInt(arrival.substring(0, 4));
                                int month = Integer
                                        .parseInt(arrival.substring(5, 7));
                                int day = Integer
                                        .parseInt(arrival.substring(8, 10));
                                if (year > 1900 && month >= 1 && month <= 12
                                        && day >= 1 && day <= 31) {
                                    year = year - 1900;
                                    month = month - 1;
                                    Date arrivalDate = new Date(year, month,
                                            day);
                                    String sqlT = "UPDATE Ordered_Item SET "
                                            + "arrival = ? WHERE orderNumber = ?";
                                    stmt2 = conn.prepareStatement(sqlT);
                                    stmt2.setDate(1, arrivalDate);
                                    stmt2.setInt(2, orderNumber);
                                    row = stmt2.executeUpdate();
                                    if (row > 0) {
                                        System.out.println();
                                        System.out.println(
                                                "Successfully changed arrival "
                                                        + "date of order"
                                                        + orderNumber + ".");
                                        notDone = false;
                                    } else {
                                        System.out.println();
                                        System.out.println(
                                                "Error updating arrival date.");
                                    }
                                } else {
                                    System.out.println(
                                            "Invalid date entered. Try again.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid date structure. Try again.");
                            }
                            break;
                        case 's':
                            String stat;
                            boolean invalidS = true;
                            while (invalidS) {
                                System.out.println(
                                        "Input new order status: ex:( IT / AW )");
                                System.out.println(
                                        "Current status: " + rs.getString(8));
                                stat = in.readLine();
                                if (stat == "IT" || stat == "AW") {
                                    String sqlL = "UPDATE Ordered_Item "
                                            + "SET status = ? "
                                            + "WHERE orderNumber = ?";
                                    stmt2 = conn.prepareStatement(sqlL);
                                    stmt2.setString(1, stat);
                                    stmt2.setInt(2, orderNumber);
                                    row = stmt2.executeUpdate();
                                    if (row > 0) {
                                        System.out.println();
                                        System.out.println(
                                                "Successfully changed status "
                                                        + "of order number "
                                                        + orderNumber + ".");
                                        notDone = false;
                                        invalidS = false;
                                    } else {
                                        System.out.println();
                                        System.out.println(
                                                "Error updating status.");
                                    }
                                } else {
                                    System.out.println(
                                            "Invalid status selection. "
                                                    + "Make sure to capitalize "
                                                    + "your choice.");
                                }
                            }
                            break;
                        case 'w':
                            System.out.println(
                                    "Input new order warehouse ID: ex:(1)");
                            System.out.println("Current order warehouse ID: "
                                    + rs.getString(9));
                            String warID = in.readLine();
                            if (isInteger(warID)) {
                                int wID = Integer.parseInt(warID);
                                String sqlB = "UPDATE member SET "
                                        + "wId = ? WHERE orderNumber " + "= ?";
                                stmt2 = conn.prepareStatement(sqlB);
                                stmt2.setDouble(1, wID);
                                stmt2.setInt(2, orderNumber);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed warehouse ID of order "
                                                    + orderNumber + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating warehouse ID.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid input for warehouse ID.");
                            }
                            break;
                        case 'm':
                            System.out.println(
                                    "Input new order manufacturer ID: ex:(1)");
                            System.out.println("Current order manufacturer ID: "
                                    + rs.getString(10));
                            String manID = in.readLine();
                            if (isInteger(manID)) {
                                int mID = Integer.parseInt(manID);
                                String sqlB = "UPDATE member SET "
                                        + "mId = ? WHERE orderNumber " + "= ?";
                                stmt2 = conn.prepareStatement(sqlB);
                                stmt2.setDouble(1, mID);
                                stmt2.setInt(2, orderNumber);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed manufacturer "
                                                    + "ID of order "
                                                    + orderNumber + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating manufacturer ID.");
                                }
                            } else {
                                System.out.println(
                                        "Invalid input for manufacturer ID.");
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