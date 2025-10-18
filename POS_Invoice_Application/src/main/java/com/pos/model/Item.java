package com.pos.model;

public class Item {
    private int id;
    private String itemCode;
    private String itemName;
    private String category;
    private double cost;
    private double wholesalePrice;
    private double retailPrice;
    private double labelPrice;
    private double creditPrice;
    private String status;
    private String imagePath;

    // --- Constructors ---
    public Item() {}

    public Item(int id, String itemCode, String itemName, String category,
                double cost, double wholesalePrice, double retailPrice,
                double labelPrice, double creditPrice, String status, String imagePath) {
        this.id = id;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.category = category;
        this.cost = cost;
        this.wholesalePrice = wholesalePrice;
        this.retailPrice = retailPrice;
        this.labelPrice = labelPrice;
        this.creditPrice = creditPrice;
        this.status = status;
        this.imagePath = imagePath;
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public double getWholesalePrice() { return wholesalePrice; }
    public void setWholesalePrice(double wholesalePrice) { this.wholesalePrice = wholesalePrice; }

    public double getRetailPrice() { return retailPrice; }
    public void setRetailPrice(double retailPrice) { this.retailPrice = retailPrice; }

    public double getLabelPrice() { return labelPrice; }
    public void setLabelPrice(double labelPrice) { this.labelPrice = labelPrice; }

    public double getCreditPrice() { return creditPrice; }
    public void setCreditPrice(double creditPrice) { this.creditPrice = creditPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
