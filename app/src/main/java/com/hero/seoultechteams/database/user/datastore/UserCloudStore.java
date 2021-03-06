package com.hero.seoultechteams.database.user.datastore;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.hero.seoultechteams.database.CacheStore;
import com.hero.seoultechteams.database.CloudStore;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.member.datastore.MemberCacheStore;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.TeamRepository;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.database.user.entity.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class UserCloudStore extends CloudStore<UserData> {

    public UserCloudStore(Context context) {
        super(context);
    }

    @Override
    public void getData(final OnCompleteListener<UserData> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }
        String userKey = params[0].toString();
        getFirestore().collection("User")
                .document(userKey)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserData userData = documentSnapshot.toObject(UserData.class);
                        UserCacheStore.getInstance().add(userData);
                        onCompleteListener.onComplete(true, userData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

    public void getDataByUserName(final OnCompleteListener<ArrayList<UserData>> onCompleteListener, String userName) {
        if (TextUtils.isEmpty(userName)) {
            onCompleteListener.onComplete(false, null);
            return;
        }
        getFirestore().collection("User")
                .whereEqualTo("name", userName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {  // ????????? ?????? ?????? ????????? ??????????????? ????????? ????????????.
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            onCompleteListener.onComplete(true, null);
                            return;
                        }
                        ArrayList<UserData> userDataList = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            UserData userData = snapshot.toObject(UserData.class);
                            userDataList.add(userData);
                        }
                        onCompleteListener.onComplete(true, userDataList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

    public void getDataByUserEmail(final OnCompleteListener<ArrayList<UserData>> onCompleteListener, String userEmail) {
        if (TextUtils.isEmpty(userEmail)) {
            onCompleteListener.onComplete(false, null);
            return;
        }
        getFirestore().collection("User")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {  // ????????? ?????? ?????? ????????? ??????????????? ????????? ????????????.
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            onCompleteListener.onComplete(true, null);
                            return;
                        }
                        ArrayList<UserData> userDataList = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            UserData userData = snapshot.toObject(UserData.class);
                            userDataList.add(userData);
                        }
                        onCompleteListener.onComplete(true, userDataList);
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
    public void getDataList(final OnCompleteListener<ArrayList<UserData>> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }
        getFirestore().collection("User")
                .document()
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

    // ??????????????? ???????????? ?????????
    @Override
    public void add(final OnCompleteListener<UserData> onCompleteListener, final UserData userData) {
        getFirestore().collection("User")
                .document(userData.getKey())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        UserCacheStore.getInstance().add(null, userData);
                        onCompleteListener.onComplete(true, userData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

    public void addUserListToTeam(final OnCompleteListener<ArrayList<UserData>> onCompleteListener, final TeamData teamData, ArrayList<UserData> userDataList, ArrayList<MemberData> memberDataList) {
        getFirestore().runTransaction(new Transaction.Function<ArrayList<UserData>>() {
            @Nullable
            @Override
            public ArrayList<UserData> apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                Log.d("qwer9", "UserCloudStore-addUserListToTeam() - ???????????? ?????? ???????????? ??? : " + userDataList.size());
                for (UserData userData1 : userDataList) {
                    Log.d("qwer10", "UserCloudStore-addUserListToTeam - ???????????? ?????? ???????????? ?????? : " + userData1.getName());
                }

                Log.d("qwer11", "UserCloudStore-???????????? ??? ????????? ??? : " + memberDataList.size());
                for (MemberData memberData : memberDataList) {
                    Log.d("qwer12", "UserCloudStore-???????????? ??? ????????? ?????? : " + memberData.getName());
                }

                MemberData newMemberData = new MemberData();
//                for (MemberData memberData : memberDataList) {
//                    for (UserData userData : userDataList) {
//                        if (!userData.getKey().equals(memberData.getKey())) {
//                            Log.d("qwer13", "UserCloudStore-???????????? ??? ????????? ??? : " + memberDataList.size());
//                            for (MemberData memberData01 : memberDataList) {
//                                Log.d("qwer14", "UserCloudStore-???????????? ??? ????????? ?????? : " + memberData01.getName());
//                            }
//                            newMemberData.setProfileImageUrl(userData.getProfileImageUrl());
//                            newMemberData.setName(userData.getName());
//
//                            newMemberData.setTeamKey(teamData.getTeamKey());
//
//                            newMemberData.setEmail(userData.getEmail());
//
//                            newMemberData.setKey(userData.getKey());
//
//                            memberDataList.add(newMemberData);
//                            Log.d("qwer15", "UserCloudStore-????????? ??? ????????? ??? : " + memberDataList.size());
//                            for (MemberData memberData02 : memberDataList) {
//                                Log.d("qwer16", "UserCloudStore-????????? ??? ????????? ?????? : " + memberData02.getName());
//                            }
//                        }
//                        Log.d("qwer17", "UserCloudStore- userDataList() : " + userDataList.size());
//                        for (UserData user: userDataList) {
//                            Log.d("qwer18", "UserCloudStore- userDataList() : " + user.getName());
//                        }
//                    }
//                    //Log.d("qwer-no_print", "UserCloudStore- userData.getName() : " + userData.getName());
//                }

                for (UserData userData : userDataList) {
                    if (isNotMember(userData, memberDataList)) {
                        newMemberData.setProfileImageUrl(userData.getProfileImageUrl());
                        newMemberData.setName(userData.getName());

                        newMemberData.setTeamKey(teamData.getTeamKey());

                        newMemberData.setEmail(userData.getEmail());

                        newMemberData.setKey(userData.getKey());

                        memberDataList.add(newMemberData);
                    }
                }

                for (UserData data : userDataList) {
                    DocumentReference teamRef = getFirestore().collection("Team").document(teamData.getTeamKey());
                    DocumentReference memberRef = teamRef.collection("Member").document(data.getKey());
                    transaction.set(memberRef, newMemberData);

                    DocumentReference userRef = getFirestore().collection("User")
                            .document(data.getKey());
                    DocumentReference myTeamRef = userRef.collection("MyTeam")
                            .document(teamData.getTeamKey());
                    transaction.set(myTeamRef, teamData);
                }
                return userDataList;
            }
        }).addOnSuccessListener(new OnSuccessListener<ArrayList<UserData>>() {
            @Override
            public void onSuccess(ArrayList<UserData> userDataList) {
//                UserCacheStore.getInstance().add(null, userDataList);
                onCompleteListener.onComplete(true, userDataList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("qwerOnFailure", "onFailure: " + e.getMessage());
                onCompleteListener.onComplete(false, null);
            }
        });
    }

    private boolean isNotMember(UserData data, ArrayList<MemberData> teamMemberDataList) {
        return !isMember(data, teamMemberDataList);
    }

    private boolean isMember(UserData data, ArrayList<MemberData> teamMemberDataList) {
        for (MemberData memberData : teamMemberDataList) {
            if (data.getKey().equals(memberData.getKey())) {
                return true;
            }
        }
        return false;
    }

    // FIXME: 2021-05-14 EditProfileActivity??? ?????? ?????? ????????? ??????(?????? ????????? ??????, ?????? ????????? ??????)??? ??????????????? ???
    //  UserCloudStore??? update() ??????????????? ???????????? ????????? ??? ????????? ?????????
    //  1. UserData??? profileImageUrl, name
    //  2. TodoData??? managerProfileImageUrl, managerName
    //  3. MemberData??? profileImageUrl, name
    @Override
    public void update(final OnCompleteListener<UserData> onCompleteListener, final UserData userData) {
        TeamRepository teamRepository = new TeamRepository(getContext());
        teamRepository.getTeamList(new OnCompleteListener<ArrayList<TeamData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TeamData> data) {
                if (isSuccess) {
                    if (data == null) {
                        // ?????? ????????????
                        updateUser(onCompleteListener, userData);
                    } else {
                        // ??????????????? ?????? ????????????
                        updateUserWithMemberAndTodo(onCompleteListener, data, userData);
                    }
                }
            }
        });
    }

    private void updateUser(final OnCompleteListener<UserData> onCompleteListener, final UserData userData) {
        HashMap<String, Object> editData = new HashMap<>();
        if (!TextUtils.isEmpty(userData.getProfileImageUrl())) {
            editData.put("profileImageUrl", userData.getProfileImageUrl());
        }
        editData.put("name", userData.getName());

        getFirestore().collection("User")
                .document(userData.getKey())
                .update(editData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateLocalUser(userData);
                        onCompleteListener.onComplete(true, userData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

    public void updateLocalUser(UserData userData) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder()
                .setDisplayName(userData.getName());
        if (!TextUtils.isEmpty(userData.getProfileImageUrl())) {
            builder.setPhotoUri(Uri.parse(userData.getProfileImageUrl()));
        }
        UserProfileChangeRequest request = builder.build();
        firebaseUser.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    // FIXME: 2021-05-14 EditProfileActivity??? ?????? ?????? ????????? ??????(?????? ????????? ??????, ?????? ????????? ??????)??? ??????????????? ???
    //  UserCloudStore??? update() ??????????????? ???????????? ????????? ??? ????????? ?????????
    //  1. UserData??? profileImageUrl, name
    //  2. TodoData??? managerProfileImageUrl, managerName
    //  3. MemberData??? profileImageUrl, name
    private void updateUserWithMemberAndTodo(final OnCompleteListener<UserData> onCompleteListener,
                                             final ArrayList<TeamData> teamDataList,
                                             final UserData userData) {
        getFirestore().runTransaction(new Transaction.Function<Object>() {
            @Nullable
            @Override
            public Object apply(@NonNull final Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference userRef = getFirestore().collection("User").document(userData.getKey());
//                DocumentReference teamRef = getFirestore().collection("Team").document();
//                TodoData todoData = transaction.get(teamRef).toObject(TodoData.class);
//                DocumentReference todoRef = getFirestore().collection("Team")
//                        .document(todoData.getTeamKey())
//                        .collection("Todo")
//                        .document(todoData.getTodoKey());
                // ?????????????????? ????????? ?????? 500????????? ???????????? ?????? -> ?????? 500??? ?????? ???????????? ???????????? ???
                HashMap<String, Object> editUserData = new HashMap<>();
                if (!TextUtils.isEmpty(userData.getProfileImageUrl())) {
                    editUserData.put("profileImageUrl", userData.getProfileImageUrl());
                    //editUserData.put("managerProfileImageUrl", todoData.getManagerProfileImageUrl());
                }
                editUserData.put("name", userData.getName());
//                editUserData.put("managerName", todoData.getManagerName());
                for (TeamData teamData : teamDataList) {
                    DocumentReference memberRef = getFirestore()
                            .collection("Team")
                            .document(teamData.getTeamKey())
                            .collection("Member")
                            .document(userData.getKey());
                    transaction.update(memberRef, editUserData);

                    
//                    transaction.update(todoRef, editUserData);
                }
                transaction.update(userRef, editUserData);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Object>() {
            @Override
            public void onSuccess(Object o) {
                UserCacheStore.getInstance().update(null, userData);
                updateLocalUser(userData);
                onCompleteListener.onComplete(true, userData);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onCompleteListener.onComplete(false, null);
            }
        });
    }

    private void getMemberDataList(final OnCompleteListener<ArrayList<MemberData>> onCompleteListener, final UserData userData) {
        getFirestore().collectionGroup("Member")
                .whereEqualTo("key", userData.getKey())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<MemberData> memberDataList = new ArrayList<>();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapShot : queryDocumentSnapshots) {
                                memberDataList.add(documentSnapShot.toObject(MemberData.class));
                            }
                            onCompleteListener.onComplete(true, memberDataList);
                            return;
                        }
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

    /* TODO: 2021-03-12 ?????? ?????? ????????? ?????? ??? ???
    * 1. ???????????? ??????
    * 2. ????????? ????????? ?????? ?????? ??????
    * 3. ????????? ????????? ?????? ?????? ??????
    * 4. 1, 2, 3?????? ??? ???????????? ????????? ???????????? ????????? ???
    */ 

    @Override
    public void remove(final OnCompleteListener<UserData> onCompleteListener, final UserData userData) {
//        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        firestore.collection("Team")
//                .document(userData.getTeamKey())
//                .collection("Member")
//                .document(userData.getUserKey())
//                .delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        onCompleteListener.onComplete(true, userData);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        onCompleteListener.onComplete(false, null);
//                    }
//                });
    }
}