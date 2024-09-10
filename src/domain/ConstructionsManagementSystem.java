package domain;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import javax.swing.JOptionPane;

/*
    Davit Dostourian Erbe 281665 & Diego Pereira Puig - 329028
*/

public class ConstructionsManagementSystem extends Observable{
   // Atributos de la clase
   private List<Foreman> foremen;
   private List<Owner> owners;
   private List<ConstructionSite> constructionSites;
   private List<Category> categories;
   private Map<String, Category> categoriesMap;
   

   // Constructor para inicializar los atributos
   public ConstructionsManagementSystem() {
       this.foremen = new ArrayList<>();
       this.owners = new ArrayList<>();
       this.constructionSites = new ArrayList<>();
       this.categories = new ArrayList<>();
       this.categoriesMap = new HashMap<>();
       
       Runtime.getRuntime().addShutdownHook(new Thread(() -> {
           FileManager.saveToFile(foremen, owners, constructionSites, categories, categoriesMap);
           System.out.println("Data saved to data.ser on shutdown.");
       }));
   }
   
   // Métodos para registrar/modificar rubro
   public void registerCategory(String name, String description) {
  if (!categoriesMap.containsKey(name)) {
    Category newCategory = new Category(name, description, 0.0); // Set initial budget to 0.0
    categories.add(newCategory);
    categoriesMap.put(name, newCategory);
    somethingChanged();
       } else {
           Category existingCategory = categoriesMap.get(name);
           existingCategory.setDescription(description);
       }
   }
    // Method to import data for a new construction site
    public void importDataConstructionSite(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            String[] parts = line.split("#");

            String foremanId = parts[0];
            String ownerId = parts[1];
            String address = parts[2];
            int startMonth = Integer.parseInt(parts[3]);
            int startYear = Integer.parseInt(parts[4]);
            String permitNumber = parts[5];

            // Check if permit number is unique
            for (ConstructionSite site : constructionSites) {
                if (site.getPermitNumber().equals(permitNumber)) {
                    JOptionPane.showMessageDialog(null, "Permit number already exists. Import aborted.");
                    return;
                }
            }

            // Find foreman and owner
            Foreman foreman = null;
            for (Foreman f : foremen) {
                if (f.getId().equals(foremanId)) {
                    foreman = f;
                    break;
                }
            }

            Owner owner = null;
            for (Owner o : owners) {
                if (o.getId().equals(ownerId)) {
                    owner = o;
                    break;
                }
            }

            if (foreman == null || owner == null) {
                JOptionPane.showMessageDialog(null, "Foreman or owner not registered. Import aborted.");
                return;
            }

            // Read budget categories
            Map<String, Double> categoryBudgets = new HashMap<>();
            int numCategories = Integer.parseInt(reader.readLine().trim());

            for (int i = 0; i < numCategories; i++) {
                line = reader.readLine();
                parts = line.split("#");
                String categoryName = parts[0];
                double budget = Double.parseDouble(parts[1]);

                if (!categoriesMap.containsKey(categoryName)) {
                    Category newCategory = new Category(categoryName, "Descripción a Definir", 0.0);
                    categories.add(newCategory);
                    categoriesMap.put(categoryName, newCategory);
                }

                categoryBudgets.put(categoryName, budget);
            }

            // Register new construction site
            registerConstructionSite(owner, foreman, permitNumber, address, startMonth, startYear, 0.0, categoryBudgets);
            JOptionPane.showMessageDialog(null, "Construction site imported successfully!");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
        }
    }

   public List<Category> obtainCategories() {
       return new ArrayList<>(categories);
   }

   public void somethingChanged(){
        setChanged();
        notifyObservers();
   }
   
   // Métodos para registrar capataz
      public boolean registerForeman(String name, String id, String address, int yearHired) {
        Foreman newForeman = new Foreman(name, id, address, yearHired);
        boolean isUnique = true;
        for (Foreman f : this.obtainForemen()) {
            if (f.equals(newForeman)) {
                isUnique = false;
                break;
            }
        }
        if (isUnique) {
            foremen.add(newForeman);
            somethingChanged();
            //saveAllData(); only save on shutdown, so moved this out
        }
        return isUnique;
    }
      
      public boolean removeForeman(String name, String id, String address, int yearHired) {
        Foreman newForeman = new Foreman(name, id, address, yearHired);
        boolean found = false;
        for (Foreman f : this.obtainForemen()) {
            if (f.equals(newForeman)) {
                found = true;
                System.out.println(obtainForemen());
                this.foremen.remove(this.foremen.indexOf(f));
                System.out.println(obtainForemen());
                break;
            }
        }
        if (found) {
            
            //foremen.add(newForeman);
            somethingChanged();
            //saveAllData(); only save on shutdown, so moved this out
        }
        return found;
    }

   public List<Foreman> obtainForemen() {
       return new ArrayList<>(foremen);
   }

   // Métodos para registrar propietario 
   public boolean registerOwner(String name, String id, String address, int cellphone) {
        Owner newOwner = new Owner(name, id, address, cellphone);
        boolean isUnique = true;
        for (Owner o : this.obtainOwners()) {
            if (o.equals(newOwner)) {
                isUnique = false;
                break;
            }
        }
        if (isUnique) {
            owners.add(newOwner);
            somethingChanged();
            //saveAllData(); only save on shutdown, so moved this out
        }
        return isUnique;
    }

   public List<Owner> obtainOwners() {
       return new ArrayList<>(owners);
   }
   
       public void setForemen(List<Foreman> foremen) {
        this.foremen = foremen;
    }

    public void setOwners(List<Owner> owners) {
        this.owners = owners;
    }

    public void setConstructionSites(List<ConstructionSite> constructionSites) {
        this.constructionSites = constructionSites;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setCategoriesMap(Map<String, Category> categoriesMap) {
        this.categoriesMap = categoriesMap;
    }
   
   /*
   private void saveAllData() {
        FileManager.saveToFile(foremen, owners);
    }*/

   // Métodos para registrar obra
  public void registerConstructionSite(Owner owner, Foreman foreman, String permitNumber, String address, int startMonth, int startYear, double totalBudget, Map<String, Double> categoryBudgets) {
  ConstructionSite newConstructionSite = new ConstructionSite(owner, foreman, permitNumber, address, startMonth, startYear, totalBudget, categoryBudgets);
  constructionSites.add(newConstructionSite);
  somethingChanged();
}

   public List<ConstructionSite> obtainConstructionSites() {
       return new ArrayList<>(constructionSites);
   }

   // Métodos para registrar gasto
   public void registerExpenditures(ConstructionSite constructionSite, Category category, double amount, int month, int year, String description, Boolean paid) {
       Expenditures newExpenditures = new Expenditures(constructionSite, category, amount, month, year, description, paid);
       constructionSite.addExpenditure(newExpenditures);
       somethingChanged();
     
   }

   // Métodos para registrar pago de gasto
 

   // Métodos para obtener estado de obra
   public String obtainConstructionSiteStatus(ConstructionSite constructionSite) {
       StringBuilder status = new StringBuilder();
       status.append("Owner: ").append(constructionSite.getOwner().getName()).append("\n");
       status.append("Foreman: ").append(constructionSite.getForeman().getName()).append("\n");
       status.append("Start Date: ").append(constructionSite.getStartMonth()).append("/").append(constructionSite.getStartYear()).append("\n");
       status.append("Total Budget: ").append(constructionSite.getTotalBudget()).append("\n");
     //  status.append("Total Spent: ").append(constructionSite.calculateTotalExpenditures()).append("\n");
     //  status.append("Total Given Back: ").append(constructionSite.calculateTotalGivenBack()).append("\n");
      // status.append("Total Not Given Back: ").append(constructionSite.calculateTotalNotGivenBack()).append("\n");
      // status.append("Balance: ").append(constructionSite.calculateBalance()).append("\n");

       status.append("Categories with Expenditures: \n");
       for (Category category : constructionSite.obtainCategoriesWithExpenditures()) {
           status.append("  - ").append(category.getName()).append("\n");
       }

       status.append("Expenditures by Category: \n");
       for (Category category : constructionSite.obtainCategoriesWithExpenditures()) {
           status.append("  Category: ").append(category.getName()).append("\n");
           for (Expenditures expenditures : constructionSite.obtainExpendituresPerCategory(category)) {
               status.append("    - Expenditure: ").append(expenditures.getAmount()).append(", Paid: ").append(expenditures.isPaid()).append(", Description: ").append(expenditures.getDescription()).append("\n");
           }
       }

       return status.toString();
   }
   public void exportData(String filename, int option) { // if option is 0 save via id if 1 or anything else save via name
        RecordFile recordFile = null;
        try {
            recordFile = new RecordFile(filename);
            if (option == 0) { // Save via cedula number creciente
                System.out.println("Saving via ID number creciente");
                // Sort owners and foremen by cedula
                Collections.sort(owners, Comparator.comparing(Owner::getId));
                Collections.sort(foremen, Comparator.comparing(Foreman::getId)); 

            } else { // Save via name
                System.out.println("Saving via name creciente");

                // Sort owners and foremen by name
                Collections.sort(owners, Comparator.comparing(Owner::getName,String.CASE_INSENSITIVE_ORDER));
                Collections.sort(foremen, Comparator.comparing(Foreman::getName,String.CASE_INSENSITIVE_ORDER));
            }
            recordFile.recordLine("Propetarios: ");
            for (Owner owner : owners) {
                recordFile.recordLine(owner.toString());
            }
            recordFile.recordLine("Capataces: ");
            for (Foreman foreman : foremen) {
                recordFile.recordLine(foreman.toString());
            }

            System.out.println("Datos guardado en: " + filename);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (recordFile != null) {
                recordFile.close(); // Close the file
            }
        }
   }
    


   public void exportDataPersons(String order) {
       // Implementar la lógica para exportar datos de propietarios y capataces
   }
   
   public void RegisterExpendituresForConstruction(){
       //logic here
   }
   public void registerPaymentExpenditure(ConstructionSite site, Expenditures expenditure) {
  // Register payment logic (assuming implemented)
   expenditure.setPaid(true); // Set expenditure to paid
    somethingChanged(); // Notify observers about the change
}
 
}

