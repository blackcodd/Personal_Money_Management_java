package com.example.repositories;

import com.example.Model.income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<income, Long> {
  @Query(nativeQuery = true,value = "select sum(amount) from income where user_id=:user_id ")
   Long getTotalincomebyId(@Param("user_id") long user_id);

}
