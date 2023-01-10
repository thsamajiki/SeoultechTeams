package com.hero.seoultechteams.database.todo.datastore;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.hero.seoultechteams.database.CloudStore;
import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TodoCloudStore extends CloudStore<TodoData> {

    private static TodoCloudStore instance;
    private final TodoLocalStore todoLocalStore;
    private final TodoCacheStore todoCacheStore;

    private TodoCloudStore(Context context, TodoLocalStore todoLocalStore, TodoCacheStore todoCacheStore) {
        super(context);
        this.todoLocalStore = todoLocalStore;
        this.todoCacheStore = todoCacheStore;
    }

    public static TodoCloudStore getInstance(Context context, TodoLocalStore todoLocalStore, TodoCacheStore todoCacheStore) {
        if (instance == null) {
            instance = new TodoCloudStore(context, todoLocalStore, todoCacheStore);
        }

        return instance;
    }

    @Override
    public void getData(OnCompleteListener<TodoData> onCompleteListener, Object... params) {
        String todoKey = params[0].toString();

        getFirestore().collectionGroup("Todo")
                .whereEqualTo("todoKey", todoKey)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            onCompleteListener.onComplete(true, null);
                            return;
                        }

                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        TodoData todoData = documentSnapshot.toObject(TodoData.class);

                        todoLocalStore.add(null, todoData);
                        TodoCacheStore.getInstance().add(todoData);
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, todoData);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("getTodo-error", "onFailure: " + e.getMessage());
                        onCompleteListener.onComplete(false, null);
                    }
                });
    }

    @Override
    public void getDataList(OnCompleteListener<List<TodoData>> onCompleteListener, Object... params) {
        DataType type = (DataType) params[0];
        String key = params[1].toString();
        switch (type) {
            case MY:
                loadMyTodoDataList(key, onCompleteListener);
                break;
            case TEAM:
                loadTeamTodoDataList(key, onCompleteListener);
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
                        todoLocalStore.add(null, todoData);
                        TodoCacheStore.getInstance().add(null, todoData);
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, todoData);
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
    public void update(final OnCompleteListener<TodoData> onCompleteListener, final TodoData todoData) {
        HashMap<String, Object> editData = new HashMap<>();
        editData.put("eventHistory", todoData.getEventHistory());
        editData.put("todoState", todoData.getTodoState());
        editData.put("todoTitle", todoData.getTodoTitle());
        editData.put("todoDesc", todoData.getTodoDesc());
        editData.put("teamName", todoData.getTeamName());
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
                            todoLocalStore.update(null, todoData);
                            TodoCacheStore.getInstance().update(null, todoData);
                        }
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, todoData);
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
    public void remove(final OnCompleteListener<TodoData> onCompleteListener, final TodoData todoData) {
        String todoKey = todoData.getTodoKey();
        getFirestore().runTransaction(new Transaction.Function<TodoData>() {
            @Nullable
            @Override
            public TodoData apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                return null;
            }
        });

        getFirestore().collectionGroup("Todo")
                .whereEqualTo("todoKey", todoKey)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    }
                });
        getFirestore().collection("Team")
                .document(todoData.getTeamKey())
                .collection("Todo")
                .document(todoData.getTodoKey())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        todoLocalStore.remove(null, todoData);
                        TodoCacheStore.getInstance().remove(null, todoData);
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, todoData);
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

    private void loadTeamTodoDataList(String teamKey, final OnCompleteListener<List<TodoData>> onCompleteListener) {
        getFirestore().collection("Team")
                .document(teamKey)
                .collection("Todo")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            onCompleteListener.onComplete(true, Collections.emptyList());
                            return;
                        }
                        List<TodoData> teamTodoDataList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                            TodoData todoData = documentSnapshot.toObject(TodoData.class);
                            teamTodoDataList.add(todoData);
                            todoLocalStore.update(null, todoData);
                            TodoCacheStore.getInstance().update(null, todoData);
                        }
                        Collections.sort(teamTodoDataList);

                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, teamTodoDataList);
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

    private void loadMyTodoDataList(String userKey, final OnCompleteListener<List<TodoData>> onCompleteListener) {
        getFirestore().collectionGroup("Todo")
                .whereEqualTo("userKey", userKey)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            onCompleteListener.onComplete(true, Collections.emptyList());
                            return;
                        }
                        List<TodoData> myTodoDataList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                            TodoData todoData = documentSnapshot.toObject(TodoData.class);
                            myTodoDataList.add(todoData);
                        }
                        Collections.sort(myTodoDataList);
                        todoLocalStore.addAll(myTodoDataList);
                        TodoCacheStore.getInstance().addAll(myTodoDataList);
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, myTodoDataList);
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
}