package com.example.repositories;

import com.example.Model.ExpenseDTO;
import com.example.Model.expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<expense, Long> {
     @Query(nativeQuery = true,value="select  COALESCE(sum(amount), 0) from expense where user_id=:user_id ")
     Long  gettotalexpence(@Param("user_id") long user_id);

     @Query(nativeQuery = true,value = "select * from expense where user_id=:user_id")
     List<expense> allexpense(@Param("user_id") long user_id);
}
