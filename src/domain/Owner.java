package domain;

import java.io.Serializable;
import java.util.Objects;

/*
    Davit Dostourian Erbe 281665 & Diego Pereira Puig - 329028
*/

public class Owner implements Serializable{
    private String name;
    private String id;
    private String address;
    private int cellphone;

    public Owner(String name, String id, String address, int cellphone) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.cellphone = cellphone;
    }

    // getter and setter for each attribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCellphone() {
        return cellphone;
    }

    public void setCellphone(int cellphone) {
        this.cellphone = cellphone;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return cellphone == owner.cellphone &&
               Objects.equals(name, owner.name) &&
               Objects.equals(id, owner.id) &&
               Objects.equals(address, owner.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, address, cellphone);
    }
    
    @Override
    public String toString() {
        return "Owner{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", cellphone='" + cellphone + '\'' +
                '}';
    }
    public static Owner fromString(String str) {
        // Remove "Owner{" and "}" from the string
        str = str.replace("Owner{", "").replace("}", "");

        // Split the string into parts based on ", "
        String[] parts = str.split(", ");

        // Initialize variables to store attributes
        String name = null;
        String id = null;
        String address = null;
        int cellphone = 0;

        // Iterate through parts to extract attributes
        for (String part : parts) {
            if (part.startsWith("name='")) {
                name = part.substring(6, part.length() - 1); // Extract substring after "name='"
            } else if (part.startsWith("id='")) {
                id = part.substring(4, part.length() - 1); // Extract substring after "id='"
            } else if (part.startsWith("address='")) {
                address = part.substring(9, part.length() - 1); // Extract substring after "address='"
            } else if (part.startsWith("cellphone=")) {
                cellphone = Integer.parseInt(part.substring(10)); // Extract substring after "cellphone=" and parse to int
            }
        }

        // Create and return a new Owner object
        return new Owner(name, id, address, cellphone);
    }
}
