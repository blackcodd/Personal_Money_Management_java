package com.example.repositories;

import com.example.Model.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface LoginRepository extends JpaRepository<user, Long> {
    @Autowired

    user findByNameAndPassword(String username, String password);
}