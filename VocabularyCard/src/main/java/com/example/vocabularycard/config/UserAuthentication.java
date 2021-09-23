package com.example.vocabularycard.config;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthentication implements Authentication{

	private AccountUserDetails userDetails;
	private boolean authentication=true;

	public UserAuthentication(AccountUserDetails userDetails,boolean authentication) {
		this.userDetails=userDetails;
		this.authentication=authentication;
	}

	public UserAuthentication(AccountUserDetails userDetails) {
		this.userDetails=userDetails;
	}

	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return userDetails.getUsername();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO 自動生成されたメソッド・スタブ
		return userDetails.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Object getDetails() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Object getPrincipal() {
		// TODO 自動生成されたメソッド・スタブ
		return userDetails.getAccount();
	}

	@Override
	public boolean isAuthenticated() {
		// TODO 自動生成されたメソッド・スタブ
		return authentication;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		// TODO 自動生成されたメソッド・スタブ

	}

}
