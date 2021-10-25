package com.example.vocabularycard.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.vocabularycard.common.OpMsg;
import com.example.vocabularycard.dao.VocabularyCardDaoImpl;
import com.example.vocabularycard.entity.Tag;
import com.example.vocabularycard.entity.VocabularyCard;
import com.example.vocabularycard.form.LoginData;
import com.example.vocabularycard.form.TagData;
import com.example.vocabularycard.form.TagQuery;
import com.example.vocabularycard.form.VocabularyCardData;
import com.example.vocabularycard.form.VocabularyCardQuery;
import com.example.vocabularycard.repository.TagRepository;
import com.example.vocabularycard.repository.VocabularyCardRepository;
import com.example.vocabularycard.service.VocabularyCardService;

import lombok.RequiredArgsConstructor;
@Controller
@RequiredArgsConstructor
public class VocabularyCardController {
	private final VocabularyCardRepository vocabularyCardRepository;
	private final TagRepository tagRepository;
	private final VocabularyCardService vocabularyCardService;
	private final HttpSession session;
	private final MessageSource messageSource;
	@PersistenceContext
	private EntityManager entityManager;
	VocabularyCardDaoImpl vocabularyCardDaoImpl;

	@PostConstruct
	public void init() {
		vocabularyCardDaoImpl=new VocabularyCardDaoImpl(entityManager);
	}

	@GetMapping({"/vocabularyCard"})
	public ModelAndView showCard(ModelAndView mv) {
		Integer accountId=(Integer)session.getAttribute("accountId");

		//ログアウト後にアクセスできないようにする
		if(accountId==null) {
			mv.setViewName("loginForm");
			mv.addObject("loginData", new LoginData());
			return mv;
		}

		//全カードをデータベースから取得、Listに格納
		List<VocabularyCard> cardList=vocabularyCardRepository.findByUserIdOrderByIdDesc(accountId);
		//vocabularyCard.htmlへ送る
		mv.setViewName("vocabularyCard");

		//cardListをバインド
		mv.addObject("cardList", cardList);
		//タグリストをデータベースから取得
		List<Tag> tagList=tagRepository.findByUserIdOrderByIdDesc(accountId);
		//tugListをバインド
		mv.addObject("tagList", tagList);
		//form用のvocabularyDataを作成、バインド
		mv.addObject("vocabularyCardData",new VocabularyCardData());
		//form用のtugDataを作成、バインド
		mv.addObject("tagData",new TagData());
		//検索用フォームのvocabularyCardQueryをバインド
		mv.addObject("vocabularyCardQuery", new VocabularyCardQuery());
		mv.addObject("tagQuery", new TagQuery());
		return mv;
	}

	//つかいかた解説ページ
	@GetMapping("/vocabularyCard/operation")
	public String guideOperation() {
		return "operation";
	}

	//検索フォーム送信時、タグ検索時
	@PostMapping("/vocabularyCard/search")
	public ModelAndView showCard(@ModelAttribute VocabularyCardQuery vocabularyCardQuery,
			ModelAndView mv,Locale locale) {
		Integer accountId=(Integer)session.getAttribute("accountId");
		//vocabularyCard.htmlへ送る
		mv.setViewName("vocabularyCard");
		//タグ、検索
		List<VocabularyCard> cardList=vocabularyCardDaoImpl.findByCriteria(vocabularyCardQuery, accountId);
		//cardListをバインド
		mv.addObject("cardList", cardList);
		//タグリストをデータベースから取得
		List<Tag> tagList=tagRepository.findByUserIdOrderByIdDesc(accountId);
		//tugListをバインド
		mv.addObject("tagList", tagList);
		//form用のvocabularyDataを作成、バインド
		mv.addObject("vocabularyCardData",new VocabularyCardData());
		//form用のtugDataを作成、バインド
		mv.addObject("tagData",new TagData());

		if(cardList.size()==0) {
			String msg=messageSource.getMessage("msg.w.card_not_found",null, locale);
			mv.addObject("msg", new OpMsg("W",msg));
		}
		return mv;
	}

	@PostMapping("/vocabularyCard/create")
	public String createCard(@ModelAttribute @Validated VocabularyCardData vocabularyCardData,
			BindingResult result,RedirectAttributes redirectAttributes,Locale locale
			) {
		//エラーの有無で分岐
		if(!result.hasErrors()&&vocabularyCardService.isValid(vocabularyCardData, result)) {
			//エラーがない場合、カードを追加し、一覧を更新して表示
			VocabularyCard vocabularyCard=vocabularyCardData.toEntity((Integer)session.getAttribute("accountId"));
			vocabularyCardRepository.saveAndFlush(vocabularyCard);
			//追加完了のメッセージ
			String msg=messageSource.getMessage("msg.i.card_created",null, locale);
			redirectAttributes.addFlashAttribute("msg", new OpMsg("I",msg));
		}else {
			//エラーがあった場合、エラーメッセ―ジを表示して、初期画面に戻す
			if(vocabularyCardData.getSpelling1().isEmpty()) {
				String msg=messageSource.getMessage("msg.e.spelling1_not_exist",null, locale);
				redirectAttributes.addFlashAttribute("msg", new OpMsg("E",msg));
			}else {
				String msg=messageSource.getMessage("msg.e.input_something_wrong",null,locale);
				redirectAttributes.addFlashAttribute("msg", new OpMsg("E",msg));
			}
		}
		return "redirect:/vocabularyCard";
	}

