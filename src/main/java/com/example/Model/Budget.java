package com.example.Model;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.Date;

@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long B_id;
    private String start_date;
    private String end_date;
    private Long amount;
    @ManyToOne
    @JoinColumn(name="id")
    private user user;

    public Long getB_id() {
        return B_id;
    }

    public void setB_id(Long b_id) {
        B_id = b_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public com.example.Model.user getUser() {
        return user;
    }

    public void setUser(com.example.Model.user user) {
        this.user = user;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
