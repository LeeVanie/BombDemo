package com.test.bombdemo.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.test.bombdemo.Common.Config;
import com.test.bombdemo.activity.gesturepsd.LockActivity;
import com.test.bombdemo.application.MyApplication;


/**
 * @类名: ${type_name}
 * @功能描述:
 * @作者: ${user}
 * @时间: ${date}
 * @最后修改者:
 * @最后修改内容:
 */
public abstract class BaseActivity extends AppCompatActivity {
    
    private Context mContext;
    private MyApplication myApp;

    // 页面是否允许唤起手势密码
    private boolean enableLock = true;
    // 下一个页面是否唤起手势密码
    private boolean nextShowLock = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        myApp = MyApplication.myApplication;
        initState();
        setContentView(getresLayoutID());
        myApp.addActivity(this);   //把所有的Activity加入到集合中
        initInjecter();

        //注册一个监听连接状态的listener
        initEventAndData();
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (enableLock) {
            // 减得当前APP在后台滞留的时间 durTime
            long durTime = System.currentTimeMillis() - myApp.getLockTime();
            if (durTime > Config.LOCK_TIME) {
                // 显示手势密码页面
                showLockActivity();
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (enableLock || !nextShowLock) {
            // 更新 lockTime
            myApp.setLockTime(System.currentTimeMillis());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //        myApp.setLockTime(0);
    }

    protected abstract void initEventAndData();

    protected abstract void initInjecter();

    protected abstract int getresLayoutID();

    /**
     * 沉浸式状态栏   
     */
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void onDestroy() {

      
        // AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    /**
     * 跳转至手势密码页面
     */
    private void showLockActivity() {
        if (myApp.getSettings() != null
                && myApp.getSettings().getGesture() != null
                && !myApp.getSettings().getGesture().isEmpty()) {
            startActivity(new Intent(mContext, LockActivity.class));
        }
    }

    /**
     * 部分页面禁用手势密码需要调用该方法，例如启动页、注册登录页、解锁页（LockActivity）等
     * 在这些页面如果停留时间较久后，如果想进入下一个页面时不弹出手势，需要在finish前手动添加
     * myApp.setLockTime(System.currentTimeMillis());
     * 或者传入新的参数进行标识，在onPause中根据标识判断是否setLockTime
     * 本例选择传入参数
     * nextShowLock 为false 表示onPause()会调用setLockTime()，则下一个页面不会唤起手势
     *
     * @param nextShowLock
     */
    protected void disablePatternLock(boolean nextShowLock) {
        enableLock = false;
        this.nextShowLock = nextShowLock;
    }


}
//jhfghfh