package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WisdomController {

    private final QuoteRepository quoteRepository;

    // 생성자 주입: 스프링에게 "QuoteRepository 좀 갖다줘"라고 요청
    @Autowired 
    public WisdomController(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

 // WisdomController.java

    @GetMapping("/")
    // @RequestParam(required = false): 검색어는 없을 수도 있으니까 에러 내지 말라는 뜻
    public String home(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        
        List<Quote> quotes;

        if (keyword != null && !keyword.isEmpty()) {
            // 1. 검색어가 있으면 -> 검색해서 가져오기
            quotes = quoteRepository.findByContentContaining(keyword);
        } else {
            // 2. 검색어가 없으면 -> 전체 다 가져오기 (기존 로직)
            quotes = quoteRepository.findAll();
        }
        
        // ... (랜덤 명언 뽑기 로직은 그대로 유지하거나, 리스트가 비었을 때 예외처리 추가) ...
        // 편의상 랜덤 로직은 생략하고 리스트만 보냅니다. (원하시면 기존 코드 유지하세요)
        
        model.addAttribute("quotes", quotes);
        model.addAttribute("keyword", keyword); // 검색창에 검색어 남겨두기 위해
        
        return "index"; // index.html 보여줌
    }

 // 1. [추가] 기능 수정
    @PostMapping("/add")
    public String addQuote(@RequestParam("text") String text, 
                           @RequestParam("category") String category) { // category 추가됨
        
        if (text != null && !text.isEmpty()) {
            Quote newQuote = new Quote(text);
            newQuote.setCategory(category); // 카테고리 저장
            quoteRepository.save(newQuote);
        }
        return "redirect:/";
    }
	// [삭제 기능]
	// 주소 모양: /delete/1 (1번 기억을 지워라), /delete/5 (5번 기억을 지워라)
	// @PathVariable: 주소창에 있는 숫자(id)를 변수로 낚아채는 기술입니다.
	@PostMapping("/delete/{id}")
	public String deleteQuote(@org.springframework.web.bind.annotation.PathVariable("id") Long id) {

		// 1. 해마(Repository)에게 ID를 주고 삭제하라고 명령합니다.
		quoteRepository.deleteById(id);

		// 2. 삭제 후 다시 메인 화면으로 돌아갑니다.
		return "redirect:/";
	}
	
	// [수정 화면 보여주기]
	// 예: /edit/3 -> 3번 글을 수정하는 화면으로 이동
	@GetMapping("/edit/{id}")
	public String editForm(@org.springframework.web.bind.annotation.PathVariable("id") Long id, Model model) {
	    // 1. 수정할 데이터를 DB에서 찾아옵니다. (없으면 에러 처리 대신 null 반환)
	    Quote quote = quoteRepository.findById(id).orElse(null);
	    
	    if (quote != null) {
	        model.addAttribute("quote", quote); // 수정할 데이터를 모델에 담음
	        return "edit"; // edit.html을 보여줘라
	    } else {
	        return "redirect:/"; // 데이터가 없으면 그냥 메인으로 도망가기
	    }
	}

	// [수정 요청 처리하기]
	// 예: /update/3 -> 3번 글을 실제로 변경해서 저장
	// 2. [수정] 기능 수정
	@PostMapping("/update/{id}")
	public String updateQuote(@PathVariable("id") Long id, 
	                          @RequestParam("text") String text,
	                          @RequestParam("category") String category) { // category 추가됨
	    
	    Quote quote = quoteRepository.findById(id).orElse(null);
	    
	    if (quote != null) {
	        quote.setContent(text);
	        quote.setCategory(category); // 카테고리 수정 반영
	        quoteRepository.save(quote);
	    }
	    return "redirect:/";
	}
}