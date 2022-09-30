package com.hero.seoultechteams.domain.member.entity;

public class ParticipationEntity {

    private int countTodoRegularSubmitted;
    private int countTodoLateSubmitted;
    private int countTodoResubmitted;
    private int countTodoConfirmed;
    private int countTodoDismissed;
    private int memberTodoFaithfulPercent;
    private int memberTodoPerformancePercent;
    private int memberTodoLatePercent;

    public ParticipationEntity(int countTodoRegularSubmitted,
                               int countTodoLateSubmitted,
                               int countTodoResubmitted,
                               int countTodoConfirmed,
                               int countTodoDismissed,
                               int memberTodoFaithfulPercent,
                               int memberTodoPerformancePercent, int memberTodoLatePercent) {
        this.countTodoRegularSubmitted = countTodoRegularSubmitted;
        this.countTodoLateSubmitted = countTodoLateSubmitted;
        this.countTodoResubmitted = countTodoResubmitted;
        this.countTodoConfirmed = countTodoConfirmed;
        this.countTodoDismissed = countTodoDismissed;
        this.memberTodoFaithfulPercent = memberTodoFaithfulPercent;
        this.memberTodoPerformancePercent = memberTodoPerformancePercent;
        this.memberTodoLatePercent = memberTodoLatePercent;
    }

    //   - 멤버들 평가 기준
//  1. 성실도 = todoFaithfulPercent = 마감시간 전에 제출한 것에 대한 척도 = ((정상)제출한 횟수 / ((정상)제출한 횟수 + 지연제출한 횟수)) * 100
//  2. 불성실도(지연율) = todoLatePercent = 마감시간 후 제출한 것에 대한 척도 = (지연제출한 횟수 / ((정상)제출한 횟수 + 지연제출한 횟수) * 100
//
//  (*** 다시 제출한 횟수, 승인 횟수, 반려 횟수는 성실도 및 불성실도에 반영되지 않음 ***)
//
//  - 예시
//  어느 팀 멤버가 10개의 Todo를 담당했다.
//  => 담당한 Todo의 총 개수 = (정상)제출한 횟수 + 지연제출한 횟수
//
//
//  7개의 Todo를 마감시간 전에 (정상)제출하고,
//  3개의 Todo를 마감시간 후에 지연제출하면,
//  => 성실도 = (7 / 10) * 100 = 70%
//  => 불성실도 = (3 / 10) * 100 = 30%
//
//  3. 성과도 = todoPerformancePercent = 승인을 받은 정도에 대한 척도 = (승인된 횟수 / 총 제출한 횟수) * 100
//                                     = (승인된 횟수 / ((정상)제출한 횟수 + 지연제출한 횟수 + 다시 제출한 횟수)) * 100


    public int getCountTodoDismissed() {
        return countTodoDismissed;
    }

    public void setCountTodoDismissed(int countTodoDismissed) {
        this.countTodoDismissed = countTodoDismissed;
    }

    public int getCountTodoRegularSubmitted() {
        return countTodoRegularSubmitted;
    }

    public void setCountTodoRegularSubmitted(int countTodoRegularSubmitted) {
        this.countTodoRegularSubmitted = countTodoRegularSubmitted;
    }

    public int getCountTodoLateSubmitted() {
        return countTodoLateSubmitted;
    }

    public void setCountTodoLateSubmitted(int countTodoLateSubmitted) {
        this.countTodoLateSubmitted = countTodoLateSubmitted;
    }

    public int getCountTodoResubmitted() {
        return countTodoResubmitted;
    }

    public void setCountTodoResubmitted(int countTodoResubmitted) {
        this.countTodoResubmitted = countTodoResubmitted;
    }

    public int getCountTodoConfirmed() {
        return countTodoConfirmed;
    }

    public void setCountTodoConfirmed(int countTodoConfirmed) {
        this.countTodoConfirmed = countTodoConfirmed;
    }

    public int getMemberTodoFaithfulPercent() {
        return memberTodoFaithfulPercent;
    }

    public void setMemberTodoFaithfulPercent(int memberTodoFaithfulPercent) {
        this.memberTodoFaithfulPercent = memberTodoFaithfulPercent;
    }

    public int getMemberTodoPerformancePercent() {
        return memberTodoPerformancePercent;
    }

    public void setMemberTodoPerformancePercent(int memberTodoPerformancePercent) {
        this.memberTodoPerformancePercent = memberTodoPerformancePercent;
    }

    public int getMemberTodoLatePercent() {
        return memberTodoLatePercent;
    }

    public void setMemberTodoLatePercent(int memberTodoLatePercent) {
        this.memberTodoLatePercent = memberTodoLatePercent;
    }
}
