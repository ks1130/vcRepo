package com.example.vocabularycard.form;

import com.example.vocabularycard.entity.Classroom;

import lombok.Data;

@Data
public class ClassroomData {
	private String name;
	private String password;
	public Classroom toClassroom() {
		Classroom classroom=new Classroom(null,this.name,this.password);
		return classroom;
	}
}
