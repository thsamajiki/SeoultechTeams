package com.hero.seoultechteams.database.team.datastore;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.hero.seoultechteams.database.CloudStore;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TeamCloudStore extends CloudStore<TeamData> {

    private final TeamLocalStore teamLocalStore;
    private final TeamCacheStore teamCacheStore;
    public TeamCloudStore(Context context, TeamLocalStore teamLocalStore, TeamCacheStore teamCacheStore) {
        super(context);
        this.teamLocalStore = teamLocalStore;
        this.teamCacheStore = teamCacheStore;
    }

    @Override
    public void getData(OnCompleteListener<TeamData> onCompleteListener, Object... params) {
        String teamKey = params[0].toString();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getFirestore().collection("User")
                .document(firebaseUser.getUid())
                .collection("MyTeam")
                .whereEqualTo(teamKey, "teamKey")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            onCompleteListener.onComplete(true, null);
                            return;
                        } else {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            TeamData teamData = documentSnapshot.toObject(TeamData.class);

//                            for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
//                                TeamData teamData = documentSnapshot.toObject(TeamData.class);
//                                if (teamData.getTeamKey().equals(teamKey)) {
//                                    teamLocalStore.add(onCompleteListener, teamData);
//                                    onCompleteListener.onComplete(true, teamData);
//                                }
//                            }

                            teamLocalStore.add(null, teamData);
                            TeamCacheStore.getInstance().add(null, teamData);
                            onCompleteListener.onComplete(true, teamData);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

    @Override
    public void getDataList(final OnCompleteListener<List<TeamData>> onCompleteListener, Object... params) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getFirestore().collection("User")
                .document(firebaseUser.getUid())
                .collection("MyTeam")
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            onCompleteListener.onComplete(true, Collections.emptyList());
                            return;
                        }
                        ArrayList<TeamData> teamDataList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                            TeamData teamData = documentSnapshot.toObject(TeamData.class);
                            teamDataList.add(teamData);
                        }
                        // FIXME: 2021-03-19 팀데이터 불러와서 로컬스토어 및 캐시스토어에 저장하기 추가
//                        teamLocalStore.addAll(teamDataList);
                        TeamCacheStore.getInstance().addAll(teamDataList);
                        onCompleteListener.onComplete(true, teamDataList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

    @Override
    public void add(final OnCompleteListener<TeamData> onCompleteListener, final TeamData teamData) {
        getFirestore().runTransaction(new Transaction.Function<TeamData>() {
            @Nullable
            @Override
            public TeamData apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference teamRef = getFirestore().collection("Team").document();

                teamData.setTeamKey(teamRef.getId());

                DocumentReference myRef = getFirestore().collection("User")
                        .document(teamData.getLeaderKey());
                DocumentReference myTeamRef = myRef.collection("MyTeam")
                        .document(teamRef.getId());

                MemberData memberData = new MemberData();
                UserData userData = transaction.get(myRef).toObject(UserData.class);
                memberData.setProfileImageUrl(userData.getProfileImageUrl());
                memberData.setName(userData.getName());
                memberData.setTeamKey(teamData.getTeamKey());
                memberData.setEmail(userData.getEmail());
                memberData.setKey(teamData.getLeaderKey());

                DocumentReference memberRef = teamRef.collection("Member")
                        .document(memberData.getKey());
                transaction.set(teamRef, teamData);
                transaction.set(memberRef, memberData);
                transaction.set(myTeamRef, teamData);
                return teamData;
            }
        }).addOnSuccessListener(new OnSuccessListener<TeamData>() {
            @Override
            public void onSuccess(TeamData teamData) {
                TeamCacheStore.getInstance().add(null, teamData);
                onCompleteListener.onComplete(true, teamData);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onCompleteListener.onComplete(false, null);
            }
        });
    }

    // FIXME: 2021-05-14 TeamDetailActivity를 통해 TeamData의 정보가 변경되었을 때,
    //  TeamCloudStore의 update() 메소드에서 트랜잭션 처리할 때 필요한 데이터
    //  1. TeamData
    //     1) teamName
    //     2) teamDesc
    //  2. TodoEntity
    //     1) teamName
    @Override
    public void update(final OnCompleteListener<TeamData> onCompleteListener, final TeamData teamData) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String, Object> editData = new HashMap<>();
        editData.put("teamName", teamData.getTeamName());
        editData.put("teamDesc", teamData.getTeamDesc());

        getFirestore().runTransaction(new Transaction.Function<TeamData>() {
            @Nullable
            @Override
            public TeamData apply(@NonNull final Transaction transaction) throws FirebaseFirestoreException {

                DocumentReference teamRef = getFirestore().collection("Team")
                        .document(teamData.getTeamKey());
                DocumentReference myRef = getFirestore().collection("User")
                        .document(firebaseUser.getUid());
                DocumentReference myTeamRef = myRef.collection("MyTeam")
                        .document(teamRef.getId());

//                DocumentReference todoRef = getFirestore().collection("Team")
//                        .document(teamData.getTeamKey())
//                        .collection("Todo")
//                        .document(firebaseUser.getUid());

                transaction.update(teamRef, editData);
                transaction.update(myTeamRef, editData);
//                transaction.update(todoRef, editData);

                return null;
            }
        })
       .addOnSuccessListener(new OnSuccessListener<Object>() {
           @Override
           public void onSuccess(Object o) {
               onCompleteListener.onComplete(true, teamData);
           }
       })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onCompleteListener.onComplete(false, null);
            }
        });
    }

    @Override
    public void remove(final OnCompleteListener<TeamData> onCompleteListener, final TeamData teamData) {
        /* TODO: 2021-03-12 팀 삭제시에 해야 할 일
        * 1. 팀 컬렉션 삭제
        * 2. 멤버 데이터 삭제
        * 3. 투두 데이터 삭제
        * 4. 1, 2, 3번이 한 트랜잭션 안에서 무결성을 이뤄야 함
         */
        getFirestore().runTransaction(new Transaction.Function<TeamData>() {
            @Nullable
            @Override
            public TeamData apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                return null;
            }
        });

        getFirestore().collection("Team")
                .document(teamData.getTeamKey())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        TeamCacheStore.getInstance().remove(null, teamData);
                        onCompleteListener.onComplete(true, teamData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }
}