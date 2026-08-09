package com.example.likelion14th_springboot.service;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException());
    }

    public Page<Member> getMembersByPage(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return memberRepository.findAll(pageable);
    }

    public Page<Member> getAdultMembersSortedByName(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return memberRepository.findByAgeGreaterThanEqual(20, pageable);
    }

    public Page<Member> getMembersByNamePrefix(String prefix, Pageable pageable){
        return memberRepository.findByNameStartingWith(prefix, pageable);
    }
}
