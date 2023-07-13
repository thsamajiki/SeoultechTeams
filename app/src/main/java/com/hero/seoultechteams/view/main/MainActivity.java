package com.hero.seoultechteams.view.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.FragmentAdapter;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityMainBinding;
import com.hero.seoultechteams.view.login.LoginActivity;
import com.hero.seoultechteams.view.main.account.AboutUsDialog;
import com.hero.seoultechteams.view.main.account.AccountFragment;
import com.hero.seoultechteams.view.main.account.setting.SettingActivity;
import com.hero.seoultechteams.view.main.mytodo.MyTodoListFragment;
import com.hero.seoultechteams.view.main.team.TeamListFragment;


public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener, MainContract.View {

    private final MainContract.Presenter presenter = new MainPresenter(this,
            Injector.getInstance().provideSignOutUseCase());

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFragmentAdapter();
        setBottomNavClickListener();
        setOnClickListener();
        binding.mainViewPager.setCurrentItem(1, false);
    }


    private void setFragmentAdapter() {
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        fragmentAdapter.addFragment(new TeamListFragment());
        fragmentAdapter.addFragment(new MyTodoListFragment());
        fragmentAdapter.addFragment(new AccountFragment());
        binding.mainViewPager.setAdapter(fragmentAdapter);

        final String[] titleArr = getResources().getStringArray(R.array.title_array);
        binding.mainViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.mainBottomNav.getMenu().getItem(position).setChecked(true);
                binding.tvMainTitle.setText(titleArr[position]);

                if (position == 2) {
                    binding.ivAccountOptionMenu.setVisibility(View.VISIBLE);
                    binding.ivAccountOptionMenu.setClickable(true);
                } else {
                    binding.ivAccountOptionMenu.setVisibility(View.INVISIBLE);
                    binding.ivAccountOptionMenu.setClickable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void setBottomNavClickListener() {
        binding.mainBottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_team:
                        binding.mainViewPager.setCurrentItem(0, true);
                        binding.ivAccountOptionMenu.setVisibility(View.INVISIBLE);
                        binding.ivAccountOptionMenu.setClickable(false);
                        break;
                    case R.id.menu_mytodo:
                        binding.mainViewPager.setCurrentItem(1, true);
                        binding.ivAccountOptionMenu.setVisibility(View.INVISIBLE);
                        binding.ivAccountOptionMenu.setClickable(false);
                        break;
                    case R.id.menu_account:
                        binding.mainViewPager.setCurrentItem(2, true);
                        binding.ivAccountOptionMenu.setVisibility(View.VISIBLE);
                        binding.ivAccountOptionMenu.setClickable(true);
                        break;
                }
                return false;
            }
        });
    }

    private void setOnClickListener() {
        binding.ivAccountOptionMenu.setOnClickListener(this);
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
        PopupMenu popupMenu = new PopupMenu(this, binding.ivAccountOptionMenu);
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
        presenter.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finishAffinity();
    }
}