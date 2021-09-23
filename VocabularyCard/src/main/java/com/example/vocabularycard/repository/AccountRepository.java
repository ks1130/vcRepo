package com.example.vocabularycard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vocabularycard.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
	Optional<Account> findByLoginId(String loginId);

}
