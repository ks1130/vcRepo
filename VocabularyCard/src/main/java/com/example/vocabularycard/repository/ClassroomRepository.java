package com.example.vocabularycard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vocabularycard.entity.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom,Integer>{
	List<Classroom> findAllById(Integer id);
	List<Classroom> findByName(String name);

}
