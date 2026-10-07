package com.example.likelion14th_springboot.service;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 토큰 확인
        String accessToken = userRequest.getAccessToken().getTokenValue();
        System.out.println("Access Token: " + accessToken);
        //

        OAuth2User oAuth2User = super.loadUser(userRequest);
//        System.out.println("OAuth attributes = " + oAuth2User.getAttributes());


//        String email = oAuth2User.getAttribute("email");
//        String username = oAuth2User.getAttribute("name");
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email;
        String username;

        if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            email = (String) kakaoAccount.get("email");
            username = (String) profile.get("nickname");
        } else {
            email = oAuth2User.getAttribute("email");
            username = oAuth2User.getAttribute("name");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> Member.builder()
                        .email(email)
                        .name(username)
                        .password("") // OAuth 사용자는 비밀번호가 필요하지 않으므로 빈 문자열로 처리
                        .build());

        // 새로운 사용자는 DB에 저장
        if (member.getId() == null) {
            memberRepository.save(member);
        }

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                userNameAttributeName);
    }
}