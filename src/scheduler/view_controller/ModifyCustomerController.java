package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.Scheduler;
import scheduler.model.AlertDialog;
import scheduler.model.Customer;

public class ModifyCustomerController {

  private Stage stage;
  private Connection connection;
  private String customerID;
  private String addressID;
  private String userName = "";

  @FXML
  private TextField nameTextField;

  @FXML
  private TextField phoneTextField;
  
  @FXML
  private TextField addressTextField;
  
  @FXML
  private TextField address2TextField;
  
  @FXML
  private TextField cityTextField;
  
  @FXML
  private TextField countryTextField;
  
  @FXML
  private TextField postalCodeTextField;
  
  @FXML
  private void handleCancelButton() {
    stage.close();
  }
  
  private void handleCustomer(boolean newCustomer){
    PreparedStatement preparedStatement;
    try {
      if(newCustomer){
        int countryID = handleCountry();
        int cityID = handleCity(countryID);
        handleAddress(cityID, false);
        preparedStatement = connection.prepareStatement(
        "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy) " +
        "VALUES (?, ?, 1, CURDATE(), ?, ?)");
      }else{
        int countryID = handleCountry();
        int cityID = handleCity(countryID);
        handleAddress(cityID, true);
        preparedStatement = connection.prepareStatement(
        "UPDATE customer " +
        "SET customerName=?, addressId=?, lastUpdateBy=? " +
        "WHERE customerid = ?");
      }
      preparedStatement.setString(1, nameTextField.getText());
      preparedStatement.setString(2, addressID);
      preparedStatement.setString(3, this.userName);
      if(newCustomer){
        preparedStatement.setString(4, this.userName);
      }else{
        preparedStatement.setString(4, customerID);
      }
      preparedStatement.execute();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    stage.close();
  }
  
  private int handleCountry() throws SQLException{
    String country = countryTextField.getText();
    String query =  
    "INSERT INTO country (country, createDate, createdBy, lastUpdateBy) " +
    "SELECT ?, CURDATE(), ?, ? FROM country " +
    "WHERE NOT EXISTS ( " +
    "  SELECT country FROM country WHERE country=? " +
    ") LIMIT 1;";
 
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setString(1, country);
    preparedStatement.setString(2, this.userName);
    preparedStatement.setString(3, this.userName);
    preparedStatement.setString(4, country);
    preparedStatement.execute();

    query = "SELECT countryid FROM country WHERE country=?";
    preparedStatement = connection.prepareStatement(query);
    preparedStatement.setString(1, country);
    
    ResultSet resultSet = preparedStatement.executeQuery();
    int countryID = 0;
    if (resultSet.next()) {
      countryID = resultSet.getInt(1);
    }
    return countryID;
  }
  
  private int handleCity(int countryID) throws SQLException{
    String city = cityTextField.getText();
    String query =  
    "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdateBy) " +
    "SELECT ?, ?, CURDATE(), ?, ? FROM city " +
    "WHERE NOT EXISTS ( " +
      "SELECT city FROM city WHERE city=? " +
    ") LIMIT 1;";
 
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setString(1, city);
    preparedStatement.setString(2, Integer.toString(countryID));
    preparedStatement.setString(3, this.userName);
    preparedStatement.setString(4, this.userName);
    preparedStatement.setString(5, city);
    preparedStatement.execute();

    query = "SELECT cityid FROM city WHERE city=?";
    preparedStatement = connection.prepareStatement(query);
    preparedStatement.setString(1, city);
    
    ResultSet resultSet = preparedStatement.executeQuery();
    int cityID = 0;
    if (resultSet.next()) {
      cityID = resultSet.getInt(1);
    }
    return cityID;
  }
  
  private void handleAddress(int cityID, boolean update) throws SQLException{
    String address = addressTextField.getText();
    String query;
    if(update){
      query = 
      "UPDATE address " +
      "SET address=?, address2=?, cityId=?, postalCode=?, phone=?, lastUpdateBy=?" +
      "WHERE addressid=?;";
    }else{
      query =  
      "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) " +
      "SELECT ?, ?, ?, ?, ?, CURDATE(), ?, ? FROM address " +
      "WHERE NOT EXISTS ( " +
        "SELECT address FROM address WHERE address=? " +
      ") LIMIT 1;";
    }
 
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setString(1, address);
    preparedStatement.setString(2, address2TextField.getText());
    preparedStatement.setString(3, Integer.toString(cityID));
    preparedStatement.setString(4, postalCodeTextField.getText());
    preparedStatement.setString(5, phoneTextField.getText());
    preparedStatement.setString(6, this.userName);
    if(update){
      preparedStatement.setString(7, addressID);
    }else{
      preparedStatement.setString(7, this.userName);
      preparedStatement.setString(8, address);
    }
    preparedStatement.execute();

    if(!update){
      query = "SELECT addressid FROM address WHERE address=?";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, address);

      ResultSet resultSet = preparedStatement.executeQuery();
      int id = 0;
      if (resultSet.next()) {
        id = resultSet.getInt(1);
      }
      addressID = Integer.toString(id);
    }
  }

