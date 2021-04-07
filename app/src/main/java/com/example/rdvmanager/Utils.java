package com.example.rdvmanager;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
public class Utils
{
    private static int sTheme;
    public final static int LightTheme = 0;
    public final static int DarkTheme = 1;

    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case LightTheme:
                activity.setTheme(R.style.LightTheme);
                break;
            case DarkTheme:
                activity.setTheme(R.style.DarkTheme);
                break;
        }
    }
}
