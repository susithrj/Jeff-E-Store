package sample;
import emailService.Email;
import emailService.IndividualEmail;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String password;
    private String address;



    public Customer(String email, String password) {
        this.email = email;
        this.password = password;

    }

    public Customer(String name, String email, String password, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;

    }

    public Customer(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getRegistered(Customer newCustomer) throws SQLException {

        Connection connection = DatabaseConnecter.connectToDatabase();
        Statement getMax = connection.createStatement();
        ResultSet resultSet = getMax.executeQuery("SELECT MAX(c_id) FROM customer");
        if (resultSet.next()) {
          newCustomer.id   = resultSet.getInt(1) + 1;//adding the next customer id by getting the max id
        }else {
            newCustomer.id = 1;//if it's the first data entry id will be one
        }


        String sqlCode = "INSERT into customer (c_id,c_email,c_name,c_password,c_address) " +
                "VALUES (?,?,?,?,?)";//sql code to add customer to database
        try {
                Connection conn = DatabaseConnecter.connectToDatabase();
                PreparedStatement stm = conn.prepareStatement(sqlCode, Statement.RETURN_GENERATED_KEYS);
                stm.setInt(1,newCustomer.id);//pushing values to the statement
                stm.setString(2,newCustomer.email);
                stm.setString(3,newCustomer.name);
                stm.setString(4,newCustomer.password);
                stm.setString(5,newCustomer.address);
                stm.executeUpdate();//execute sql code

        }catch (SQLIntegrityConstraintViolationException s){
            System.out.println("Already exist");//if primary key duplicates user already exist
            return false;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
        return true;


    }
    public boolean login(Customer activeCustomer){
        boolean isvalidCustomer;
        String sqlCode = "SELECT * FROM customer WHERE c_email = ?";// sql code
        ResultSet result = null;
        try {
            Connection conn = DatabaseConnecter.connectToDatabase();
            PreparedStatement stm = conn.prepareStatement(sqlCode);
            stm.setString(1,activeCustomer.email);
            result = stm.executeQuery();// getting data from database

            if(result.next()){
               if(activeCustomer.password.equals(result.getString("c_password"))){//if the password is correct against the database
                   activeCustomer.id= result.getInt("c_id");//other attributes will be added to the customer object
                   activeCustomer.name = result.getString("c_name");
                   activeCustomer.address= result.getString("c_address");
                   isvalidCustomer = true;
                   ShopGuiController.setActiveCustomer(activeCustomer);
                   System.out.println(activeCustomer.name);
               }else {
                   System.out.println("invalid user");
                   activeCustomer = null;
                   isvalidCustomer = false;
               }
            }else {
                activeCustomer = null;
                System.out.println("user not found");
                isvalidCustomer = false;

            }


        } catch (SQLException e) {
            System.err.println(e);
            isvalidCustomer = false;

        }




    return  isvalidCustomer;
    }


    private boolean order(Order customerOrder){
        try {
        Connection connection = DatabaseConnecter.connectToDatabase();
        Statement getMax = connection.createStatement();
        ResultSet resultSet = getMax.executeQuery("SELECT MAX(o_id) FROM order_");
        if (resultSet.next()) {
            customerOrder.setOrderId(resultSet.getInt(1) + 1);
        }else {
            customerOrder.setOrderId(1);
        }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }



        try {
            String sql = "INSERT into order_ (o_id,o_date,o_totalAmount,c_email) " +
                    "VALUES (?,?,?,?)";
            Connection conn = DatabaseConnecter.connectToDatabase();
            PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1,customerOrder.getOrderId());
            stm.setString(2,new SimpleDateFormat("dd/MM/yyyy").format(customerOrder.getOrderDate()));
            stm.setDouble(3,customerOrder.getTotalAmount());
            stm.setString(4,customerOrder.getCustomer().getEmail());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


        try {
            String sqlOrderItem = "INSERT into orderitem (o_id,i_code,i_size,i_quantity,cost) " +
                    "VALUES (?,?,?,?,?)";
            Connection conn = DatabaseConnecter.connectToDatabase();
            PreparedStatement stm = conn.prepareStatement(sqlOrderItem, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0;i < customerOrder.getItemlist().size() ;i++){
                stm.setInt(1,customerOrder.getOrderId());
                stm.setString(2,customerOrder.getItemlist().get(i).getItem().getItemCode());
                stm.setString(3,customerOrder.getItemlist().get(i).getSize());
                stm.setInt(4,customerOrder.getItemlist().get(i).getQuantity());
                stm.setDouble(5,customerOrder.getItemlist().get(i).getPrice());
                stm.addBatch();
            }
            stm.executeBatch();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean modifyAccountDetails(Customer modifyCustomer,String value,int option){
        String type;
        if(option == 1){               //execution method is different according to values selected
            type = "c_password = ?";
        }else if(option == 2){
            type = "c_email = ?";
        }else {
            type = "c_address = ?";
        }
        String sql = "UPDATE customer SET " + type + " WHERE c_email = ?";
        Connection connectdb = DatabaseConnecter.connectToDatabase();
        PreparedStatement statement = null;
        try {
            statement = connectdb.prepareStatement(sql);
            statement.setString(1,value);
            statement.setString(2,modifyCustomer.email);
            statement.executeUpdate();//updating the value in database
            if(option == 1){
                modifyCustomer.password = value;
            }else if(option == 2){                 //updating the value in current object
                modifyCustomer.email = value;
            }else {
                modifyCustomer.address = value;
            }
            return true;
        } catch (SQLIntegrityConstraintViolationException s){
            System.out.println("Already exist");
            return false;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }


    }
    public void makePayment(Customer activeCustomer){
       Order customerOrder = new Order(new Date(),activeCustomer,ShopGuiController.getTotalPrice(),ShopGuiController.getCart());

       if(activeCustomer.order(customerOrder)){
           String emailBody = customerOrder.generateOrderInvoice(customerOrder);//generating the bill
           Email email = new IndividualEmail("Jeff Stores",emailBody,activeCustomer.email);//creating an email object
           email.sendEmail();//sending the bill through an email
           System.out.println("payment done");
       }else{
           System.out.println("order fail");
       }
        ShopGuiController.getCart().clear();
        ShopGuiController.setTotalPrice(0);


    }
    public void emailInquiry(Customer activeCustomer,String body){
        Email inquiry = new IndividualEmail( "Inquiry from   " + activeCustomer.getName()+"("+activeCustomer.email+")",body);
        inquiry.sendEmail();// sending the inquiry through  an email

    }




}
