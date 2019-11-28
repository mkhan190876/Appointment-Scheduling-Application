package scheduler.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {
  private final IntegerProperty customerID;
  private final StringProperty name;
  private final IntegerProperty addressID;
  private final StringProperty address;
  private final StringProperty address2;
  private final StringProperty city;
  private final StringProperty postalCode;
  private final StringProperty phone;
  private final StringProperty country;

  public Customer(int customerID, String name, int addressID, String address, String address2, String city, String postalCode, String phone, String country) {
    this.customerID = new SimpleIntegerProperty(customerID);
    this.name = new SimpleStringProperty(name);
    this.addressID = new SimpleIntegerProperty(addressID);
    this.address = new SimpleStringProperty(address);
    this.address2 = new SimpleStringProperty(address2);
    this.city = new SimpleStringProperty(city);
    this.postalCode = new SimpleStringProperty(postalCode);
    this.phone = new SimpleStringProperty(phone);
    this.country = new SimpleStringProperty(country);
  }
    
  public int getID(){
    return this.customerID.get();
  }

  public IntegerProperty IDProperty() {
    return customerID;
  }

  public void setName(String name){
    this.name.set(name);
  }

  public String getName(){
    return this.name.get();
  }
    
  public StringProperty nameProperty() {
    return name;
  }

  public void setAddressID(int addressID){
    this.addressID.set(addressID);
  }

  public int getAddressID(){
    return this.addressID.get();
  }
    
  public IntegerProperty addressIDProperty() {
    return addressID;
  }
  
  public void setAddress(String address){
    this.address.set(address);
  }

  public String getAddress(){
    return this.address.get();
  }
    
  public StringProperty addressProperty() {
    return address;
  }
  
  public void setAddress2(String address2){
    this.address2.set(address2);
  }

  public String getAddress2(){
    return this.address2.get();
  }
    
  public StringProperty address2Property() {
    return address2;
  }
  
  public void setCity(String city){
    this.city.set(city);
  }

  public String getCity(){
    return this.city.get();
  }
    
  public StringProperty cityProperty() {
    return city;
  }
  
  public void setPostalCode(String postalCode){
    this.postalCode.set(postalCode);
  }

  public String getPostalCode(){
    return this.postalCode.get();
  }
    
  public StringProperty postalCodeProperty() {
    return postalCode;
  }

  public void setPhone(String phone){
    this.phone.set(phone);
  }

  public String getPhone(){
    return this.phone.get();
  }
    
  public StringProperty phoneProperty() {
    return phone;
  }
  
  public void setCountry(String country){
    this.country.set(country);
  }

  public String getCountry(){
    return this.country.get();
  }
    
  public StringProperty countryProperty() {
    return country;
  }
}
