package com.example.demo.api;

import com.example.demo.Quote;
import com.example.demo.QuoteRepository;
import com.example.demo.dto.QuoteDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation; // import 추가
import io.swagger.v3.oas.annotations.tags.Tag;

// @RestController = @Controller + @ResponseBody
// "나는 화면(HTML) 말고 데이터(JSON)만 뱉겠다"는 선언입니다.
@Tag(name = "명언 관리", description = "명언 조회 및 추가 관련 API") // 컨트롤러 이름표
@RestController
@RequestMapping("/api/v1/quotes") // 주소를 멋지게 구조화합니다. (버전 관리 포함)
public class QuoteApiController {

    private final QuoteRepository quoteRepository;

    public QuoteApiController(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    // 1. [조회] 모든 명언을 JSON으로 주세요.
    // GET http://localhost:8080/api/v1/quotes
    @Operation(summary = "모든 명언 조회", description = "DB에 저장된 모든 명언 목록을 JSON으로 반환합니다.")
    @GetMapping
    public List<QuoteDto> getAllQuotes() {
        List<Quote> quotes = quoteRepository.findAll();
        
        // 반복문(Stream)을 돌면서 Quote(Entity)를 QuoteDto(포장지)로 바꿉니다.
        return quotes.stream()
                .map(quote -> new QuoteDto(quote))
                .collect(Collectors.toList());
    }

    // 2. [추가] 명언을 추가해 주세요. (JSON으로 받음)
    // POST http://localhost:8080/api/v1/quotes
    // @RequestBody: "요청 몸통(Body)에 들어있는 JSON을 자바 객체로 바꿔줘!"
    @Operation(summary = "명언 추가", description = "새로운 명언 데이터를 받아 DB에 저장합니다.")
    @PostMapping
    public QuoteDto createQuote(@RequestBody QuoteDto quoteDto) {
        Quote quote = new Quote(quoteDto.getContent());
        quote.setCategory(quoteDto.getCategory());
        
        Quote savedQuote = quoteRepository.save(quote);
        
        return new QuoteDto(savedQuote); // 저장된 결과를 다시 보여줌
    }
}