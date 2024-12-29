package com.example.Model;

public class CategoryPercentageDTO {
    private String category;
    private double percentage;
    private String type; // "income" or "expense"

    public CategoryPercentageDTO(String category, double percentage, String type) {
        this.category = category;
        this.percentage = percentage;
        this.type = type;
    }

    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
