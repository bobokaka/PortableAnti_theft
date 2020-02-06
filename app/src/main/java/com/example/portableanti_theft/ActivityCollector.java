package com.example.portableanti_theft;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    private static List<Activity> activities = new ArrayList<>();

    //添加活动
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static List<Activity> getActivities() {
        return activities;
    }

    //移除活动
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    //销毁所有活动
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
