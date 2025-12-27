package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<다룰 대상, ID의 타입>
// 이것만 상속받으면 저장, 조회, 삭제 기능이 자동으로 생깁니다. (마법!)
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    
    // [검색 기능]
    // 규칙: findBy + 필드명 + Containing (SQL의 LIKE %검색어% 와 같습니다)
    
    // 1. 내용(Content)에 특정 단어가 포함된 것 찾기
    List<Quote> findByContentContaining(String keyword);
    
    // 2. 카테고리(Category)가 일치하는 것 찾기 (필요하다면 나중에 사용)
    List<Quote> findByCategory(String category);
}