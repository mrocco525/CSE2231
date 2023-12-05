import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Group 8
 *
 */
public class ServiceRepairer {

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
     *
     * @param in
     * @param wID
     * @param conn
     * @throws IOException
     */
    static void serviceMenu(BufferedReader in, int wID, Connection conn)
            throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println("Enter the single character to select option:");
            System.out.println("(n) New Service Repairer");
            System.out.println("(e) Edit Service Repairer");
            System.out.println("(d) Delete Service Repairer");
            System.out.println("(s) Search Service Repairer");
            System.out.println("(l) Service Repairer List");
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
                            newService(in, wID, conn);
                            break;
                        case 'e':
                            System.out.println("Enter Company Name:");
                            String companyName = in.readLine();
                            String sqlE = "SELECT * FROM service_repairer WHERE companyName = ?;";
                            stmt = conn.prepareStatement(sqlE);
                            stmt.setString(1, companyName);
                            rs = stmt.executeQuery();
                            if (rs.next()) {
                                editService(in, rs, companyName, conn);
                            } else {
                                System.out.println();
                                System.out.println(
                                        "ERROR 404: Company Not Found.");
                            }
                            break;
                        case 'd':
                            System.out.println("Enter Company Name:");
                            companyName = in.readLine();
                            deleteService(companyName, conn, wID);
                            break;
                        case 's':
                            System.out.println("Enter Company Name:");
                            companyName = in.readLine();
                            searchService(in, companyName, conn, wID);
                            break;
                        case 'l':
                            String sqlML = "SELECT companyName, phoneNumber, distance, wId, type "
                                    + "FROM service_repairer "
                                    + "WHERE service_repairer.wId = ?;";
                            stmt = conn.prepareStatement(sqlML);
                            stmt.setInt(1, wID);
                            rs = stmt.executeQuery();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int columnCount = rsmd.getColumnCount();
                            System.out.println("WAREHOUSE #" + wID
                                    + " SERVICE REPAIRER LIST");
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