  @FXML
  private void handleSaveButton() {
    if(isInputValid()){
      if(this.customerID == null){
        handleCustomer(true);
      }else{
        handleCustomer(false);
      }
    }
  }
  
  private boolean isInputValid() {
    String errorMessage = "";

    if (nameTextField.getText() == null || nameTextField.getText().length() == 0) {
      errorMessage += "No valid name!\n"; 
    }
    
    if (countryTextField.getText() == null || countryTextField.getText().length() == 0) {
      errorMessage += "No valid country!\n"; 
    }
    
    if (cityTextField.getText() == null || countryTextField.getText().length() == 0) {
      errorMessage += "No valid city!\n"; 
    }
    
    if (addressTextField.getText() == null || addressTextField.getText().length() == 0) {
      errorMessage += "No valid address!\n"; 
    }
    
    if (errorMessage.length() == 0) {
      return true;
    } else {
      AlertDialog.errorDialog(errorMessage);
      return false;
    }
  }
  
  public ResultSet getDataFromDataBase(String query) throws ClassNotFoundException {
    ResultSet resultSet = null;
    Statement statement;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return resultSet;
  }
  
  public void setConnection(Connection connection) {
    this.connection = connection;
  }
  
  public void setCustomer(Customer customer) {
    if(customer == null){
      this.customerID = null;
      this.addressID = null;
      nameTextField.setText("");
      phoneTextField.setText("");
      addressTextField.setText("");
      address2TextField.setText("");
      cityTextField.setText("");
      countryTextField.setText("");
      postalCodeTextField.setText("");
    }else{
      this.customerID = Integer.toString(customer.getID());
      this.addressID = Integer.toString(customer.getAddressID());
      nameTextField.setText(customer.getName());
      phoneTextField.setText(customer.getPhone());
      addressTextField.setText(customer.getAddress());
      address2TextField.setText(customer.getAddress2());
      cityTextField.setText(customer.getCity());
      countryTextField.setText(customer.getCountry());
      postalCodeTextField.setText(customer.getPostalCode());
    }
  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public void setUserName(String userName) {
    this.userName = userName;
  }
  
  public static void showDialog(Stage primaryStage, Connection connection, Customer customer, String title, String userName) throws IOException, ClassNotFoundException{
    
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Scheduler.class.getResource("view_controller/ModifyCustomer.fxml"));
    AnchorPane page = (AnchorPane) loader.load();

    Stage stage = new Stage();
    stage.setTitle(title);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(primaryStage);
    Scene scene = new Scene(page);
    stage.setScene(scene);
    
    ModifyCustomerController modifyCustomerController = loader.getController();
    modifyCustomerController.setStage(stage);
    modifyCustomerController.setConnection(connection);
    modifyCustomerController.setUserName(userName);
    
    if(customer != null){
      modifyCustomerController.setCustomer(customer);
    }else{
      modifyCustomerController.setCustomer(null);
    }
    
    stage.showAndWait();
  }
}
