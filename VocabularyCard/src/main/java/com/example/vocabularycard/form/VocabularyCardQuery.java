package com.example.vocabularycard.form;

import lombok.Data;

@Data
public class VocabularyCardQuery {
	private String searchSpelling;
	private String searchMeaning;
	private String searchTag;

	public VocabularyCardQuery() {
		searchSpelling="";
		searchMeaning="";
		searchTag="";
	}
}
