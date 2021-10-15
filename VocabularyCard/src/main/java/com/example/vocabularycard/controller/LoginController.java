package com.example.vocabularycard.controller;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.vocabularycard.common.OpMsg;
import com.example.vocabularycard.entity.Account;
import com.example.vocabularycard.form.LoginData;
import com.example.vocabularycard.form.RegistData;
import com.example.vocabularycard.repository.AccountRepository;
import com.example.vocabularycard.service.LoginService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final AccountRepository accountRepository;
	private final MessageSource messageSource;
	private final LoginService loginService;
	private final HttpSession session;


	PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

	@GetMapping("/")
	public ModelAndView showLogin(ModelAndView mv) {
		mv.setViewName("loginForm");
		mv.addObject("loginData", new LoginData());
		return mv;
	}
	@GetMapping("/login")
	public ModelAndView login(ModelAndView mv) {
		System.out.println("/login");
		mv.setViewName("loginForm");
		mv.addObject("loginData", new LoginData());
		return mv;
	}
	/*
	@PostMapping("/login/do")
	public String login(@ModelAttribute @Validated LoginData loginData,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes,
			Locale locale) {
		System.out.println("/login/do");
		if(!loginService.isValid(loginData,result,locale)) {
			String msg=messageSource.getMessage("msg.e.input_something_wrong",null,locale);
			model.addAttribute("msg",new OpMsg("E",msg));
			return "loginForm";
		}

		Account account=accountRepository.findByLoginId(loginData.getLoginId()).get();

		String msg=messageSource.getMessage("msg.i.login_success",
				new Object[] {account.getLoginId(),account.getName()},
				locale);
		redirectAttributes.addFlashAttribute("msg", new OpMsg("I",msg));
		return "redirect:/vocabularyCard";
	}
	*/

	@GetMapping("/logout")
	public String logout(RedirectAttributes redirectAttributes,Locale locale) {
		System.out.println("/logout");
		String msg=messageSource.getMessage("msg.i.logout",null,locale);
		redirectAttributes.addFlashAttribute("msg", new OpMsg("I",msg));
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/login/failure")
	public String loginFailure(RedirectAttributes redirectAttributes,Locale locale) {
		String msg=messageSource.getMessage("msg.e.loginId_failure",null, locale);
		redirectAttributes.addFlashAttribute("msg", new OpMsg("E",msg));
		return "redirect:/";
	}

	@GetMapping("/regist")
	public ModelAndView regist(ModelAndView mv) {
		mv.setViewName("registForm");
		mv.addObject("registData",new RegistData());
		return mv;
	}


	@PostMapping("/regist/do")
	public String regist(@ModelAttribute @Validated RegistData registData,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes,
			Locale locale) {
		System.out.println(result.hasErrors());
		if(!result.hasErrors()&&loginService.isValid(registData, result, locale)) {
			Account account=registData.toAccount();
			String pass=passwordEncoder.encode(account.getPassword());
			account.setPassword(pass);
			accountRepository.saveAndFlush(account);
			String msg=messageSource.getMessage("msg.i.account_created",
					new Object[] {account.getLoginId(),account.getName()},
					locale);
			redirectAttributes.addFlashAttribute("msg",new OpMsg("I",msg));
		}else {
			String msg=messageSource.getMessage("msg.e.account_failed",null,locale);
			//redirectAttributes.addFlashAttribute("msg", new OpMsg("E",msg));
			model.addAttribute("msg", new OpMsg("E",msg));
			return "registForm";
		}
		return "redirect:/";
	}


	@GetMapping("/regist/cancel")
	public String cancel() {
		return "redirect:/";
	}
}
