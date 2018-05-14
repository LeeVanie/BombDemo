package com.test.bombdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.bombdemo.R;
import com.test.bombdemo.base.BaseActivity;
import com.test.bombdemo.bean.UserBean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscriber;

/**
 * @类名: ${type_name}
 * @功能描述:
 * @作者: ${user}
 * @时间: ${date}
 * @最后修改者:
 * @最后修改内容:
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText user, psd;
    private Button login, register;
    private ImageView qqLogin, wxLogin, sinaLogin;
    private TextView forgetpsd;
    
    @Override
    protected void initEventAndData() {
        // 启动页不开启手势
        disablePatternLock(true);
        
    }

    @Override
    protected void initInjecter() {
        user = (EditText) findViewById(R.id.loginname);
        psd = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        qqLogin = (ImageView) findViewById(R.id.qq_login);
        wxLogin = (ImageView) findViewById(R.id.wx_login);
        sinaLogin = (ImageView) findViewById(R.id.sina_login);
        forgetpsd = (TextView) findViewById(R.id.forgetpsd);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        qqLogin.setOnClickListener(this);
        wxLogin.setOnClickListener(this);
        sinaLogin.setOnClickListener(this);
        forgetpsd.setOnClickListener(this);
    }

    @Override
    protected int getresLayoutID() {
        return R.layout.activity_login;
    }

    public boolean isEmpty(){
        if (user.getText().toString().trim().equals("") && 
                psd.getText().toString().trim().equals("")){
            Toast.makeText(this, "Please fill in the complete!!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                loginUser();
                break;
            case R.id.register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.qq_login:
                break;
            case R.id.wx_login:
                break;
            case R.id.sina_login:
                break;
            case R.id.forgetpsd:
                startActivity(new Intent(this, ForgetActivity.class));
                break;
        }
    }
    
    private void loginUser() {
        if (isEmpty()){
            BmobUser bu = new BmobUser();
            bu.setUsername(user.getText().toString().trim());
            bu.setPassword(psd.getText().toString().trim());
            bu.loginObservable(BmobUser.class).subscribe(new Subscriber<BmobUser>() {
                @Override
                public void onCompleted() {
                    Toast.makeText(LoginActivity.this, "Login Success!!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Throwable throwable) {
                    Toast.makeText(LoginActivity.this, "Login Failed!!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(BmobUser bmobUser) {
                    UserBean user =  BmobUser.getCurrentUser(UserBean.class);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }


}
