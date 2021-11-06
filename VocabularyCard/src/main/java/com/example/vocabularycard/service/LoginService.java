package com.example.vocabularycard.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.vocabularycard.config.AccountUserDetails;
import com.example.vocabularycard.entity.Account;
import com.example.vocabularycard.form.RegistData;
import com.example.vocabularycard.repository.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@Component
@AllArgsConstructor
public class LoginService implements UserDetailsService{
	private final MessageSource messageSource;
	private final AccountRepository accountRepository;
	private final HttpSession session;

	/*
	public boolean isValid(LoginData loginData,BindingResult result,Locale locale) {
		Optional<Account> account=accountRepository.findByLoginId(loginData.getLoginId());
		System.out.println(account);
		if(account.isEmpty()) {
			FieldError fieldError=new FieldError(
					result.getObjectName(),
					"loginId",
					messageSource.getMessage("msg.e.loginId_not_found",null,locale));
			result.addError(fieldError);
			return false;
		}
		if(!loginData.getPassword().equals(account.get().getPassword())) {
			FieldError fieldError=new FieldError(
					result.getObjectName(),
					"password",
					messageSource.getMessage("msg.e.password_wrong",null,locale));
			result.addError(fieldError);
			return false;
		}
		return true;
	}
	*/

	public boolean isValid(RegistData registData,BindingResult result,Locale locale) {
		Optional<Account> account=accountRepository.findByLoginId(registData.getLoginId());
		if(!registData.getPassword().equals(registData.getPasswordForCheck())) {
			FieldError fieldError=new FieldError(
					result.getObjectName(),
					"passwordForCheck",
					messageSource.getMessage("msg.e.mismatch_password",null,locale));
			result.addError(fieldError);
			return false;
		}
		if(account.isPresent()) {
			FieldError fieldError=new FieldError(
					result.getObjectName(),
					"loginId",
					messageSource.getMessage("msg.e.used_id",null,locale));
			result.addError(fieldError);
			return false;
		}

		return true;
	}

	@Override
	public UserDetails loadUserByUsername(String loginId)throws UsernameNotFoundException{
		Account account=accountRepository.findByLoginId(loginId).get();

		if(account==null) {
			throw new UsernameNotFoundException("username is empty");
		}
		Collection<GrantedAuthority> authorities=new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(account.getRole()));

		AccountUserDetails user=new AccountUserDetails(account,authorities);
		session.setAttribute("accountId", account.getId());
		session.setAttribute("lang1", account.getLang1());
		session.setAttribute("lang2", account.getLang2());
		session.setAttribute("preVoicename", account.getVoicename());
		return user;

	}
}
