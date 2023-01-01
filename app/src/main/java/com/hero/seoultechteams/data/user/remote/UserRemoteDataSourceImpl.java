package com.hero.seoultechteams.data.user.remote;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.user.datastore.UserCloudStore;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.common.OnFailedListener;
import com.hero.seoultechteams.domain.exception.FailedCreateUserException;
import com.hero.seoultechteams.domain.exception.FailedUpdateUserException;
import com.hero.seoultechteams.domain.exception.UserEmptyException;

import java.util.List;

public class UserRemoteDataSourceImpl implements UserRemoteDataSource {
    private final UserCloudStore userCloudStore;
    private final FirebaseAuth firebaseAuth;

    public UserRemoteDataSourceImpl(UserCloudStore userCloudStore, FirebaseAuth firebaseAuth) {
        this.userCloudStore = userCloudStore;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void addUser(OnCompleteListener<UserData> onCompleteListener, OnFailedListener onFailedListener, String userName, String email, String pwd) {
        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();
                        if (user != null) {
                            updateProfile(onCompleteListener, onFailedListener, authResult.getUser(), userName, email);
                        } else {
                            onFailedListener.onFailed(new UserEmptyException("사용자를 불러올 수 없습니다.", new Throwable()));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        firebaseAuth.signInWithEmailAndPassword(email, pwd);

                        onFailedListener.onFailed(new FailedCreateUserException("failed addUser", e));
                    }
                });
    }

    private void updateProfile(OnCompleteListener<UserData> onCompleteListener, OnFailedListener onFailedListener, final FirebaseUser firebaseUser, String userName, String email) {
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();
        firebaseUser.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        UserData userData = new UserData(
                                userName,
                                email,
                                null,
                                firebaseUser.getUid()
                        );
                        addUserToCloudStore(onCompleteListener, userData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onFailedListener.onFailed(new FailedUpdateUserException("failed updateProfile", e));

                        UserData userData = new UserData(
                                userName,
                                email,
                                null,
                                firebaseUser.getUid()
                        );

                        addUserToCloudStore(onCompleteListener, userData);
                    }
                });
    }

    private void addUserToCloudStore(final OnCompleteListener<UserData> onCompleteListener, final UserData userData) {
        userCloudStore.add(onCompleteListener, userData);
    }

    @Override
    public void updateUser(OnCompleteListener<UserData> onCompleteListener, UserData userData) {
        userCloudStore.update(onCompleteListener, userData);
    }

    @Override
    public void addUserList(OnCompleteListener<List<UserData>> onCompleteListener, TeamData teamData, List<UserData> userDataList, List<MemberData> memberDataList) {
        userCloudStore.addUserListToTeam(onCompleteListener, teamData, userDataList, memberDataList);
    }

    @Override
    public void getData(OnCompleteListener<UserData> onCompleteListener, String userKey) {
        userCloudStore.getData(onCompleteListener, userKey);
    }

    @Override
    public void getDataByUserName(OnCompleteListener<List<UserData>> onCompleteListener, String userName) {
        userCloudStore.getDataByUserName(onCompleteListener, userName);
    }

    @Override
    public void getDataByUserEmail(OnCompleteListener<List<UserData>> onCompleteListener, String userEmail) {
        userCloudStore.getDataByUserEmail(onCompleteListener, userEmail);
    }

    @Override
    public UserData getFirebaseAuthProfile() {
        FirebaseUser firebaseUser = getCurrentUser();

        String profileImageUrl;
        if (firebaseUser.getPhotoUrl() != null) {
            profileImageUrl = firebaseUser.getPhotoUrl().toString();
        } else {
            profileImageUrl = null;
        }

        UserData userData = new UserData(
                firebaseUser.getDisplayName(),
                firebaseUser.getEmail(),
                profileImageUrl,
                firebaseUser.getUid()
        );
        userData.setKey(firebaseUser.getUid());
        userData.setName(firebaseUser.getDisplayName());
        userData.setEmail(firebaseUser.getEmail());
        if (firebaseUser.getPhotoUrl() != null) {
            userData.setProfileImageUrl(firebaseUser.getPhotoUrl().toString());
        } else {
            userData.setProfileImageUrl(null);
        }

        return userData;
    }

    @Override
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