    private static void newService(BufferedReader in, int wID, Connection conn)
            throws IOException {

        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        try {
            String cName = "";
            boolean invalidName = true;
            while (invalidName) {
                System.out.println("Enter Company Name (ex: Amazon): ");
                cName = in.readLine();
                if (cName.length() == 0) {
                    System.out.println("Invalid name length.  Try again.");
                } else {
                    invalidName = false;
                }
            }
            String phoneNum = "";
            boolean invalidPNum = true;
            while (invalidPNum) {
                System.out.println(
                        "Enter Company Phone Number: (ex: 1234567890)");
                phoneNum = in.readLine();
                if (phoneNum.length() != 10) {
                    System.out.println(
                            "Invalid phone number length.  Try again.");
                } else {
                    invalidPNum = false;
                }
            }
            boolean invalidDist = true;
            double distance = 0.0;
            String dist = "";
            while (invalidDist) {
                System.out.println("Enter Distance to Warehouse: (3.0)");
                dist = in.readLine();
                if (isDouble(dist)) {
                    distance = Double.parseDouble(dist);
                    invalidDist = false;
                } else {
                    System.out.println(
                            "Invalid distance measurement. Try again.");
                }
            }
            boolean invalidType = true;
            String type = "";
            while (invalidType) {
                System.out.println(
                        "Enter type of service repair (Equipment or Drone): ");
                type = in.readLine();
                if (type.compareTo("Equipment") == 1
                        && type.compareTo("Drone") == 1) {
                    System.out.println("Invalid type. Please try again.");
                } else {
                    invalidType = false;
                }
            }

            String sql2 = "INSERT INTO SERVICE_REPAIRER (companyName, phoneNumber, "
                    + "distance, wId, type) " + "VALUES ('" + cName + "', '"
                    + phoneNum + "', '" + distance + "', '" + wID + "', '"
                    + type + "');";
            stmt2 = conn.prepareStatement(sql2);
            int rowChange = stmt2.executeUpdate();
            if (rowChange > 0) {
                System.out.println(
                        "Success! You managed to add a service repairer!");
            } else {
                System.out.println(
                        "Service repairer information unable to be stored.");
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

    }

    private static void editService(BufferedReader in, ResultSet rs,
            String companyName, Connection conn) throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println(
                    "Enter the single character to select edit category:");
            System.out.println("(d) Distance");
            System.out.println("(p) Phone Number");
            System.out.println("(w) Warehouse ID");
            System.out.println("(t) Type");
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
                                    "Input new phone number: ex:(0123456789)");
                            System.out.println(
                                    "Current address: " + rs.getString(2));
                            String phoneNum = in.readLine();
                            if (phoneNum.length() == 10) {
                                String sqlP = "UPDATE service_repairer SET phoneNumber = ? WHERE companyName = ?";
                                stmt2 = conn.prepareStatement(sqlP);
                                stmt2.setString(1, phoneNum);
                                stmt2.setString(2, companyName);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed phone number of company: "
                                                    + companyName + ".");
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
                        case 'd':
                            System.out.println(
                                    "Input new service repairer distance: ex:(1)");
                            System.out.println(
                                    "Current distance : " + rs.getString(3));
                            String dist = in.readLine();
                            if (isDouble(dist)) {
                                double distance = Double.parseDouble(dist);
                                String sqlB = "UPDATE service_repairer SET "
                                        + "distance = ? WHERE companyName "
                                        + "= ?";
                                stmt2 = conn.prepareStatement(sqlB);
                                stmt2.setDouble(1, distance);
                                stmt2.setString(2, companyName);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed distance of company: "
                                                    + companyName + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating distance.");
                                }
                            } else {
                                System.out
                                        .println("Invalid input for distance");
                            }
                            break;
                        case 'w':
                            System.out.println(
                                    "Input new service repairer's warehouse ID: ex:(1)");
                            System.out.println(
                                    "Current warehouse ID: " + rs.getString(4));
                            String wareID = in.readLine();
                            if (isInteger(wareID)) {
                                int warehouseID = Integer.parseInt(wareID);
                                String sqlB = "UPDATE service_repairer SET "
                                        + "wId = ? WHERE companyName " + "= ?";
                                stmt2 = conn.prepareStatement(sqlB);
                                stmt2.setInt(1, warehouseID);
                                stmt2.setString(2, companyName);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed warehouseID of company "
                                                    + companyName + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println(
                                            "Error updating warehouseID.");
                                }
                            }
                            break;
                        case 't':
                            System.out.println(
                                    "Input new service repairer's type: (Equipment or Drone");
                            System.out.println(
                                    "Current type: " + rs.getString(5));
                            String type = in.readLine();
                            if (type.compareTo("Equipment") == 0
                                    || type.compareTo("Drone") == 0) {
                                String sqlT = "UPDATE service_repairer SET "
                                        + "Type = ? WHERE companyName" + "= ?";
                                stmt2 = conn.prepareStatement(sqlT);
                                stmt2.setString(1, type);
                                stmt2.setString(2, companyName);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed type of company "
                                                    + companyName + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println("Error updating type.");
                                }
                                break;
                            } else {
                                System.out.println(
                                        "Invalid input for warehouse ID.");
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
            }
            System.out.println();
        }
    }

    private static void deleteService(String companyName, Connection conn,
            int wID) {
        PreparedStatement stmt = null;
        try {
            String sqlD = "DELETE FROM service_repairer WHERE companyName = ? AND wId = ?;";
            stmt = conn.prepareStatement(sqlD);
            stmt.setString(1, companyName);
            stmt.setInt(2, wID);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println(
                        "Successfully Deleted Company " + companyName + ".");
            } else {
                System.out.println("ERROR DELETING Company");
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

    private static void searchService(BufferedReader in, String companyName,
            Connection conn, int wID) throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM service_repairer WHERE companyName = ? AND wId = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, companyName);
            stmt.setInt(2, wID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                System.out.println("Company: " + companyName);
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
                System.out.println("ERROR 404: Service Repairer Not Found.");
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
