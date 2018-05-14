package com.test.bombdemo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.test.bombdemo.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @类名: ${type_name}
 * @功能描述:
 * @作者: ${user}
 * @时间: ${date}
 * @最后修改者:
 * @最后修改内容:
 */
public class CommonUtils {

    /**
     * 检查手机号是否正确
     *
     * @param phone  手机号码
     */
    public static boolean checkPhoneNum(Context context, String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(context, context.getResources().getString(R.string.phone_num_is_not_empty));
            return false;
        }

        if (phone.length() != 11) {
            ToastUtils.show(context, context.getResources().getString(R.string.phone_num_length_is_error));
            return false;
        }
        
        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            ToastUtils.show(context, context.getResources().getString(R.string.check_your_phone_format));
            return false;
        }
        return true;
    }


    public static String judCord(Context context, String phone, EditText edit_cord) {
        if(TextUtils.isEmpty(phone)) {
            ToastUtils.show(context,"请输入您的验证码");
            edit_cord.requestFocus();
            return "";
        } else if(edit_cord.getText().toString().trim().length()!=6) {
            ToastUtils.show(context,"您的验证码位数不正确");
            edit_cord.requestFocus();

            return "";
        } else {
            return edit_cord.getText().toString().trim();
        }
    }

}
