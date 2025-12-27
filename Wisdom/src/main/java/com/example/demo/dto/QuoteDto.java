package com.example.demo.dto;

import com.example.demo.Quote;

// 이 객체는 오직 "데이터 전달"만을 위해 존재합니다. (로직 없음)
public class QuoteDto {
    private Long id;
    private String content;
    private String category;

    // 기본 생성자
    public QuoteDto() {}

    // Entity(날것의 데이터)를 받아서 DTO(포장된 데이터)로 바꾸는 생성자
    public QuoteDto(Quote quote) {
        this.id = quote.getId();
        this.content = quote.getContent();
        this.category = quote.getCategory();
    }

    // Getter & Setter (Lombok이 없으므로 수동 추가)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}