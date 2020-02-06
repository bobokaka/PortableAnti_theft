package com.example.portableanti_theft.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.portableanti_theft.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignOrRegisterActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_register);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.sign_signin, R.id.sign_register, R.id.sign_qq, R.id.sign_weixin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //登录
            case R.id.sign_signin:
                Intent intent = new Intent(SignOrRegisterActivity.this, SignInActivity.class);
                //实现只产生一个实例
//                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            //注册
            case R.id.sign_register:
                Intent intent2 = new Intent(SignOrRegisterActivity.this, RegisterActivity.class);
//                intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent2);
                break;
            //QQ登录
            case R.id.sign_qq:
                Toast.makeText(this, "暂未开放", Toast.LENGTH_SHORT).show();
                break;
            //微信登录
            case R.id.sign_weixin:
                Toast.makeText(this, "暂未开放", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
