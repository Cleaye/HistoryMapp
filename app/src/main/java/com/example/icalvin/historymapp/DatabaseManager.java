package com.example.icalvin.historymapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private final String url = "jdbc:mysql://185.13.227.203/jeffrvw171_mapp";
    private final String user = "jeffrvw171_mapp";
    private final String password = "16DILO059861Fg4FCP8d";

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public void getPlaces() {
        String query = "SELECT * FROM Location";

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                //resultSet
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { connection.close(); }
            catch(SQLException se) {}
            try { statement.close(); }
            catch(SQLException se) {}
            try { resultSet.close(); }
            catch(SQLException se) {}
        }
    }
}
