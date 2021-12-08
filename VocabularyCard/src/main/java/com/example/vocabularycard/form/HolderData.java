package com.example.vocabularycard.form;

import com.example.vocabularycard.entity.Holder;

import lombok.Data;

@Data
public class HolderData {
	private Integer ownerId;
	private Integer classroomId;
	private String name;
	public Holder toHolder() {
		return new Holder(null,this.ownerId,this.classroomId,this.name);
	}
}
