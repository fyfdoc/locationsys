package com.dtm.locationsys.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.dtm.locationsys.R;
import com.dtm.locationsys.fragment.LoginFragment;
import com.dtm.locationsys.fragment.NetworkConfigFragment;

public class IndexActivity extends AppCompatActivity implements ActionBar.TabListener {
    private static final String SELECTED_ITEM = "selected_item";
    Fragment longinFragment;
    Fragment networkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        final ActionBar actionBar = getSupportActionBar();

        // 设置ActionBar的导航方式:Tab导航
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // 依次添加Tab页，并为Tab标签添加事件监听
        actionBar.addTab(actionBar.newTab().setText("登录").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("配置").setTabListener(this));

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState.containsKey(SELECTED_ITEM)){
            // 选中前面保存的索引对应的Fragment
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(SELECTED_ITEM));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 将当前选中的Fragment页的索引保存到Bundle中
        outState.putInt(SELECTED_ITEM, getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        // 创建一个Bundle对象，用于向Fragment传递参数
        Bundle args =new Bundle();

        // 登录
        if(tab.getPosition() == 0){
            if(longinFragment == null){
                // 创建一个新的Fragment对象
                longinFragment = new LoginFragment();
            }
            args.putInt(LoginFragment.ARG_SECTION_NUMBER,tab.getPosition() + 1);
            // 向Fragment传入参数
            longinFragment.setArguments(args);
            // 获取FragmentTransacton对象
            FragmentTransaction ft2 = getFragmentManager().beginTransaction();
            // 使用fragment替代该Activity中的container
            ft2.replace(R.id.indexContainer, longinFragment);
            // 提交事务
            ft2.commit();
        }else{//网络配置
            if(networkFragment == null){
                // 创建一个新的Fragment对象
                networkFragment = new NetworkConfigFragment();
            }
            args.putInt(NetworkConfigFragment.ARG_SECTION_NUMBER,tab.getPosition() + 1);
            // 向Fragment传入参数
            networkFragment.setArguments(args);
            // 获取FragmentTransacton对象
            FragmentTransaction ft2 = getFragmentManager().beginTransaction();
            // 使用fragment替代该Activity中的container
            ft2.replace(R.id.indexContainer, networkFragment);
            // 提交事务
            ft2.commit();

        }

    }


    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}
