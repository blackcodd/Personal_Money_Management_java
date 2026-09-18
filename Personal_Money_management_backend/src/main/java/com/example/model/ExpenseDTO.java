package com.example.model;

import java.time.LocalDate;

public class ExpenseDTO {

    private String date;  // Use LocalDate for better date handling
    private String category;
    private double amount;
    private String media;
    private String type;

    // Constructor with parameters to match the query fields
    public  ExpenseDTO(){

    }

    public ExpenseDTO(String date, String type, String category, double amount, String media) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.media = media;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
