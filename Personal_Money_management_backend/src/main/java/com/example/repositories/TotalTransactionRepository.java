package com.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import com.example.model.CategoryPercentageDTO;
import com.example.model.IncomeExpenseDTO;

import java.util.ArrayList;
import java.util.List;


@Repository
public class TotalTransactionRepository {
    @PersistenceContext
    private EntityManager entityManager;
    //for total income expense dto
    public IncomeExpenseDTO getTotalIncomeAndExpense(long userId, Integer year, Integer month, Integer date) {
        StringBuilder incomeSql = new StringBuilder(
                "SELECT SUM(i.amount) FROM income i WHERE i.user_id = :userId"
        );
        StringBuilder expenseSql = new StringBuilder(
                "SELECT SUM(e.amount) FROM expense e WHERE e.user_id = :userId"
        );

        // Add conditions dynamically for income
        if (year != null) {
            incomeSql.append(" AND YEAR(i.date) = :year");
        }
        if (month != null) {
            incomeSql.append(" AND MONTH(i.date) = :month");
        }
        if (date != null) {
            incomeSql.append(" AND DAY(i.date) = :date");
        }

        // Add conditions dynamically for expense
        if (year != null) {
            expenseSql.append(" AND YEAR(e.date) = :year");
        }
        if (month != null) {
            expenseSql.append(" AND MONTH(e.date) = :month");
        }
        if (date != null) {
            expenseSql.append(" AND DAY(e.date) = :date");
        }

        // Execute income query
        Query incomeQuery = entityManager.createNativeQuery(incomeSql.toString());
        incomeQuery.setParameter("userId", userId);
        if (year != null) {
            incomeQuery.setParameter("year", year);
        }
        if (month != null) {
            incomeQuery.setParameter("month", month);
        }
        if (date != null) {
            incomeQuery.setParameter("date", date);
        }
        Object incomeResult = incomeQuery.getSingleResult();
        long totalIncome = incomeResult != null ? ((Number) incomeResult).longValue() : 0L;

        // Execute expense query
        Query expenseQuery = entityManager.createNativeQuery(expenseSql.toString());
        expenseQuery.setParameter("userId", userId);
        if (year != null) {
            expenseQuery.setParameter("year", year);
        }
        if (month != null) {
            expenseQuery.setParameter("month", month);
        }
        if (date != null) {
            expenseQuery.setParameter("date", date);
        }
        Object expenseResult = expenseQuery.getSingleResult();
        long totalExpense = expenseResult != null ? ((Number) expenseResult).longValue() : 0L;

        // Return the result as a DTO
        return new IncomeExpenseDTO(totalIncome, totalExpense);
    }
    // for category parcentage
    public List<CategoryPercentageDTO> getCategoryPercentage(long userId, Integer year, Integer month, Integer date) {
        StringBuilder incomeCategorySql = new StringBuilder(
                "SELECT i.category, SUM(i.amount) FROM income i WHERE i.user_id = :userId"
        );
        StringBuilder expenseCategorySql = new StringBuilder(
                "SELECT e.category, SUM(e.amount) FROM expense e WHERE e.user_id = :userId"
        );

        // Add conditions dynamically for income and expense
        if (year != null) {
            incomeCategorySql.append(" AND YEAR(i.date) = :year");
            expenseCategorySql.append(" AND YEAR(e.date) = :year");
        }
        if (month != null) {
            incomeCategorySql.append(" AND MONTH(i.date) = :month");
            expenseCategorySql.append(" AND MONTH(e.date) = :month");
        }
        if (date != null) {
            incomeCategorySql.append(" AND DAY(i.date) = :date");
            expenseCategorySql.append(" AND DAY(e.date) = :date");
        }

        incomeCategorySql.append(" GROUP BY i.category");
        expenseCategorySql.append(" GROUP BY e.category");

        // Execute income category query
        Query incomeCategoryQuery = entityManager.createNativeQuery(incomeCategorySql.toString());
        incomeCategoryQuery.setParameter("userId", userId);
        if (year != null) {
            incomeCategoryQuery.setParameter("year", year);
        }
        if (month != null) {
            incomeCategoryQuery.setParameter("month", month);
        }
        if (date != null) {
            incomeCategoryQuery.setParameter("date", date);
        }
        List<Object[]> incomeResults = incomeCategoryQuery.getResultList();

        // Execute expense category query
        Query expenseCategoryQuery = entityManager.createNativeQuery(expenseCategorySql.toString());
        expenseCategoryQuery.setParameter("userId", userId);
        if (year != null) {
            expenseCategoryQuery.setParameter("year", year);
        }
        if (month != null) {
            expenseCategoryQuery.setParameter("month", month);
        }
        if (date != null) {
            expenseCategoryQuery.setParameter("date", date);
        }
        List<Object[]> expenseResults = expenseCategoryQuery.getResultList();

        // Calculate percentages
        double totincome=(double) getTotalIncomeAndExpense(userId, year, month, date).getTotalIncome();
        List<CategoryPercentageDTO> categoryPercentages = new ArrayList<>();
        for (Object[] result : incomeResults) {
            String category = (String) result[0];
            long amount = ((Number) result[1]).longValue();
            double percentage = (amount * 100.0) / totincome;
            categoryPercentages.add(new CategoryPercentageDTO(category, percentage, "income"));
        }
           double totexp=(double) getTotalIncomeAndExpense(userId, year, month, date).getTotalExpense();
        for (Object[] result : expenseResults) {
            String category = (String) result[0];
            long amount = ((Number) result[1]).longValue();
            double percentage = (amount * 100.0) / totexp;
            categoryPercentages.add(new CategoryPercentageDTO(category, percentage, "expense"));
        }

        return categoryPercentages;
    }
}
