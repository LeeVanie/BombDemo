package com.test.bombdemo.base;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment{
    protected View contentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(getContentViewResID(),null);
        return contentView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initDatas();
    }

    protected abstract int getContentViewResID();
    protected abstract void initViews();
    protected abstract void initDatas();

    /**
     * 获取状态栏高度
     */
    public int getStatusHeight() {
        final Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int n = rect.top;
        if (n != 0) {
            return n;
        }
        try {
            final Class<?> forName = Class.forName("com.android.internal.R$dimen");
            n = getResources().getDimensionPixelSize(Integer.parseInt(forName.getField("status_bar_height").get(forName.newInstance()).toString()));
            return n;
        }catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return n;
        }catch (IllegalAccessException ex2) {
            ex2.printStackTrace();
            return n;
        }catch (java.lang.InstantiationException ex3) {
            ex3.printStackTrace();
            return n;
        }catch (NumberFormatException ex4) {
            ex4.printStackTrace();
            return n;
        }catch (IllegalArgumentException ex5) {
            ex5.printStackTrace();
            return n;
        }catch (SecurityException ex6) {
            ex6.printStackTrace();
            return n;
        }catch (NoSuchFieldException ex7) {
            ex7.printStackTrace();
            return n;
        }
    }
}
