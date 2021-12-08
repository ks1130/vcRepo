package com.example.vocabularycard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vocabularycard.entity.Holder;

public interface HolderRepository extends JpaRepository<Holder,Integer>{
	List<Holder> findByClassroomId(Integer classroomId);
	List<Holder> findByOwnerId(Integer ownerId);
}
