/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DBC;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import com.mysql.cj.jdbc.JdbcPropertySet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author halap
 */
public class DBConnection {
    public static Connection getConnection(){
        Connection con = null;
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            String url ="jdbc:sqlserver://localhost:1433;databaseName=DANGNHAP";
            String sqlSELECT = "SELECT * FROM Account";
            String user = "sa";
            String password = "1111";
            con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlSELECT);
            
            System.out.println("DBC.DBConnection.getConnection()");
      
        } catch (SQLException e) {
            
        }
        return con;
    }
    public static void closeConnection(Connection con){
        try {
            if(con!=null){
                con.close();
            }
        } catch (Exception e) {
        }
    }


}   
