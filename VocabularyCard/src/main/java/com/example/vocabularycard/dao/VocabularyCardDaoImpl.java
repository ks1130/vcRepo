package com.example.vocabularycard.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.example.vocabularycard.entity.VocabularyCard;
import com.example.vocabularycard.form.VocabularyCardQuery;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VocabularyCardDaoImpl implements VocabularyCardDao{
	private final EntityManager entityManager;

	@Override
	public List<VocabularyCard> findByCriteria(VocabularyCardQuery vocabularyCardQuery,Integer accountId){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<VocabularyCard> query=criteriaBuilder.createQuery(VocabularyCard.class);
		Root<VocabularyCard> root=query.from(VocabularyCard.class);
		//and()へのPredicate格納用、可変長引数として代入する用のリスト
		//spellingまたはmeaningが空欄の可能性があるため、可変長引数として代入する必要がある
		List<Predicate> predicates=new ArrayList<>();
		//accountIdで取得
		Predicate predicateByAcountId=criteriaBuilder.equal(root.get("userId"),accountId);
		predicates.add(predicateByAcountId);
		//Spelling,meaning,tagをそれぞれ取得
		String searchSpelling="%"+vocabularyCardQuery.getSearchSpelling()+"%";
		String searchMeaning=vocabularyCardQuery.getSearchMeaning();
		String searchTag=vocabularyCardQuery.getSearchTag();

		if(!searchTag.isEmpty()){
			Predicate predicateByTag1=criteriaBuilder.equal(root.get("tag1"), searchTag);
			Predicate predicateByTag2=criteriaBuilder.equal(root.get("tag2"), searchTag);
			Predicate predicateByTag3=criteriaBuilder.equal(root.get("tag3"), searchTag);
			Predicate predicateByTag=criteriaBuilder.or(predicateByTag1,predicateByTag2,predicateByTag3);
			predicates.add(predicateByTag);
		//spellingまたはmeaningで検索するか判定
		}else if(!(searchSpelling.isEmpty()&&searchMeaning.isEmpty())) {
			if(!searchSpelling.isEmpty()) {
				Predicate predicateBySpelling1=criteriaBuilder.like(root.get("spelling1"),searchSpelling);
				Predicate predicateBySpelling2=criteriaBuilder.like(root.get("spelling2"),searchSpelling);
				Predicate predicateBySpelling3=criteriaBuilder.like(root.get("spelling3"),searchSpelling);
				Predicate predicateBySpelling=criteriaBuilder.or(predicateBySpelling1,predicateBySpelling2,predicateBySpelling3);
				predicates.add(predicateBySpelling);
			}
			if(!searchMeaning.isEmpty()){
				Predicate predicateByMeaning1=criteriaBuilder.like(root.get("meaning1"),searchMeaning);
				Predicate predicateByMeaning2=criteriaBuilder.like(root.get("meaning2"),searchMeaning);
				Predicate predicateByMeaning3=criteriaBuilder.like(root.get("meaning3"),searchMeaning);
				Predicate predicateByMeaning=criteriaBuilder.or(predicateByMeaning1,predicateByMeaning2,predicateByMeaning3);
				predicates.add(predicateByMeaning);
			}
		}
		Predicate[] predicateArray=new Predicate[predicates.size()];
		predicates.toArray(predicateArray);
		Predicate finalPredicate=criteriaBuilder.and(predicateArray);
		query=query.select(root).where(finalPredicate).orderBy(criteriaBuilder.desc(root.get("id")));
		List<VocabularyCard> cardList=entityManager.createQuery(query).getResultList();
		return cardList;
	}

}
