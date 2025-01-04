package com.example.repositories;

import com.example.Model.Budget;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO budget (start_date, end_date, id, amount) VALUES (:start_date, :end_date, :id,:amount)", nativeQuery = true)
    void insertBudget(@Param("id") Long id, @Param("start_date") Date startDate, @Param("end_date") Date endDate ,@Param("amount") long amount);
}
