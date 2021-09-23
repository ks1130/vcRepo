package com.example.vocabularycard.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.vocabularycard.entity.VocabularyCard;
import com.example.vocabularycard.form.TagData;
import com.example.vocabularycard.form.VocabularyCardData;
import com.example.vocabularycard.form.VocabularyCardQuery;
import com.example.vocabularycard.repository.VocabularyCardRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VocabularyCardService {
	private final VocabularyCardRepository vocabularyCardRepository;

	//NotBlankで対応可能
	public boolean isValid(VocabularyCardData vocabularyCardData,BindingResult result) {
		boolean ans=true;

		String spelling1=vocabularyCardData.getSpelling1();
		if(spelling1==null||spelling1.equals("")) {
			FieldError fieldError=new FieldError(
					result.getObjectName(),
					"spelling1",
					"spelling1 は必ず記述してください。");
			result.addError(fieldError);
			ans=false;
		}
		return ans;
	}
	public boolean isValidTug(TagData tag,BindingResult result) {
		boolean ans=true;
		String name=tag.getName();
		if(name==null||name.equals("")) {
			FieldError fieldError=new FieldError(
					result.getObjectName(),
					"name",
					"タグ名を記入してください。");
			result.addError(fieldError);
			ans=false;
		}
		return ans;
	}

	public List<VocabularyCard> doQuery(VocabularyCardQuery vocabularyCardQuery,Integer accountId){

		//タグクリック時
		List<VocabularyCard> searchedCard=null;
		if(!vocabularyCardQuery.getSearchTag().isBlank()) {
			String searchTag=vocabularyCardQuery.getSearchTag();
			List<VocabularyCard> cardByTug=vocabularyCardRepository.findByUserIdAndTag1OrTag2OrTag3(accountId,searchTag,searchTag,searchTag);
			LinkedHashSet<VocabularyCard> tmpSet=new LinkedHashSet<VocabularyCard>(cardByTug);
			searchedCard=new ArrayList<VocabularyCard>(tmpSet);
		}else {
			//検索フォーム入力時
			String searchedSpelling=vocabularyCardQuery.getSearchSpelling();
			String searchedMeaning=vocabularyCardQuery.getSearchMeaning();
			List<VocabularyCard> cardBySpelling=new ArrayList<VocabularyCard>();
			List<VocabularyCard> cardByMeaning=new ArrayList<VocabularyCard>();
			//spelling,meaningどちらかが入力されたときに検索を実行
			if(!searchedSpelling.isEmpty()||!searchedMeaning.isEmpty()) {
				if(searchedMeaning.isEmpty()) {
					cardBySpelling=vocabularyCardRepository.findByUserIdAndSpelling1OrSpelling2OrSpelling3(accountId,searchedSpelling, searchedSpelling, searchedSpelling);
				}
				if(searchedSpelling.isEmpty()) {
					cardByMeaning=vocabularyCardRepository.findByUserIdAndMeaning1OrMeaning2OrMeaning3(accountId,searchedMeaning,searchedMeaning,searchedMeaning);
				}
				cardBySpelling.addAll(cardByMeaning);
				LinkedHashSet<VocabularyCard> tmpSet=new LinkedHashSet<VocabularyCard>(cardBySpelling);
				searchedCard=new ArrayList<VocabularyCard>(tmpSet);
			}else {
				searchedCard=vocabularyCardRepository.findByUserIdOrderByIdDesc(accountId);
			}
		}
		return searchedCard;
	}
}
