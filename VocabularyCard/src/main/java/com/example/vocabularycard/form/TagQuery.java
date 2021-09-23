package com.example.vocabularycard.form;

public class TagQuery extends VocabularyCardQuery{
	private String searchTag;

	public TagQuery() {
		searchTag="";
	}
	public String getSearchTag() {
		return this.searchTag;
	}
}
