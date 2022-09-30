package com.hero.seoultechteams.view.main.team.todo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.view.main.team.todo.contract.TodoDetailContract;
import com.hero.seoultechteams.view.main.team.todo.presenter.TodoDetailPresenter;

import static com.hero.seoultechteams.view.main.team.todo.TeamTodoListActivity.EXTRA_TODO_DATA;

public class TodoDetailActivity extends BaseActivity implements View.OnClickListener, TodoDetailContract.View {
    private ImageView ivBack, ivOptionMenu;
    private EditText editTodoTitle, editTodoDesc;
    public static final String EXTRA_UPDATE_TODO = "updateTodo";
    private final TodoDetailContract.Presenter presenter = new TodoDetailPresenter(this,
            Injector.getInstance().provideUpdateTodoDetailUseCase());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        initView();
        setOnClickListener();
        addTextWatcher();
    }

    private TodoEntity initTodoEntity;

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        ivOptionMenu = findViewById(R.id.iv_option_menu);
        editTodoTitle = findViewById(R.id.edit_team_name);
        editTodoDesc = findViewById(R.id.edit_todo_desc);

        initTodoEntity = getIntent().getParcelableExtra(EXTRA_TODO_DATA);
        initializeTodoDetail(initTodoEntity);
    }

    private void initializeTodoDetail(TodoEntity todoEntity) {
        String myUserKey = getCurrentUser().getUid();
        if (myUserKey.equals(todoEntity.getUserKey())) {
            toggleEditText(editTodoTitle, true);
            toggleEditText(editTodoDesc, true);
        } else {
            toggleEditText(editTodoTitle, false);
            toggleEditText(editTodoDesc, false);
        }

        String todoTitle = todoEntity.getTodoTitle();
        String todoDesc = todoEntity.getTodoDesc();
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

    private void setOnClickListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTodoDetail();
            }
        });

        ivOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showTodoDetailOptionMenu();
//                break;
            }
        });
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
        final String todoTitle = editTodoTitle.getText().toString();
        final String todoDesc = editTodoDesc.getText().toString();

        presenter.updateTodoDetail(todoTitle, todoDesc, initTodoEntity);
    }

    @Override
    public void updatedTodoDetail(TodoEntity data) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_UPDATE_TODO, data);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void failedUpdateTodoDetail() {
        Toast.makeText(TodoDetailActivity.this, "데이터가 수정되지 않았습니다.", Toast.LENGTH_SHORT).show();
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