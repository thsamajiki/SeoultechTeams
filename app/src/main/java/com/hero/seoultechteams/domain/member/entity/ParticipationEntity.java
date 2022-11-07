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
