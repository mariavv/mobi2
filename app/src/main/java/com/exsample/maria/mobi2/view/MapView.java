package com.exsample.maria.mobi2.view;

import com.arellomobile.mvp.MvpView;

/**
 * Created by maria on 07.03.2018
 */

public interface MapView extends MvpView {
    void sayHi(String greeting);
    void sayHi(int greeting);
    void showError(String errMessage);
}
