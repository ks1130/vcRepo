package com.example.vocabularycard.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginData {
	@NotBlank(message="8 文字以上、16 文字以下のIDを入力してください。")
	private String loginId;

	@NotBlank(message="8 文字以上、16 文字以下のパスワードを入力してください。")
	private String password;

}
