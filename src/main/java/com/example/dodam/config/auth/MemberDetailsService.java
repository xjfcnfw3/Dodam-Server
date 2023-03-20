package com.example.dodam.config.auth;

import com.example.dodam.domain.member.Member;
import com.example.dodam.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService{

	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService : 진입");
		Member member = memberRepository.findByUsername(username)
			.orElseThrow();
		return new MemberDetails(member);	// 여기서 return 되면 session 에 들어감 -> 권한 관리를 위해서만 사용됨
	}
}
