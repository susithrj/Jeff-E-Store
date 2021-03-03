package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class  StaffMember extends CompanyAdministrator{

 public StaffMember(String email, String password) {
  super(email, password);
 }

 @Override
 public boolean login(CompanyAdministrator activeAdmin) {
  boolean isValidMember;
  String sqlCode = "SELECT * FROM staffmember WHERE s_email = ?";
  ResultSet result = null;
  try {
   Connection conn = DatabaseConnecter.connectToDatabase();
   PreparedStatement stm = conn.prepareStatement(sqlCode);
   stm.setString(1,activeAdmin.getEmail());
   result = stm.executeQuery();

   if(result.next()){
    if(activeAdmin.getPassword().equals(result.getString("s_password"))){
     activeAdmin.setName(result.getString("s_name"));
     isValidMember = true;
     System.out.println(activeAdmin.getEmail());
    }else {
     System.out.println("invalid user");
     activeAdmin = null;
     isValidMember = false;
    }
   }else {
    activeAdmin = null;
    System.out.println("user not found");
    isValidMember = false;

   }


  } catch (SQLException e) {
   System.err.println(e);
   isValidMember = false;

  }




  return  isValidMember;
 }


 public String[] checkOrder(int searchValue){
  String sqlCode = "SELECT * FROM order_ WHERE o_id = ?";
  ResultSet result = null;
  try {
   Connection conn = DatabaseConnecter.connectToDatabase();
   PreparedStatement stm = conn.prepareStatement(sqlCode);
   stm.setInt(1,searchValue);
   result = stm.executeQuery();

   if(result.next()){
     String values[] = {String.valueOf(result.getInt(1)),result.getString(2),result.getString(4),
             String.valueOf(result.getDouble(3))};
     return values;

   }else {
     return null;
   }


  } catch (SQLException e) {
    System.err.println(e);
    return null;


  }

 }
}

