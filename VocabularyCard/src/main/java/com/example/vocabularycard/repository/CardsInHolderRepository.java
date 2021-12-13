package com.example.vocabularycard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vocabularycard.entity.CardsInHolder;

@Repository
public interface CardsInHolderRepository extends JpaRepository<CardsInHolder,Integer> {
	List<CardsInHolder> findByHolderId(Integer holderId);
	List<CardsInHolder> findByHolderIdAndCardId(Integer holderId,Integer cardId);
}
