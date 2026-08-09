package com.example.likelion14th_springboot.repository;
import com.example.likelion14th_springboot.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {


    Page<Member> findAll(Pageable pageable);
    Optional<Member> findByEmail(String email);

    Page<Member> findByAgeGreaterThanEqual(int age, Pageable pageable);
    Page<Member> findByNameStartingWith(String prefix, Pageable pageable);

}
