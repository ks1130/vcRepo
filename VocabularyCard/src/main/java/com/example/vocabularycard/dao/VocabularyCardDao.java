package com.example.vocabularycard.dao;
import java.util.List;

import com.example.vocabularycard.entity.VocabularyCard;
import com.example.vocabularycard.form.VocabularyCardQuery;

public interface VocabularyCardDao {
	List<VocabularyCard> findByCriteria(VocabularyCardQuery vocabularyCradQuery,Integer accountId);

}
