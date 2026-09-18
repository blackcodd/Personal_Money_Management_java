package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.Expense;
import com.example.model.Income;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

}
