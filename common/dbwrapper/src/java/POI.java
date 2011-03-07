package eo.common;

import java.lang.*;
import java.util.*;
import java.sql.*;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

// ================================================================================

public class POI {
    private SimpleJdbcTemplate conn_;

    private static final String POI_TABLE_NAME = "place_of_interest";
    private static final String POI_FIELD_ID = "id";
    private static final String POI_FIELD_NAME = "name";
    private static final String POI_FIELD_DESCR = "descr";

    public POI(SimpleJdbcTemplate conn) {
        conn_ = conn;
    }

    public void add(final String name, final String descr) throws SQLException {
        System.out.println(name + ", " + descr);

        /*        try {
            String query = "INSERT INTO " + POI_TABLE_NAME +
                    "(" + POI_FIELD_NAME + ", " + POI_FIELD_DESCR + ")" +
                    "VALUES ('" + name + "', '" + descr + "')";

            conn_.getJdbcOperations().execute(query);
        } catch (SQLException e) {
            System.out.println("SQLException!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
}

// ================================================================================