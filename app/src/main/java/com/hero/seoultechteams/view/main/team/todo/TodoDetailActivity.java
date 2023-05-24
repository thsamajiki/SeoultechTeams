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
import android.widget.Toast;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityTodoDetailBinding;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.view.main.team.todo.contract.TodoDetailContract;
import com.hero.seoultechteams.view.main.team.todo.presenter.TodoDetailPresenter;

public class TodoDetailActivity extends BaseActivity implements View.OnClickListener, TodoDetailContract.View {

    private ActivityTodoDetailBinding binding;
    public static final String EXTRA_UPDATE_TODO = "updateTodo";
    public static final String EXTRA_TODO_KEY = "todoKey";
    private final TodoDetailContract.Presenter presenter = new TodoDetailPresenter(this,
            Injector.getInstance().provideUpdateTodoDetailUseCase(),
            Injector.getInstance().provideGetTodoUseCase());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTodoDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initView();
        setOnClickListener();
        addTextWatcher();
    }

    private void initView() {
        String todoKey = getIntent().getStringExtra(EXTRA_TODO_KEY);

        presenter.requestTodoData(todoKey);
    }

    private void initializeTodoDetail(TodoEntity todoEntity) {
        String myUserKey = getCurrentUser().getUid();
        if (myUserKey.equals(todoEntity.getUserKey())) {
            toggleEditText(binding.editTodoTitle, true);
            toggleEditText(binding.editTodoDesc, true);
        } else {
            toggleEditText(binding.editTodoTitle, false);
            toggleEditText(binding.editTodoDesc, false);
        }

        String todoTitle = todoEntity.getTodoTitle();
        String todoDesc = todoEntity.getTodoDesc();
        binding.editTodoTitle.setText(todoTitle);
        binding.editTodoDesc.setText(todoDesc);
        if (TextUtils.isEmpty(todoDesc)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.editTodoDesc.requestFocus();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(binding.editTodoDesc, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 100);
        } else {
            binding.editTodoTitle.clearFocus();
            binding.editTodoDesc.clearFocus();
        }
    }

    private void setOnClickListener() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTodoDetail();
            }
        });

        binding.ivOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        binding.editTodoTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.ivBack.setClickable(false);
                    Toast.makeText(TodoDetailActivity.this, "할 일 제목을 입력해야 합니다!", Toast.LENGTH_SHORT).show();
                } else {
                    binding.ivBack.setClickable(true);
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
        final String todoTitle = binding.editTodoTitle.getText().toString();
        final String todoDesc = binding.editTodoDesc.getText().toString();

        presenter.updateTodoDetail(todoTitle, todoDesc);
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

    @Override
    public void onLoadTodo(TodoEntity data) {
        initializeTodoDetail(data);
    }

    @Override
    public void failedLoadTodo() {
        Toast.makeText(this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
    }
}