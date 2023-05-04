package com.hero.seoultechteams;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.hero.seoultechteams.data.member.local.MemberLocalDataSourceImpl;
import com.hero.seoultechteams.data.member.remote.MemberRemoteDataSourceImpl;
import com.hero.seoultechteams.data.notice.local.NoticeLocalDataSourceImpl;
import com.hero.seoultechteams.data.notice.remote.NoticeRemoteDataSourceImpl;
import com.hero.seoultechteams.data.team.local.TeamLocalDataSourceImpl;
import com.hero.seoultechteams.data.team.remote.TeamRemoteDataSourceImpl;
import com.hero.seoultechteams.data.todo.local.TodoLocalDataSourceImpl;
import com.hero.seoultechteams.data.todo.remote.TodoRemoteDataSourceImpl;
import com.hero.seoultechteams.data.user.local.UserLocalDataSourceImpl;
import com.hero.seoultechteams.data.user.remote.UserRemoteDataSourceImpl;
import com.hero.seoultechteams.database.member.MemberRepositoryImpl;
import com.hero.seoultechteams.database.member.database.AppMemberDatabase;
import com.hero.seoultechteams.database.member.datastore.MemberCacheStore;
import com.hero.seoultechteams.database.member.datastore.MemberCloudStore;
import com.hero.seoultechteams.database.member.datastore.MemberLocalStore;
import com.hero.seoultechteams.database.notice.NoticeRepositoryImpl;
import com.hero.seoultechteams.database.notice.database.AppNoticeDatabase;
import com.hero.seoultechteams.database.notice.datastore.NoticeCloudStore;
import com.hero.seoultechteams.database.notice.datastore.NoticeLocalStore;
import com.hero.seoultechteams.database.team.TeamRepositoryImpl;
import com.hero.seoultechteams.database.team.database.AppTeamDatabase;
import com.hero.seoultechteams.database.team.datastore.TeamCacheStore;
import com.hero.seoultechteams.database.team.datastore.TeamCloudStore;
import com.hero.seoultechteams.database.team.datastore.TeamLocalStore;
import com.hero.seoultechteams.database.todo.TodoRepositoryImpl;
import com.hero.seoultechteams.database.todo.database.AppTodoDatabase;
import com.hero.seoultechteams.database.todo.datastore.TodoCacheStore;
import com.hero.seoultechteams.database.todo.datastore.TodoCloudStore;
import com.hero.seoultechteams.database.todo.datastore.TodoLocalStore;
import com.hero.seoultechteams.database.user.UserRepositoryImpl;
import com.hero.seoultechteams.database.user.database.AppUserDatabase;
import com.hero.seoultechteams.database.user.datastore.UserCloudStore;
import com.hero.seoultechteams.database.user.datastore.UserLocalStore;
import com.hero.seoultechteams.domain.member.repository.MemberRepository;
import com.hero.seoultechteams.domain.member.usecase.GetMemberListUseCase;
import com.hero.seoultechteams.domain.member.usecase.GetUserListByEmailUseCase;
import com.hero.seoultechteams.domain.member.usecase.GetUserListByNameUseCase;
import com.hero.seoultechteams.domain.member.usecase.InviteNewMemberListUseCase;
import com.hero.seoultechteams.domain.notice.repository.NoticeRepository;
import com.hero.seoultechteams.domain.notice.usecase.GetNoticeListUseCase;
import com.hero.seoultechteams.domain.team.repository.TeamRepository;
import com.hero.seoultechteams.domain.team.usecase.AddTeamUseCase;
import com.hero.seoultechteams.domain.team.usecase.GetMemberParticipationUseCase;
import com.hero.seoultechteams.domain.team.usecase.GetTeamListUseCase;
import com.hero.seoultechteams.domain.team.usecase.GetTeamUseCase;
import com.hero.seoultechteams.domain.team.usecase.UpdateTeamDetailUseCase;
import com.hero.seoultechteams.domain.todo.repository.TodoRepository;
import com.hero.seoultechteams.domain.todo.usecase.AddTodoUseCase;
import com.hero.seoultechteams.domain.todo.usecase.GetMyTodoListUseCase;
import com.hero.seoultechteams.domain.todo.usecase.GetTeamTodoListUseCase;
import com.hero.seoultechteams.domain.todo.usecase.GetTodoUseCase;
import com.hero.seoultechteams.domain.todo.usecase.SetRefreshUseCase;
import com.hero.seoultechteams.domain.todo.usecase.UpdateTodoDetailUseCase;
import com.hero.seoultechteams.domain.todo.usecase.UpdateTodoStateUseCase;
import com.hero.seoultechteams.domain.user.repository.UserRepository;
import com.hero.seoultechteams.domain.user.usecase.GetAccountProfileUseCase;
import com.hero.seoultechteams.domain.user.usecase.GetUserUseCase;
import com.hero.seoultechteams.domain.user.usecase.RemoveUserUseCase;
import com.hero.seoultechteams.domain.user.usecase.SignUpUseCase;
import com.hero.seoultechteams.domain.user.usecase.UpdateUserUseCase;
import com.hero.seoultechteams.view.main.SignOutUseCase;

