package com.hero.seoultechteams.view.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.hero.seoultechteams.FragmentAdapter;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.utils.CacheManager;
import com.hero.seoultechteams.view.login.LoginActivity;
import com.hero.seoultechteams.view.main.account.AboutUsDialog;
import com.hero.seoultechteams.view.main.account.AccountFragment;
import com.hero.seoultechteams.view.main.account.setting.SettingActivity;
import com.hero.seoultechteams.view.main.mytodo.MyTodoListFragment;
import com.hero.seoultechteams.view.main.team.TeamListFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager2 mainViewPager;
    private BottomNavigationView mainBottomNav;
    private TextView tvMainTitle;
    private ImageView ivAccountOptionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setFragmentAdapter();
        setBottomNavClickListener();
        setOnClickListener();
        mainViewPager.setCurrentItem(1, false);
    }

    private void initView() {
        mainViewPager = findViewById(R.id.main_view_pager);
        mainBottomNav = findViewById(R.id.main_bottom_nav);
        tvMainTitle = findViewById(R.id.tv_main_title);
        ivAccountOptionMenu = findViewById(R.id.iv_account_option_menu);
    }

    private void setFragmentAdapter() {
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        fragmentAdapter.addFragment(new TeamListFragment());
        fragmentAdapter.addFragment(new MyTodoListFragment());
        fragmentAdapter.addFragment(new AccountFragment());
        mainViewPager.setAdapter(fragmentAdapter);

        final String[] titleArr = getResources().getStringArray(R.array.title_array);
        mainViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mainBottomNav.getMenu().getItem(position).setChecked(true);
                tvMainTitle.setText(titleArr[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void setBottomNavClickListener() {
        mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_team:
                        mainViewPager.setCurrentItem(0, true);
                        ivAccountOptionMenu.setVisibility(View.GONE);
                        ivAccountOptionMenu.setClickable(false);
                        break;
                    case R.id.menu_mytodo:
                        mainViewPager.setCurrentItem(1, true);
                        ivAccountOptionMenu.setVisibility(View.GONE);
                        break;
                    case R.id.menu_account:
                        mainViewPager.setCurrentItem(2, true);
                        ivAccountOptionMenu.setVisibility(View.VISIBLE);
                        ivAccountOptionMenu.setClickable(true);
                        break;
                }
                return false;
            }
        });
    }

    private void setOnClickListener() {
        ivAccountOptionMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_account_option_menu:
                showAccountOptionMenu();
                break;
        }
    }

    private void showAccountOptionMenu() {
        PopupMenu popupMenu = new PopupMenu(this, ivAccountOptionMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_account_actionbar_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_setting:
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_about_us:
                        showAboutUsDialog();
                        break;
                    case R.id.menu_logout:
                        showLogoutDialog();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void showLogoutDialog() {
        String logout_message = "로그아웃하시겠습니까?";
        new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("로그아웃")
                .setMessage(logout_message)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                    }
                })
                .setNegativeButton("아니오", null)
                .create()
                .show();
    }

    private void showAboutUsDialog() {
        AboutUsDialog aboutUsDialog = new AboutUsDialog(MainActivity.this);
        aboutUsDialog.getAboutUsDialog();
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        CacheManager.getInstance().allClear();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finishAffinity();
    }
}