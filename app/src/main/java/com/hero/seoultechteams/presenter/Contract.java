package com.hero.seoultechteams.presenter;

import androidx.annotation.NonNull;

import com.hero.seoultechteams.BasePresenter;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.view.BaseView;

import java.util.ArrayList;

public interface Contract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTeamData(ArrayList<TeamData> teamData);

        void showAddTask();

        void showTaskDetailsUi(String taskId);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showCompletedTasksCleared();

        void showLoadingTasksError();

        void showNoTasks();

        void showActiveFilterLabel();

        void showCompletedFilterLabel();

        void showAllFilterLabel();

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showSuccessfullySavedMessage();

        boolean isActive();

        void showFilteringPopUpMenu();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadTasks(boolean forceUpdate);

        void addNewTask();

//        void openTaskDetails(@NonNull Task requestedTask);
//
//        void completeTask(@NonNull Task completedTask);
//
//        void activateTask(@NonNull Task activeTask);

        void clearCompletedTasks();

        void setFiltering(TasksFilterType requestType);

        TasksFilterType getFiltering();
    }
}
