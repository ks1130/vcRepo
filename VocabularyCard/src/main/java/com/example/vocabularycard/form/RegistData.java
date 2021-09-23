package com.example.vocabularycard.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.example.vocabularycard.entity.Account;

import lombok.Data;

@Data
public class RegistData {
	@NotBlank
	private String name;
	@NotBlank
	@Length(min=8,max=16)
	@Pattern(regexp="^[a-zA-Z0-9]+$")
	private String loginId;
	@NotBlank
	@Length(min=8,max=16)
	@Pattern(regexp="^[a-zA-Z0-9]+$")
	private String password;
	@NotBlank
	@Length(min=8,max=16)
	@Pattern(regexp="^[a-zA-Z0-9]+$")
	private String passwordForCheck;

	public Account toAccount() {
		Account account=new Account(null,this.loginId,this.name,this.password,"ROLE_USER",true);
		return account;
	}

}
