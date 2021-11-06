package com.example.vocabularycard.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.example.vocabularycard.entity.Account;

import lombok.Data;

@Data
public class RegistData {
	@NotBlank
	/*private String name;
	@NotBlank
	@Length(max=16,message="16 文字以内で入力してください。")*/
	private String loginId;
	@NotBlank
	@Length(min=8,max=16,message="8 文字以上、16 文字以内で入力してください。")
	@Pattern(regexp="^[a-zA-Z0-9]+$",message="半角の英数字で入力してください。")
	private String password;
	@NotBlank
	@Length(min=8,max=16,message="8 文字以上、16 文字以内で入力してください。")
	@Pattern(regexp="^[a-zA-Z0-9]+$",message="半角の英数字で入力してください。")
	private String passwordForCheck;

	public Account toAccount() {
		Account account=new Account(null,this.loginId,/*this.name,*/this.password,"ROLE_USER",true,"en-US","en-US","notselected");
		return account;
	}

}
