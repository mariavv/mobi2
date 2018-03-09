package com.exsample.maria.mobi2.mvp.present.i;

import com.exsample.maria.mobi2.ui.activities.MapActivity;

/**
 * Created by maria on 10.03.2018
 */

public interface IMapPresenter {
    void changeText(MapActivity activity, int resGreeting);

    void error(String message);
}
