package com.hero.seoultechteams.domain.member.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.member.repository.MemberRepository;

import java.util.List;

public class GetMemberListUseCase {
    private MemberRepository memberRepository;

    public GetMemberListUseCase(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void invoke(final OnCompleteListener<List<MemberEntity>> onCompleteListener, final String teamKey) {
        memberRepository.getMemberList(onCompleteListener, teamKey);
    }
}
