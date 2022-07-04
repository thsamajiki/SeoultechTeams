package com.hero.seoultechteams.database.notice.datastore;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hero.seoultechteams.database.CloudStore;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.notice.entity.NoticeData;

import java.util.ArrayList;

public class NoticeCloudStore extends CloudStore<NoticeData> {

    public NoticeCloudStore(Context context) {
        super(context);
    }

    @Override
    public void getData(OnCompleteListener<NoticeData> onCompleteListener, Object... params) {

    }

    @Override
    public void getDataList(final OnCompleteListener<ArrayList<NoticeData>> onCompleteListener, Object... params) {
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

    @Override
    public void add(final OnCompleteListener<NoticeData> onCompleteListener, final NoticeData noticeData) {
        getFirestore().collection("Notice")
                .document()
                .set(noticeData)
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

    @Override
    public void update(OnCompleteListener<NoticeData> onCompleteListener, NoticeData data) {

    }

    @Override
    public void remove(OnCompleteListener<NoticeData> onCompleteListener, NoticeData data) {

    }
}