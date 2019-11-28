package scheduler.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
  private final IntegerProperty userID;
  private final StringProperty username;
  private final StringProperty password;
  
  public User(int userID, String username, String password) {
    this.userID = new SimpleIntegerProperty(userID);
    this.username = new SimpleStringProperty(username);
    this.password = new SimpleStringProperty(password);
  }
    
  public int getID(){
    return this.userID.get();
  }

  public IntegerProperty IDProperty() {
    return userID;
  }

  public void setUsername(String username){
    this.username.set(username);
  }

  public String getUsername(){
    return this.username.get();
  }
    
  public StringProperty usernameProperty() {
    return username;
  }

  public void setPassword(String password){
    this.password.set(password);
  }

  public String getPassword(){
    return this.password.get();
  }
    
  public StringProperty passwordProperty() {
    return password;
  }
}
