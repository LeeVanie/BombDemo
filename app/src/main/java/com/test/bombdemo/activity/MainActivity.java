package com.test.bombdemo.activity;

import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.test.bombdemo.R;
import com.test.bombdemo.application.MyApplication;
import com.test.bombdemo.base.BaseActivity;
import com.test.bombdemo.bean.UserBean;
import com.test.bombdemo.fragment.CategoryFragment;
import com.test.bombdemo.fragment.GroupFragment;
import com.test.bombdemo.fragment.HomeFragment;
import com.test.bombdemo.fragment.LiveFragment;
import com.test.bombdemo.fragment.PersonFragment;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.update.BmobUpdateAgent;

public class MainActivity extends BaseActivity implements View.OnClickListener {
   
    public static final String TAG = "MainActivity";
    private FrameLayout mContainer;
    private RadioGroup mRgTabBar;
    private RadioButton mHome, mCategory, mGroup, mPerson;
    
    private HomeFragment mHomeFragment;
    private CategoryFragment mCategoryFragment;
    private LiveFragment mLiveFragment;
    private GroupFragment mGroupFragment;
    private PersonFragment mPersonFragment;
    private static final int TAB_HOME = 0X1;
    private static final int TAB_ADVISORY = 0X2;
//    private static final int TAB_LIVE = 0X3;
    private static final int TAB_LORD = 0X3;
    private static final int TAB_THOURGHTS = 0X4;
    
    private static int CURRENT_ITEM = 1;
    @Override
    protected void initEventAndData() {
        BmobUpdateAgent.setUpdateOnlyWifi(false);  //判断是否是wifi网络
        BmobUpdateAgent.update(this); //检测是否有更新

        UserBean myUser = BmobUser.getCurrentUser(UserBean.class); 
        
        // 开启手势
        disablePatternLock(false);
    }

    @Override
    protected void initInjecter() {
        mContainer = (FrameLayout) findViewById(R.id.container);
        mRgTabBar = (RadioGroup) findViewById(R.id.rgTabBar);
        mHome = (RadioButton) findViewById(R.id.home);
        mCategory = (RadioButton) findViewById(R.id.category);
        mGroup = (RadioButton) findViewById(R.id.group);
        mPerson = (RadioButton) findViewById(R.id.person);

        initFragment();
        addFragment();
        initRadio();
    }

    @Override
    protected int getresLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mCategoryFragment = new CategoryFragment();
//        mLiveFragment = new LiveFragment();
        mGroupFragment = new GroupFragment();
        mPersonFragment = new PersonFragment();

    }
    /**
     * 添加fragment
     */
    private void addFragment() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(R.id.container, mHomeFragment);
        transaction.add(R.id.container, mCategoryFragment);
//        transaction.add(R.id.container, mLiveFragment);
        transaction.add(R.id.container, mGroupFragment);
        transaction.add(R.id.container, mPersonFragment);
        transaction.commit();

        showFragment(CURRENT_ITEM);
    }
    /**
     * 显示Fragment
     *
     * @param tab 当前需要显示的位置
     */
    private void showFragment(int tab) {
        hideFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (tab) {
            case TAB_HOME:
                transaction.show(mHomeFragment);
                break;
            case TAB_ADVISORY:
                transaction.show(mCategoryFragment);
                break;
//            case TAB_LIVE:
//                transaction.show(mLiveFragment);
//                break;
            case TAB_LORD:
                transaction.show(mGroupFragment);
                break;
            case TAB_THOURGHTS:
                transaction.show(mPersonFragment);
                break;
        }
        transaction.commit();
    }
    /**
     * hide所有的fragment
     */
    private void hideFragment() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.hide(mHomeFragment);
        transaction.hide(mCategoryFragment);
//        transaction.hide(mLiveFragment);
        transaction.hide(mGroupFragment);
        transaction.hide(mPersonFragment);
        transaction.commit();
    }
    /**
     * RadioGroup的点击事件
     */
    private void initRadio() {
        mHome.setChecked(true);
        mRgTabBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home:
                        changeTab(TAB_HOME);
                        break;
                    case R.id.category:
                        changeTab(TAB_ADVISORY);
                        break;
                    case R.id.group:
                        changeTab(TAB_LORD);
                        break;
                    case R.id.person:
                        changeTab(TAB_THOURGHTS);
                        break;
                }
            }
        }); 
    }
    /**
     * 改变Tab
     *
     * @param checkId index
     */
    private void changeTab(int checkId) {
        switch (checkId) {
            case TAB_HOME:
                CURRENT_ITEM = TAB_HOME;
                showFragment(TAB_HOME);
                mHome.setChecked(true);
                break;
            case TAB_ADVISORY:
                CURRENT_ITEM = TAB_ADVISORY;
                showFragment(TAB_ADVISORY);
                mCategory.setChecked(true);
                break;
//            case TAB_LIVE:
//                CURRENT_ITEM = TAB_LIVE;
//
//                //  mRadioHome.setChecked(false);
//                //mRadioLord.setChecked(false);
//                //mRadioThoughts.setChecked(false);
//                //mRadioAdvisory.setChecked(false);
//                mRadioLive.setChecked(true);  //虽然这个没有用到，但是为了在点击这个live图片的时候
//                //使得其他的处于未点击状态，需要这个为true(RadioGroup的互斥事件)
//
//                showFragment(TAB_LIVE);
//
//
//                break;
            case TAB_LORD:
                CURRENT_ITEM = TAB_LORD;
                showFragment(TAB_LORD);
                mGroup.setChecked(true);
                break;
            case TAB_THOURGHTS:
                CURRENT_ITEM = TAB_THOURGHTS;
                mPerson.setChecked(true);
                showFragment(TAB_THOURGHTS);
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.tip))
                    .setMessage(getResources().getString(R.string.exit))
                    .setNegativeButton(getResources().getString(R.string.cancel), null)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                            MyApplication.myApplication.finishActivity();
                            dialog.dismiss();
                        }
                    }).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 不置零的效果是，如果在设定的一分钟之内再次打开APP则不会弹出手势密码
        // 因为应用的Activity全部finish后Application可能还存在
        // 这句置零代码也可以放在启动APP页面onResume()方法之前
        MyApplication.myApplication.setLockTime(0);
    }
}