	@PostMapping("/vocabularyCard/createTag")
	public String createTug(@ModelAttribute @Validated TagData tagData,BindingResult result,Model model,RedirectAttributes redirectAttributes,Locale locale) {
		if(!result.hasErrors()&&vocabularyCardService.isValidTug(tagData, result)) {
			Tag tag=tagData.toTag();
			/*Integer accountId=null;
			if(authentication.getPrincipal() instanceof AccountUserDetails) {
				AccountUserDetails userDetails=AccountUserDetails.class.cast(authentication.getPrincipal());
				accountId=userDetails.getAccount().getId();
			}
			*/
			tag.setUserId((Integer)session.getAttribute("accountId"));
			//tug.setUserId(accountId);
			tagRepository.saveAndFlush(tag);
			String msg=messageSource.getMessage("msg.i.tug_added",null,locale);
			redirectAttributes.addFlashAttribute("msg", new OpMsg("I",msg));
		}else {
			String msg=messageSource.getMessage("msg.e.input_something_wrong",null,locale);
			redirectAttributes.addFlashAttribute("msg", new OpMsg("E",msg));
		}

		return "redirect:/vocabularyCard";
	}

	@PostMapping("/vocabularyCard/delete")
	public String deleteCard(
			@ModelAttribute VocabularyCard vocabularyCard,
			@RequestParam(name="deletedId",required=false) ArrayList<Integer> deletedId,
			@RequestParam(name="deletedTagId",required=false) ArrayList<Integer> deletedTagId,
			Model model) {
		System.out.println("card:"+deletedId+"tag:"+deletedTagId);
		if(deletedId!=null) {
		    vocabularyCardRepository.deleteAllById(deletedId);
		}
		if(deletedTagId!=null) {
			tagRepository.deleteAllById(deletedTagId);
		}
		return "redirect:/vocabularyCard";
	}

	@PostMapping("/vocabularyCard/{id}")
	public ModelAndView editCard(@PathVariable(name="id") int id,
			ModelAndView mv,
			RedirectAttributes redirectAttributes,
			Locale locale) {
		Optional<VocabularyCard> someCard=vocabularyCardRepository.findById(id);

		if(!(someCard==null)) {
			Integer accountId=(Integer)session.getAttribute("accountId");
			VocabularyCard vocabularyCard=someCard.get();
			if(vocabularyCard.getUserId().equals(accountId)) {
				mv.setViewName("editForm");
				mv.addObject("editedCard", vocabularyCard);
			}else {
				messageError(mv,redirectAttributes,locale);
			}
		}

		/*
		someCard.ifPresentOrElse(vocabularyCard->{
			Integer accountId=(Integer)session.getAttribute("accountId");
			if(vocabularyCard.getUserId().equals(accountId)) {
				mv.setViewName("editForm");
				VocabularyCard editedCard=vocabularyCardRepository.findById(id).get();
				mv.addObject("editedCard", editedCard);
			}else {
				messageError(mv,redirectAttributes,locale);
			}
		}, ()->{
			messageError(mv,redirectAttributes,locale);

		});
		*/
		return mv;
	}

	@PostMapping("/vocabularyCard/update")
	public String update(
			@ModelAttribute @Validated VocabularyCardData vocabularyCardData,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes,
			Locale locale) {
		System.out.println(vocabularyCardData.getId());
		VocabularyCard vc=vocabularyCardData.toEntity((Integer)session.getAttribute("accountId"));
		if(!result.hasErrors()&&vocabularyCardService.isValid(vocabularyCardData, result)) {
			String msg=messageSource.getMessage("msg.i.card_updated",null, locale);
			redirectAttributes.addFlashAttribute("msg", new OpMsg("I",msg));
			vocabularyCardRepository.saveAndFlush(vc);
			return "redirect:/vocabularyCard";
		}else {
			model.addAttribute("editedCard", vc);
			String msg=messageSource.getMessage("msg.e.input_something_wrong",null,locale);
			model.addAttribute("msg", new OpMsg("E",msg));
			return "editForm";
		}

	}

	@PostMapping("/vocabularyCard/cancel")
	public String cancel() {
		return "redirect:/vocabularyCard";
	}
	@PostMapping("/vocabularyCard/addTag")
	public String addTug(@RequestParam(name="cardId",required=false)String cardId,
			@RequestParam("draggedTag")String draggedTag) {
		VocabularyCard vc=vocabularyCardRepository.findById(Integer.parseInt(cardId)).get();
		if(vc.getTag1()==null||vc.getTag1().isEmpty()) {
			vc.setTag1(draggedTag);
			vocabularyCardRepository.saveAndFlush(vc);
		}else if(vc.getTag2()==null||vc.getTag2().isEmpty()) {
			vc.setTag2(draggedTag);
			vocabularyCardRepository.saveAndFlush(vc);
		}else if(vc.getTag3()==null||vc.getTag3().isEmpty()) {
			vc.setTag3(draggedTag);
			vocabularyCardRepository.saveAndFlush(vc);
		}
		return "redirect:/vocabularyCard";
	}

	private void  messageError(ModelAndView mv,
			RedirectAttributes redirectAttributes,Locale locale) {
		mv.setViewName("loginForm");
		String msg=messageSource.getMessage("msg.e.operation_error",null,locale);
		redirectAttributes.addFlashAttribute("msg", new OpMsg("E",msg));
	}
}
