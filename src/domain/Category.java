package domain;

import java.io.Serializable;
import java.util.Objects;

/*
    Davit Dostourian Erbe 281665 & Diego Pereira Puig - 329028
*/

public class Category implements Serializable {
    // class atributes
  private String name;
  private String description;
  private double categorySelectedBudget;

  // Constructor to initialize atributes
  public Category(String name, String description, double budget) {
      this.name = name;
      this.description = description;
      this.categorySelectedBudget = budget;
  }

  // getter and setter for each atribute.
  public String getName() {
      return name;
  }

  public void setName(String name) {
      this.name = name;
  }

  public String getDescription() {
      return description;
  }

  public void setDescription(String description) {
      this.description = description;
  }
  
  public void setCategorySelectedBudget(double budget){
      this.categorySelectedBudget = budget;
  }
  
public double getCategorySelectedBudget() {
  return this.categorySelectedBudget;
}
  
  @Override
    public String toString() {
        return "Category{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", categorySelectedBudget='" + categorySelectedBudget + '\'' +
            '}';
    }
    
  public static Category fromString(String str) {
  String name = str.split("name='")[1].split("'")[0];
  String description = str.split("description='")[1].split("'")[0];
  double budget = Double.parseDouble(str.split("categorySelectedBudget='")[1].split("'")[0]); // Extract and parse budget
  return new Category(name, description, budget);
}
   @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return this.getName().equals(category.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}

