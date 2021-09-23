package com.example.vocabularycard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.vocabularycard.entity.VocabularyCard;

@Repository
public interface VocabularyCardRepository extends JpaRepository<VocabularyCard,Integer> {
	List<VocabularyCard> findBySpelling1(String spelling1);
	List<VocabularyCard> findBySpelling1OrSpelling2OrSpelling3(String spelling1,String spelling2,String spelling3);
	List<VocabularyCard> findByMeaning1OrMeaning2OrMeaning3(String meaning1,String meaning2,String meaning3);
	List<VocabularyCard> findByTag1OrTag2OrTag3(String tag1,String tag2,String tag3);

	List<VocabularyCard> findByUserIdOrderByIdDesc(Integer userId);
	List<VocabularyCard> findByUserIdAndSpelling1OrSpelling2OrSpelling3(Integer userId,String spelling1,String spelling2,String spelling3);
	List<VocabularyCard> findByUserIdAndMeaning1OrMeaning2OrMeaning3(Integer userId,String meaning1,String meaning2,String meaning3);

	List<VocabularyCard> findByUserIdAndTag1OrTag2OrTag3(Integer userId,String tag1,String tag2,String tag3);
}
