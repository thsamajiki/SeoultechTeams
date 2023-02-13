# SeoultechTeams
협업 관리 서비스 앱



## 개요
조별 프로젝트의 목표를 성공적으로 달성하기 위해 개인의 자발적 참여를 유도할 수 있는 협업 관리 서비스


## 프로젝트 기간
- 2020\. 09 ~ 2021\. 06
- 틈틈이 발견된 버그도 수정했습니다.


## 기여도
- 기획, 개발, 디자인 모두 했습니다.


## 사용 프로그램 및 언어
- 사용 프로그램 : Android Studio, Google Firebase, GitHub
- 사용 언어 : Java


## 앱의 버전
- minSdkVersion : 21
- targetSdkVersion : 32


## 이용 대상
- 조별 과제 및 발표를 준비하는 학생들
- 프로젝트 협업을 하는 학생들


## 주요 기능
- 각 팀원은 할 일을 등록하여 마감기한을 설정하고 제출할 수 있다.
- 팀장은 제출된 할 일을 승인/반려할 수 있다.
- 팀을 만들어서 팀원을 초대할 수 있다.
- 팀원들의 협업 참여도를 확인할 수 있다.


## 사용된 기술
- Android Clean Architecture
- MVP 디자인 패턴
- ROOM
- RecyclerView
- ViewBinding
- Swipe Refresh Layout
- Cache Manager
- @StringDef


## 사용된 라이브러리
- 머티리얼 디자인 (com.google.android.material:material:1.6.1)
- 머티리얼 프로그래스바 (me.zhanghai.android.materialprogressbar:library:1.6.1)
- 머리티얼 날짜시간피커 (com.wdullaer:materialdatetimepicker:4.2.3)
- 포토뷰 (com.github.chrisbanes:PhotoView:2.3.0)
- 사진 첨부 (com.github.bumptech.glide:glide:4.11.0) (com.github.bumptech.glide:compiler:4.11.0)
- (프로필) 사진 원형 표시 (de.hdodenhof:circleimageview:3.1.0)
- 스와이프 새로고침 레이아웃 (androidx.swiperefreshlayout:swiperefreshlayout:1.1.0)


## 문제 및 해결 과정
- 협업에 얼마나 열심히 참여했는지에 대해 데이터로 수치화하는 어려움이 있었습니다.<br>
그래서 해야 할 일에 기한을 설정하여 기한 내에 제출하면 성실도에 반영했고,<br>
팀장이 해야 할 일이 적절히 완료되었는지를 평가하여 승인 또는 반려하고 이를 성과도에 반영하는 것으로 데이터를 모델링했습니다.
- 참여도를 평가하기 위한 데이터를 어떻게 모델링할지 고민을 많이 했습니다.<br>
처음에는 Submission이라는 큰 단위를 Todo라는 작은 단위로 구성되도록 모델링했지만 데이터를 처리하는데 많은 어려움이 있을 것으로 생각하여, Submission을 없애고 Todo 단위로 통일했습니다.
- 해야 할 일에 대한 데이터인 TodoData를 제출, 승인, 반려하는 기능을 어떻게 구현할지 고민을 많이 했는데,<br>
TodoData마다 생성, 제출, 승인, 반려된 시점들을 담은 List 변수를 추가했고, 이것이 팀 참여도에 반영되도록 했습니다.
- TodoData의 상태가 제대로 전달되지 않는 문제가 발생했는데, Enum으로는 해결되지 않았습니다.<br>
그래서 @StringDef을 사용하여 TodoData의 상태를 전달하여 TodoList에 표시되는 버튼 이름값을 validate할 수 있었습니다.



## 개발 후 느낀 점
- 데이터를 좀 더 효율적으로 관리할 수 있도록 MVP 디자인 패턴을 채택하는데 우려했던 것과 달리 패턴을 성공적으로 적용할 수 있었습니다.<br>
객체지향적인 관점에서 앱의 구조를 파악할 수 있는 통찰력을 기를 수 있었습니다.
- 클린 아키텍처를 적용하는 과정에서 시행착오가 많았고 예상보다 시간이 걸렸지만 성공적으로 구현했고, 앞으로 배우게 될 어려운 기술을 이해하고 구현할 수 있다는 자신감을 가지게 되었습니다. 또한 이번 개발을 계기로 클린 코드와 리팩토링에 관심을 가지게 되었습니다.

## 스크린샷
<img src="/images/Splash.png" width="150px" height="320px" title="Splash" alt="Splash"></img>
<img src="/images/Login.png" width="150px" height="320px" title="Login" alt="Login"></img>
<img src="/images/SignUp.png" width="150px" height="320px" title="SignUp" alt="SignUp"></img>
<img src="/images/MyTodoList.png" width="150px" height="320px" title="MyTodoList" alt="MyTodoList"></img>
<img src="/images/TeamList.png" width="150px" height="320px" title="TeamList" alt="TeamList"></img>
<img src="/images/CreateTeam.png" width="150px" height="320px" title="CreateTeam" alt="CreateTeam"></img>
<img src="/images/TeamTodoList.png" width="150px" height="320px" title="TeamTodoList" alt="TeamTodoList"></img>
<img src="/images/CreateTodo.png" width="150px" height="320px" title="CreateTodo" alt="CreateTodo"></img>
<img src="/images/TeamMemberList.png" width="150px" height="320px" title="TeamMemberList" alt="TeamMemberList"></img>
<img src="/images/MemberEvaluation.png" width="150px" height="320px" title="MemberEvaluation" alt="MemberEvaluation"></img>
<img src="/images/InviteList.png" width="150px" height="320px" title="InviteList" alt="InviteList"></img>
<img src="/images/Profile.png" width="150px" height="320px" title="Profile" alt="Profile"></img>
<img src="/images/ProfileEdit.png" width="150px" height="320px" title="ProfileEdit" alt="ProfileEdit"></img>



## 시연 영상
<img src="/images/서울텍팀즈앱_시연.gif" width="330px" height="640px" title="test_video" alt="Test_video"></img>


## 구글 플레이스토어 출시
https://play.google.com/store/apps/details?id=com.hero.seoultechteams
