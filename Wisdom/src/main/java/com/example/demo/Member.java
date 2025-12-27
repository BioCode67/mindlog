package com.example.demo;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 아이디 (중복되면 안 되니까 unique = true)
    @Column(unique = true, nullable = false)
    private String username;

    // 비밀번호 (암호화해서 저장할 거라 좀 길게 잡습니다)
    @Column(nullable = false)
    private String password;

    // 이메일 (선택 사항)
    private String email;

    // 역할 (일반유저, 관리자 등)
    private String role; // 예: "ROLE_USER"

    // 기본 생성자
    public Member() {}

    // 생성자
    public Member(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getter (Setter는 가급적 닫아두는 게 보안상 좋습니다)
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}