package com.chinatelecom.pimtest.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.WindowManager;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.adapter.AbFragmentPagerAdapter;
import com.chinatelecom.pimtest.fragement.CallLogListFragment;
import com.chinatelecom.pimtest.fragement.ContactListFragment;
import com.chinatelecom.pimtest.fragement.PersonalCenterFragment;
import com.chinatelecom.pimtest.fragement.SmsListFragment;
import com.chinatelecom.pimtest.view.PagerSlidingTabStrip;

import java.util.ArrayList;

public class HomeActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setWidth(getscreen()[0]);
        pager=(ViewPager)findViewById(R.id.pager);
        context = HomeActivity.this;

        initPagerData();

    }



    //初始化页面数据
    private void initPagerData(){
        ArrayList<Fragment> pages=new ArrayList<Fragment>();
        String[] titls={"拨号","联系人","短信","我的"};
        pages.add(new CallLogListFragment());
        pages.add(new ContactListFragment());
        pages.add(new SmsListFragment());
        pages.add(new PersonalCenterFragment());
        // pages.add(new SettingsFragment());
        //页面适配器
        AbFragmentPagerAdapter fp=new AbFragmentPagerAdapter(getSupportFragmentManager(), pages, titls);
        pager.setAdapter(fp);

        pager.setOffscreenPageLimit(4);
      //  pager.setCurrentItem(0);
        tabs.setOnPageChangeListener(this);
        tabs.setViewPager(pager);
        tabs.setBackgroundColor(Color.WHITE);
        //指示器颜色,即pager中下划线颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.colorPrimary));
        //指示器的高度
        tabs.setIndicatorHeight(12);
        //字体大小
        tabs.setTextSize(40);
        //设置底线的高度
        tabs.setUnderlineHeight(1);
        //设置底线的颜色
        tabs.setUnderlineColor(getResources().getColor(R.color.colorPrimary));
        //适配时指定的高度
        tabs.getLayoutParams().height=180;
        tabs.setDefault_select_page(0);
        pager.setCurrentItem(0);
        tabs.invalidate();
    }
    //获取屏幕宽高
    private int[] getscreen(){
        int[] w_h=new int[2];
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        w_h[0]=width;
        w_h[1]=height;
        return w_h;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



}
