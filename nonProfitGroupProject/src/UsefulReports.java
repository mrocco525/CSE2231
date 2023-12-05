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
class UsefulReports {

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
     */
    /**
     * Allows user to navigate different tasks pertaining to members.
     */
    static void usefulReportMenu(BufferedReader in, Connection conn)
            throws IOException {
        boolean notDone = true;
        int columnCount = 0;
        ResultSetMetaData rsmd = null;
        while (notDone) {
            System.out.println();
            System.out.println("Enter the single character to select option:");
            System.out.println("(1) Check number of rented equipment items for a customer");
            System.out.println("(2) Most popular item rented");
            System.out.println("(3) Most popular manufacturer");
            System.out.println("(4) Most used drone");
            System.out.println("(5) Find the customer with the most items rented out");
            System.out.println("(6) Find equipment by type and year");
            System.out.println("(b) Back");
            String menuChoice = in.readLine();
            String sql1 = "SELECT fName, lName, count(RENT.userID) "
            		+ "FROM MEMBER, RENT "
            		+ "WHERE MEMBER.userId = RENT.userId "
            		+ "AND MEMBER.userID = ?"
            		+ ";";
            String sql2 = "SELECT O.description, sum(daysRented) "
            		+ "FROM Ordered_Item O, Add_To A, Equipment_Model M, Equipment E, Rent R "
            		+ "WHERE O.orderNumber = A.orderNumber "
            		+ "AND A.inventoryId = E.inventoryID "
            		+ "AND E.modelNumber = M.modelNumber "
            		+ "GROUP BY M.modelNumber "
            		+ "ORDER BY sum(daysRented) DESC "
            		+ "LIMIT 1; ";
            String sql3 = "SELECT manufacturerName, count() "
            		+ "FROM Manufacturer M, Rent R, Equipment E, Equipment_Model X "
            		+ "WHERE R.serialNumber = E.serialNumber "
            		+ "AND E.modelNumber = X.modelNumber "
            		+ "AND X.manufacturer = M.manufacturerId "
            		+ "GROUP BY M.manufacturerId "
            		+ "ORDER BY count() DESC "
            		+ "LIMIT 1; ";
            String sql4 = "SELECT name, count() "
            		+ "FROM Drones_Model M, Drones D "
            		+ "WHERE M.modelNumber = D.modelNumber "
            		+ "GROUP BY D.modelNumber "
            		+ "ORDER BY count() DESC "
            		+ "LIMIT 1; ";
            String sql5 = "SELECT fname, lname, count() AS count "
            		+ "FROM Member M, Rent R "
            		+ "WHERE M.userId = R.userId "
            		+ "GROUP BY M.userId "
            		+ "ORDER BY count DESC "
            		+ "LIMIT 1"
            		+ ";";
            String sql6 = "SELECT EM.description, I.description "
            		+ "FROM Equipment_Model EM, Ordered_Item I, ADD_TO A, Equipment E "
            		+ "WHERE EM.modelNumber = E.modelNumber "
            		+ "AND I.orderNumber = A.orderNumber "
            		+ "AND E.inventoryId = A.inventoryId "
            		+ "GROUP BY I.description "
            		+ "HAVING EM.year < ?;";
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                if (menuChoice.length() == 0) {
                    System.out.println("Invalid Choice Selection.");
                } else {
                    switch (menuChoice.charAt(0)) {
                        case '1':
                        	
                        	System.out.print("Enter member's userID: ");
                        	String userIDString = in.readLine();
                        	int userID = Integer.parseInt(userIDString);
                        	stmt = conn.prepareStatement(sql1);
                        	stmt.setInt(1, userID);
                            rs = stmt.executeQuery();
                            rsmd = rs.getMetaData();
                        	columnCount = rsmd.getColumnCount();
                            
                            for (int i = 1; i <= columnCount; i++) {
                    			String columnValue = rs.getString(i);
                        		System.out.print(columnValue);
                        		if (i < columnCount) System.out.print(",  ");
                    		}

                            break;
                        case '2':
                        	stmt = conn.prepareStatement(sql2);
                            rs = stmt.executeQuery();
                            rsmd = rs.getMetaData();
                        	columnCount = rsmd.getColumnCount();
                            for (int i = 1; i <= columnCount; i++) {
                    			String columnValue = rs.getString(i);
                        		System.out.print(columnValue);
                        		if (i < columnCount) System.out.print(",  ");
                    		}

                            break;
                        case '3':
                        	stmt = conn.prepareStatement(sql3);
                            rs = stmt.executeQuery();
                            
                            rsmd = rs.getMetaData();
                        	columnCount = rsmd.getColumnCount();
                            for (int i = 1; i <= columnCount; i++) {
                    			String columnValue = rs.getString(i);
                        		System.out.print(columnValue);
                        		if (i < columnCount) System.out.print(",  ");
                    		}

                            break;
                        case '4':
                        	stmt = conn.prepareStatement(sql4);
                            rs = stmt.executeQuery();
                            rsmd = rs.getMetaData();
                        	columnCount = rsmd.getColumnCount();
                            for (int i = 1; i <= columnCount; i++) {
                    			String columnValue = rs.getString(i);
                        		System.out.print(columnValue);
                        		if (i < columnCount) System.out.print(",  ");
                    		}

                            break;
                        case '5':
                            stmt = conn.prepareStatement(sql5);
                            rs = stmt.executeQuery();
                            rsmd = rs.getMetaData();
                        	columnCount = rsmd.getColumnCount();
                            for (int i = 1; i <= columnCount; i++) {
                    			String columnValue = rs.getString(i);
                        		System.out.print(columnValue);
                        		if (i < columnCount) System.out.print(",  ");
                    		}
                            break;
                            
                        case '6':
                        	System.out.print("Enter Year: ");
                        	String yearString = in.readLine();
                        	int year = Integer.parseInt(yearString);
                        	stmt = conn.prepareStatement(sql6);
                        	stmt.setInt(1, year);
                            rs = stmt.executeQuery();
                            rsmd = rs.getMetaData();
                        	columnCount = rsmd.getColumnCount();
                            while(rs.next()) {
                            	for (int i = 1; i <= columnCount; i++) {
                        			String columnValue = rs.getString(i);
                            		System.out.print(columnValue);
                            		if (i < columnCount) System.out.print(",  ");
                        		}
                            	System.out.println("");
                            }
                        	

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
}