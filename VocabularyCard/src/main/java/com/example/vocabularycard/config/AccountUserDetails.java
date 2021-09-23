package com.example.vocabularycard.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.vocabularycard.entity.Account;

public class AccountUserDetails implements UserDetails{
	private final Account account;
	private final Collection<GrantedAuthority> authorities;

	public AccountUserDetails(
			Account account,Collection<GrantedAuthority> authorities) {
		this.account=account;
		this.authorities=authorities;
	}

	public String getPassword() {
		return account.getPassword();
	}
	public String getUsername() {
		return account.getLoginId();
	}
	public boolean isEnabled() {
		return account.getEnabled();
	}
	public Collection<GrantedAuthority> getAuthorities(){
		return authorities;
	}
	public boolean isAccountNonExpired() {
		return true;
	}
	public boolean isAccountNonLocked() {
		return true;
	}
	public boolean isCredentialsNonExpired() {
		return true;
	}
	public Account getAccount() {
		return account;
	}

}
