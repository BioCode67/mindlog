package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<다룰 대상, ID의 타입>
// 이것만 상속받으면 저장, 조회, 삭제 기능이 자동으로 생깁니다. (마법!)
public interface QuoteRepository extends JpaRepository<Quote, Long> {
}