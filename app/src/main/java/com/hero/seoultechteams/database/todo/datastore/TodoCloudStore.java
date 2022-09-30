package com.hero.seoultechteams.database.todo.datastore;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hero.seoultechteams.database.CloudStore;
import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.todo.entity.TodoData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TodoCloudStore extends CloudStore<TodoData> {

    private static TodoCloudStore instance;

    private TodoCloudStore(Context context) {
        super(context);
    }

    public static TodoCloudStore getInstance(Context context) {
        if (instance == null) {
            instance = new TodoCloudStore(context);
        }

        return instance;
    }

    @Override
    public void getData(OnCompleteListener<TodoData> onCompleteListener, Object... params) {

    }

    @Override
    public void getDataList(OnCompleteListener<List<TodoData>> onCompleteListener, Object... params) {
        DataType type = (DataType) params[0];
        String key = params[1].toString();
        switch (type) {
            case MY:
                loadMyTodoData(key, onCompleteListener);
                break;
            case TEAM:
                loadTeamTodoData(key, onCompleteListener);
                break;
            default:
                throw new IllegalArgumentException("정의되지 않은 타입입니다.");
        }
    }

    @Override
    public void add(final OnCompleteListener<TodoData> onCompleteListener, final TodoData todoData) {
        DocumentReference todoRef = getFirestore().collection("Team")
                .document(todoData.getTeamKey())
                .collection("Todo")
                .document();
        todoData.setTodoKey(todoRef.getId());
        todoRef.set(todoData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        TodoCacheStore.getInstance().add(null, todoData);
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

    @Override
    public void update(final OnCompleteListener<TodoData> onCompleteListener, final TodoData todoData) {
        HashMap<String, Object> editData = new HashMap<>();
        editData.put("eventHistory", todoData.getEventHistory());
        editData.put("todoState", todoData.getTodoState());
        editData.put("todoTitle", todoData.getTodoTitle());
        editData.put("todoDesc", todoData.getTodoDesc());
        getFirestore().collection("Team")
                .document(todoData.getTeamKey())
                .collection("Todo")
                .document(todoData.getTodoKey())
                .update(editData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (todoData.getUserKey().equals(firebaseUser.getUid())) {
                            TodoCacheStore.getInstance().update(null, todoData);
                        }
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

    @Override
    public void remove(final OnCompleteListener<TodoData> onCompleteListener, final TodoData todoData) {
        getFirestore().collection("Team")
                .document(todoData.getTeamKey())
                .collection("Todo")
                .document(todoData.getTodoKey())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        TodoCacheStore.getInstance().remove(null, todoData);
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

    private void loadTeamTodoData(String teamKey, final OnCompleteListener<List<TodoData>> onCompleteListener) {
        getFirestore().collection("Team")
                .document(teamKey)
                .collection("Todo")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            onCompleteListener.onComplete(true, null);
                            return;
                        }
                        List<TodoData> teamTodoDataList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                            TodoData todoData = documentSnapshot.toObject(TodoData.class);
                            teamTodoDataList.add(todoData);
                            TodoCacheStore.getInstance().update(null, todoData);
                        }
                        Collections.sort(teamTodoDataList);

                        onCompleteListener.onComplete(true, teamTodoDataList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("qwer-OnFailure", "onFailure: " + e.getMessage());
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

    private void loadMyTodoData(String userKey, final OnCompleteListener<List<TodoData>> onCompleteListener) {
        getFirestore().collectionGroup("Todo")
                .whereEqualTo("userKey", userKey)
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
                        Collections.sort(myTodoDataList);
                        TodoCacheStore.getInstance().addAll(myTodoDataList);
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
}