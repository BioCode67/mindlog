package com.example.demo.api;

import com.example.demo.Member; // [추가] Member 클래스 import
import com.example.demo.MemberRepository; // [추가] MemberRepository import
import com.example.demo.Quote;
import com.example.demo.QuoteRepository;
import com.example.demo.dto.QuoteDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag; // [수정] annotaations -> annotations (오타 수정)

import org.springframework.security.core.userdetails.UserDetails;
import java.security.Principal;

@Tag(name = "명언 관리", description = "명언 조회 및 추가 관련 API")
@RestController
@RequestMapping("/api/v1/quotes")
public class QuoteApiController {

	private final QuoteRepository quoteRepository;
	private final MemberRepository memberRepository;

	public QuoteApiController(QuoteRepository quoteRepository, MemberRepository memberRepository) {
		this.quoteRepository = quoteRepository;
		this.memberRepository = memberRepository;
	}

	// 1. [조회] 모든 명언 조회
	@Operation(summary = "모든 명언 조회", description = "DB에 저장된 모든 명언 목록을 JSON으로 반환합니다.")
	@GetMapping
	public List<QuoteDto> getAllQuotes() {
		List<Quote> quotes = quoteRepository.findAll();
		return quotes.stream().map(quote -> new QuoteDto(quote)).collect(Collectors.toList());
	}

	// 5. [상세 조회] 명언 하나만 가져오기
	// GET http://localhost:8080/api/v1/quotes/75
	@Operation(summary = "명언 상세 조회", description = "ID로 특정 명언 하나를 조회합니다.")
	@GetMapping("/{id}")
	public QuoteDto getQuote(@PathVariable("id") Long id) {

		Quote quote = quoteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

		return new QuoteDto(quote);
	}

	// 2. [추가] 명언 추가 (로그인한 사용자 정보 포함)
	@Operation(summary = "명언 추가", description = "새로운 명언 데이터를 받아 DB에 저장합니다.")
	@PostMapping
	public QuoteDto createQuote(@RequestBody QuoteDto quoteDto, Principal principal) {

		// (1) 현재 로그인한 사람의 아이디(username)를 꺼냅니다.
		String username = principal.getName();

		// (2) DB에서 회원 객체(Member)를 가져옵니다.
		Member member = memberRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

		// (3) 명언 객체 생성 및 정보 주입
		Quote quote = new Quote(quoteDto.getContent());
		quote.setCategory(quoteDto.getCategory());
		quote.setWriter(member); // [핵심] 작성자 정보 저장

		Quote savedQuote = quoteRepository.save(quote);

		return new QuoteDto(savedQuote);
	}

	// 3. [수정] 내 글 수정하기
	@Operation(summary = "명언 수정", description = "작성자 본인만 글을 수정할 수 있습니다.")
	@PutMapping("/{id}") // 예: PUT /api/v1/quotes/1
	public QuoteDto updateQuote(@PathVariable("id") Long id, @RequestBody QuoteDto quoteDto, Principal principal) {

		// (1) 수정할 글 찾기
		Quote quote = quoteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

		// (2) 권한 검사 (핵심!)
		// 글의 작성자가 없거나(옛날 글), 현재 로그인한 사람과 아이디가 다르면 -> 튕겨냄!
		if (quote.getWriter() == null || !quote.getWriter().getUsername().equals(principal.getName())) {
			throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
		}

		// (3) 통과했으면 수정 진행
		quote.setContent(quoteDto.getContent());
		quote.setCategory(quoteDto.getCategory());

		Quote updatedQuote = quoteRepository.save(quote);
		return new QuoteDto(updatedQuote);
	}

	// 4. [삭제] 내 글 삭제하기
	@Operation(summary = "명언 삭제", description = "작성자 본인만 글을 삭제할 수 있습니다.")
	@DeleteMapping("/{id}") // 예: DELETE /api/v1/quotes/1
	public String deleteQuote(@PathVariable("id") Long id, Principal principal) {

		// (1) 삭제할 글 찾기
		Quote quote = quoteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

		// (2) 권한 검사
		if (quote.getWriter() == null || !quote.getWriter().getUsername().equals(principal.getName())) {
			throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
		}

		// (3) 삭제 진행
		quoteRepository.delete(quote);
		return "삭제가 완료되었습니다.";
	}
}