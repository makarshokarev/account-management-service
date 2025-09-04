package com.fintech.repository;

import com.fintech.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.phoneNr = :phoneNr AND a.deletedTime IS NULL")
    boolean existsByPhoneNr(String phoneNr);

    @Query("SELECT a FROM Account a WHERE a.id = :id AND a.deletedTime IS NULL")
    @Override
    Optional<Account> findById(@Param("id") Long id);
}