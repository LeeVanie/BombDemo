package com.test.bombdemo.activity.gesturepsd;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.bombdemo.Common.Setting;
import com.test.bombdemo.R;
import com.test.bombdemo.application.MyApplication;
import com.test.bombdemo.base.BaseActivity;
import com.test.bombdemo.widget.GestureLockViewGroup;


/**
 * 取消手势密码
 */
public class LockOffActivity extends BaseActivity {
    private static final String TAG = LockOffActivity.class.getSimpleName();
    private TextView mTextView;
    private GestureLockViewGroup mGesture;
    private LinearLayout activityUnlock;

    private Context mContext;
    private MyApplication myApp;

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected void initInjecter() {
        setTitle("取消手势密码");
        mContext = this;

        myApp = MyApplication.myApplication;
        
        mTextView = (TextView) findViewById(R.id.tv_prompt_lock_off);
        mGesture = (GestureLockViewGroup) findViewById(R.id.gesture_lock_view_group_lock_off);
        activityUnlock = (LinearLayout) findViewById(R.id.activity_unlock);
        initView();
    }

    @Override
    protected int getresLayoutID() {
        return R.layout.activity_lock_off;
    }

    private void initView() {
        mTextView.setText("请绘制手势密码");

        mGesture.setAnswer(myApp.getSettings().getGesture());
        mGesture.setShowPath(Setting.SHOW_PATH.equals(myApp.getSettings().getShowPath()));
        mGesture.setOnGestureLockViewListener(mListener);
    }

    private void gestureEvent(boolean matched) {
        if (matched) {
            mTextView.setText("输入正确，手势关闭");
            myApp.setSettings(new Setting("", Setting.SHOW_PATH));
            setResult(RESULT_OK);
            finish();
        } else {
            mTextView.setText("手势错误，还剩" + mGesture.getTryTimes() + "次");
        }
    }

    private void unmatchedExceedBoundary() {
        // 正常情况这里需要做处理（如退出或重登）
        Toast.makeText(mContext, "错误次数太多，请重新登录", Toast.LENGTH_SHORT).show();
    }

    // 回调监听
    private GestureLockViewGroup.OnGestureLockViewListener mListener = new GestureLockViewGroup
            .OnGestureLockViewListener() {
        @Override
        public void onGestureEvent(boolean matched) {
            gestureEvent(matched);
        }

        @Override
        public void onUnmatchedExceedBoundary() {
            unmatchedExceedBoundary();
        }

        @Override
        public void onFirstSetPattern(boolean patternOk) {
        }
    };
}
