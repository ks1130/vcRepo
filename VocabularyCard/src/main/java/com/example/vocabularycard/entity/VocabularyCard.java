package com.example.vocabularycard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="vocabularycard")
@Data
public class VocabularyCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	@Column(name="user_id")
	private Integer userId;
	@Column(name="spelling1")
    private String spelling1;
	@Column(name="spelling2")
	private String spelling2;
	@Column(name="spelling3")
	private String spelling3;
	@Column(name="meaning1")
	private String meaning1;
	@Column(name="meaning2")
	private String meaning2;
	@Column(name="meaning3")
	private String meaning3;
	@Column(name="tag1")
	private String tag1;
	@Column(name="tag2")
	private String tag2;
	@Column(name="tag3")
	private String tag3;
}
