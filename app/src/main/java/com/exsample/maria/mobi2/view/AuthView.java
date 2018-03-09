package com.exsample.maria.mobi2.view;


import com.arellomobile.mvp.MvpView;

/**
 * Created by maria on 09.03.2018
 */

public interface AuthView extends MvpView {
    void setUpLoginBtn(int resText, int resColor, boolean enabled);

    void showError(String message);

    void close(int result);
}
