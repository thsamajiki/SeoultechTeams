package com.hero.seoultechteams.database;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class CloudStore<T> implements DataStore<T> {
    private Context context;
    private FirebaseFirestore firestore;

    public CloudStore(Context context) {
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getFirestore() {
        return firestore;
    }

    public Context getContext() {
        return context;
    }
}
