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
//                        UserCacheStore.getInstance().add(userData);
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
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {  // 쿼리를 날린 후의 결과를 스냅샷으로 찍어서 보내준다.
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
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {  // 쿼리를 날린 후의 결과를 스냅샷으로 찍어서 보내준다.
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

    // 회원가입시 호출되는 메소드
    @Override
    public void add(final OnCompleteListener<UserData> onCompleteListener, final UserData userData) {
        getFirestore().collection("User")
                .document(userData.getKey())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        UserCacheStore.getInstance().add(null, userData);
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
            @NonNull
            @Override
            public List<UserData> apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                MemberData newMemberData = new MemberData();

                for (UserData userData : userDataList) {
                    if (isNotMember(userData, memberDataList)) {
                        // db에 저장할 객체 만듦
                        newMemberData.setProfileImageUrl(userData.getProfileImageUrl());
                        newMemberData.setName(userData.getName());

                        newMemberData.setTeamKey(teamData.getTeamKey());

                        newMemberData.setEmail(userData.getEmail());

                        newMemberData.setKey(userData.getKey());

//                        memberDataList.add(newMemberData);

                        // db에 저장함
                        DocumentReference teamRef = getFirestore().collection("Team").document(teamData.getTeamKey());
                        DocumentReference memberRef = teamRef.collection("Member").document(userData.getKey());
                        transaction.set(memberRef, newMemberData);

                        //
                        DocumentReference userRef = getFirestore().collection("User")
                                .document(userData.getKey());
                        DocumentReference myTeamRef = userRef.collection("MyTeam")
                                .document(teamData.getTeamKey());

                        // 내 팀 db에 신규 데이터 추가
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

    // FIXME: 2021-05-14 EditProfileActivity를 통해 나의 사용자 정보(나의 프로필 사진, 나의 사용자 이름)가 변경되었을 때
    //  UserCloudStore의 update() 메소드에서 트랜잭션 처리할 때 필요한 데이터
    //  1. UserData의 profileImageUrl, name
    //  2. TodoData의 managerProfileImageUrl, managerName
    //  3. MemberData의 profileImageUrl, name
    @Override
    public void update(final OnCompleteListener<UserData> onCompleteListener, final UserData userData) {
        // 동작을 동일하게 하기 위해, 근데 Repository 호출은 여기서 안할거니까
        // 기존 코드를 보고 똑같이 가져온거.

        /* 최상위에서 하위로
        CloudStore / LocalStore / CacheStore
        ------------------------------------
        ~~RemoteDataSource
        ------------------------------------
        ~~Repository
        ------------------------------------
        ~~UseCase
        ------------------------------------
        ~~Presenter
       */

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

    // FIXME: 2021-05-14 EditProfileActivity 를 통해 나의 사용자 정보(나의 프로필 사진, 나의 사용자 이름)가 변경되었을 때
    //  UserCloudStore 의 update() 메소드에서 트랜잭션 처리할 때 필요한 데이터
    //  1. UserData 의 profileImageUrl, name
    //  2. TodoData 의 managerProfileImageUrl, managerName
    //  3. MemberData 의 profileImageUrl, name
    private void updateUserWithMemberAndTodo(final OnCompleteListener<UserData> onCompleteListener,
                                             final List<TeamData> teamDataList,
                                             final List<TodoData> todoDataList,
                                             final UserData userData) {
        getFirestore().runTransaction(new Transaction.Function<Object>() {
            @Nullable
            @Override
            public Object apply(@NonNull final Transaction transaction) throws FirebaseFirestoreException {

                // 1. UserData 의 profileImageUrl, name
                HashMap<String, Object> editUserData = new HashMap<>();
                if (!TextUtils.isEmpty(userData.getProfileImageUrl())) {
                    editUserData.put("profileImageUrl", userData.getProfileImageUrl());
                }
                editUserData.put("name", userData.getName());
                DocumentReference userRef = getFirestore().collection("User")
                        .document(userData.getKey());
                transaction.update(userRef, editUserData);


                // 2. TodoData 의 managerProfileImageUrl, managerName
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


                // 3. MemberData 의 profileImageUrl, name
                // 파이어베이스 지침상 최대 500까지만 업데이트 가능 -> 팀을 500개 이상 가입하지 못한다는 뜻
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
        FirebaseAuth.getInstance().getCurrentUser().delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (onCompleteListener != null) {
//                            removeUser(onCompleteListener, userData);
                            onCompleteListener.onComplete(true, userData);
                            Log.d("UserCloudStore2", "onComplete: remote remove success");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(false, null);
                            Log.d("UserCloudStore2", "onComplete: remote remove failed");
                        }
                    }
                });

        // 1. 유저정보 삭제

        // 2. 각각의 팀들의 멤버 정보 삭제
        // 3. 각각의 팀들의 투두 정보 삭제
        // 4. 1, 2, 3번이 한 트랜잭션 안에서 무결성을 이뤄야 함
        //
//        teamCacheStore.getDataList(new OnCompleteListener<List<TeamData>>() {
//            @Override
//            public void onComplete(boolean isSuccess, List<TeamData> teamList) {
//                todoCloudStore.getDataList(new OnCompleteListener<List<TodoData>>() {
//                    @Override
//                    public void onComplete(boolean isSuccess, List<TodoData> todoList) {
//                        if (isSuccess) {
//                            removeUser(onCompleteListener, teamList, todoList, userData);
//                        } else {
//
//                        }
//                    }
//                });
//            }
//        });
//
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

    private void removeUser(final OnCompleteListener<UserData> onCompleteListener, UserData userData) {
        getFirestore().collection("User")
                .document(userData.getKey())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, userData);
                            Log.d("UserCloudStore1", "onComplete: remote remove success");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(false, null);
                            Log.d("UserCloudStore1", "onComplete: remote remove failed");
                        }
                    }
                });
//        getFirestore().runTransaction(new Transaction.Function<Object>() {
//            @Nullable
//            @Override
//            public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                DocumentReference userRef = getFirestore().collection("User")
//                        .document(userData.getKey());
//                transaction.delete(userRef);


//                for (TodoData todoData : todoDataList) {
//                    DocumentReference todoRef = getFirestore()
//                            .collection("Team")
//                            .document(todoData.getTeamKey())
//                            .collection("Todo")
//                            .document(todoData.getUserKey());
//
//                    transaction.delete(todoRef);
//                }


//                for (TeamData teamData : teamDataList) {
//                    DocumentReference memberRef = getFirestore()
//                            .collection("Team")
//                            .document(teamData.getTeamKey())
//                            .collection("Member")
//                            .document(userData.getKey());
//                    transaction.delete(memberRef);
//                }
//
//
//                return null;
//            }
//        }).addOnSuccessListener(new OnSuccessListener<Object>() {
//            @Override
//            public void onSuccess(Object o) {
//                if (onCompleteListener != null) {
//                    onCompleteListener.onComplete(true, userData);
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                if (onCompleteListener != null) {
//                    onCompleteListener.onComplete(false, null);
//                }
//            }
//        });
    }
}