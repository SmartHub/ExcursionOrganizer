/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 3/4/11
 * Time: 1:43 AM
 * To change this template use File | Settings | File Templates.
 */

package ExcursionOrganizer.DB;

// Do we need it?
//import sun.plugin2.main.server.Plugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DBWrapper {
    public static final String USER_NAME = "exorg";
    public static final String USER_PASSWORD = "";
    //public static final String DB_URL = "jdbc:mysql://localhost:3306/excursion_organizer?useUnicode=true&amp;characterEncoding=utf8&amp;characterSetResults=utf8";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/excursion_organizer?characterEncoding=utf8";

    public static final String POI_TABLE_NAME = "place_of_interest";
    public static final String POI_FIELD_ID = "id";
    public static final String POI_FIELD_NAME = "name";
    public static final String POI_FIELD_DESCR = "descr";

    public static void execQuery(String query) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, USER_PASSWORD);

        try {
            Statement stat = conn.createStatement();
            stat.execute(query);
        } catch (SQLException e) {
            System.out.println("SQLException!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    public static void insertPlaceOfInterest(PlaceOfInterest poi) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, USER_PASSWORD);

        try {
            Statement stat = conn.createStatement();
            String query = "INSERT INTO " + POI_TABLE_NAME +
                    "(" + POI_FIELD_NAME + ", " + POI_FIELD_DESCR + ")" +
                    "VALUES ('" + poi.getName() + "', '" + poi.getDescription() + "')";

            stat.execute(query);
        } catch (SQLException e) {
            System.out.println("SQLException!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    public static void insertPlaceOfInterest(ArrayList<PlaceOfInterest> listOfPOI) throws SQLException {
        if (listOfPOI.isEmpty())
            return;

        Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, USER_PASSWORD);

        try {
            Statement stat = conn.createStatement();
            String query = "INSERT INTO " + POI_TABLE_NAME +
                    "(" + POI_FIELD_NAME + ", " + POI_FIELD_DESCR + ") " +
                    "VALUES ";
            for (Iterator<PlaceOfInterest> i = listOfPOI.iterator(); i.hasNext();) {
                PlaceOfInterest poi =  i.next();

                query += "('" + poi.getName() + "', '" + poi.getDescription() + "'), ";
            }
            query = query.substring(0, query.length() - 2);

            stat.execute(query);
        } catch (SQLException e) {
            System.out.println("SQLException!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    public static ArrayList<PlaceOfInterest> getAllPlaceOfInterest() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, USER_PASSWORD);

        try {
            Statement stat = conn.createStatement();
            String query = "SELECT * FROM " + POI_TABLE_NAME;
            ResultSet result = stat.executeQuery(query);
            ArrayList<PlaceOfInterest> listOfPOI = new ArrayList<PlaceOfInterest>();
            while (result.next()) {
                PlaceOfInterest poi = new PlaceOfInterest(result.getInt(POI_FIELD_ID),
                        result.getString(POI_FIELD_NAME), result.getString(POI_FIELD_DESCR));
                listOfPOI.add(poi);
            }
            return listOfPOI;
        } catch (SQLException e) {
            System.out.println("SQLException!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        return null;
    }

    public static PlaceOfInterest getPlaceOfInterestById(int id) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, USER_PASSWORD);

        try {
            Statement stat = conn.createStatement();
            String query = "SELECT * FROM " + POI_TABLE_NAME +
                    " WHERE " + POI_FIELD_ID + " = " + id;
            ResultSet result = stat.executeQuery(query);
            if (result.next()) {
                PlaceOfInterest poi = new PlaceOfInterest(result.getString(POI_FIELD_NAME), result.getString(POI_FIELD_DESCR));
                return poi;
            }
        } catch (SQLException e) {
            System.out.println("SQLException!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        return null;
    }
}