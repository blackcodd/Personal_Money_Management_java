package com.example.repositories;

import com.example.Model.expense;
import com.example.Model.income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<income, Long> {
  @Query(nativeQuery = true,value = "select COALESCE(sum(amount), 0) from income where user_id=:user_id ")
   Long  getTotalincomebyId(@Param("user_id") long user_id);
    @Query(nativeQuery = true,value = "select * from income where user_id=:user_id")
    List<income> allincome(@Param("user_id") long user_id);
     @Query(nativeQuery = true,value = "SELECT SUM(amount) FROM income  WHERE user_id = :user_id")
     Long findTotalIncomeByUserId(@Param("user_id") long user_id);
     @Query(nativeQuery = true,value = "SELECT SUM(amount) FROM income  WHERE user_id = :user_id AND YEAR(date) = :year AND MONTH(date) = :month")
     Long findTotalIncomeByUserIdAndYearAndMonth(@Param("user_id") long user_id, @Param("year") int year, @Param("month") int month);
     @Query(nativeQuery = true,value = "SELECT SUM(amount) FROM income  WHERE user_id = :user_id AND YEAR(date) = :year AND MONTH(date) = :month AND DAY(date) = :date")
     Long findTotalIncomeByUserIdAndYearAndMonthAndDate(@Param("user_id") long user_id, @Param("year") int year, @Param("month") int month, @Param("date") int date);

}
