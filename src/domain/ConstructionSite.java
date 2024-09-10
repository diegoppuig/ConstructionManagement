package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Davit Dostourian Erbe 281665 & Diego Pereira Puig - 329028
*/

public class ConstructionSite implements Serializable {
    private Owner owner;
    private Foreman foreman;
    private String permitNumber;
    private String address;
    private int startMonth;
    private int startYear;
    private double totalBudget;
    private List<Expenditures> expenditures;
    private Map<String, Double> categoryBudgets;

    public ConstructionSite(Owner owner, Foreman foreman, String permitNumber, String address, int startMonth, int startYear, double totalBudget, Map<String, Double> categoryBudgets) {
        this.owner = owner;
        this.foreman = foreman;
        this.permitNumber = permitNumber;
        this.address = address;
        this.startMonth = startMonth;
        this.startYear = startYear;
        this.totalBudget = totalBudget;
        this.expenditures = new ArrayList<>();
        this.categoryBudgets = categoryBudgets;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Foreman getForeman() {
        return foreman;
    }

    public void setForeman(Foreman foreman) {
        this.foreman = foreman;
    }

    public String getPermitNumber() {
        return permitNumber;
    }

    public void setPermitNumber(String permitNumber) {
        this.permitNumber = permitNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public List<Expenditures> getExpenditures() {
        return expenditures;
    }

    public void setExpenditures(List<Expenditures> expenditures) {
        this.expenditures = expenditures;
    }

    public Map<String, Double> getBudgetCategories() {
        return categoryBudgets;
    }

    public void addExpenditure(Expenditures expenditure) {
        this.expenditures.add(expenditure);
    }

    public List<Expenditures> getUnpaidExpenditures() {
        List<Expenditures> unpaidExpenditures = new ArrayList<>();
        for (Expenditures expenditure : expenditures) {
            if (!expenditure.isPaid()) {
                unpaidExpenditures.add(expenditure);
            }
        }
        return unpaidExpenditures;
    }

    public List<Expenditures> getPaidExpenditures() {
        List<Expenditures> paidExpenditures = new ArrayList<>();
        for (Expenditures expenditure : expenditures) {
            if (expenditure.isPaid()) {
                paidExpenditures.add(expenditure);
            }
        }
        return paidExpenditures;
    }

    public List<Category> obtainCategoriesWithExpenditures() {
        List<Category> categoriesWithExpenditures = new ArrayList<>();
        for (Expenditures expenditure : expenditures) {
            if (!categoriesWithExpenditures.contains(expenditure.getCategory())) {
                categoriesWithExpenditures.add(expenditure.getCategory());
            }
        }
        return categoriesWithExpenditures;
    }
    public double getTotalExpenditures() {
    double total = 0;
    for (Expenditures expenditure : expenditures) {
        total += expenditure.getAmount();
    }
    return total;
}

public double getTotalPaidExpenditures() {
    double total = 0;
    for (Expenditures expenditure : expenditures) {
        if (expenditure.isPaid()) {
            total += expenditure.getAmount();
        }
    }
    return total;
}

public double getTotalUnpaidExpenditures() {
    double total = 0;
    for (Expenditures expenditure : expenditures) {
        if (!expenditure.isPaid()) {
            total += expenditure.getAmount();
        }
    }
    return total;
}

    public List<Expenditures> obtainExpendituresPerCategory(Category category) {
        List<Expenditures> expendituresPerCategory = new ArrayList<>();
        System.out.println("Looking for expenditures in category: " + category.getName());
        for (Expenditures expenditure : expenditures) {
            System.out.println("Checking expenditure with category: " + expenditure.getCategory().getName());
            if (expenditure.getCategory().equals(category)) {
                expendituresPerCategory.add(expenditure);
            }
        }
        System.out.println("Found " + expendituresPerCategory.size() + " expenditures for category: " + category.getName());
        return expendituresPerCategory;
    }

    @Override
    public String toString() {
        return "ConstructionSite{" +
                "owner=" + owner +
                ", foreman=" + foreman +
                ", permitNumber='" + permitNumber + '\'' +
                ", address='" + address + '\'' +
                ", startMonth=" + startMonth +
                ", startYear=" + startYear +
                ", totalBudget=" + totalBudget +
                ", expenditures=" + expenditures +
                ", categoryBudgets=" + categoryBudgets +
                '}';
    }

    public static ConstructionSite fromString(String str) {
        str = str.replace("ConstructionSite{", "").replace("}", "");

        String[] parts = str.split(", ");
        Owner owner = null;
        Foreman foreman = null;
        String permitNumber = null;
        String address = null;
        int startMonth = 0;
        int startYear = 0;
        double totalBudget = 0;
        List<Expenditures> expenditures = new ArrayList<>();
        Map<String, Double> categoryBudgets = new HashMap<>();

        for (String part : parts) {
            if (part.startsWith("owner=")) {
                String ownerStr = part.substring("owner=".length());
                owner = Owner.fromString(ownerStr);
            } else if (part.startsWith("foreman=")) {
                String foremanStr = part.substring("foreman=".length());
                foreman = Foreman.fromString(foremanStr);
            } else if (part.startsWith("permitNumber='")) {
                permitNumber = part.substring("permitNumber='".length(), part.length() - 1);
            } else if (part.startsWith("address='")) {
                address = part.substring("address='".length(), part.length() - 1);
            } else if (part.startsWith("startMonth=")) {
                startMonth = Integer.parseInt(part.substring("startMonth=".length()));
            } else if (part.startsWith("startYear=")) {
                startYear = Integer.parseInt(part.substring("startYear=".length()));
            } else if (part.startsWith("totalBudget=")) {
                totalBudget = Double.parseDouble(part.substring("totalBudget=".length()));
            } else if (part.startsWith("expenditures=[")) {
                String expendituresStr = part.substring("expenditures=[".length(), part.length() - 1);
                if (!expendituresStr.isEmpty()) {
                    String[] expendituresParts = expendituresStr.split("}, Expenditures\\{");
                    for (String expenditureStr : expendituresParts) {
                        expenditureStr = "Expenditures{" + expenditureStr + "}";
                        Expenditures expenditure = Expenditures.fromString(expenditureStr);
                        expenditures.add(expenditure);
                    }
                }
            } else if (part.startsWith("categories={")) {
                String categoriesStr = part.substring("categories={".length(), part.length() - 1);
                if (!categoriesStr.isEmpty()) {
                    String[] categoriesParts = categoriesStr.split(", ");
                    for (String categoryStr : categoriesParts) {
                        String[] categoryPair = categoryStr.split("=");
                        String categoryName = categoryPair[0].trim();
                        Double budget = Double.parseDouble(categoryPair[1].trim());
                        categoryBudgets.put(categoryName, budget);
                    }
                }
            }
        }

        ConstructionSite site = new ConstructionSite(owner, foreman, permitNumber, address, startMonth, startYear, totalBudget, categoryBudgets);
        site.setExpenditures(expenditures);
        return site;
    }
}
