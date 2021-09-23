package com.example.vocabularycard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vocabularycard.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag,Integer>{
	List<Tag> findByUserIdOrderByIdDesc(Integer UserId);

}
