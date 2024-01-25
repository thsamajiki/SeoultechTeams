package com.hero.seoultechteams.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.hero.seoultechteams.database.notice.entity.NoticeData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FireStore {

//    1. 나의 팀 목록을 불러오는데, where 조건절로 기준 필드를 정해오기
//    -> where userKey = ~~~ and teamKey = ~~~;
//
//    2. 팀별로 할 일 목록을 불러오는데, where 조건절로 기준 필드를 정해오기
//    -> where teamKey = ~~~ and todoKey = ~~~;
//
//    3. 팀별로 멤버 목록을 불러오는데, where 조건절로 기준 필드를 정해오기
//    -> where userKey = ~~~ and teamKey = ~~~;
//
//    4. 나의 할 일 목록을 불러오는데, where 조건절로 기준 필드를 정해오기
//    -> where userKey = ~~~ and teamKey = ~~~  and todoKey = ~~~;
//
//    5. 공지사항을 불러오는데, where 조건절로 기준 필드를 정해오기
//    -> where noticeKey = ~~~;


    // 데이터 쓰기
    public void addTeamData(final TeamData teamData, final OnCompleteListener<TeamData> onCompleteListener) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.runTransaction(new Transaction.Function<TeamData>() {
            @Nullable
            @Override
            public TeamData apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference teamRef = firestore.collection("Team").document();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                teamData.setLeaderKey(firebaseUser.getUid());
                teamData.setTeamKey(teamRef.getId());
                transaction.set(teamRef, teamData);
                DocumentReference myTeamRef = firestore.collection("MyTeam").document(teamRef.getId());
                transaction.set(myTeamRef, teamData);
                return teamData;
            }
        }).addOnSuccessListener(new OnSuccessListener<TeamData>() {
            @Override
            public void onSuccess(TeamData teamData) {
                onCompleteListener.onComplete(true, teamData);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onCompleteListener.onComplete(false, null);
            }
        });
    }

    public void addTodoData(String teamKey, final TodoData todoData, final OnCompleteListener<TodoData> onCompleteListener) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference todoRef = firestore.collection("Team")
                .document(teamKey)
                .collection("Todo")
                .document();
        todoData.setTodoKey(todoRef.getId());
        todoRef.set(todoData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        onCompleteListener.onComplete(true, todoData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

    public void addMemberData(String teamKey, final UserData userData, final OnCompleteListener<UserData> onCompleteListener) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference userRef = firestore.collection("Team")
                .document(teamKey)
                .collection("Member")
                .document(firebaseUser.getUid());
        userRef.set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    public void addMyTodoData(String userKey, final TodoData todoData, final OnCompleteListener<TodoData> onCompleteListener) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference todoRef = firestore.collection("User")
                .document(userKey)
                .collection("MyTodo")
                .document();
        todoData.setTodoKey(todoRef.getId());
        todoRef.set(todoData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        onCompleteListener.onComplete(true, todoData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

//    public void addMyNotificationData(final MyNotificationData myNotificationData, final OnCompleteListener<MyNotificationData> onCompleteListener) {
//        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DocumentReference myNotificationRef = firestore.collection("User")
//                .document(firebaseUser.getUid())
//                .collection("MyNotification")
//                .document();
//        myNotificationData.setNotificationKey(myNotificationRef.getId());
//        myNotificationRef.set(myNotificationData)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        onCompleteListener.onComplete(true, myNotificationData);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        onCompleteListener.onComplete(false, null);
//                    }
//                });
//    }

    public void addNoticeData(final NoticeData noticeData, final OnCompleteListener<NoticeData> onCompleteListener) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        DocumentReference noticeRef = firestore.collection("Notice").document();
        noticeRef.set(noticeData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        onCompleteListener.onComplete(true, noticeData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }


    // 데이터 가져오기
    public void loadMyTeamData(final OnCompleteListener<List<TeamData>> onCompleteListener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore.collection("User")
                .document(firebaseUser.getUid())
                .collection("MyTeam")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            return;
                        }
                        List<TeamData> teamDataList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                            TeamData teamData = documentSnapshot.toObject(TeamData.class);
                            teamDataList.add(teamData);
                        }
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

    public void loadTodoData(String teamKey, final OnCompleteListener<List<TodoData>> onCompleteListener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Team")
                .document(teamKey)
                .collection("Todo")
                .orderBy("postDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            return;
                        }
                        List<TodoData> myTodoDataList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                            TodoData todoData = documentSnapshot.toObject(TodoData.class);
                            myTodoDataList.add(todoData);
                        }
                        onCompleteListener.onComplete(true, myTodoDataList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

    public void loadMemberData(String teamKey, final OnCompleteListener<List<UserData>> onCompleteListener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference teamRef = firestore.collection("Team").document();
        firestore.collection("Team")
                .document(teamKey)
                .collection("Member")
                .orderBy("name", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            return;
                        }
                        List<UserData> userDataList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                            UserData userData = documentSnapshot.toObject(UserData.class);
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

    public void loadMyTodoData(final OnCompleteListener<List<TodoData>> onCompleteListener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore.collectionGroup("User")
                .whereEqualTo("key", currentUser.getUid())
                .orderBy("postDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            return;
                        }
                        List<TodoData> mytodoDataList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                            TodoData todoData = documentSnapshot.toObject(TodoData.class);
                            mytodoDataList.add(todoData);
                        }
                        onCompleteListener.onComplete(true, mytodoDataList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

//    public void loadMyNotificationData(final OnCompleteListener<List<MyNotificationData>> onCompleteListener) {
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        firestore.collectionGroup("User")
//                .whereEqualTo("key", currentUser.getUid())
//                .orderBy("notificationDate", Query.Direction.DESCENDING)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if (queryDocumentSnapshots.isEmpty()) {
//                            return;
//                        }
//                        List<MyNotificationData> myNotificationDataList = new ArrayList<>();
//                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
//                            MyNotificationData myNotificationData = documentSnapshot.toObject(MyNotificationData.class);
//                            myNotificationDataList.add(myNotificationData);
//                        }
//                        onCompleteListener.onComplete(true, myNotificationDataList);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        onCompleteListener.onComplete(false, null);
//                    }
//                });
//    }

    public void loadNoticeData(final OnCompleteListener<List<NoticeData>> onCompleteListener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Notice")
                .orderBy("postDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            return;
                        }
                        List<NoticeData> noticeDataList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                            NoticeData noticeData = documentSnapshot.toObject(NoticeData.class);
                            noticeDataList.add(noticeData);
                        }
                        onCompleteListener.onComplete(true, noticeDataList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }



    // 데이터 쿼리

    // 갱신
    public void updateTeamData(String teamKey, HashMap<String, Object> editData, final OnCompleteListener<Void> onCompleteListener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("TeamData")
                .document(teamKey)
                .update(editData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    public void updateTodoData(String teamKey, String todoKey, HashMap<String, Object> editData, final OnCompleteListener<Void> onCompleteListener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Team")
                .document(teamKey)
                .collection("Todo")
                .document(todoKey)
                .update(editData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    public void updateMyTodoData(String userKey, String todoKey, HashMap<String, Object> editData, final OnCompleteListener<Void> onCompleteListener) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("User")
                .document(userKey)
                .collection("MyTodo")
                .document(todoKey)
                .update(editData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    public void updateMemberData(String teamKey, String userKey, HashMap<String, Object> editData, final OnCompleteListener<Void> onCompleteListener) {
        final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("Team")
                .document(teamKey)
                .collection("Member")
                .document(userKey)
                .update(editData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    public void updateMyProfileData(String userKey, HashMap<String, Object> editData, final OnCompleteListener<Void> onCompleteListener) {
        final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("User")
                .document(userKey)
                .update(editData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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



    // 삭제
    public void deleteTeamData(String teamKey, final TeamData teamData, final OnCompleteListener<TeamData> onCompleteListener) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Team")
                .document(teamKey)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    public void deleteTodoData(String teamKey, String todoKey, final TodoData todoData, final OnCompleteListener<TodoData> onCompleteListener) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Team")
                .document(teamKey)
                .collection("Todo")
                .document(todoKey)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        onCompleteListener.onComplete(true, todoData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

    public void deleteMemberData(String teamKey, String userKey, final UserData userData, final OnCompleteListener<UserData> onCompleteListener) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore.collection("Team")
                .document(teamKey)
                .collection("Member")
                .document(userKey)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

//    public void deleteMyNotificationData(final MyNotificationData myNotificationData, final OnCompleteListener<MyNotificationData> onCompleteListener) {
//        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        firestore.collection("User")
//                .document(firebaseUser.getUid())
//                .collection("MyNotification")
//                .document()
//                .delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        onCompleteListener.onComplete(true, myNotificationData);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        onCompleteListener.onComplete(false, null);
//                    }
//                });
//    }
}
