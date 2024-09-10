package domain;
import java.io.Serializable;
import java.util.Objects;

/*
    Davit Dostourian Erbe 281665 & Diego Pereira Puig - 329028
*/

public class Foreman implements Serializable{
    private String name;
    private String id;
    private String address;
    private int year;

    public Foreman(String name, String id, String address, int year) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Foreman foreman = (Foreman) o;
        return year == foreman.year &&
               Objects.equals(name, foreman.name) &&
               Objects.equals(id, foreman.id) &&
               Objects.equals(address, foreman.address);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, id, address, year);
    }  
    
    @Override
    public String toString() {
        return "Foreman{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", year=" + year +
                '}';
    }
    public static Foreman fromString(String str) {
        String name = str.split("name='")[1].split("'")[0];
        String id = str.split("id='")[1].split("'")[0];
        String address = str.split("address='")[1].split("'")[0];
        int year = Integer.parseInt(str.split("year=")[1].split("}")[0]);
        return new Foreman(name, id, address, year);
    }
    
}
