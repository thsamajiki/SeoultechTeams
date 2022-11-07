package com.hero.seoultechteams.database.notice.datastore;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hero.seoultechteams.database.CloudStore;
import com.hero.seoultechteams.database.notice.entity.NoticeData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class NoticeCloudStore extends CloudStore<NoticeData> {

    public NoticeCloudStore(Context context) {
        super(context);
    }

    @Override
    public void getData(OnCompleteListener<NoticeData> onCompleteListener, Object... params) {
        String noticeKey = params[0].toString();
        getFirestore().collection("Notice")
                .document(noticeKey)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot == null) {
                            onCompleteListener.onComplete(true, null);
                        } else {
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
    public void getDataList(final OnCompleteListener<List<NoticeData>> onCompleteListener, Object... params) {
        getFirestore().collection("Notice")
                .orderBy("postDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            return;
                        }
                        ArrayList<NoticeData> noticeDataList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                            NoticeData noticeData = documentSnapshot.toObject(NoticeData.class);
                            noticeDataList.add(noticeData);
                        }

                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, noticeDataList);
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
    public void add(final OnCompleteListener<NoticeData> onCompleteListener, final NoticeData noticeData) {
        getFirestore().collection("Notice")
                .document()
                .set(noticeData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, noticeData);
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
    public void update(OnCompleteListener<NoticeData> onCompleteListener, NoticeData data) {

    }

    @Override
    public void remove(OnCompleteListener<NoticeData> onCompleteListener, NoticeData data) {

    }
}