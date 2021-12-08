package com.example.vocabularycard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.array.IntArrayType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="holder")
@Data
@TypeDef(name="int-array",
		typeClass=IntArrayType.class)
@AllArgsConstructor
@NoArgsConstructor
public class Holder {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	@Column(name="owner_id")
	private Integer ownerId;
	@Column(name="classroomId")
	private Integer classroomId;//classroomで共有する場合に必要になる
	@Column(name="name")
	private String name;
}
