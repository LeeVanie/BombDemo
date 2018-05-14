package com.test.bombdemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.bombdemo.R;
import com.test.bombdemo.bean.UserBean;
import com.test.bombdemo.utils.CommonUtils;
import com.test.bombdemo.utils.ToastUtils;
import com.test.bombdemo.widget.CustomPrograss;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * @类名: ${type_name}
 * @功能描述:
 * @作者: ${user}
 * @时间: ${date}
 * @最后修改者:
 * @最后修改内容:
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    
    public static final String TAG = "RegisterActivity";
    
    private EditText user, password, email, phone, yzm;
    private TextView btn_reSend;
    private TextView finish;
    private ImageView back;

    private EventHandler eventHandler;
    private int time=60;
    private boolean flag=true;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        bindView();

        eventHandler = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void bindView() {
        user = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        yzm = (EditText) findViewById(R.id.yzm);
        btn_reSend = (TextView) findViewById(R.id.btn_reSend);
        finish = (TextView) findViewById(R.id.finish);
        back = (ImageView) findViewById(R.id.iv_back);
        
        back.setOnClickListener(this);
        btn_reSend.setOnClickListener(this);
        finish.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_reSend:
                if (CommonUtils.checkPhoneNum(this, phone.getText().toString().trim())){
//                    timer = new MyCountTimer(60000, 1000);
//                    timer.start();
                    SMSSDK.getVerificationCode("86", phone.getText().toString().trim());
                    yzm.requestFocus();
                }
                break;
            case R.id.finish:

                registerUser();
               
                flag=false;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    class SMSEvenHanlder extends EventHandler {
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg=new Message();
            msg.arg1=event;
            msg.arg2=result;
            msg.obj=data;
            handler.sendMessage(msg);
        }
    }
    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if(result == SMSSDK.RESULT_COMPLETE) {
                    boolean smart = (Boolean)data;
                    if(smart) {
                        ToastUtils.show(RegisterActivity.this,"该手机号已经注册过，请重新输入");
                        phone.requestFocus();
                        return;
                    }
                }
            }
            if(result==SMSSDK.RESULT_COMPLETE) {

                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    ToastUtils.show(RegisterActivity.this, "验证码输入正确");
                }
            }
            else {
                if(flag) {
                    btn_reSend.setText("重新发送验证码");
                    ToastUtils.show(RegisterActivity.this,"验证码获取失败请重新获取");
                    phone.requestFocus();
                } else {
                    ToastUtils.show(RegisterActivity.this,"验证码输入错误");
                }
            }

        }

    };
    
    public void registerUser(){
        if (isEmpty()) {
            CustomPrograss.show(this,getResources()
                    .getString(R.string.register_loading),false,null);
            UserBean bu = new UserBean();
            bu.setUsername(user.getText().toString().trim());
            bu.setPassword(password.getText().toString().trim());
            bu.setMobilePhoneNumber(phone.getText().toString().trim());
            bu.setEmail(email.getText().toString().trim());
//            if (bindMobilePhone(phone.getText().toString().trim())) {
                bu.signUp(new SaveListener<UserBean>() {
                    @Override
                    public void done(UserBean s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//            }
            CustomPrograss.disMiss();
        }
    }

    public boolean isEmpty(){
        if (user.getText().toString().trim().equals("")
                && password.getText().toString().trim().equals("")
                && email.getText().toString().trim().equals("")
                && phone.getText().toString().trim().equals("")){
            ToastUtils.show(this, "请填写完整!!!");
            return false;
        } else {
            final String phoneNumber = phone.getText().toString().trim();
            String code = yzm.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                ToastUtils.show(this, "验证码不能为空");
                return false;
            }
            if(!CommonUtils.judCord(this, phoneNumber, yzm).equals("")) {
                SMSSDK.submitVerificationCode("86", phoneNumber, yzm.getText().toString().trim());
                return true;
            }
            return false;
        }
    }
    private boolean isFlag;
    private boolean bindMobilePhone(String phone){
        isFlag = false;
        //开发者在给用户绑定手机号码的时候需要提交两个字段的值：mobilePhoneNumber、mobilePhoneNumberVerified  
        UserBean user =new UserBean();
        user.setMobilePhoneNumber(phone);
        user.setMobilePhoneNumberVerified(true);
        UserBean cur= BmobUser.getCurrentUser(UserBean.class);
        user.update(cur.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    isFlag = true;
                    ToastUtils.show(RegisterActivity.this, "手机号码绑定成功");
                }
                else{
                    isFlag = false;
                    ToastUtils.show(RegisterActivity.this, "手机号码绑定失败");
                }
            }
        });
        return isFlag;
    }


    MyCountTimer timer;
    
    class MyCountTimer extends CountDownTimer {

        public MyCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            btn_reSend.setText((millisUntilFinished / 1000) +"秒后重发");
        }
        @Override
        public void onFinish() {
            btn_reSend.setText("重新发送验证码");
        }
    }

    
}
