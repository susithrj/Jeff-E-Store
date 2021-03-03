package sample;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Owner extends CompanyAdministrator {
    public Owner(String email, String password) {
        super(email, password);
    }

    @Override
    public boolean login(CompanyAdministrator activeAdmin) {
        boolean isValidOwner;
        String sqlCode = "SELECT * FROM owner WHERE o_email = ?";
        ResultSet result = null;
        try {
            Connection conn = DatabaseConnecter.connectToDatabase();
            PreparedStatement stm = conn.prepareStatement(sqlCode);
            stm.setString(1,activeAdmin.getEmail());
            result = stm.executeQuery();

            if(result.next()){
                if(activeAdmin.getPassword().equals(result.getString("o_password"))){
                    activeAdmin.setName(result.getString("o_name"));
                    isValidOwner = true;
                    System.out.println(activeAdmin.getEmail());
                }else {
                    System.out.println("invalid user");
                    activeAdmin = null;
                    isValidOwner = false;
                }
            }else {
                activeAdmin = null;
                System.out.println("user not found");
                isValidOwner = false;

            }


        } catch (SQLException e) {
            System.err.println(e);
            isValidOwner = false;

        }




        return  isValidOwner;
    }


    public boolean viewLog(String date, ArrayList<Order> ordersList){
        String sqlCode = "SELECT * FROM order_ WHERE o_date = ?";
        ResultSet result;
        try {
            Connection conn = DatabaseConnecter.connectToDatabase();
            PreparedStatement stm = conn.prepareStatement(sqlCode);
            stm.setString(1,date);
            result = stm.executeQuery();
            if (result !=null) {
                for (int i = 0; result.next(); i++) { //if data is not empty in the database add data to list
                    try {
                        ordersList.add(i, new Order(result.getInt(1),new SimpleDateFormat("dd/MM/yyyy").parse(result.getString(2)),
                                new Customer(result.getString(4)), result.getDouble(3)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean getCustomerReport(String email,ArrayList<Order> ordersList){
        String sqlCode = "SELECT * FROM order_ WHERE c_email = ?";
        ResultSet result;
        try {
            Connection conn = DatabaseConnecter.connectToDatabase();
            PreparedStatement stm = conn.prepareStatement(sqlCode);
            stm.setString(1,email);
            result = stm.executeQuery();
            if (result !=null) {
                for (int i = 0; result.next(); i++) {  //if data is not empty in the database add data to list
                    try {
                        ordersList.add(i, new Order(result.getInt(1),new SimpleDateFormat("dd/MM/yyyy").parse(result.getString(2)),
                                new Customer(result.getString(4)), result.getDouble(3)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean addStaffMember(StaffMember staffMember){
        String sqlCode = "INSERT into staffmember (s_email,s_name,s_password) " +
                "VALUES (?,?,?)";
        try {
            Connection conn = DatabaseConnecter.connectToDatabase();
            PreparedStatement stm = conn.prepareStatement(sqlCode, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1,staffMember.getEmail());
            stm.setString(2,staffMember.getName());
            stm.setString(3,staffMember.getPassword());
            stm.executeUpdate();//executing code

        }catch (SQLIntegrityConstraintViolationException s){
            System.out.println("Already exist");
            return false;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
        return true;

    }

}
