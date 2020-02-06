package com.example.portableanti_theft.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.portableanti_theft.Fragment.Register1Fragment;
import com.example.portableanti_theft.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    /**
     * 注册手机号码
     */
    private EditText etPhoneNumber;
    private EditText ePassword;
    private Button bnext1;
    @BindView(R.id.nav_back)
    ImageView navBack;
    @BindView(R.id.nav_home_text)
    TextView navHomeText;

    @OnClick(R.id.nav_back)
    public void onViewClicked() {
        finish();
    }



    private Register1Fragment register1Fragment = new Register1Fragment();
    //    private Register2Fragment register2Fragment;
//    private Register3Fragment register3Fragment;
    private static String URL = "http://129.204.201.70:8080/gpshyy/user/gologin";
    private static String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        navHomeText.setText("手机号注册");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.register_framelayout, register1Fragment);
        //实现碎片返回栈
        transaction.commit();
    }

    @Override
    public void onClick(View v) {

    }

    //动态加载碎片
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.register_framelayout, fragment);
        //实现碎片返回栈
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
    }
}
