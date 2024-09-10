package domain;
import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
    Davit Dostourian Erbe 281665 & Diego Pereira Puig - 329028
*/

public class FileManager {
    
    public static void saveToFile(List<Foreman> foremen, List<Owner> owners, List<ConstructionSite> constructionSites, List<Category> categories, Map<String, Category> categoriesMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.ser"))) {
           oos.writeObject(foremen);
           oos.writeObject(owners);
           oos.writeObject(constructionSites);
           oos.writeObject(categories);
           oos.writeObject(categoriesMap);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }
        
    public static Object[] loadFromFile() {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.ser"))) {
                List<Foreman> foremen = (List<Foreman>) ois.readObject();
                List<Owner> owners = (List<Owner>) ois.readObject();
                List<ConstructionSite> constructionSites = (List<ConstructionSite>) ois.readObject();
                List<Category> categories = (List<Category>) ois.readObject();
                Map<String, Category> categoriesMap = (Map<String, Category>) ois.readObject();
                return new Object[] {foremen, owners, constructionSites, categories, categoriesMap};
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
    }
    
      /*   public static ArrayList<Foreman> loadForemenFromFile() {
        ArrayList<Foreman> foremen = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FOREMAN_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) { // Ensure all data is present
                    String name = data[0].trim();
                    String id = data[1].trim();
                    String address = data[2].trim();
                    int year = Integer.parseInt(data[3].trim());
                    Foreman foreman = new Foreman(name, id, address, year);
                    foremen.add(foreman);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading foremen file: " + e.getMessage());
        }
        return foremen;
    }

    public static ArrayList<Owner> loadOwnersFromFile() {
        ArrayList<Owner> owners = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(OWNER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) { // Ensure all data is present
                    String name = data[0].trim();
                    String id = data[1].trim();
                    String address = data[2].trim();
                    String cellphone = data[3].trim();
                    Owner owner = new Owner(name, id, address, cellphone);
                    owners.add(owner);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading owners file: " + e.getMessage());
        }
        return owners;
    }*/
}
