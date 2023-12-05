import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Manufacturer {

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

    static void manMenu(BufferedReader in, Connection conn) throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println("Enter the single character to select option:");
            System.out.println("(n) New Manufacturer");
            System.out.println("(e) Edit Manufacturer");
            System.out.println("(d) Delete Manufacturer");
            System.out.println("(s) Search Manufacturer");
            System.out.println("(l) Manufacturer List");
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
                            newMan(in, conn);
                            break;
                        case 'e':
                            System.out.println("Enter Manufacturer Id:");
                            int manId = Integer.parseInt(in.readLine());
                            String sqlE = "SELECT * FROM Manufacturer WHERE manufacturerId = ?;";
                            stmt = conn.prepareStatement(sqlE);
                            stmt.setInt(1, manId);
                            rs = stmt.executeQuery();
                            if (rs.next()) {
                                editMan(in, rs, manId, conn);
                            } else {
                                System.out.println();
                                System.out.println(
                                        "ERROR 404: Manufacturer Not Found.");
                            }
                            break;
                        case 'd':
                            System.out.println("Enter Manufacturer Id:");
                            manId = Integer.parseInt(in.readLine());
                            deleteMan(manId, conn);
                            break;
                        case 's':
                            System.out.println("Enter Manufacturer Id:");
                            manId = Integer.parseInt(in.readLine());
                            searchMan(in, manId, conn);
                            break;
                        case 'l':
                            String sqlML = "SELECT * FROM MANUFACTURER;";
                            stmt = conn.prepareStatement(sqlML);
                            rs = stmt.executeQuery();
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int columnCount = rsmd.getColumnCount();
                            System.out.println("MANUFACTURER LIST");
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

    private static void newMan(BufferedReader in, Connection conn)
            throws IOException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        try {
            String sqlNM = "SELECT Max(manufacturerId) From Manufacturer;";
            stmt = conn.prepareStatement(sqlNM);
            rs = stmt.executeQuery();
            String count = rs.getString(1);
            int manId = 1 + Integer.parseInt(count);
            System.out.println();
            String city = "";
            boolean invalidCity = true;
            while (invalidCity) {
                System.out
                        .println("Enter Manufacturer City Name (ex: Dallas): ");
                city = in.readLine();
                if (city.length() == 0) {
                    System.out.println("Invalid city length.  Try again.");
                } else {
                    invalidCity = false;
                }
            }
            String phoneNum = "";
            boolean invalidPNum = true;
            while (invalidPNum) {
                System.out.println(
                        "Enter Manufacturer Phone Number: (ex: 1234567890)");
                phoneNum = in.readLine();
                if (phoneNum.length() != 10) {
                    System.out.println(
                            "Invalid phone number length.  Try again.");
                } else {
                    invalidPNum = false;
                }
            }
            String manName = "";
            boolean invalidName = true;
            while (invalidName) {
                System.out.println(
                        "Enter Manufacturer Name (ex: Volmando LLC): ");
                manName = in.readLine();
                if (city.length() == 0) {
                    System.out.println("Invalid name length.  Try again.");
                } else {
                    invalidName = false;
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

            String sql2 = "INSERT INTO Manufacturer (manufacturerId, city, "
                    + "phoneNumber, ManufacturerName, type) " + "VALUES ('"
                    + manId + "', '" + city + "', '" + phoneNum + "', '"
                    + manName + "', '" + type + "');";
            stmt2 = conn.prepareStatement(sql2);
            int rowChange = stmt2.executeUpdate();
            if (rowChange > 0) {
                System.out
                        .println("Success! You managed to add a manufacturer!");
            } else {
                System.out.println(
                        "Manufacturer information unable to be stored.");
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

    private static void editMan(BufferedReader in, ResultSet rs, int manId,
            Connection conn) throws IOException {
        boolean notDone = true;
        while (notDone) {
            System.out.println();
            System.out.println(
                    "Enter the single character to select edit category:");
            System.out.println("(c) City");
            System.out.println("(p) Phone Number");
            System.out.println("(m) Manufacturer Name");
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
                        case 'c':
                            System.out.println("Input new city: ex:(Dallas)");
                            System.out.println(
                                    "Current city: " + rs.getString(2));
                            String city = in.readLine();
                            if (city.length() != 0) {
                                String sqlA = "UPDATE Manufacturer SET city = ? WHERE manufacturerId = ?";
                                stmt2 = conn.prepareStatement(sqlA);
                                stmt2.setString(1, city);
                                stmt2.setInt(2, manId);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed city of manufacturer "
                                                    + manId + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println("Error updating city.");
                                }
                            } else {
                                System.out.println("Invalid City Length.");
                            }
                            break;
                        case 'm':
                            System.out.println(
                                    "Input new manufacturer name: ex:(Sunny Co)");
                            System.out.println(
                                    "Current address: " + rs.getString(4));
                            String name = in.readLine();
                            if (name.length() != 0) {
                                String sqlA = "UPDATE Manufacturer SET manufacturerName = ? WHERE manufacturerId = ?";
                                stmt2 = conn.prepareStatement(sqlA);
                                stmt2.setString(1, name);
                                stmt2.setInt(2, manId);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed name of manufacturer "
                                                    + manId + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println("Error updating name.");
                                }
                            } else {
                                System.out.println("Invalid Name Length.");
                            }
                            break;
                        case 'p':
                            System.out.println(
                                    "Input new phone number: ex:(0123456789)");
                            System.out.println(
                                    "Current address: " + rs.getString(3));
                            String phoneNum = in.readLine();
                            if (phoneNum.length() == 10) {
                                String sqlP = "UPDATE manufacturer SET phoneNumber = ?"
                                        + "WHERE manufaacturerId = ?";
                                stmt2 = conn.prepareStatement(sqlP);
                                stmt2.setString(1, phoneNum);
                                stmt2.setInt(2, manId);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed phone number of manufacturer: "
                                                    + manId + ".");
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
                        case 't':
                            System.out.println(
                                    "Input new manufacturer type: (Equipment or Drone");
                            System.out.println(
                                    "Current type: " + rs.getString(5));
                            String type = in.readLine();
                            if (type.compareTo("Equipment") == 0
                                    || type.compareTo("Drone") == 0) {
                                String sqlT = "UPDATE manufacturer SET "
                                        + "Type = ? WHERE manufacturerId"
                                        + "= ?";
                                stmt2 = conn.prepareStatement(sqlT);
                                stmt2.setString(1, type);
                                stmt2.setInt(2, manId);
                                row = stmt2.executeUpdate();
                                if (row > 0) {
                                    System.out.println();
                                    System.out.println(
                                            "Successfully changed type of manufacturer "
                                                    + manId + ".");
                                    notDone = false;
                                } else {
                                    System.out.println();
                                    System.out.println("Error updating type.");
                                }
                                break;
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

    private static void deleteMan(int manId, Connection conn) {
        PreparedStatement stmt = null;
        try {
            String sqlD = "DELETE FROM manufacturer WHERE manufacturerId = ?;";
            stmt = conn.prepareStatement(sqlD);
            stmt.setInt(1, manId);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println(
                        "Successfully Deleted Manufacturer " + manId + ".");
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

    private static void searchMan(BufferedReader in, int manId, Connection conn)
            throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM manufacturer WHERE manufacturerId = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, manId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                System.out.println("Manufacturer #" + manId);
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
                System.out.println("ERROR 404: Manufacturer Not Found.");
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
