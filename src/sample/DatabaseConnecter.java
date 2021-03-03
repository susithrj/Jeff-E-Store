package sample;

import java.sql.*;


public class DatabaseConnecter {
    private static final String USERNAME = "Rajitha";//username of database
    private static final String PASSWORD = "Test123";//password of database
    private static final String URL = "jdbc:mysql://localhost/fish_shop";//url of database

    private static Connection connectToDatabase(){
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL,USERNAME,PASSWORD);//get connection to database
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database Error occurred");
        }
        return con;
    }
    public static ResultSet getData(String table,String coulumn){
        String sqlCode = "select  " + coulumn + " from " + table;//sql code to get data
        ResultSet result;
        try {
            Connection conn = DatabaseConnecter.connectToDatabase();
            PreparedStatement stm = conn.prepareStatement(sqlCode);
            result = stm.executeQuery();//execute code
            return result; //returning the data set

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
