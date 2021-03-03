package sample;


import emailService.Email;
import emailService.GroupEmail;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ShopGuiController implements Initializable {
  @FXML private   Label id;
  @FXML private   Label name;
  @FXML  private Label total;
  @FXML private ImageView imageOne;
  @FXML private ImageView imageTwo;
  @FXML private ImageView imageThree;
  @FXML private ImageView imageFour;
  @FXML private ImageView imageFive;
  @FXML private ImageView imageSix;
  @FXML private  RadioButton itemOne;
  @FXML private  RadioButton itemTwo;
  @FXML private  RadioButton itemThree;
  @FXML private  RadioButton itemFour;
  @FXML private  RadioButton itemFive;
  @FXML private  RadioButton itemSix;
  @FXML private ImageView mainImage;
  @FXML private Label itemName;
  @FXML private TextField quantity;
  @FXML private ComboBox<String> sizeOptions = new ComboBox<>();
  @FXML private TableView tableView;
  @FXML private TableColumn c_name;
  @FXML private TableColumn c_cost;
  @FXML private TableColumn c_quantity;
  @FXML private TableColumn c_size;
  private static double totalPrice = 0;
  private ToggleGroup group = new ToggleGroup();
  private ObservableList<String> standard = FXCollections.observableArrayList("S", "M", "L");
  private ObservableList<String> weight = FXCollections.observableArrayList("100g", "200g", "350g");
  private ObservableList<String> length1 = FXCollections.observableArrayList("100m", "200m", "300m");
  private ObservableList<String> length2 = FXCollections.observableArrayList("1m", "2m", "3m");
  private ObservableList<String> number = FXCollections.observableArrayList("1","2","3","4","5", "6");
  private int itemNumber;
  private static Customer activeCustomer;
  private static ArrayList<Item> list = new ArrayList<>();
  private static ArrayList<OrderItem> cart = new ArrayList<>();


    public static void setTotalPrice(double totalPrice) {
        ShopGuiController.totalPrice = totalPrice;
    }

    public static ArrayList<OrderItem> getCart() {
        return cart;
    }

    public static void setActiveCustomer(Customer activeCustomer) {
    ShopGuiController.activeCustomer = activeCustomer;

  }

    public static double getTotalPrice() {
        return totalPrice;
    }

    @FXML private void modifyingProcess() throws IOException {
     ModifyDetailsController.setActiveCustomer(activeCustomer);
     FXMLLoader fxmlLoader = new FXMLLoader();
     fxmlLoader.setLocation(getClass().getResource("modifyDetails.fxml"));
     Scene scene = new Scene(fxmlLoader.load(), 650, 400);
     Stage stage = new Stage();
     stage.setTitle("Change Details");
     stage.setScene(scene);
     stage.show();

 }
 private void displayCart(){
      ObservableList<OrderItem> cartList = FXCollections.observableArrayList();
     for(int i = 0;i < cart.size();i++){
        cartList.add(cart.get(i)) ;
     }
     tableView.setItems(cartList);
     c_quantity.setCellValueFactory(new PropertyValueFactory<OrderItem,Integer>("quantity"));
     c_size.setCellValueFactory(new  PropertyValueFactory<OrderItem,String>("size"));
     c_cost.setCellValueFactory((new PropertyValueFactory<OrderItem,Double>("price")));
     c_name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderItem,String>, ObservableValue<String>>() {
         @Override
         public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderItem,String> param) {
             return new SimpleStringProperty(param.getValue().getItem().getItemName());
         }
     });
     total.setText(String.valueOf(totalPrice));


 }
 @FXML private void logoutProcess() throws IOException {
     FXMLLoader fxmlLoader = new FXMLLoader();
     fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
     Scene scene = new Scene(fxmlLoader.load(), 650, 400);
     Stage stage = new Stage();
     stage.setTitle("Welcome To Jeff's Store");
     stage.setScene(scene);
     stage.show();
     activeCustomer = null;
     id.getScene().getWindow().hide();


 }
 @FXML private void addToCartProcess(){
        try{
            int valueSize = Integer.parseInt(quantity.getText());
            OrderItem newItem  = new OrderItem(list.get(itemNumber),valueSize,sizeOptions.getSelectionModel().getSelectedItem());
            newItem.setPrice(newItem.calculateItemPrice(newItem));
            cart.add(newItem);
            totalPrice = totalPrice + newItem.calculateItemPrice(newItem);
            displayCart();
            System.out.println("added to cart");
        }catch (NumberFormatException  | java.lang.NullPointerException e ){//if quantity entered wrong or dropdown not selected display error message
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Error");
            error.setContentText("Please select the size and Quantity correctly");
            error.showAndWait();

        }



 }
 @FXML private void openEmailWindow() throws IOException {
     EmailInquiryController.setActiveCustomer(activeCustomer);
     FXMLLoader fxmlLoader = new FXMLLoader();
     fxmlLoader.setLocation(getClass().getResource("EmailInquiry.fxml"));
     Scene scene = new Scene(fxmlLoader.load(), 650, 400);
     Stage stage = new Stage();
     stage.setTitle("Email Inquiry");
     stage.setScene(scene);
     stage.show();
 }
 @FXML private void clearCart(){
      cart.clear();
      totalPrice = 0;
      displayCart();
 }
 @FXML private void select(){
     itemNumber = (int)group.selectedToggleProperty().getValue().getUserData();
     mainImage.setImage(list.get(itemNumber).getImage());
     itemName.setText(list.get(itemNumber).getItemName());
     String type = list.get(itemNumber).getSizeType();
     if (type.equals("number")){
         sizeOptions.setItems(number); //setting the values in dropdown according to Item
     }else if(type.equals("length1")){
         sizeOptions.setItems(length1);
     }else if(type.equals("weight")){
         sizeOptions.setItems(weight);
      }else if(type.equals("stadard")){
         sizeOptions.setItems(standard);
     }else {
         sizeOptions.setItems(length2);
     }



 }
  @FXML private void paymentProcess() throws IOException {
      PaymentController.setPaymentCustomer(activeCustomer);
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("Payment.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 358, 400);
      Stage stage = new Stage();
      stage.setTitle("Payment");
      stage.setScene(scene);
      stage.show();

  }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      id.setText(Integer.toString(activeCustomer.getId()));                //initializing variables on window startup
      name.setText(activeCustomer.getName());
      tableView.setPlaceholder(new Label("No Items Selected"));
      ResultSet result = DatabaseConnecter.getData("item","*");
      if(result != null){
          try{
              for(int i = 0;result.next();i++) {//if data is not empty adding to list
                  list.add(i,new Item(result.getString(1),result.getString(2)
                     ,result.getDouble(3),new Image(new File("src\\images\\" + result.getString(4)).toURI().toString()),result.getString(5)));
              }

          }catch (SQLException e){
              System.out.println("Database Error");
          }

      }
      setData();
      itemOne.setToggleGroup(group);
      itemOne.setUserData(0);
      itemOne.setToggleGroup(group);
      itemTwo.setUserData(1);
      itemTwo.setToggleGroup(group);
      itemThree.setUserData(2);
      itemThree.setToggleGroup(group);
      itemFour.setUserData(3);
      itemFour.setToggleGroup(group);
      itemFive.setToggleGroup(group);
      itemFive.setUserData(4);
      itemSix.setToggleGroup(group);
      itemSix.setUserData(5);

    }
    private void setData(){
        imageOne.setImage(list.get(0).getImage());
        imageTwo.setImage(list.get(1).getImage());
        imageThree.setImage(list.get(2).getImage());
        imageFour.setImage(list.get(3).getImage());
        imageFive.setImage(list.get(4).getImage());
        imageSix.setImage(list.get(5).getImage());
        itemOne.setText(list.get(0).getItemName());
        itemTwo.setText(list.get(1).getItemName());
        itemThree.setText(list.get(2).getItemName());
        itemFour.setText(list.get(3).getItemName());
        itemFive.setText(list.get(4).getItemName());
        itemSix.setText(list.get(5).getItemName());
    }
}
