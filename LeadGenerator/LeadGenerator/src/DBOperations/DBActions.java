/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBOperations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author NDB
 */
public class DBActions {

    public int insertCriteriaDetails(String criteria) {
        int generatedPK = 0;
        Connection dBConnection = null;
        try {
            dBConnection = new DBConnection().createConnection();
            String sql = "insert into criteria (criteria,searchDate) values (?,?)";
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            PreparedStatement ps = dBConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, criteria);
            ps.setString(2, df.format(Calendar.getInstance().getTime()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedPK = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                dBConnection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return generatedPK;
    }

    public int insertLeadDetails(String leadName, int criteriaID) {
        int generatedPK = 0;
        Connection dBConnection = null;
        try {
            dBConnection = new DBConnection().createConnection();
            String sql = "insert into leadnames (leadname,criteria_id) values (?,?)";

            PreparedStatement ps = dBConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, leadName);
            ps.setInt(2, criteriaID);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedPK = rs.getInt(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                dBConnection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return generatedPK;
    }

    public void insertContactNoDetails(String contactNo, int leadID) {
        Connection dBConnection = null;
        try {
            dBConnection = new DBConnection().createConnection();
            String sql = "insert into contactNos (contactno,lead_id) values (?,?)";

            PreparedStatement ps = dBConnection.prepareStatement(sql);
            ps.setString(1, contactNo);
            ps.setInt(2, leadID);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                dBConnection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void insertEmailAddressDetails(String emailAddress, int leadID) {

        Connection dBConnection = null;
        try {
            dBConnection = new DBConnection().createConnection();
            String sql = "insert into emailaddress (emailaddress,lead_id) values (?,?)";

            PreparedStatement ps = dBConnection.prepareStatement(sql);
            ps.setString(1, emailAddress);
            ps.setInt(2, leadID);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                dBConnection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void insertDesignationDetails(String designation, int leadID) {

        Connection dBConnection = null;
        try {
            dBConnection = new DBConnection().createConnection();
            String sql = "insert into designations (designation,lead_id) values (?,?)";

            PreparedStatement ps = dBConnection.prepareStatement(sql);
            ps.setString(1, designation);
            ps.setInt(2, leadID);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                dBConnection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void convertToCSV(ResultSet rs, String filename) throws SQLException, FileNotFoundException {
        PrintWriter csvWriter = new PrintWriter(new File(filename + ".csv"));
        ResultSetMetaData meta = rs.getMetaData();
        int numberOfColumns = meta.getColumnCount();
        String dataHeaders = "\"" + meta.getColumnName(1) + "\"";
        for (int i = 2; i < numberOfColumns + 1; i++) {
            dataHeaders += ",\"" + meta.getColumnName(i).replaceAll("\"", "\\\"") + "\"";
        }
        csvWriter.println(dataHeaders);
        while (rs.next()) {
            String row = "\"" + rs.getString(1).replaceAll("\"", "\\\"") + "\"";
            for (int i = 2; i < numberOfColumns + 1; i++) {
                row += ",\"" + rs.getString(i).replaceAll("\"", "\\\"") + "\"";
            }
            csvWriter.println(row);
        }
        csvWriter.close();

        JOptionPane.showMessageDialog(null, "File dowmloaded to " + System.getProperty("user.dir") + "\\" + filename + ".csv", "Lead Generator", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void populateFinalOutputTable(int criteria_id) {
        Connection dBConnection = null;
        try {
            dBConnection = new DBConnection().createConnection();           

            ArrayList<String> criteria_idList = new ArrayList<>();
            criteria_idList.add(criteria_id + "");
            Object[] criteria_idListArray = criteria_idList.toArray();

            ArrayList<String> criteriaList = new ArrayList<>();

            String sql1 = "select criteria from criteria where criteria_id=?";
            PreparedStatement ps1 = dBConnection.prepareStatement(sql1);
            ps1.setInt(1, criteria_id);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                criteriaList.add(rs1.getString("criteria"));
            }

            Object[] criteriaListArray = criteriaList.toArray();

            ArrayList<String> leadIDs = new ArrayList<>();
            ArrayList<String> leadnames = new ArrayList<>();

            String sql2 = "select lead_id,leadname from leadnames where criteria_id=?";
            PreparedStatement ps2 = dBConnection.prepareStatement(sql2);
            ps2.setInt(1, criteria_id);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                leadIDs.add(rs2.getString("lead_id"));
                leadnames.add(rs2.getString("leadname"));
            }

            Object[] leadIDsArray = leadIDs.toArray();
            Object[] leadnamesArray = leadnames.toArray();

            for (int i = 0; i < leadIDsArray.length; i++) {

                ArrayList<String> designations = new ArrayList<>();
                String sql3 = "select designation from designations where lead_id=?";
                PreparedStatement ps3 = dBConnection.prepareStatement(sql3);
                ps3.setInt(1, Integer.parseInt(leadIDsArray[i].toString()));
                ResultSet rs3 = ps3.executeQuery();

                while (rs3.next()) {
                    designations.add(rs3.getString("designation"));
                }
                Object[] designationsArray = designations.toArray();

                ArrayList<String> emailAddress = new ArrayList<>();
                String sql4 = "select emailaddress from emailaddress where lead_id=?";
                PreparedStatement ps4 = dBConnection.prepareStatement(sql4);
                ps4.setInt(1, Integer.parseInt(leadIDsArray[i].toString()));
                ResultSet rs4 = ps4.executeQuery();

                while (rs4.next()) {
                    emailAddress.add(rs4.getString("emailaddress"));
                }
                Object[] emailAddressArray = emailAddress.toArray();

                ArrayList<String> contactNos = new ArrayList<>();
                String sql5 = "select contactno from contactnos where lead_id=?";
                PreparedStatement ps5 = dBConnection.prepareStatement(sql5);
                ps5.setInt(1, Integer.parseInt(leadIDsArray[i].toString()));
                ResultSet rs5 = ps5.executeQuery();

                while (rs5.next()) {
                    contactNos.add(rs5.getString("contactno"));
                }
                Object[] contactNosArray = contactNos.toArray();

                int recordCountMax = Math.max(designationsArray.length, Math.max(emailAddressArray.length, contactNosArray.length));
                recordCountMax = recordCountMax > 0 ? recordCountMax : 1;
                for (int j = 0; j < recordCountMax; j++) {

                    String criteria_id_TBE = (i == 0) ? ((j == 0) ? criteria_idListArray[0].toString() : "") : "";
                    String criteria_TBE = (i == 0) ? ((j == 0) ? criteriaListArray[0].toString() : "") : "";
                    String leadname_TBE = (j == 0) ? leadnamesArray[i].toString() : "";
                    String designation_TBE = designationsArray.length > j ? designationsArray[j].toString() : "";
                    String emailAddress_TBE = emailAddressArray.length > j ? emailAddressArray[j].toString() : "";
                    String contactNos_TBE = contactNosArray.length > j ? contactNosArray[j].toString() : "";

                    String sql6 = "insert into finaloutput (criteria_id,criteria,leadname,designation,emailAddress,contactNo) values (?,?,?,?,?,?)";

                    PreparedStatement ps6 = dBConnection.prepareStatement(sql6);
                    ps6.setString(1, criteria_id_TBE);
                    ps6.setString(2, criteria_TBE);
                    ps6.setString(3, leadname_TBE);
                    ps6.setString(4, designation_TBE);
                    ps6.setString(5, emailAddress_TBE);
                    ps6.setString(6, contactNos_TBE);
                    ps6.executeUpdate();
                }

                String sql7 = "insert into finaloutput (criteria_id,criteria,leadname,designation,emailAddress,contactNo) values (?,?,?,?,?,?)";

                PreparedStatement ps7 = dBConnection.prepareStatement(sql7);
                ps7.setString(1, "");
                ps7.setString(2, "");
                ps7.setString(3, "");
                ps7.setString(4, "");
                ps7.setString(5, "");
                ps7.setString(6, "");
                ps7.executeUpdate();

            }

            //String sql7 = "select criteria,leadname,designation,emailAddress,contactNo from finalOutput where criteria_id=?";
            String sql7 = "select criteria,leadname,designation,emailAddress,contactNo from finaloutput where record_id between (SELECT record_id FROM finaloutput where criteria_id=?) and (select record_id from finalOutput order by record_id desc limit 1)";
            PreparedStatement ps7 = dBConnection.prepareStatement(sql7);
            ps7.setInt(1, criteria_id);
            ResultSet rs7 = ps7.executeQuery();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            convertToCSV(rs7, "FinalOutput_" + df.format(cal.getTime()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        populateFinalOutputTable(74);
    }

}
