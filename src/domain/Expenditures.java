package domain;

import domain.ConstructionSite;
import domain.Category;
import java.io.Serializable;

/*
    Davit Dostourian Erbe 281665 & Diego Pereira Puig - 329028
*/

public class Expenditures implements Serializable{
    // Atributos de la clase
    private ConstructionSite constructionSite;
    private Category category;
    private double amount;
    private int month;
    private int year;
    private String description;
    private boolean paid;

    // Constructor para inicializar los atributos
    public Expenditures(ConstructionSite constructionSite, Category category, double amount, int month, int year, String description, Boolean paid) {
        this.constructionSite = constructionSite;
        this.category = category;
        this.amount = amount;
        this.month = month;
        this.year = year;
        this.description = description;
        this.paid = paid; // Por defecto, el gasto no está pagado
    }

    // Métodos getter y setter para cada atributo
    public ConstructionSite getConstructionSite() {
        return constructionSite;
    }

    public void setConstructionSite(ConstructionSite constructionSite) {
        this.constructionSite = constructionSite;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "Expenditures{" +
                "constructionSite=" + constructionSite +
                ", category=" + category +
                ", amount=" + amount +
                ", month=" + month +
                ", year=" + year +
                ", description='" + description + '\'' +
                ", paid=" + paid +
                '}';
    }
        public static Expenditures fromString(String str) {
        // Remove "Expenditures{" and "}" from the string
        str = str.replace("Expenditures{", "").replace("}", "");

        // Split the string into parts based on ", "
        String[] parts = str.split(", ");

        // Initialize variables to store attributes
        ConstructionSite constructionSite = null;
        Category category = null;
        double amount = 0;
        int month = 0;
        int year = 0;
        String description = null;
        boolean paid = false;

        // Iterate through parts to extract attributes
        for (String part : parts) {
            if (part.startsWith("constructionSite=")) {
                String constructionSiteStr = part.substring("constructionSite=".length());
                constructionSite = ConstructionSite.fromString(constructionSiteStr); // Assuming ConstructionSite has a fromString method
            } else if (part.startsWith("category=")) {
                String categoryStr = part.substring("category=".length());
                category = Category.fromString(categoryStr); // Assuming Category has a fromString method
            } else if (part.startsWith("amount=")) {
                amount = Double.parseDouble(part.substring("amount=".length()));
            } else if (part.startsWith("month=")) {
                month = Integer.parseInt(part.substring("month=".length()));
            } else if (part.startsWith("year=")) {
                year = Integer.parseInt(part.substring("year=".length()));
            } else if (part.startsWith("description='")) {
                description = part.substring("description='".length(), part.length() - 1);
            } else if (part.startsWith("paid=")) {
                paid = Boolean.parseBoolean(part.substring("paid=".length()));
            }
        }

        // Create and return a new Expenditures object
        return new Expenditures(constructionSite, category, amount, month, year, description, paid);
    }
}