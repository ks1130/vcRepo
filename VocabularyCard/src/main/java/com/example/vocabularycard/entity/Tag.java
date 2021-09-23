package com.example.vocabularycard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Table(name="taglist")
@Data
public class Tag {
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private Integer id;
	@Column(name="user_id")
	private Integer userId;
	@Column(name="name")
	private String name;
}
