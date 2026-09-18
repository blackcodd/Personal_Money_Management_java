package com.example.model;

import jakarta.persistence.*;

@Entity
public  class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long I_id;
    private double amount;
    private String media;
    private String category;
    private String date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user;

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

    public User get_User() {
        return user;
    }

    public void setUser(User user) {
        this.user= user;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}

