package com.exsample.maria.mobi2.mvp.present;

import android.content.Context;
import android.util.Patterns;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.manager.AuthManager;
import com.exsample.maria.mobi2.manager.DbManager;
import com.exsample.maria.mobi2.mvp.model.User;
import com.exsample.maria.mobi2.mvp.view.ProfileView;

/**
 * Created by maria on 01.03.2018
 */

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ProfileView>
        implements AuthManager.Listener, DbManager.Listener {

    public void onActivityCreate(Context context) {
        if (!(new AuthManager(this).userExists())) {
            getViewState().close();
        }
        //AuthManager manager = new AuthManager(this);
        //getViewState().fillFields(manager.getEmail(), manager.getDisplayName(), manager.getPhoneNumber());


        (new DbManager(this))
               .read(context.getString(R.string.db_users_table), (new AuthManager(this)).getUserId(), User.class);
    }

    public void onSaveBtnPressed(Context context, String email, String displayName, String phoneNumber) {
        if ((Patterns.EMAIL_ADDRESS.matcher(email).matches()) /*&& (displayName.length() > 0)*/) {
            (new DbManager(this))
                    .write(context.getString(R.string.db_users_table),
                            (new AuthManager(this)).getUserId(),
                            (new User(email, displayName, phoneNumber)));

            //AuthManager manager = new AuthManager(this);
            //manager.updateEmail(email);
            getViewState().say(R.string.saved);
        } else {
            getViewState().say(R.string.not_valid);
        }
    }

    @Override
    public void error(String message) {
        getViewState().say(message);
    }

    @Override
    public void onDataChange(Object value) {
        User user = (User) value;
        if (user != null) {
            //getViewState().say(user.getEmail());
        } else {
            //getViewState().say("user null");
        }
    }

    @Override
    public void onError(String error) {
        getViewState().say(error);
    }

    public void onChangePhotoBtnPressed() {
        getViewState().showPopupMenu();
    }
}
