package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
    // 로그인할 때 아이디로 회원을 찾아야 하니까 필요합니다.
    // Optional은 "찾았는데 없을 수도 있다"는 걸 명시하는 안전한 상자입니다.
    Optional<Member> findByUsername(String username);
}