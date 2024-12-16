package com.example.Model;

import jakarta.persistence.*;

@Entity
public class expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long E_id;
    private double amount;
    private String media;
    private String category;
    private String date;
    @ManyToOne
    @JoinColumn(name="user_id")
    private user user;

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

    public user get_User() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
