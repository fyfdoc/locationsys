package com.dtm.locationsys.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import com.dtm.locationsys.cust.ChangeColorIconWithText;
import com.dtm.locationsys.fragment.ConfigFragment;
import com.dtm.locationsys.fragment.HomeFragment;
import com.dtm.locationsys.fragment.MainPageFragment;
import com.dtm.locationsys.R;
import com.dtm.locationsys.fragment.StationFragment;
import com.dtm.locationsys.fragment.TeamMainFragment;
import com.dtm.locationsys.fragment.UserFragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private Integer[] mTitles = new Integer[]
            { R.string.index_menu0, R.string.index_menu1, R.string.index_menu2, R.string.index_menu3, R.string.index_menu4};
    private FragmentPagerAdapter mAdapter;

    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setOverflowButtonAlways();
//        getActionBar().setDisplayShowHomeEnabled(false);

        initView();
        initDatas();
        mViewPager.setAdapter(mAdapter);
        initEvent();

    }

    /**
     * 初始化所有事件
     */
    private void initEvent()
    {

        mViewPager.setOnPageChangeListener(this);

    }

    /**
     * 初始化ViewPager的数据
     */
    private void initDatas()
    {
        for (int item : mTitles)
        {
            // 菜单索引
            String menuIndex = getResources().getText(item).toString();

            if (getResources().getText(R.string.index_menu0).toString().equals(menuIndex)) {
                // 主界面菜单
//                MainPageFragment mainPageFragment = new MainPageFragment();
                HomeFragment homeFragment = new HomeFragment();
                mTabs.add(homeFragment);
            } else if(getResources().getText(R.string.index_menu1).toString().equals(menuIndex)) {
                // 基站菜单
                StationFragment stationFragment = new StationFragment();
                mTabs.add(stationFragment);
            } else if (getResources().getText(R.string.index_menu2).toString().equals(menuIndex)){
                // 用户菜单
                UserFragment userFragment = new UserFragment();
                mTabs.add(userFragment);
            } else if (getResources().getText(R.string.index_menu3).toString().equals(menuIndex)){
                // 协同菜单
                TeamMainFragment teamMainFragment = new TeamMainFragment();
                mTabs.add(teamMainFragment);
            } else if (getResources().getText(R.string.index_menu4).toString().equals(menuIndex)){
                // 设置菜单
                ConfigFragment configFragment = new ConfigFragment();
                mTabs.add(configFragment);
            }
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {

            @Override
            public int getCount()
            {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position)
            {
                return mTabs.get(position);
            }
        };
    }

    /**
     * 初始化主菜单View
     */
    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        // 主页面
        ChangeColorIconWithText mainPage = findViewById(R.id.id_menu_main_page);
        mTabIndicators.add(mainPage);
        // 基站
        ChangeColorIconWithText station = findViewById(R.id.id_menu_station);
        mTabIndicators.add(station);
        // 用户
        ChangeColorIconWithText user = findViewById(R.id.id_menu_user);
        mTabIndicators.add(user);
        // 协同
        ChangeColorIconWithText team = findViewById(R.id.id_menu_team);
        mTabIndicators.add(team);
        // 设置
        ChangeColorIconWithText config = findViewById(R.id.id_menu_config);
        mTabIndicators.add(config);

        mainPage.setOnClickListener(this);
        station.setOnClickListener(this);
        user.setOnClickListener(this);
        team.setOnClickListener(this);
        config.setOnClickListener(this);

        mainPage.setIconAlpha(1.0f);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 底部菜单点击事件
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        clickTab(v);
    }

    /**
     * 点击Tab按钮
     *
     * @param v
     */
    private void clickTab(View v)
    {

        resetOtherTabs();

        switch (v.getId())
        {
            case R.id.id_menu_main_page:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_menu_station:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_menu_user:
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_menu_team:
                mTabIndicators.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
            case R.id.id_menu_config:
                mTabIndicators.get(4).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(4, false);
                break;
        }
    }


    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs()
    {
        for (int i = 0; i < mTabIndicators.size(); i++)
        {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels)
    {
        // Log.e("TAG", "position = " + position + " ,positionOffset =  "
        // + positionOffset);
        if (positionOffset > 0)
        {
            ChangeColorIconWithText left = mTabIndicators.get(position);
            ChangeColorIconWithText right = mTabIndicators.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }

    }

    @Override
    public void onPageSelected(int position)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
        // TODO Auto-generated method stub

    }

    private void setOverflowButtonAlways()
    {
        try
        {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKey = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(config, false);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * 设置menu显示icon
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {

        if (featureId == Window.FEATURE_ACTION_BAR && menu != null)
        {
            if (menu.getClass().getSimpleName().equals("MenuBuilder"))
            {
                try
                {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }




}

