/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBOperations;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author NDB
 */
public class DBConnection {
    public Connection createConnection() throws ClassNotFoundException,SQLException{
    Class.forName("com.mysql.jdbc.Driver");
    Connection c=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/leadgeneratordb","root","");
    return c;
    }
}