public class Injector {

    public static final Injector instance = new Injector();

    private Injector() {
    }

    public static Injector getInstance() {
        return instance;
    }

    @NonNull
    private TeamLocalStore provideTeamLocalStore() {
        return TeamLocalStore.getInstance(SeoultechTeamsApp.getInstance(), provideAppTeamDatabase().getTeamDao());
    }

    private AppTeamDatabase provideAppTeamDatabase() {
        return AppTeamDatabase.getInstance(SeoultechTeamsApp.getInstance());
    }

    @NonNull
    public GetTeamListUseCase provideGetTeamListUseCase() {
        return new GetTeamListUseCase(new TeamRepositoryImpl(
                new TeamRemoteDataSourceImpl(
                        new TeamCloudStore(SeoultechTeamsApp.getInstance(), provideTeamLocalStore(), TeamCacheStore.getInstance(), provideTodoCloudStore())
                ),
                new TeamLocalDataSourceImpl(
                        provideTeamLocalStore(),
                        TeamCacheStore.getInstance()
                )
        ));
    }

    @NonNull
    public GetTeamUseCase provideGetTeamUseCase() {
        return new GetTeamUseCase(new TeamRepositoryImpl(
                new TeamRemoteDataSourceImpl(
                        new TeamCloudStore(SeoultechTeamsApp.getInstance(), provideTeamLocalStore(), TeamCacheStore.getInstance(), provideTodoCloudStore())
                ),
                new TeamLocalDataSourceImpl(
                        provideTeamLocalStore(),
                        TeamCacheStore.getInstance()
                )
        ));
    }

    @NonNull
    public AddTeamUseCase provideAddTeamUseCase() {
        return new AddTeamUseCase(new TeamRepositoryImpl(
                new TeamRemoteDataSourceImpl(
                        new TeamCloudStore(SeoultechTeamsApp.getInstance(), provideTeamLocalStore(), TeamCacheStore.getInstance(), provideTodoCloudStore())
                ),
                new TeamLocalDataSourceImpl(
                        provideTeamLocalStore(),
                        TeamCacheStore.getInstance()
                )
        ));
    }

    @NonNull
    public UpdateTeamDetailUseCase provideUpdateTeamDetailUseCase() {
        return new UpdateTeamDetailUseCase(new TeamRepositoryImpl(
                new TeamRemoteDataSourceImpl(
                        new TeamCloudStore(SeoultechTeamsApp.getInstance(), provideTeamLocalStore(), TeamCacheStore.getInstance(), provideTodoCloudStore())
                ),
                new TeamLocalDataSourceImpl(
                        provideTeamLocalStore(),
                        TeamCacheStore.getInstance()
                )
        ));
    }

    @NonNull
    private TeamRepository getTeamRepository() {
        return new TeamRepositoryImpl(
                new TeamRemoteDataSourceImpl(
                        new TeamCloudStore(SeoultechTeamsApp.getInstance(), provideTeamLocalStore(), TeamCacheStore.getInstance(), provideTodoCloudStore())
                ),
                new TeamLocalDataSourceImpl(
                        provideTeamLocalStore(),
                        TeamCacheStore.getInstance()
                )
        );
    }

    @NonNull
    private TodoLocalStore provideTodoLocalStore() {
        return TodoLocalStore.getInstance(SeoultechTeamsApp.getInstance(), provideAppTodoDatabase().getTodoDao());
    }

    private AppTodoDatabase provideAppTodoDatabase() {
        return AppTodoDatabase.getInstance(SeoultechTeamsApp.getInstance());
    }

    @NonNull
    public GetTodoUseCase provideGetTodoUseCase() {
        return new GetTodoUseCase(new TodoRepositoryImpl(
                new TodoRemoteDataSourceImpl(
                        provideTodoCloudStore()
                ),
                new TodoLocalDataSourceImpl(
                        provideTodoLocalStore(),
                        TodoCacheStore.getInstance()
                )
        ));
    }

