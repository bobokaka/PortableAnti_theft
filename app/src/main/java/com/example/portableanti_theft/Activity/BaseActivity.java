package com.example.portableanti_theft.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.portableanti_theft.ActivityCollector;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static List<Activity> activities = new ArrayList<>();
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init();
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        ActivityCollector.addActivity(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);

    }
}
