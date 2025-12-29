package com.example.demo;

import jakarta.persistence.Column; // [추가 1] 이거 임포트 해야 합니다!
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Quote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// [추가 2] length = 2000 (2000자까지 저장 가능하게 늘림)
	// columnDefinition = "TEXT" 를 쓰면 무제한도 가능합니다.
	@Column(length = 2000)
	private String content;

	// 카테고리 변수 추가
	private String category;

	// [추가] 작성자 연결 (N : 1 관계)
	// FetchType.LAZY: "작성자 정보는 필요할 때만 천천히 가져와라" (성능 최적화 필수 옵션!)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id") // DB에는 'member_id'라는 숫자로 저장됨
	private Member writer;

	public Quote() {
	}

	public Quote(String content) {
		this.content = content;
	}

	// --- [여기서부터 추가하세요] ---

	// 1. ID를 꺼내는 문 (이게 없어서 에러가 난 겁니다!)
	public Long getId() {
		return id;
	}

	// 2. 내용을 꺼내는 문 (이건 원래 있었죠?)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// 2. Getter & Setter 추가 (필수!)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	// Getter & Setter 추가
	public Member getWriter() {
		return writer;
	}

	public void setWriter(Member writer) {
		this.writer = writer;
	}

}