    @NonNull
    public GetTeamTodoListUseCase provideGetTeamTodoListUseCase() {
        return new GetTeamTodoListUseCase(new TodoRepositoryImpl(
                new TodoRemoteDataSourceImpl(
                        provideTodoCloudStore()
                ),
                new TodoLocalDataSourceImpl(
                        provideTodoLocalStore(),
                        TodoCacheStore.getInstance()
                )
        ));
    }


    @NonNull
    public GetMyTodoListUseCase provideGetMyTodoListUseCase() {
        return new GetMyTodoListUseCase(new TodoRepositoryImpl(
                new TodoRemoteDataSourceImpl(
                        provideTodoCloudStore()
                ),
                new TodoLocalDataSourceImpl(
                        provideTodoLocalStore(),
                        TodoCacheStore.getInstance()
                )
        ));
    }

    public SetRefreshUseCase provideSetRefreshUseCase() {
        return new SetRefreshUseCase(getTodoRepository());
    }

    public AddTodoUseCase provideAddTodoUseCase() {
        return new AddTodoUseCase(getTodoRepository());
    }

    public UpdateTodoDetailUseCase provideUpdateTodoDetailUseCase() {
        return new UpdateTodoDetailUseCase(getTodoRepository());
    }

    public UpdateTodoStateUseCase provideUpdateTodoStateUseCase() {
        return new UpdateTodoStateUseCase(getTodoRepository());
    }

    @NonNull
    private TodoRepository getTodoRepository() {
        return new TodoRepositoryImpl(
                new TodoRemoteDataSourceImpl(
                        provideTodoCloudStore()
                ),
                new TodoLocalDataSourceImpl(
                        provideTodoLocalStore(),
                        TodoCacheStore.getInstance()
                )
        );
    }

    @NonNull
    public GetUserUseCase provideGetUserUseCase() {
        return new GetUserUseCase(new UserRepositoryImpl(
                new UserRemoteDataSourceImpl(
                        getUserCloudStore(),
                        FirebaseAuth.getInstance()
                ),
                new UserLocalDataSourceImpl(
                        provideUserLocalStore()
                )
        ));
    }

    @NonNull
    private UserCloudStore getUserCloudStore() {
        return new UserCloudStore(SeoultechTeamsApp.getInstance(),
                TeamCacheStore.getInstance(),
                provideTodoCloudStore()
        );
    }

    @NonNull
    private UserLocalStore getUserLocalStore() {
        return provideUserLocalStore();
    }

    private TodoCloudStore provideTodoCloudStore() {
        return TodoCloudStore.getInstance(SeoultechTeamsApp.getInstance(), provideTodoLocalStore(), TodoCacheStore.getInstance());
    }

    @NonNull
    public GetUserListByNameUseCase provideGetUserListByNameUseCase() {
        return new GetUserListByNameUseCase(new UserRepositoryImpl(
                new UserRemoteDataSourceImpl(
                        getUserCloudStore(),
                        FirebaseAuth.getInstance()
                ),
                new UserLocalDataSourceImpl(
                        provideUserLocalStore()
                )
        ));
    }

    @NonNull
    private UserLocalStore provideUserLocalStore() {
        return UserLocalStore.getInstance(SeoultechTeamsApp.getInstance(), provideAppUserDatabase().getUserDao());
    }

    private AppUserDatabase provideAppUserDatabase() {
        return AppUserDatabase.getInstance(SeoultechTeamsApp.getInstance());
    }

    @NonNull
    public GetUserListByEmailUseCase provideGetUserListByEmailUseCase() {
        return new GetUserListByEmailUseCase(new UserRepositoryImpl(
                new UserRemoteDataSourceImpl(
                        getUserCloudStore(),
                        FirebaseAuth.getInstance()
                ),
                new UserLocalDataSourceImpl(
                        provideUserLocalStore()
                )
        ));
    }

    @NonNull
    public GetAccountProfileUseCase provideGetAccountProfileUseCase() {
        return new GetAccountProfileUseCase(new UserRepositoryImpl(
                new UserRemoteDataSourceImpl(
                        getUserCloudStore(),
                        FirebaseAuth.getInstance()
                ),
                new UserLocalDataSourceImpl(
                        provideUserLocalStore()
                )
        ));
    }

    @NonNull
    public SignUpUseCase provideSignUpUserUseCase() {
        return new SignUpUseCase(new UserRepositoryImpl(
                new UserRemoteDataSourceImpl(
                        getUserCloudStore(),
                        FirebaseAuth.getInstance()
                ),
                new UserLocalDataSourceImpl(
                        provideUserLocalStore()
                )
        ));
    }

