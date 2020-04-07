
package com.example.snmlangerandcanteenstore.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.snmlangerandcanteenstore.constant.ParamConstants;
import com.example.snmlangerandcanteenstore.model.User;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceUtil {

    public static User getUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String str_user = preferences.getString(ParamConstants.PARAM_USER, null);
        if (str_user != null && str_user.trim().length() > 0) {
            User user = new Gson().fromJson(str_user, User.class);
            return user;
        }
        return null;
    }

    public static void setUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        String json = "";
        if (user != null) {
            json = new Gson().toJson(user);
        }
        preferences.edit().putString(ParamConstants.PARAM_USER, json).commit();
    }

    public static Boolean getLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        return preferences.getBoolean(ParamConstants.PARAM_LOGIN, false);
    }

    public static void setLogin(Context context, Boolean login) {
        SharedPreferences preferences = context.getSharedPreferences(ParamConstants.PARAM_SNM, MODE_PRIVATE);
        preferences.edit().putBoolean(ParamConstants.PARAM_LOGIN, login).commit();
    }
}
