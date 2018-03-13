package com.exsample.maria.mobi2.mvp.view;

import com.arellomobile.mvp.MvpView;

/**
 * Created by maria on 07.03.2018
 */

public interface MapView extends MvpView {
    void changeText(String user);

    void changeText(int greetingRes);

    void say(String message);

    void startAuthActivity(final int reguestCode);

    void startProfileActivity();
}
