package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

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
}