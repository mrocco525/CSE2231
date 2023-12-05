import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 *
 * @author Group 8
 *
 */
class Inventory {

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
     * Prompts user to select from several different inventory options.
     *
     * @param in
     * @throws IOException
     */
    static void inventoryMenu(BufferedReader in, int wID, Connection conn)
            throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println("Enter the single character to select option:");
            System.out.println("(a) Add Order to Inventory");
            System.out.println("(o) Ordered Items");
            System.out.println("(i) Edit Inventory Location");
            System.out.println("(e) Edit Drone Information");
            System.out.println("(z) Edit Equipment Information");
            System.out.println("(d) Delete Inventory");
            System.out.println("(s) Search Drones");
            System.out.println("(q) Search Equipment");
            System.out.println("(r) Reviews");
            System.out.println("(b) Back");
            String menuChoice = in.readLine();
            String inventoryId;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Choice Selection.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case 'a':
                            selectOrderToAdd(in, conn, wID);
                            break;
                        case 'o':
                            OrderedItems.orderedItemMenu(in, wID, conn);
                            break;
                        case 'i':
                            System.out.println("Enter Inventory ID:");
                            inventoryId = in.readLine();
                            if (isInteger(inventoryId)) {
                                int iId = Integer.parseInt(inventoryId);
                                String sqlI = "SELECT * FROM Inventory "
                                        + "WHERE inventoryId = ?;";
                                stmt = conn.prepareStatement(sqlI);
                                stmt.setInt(1, iId);
                                rs = stmt.executeQuery();
                                if (rs.next()) {
                                    editInventory(in, rs, iId, conn);
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "ERROR 404: Inventory Not Found.");
                                }
                            } else {
                                System.out.println("Invalid inventory ID.");
                                System.out.println();
                            }
                            break;
                        case 'z':
                            System.out.println("Enter Inventory ID:");
                            inventoryId = in.readLine();
                            if (isInteger(inventoryId)) {
                                int iId = Integer.parseInt(inventoryId);
                                String sqlI = "SELECT * FROM Equipment "
                                        + "WHERE inventoryId = ?;";
                                stmt = conn.prepareStatement(sqlI);
                                stmt.setInt(1, iId);
                                rs = stmt.executeQuery();
                                if (rs.next()) {
                                    editEquipmentData(in, iId, conn);
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "ERROR 404: Inventory Not Found.");
                                }
                            } else {
                                System.out.println("Invalid inventory ID.");
                                System.out.println();
                            }
                            break;
                        case 'e':
                            System.out.println("Enter Inventory ID:");
                            inventoryId = in.readLine();
                            if (isInteger(inventoryId)) {
                                int iId = Integer.parseInt(inventoryId);
                                String sqlI = "SELECT * FROM Drones "
                                        + "WHERE inventoryId = ?;";
                                stmt = conn.prepareStatement(sqlI);
                                stmt.setInt(1, iId);
                                rs = stmt.executeQuery();
                                if (rs.next()) {
                                    editDroneData(in, iId, conn, rs);
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "ERROR 404: Inventory Not Found.");
                                }
                            } else {
                                System.out.println("Invalid inventory ID.");
                                System.out.println();
                            }
                            break;
                        case 'd':
                            deleteInventoryItem(in, conn, wID);
                            break;
                        case 's':
                            System.out.println("Enter Inventory ID:");
                            inventoryId = in.readLine();
                            if (isInteger(inventoryId)) {
                                int iId = Integer.parseInt(inventoryId);
                                String sqlI = "SELECT Inventory.inventoryId, "
                                        + "Drones.serialNumber, Drones.modelNumber, "
                                        + "Drones.fleetId, Drones_Model.maxSpeed, "
                                        + "Drones_Model.distanceAutonomy, "
                                        + "Drones_Model.weightCapacity FROM Inventory, "
                                        + "Drones, Drones_Model "
                                        + "WHERE Inventory.inventoryId = ? AND "
                                        + "Inventory.inventoryId = "
                                        + "Drones.inventoryId AND "
                                        + "Drones.modelNumber = "
                                        + "Drones_Model.modelNumber;";
                                stmt = conn.prepareStatement(sqlI);
                                stmt.setInt(1, iId);
                                rs = stmt.executeQuery();
                                ResultSetMetaData rsmd = rs.getMetaData();
                                int colCount = rsmd.getColumnCount();
                                if (rs.next()) {
                                    System.out.println("DRONE INFO");
                                    System.out.println("----------");
                                    System.out.print(rs.getString(1));
                                    for (int i = 2; i < colCount; i++) {
                                        System.out
                                                .print(", " + rs.getString(i));
                                    }
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "ERROR 404: Inventory Not Found.");
                                }
                            } else {
                                System.out.println("Invalid inventory ID.");
                                System.out.println();
                            }
                            break;
                        case 'q':
                            System.out.println("Enter Inventory ID:");
                            inventoryId = in.readLine();
                            if (isInteger(inventoryId)) {
                                int iId = Integer.parseInt(inventoryId);
                                String sqlI = "SELECT Inventory.inventoryId, "
                                        + "Equipment.serialNumber, "
                                        + "Equipment.modelNumber, "
                                        + "Equipment_Model.weight, "
                                        + "Equipment_Model.description, "
                                        + "Equipment_Model.costPerDay, "
                                        + "Equipment_Model.year FROM Inventory, "
                                        + "Equipment, Equipment_Model "
                                        + "WHERE Inventory.inventoryId = ? AND "
                                        + "Inventory.inventoryId = "
                                        + "Equipment.inventoryId AND "
                                        + "Equipment.modelNumber = "
                                        + "Equipment_Model.modelNumber;";
                                stmt = conn.prepareStatement(sqlI);
                                stmt.setInt(1, iId);
                                rs = stmt.executeQuery();
                                ResultSetMetaData rsmd = rs.getMetaData();
                                int colCount = rsmd.getColumnCount();
                                if (rs.next()) {
                                    System.out.println("EQUIPMENT INFO");
                                    System.out.println("--------------");
                                    System.out.print(rs.getString(1));
                                    for (int i = 2; i < colCount; i++) {
                                        System.out
                                                .print(", " + rs.getString(i));
                                    }
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "ERROR 404: Inventory Not Found.");
                                }
                            } else {
                                System.out.println("Invalid inventory ID.");
                                System.out.println();
                            }
                            break;
                        case 'r':
                            Reviews_Ratings.reviewsEquip(in, conn, wID);
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
     * @param in
     * @param conn
     * @param wID
     * @throws IOException
     */
    private static void deleteInventoryItem(BufferedReader in, Connection conn,
            int wID) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        int iId = 0;
        while (iId == 0) {
            System.out
                    .println("Enter inventory ID of item you wish to delete: ");
            String invID = in.readLine();
            if (isInteger(invID)) {
                iId = Integer.parseInt(invID);

            } else {
                System.out.println("INVALID INVENTORY ID.");
                System.out.println();
            }
        }

        try {

            String sqlDII = "SELECT elementType, O.orderNumber "
                    + "FROM Ordered_Item O, Add_To A, Inventory I "
                    + "WHERE I.inventoryId = ? AND O.orderNumber = "
                    + "A.orderNumber AND A.inventoryId = I.inventoryId;";
            stmt = conn.prepareStatement(sqlDII);
            stmt.setInt(1, iId);
            rs = stmt.executeQuery();
            int orderNumber = rs.getInt(2);
            if (rs.getString(1).equals("Drone")) {
                checkDroneModelTotal(conn, iId, orderNumber);
            } else {
                checkEquipModelTotal(conn, iId, orderNumber);
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
     * @param iId
     * @param orderNumber
     */
    private static void checkEquipModelTotal(Connection conn, int iId,
            int orderNumber) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlCD = "SELECT count(*) FROM Add_To WHERE orderNumber = ?;";
            stmt = conn.prepareStatement(sqlCD);
            stmt.setInt(1, orderNumber);
            rs = stmt.executeQuery();
            if (rs.getInt(1) == 1) {
                getEquipModel(conn, iId);
            } else {
                deleteEquip(conn, iId, null);
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
     * @param iId
     */
    private static void getEquipModel(Connection conn, int iId) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlDEM = "SELECT modelNumber FROM Equipment "
                    + "WHERE inventoryId = " + iId + ";";
            stmt = conn.prepareStatement(sqlDEM);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String modelNumber = rs.getString(1);
                deleteEquip(conn, iId, modelNumber);
            } else {
                System.out.println("ERROR FINDING EQUIPMENT_MODEL");
                System.out.println();
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
     * @param iId
     * @param modelNumber
     */
    private static void deleteEquipModel(Connection conn, int iId,
            String modelNumber) {

        PreparedStatement stmt = null;

        try {

            foreignKeysOff(conn);
            String sqlDEM = "DELETE FROM Equipment_Model WHERE modelNumber = '"
                    + modelNumber + "';";
            stmt = conn.prepareStatement(sqlDEM);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Successfully deleted Equipment_Model.");
                System.out.println();
            } else {
                System.out.println("ERROR DELETING EQUIPMENT_MODEL");
                System.out.println();
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

    }

    /**
     *
     * @param conn
     * @param iId
     * @param modelNumber
     */
    private static void deleteEquip(Connection conn, int iId,
            String modelNumber) {

        PreparedStatement stmt = null;

        try {

            //foreignKeysOff(conn);
            String sqlDI = "DELETE FROM Inventory WHERE inventoryId = " + iId
                    + ";";
            stmt = conn.prepareStatement(sqlDI);
            //stmt.setInt(1, iId);
            int row = stmt.executeUpdate();
            if (modelNumber != null) {
                deleteEquipModel(conn, iId, modelNumber);
            }
            if (row > 0) {
                System.out.println("Successfully deleted Equipment.");
                System.out.println();
            } else {
                System.out.println("ERROR DELETING EQUIPMENT");
                System.out.println();
            }
            //foreignKeysOn(conn);
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
     * @param iId
     * @param orderNumber
     */
    private static void checkDroneModelTotal(Connection conn, int iId,
            int orderNumber) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlCD = "SELECT count(*) FROM Add_To WHERE orderNumber = ?;";
            stmt = conn.prepareStatement(sqlCD);
            stmt.setInt(1, orderNumber);
            rs = stmt.executeQuery();
            if (rs.getInt(1) == 1) {
                getDroneModel(conn, iId);
            } else {
                deleteDrone(conn, iId, null);
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
     * @param iId
     */
    private static void getDroneModel(Connection conn, int iId) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlDEM = "SELECT modelNumber FROM Drones "
                    + "WHERE inventoryId = " + iId + ";";
            stmt = conn.prepareStatement(sqlDEM);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String modelNumber = rs.getString(1);
                deleteDrone(conn, iId, modelNumber);
            } else {
                System.out.println("ERROR FINDING EQUIPMENT_MODEL");
                System.out.println();
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
     * @param iId
     */
    private static void deleteDroneModel(Connection conn, int iId,
            String modelNumber) {

        PreparedStatement stmt = null;

        try {

            foreignKeysOff(conn);
            String sqlDEM = "DELETE FROM Drones_Model WHERE modelNumber = '"
                    + modelNumber + "';";
            stmt = conn.prepareStatement(sqlDEM);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Successfully deleted Drone_Model.");
                System.out.println();
            } else {
                System.out.println("ERROR DELETING DRONE_MODEL");
                System.out.println();
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

    }

    /**
     *
     * @param conn
     * @param iId
     */
    private static void deleteDrone(Connection conn, int iId,
            String modelNumber) {

        PreparedStatement stmt = null;

        try {

            //foreignKeysOff(conn);
            String sqlDI = "DELETE FROM Inventory WHERE inventoryId = " + iId
                    + ";";
            stmt = conn.prepareStatement(sqlDI);
            //stmt.setInt(1, iId);
            int row = stmt.executeUpdate();
            if (modelNumber != null) {
                deleteDroneModel(conn, iId, modelNumber);
            }
            if (row > 0) {
                System.out.println("Successfully deleted Drone.");
                System.out.println();
            } else {
                System.out.println("ERROR DELETING DRONE");
                System.out.println();
            }
            //foreignKeysOn(conn);
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
     * @param in
     * @param iId
     * @param rs
     * @param conn
     * @throws IOException
     */
    private static void editDroneData(BufferedReader in, int iId,
            Connection conn, ResultSet rs) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs2 = null;
        try {

            boolean notDone = true;
            while (notDone) {
                System.out.println();
                System.out.println(
                        "Enter the single character to select edit category:");
                System.out.println("(s) Serial Number");
                System.out.println("(t) Status");
                System.out.println("(f) Fleet ID");
                System.out.println("(e) Edit Model Information");
                System.out.println("(q) Back");
                String menuChoice = in.readLine();
                System.out.println();

                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Menu Choice.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case 's':
                            String sqlDS = "SELECT serialNumber "
                                    + "FROM Drones WHERE inventoryId = ?";
                            stmt = conn.prepareStatement(sqlDS);
                            stmt.setInt(1, iId);
                            rs2 = stmt.executeQuery();
                            String serialNumber = rs2.getString(1);
                            updateDSerialNumber(conn, in, serialNumber, iId);
                            break;
                        case 't':
                            updateDStatus(conn, in, iId, rs);
                            break;
                        case 'f':
                            updateDFleetID(conn, in, iId, rs);
                            break;
                        case 'e':
                            editDroneModelSelect(conn, in, iId);
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
     * @param iId
     * @param rs
     * @throws IOException
     */
    private static void editDroneModelSelect(Connection conn, BufferedReader in,
            int iId) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            String sqlM = "SELECT modelNumber FROM Drones WHERE inventoryId = ?";
            stmt = conn.prepareStatement(sqlM);
            stmt.setInt(1, iId);
            rs = stmt.executeQuery();
            String modNum = rs.getString(1);
            editDroneModelMenu(in, conn, iId, modNum);

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
     * @param iId
     * @param modNum
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    private static void editDroneModelMenu(BufferedReader in, Connection conn,
            int iId, String modNum) throws IOException {

        PreparedStatement stmt = null;
        try {

            boolean notDone = true;
            while (notDone) {
                System.out.println();
                System.out.println(
                        "Enter the single character to select edit category:");
                System.out.println("(o) Model Number");
                System.out.println("(e) Expiration Date");
                System.out.println("(s) Max Speed");
                System.out.println("(d) Distance Autonomy");
                System.out.println("(w) Weight Capacity");
                System.out.println("(y) Year");
                System.out.println("(m) Manufacturer");
                System.out.println("(n) Name");
                System.out.println("(q) Back");
                String menuChoice = in.readLine();
                System.out.println();
                String category;

                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Menu Choice.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case 'o':
                            category = "modelNumber";
                            String modNew = "";
                            while (modNew.length() != 6) {
                                System.out.println(
                                        "Enter new 6-digit model number "
                                                + "of drone(s): ex:(99999L)");
                                System.out.println(
                                        "Current model number : " + modNum);
                                modNew = in.readLine();
                                if (modNew.length() != 6) {
                                    System.out.println(
                                            "Invalid model number length.");
                                    System.out.println();
                                }
                            }
                            updateDrModelCategoryString(conn, modNum, category,
                                    modNew);
                            notDone = false;
                            break;
                        case 'e':
                            int year = 1900;
                            int month = 1;
                            int day = 1;
                            boolean invalidDate = true;
                            String date = "";
                            while (invalidDate) {
                                System.out.println(
                                        "Enter new model number warranty "
                                                + "expiration date: (ex: 2077-01-22)");
                                date = in.readLine();
                                if (isInteger(date.substring(0, 4))
                                        && isInteger(date.substring(5, 7))
                                        && isInteger(date.substring(8, 10))) {
                                    year = Integer
                                            .parseInt(date.substring(0, 4));
                                    month = Integer
                                            .parseInt(date.substring(5, 7));
                                    day = Integer
                                            .parseInt(date.substring(8, 10));
                                    if (year > 1900 && month >= 1 && month <= 12
                                            && day >= 1 && day <= 31) {
                                        year = year - 1900;
                                        month = month - 1;
                                        invalidDate = false;
                                    } else {
                                        System.out.println(
                                                "Invalid date entered. Try again.");
                                    }
                                } else {
                                    System.out.println(
                                            "Invalid date structure. Try again.");
                                }
                            }
                            Date warrantyExp = new Date(year, month, day);
                            String sqlWE = "UPDATE Drones_Model "
                                    + "SET warrantyExp = ? "
                                    + "WHERE modelNumber = ?";
                            stmt = conn.prepareStatement(sqlWE);
                            stmt.setDate(1, warrantyExp);
                            stmt.setString(2, modNum);
                            int row = stmt.executeUpdate();
                            if (row > 0) {
                                System.out.println(
                                        "Warranty expiration updated.");
                            } else {
                                System.out.println(
                                        "Failed to update warranty expiration.");
                            }
                            break;
                        case 's':
                            category = "maxSpeed";
                            int mSpeed = 0;
                            String maxSpeed = "";
                            while (maxSpeed.length() == 0) {
                                System.out.println(
                                        "Enter new max speed: ex:(55)");
                                maxSpeed = in.readLine();
                                if (isInteger(maxSpeed)) {
                                    mSpeed = Integer.parseInt(maxSpeed);
                                } else {
                                    System.out.println("Invalid input.");
                                    System.out.println();
                                }
                            }
                            updateDrModelCategoryInt(conn, modNum, category,
                                    mSpeed);
                            break;
                        case 'd':
                            category = "distanceAutonomy";
                            double dA = 0.0;
                            String distA = "";
                            while (distA.length() == 0) {
                                System.out.println("Enter new distance "
                                        + "autonomy: ex:(33.5)");
                                distA = in.readLine();
                                if (isDouble(distA)) {
                                    dA = Double.parseDouble(distA);
                                } else {
                                    System.out.println("Invalid input.");
                                    System.out.println();
                                }
                            }
                            updateDrModelCategoryDouble(conn, modNum, category,
                                    dA);
                            break;
                        case 'w':
                            category = "weightCapacity";
                            int weight = 0;
                            String w = "";
                            while (w.length() == 0) {
                                System.out.println(
                                        "Enter new weight capacity in lbs: ex:(15)");
                                w = in.readLine();
                                if (isInteger(w)) {
                                    weight = Integer.parseInt(w);
                                } else {
                                    System.out.println("Invalid input.");
                                    System.out.println();
                                }
                            }
                            updateDrModelCategoryInt(conn, modNum, category,
                                    weight);
                            break;
                        case 'y':
                            category = "year";
                            int yr = 0;
                            String y = "";
                            while (y.length() == 0) {
                                System.out.println("Enter new year: ex:(2001)");
                                y = in.readLine();
                                if (isInteger(y)) {
                                    yr = Integer.parseInt(y);
                                } else {
                                    System.out.println("Invalid input.");
                                    System.out.println();
                                }
                            }
                            updateDrModelCategoryInt(conn, modNum, category,
                                    yr);
                            break;
                        case 'm':
                            category = "manufacturer";
                            int mID = 0;
                            String manID = "";
                            while (manID.length() == 0) {
                                System.out.println(
                                        "Enter new manufacturer ID: ex:(1)");
                                manID = in.readLine();
                                if (isInteger(manID)) {
                                    mID = Integer.parseInt(manID);
                                } else {
                                    System.out.println("Invalid input.");
                                    System.out.println();
                                }
                            }
                            updateDrModelCategoryInt(conn, modNum, category,
                                    mID);
                            break;
                        case 'n':
                            category = "name";
                            String name = "";
                            while (name.length() != 6) {
                                System.out.println(
                                        "Enter new name of drone model: ex:(Nexus)");
                                name = in.readLine();
                                if (name.length() != 6) {
                                    System.out.println(
                                            "Invalid model number length.");
                                    System.out.println();
                                }
                            }
                            updateDrModelCategoryString(conn, modNum, category,
                                    name);
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
     * @param modNum
     * @param category
     * @param dA
     */
    private static void updateDrModelCategoryDouble(Connection conn,
            String modNum, String category, double dA) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlEMCS = "UPDATE Drones_Model SET " + category
                    + " = ? WHERE modelNumber = ?;";
            stmt = conn.prepareStatement(sqlEMCS);
            stmt.setDouble(1, dA);
            stmt.setString(2, modNum);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Category updated.");
                System.out.println();
            } else {
                System.out.println("Error updating category.");
                System.out.println();
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
     * @param modNum
     * @param category
     * @param mSpeed
     */
    private static void updateDrModelCategoryInt(Connection conn, String modNum,
            String category, int mSpeed) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlEMCS = "UPDATE Drones_Model SET " + category
                    + " = ? WHERE modelNumber = ?;";
            stmt = conn.prepareStatement(sqlEMCS);
            stmt.setInt(1, mSpeed);
            stmt.setString(2, modNum);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Category updated.");
                System.out.println();
            } else {
                System.out.println("Error updating category.");
                System.out.println();
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
     * @param modNum
     * @param category
     * @param modNew
     */
    private static void updateDrModelCategoryString(Connection conn,
            String modNum, String category, String modNew) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlDMCS = "UPDATE Drones_Model SET " + category
                    + " = ? WHERE modelNumber = ?;";
            stmt = conn.prepareStatement(sqlDMCS);
            stmt.setString(1, modNew);
            stmt.setString(2, modNum);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Category updated.");
                System.out.println();
            } else {
                System.out.println("Error updating category.");
                System.out.println();
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
     * @param in
     * @param iId
     * @param rs
     * @throws IOException
     */
    private static void updateDFleetID(Connection conn, BufferedReader in,
            int iId, ResultSet rs) throws IOException {

        PreparedStatement stmt = null;

        try {
            String fleetID = "";
            int fID = 0;
            while (fID == 0) {
                System.out.println("Enter new fleet ID of drone: ex:(1)");
                System.out.println("Current fleet ID: " + rs.getString(5));
                fleetID = in.readLine();
                if (!isInteger(fleetID)) {
                    System.out.println("Invalid fleet ID length.");
                    System.out.println();
                } else {
                    fID = Integer.parseInt(fleetID);
                }
            }
            String sqlFID = "UPDATE Drones SET fleetId = ? WHERE inventoryId = ?";
            stmt = conn.prepareStatement(sqlFID);
            stmt.setInt(1, fID);
            stmt.setInt(2, iId);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out
                        .println("Successfully updated fleet ID of inventory #"
                                + iId + ".");
                System.out.println();
            } else {
                System.out.println("Error updating fleet ID.");
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
     * @param in
     * @param iId
     * @param rs
     * @throws IOException
     */
    private static void updateDStatus(Connection conn, BufferedReader in,
            int iId, ResultSet rs) throws IOException {

        PreparedStatement stmt = null;

        try {
            String status = "";
            while (!status.equals("Active") && !status.equals("Inactive")) {
                System.out.println(
                        "Enter new status of drone: ex:(Active / Inactive)");
                System.out.println("Current status: " + rs.getString(4));
                status = in.readLine();
                if (!status.equals("Active") && !status.equals("Inactive")) {
                    System.out.println(
                            "Invalid status. " + "Make sure to capitalize.");
                    System.out.println();
                }
            }
            String sqlSN = "UPDATE Drones SET status = ? WHERE inventoryId = ?";
            stmt = conn.prepareStatement(sqlSN);
            stmt.setString(1, status);
            stmt.setInt(2, iId);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Successfully updated status of inventory #"
                        + iId + ".");
                System.out.println();
            } else {
                System.out.println("Error updating status.");
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
     * @param in
     * @param serialNumber
     * @param iId
     * @throws IOException
     */
    private static void updateDSerialNumber(Connection conn, BufferedReader in,
            String serialNumber, int iId) throws IOException {
        PreparedStatement stmt = null;

        try {
            String serialNum = "";
            while (serialNum.length() != 6) {
                System.out.println(
                        "Enter new 6-digit serial number of drone: ex:(ENORD1)");
                System.out.println("Current serial number: " + serialNumber);
                serialNum = in.readLine();
                if (serialNum.length() != 6) {
                    System.out.println("Invalid serial number length.");
                    System.out.println();
                }
            }
            String sqlSN = "UPDATE Drones SET serialNumber = ? WHERE inventoryId = ?";
            stmt = conn.prepareStatement(sqlSN);
            stmt.setString(1, serialNum);
            stmt.setInt(2, iId);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println(
                        "Successfully updated serial number of inventory #"
                                + iId + ".");
                System.out.println();
            } else {
                System.out.println("Error updating serial number.");
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
     * @param in
     * @param iId
     * @param conn
     * @throws IOException
     */
    private static void editEquipmentData(BufferedReader in, int iId,
            Connection conn) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            boolean notDone = true;
            while (notDone) {
                System.out.println();
                System.out.println(
                        "Enter the single character to select edit category:");
                System.out.println("(s) Serial Number");
                System.out.println("(e) Edit Model Information");
                System.out.println("(q) Back");
                String menuChoice = in.readLine();
                System.out.println();

                String sqlES = "SELECT serialNumber FROM "
                        + "Equipment WHERE inventoryId = ?";
                stmt = conn.prepareStatement(sqlES);
                stmt.setInt(1, iId);
                rs = stmt.executeQuery();
                String serialNumber = rs.getString(1);

                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Menu Choice.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case 's':
                            updateESerialNumber(conn, in, serialNumber, iId);
                            break;
                        case 'e':
                            editEquipmentModelSelect(in, conn, iId,
                                    serialNumber);
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
     * @param iId
     * @throws IOException
     */
    private static void editEquipmentModelSelect(BufferedReader in,
            Connection conn, int iId, String serialNumber) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            String sqlM = "SELECT modelNumber FROM Equipment WHERE inventoryId = ?";
            stmt = conn.prepareStatement(sqlM);
            stmt.setInt(1, iId);
            rs = stmt.executeQuery();
            String modNum = rs.getString(1);
            editEquipmentModelMenu(in, conn, iId, modNum);

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
     * @param iId
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    private static void editEquipmentModelMenu(BufferedReader in,
            Connection conn, int iId, String modNum) throws IOException {

        PreparedStatement stmt = null;
        try {

            boolean notDone = true;
            while (notDone) {
                System.out.println();
                System.out.println(
                        "Enter the single character to select edit category:");
                System.out.println("(m) Model Number");
                System.out.println("(s) Size");
                System.out.println("(a) Manufacturer");
                System.out.println("(w) Weight");
                System.out.println("(e) Warranty Expiration");
                System.out.println("(d) Description");
                System.out.println("(y) Year");
                System.out.println("(c) Cost Per Day");
                System.out.println("(q) Back");
                String menuChoice = in.readLine();
                System.out.println();
                String category;

                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Menu Choice.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case 'm':
                            category = "modelNumber";
                            String modNew = "";
                            while (modNew.length() != 6) {
                                System.out.println("Enter new 6-digit model "
                                        + "number of equipment item(s): ex:(99999L)");
                                modNew = in.readLine();
                                if (modNew.length() != 6) {
                                    System.out.println(
                                            "Invalid model number length.");
                                    System.out.println();
                                }
                            }
                            updateEqModelCategoryString(conn, modNum, category,
                                    modNew);
                            notDone = false;
                            break;
                        case 's':
                            category = "size";
                            String size = "";
                            while (size.length() == 0) {
                                System.out.println("Enter new model number "
                                        + "size in feet: ex:(1x1x1)");
                                size = in.readLine();
                                if (size.length() == 0) {
                                    System.out.println("Invalid size input.");
                                    System.out.println();
                                }
                            }
                            updateEqModelCategoryString(conn, modNum, category,
                                    size);
                            break;
                        case 'a':
                            category = "manufacturer";
                            int mID = 0;
                            String manID = "";
                            while (manID.length() == 0) {
                                System.out.println(
                                        "Enter new manufacturer ID: ex:(1)");
                                manID = in.readLine();
                                if (isInteger(manID)) {
                                    mID = Integer.parseInt(manID);
                                } else {
                                    System.out.println("Invalid input.");
                                    System.out.println();
                                }
                            }
                            updateEqModelCategoryInt(conn, modNum, category,
                                    mID);
                            break;
                        case 'w':
                            category = "weight";
                            int weight = 0;
                            String w = "";
                            while (w.length() == 0) {
                                System.out.println(
                                        "Enter new weight in lbs: ex:(1)");
                                w = in.readLine();
                                if (isInteger(w)) {
                                    weight = Integer.parseInt(w);
                                } else {
                                    System.out.println("Invalid input.");
                                    System.out.println();
                                }
                            }
                            updateEqModelCategoryInt(conn, modNum, category,
                                    weight);
                            break;
                        case 'e':
                            int year = 1900;
                            int month = 1;
                            int day = 1;
                            boolean invalidDate = true;
                            String date = "";
                            while (invalidDate) {
                                System.out.println("Enter new model number "
                                        + "warranty expiration date: "
                                        + "(ex: 2077-01-22)");
                                date = in.readLine();
                                if (isInteger(date.substring(0, 4))
                                        && isInteger(date.substring(5, 7))
                                        && isInteger(date.substring(8, 10))) {
                                    year = Integer
                                            .parseInt(date.substring(0, 4));
                                    month = Integer
                                            .parseInt(date.substring(5, 7));
                                    day = Integer
                                            .parseInt(date.substring(8, 10));
                                    if (year > 1900 && month >= 1 && month <= 12
                                            && day >= 1 && day <= 31) {
                                        year = year - 1900;
                                        month = month - 1;
                                        invalidDate = false;
                                    } else {
                                        System.out.println(
                                                "Invalid date entered. Try again.");
                                    }
                                } else {
                                    System.out.println(
                                            "Invalid date structure. Try again.");
                                }
                            }
                            Date warrantyExp = new Date(year, month, day);
                            String sqlWE = "UPDATE Equipment_Model SET "
                                    + "warrantyExpiration = '" + warrantyExp
                                    + "' WHERE " + "modelNumber = ?";
                            stmt = conn.prepareStatement(sqlWE);
                            stmt.setString(1, modNum);
                            int row = stmt.executeUpdate();
                            if (row > 0) {
                                System.out.println(
                                        "Warranty expiration updated.");
                            } else {
                                System.out.println(
                                        "Failed to update warranty expiration.");
                            }
                            break;
                        case 'd':
                            category = "description";
                            String description = "";
                            while (description.length() == 0) {
                                System.out.println("Enter new model number "
                                        + "description: "
                                        + "ex:(Tool/Electric/Gardening)");
                                description = in.readLine();
                                if (description.length() == 0) {
                                    System.out.println(
                                            "Invalid description input.");
                                    System.out.println();
                                }
                            }
                            updateEqModelCategoryString(conn, modNum, category,
                                    description);
                            break;
                        case 'y':
                            category = "year";
                            int yr = 0;
                            String y = "";
                            while (y.length() == 0) {
                                System.out.println(
                                        "Enter new weight in lbs: ex:(1)");
                                y = in.readLine();
                                if (isInteger(y)) {
                                    yr = Integer.parseInt(y);
                                } else {
                                    System.out.println("Invalid input.");
                                    System.out.println();
                                }
                            }
                            updateEqModelCategoryInt(conn, modNum, category,
                                    yr);
                            break;
                        case 'c':
                            category = "costPerDay";
                            double cPD = 0;
                            String costPD = "";
                            while (costPD.length() == 0) {
                                System.out.println(
                                        "Enter new cost per day: ex:(1)");
                                costPD = in.readLine();
                                if (isDouble(costPD)) {
                                    cPD = Double.parseDouble(costPD);
                                } else {
                                    System.out.println("Invalid input.");
                                    System.out.println();
                                }
                            }
                            updateEqModelCategoryDouble(conn, modNum, category,
                                    cPD);
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
     * @param modNum
     * @param category
     * @param cPD
     */
    private static void updateEqModelCategoryDouble(Connection conn,
            String modNum, String category, double cPD) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlEMCS = "UPDATE Equipment_Model SET " + category
                    + " = ? WHERE modelNumber = ?";
            stmt = conn.prepareStatement(sqlEMCS);
            stmt.setDouble(1, cPD);
            stmt.setString(2, modNum);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Category updated.");
                System.out.println();
            } else {
                System.out.println("Error updating category.");
                System.out.println();
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
     * @param modNum
     * @param category
     * @param mID
     */
    private static void updateEqModelCategoryInt(Connection conn, String modNum,
            String category, int mID) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlEMCS = "UPDATE Equipment_Model SET " + category
                    + " = ? WHERE modelNumber = ?";
            stmt = conn.prepareStatement(sqlEMCS);
            stmt.setInt(1, mID);
            stmt.setString(2, modNum);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Category updated.");
                System.out.println();
            } else {
                System.out.println("Error updating category.");
                System.out.println();
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
     * @param modNum
     * @param category
     * @param modNew
     */
    private static void updateEqModelCategoryString(Connection conn,
            String modNum, String category, String modNew) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sqlEMCS = "UPDATE Equipment_Model SET " + category
                    + " = ? WHERE modelNumber = ?";
            stmt = conn.prepareStatement(sqlEMCS);
            stmt.setString(1, modNew);
            stmt.setString(2, modNum);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Category updated.");
                System.out.println();
            } else {
                System.out.println("Error updating category.");
                System.out.println();
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
     * @param in
     * @param serialNumber
     * @param iId
     * @throws IOException
     */
    private static void updateESerialNumber(Connection conn, BufferedReader in,
            String serialNumber, int iId) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String serialNum = "";
            while (serialNum.length() != 6) {
                System.out.println(
                        "Enter the 6-digit serial number of equipment item: ex:(EEEEE1)");
                serialNum = in.readLine();
                if (serialNum.length() != 6) {
                    System.out.println("Invalid serial number length.");
                    System.out.println();
                }
            }
            String sqlSN = "UPDATE Equipment SET serialNumber = ? WHERE inventoryId = ?";
            stmt = conn.prepareStatement(sqlSN);
            stmt.setString(1, serialNum);
            stmt.setInt(2, iId);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println(
                        "Successfully updated serial number of inventory #"
                                + iId + ".");
                System.out.println();
            } else {
                System.out.println("Error updating serial number.");
                System.out.println();
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
     * @param rs
     * @param iId
     * @param conn
     * @throws IOException
     */
    private static void editInventory(BufferedReader in, ResultSet rs, int iId,
            Connection conn) throws IOException {

        PreparedStatement stmt = null;

        try {
            String loc;
            boolean invalidL = true;
            while (invalidL) {
                System.out.println(
                        "Input new inventory location: ex:( In / Out )");
                System.out.println("Current status: " + rs.getString(2));
                loc = in.readLine();
                if (loc.equals("In") || loc.equals("Out")) {
                    String sqlL = "UPDATE Inventory SET "
                            + "location = ? WHERE inventoryId = ?";
                    stmt = conn.prepareStatement(sqlL);
                    stmt.setString(1, loc);
                    stmt.setInt(2, iId);
                    int row = stmt.executeUpdate();
                    if (row > 0) {
                        System.out.println();
                        System.out.println(
                                "Successfully changed location of inventory #"
                                        + iId + ".");
                        invalidL = false;
                    } else {
                        System.out.println();
                        System.out.println("Error updating location.");
                    }
                } else {
                    System.out.println("Invalid location selection. "
                            + "Make sure to capitalize your choice.");
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
     * @param in
     * @param conn
     * @param wID
     * @throws IOException
     */
    private static void selectOrderToAdd(BufferedReader in, Connection conn,
            int wID) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            String sqlC = "SELECT orderNumber, description, numberOfItems, ETA "
                    + "FROM Ordered_Item "
                    + "WHERE status = \"IT\" AND wId = ? "
                    + "ORDER BY orderNumber;";
            stmt = conn.prepareStatement(sqlC);
            stmt.setInt(1, wID);
            rs = stmt.executeQuery();
            System.out.println("CURRENT ORDER LIST");
            System.out.println("---------------------------------------------");
            System.out.println("orderNumber, description, numberOfItems, ETA");
            System.out.println("---------------------------------------------");
            while (rs.next()) {
                System.out.print(rs.getInt(1) + ", " + rs.getString(2) + ", "
                        + rs.getInt(3) + ", " + rs.getString(4));
                System.out.println();
            }
            System.out.println(
                    "Select order number you would like to add to inventory: ");
            String ordNum = in.readLine();
            if (isInteger(ordNum)) {
                int orderNumber = Integer.parseInt(ordNum);
                updateOrder(in, conn, wID, orderNumber);
            } else {
                System.out.println("Invalid value for order number.");
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
     * @param wID
     * @param orderNumber
     * @throws IOException
     */
    private static void updateOrder(BufferedReader in, Connection conn, int wID,
            int orderNumber) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            Date arrival = new Date(System.currentTimeMillis());
            String status = "AW";
            String sqlAO = "UPDATE Ordered_Item SET status = '" + status
                    + "', arrival = '" + arrival + "' WHERE orderNumber = "
                    + orderNumber + ";";
            stmt = conn.prepareStatement(sqlAO);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("PROCESSING ORDER ARRIVAL...");
                System.out.println();
                getInventoryID(in, conn, wID, orderNumber);
            } else {
                System.out.println("Error Processing Order Arrival.");
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
     * @param wID
     * @param orderNumber
     * @throws IOException
     */
    private static void getInventoryID(BufferedReader in, Connection conn,
            int wID, int orderNumber) throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            String sqlI = "SELECT max(inventoryId) FROM Inventory;";
            stmt = conn.prepareStatement(sqlI);
            rs = stmt.executeQuery();
            int inventoryID = Integer.parseInt(rs.getString(1)) + 1;
            getNumItems(in, conn, wID, orderNumber, inventoryID);

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
     * @param wID
     * @param orderNumber
     * @throws IOException
     */
    private static void getNumItems(BufferedReader in, Connection conn, int wID,
            int orderNumber, int inventoryID) throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            String sqlI = "SELECT numberOfItems, elementType, mId "
                    + "FROM Ordered_Item WHERE orderNumber = ?;";
            stmt = conn.prepareStatement(sqlI);
            stmt.setInt(1, orderNumber);
            rs = stmt.executeQuery();
            int numItems = Integer.parseInt(rs.getString(1));
            String desc = rs.getString(2);
            int mID = rs.getInt(3);
            addToInventory(in, conn, wID, orderNumber, inventoryID, numItems,
                    desc, mID);

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

    private static void addToInventory(BufferedReader in, Connection conn,
            int wID, int orderNumber, int inventoryID, int numItems,
            String desc, int mID) throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            int row = 0;
            int i;
            for (i = 1; i <= numItems; i++) {
                int val = inventoryID + i - 1;
                String sqlIA = "INSERT INTO Inventory (inventoryId, "
                        + "location, warehouseId) VALUES ('" + val + "', '"
                        + "In" + "', '" + wID + "');";
                stmt = conn.prepareStatement(sqlIA);
                row = stmt.executeUpdate();
            }

            if (row > 0) {
                System.out.println("IMPORTED TO INVENTORY...");
                System.out.println();
                addOrderToAddTo(in, conn, wID, orderNumber, inventoryID,
                        numItems, desc, mID);
            } else {
                System.out.println("ERROR IMPORTING TO INVENTORY");
                System.out.println();
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
     * @param wID
     * @param orderNumber
     * @param inventoryID
     * @param numItems
     * @param desc
     * @throws IOException
     */
    private static void addOrderToAddTo(BufferedReader in, Connection conn,
            int wID, int orderNumber, int inventoryID, int numItems,
            String desc, int mID) throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            int row = 0;
            for (int i = 1; i <= numItems; i++) {
                int val = inventoryID + i - 1;
                String sqlAT = "INSERT INTO Add_To (orderNumber, inventoryId) "
                        + "VALUES ('" + orderNumber + "', '" + val + "');";
                stmt = conn.prepareStatement(sqlAT);
                row = stmt.executeUpdate();
            }

            if (row > 0) {
                System.out.println("IMPORTED TO ADD_TO...");
                System.out.println();
                if (desc.equals("Drone")) {
                    addOrderToDroneModel(in, conn, mID, orderNumber,
                            inventoryID, numItems);
                } else {
                    addOrderToEquipModel(in, conn, orderNumber, inventoryID,
                            numItems, mID);
                }
            } else {
                System.out.println("ERROR IMPORTING TO ADD_TO");
                System.out.println();
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
     * @param modNum
     * @param orderNumber
     * @param inventoryID
     * @param numItems
     * @throws IOException
     */
    private static void addOrderToEquipData(BufferedReader in, Connection conn,
            int inventoryID, String modNum, int numItems, int orderNumber)
            throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            Queue<String> q = new ArrayDeque<>();
            String serialNum;
            while (q.size() != numItems) {
                System.out.println("Enter the 6-digit serial number of "
                        + "equipment item: ex:(EEEEE1)");
                serialNum = in.readLine();
                if (serialNum.length() != 6) {
                    System.out.println("Invalid serial number length.");
                    System.out.println();
                } else {
                    q.add(serialNum);
                }
            }

            int row = 0;
            for (int i = 1; i <= numItems; i++) {
                int val = inventoryID + i - 1;
                String serialNumber = q.remove();
                String sqlE = "INSERT INTO Equipment (serialNumber, "
                        + "inventoryId, modelNumber) " + "VALUES ('"
                        + serialNumber + "', " + val + ", '" + modNum + "');";
                stmt = conn.prepareStatement(sqlE);
                row = stmt.executeUpdate();
            }

            if (row > 0) {
                System.out.println("IMPORTED TO EQUIPMENT...");
                System.out.println();
                System.out.println(
                        "SUCCESSFULLY ADDED ORDER #" + orderNumber + ".");
                System.out.println();
            } else {
                System.out.println("ERROR IMPORTING TO EQUIPMENT");
                System.out.println();
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
     * @param mID
     * @param numItems
     * @param inventoryID
     * @param orderNumber
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    private static void addOrderToEquipModel(BufferedReader in, Connection conn,
            int orderNumber, int inventoryID, int numItems, int mID)
            throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        String modNum = "";
        while (modNum.length() != 6) {
            System.out.println(
                    "Enter the 6-digit model number of equipment item(s): ex:(99999L)");
            modNum = in.readLine();
            if (modNum.length() != 6) {
                System.out.println("Invalid model number length.");
                System.out.println();
            }
        }
        String size = "";
        while (size.length() == 0) {
            System.out.println("Enter model number size in feet: ex:(1x1x1)");
            size = in.readLine();
            if (size.length() == 0) {
                System.out.println("Invalid size input.");
                System.out.println();
            }
        }
        int weight = 0;
        while (weight == 0) {
            System.out.println("Enter model number weight in pounds: ex:(5)");
            String weigh = in.readLine();
            if (isInteger(weigh)) {
                weight = Integer.parseInt(weigh);
            } else {
                System.out.println("Invalid weight amount.");
            }
        }

        int year = 1900;
        int month = 1;
        int day = 1;
        boolean invalidDate = true;
        String date = "";
        while (invalidDate) {
            System.out.println(
                    "Enter model number warranty expiration date: (ex: 2077-01-22)");
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
        Date warrantyExp = new Date(year, month, day);

        String description = "";
        while (description.length() == 0) {
            System.out.println("Enter model number description: "
                    + "ex:(Tool, Watering, Electric, etc.)");
            description = in.readLine();
            if (description.length() == 0) {
                System.out.println("Invalid description length.");
            }
        }

        int modYear = 0;
        while (modYear == 0) {
            System.out.println("Enter model number year: ex:(2012)");
            String yr = in.readLine();
            if (isInteger(yr)) {
                modYear = Integer.parseInt(yr);
            } else {
                System.out.println("Invalid year input.");
            }
        }

        double costPerDay = 0.0;
        while (costPerDay < 1) {
            System.out.println("Enter model number cost per day: ex:(5.49)");
            String cPD = in.readLine();
            if (isDouble(cPD)) {
                costPerDay = Double.parseDouble(cPD);
            } else {
                System.out.println("Invalid cost per day input.");
            }
        }

        try {

            int row = 0;
            String sqlE = "INSERT INTO Equipment_Model (modelNumber, size, "
                    + "manufacturer, weight, warrantyExpiration, description,"
                    + " year, costPerDay) " + "VALUES ('" + modNum + "', '"
                    + size + "', '" + mID + "', '" + weight + "', '"
                    + warrantyExp + "', '" + description + "', '" + modYear
                    + "', '" + costPerDay + "'); COMMIT;";
            stmt = conn.prepareStatement(sqlE);
            row = stmt.executeUpdate();

            if (row > 0) {
                System.out.println("IMPORTED TO EQUIPMENT_MODEL...");
                System.out.println();
                addOrderToEquipData(in, conn, inventoryID, modNum, numItems,
                        orderNumber);
            } else {
                System.out.println("ERROR IMPORTING TO EQUIPMENT_MODEL");
                System.out.println();
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
     * @param orderNumber
     * @param inventoryID
     * @param numItems
     * @param modNum
     * @throws IOException
     */
    private static void addOrderToDroneData(BufferedReader in, Connection conn,
            String modNum, int orderNumber, int numItems, int inventoryID)
            throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            int row = 0;
            String serialNum = "";
            int fleetID = 0;
            for (int i = 1; i <= numItems; i++) {
                while (serialNum.length() != 6) {
                    System.out.println(
                            "Enter the 6-digit serial number of drone: ex:(ENORD1)");
                    serialNum = in.readLine();
                    if (serialNum.length() != 6) {
                        System.out.println("Invalid serial number length.");
                        System.out.println();
                    }
                }
                while (fleetID == 0) {
                    System.out.println("Enter associated fleet ID of drone "
                            + serialNum + ":");
                    String fID = in.readLine();
                    if (isInteger(fID)) {
                        fleetID = Integer.parseInt(fID);
                    } else {
                        System.out.println("Invalid fleet ID input.");
                        System.out.println();
                    }

                }
                int val = inventoryID + i - 1;
                String sqlD = "INSERT INTO Drones (serialNumber, inventoryId, "
                        + "modelNumber, status, fleetId) " + "VALUES ('"
                        + serialNum + "', '" + val + "', '" + modNum
                        + "', 'Active', '" + fleetID + "');";
                stmt = conn.prepareStatement(sqlD);
                row = stmt.executeUpdate();

                serialNum = "";
                fleetID = 0;
            }

            if (row > 0) {
                System.out.println("IMPORTED TO DRONE...");
                System.out.println();
                System.out.println(
                        "SUCCESSFULLY ADDED ORDER #" + orderNumber + ".");
                System.out.println();
            } else {
                System.out.println("ERROR IMPORTING TO DRONE");
                System.out.println();
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
     * @param modNum
     * @param mID
     * @param orderNumber
     * @param inventoryID
     * @param numItems
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    private static void addOrderToDroneModel(BufferedReader in, Connection conn,
            int mID, int orderNumber, int inventoryID, int numItems)
            throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        int year = 1900;
        int month = 1;
        int day = 1;
        boolean invalidDate = true;
        String date = "";

        String modNum = "";
        while (modNum.length() != 6) {
            System.out.println(
                    "Enter the 6-digit model number of drone(s): ex:(DDDDD1)");
            modNum = in.readLine();
            if (modNum.length() != 6) {
                System.out.println("Invalid model number length.");
                System.out.println();
            }
        }
        while (invalidDate) {
            System.out.println(
                    "Enter model number warranty expiration date: (ex: 2077-01-22)");
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
        Date warrantyExp = new Date(year, month, day);

        int maxSpeed = 0;
        while (maxSpeed == 0) {
            System.out.println("Enter model number max speed in mph: ex:(50)");
            String mS = in.readLine();
            if (isInteger(mS)) {
                maxSpeed = Integer.parseInt(mS);
            } else {
                System.out.println("Invalid speed value.");
            }
        }

        double distAutonomy = 0.0;
        while (distAutonomy < 1) {
            System.out.println(
                    "Enter model number distance autonomy in  miles: ex:(35.3)");
            String dA = in.readLine();
            if (isDouble(dA)) {
                distAutonomy = Double.parseDouble(dA);
            } else {
                System.out.println("Invalid distance autonomy input.");
            }
        }

        int weightCapacity = 0;
        while (weightCapacity == 0) {
            System.out.println(
                    "Enter model number weight capacity in lbs: ex:(50)");
            String wC = in.readLine();
            if (isInteger(wC)) {
                weightCapacity = Integer.parseInt(wC);
            } else {
                System.out.println("Invalid weight value.");
            }
        }

        int modYear = 0;
        while (modYear == 0) {
            System.out.println("Enter model number year: ex:(2012)");
            String yr = in.readLine();
            if (isInteger(yr)) {
                modYear = Integer.parseInt(yr);
            } else {
                System.out.println("Invalid year input.");
            }
        }

        String name = "";
        while (name.length() == 0) {
            System.out.println("Enter model number name: ex:(Nexus)");
            name = in.readLine();
            if (name.length() == 0) {
                System.out.println("Invalid name length.");
            }
        }

        try {

            int row = 0;
            String sqlE = "INSERT INTO Drones_Model (modelNumber, warrantyExp, "
                    + "maxSpeed, distanceAutonomy, weightCapacity, year,"
                    + " manufacturer, name) " + "VALUES ('" + modNum + "', '"
                    + warrantyExp + "', '" + maxSpeed + "', '" + distAutonomy
                    + "', '" + weightCapacity + "', '" + year + "', '" + mID
                    + "', '" + name + "');";
            stmt = conn.prepareStatement(sqlE);
            row = stmt.executeUpdate();

            if (row > 0) {
                System.out.println("IMPORTED TO EQUIPMENT_MODEL...");
                System.out.println();

                addOrderToDroneData(in, conn, modNum, orderNumber, numItems,
                        inventoryID);
            } else {
                System.out.println("ERROR IMPORTING TO EQUIPMENT_MODEL");
                System.out.println();
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