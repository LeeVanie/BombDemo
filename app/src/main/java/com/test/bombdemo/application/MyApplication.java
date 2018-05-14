package com.test.bombdemo.application;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

import com.mob.MobSDK;
import com.test.bombdemo.Common.Setting;
import com.test.bombdemo.utils.ManifestUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.smssdk.SMSSDK;

/**
 * @类名: ${type_name}
 * @功能描述:
 * @作者: ${user}
 * @时间: ${date}
 * @最后修改者:
 * @最后修改内容:
 */
public class MyApplication extends Application {

    public static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        
        myApplication = this;
        activityList = new ArrayList<>();
        initBomb();
        MobSDK.init(this);

    }

    private void initBomb() {

        //第一：默认初始化
        Bmob.initialize(this, "5bf8db94b0d04f44d53bba969575d3a3");
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("5bf8db94b0d04f44d53bba969575d3a3")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    
    }
    public static File getFileLocation() {
        return myApplication.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
    }

    /** ----------------------- 一些公共的变量 ------------------------- */
    private long lockTime = 0; // 保存的是最近一次调用onPause()的系统时间
    private Setting settings;  // 手势设置

    /** ----------------------- 一些set/get方法 ------------------------- */
    public long getLockTime() {
        return lockTime;
    }

    public void setLockTime(long lockTime) {
        this.lockTime = lockTime;
    }

    public Setting getSettings() {
        return settings;
    }

    public void setSettings(Setting settings) {
        this.settings = settings;
    }


    private List<Activity> activityList;

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void finishActivity() {

        if (activityList.size() == 0) {
            activityList.clear();
        }

        for (Activity activity : activityList) {
            activity.finish();
        }

    }
}
