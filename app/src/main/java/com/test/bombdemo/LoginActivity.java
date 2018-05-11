package com.test.bombdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
public class LoginActivity extends Activity {

    EditText user, psd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    
        user = (EditText) findViewById(R.id.loginname);
        psd = (EditText) findViewById(R.id.password);
        
        
        bindview();
        
    }

    private void bindview() {
    
        findViewById(R.id.forgetpsd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty()) {
                    UserBean bu = new UserBean();
                    bu.setUsername(user.getText().toString().trim());
                    bu.setPassword(psd.getText().toString().trim());
                    bu.setMobilePhoneNumber("13668141001");
                    bu.setEmail("914284937@qq.com");
                    bu.signUp(new SaveListener<UserBean>() {
                        @Override
                        public void done(UserBean s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(LoginActivity.this, "注册成功:" + s.toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            Toast.makeText(LoginActivity.this, "Login Success!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    
    public boolean isEmpty(){
        if (user.getText().toString().trim().equals("") && 
                psd.getText().toString().trim().equals("")){
            Toast.makeText(this, "Please fill in the complete!!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
