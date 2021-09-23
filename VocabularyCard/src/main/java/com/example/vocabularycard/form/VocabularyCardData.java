package com.example.vocabularycard.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.vocabularycard.entity.VocabularyCard;

import lombok.Data;

@Data
public class VocabularyCardData {
	//入力された値を格納するフィールド
	private Integer id;
	@NotNull
	@Size(min=1,max=50)
	private String spelling1;
	@Size(min=0,max=50)
	private String spelling2;
	@Size(min=0,max=50)
	private String spelling3;
	@Size(min=0,max=50)
	private String meaning1;
	@Size(min=0,max=50)
	private String meaning2;
	@Size(min=0,max=50)
	private String meaning3;
	@Size(min=0,max=20)
	private String tag1;
	@Size(min=0,max=20)
	private String tag2;
	@Size(min=0,max=20)
	private String tag3;

	//入力された値からカードを生成するメソッド
	public VocabularyCard toEntity(Integer accountId) {
		VocabularyCard vocabularyCard=new VocabularyCard();
		vocabularyCard.setId(id);
		vocabularyCard.setSpelling1(spelling1);
		vocabularyCard.setSpelling2(spelling2);
		vocabularyCard.setSpelling3(spelling3);
		vocabularyCard.setSpelling3(spelling3);
		vocabularyCard.setMeaning1(meaning1);
		vocabularyCard.setMeaning2(meaning2);
		vocabularyCard.setMeaning3(meaning3);
		vocabularyCard.setTag1(tag1);
		vocabularyCard.setTag2(tag2);
		vocabularyCard.setTag3(tag3);
		vocabularyCard.setUserId(accountId);
		return vocabularyCard;
	}

}
