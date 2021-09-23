package com.example.vocabularycard.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.vocabularycard.entity.Tag;

import lombok.Data;

@Data
public class TagData{
	private Integer id;
	@NotBlank(message="タグを入力してください。")
	@Size(min=1,max=20)
	private String name;
	public Tag toTag(){
		Tag tag=new Tag();
		tag.setName(name);
		return tag;
	}

}
