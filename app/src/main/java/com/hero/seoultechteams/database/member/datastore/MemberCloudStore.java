package com.hero.seoultechteams.database.member.datastore;

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
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.user.entity.UserData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MemberCloudStore extends CloudStore<MemberData> {

    public MemberCloudStore(Context context) {
        super(context);
    }

    @Override
    public void getData(OnCompleteListener<MemberData> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }
        String memberKey = params[0].toString();
        getFirestore().collection("Member")
                .document(memberKey)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        MemberData memberData = documentSnapshot.toObject(MemberData.class);
                        MemberCacheStore.getInstance().add(memberData);
                        onCompleteListener.onComplete(true, memberData);
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
    public void getDataList(final OnCompleteListener<List<MemberData>> onCompleteListener, Object... params) {
        String teamKey = params[0].toString();
        getFirestore().collection("Team")
                .document(teamKey)
                .collection("Member")
                .orderBy("name", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            onCompleteListener.onComplete(true, null);
                            return;
                        }
                        ArrayList<MemberData> memberDataList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            MemberData memberData = documentSnapshot.toObject(MemberData.class);
                            memberDataList.add(memberData);
                        }
                        Collections.sort(memberDataList);

                        MemberCacheStore.getInstance().addAll(memberDataList);
                        onCompleteListener.onComplete(true, memberDataList);
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
    public void add(final OnCompleteListener<MemberData> onCompleteListener, final MemberData memberData) {
        getFirestore().runTransaction(new Transaction.Function<MemberData>() {
            @Override
            public MemberData apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                TeamData teamData = new TeamData();
                DocumentReference teamRef = getFirestore().collection("Team").document(teamData.getTeamKey());

                memberData.setTeamKey(teamRef.getId());

                DocumentReference userRef = getFirestore().collection("User")
                        .document(memberData.getKey());
                DocumentReference myTeamRef = userRef.collection("MyTeam")
                        .document(teamRef.getId());

                UserData userData = transaction.get(userRef).toObject(UserData.class);
                memberData.setProfileImageUrl(userData.getProfileImageUrl());
                memberData.setName(userData.getName());
                memberData.setTeamKey(teamData.getTeamKey());
                memberData.setEmail(userData.getEmail());
                memberData.setKey(userData.getKey());

                DocumentReference memberRef = teamRef.collection("Member")
                        .document(memberData.getKey());
                transaction.set(teamRef, teamData);
                transaction.set(memberRef, memberData);
                transaction.set(myTeamRef, teamData);

                return memberData;
            }
        }).addOnSuccessListener(new OnSuccessListener<MemberData>() {
            @Override
            public void onSuccess(MemberData memberData) {
                onCompleteListener.onComplete(true, memberData);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onCompleteListener.onComplete(false, null);
            }
        });
    }

//    public void addNewMemberList(final OnCompleteListener<ArrayList<MemberData>> onCompleteListener, final TeamData teamData, ArrayList<UserData> userDataList, ArrayList<MemberData> memberDataList) {
//
//        getFirestore().runTransaction(new Transaction.Function<ArrayList<MemberData>>() {
//            @Nullable
//            @Override
//            public ArrayList<MemberData> apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                DocumentReference teamRef = getFirestore().collection("Team").document();
//                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//                DocumentReference myRef = getFirestore().collection("User")
//                        .document(firebaseUser.getUid());
//                DocumentReference myTeamRef = myRef.collection("MyTeam")
//                        .document(teamRef.getId());
//
//                MemberData newMemberData = new MemberData();
//                UserData userData = transaction.get(myRef).toObject(UserData.class);
//
//                for (UserData data : userDataList) {
//                    for (MemberData memberData : memberDataList) {
//                        if (!data.getKey().equals(memberData.getKey())) {
//                            newMemberData.setProfileImageUrl(userData.getProfileImageUrl());
//                            newMemberData.setName(userData.getName());
//                            newMemberData.setTeamKey(teamData.getTeamKey());
//                            newMemberData.setEmail(userData.getEmail());
//                            newMemberData.setKey(firebaseUser.getUid());
//                            memberDataList.add(newMemberData);
//                        }
//                    }
//                }
//
//                DocumentReference memberRef = teamRef.collection("Member").document(newMemberData.getKey());
//                transaction.set(teamRef, teamData);
//                transaction.set(memberRef, newMemberData);
//                transaction.set(myTeamRef, teamData);
//                return memberDataList;
//            }
//        }).addOnSuccessListener(new OnSuccessListener<ArrayList<MemberData>>() {
//            @Override
//            public void onSuccess(ArrayList<MemberData> memberDataList) {
//                //MemberCacheStore.getInstance().add(null, memberDataList);
//                onCompleteListener.onComplete(true, memberDataList);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                onCompleteListener.onComplete(false, null);
//            }
//        });
//    }

    @Override
    public void update(final OnCompleteListener<MemberData> onCompleteListener, final MemberData memberData) {
        HashMap<String, Object> editData = new HashMap<>();
        editData.put("name", memberData.getName());
        editData.put("profileImageUrl", memberData.getProfileImageUrl());

        getFirestore().collection("Team")
                .document(memberData.getTeamKey())
                .collection("Member")
                .document(memberData.getKey())
                .update(editData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        MemberCacheStore.getInstance().update(null, memberData);
                        onCompleteListener.onComplete(true, null);
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
    public void remove(final OnCompleteListener<MemberData> onCompleteListener, final MemberData memberData) {
        getFirestore().collection("Team")
                .document(memberData.getTeamKey())
                .collection("Member")
                .document(memberData.getKey())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        MemberCacheStore.getInstance().remove(null, memberData);
                        onCompleteListener.onComplete(true, memberData);
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