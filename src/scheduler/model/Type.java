package scheduler.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Type {
  private final IntegerProperty count;
  private final StringProperty type;
  
  public Type(int count, String type) {
    this.count = new SimpleIntegerProperty(count);
    this.type = new SimpleStringProperty(type);
  }
    
  public int getCount(){
    return this.count.get();
  }

  public IntegerProperty countProperty() {
    return count;
  }

  public String getType(){
    return this.type.get();
  }
    
  public StringProperty typeProperty() {
    return type;
  }
}
