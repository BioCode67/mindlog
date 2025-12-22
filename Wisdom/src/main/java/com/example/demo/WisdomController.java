package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Random;

@Controller
public class WisdomController {

    private final QuoteRepository quoteRepository;

    // 생성자 주입: 스프링에게 "QuoteRepository 좀 갖다줘"라고 요청
    @Autowired 
    public WisdomController(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        // 1. 모든 명언 가져오기
        List<Quote> quotes = quoteRepository.findAll();
        
        // (기존 랜덤 로직 유지...)
        String randomQuoteMsg = "저장된 명언이 없습니다.";
        if (!quotes.isEmpty()) {
            Random random = new Random();
            Quote selectedQuote = quotes.get(random.nextInt(quotes.size()));
            randomQuoteMsg = selectedQuote.getContent();
        }

        model.addAttribute("quote", randomQuoteMsg);
        
        // [추가됨] 전체 리스트도 "quotes"라는 이름으로 같이 보냅니다.
        model.addAttribute("quotes", quotes); 
        
        return "index";
    }

	@PostMapping("/add")
	public String addQuote(@RequestParam("text") String text) {
		// text: HTML 폼에서 <input name="text">에 적힌 내용을 받아옵니다. (시각 정보 수집)

		if (text != null && !text.isEmpty()) {
			// 1. 날것의 정보(String)를 기억 세포(Entity) 형태로 가공합니다.
			Quote newQuote = new Quote(text);

			// 2. 해마(Repository)에게 넘겨서 장기 기억(DB)에 각인시킵니다.
			quoteRepository.save(newQuote);
		}

		// 3. "저장 끝났으니 다시 메인 화면으로 돌아가!" (새로고침 효과)
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
}