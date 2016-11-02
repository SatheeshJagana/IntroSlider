package com.example.personal.introslider;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PERSONAL on 9/7/2016.
 */
public class IntroManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    public IntroManager(Context context)
    {
        this.context = context;
        preferences = context.getSharedPreferences("firsttime", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void setForFirstTimeLaunch(boolean isFirst)
    {
        editor.putBoolean("checkforfirsttime", isFirst);
        editor.commit();
    }

    public boolean isFirstTimeLaunch()
    {
        return preferences.getBoolean("checkforfirsttime",true);
    }
}
