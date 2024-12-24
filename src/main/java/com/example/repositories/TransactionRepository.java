package com.example.repositories;

import com.example.Model.ExpenseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;
    public List<ExpenseDTO> filterTransactions(long userId, Integer year, Integer month, Integer date)
    {
        StringBuilder sql =
                 new StringBuilder( " SELECT " +
                         "i.date AS date," +
                         " 'income' AS type," +
                         " i.category AS category, i.amount AS amount, i.media AS media "
                         + "FROM income i WHERE i.user_id = :userId");
        if (year != null) { sql.append(" AND YEAR(i.date) = :year"); }
        if (month != null) { sql.append(" AND MONTH(i.date) = :month"); }
        if (date != null) { sql.append(" AND DAY(i.date) = :date"); }
        sql.append(" UNION ALL "
                + "SELECT e.date AS date, " +
                "'expense' AS type, e.category AS category," +
                " e.amount AS amount, e.media AS media "
                + "FROM expense e WHERE e.user_id = :userId");
        if (year != null) { sql.append(" AND YEAR(e.date) = :year"); }
        if (month != null) { sql.append(" AND MONTH(e.date) = :month"); }
        if (date != null) { sql.append(" AND DAY(e.date) = :date"); }
        sql.append(" ORDER BY date desc");
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql.toString());
        query.setParameter("userId", userId);
        if (year != null) { query.setParameter("year", year); }
        if (month != null) { query.setParameter("month", month); }
        if (date != null) { query.setParameter("date", date); }
        List<Object[]> results = query.getResultList();
        return results.stream() .map(result -> new ExpenseDTO
                        (
                            result[0].toString(),
                             result[1].toString(),
                              result[2].toString(),
                                Double.parseDouble(result[3].toString()),
                                  result[4].toString()
                        )
        ) .toList();
    }



    public List<ExpenseDTO> getAllTransactionsByUserId(long userId) {
        String sql = """
            SELECT 
                i.date AS date, 
                'income' AS type, 
                i.category AS category, 
                i.amount AS amount, 
                i.media AS media
            FROM income i
            WHERE i.user_id = :userId
            UNION ALL
            SELECT 
                e.date AS date, 
                'expense' AS type, 
                e.category AS category, 
                e.amount AS amount, 
                e.media AS media
            FROM expense e
            WHERE e.user_id = :userId
            ORDER BY date
        """;

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("userId", userId)
                .getResultList();

        return results.stream()
                .map(result -> new ExpenseDTO(
                        result[0].toString(),   // date
                        result[1].toString(),   // type
                        result[2].toString(),   // category
                        Double.parseDouble(result[3].toString()),
                        result[4].toString()    // media
                ))
                .toList();
    }
}
