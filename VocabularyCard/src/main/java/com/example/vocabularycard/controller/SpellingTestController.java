package com.example.vocabularycard.controller;

import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.vocabularycard.common.OpMsg;
import com.example.vocabularycard.dao.VocabularyCardDaoImpl;
import com.example.vocabularycard.entity.Tag;
import com.example.vocabularycard.entity.VocabularyCard;
import com.example.vocabularycard.form.TagQuery;
import com.example.vocabularycard.form.VocabularyCardQuery;
import com.example.vocabularycard.repository.TagRepository;
import com.example.vocabularycard.repository.VocabularyCardRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SpellingTestController {
	private final VocabularyCardRepository vocabularyCardRepository;
	private final HttpSession session;
	private final TagRepository tagRepository;
	private final MessageSource messageSource;
	@PersistenceContext
	private EntityManager entityManager;
	VocabularyCardDaoImpl vocabularyCardDaoImpl;

	@PostConstruct
	public void init(){
		vocabularyCardDaoImpl=new VocabularyCardDaoImpl(entityManager);
	}

	@GetMapping("/vocabularyCard/spellingTest")
	public ModelAndView showCard(ModelAndView mv) {
		//追加フォームがないこと以外VocabularyCardController#showCardと同じ
		Integer accountId=(Integer)session.getAttribute("accountId");
		mv.setViewName("spellingTest");
		List<VocabularyCard> cardList=vocabularyCardRepository.findByUserIdOrderByIdDesc(accountId);
		mv.addObject("cardList", cardList);
		List<Tag> tagList=tagRepository.findByUserIdOrderByIdDesc(accountId);
		mv.addObject("tagList", tagList);
		mv.addObject("vocabularyCardQuery", new VocabularyCardQuery());
		mv.addObject("tagQuery", new TagQuery());
		return mv;
	}

	@PostMapping("/vocabularyCard/spellingTest/search")
	public ModelAndView searchCard(@ModelAttribute VocabularyCardQuery vocabularyCardQuery,
			ModelAndView mv,
			RedirectAttributes redirectAttributes,
			Locale locale
			) {
		Integer accountId=(Integer)session.getAttribute("accountId");
		mv.setViewName("spellingTest");
		List<VocabularyCard> cardList=vocabularyCardDaoImpl.findByCriteria(vocabularyCardQuery, accountId);
		mv.addObject("cardList", cardList);
		List<Tag> tagList=tagRepository.findByUserIdOrderByIdDesc(accountId);
		mv.addObject("tagList", tagList);
		mv.addObject("vocabularyCardQuery", new VocabularyCardQuery());
		mv.addObject("tagQuery", new TagQuery());
		if(cardList.size()==0) {
			String msg=messageSource.getMessage("msg.w.card_not_found", null, locale);
			mv.addObject("msg", new OpMsg("W",msg));
		}

		return mv;
	}
}
