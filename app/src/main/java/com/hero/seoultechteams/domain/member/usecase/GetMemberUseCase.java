package com.hero.seoultechteams.domain.member.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.member.repository.MemberRepository;

public class GetMemberUseCase {
    private MemberRepository memberRepository;

    public GetMemberUseCase(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
