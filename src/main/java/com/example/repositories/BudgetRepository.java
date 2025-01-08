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
    void insertBudget(@Param("id") Long id, @Param("start_date") String startDate, @Param("end_date") String endDate ,@Param("amount") Double amount);


    @Modifying
    @Transactional
    @Query(value = "delete from budget where id=:user_id",nativeQuery = true)
   int  deletebudgetByid(@Param("user_id") Long user_id);

    @Query(value = "SELECT count(*) from budget  WHERE id =:user_id", nativeQuery = true)
    int existId(@Param("user_id") Long userId);

}
