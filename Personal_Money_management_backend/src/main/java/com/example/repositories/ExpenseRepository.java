package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.ExpenseDTO;
import com.example.model.Expense;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
     @Query(nativeQuery = true,value = "SELECT SUM(amount) FROM Expense WHERE user_id = :user_id AND YEAR(date) = :year")
     Long findTotalExpenseByUserIdandYear(@Param("user_id") long user_id,@Param("year") int year);@Query(nativeQuery = true, value = """
    SELECT 
        category, 
        SUM(amount) AS total_amount, 
        (SUM(amount) * 100.0 / (SELECT SUM(amount) FROM Expense WHERE user_id = :user_id)) AS percentage 
    FROM 
        Expense 
    WHERE 
        user_id = :user_id 
    GROUP BY 
        category
                   """)
     List<Object[]> findCategoryAndPercentageByUserId(@Param("user_id") long userId);

     @Query(nativeQuery = true, value = """
    SELECT 
        category, 
        SUM(amount) AS total_amount, 
        (SUM(amount) * 100.0 / (SELECT SUM(amount) FROM Expense WHERE user_id = :user_id AND YEAR(date) = :year)) AS percentage 
    FROM 
        Expense 
    WHERE 
        user_id = :user_id AND YEAR(date) = :year 
    GROUP BY 
        category
""")
     List<Object[]> findCategoryAndPercentageByUserIdAndYear(@Param("user_id") long userId, @Param("year") int year);

     @Query(nativeQuery = true, value = """
    SELECT 
        category, 
        SUM(amount) AS total_amount, 
        (SUM(amount) * 100.0 / (SELECT SUM(amount) FROM Expense WHERE user_id = :user_id AND YEAR(date) = :year AND MONTH(date) = :month)) AS percentage 
    FROM 
        Expense 
    WHERE 
        user_id = :user_id AND YEAR(date) = :year AND MONTH(date) = :month 
    GROUP BY 
        category
""")
     List<Object[]> findCategoryAndPercentageByUserIdAndYearAndMonth(@Param("user_id") long userId, @Param("year") int year, @Param("month") int month);

     @Query(nativeQuery = true, value = """
    SELECT 
        category, 
        SUM(amount) AS total_amount, 
        (SUM(amount) * 100.0 / (SELECT SUM(amount) FROM Expense WHERE user_id = :user_id AND YEAR(date) = :year AND MONTH(date) = :month AND DAY(date) = :date)) AS percentage 
    FROM 
        Expense 
    WHERE 
        user_id = :user_id AND YEAR(date) = :year AND MONTH(date) = :month AND DAY(date) = :date 
    GROUP BY 
        category
""")
     List<Object[]> findCategoryAndPercentageByUserIdAndYearAndMonthAndDate(
             @Param("user_id") long user_id,
             @Param("year") int year,
             @Param("month") int month,
             @Param("date") int date
     );

     @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND e.date >= :startDate AND e.date <= :endDate")
     Optional<Double> findTotalExpenseByUserAndDateRange(
             @Param("userId") long userId,
             @Param("startDate") String startDate,
             @Param("endDate") String endDate);

     @Query(value = "select sum(amount) from Expense where user_id=:id and date<= :end_date and date>=:end_date",nativeQuery = true)
      Double ExpenseInPeriod(@Param("id") Long id,@Param("start_date") String start_date ,@Param("end_date") String end_date);


}