    @NonNull
    public UpdateUserUseCase provideUpdateUserUseCase() {
        return new UpdateUserUseCase(new UserRepositoryImpl(
                new UserRemoteDataSourceImpl(
                        getUserCloudStore(),
                        FirebaseAuth.getInstance()
                ),
                new UserLocalDataSourceImpl(
                        provideUserLocalStore()
                )
        ));
    }

    @NonNull
    public SignOutUseCase provideSignOutUseCase() {
        return new SignOutUseCase(new UserRepositoryImpl(
                new UserRemoteDataSourceImpl(
                        getUserCloudStore(),
                        FirebaseAuth.getInstance()
                ),
                new UserLocalDataSourceImpl(
                        provideUserLocalStore()
                )
        ));
    }

    @NonNull
    public RemoveUserUseCase provideRemoveUserUseCase() {
        return new RemoveUserUseCase(new UserRepositoryImpl(
                new UserRemoteDataSourceImpl(
                        getUserCloudStore(),
                        FirebaseAuth.getInstance()
                ),
                new UserLocalDataSourceImpl(
                        provideUserLocalStore()
                )
        ));
    }

    @NonNull
    private UserRepository getUserRepository() {
        return new UserRepositoryImpl(
                new UserRemoteDataSourceImpl(
                        getUserCloudStore(),
                        FirebaseAuth.getInstance()
                ),
                new UserLocalDataSourceImpl(
                        provideUserLocalStore()
                )
        );
    }

    private AppMemberDatabase provideMemberLocalStore() {
        return AppMemberDatabase.getInstance(SeoultechTeamsApp.getInstance());
    }

    public GetMemberListUseCase provideGetMemberListUseCase() {
        return new GetMemberListUseCase(new MemberRepositoryImpl(
                new MemberRemoteDataSourceImpl(
                        new MemberCloudStore(SeoultechTeamsApp.getInstance())
                ),
                new MemberLocalDataSourceImpl(
                        new MemberLocalStore(SeoultechTeamsApp.getInstance(), provideMemberLocalStore().getMemberDao()),
                        MemberCacheStore.getInstance()
                )
        ));
    }

    public InviteNewMemberListUseCase provideAddNewMemberListUseCase() {
        return new InviteNewMemberListUseCase(new UserRepositoryImpl(
                new UserRemoteDataSourceImpl(
                        getUserCloudStore(),
                        FirebaseAuth.getInstance()
                ),
                new UserLocalDataSourceImpl(
                        provideUserLocalStore()
                )
        ));
    }

    @NonNull
    public GetMemberParticipationUseCase provideGetMemberParticipationUseCase() {
        return new GetMemberParticipationUseCase(
                provideGetTeamTodoListUseCase(),
                provideGetMemberListUseCase()
        );
    }

    @NonNull
    private MemberRepository getMemberRepository() {
        return new MemberRepositoryImpl(
                new MemberRemoteDataSourceImpl(
                        new MemberCloudStore(SeoultechTeamsApp.getInstance())
                ),
                new MemberLocalDataSourceImpl(
                        new MemberLocalStore(SeoultechTeamsApp.getInstance(), provideMemberLocalStore().getMemberDao()),
                        MemberCacheStore.getInstance()
                )
        );
    }

    private AppNoticeDatabase provideNoticeLocalStore() {
        return AppNoticeDatabase.getInstance(SeoultechTeamsApp.getInstance());
    }

    public GetNoticeListUseCase provideGetNoticeListUseCase() {
        return new GetNoticeListUseCase(new NoticeRepositoryImpl(
                new NoticeRemoteDataSourceImpl(
                        new NoticeCloudStore(SeoultechTeamsApp.getInstance())
                ),
                new NoticeLocalDataSourceImpl(
                        new NoticeLocalStore(SeoultechTeamsApp.getInstance(), provideNoticeLocalStore().getNoticeDao())
                )
        ));
    }

    @NonNull
    private NoticeRepository getNoticeRepository() {
        return new NoticeRepositoryImpl(
                new NoticeRemoteDataSourceImpl(
                        new NoticeCloudStore(SeoultechTeamsApp.getInstance())
                ),
                new NoticeLocalDataSourceImpl(
                        new NoticeLocalStore(SeoultechTeamsApp.getInstance(), provideNoticeLocalStore().getNoticeDao())
                )
        );
    }
}