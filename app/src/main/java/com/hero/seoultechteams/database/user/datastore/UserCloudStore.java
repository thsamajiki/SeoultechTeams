package com.hero.seoultechteams.database.user.datastore;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

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
import com.hero.seoultechteams.database.CloudStore;
import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.datastore.TeamCacheStore;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.todo.datastore.TodoCloudStore;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UserCloudStore extends CloudStore<UserData> {

    private final TeamCacheStore teamCacheStore;
    private final TodoCloudStore todoCloudStore;

    public UserCloudStore(Context context, TeamCacheStore teamCacheStore, TodoCloudStore todoCloudStore) {
        super(context);

        this.teamCacheStore = teamCacheStore;
        this.todoCloudStore = todoCloudStore;
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
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, userData);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(false, null);
                        }
                    }
                });
    }

    public void getDataByUserName(final OnCompleteListener<List<UserData>> onCompleteListener, String userName) {
        if (TextUtils.isEmpty(userName)) {
            onCompleteListener.onComplete(false, null);
            return;
        }
        getFirestore().collection("User")
                .whereEqualTo("name", userName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            onCompleteListener.onComplete(true, Collections.emptyList());
                            return;
                        }
                        ArrayList<UserData> userDataList = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            UserData userData = snapshot.toObject(UserData.class);
                            userDataList.add(userData);
                        }
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, userDataList);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(false, null);
                        }
                    }
                });
    }

    public void getDataByUserEmail(final OnCompleteListener<List<UserData>> onCompleteListener, String userEmail) {
        if (TextUtils.isEmpty(userEmail)) {
            onCompleteListener.onComplete(false, null);
            return;
        }
        getFirestore().collection("User")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, userDataList);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(false, null);
                        }
                    }
                });
    }
    
    @Override
    public void getDataList(final OnCompleteListener<List<UserData>> onCompleteListener, Object... params) {
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

    @Override
    public void add(final OnCompleteListener<UserData> onCompleteListener, final UserData userData) {
        getFirestore().collection("User")
                .document(userData.getKey())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, userData);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(false, null);
                        }
                    }
                });
    }

    public void addUserListToTeam(final OnCompleteListener<List<UserData>> onCompleteListener, final TeamData teamData, List<UserData> userDataList, List<MemberData> memberDataList) {
        getFirestore().runTransaction(new Transaction.Function<List<UserData>>() {
            @Nullable
            @Override
            public List<UserData> apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                MemberData newMemberData = new MemberData();

                for (UserData userData : userDataList) {
                    if (isNotMember(userData, memberDataList)) {
                        newMemberData.setProfileImageUrl(userData.getProfileImageUrl());
                        newMemberData.setName(userData.getName());

                        newMemberData.setTeamKey(teamData.getTeamKey());

                        newMemberData.setEmail(userData.getEmail());

                        newMemberData.setKey(userData.getKey());

                        DocumentReference teamRef = getFirestore().collection("Team").document(teamData.getTeamKey());
                        DocumentReference memberRef = teamRef.collection("Member").document(userData.getKey());
                        transaction.set(memberRef, newMemberData);

                        DocumentReference userRef = getFirestore().collection("User")
                                .document(userData.getKey());
                        DocumentReference myTeamRef = userRef.collection("MyTeam")
                                .document(teamData.getTeamKey());

                        transaction.set(myTeamRef, teamData);
                    }
                }

                return userDataList;
            }
        }).addOnSuccessListener(new OnSuccessListener<List<UserData>>() {
            @Override
            public void onSuccess(List<UserData> userDataList) {
//                UserCacheStore.getInstance().add(null, userDataList);
                if (onCompleteListener != null) {
                    onCompleteListener.onComplete(true, userDataList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (onCompleteListener != null) {
                    onCompleteListener.onComplete(false, null);
                }
            }
        });
    }

    private boolean isNotMember(UserData data, List<MemberData> teamMemberDataList) {
        return !isMember(data, teamMemberDataList);
    }

    private boolean isMember(UserData data, List<MemberData> teamMemberDataList) {
        for (MemberData memberData : teamMemberDataList) {
            if (data.getKey().equals(memberData.getKey())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(final OnCompleteListener<UserData> onCompleteListener, final UserData userData) {

        teamCacheStore.getDataList(new OnCompleteListener<List<TeamData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TeamData> teamList) {
                todoCloudStore.getDataList(new OnCompleteListener<List<TodoData>>() {
                    @Override
                    public void onComplete(boolean isSuccess, List<TodoData> todoList) {
                        if (isSuccess) {
                            if (teamList == null && todoList == null) {
                                // 그냥 업데이트
                                updateUser(onCompleteListener, userData);
                            } else {
                                // 트랜잭션을 통한 업데이트
                                updateUserWithMemberAndTodo(onCompleteListener, teamList, todoList, userData);
                            }
                        }
                    }
                }, DataType.MY, userData.getKey());
            }
        }, userData.getKey());
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

    // FirebaseAuth 객체 수정
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

    private void updateUserWithMemberAndTodo(final OnCompleteListener<UserData> onCompleteListener,
                                             final List<TeamData> teamDataList,
                                             final List<TodoData> todoDataList,
                                             final UserData userData) {
        getFirestore().runTransaction(new Transaction.Function<Object>() {
            @Nullable
            @Override
            public Object apply(@NonNull final Transaction transaction) throws FirebaseFirestoreException {

                // 1. UserData의 profileImageUrl, name
                HashMap<String, Object> editUserData = new HashMap<>();
                if (!TextUtils.isEmpty(userData.getProfileImageUrl())) {
                    editUserData.put("profileImageUrl", userData.getProfileImageUrl());
                }
                editUserData.put("name", userData.getName());
                DocumentReference userRef = getFirestore().collection("User")
                        .document(userData.getKey());
                transaction.update(userRef, editUserData);


                // 2. TodoData의 managerProfileImageUrl, managerName
                HashMap<String, Object> editTodoData = new HashMap<>();
                if (!TextUtils.isEmpty(userData.getProfileImageUrl())) {
                    editTodoData.put("managerProfileImageUrl", userData.getProfileImageUrl());

                }
                editTodoData.put("managerName", userData.getName());

                for (TodoData todoData : todoDataList) {
                    DocumentReference todoRef = getFirestore()
                            .collection("Team")
                            .document(todoData.getTeamKey())
                            .collection("Todo")
                            .document(todoData.getTodoKey());

                    transaction.update(todoRef, editTodoData);
                }


                // 3. MemberData의 profileImageUrl, name
                HashMap<String, Object> editMemberData = new HashMap<>();
                if (!TextUtils.isEmpty(userData.getProfileImageUrl())) {
                    editMemberData.put("profileImageUrl", userData.getProfileImageUrl());
                }
                editMemberData.put("name", userData.getName());
                for (TeamData teamData : teamDataList) {
                    DocumentReference memberRef = getFirestore()
                            .collection("Team")
                            .document(teamData.getTeamKey())
                            .collection("Member")
                            .document(userData.getKey());
                    transaction.update(memberRef, editUserData);
                }

                transaction.update(userRef, editUserData);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Object>() {
            @Override
            public void onSuccess(Object o) {
                updateLocalUser(userData);
                if (onCompleteListener != null) {
                    onCompleteListener.onComplete(true, userData);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (onCompleteListener != null) {
                    onCompleteListener.onComplete(false, null);
                }
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

                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(false, null);
                        }
                    }
                });
    }



    @Override
    public void remove(final OnCompleteListener<UserData> onCompleteListener, final UserData userData) {
    }

    private void removeUser(final OnCompleteListener<UserData> onCompleteListener, List<TeamData> teamDataList, UserData userData) {
        getFirestore().runTransaction(new Transaction.Function<Object>() {
            @Nullable
            @Override
            public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference userRef = getFirestore().collection("User")
                        .document(userData.getKey());
                transaction.delete(userRef);

                for (TeamData teamData : teamDataList) {
                    DocumentReference memberRef = getFirestore()
                            .collection("Team")
                            .document(teamData.getTeamKey())
                            .collection("Member")
                            .document(userData.getKey());
                    transaction.delete(memberRef);
                }


                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Object>() {
            @Override
            public void onSuccess(Object o) {
                if (onCompleteListener != null) {
                    onCompleteListener.onComplete(true, userData);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (onCompleteListener != null) {
                    onCompleteListener.onComplete(false, null);
                }
            }
        });
    }
}