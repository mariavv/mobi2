package com.exsample.maria.mobi2.mvp.view;

import android.content.Context;

import com.arellomobile.mvp.MvpView;

/**
 * Created by maria on 09.03.2018
 */

public interface ProfileView extends MvpView {
    void fillFields(final String email, final String displayName, final String phoneNumber);

    void say(int messageRes);

    void say(String message);

    void close();

    void showPopupMenu();
}
