package com.example.demo.api;

import com.example.demo.Member;
import com.example.demo.MemberRepository;
import com.example.demo.dto.AuthDto;
import com.example.demo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth") // 보안 설정에서 이곳은 '통과'시켜뒀었죠?
public class AuthController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 기계
    private final JwtUtil jwtUtil; // 토큰 발급 기계

    public AuthController(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 1. 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody AuthDto authDto) {
        // 비밀번호를 암호화해서 저장 (예: "1234" -> "xkdk12...")
        String encodedPassword = passwordEncoder.encode(authDto.getPassword());
        
        Member member = new Member(authDto.getUsername(), encodedPassword, "ROLE_USER");
        memberRepository.save(member);
        
        return "회원가입 완료! 로그인 해주세요.";
    }

    // 2. 로그인 (성공하면 토큰을 줍니다!)
    @PostMapping("/login")
    public String login(@RequestBody AuthDto authDto) {
        // (1) 아이디로 회원 찾기
        Member member = memberRepository.findByUsername(authDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));

        // (2) 비밀번호 확인 (입력한 거랑 DB에 있는 암호화된 거랑 비교)
        if (!passwordEncoder.matches(authDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        // (3) 인증 성공! 토큰 발급 (이걸로 문을 따고 들어갑니다)
        return jwtUtil.createToken(member.getUsername());
    }
}