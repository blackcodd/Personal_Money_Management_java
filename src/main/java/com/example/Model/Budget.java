package com.example.Model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Budget {
    @Id
    private Date start_date;
    private Date end_date;
    private Long amount;
    @ManyToOne
    @JoinColumn(name="id")
    private user user;

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
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
