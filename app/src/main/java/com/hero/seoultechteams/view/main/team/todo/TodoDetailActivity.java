package com.hero.seoultechteams.view.main.team.todo;

import androidx.appcompat.widget.PopupMenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.todo.TodoRepository;
import com.hero.seoultechteams.database.todo.entity.TodoData;

import static com.hero.seoultechteams.view.main.team.todo.TeamTodoListActivity.EXTRA_TODO_DATA;


public class TodoDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack, btnOptionMenu;
    private EditText editTodoTitle, editTodoDesc;
    public static final String EXTRA_UPDATE_TODO = "updateTodo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        initView();
        setOnClickListener();
        addTextWatcher();
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        btnOptionMenu = findViewById(R.id.iv_option_menu);
        editTodoTitle = findViewById(R.id.edit_team_name);
        editTodoDesc = findViewById(R.id.edit_todo_desc);

        showUpdatedTodoDetail();
    }

    private void setOnClickListener() {
        ivBack.setOnClickListener(this);
        btnOptionMenu.setOnClickListener(this);
    }

    private TodoData getTodoData() {
        return getIntent().getParcelableExtra(EXTRA_TODO_DATA);
    }

    private void showUpdatedTodoDetail() {
        TodoData todoData = getTodoData();
        String myUserKey = getCurrentUser().getUid();
        if (myUserKey.equals(todoData.getUserKey())) {
            toggleEditText(editTodoTitle, true);
            toggleEditText(editTodoDesc, true);
        } else {
            toggleEditText(editTodoTitle, false);
            toggleEditText(editTodoDesc, false);
        }

        String todoTitle = todoData.getTodoTitle();
        String todoDesc = todoData.getTodoDesc();
        editTodoTitle.setText(todoTitle);
        editTodoDesc.setText(todoDesc);
        if (TextUtils.isEmpty(todoDesc)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    editTodoDesc.requestFocus();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(editTodoDesc, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 100);
        } else {
            editTodoTitle.clearFocus();
            editTodoDesc.clearFocus();
        }
    }

    private void toggleEditText(EditText editText, boolean enabled) {
        editText.setFocusable(enabled);
        editText.setFocusableInTouchMode(enabled);
        editText.setClickable(enabled);
    }

    private void addTextWatcher() {
        editTodoTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivBack.setClickable(false);
                    Toast.makeText(TodoDetailActivity.this, "할 일 제목을 입력해야 합니다!", Toast.LENGTH_SHORT).show();
                } else {
                    ivBack.setClickable(true);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                updateTodoDetail();
                break;
//            case R.id.iv_option_menu:
//                showTodoDetailOptionMenu();
//                break;
        }
    }

    private void updateTodoDetail() {
        TodoRepository todoRepository = new TodoRepository(this);
        final String todoTitle = editTodoTitle.getText().toString();
        final String todoDesc = editTodoDesc.getText().toString();

        final TodoData todoData = getTodoData();
        todoData.setTodoTitle(todoTitle);
        todoData.setTodoDesc(todoDesc);

        todoRepository.updateTodo(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData data) {
                if (isSuccess) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_UPDATE_TODO, todoData);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(TodoDetailActivity.this, "데이터가 수정되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, todoData);
    }

//    private void showTodoDetailOptionMenu(){
//        PopupMenu popupMenu = new PopupMenu(this, btnOptionMenu);
//        popupMenu.getMenuInflater().inflate(R.menu.menu_todo_detail_option, popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menu_delete_todo_at_detail:
//                        openDeleteTodoDialog();
//                        break;
//                }
//                return true;
//            }
//        });
//        popupMenu.show();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_todo_detail_option, menu);
//        return true;
//    }
//
//    private void openDeleteTodoDialog() {
//        String delete_todo_message = "할 일을 삭제하시겠습니까?";
//        String positiveText = "예";
//        String negativeText = "아니오";
//        new MaterialAlertDialogBuilder(this).setMessage(delete_todo_message)
//                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .create()
//                .show();
//    }
}