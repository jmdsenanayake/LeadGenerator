/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBOperations;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NDB
 */
public class DBActions {
    public int insertCriteriaDetails(String criteria){
        int generatedPK=0;
        Connection dBConnection = null;      
        try {
            dBConnection = new DBConnection().createConnection();
            String sql="insert into criteria (criteria,searchDate) values (?,?)";
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            PreparedStatement ps=dBConnection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, criteria);
            ps.setString(2, df.format(Calendar.getInstance().getTime()));
            ps.executeUpdate();            
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()){
             generatedPK=rs.getInt(1);
            }
        } catch (SQLException|ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        finally{
            try {
                dBConnection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return generatedPK;
    }
    
    public int insertLeadDetails(String leadName,int criteriaID){
        int generatedPK=0;
        Connection dBConnection = null;      
        try {
            dBConnection = new DBConnection().createConnection();
            String sql="insert into leadnames (leadname,criteria_id) values (?,?)";
         
            PreparedStatement ps=dBConnection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, leadName);
            ps.setInt(2, criteriaID);
             ps.executeUpdate();            
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()){
             generatedPK=rs.getInt(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        finally{
            try {
                dBConnection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return generatedPK;
    }
    
    public void insertContactNoDetails(String contactNo,int leadID){
       Connection dBConnection = null;      
        try {
            dBConnection = new DBConnection().createConnection();
            String sql="insert into contactNos (contactno,lead_id) values (?,?)";
         
            PreparedStatement ps=dBConnection.prepareStatement(sql);
            ps.setString(1, contactNo);
            ps.setInt(2, leadID);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }    
        finally{
            try {
                dBConnection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void insertEmailAddressDetails(String emailAddress,int leadID){
       
       Connection dBConnection = null;      
        try {
            dBConnection = new DBConnection().createConnection();
            String sql="insert into emailaddress (emailaddress,lead_id) values (?,?)";
         
            PreparedStatement ps=dBConnection.prepareStatement(sql);
            ps.setString(1, emailAddress);
            ps.setInt(2, leadID);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }    
        finally{
            try {
                dBConnection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void insertDesignationDetails(String designation,int leadID){
       
       Connection dBConnection = null;      
        try {
            dBConnection = new DBConnection().createConnection();
            String sql="insert into designations (designation,lead_id) values (?,?)";
         
            PreparedStatement ps=dBConnection.prepareStatement(sql);
            ps.setString(1, designation);
            ps.setInt(2, leadID);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }    
        finally{
            try {
                dBConnection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
