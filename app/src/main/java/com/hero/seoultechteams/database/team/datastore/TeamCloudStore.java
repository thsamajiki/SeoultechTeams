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
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.database.user.entity.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamCloudStore extends CloudStore<TeamData> {

    public TeamCloudStore(Context context) {
        super(context);
    }

    @Override
    public void getData(OnCompleteListener<TeamData> onCompleteListener, Object... params) {

    }

    @Override
    public void getDataList(final OnCompleteListener<ArrayList<TeamData>> onCompleteListener, Object... params) {
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
                            onCompleteListener.onComplete(true, null);
                            return;
                        }
                        ArrayList<TeamData> teamDataList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                            TeamData teamData = documentSnapshot.toObject(TeamData.class);
                            teamDataList.add(teamData);
                        }
                        // FIXME: 2021-03-19 ???????????? ???????????? ??????????????? ??? ?????????????????? ???????????? ??????
                        //TeamLocalStore.getInstance().addAll(teamDataList);
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
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                teamData.setLeaderKey(firebaseUser.getUid());
                teamData.setTeamKey(teamRef.getId());

                DocumentReference myRef = getFirestore().collection("User")
                        .document(firebaseUser.getUid());
                DocumentReference myTeamRef = myRef.collection("MyTeam")
                        .document(teamRef.getId());

                MemberData memberData = new MemberData();
                UserData userData = transaction.get(myRef).toObject(UserData.class);
                memberData.setProfileImageUrl(userData.getProfileImageUrl());
                memberData.setName(userData.getName());
                memberData.setTeamKey(teamData.getTeamKey());
                memberData.setEmail(userData.getEmail());
                memberData.setKey(firebaseUser.getUid());

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

    // FIXME: 2021-05-14 TeamDetailActivity??? ?????? TeamData??? ????????? ??????????????? ???,
    //  TeamCloudStore??? update() ??????????????? ???????????? ????????? ??? ????????? ?????????
    //  1. TeamData
    //     1) teamName
    //     2) teamDesc
    //  2. TodoData
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
        /* TODO: 2021-03-12 ??? ???????????? ?????? ??? ???
        * 1. ??? ????????? ??????
        * 2. ?????? ????????? ??????
        * 3. ?????? ????????? ??????
        * 4. 1, 2, 3?????? ??? ???????????? ????????? ???????????? ????????? ???
         */
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