package com.example.portableanti_theft.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portableanti_theft.R;
import com.example.portableanti_theft.Util.HttpUtil;
import com.example.portableanti_theft.Util.ReLoUtil;
import com.example.portableanti_theft.domain.LoginBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInActivity extends BaseActivity {

    private static String URL = "http://129.204.201.70:8080/gpshyy/user/gologin";
    private static String TAG = "SignInActivity";
    private static final int SUCCESS = 1;
    private static final int FAILURE = 2;
    private static final int ERROR = 3;

    private OkHttpClient client = null;
    //    public static final MediaType JSON = MediaType.parse("spplication/json;charset=utf-8");
    private LoginBean loginBean;
    /**
     * 1.定义一个共享参数（存放数据方便的api）
     */
    private SharedPreferences sharedPreferences;

    /**
     * get请求返回主线程处理
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //将进度条关闭,解锁点击
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            switch (msg.what) {
                case SUCCESS:
                    //登陆成功，跳转到主界面
                    //向服务器请求用户名和头像存入本地的地址，传给活动MainActivity
                    Log.d(TAG, "**********正在执行SUCCESS**********" + loginBean.toString());
                    LitePal.getDatabase();
                    loginBean.save();
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    //启动的活动如果已存在，置换到栈顶
                    //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                    break;
                case FAILURE:
                    //登陆失败
                    Toast.makeText(SignInActivity.this, "用户名或密码错误，请重新输入。", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    //登陆失败
                    Toast.makeText(SignInActivity.this, "无法登录，请重试", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    @BindView(R.id.nav_home_text)
    TextView navHomeText;
    @BindView(R.id.nav_back)
    ImageView navBack;
    @BindView(R.id.sign_sign_in_button)
    Button signSignInButton;
    @BindView(R.id.sign_checkBox_password)
    CheckBox checkBox;
    @BindView(R.id.sign_phone_number)
    EditText signPhoneNumber;
    @BindView(R.id.sign_password)
    EditText signPassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        navHomeText.setText("手机号登录");
        //通过上下文的到一个共享参数实例
        sharedPreferences = this.getSharedPreferences("config", this.MODE_PRIVATE);
        restoreInfo();
        //如果存在本地账号，则记住密码处于选中状态
        if (!signPhoneNumber.equals("")) {
            checkBox.setChecked(true);
        }

    }

    /**
     * 根据原来保存的信息，把QQ号码和密码信息显示到界面
     * 从SharedPreferences文件里面读取文件
     */
    private void restoreInfo() {
        String PhoneNumber = sharedPreferences.getString("userphonenumber", "");
        String SignPassword = sharedPreferences.getString("userpassword", "");
        signPhoneNumber.setText(ReLoUtil.convertMD5(PhoneNumber));
        signPassword.setText(ReLoUtil.convertMD5(SignPassword));
    }

    int i = 1;

    @OnClick({R.id.nav_back, R.id.sign_sign_in_button, R.id.sign_checkBox_password,
            R.id.sign_see_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                //标题栏返回键
                finish();
                break;
            case R.id.sign_see_password:
                if (i == 1) {
                    //显示密码
                    signPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    i = 0;
                } else {
                    //隐藏密码
                    signPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    i = 1;
                }
                break;
            case R.id.sign_sign_in_button:

                String phoneNumber = signPhoneNumber.getText().toString().trim();
                String password = signPassword.getText().toString().trim();
                Log.d(TAG, "手机账号:" + signPhoneNumber + "\n密码：" + signPassword);
                //判断用户名或密码不为空
                if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "手机号码和密码不能为空", Toast.LENGTH_SHORT).show();
                    break;
                } else if (!ReLoUtil.isMobileNO(phoneNumber)) {
                    Toast.makeText(this, "您输入的不是有效的手机号码", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    if (checkBox.isChecked()) {
                        //被选中状态，需要记住用户名和密码
                        //将数据保存到sharedPreferences文件
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("userphonenumber", ReLoUtil.convertMD5(phoneNumber));
                        editor.putString("userpassword", ReLoUtil.convertMD5(password));
                        editor.commit();
                    } else {
                        //不记住密码选中状态，将数据清空
                        //将数据保存到sharedPreferences文件
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userphonenumber", ReLoUtil.convertMD5(""));
                        editor.putString("userpassword", ReLoUtil.convertMD5(""));
                        editor.commit();
                    }

                    //设置加载按钮并锁定不许点击
                    progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    //登录，连接远程服务器
                    LoginRequest(phoneNumber, password);
                }
                break;
        }
    }

    //MD5 加密算法


    public void LoginRequest(final String accountNumber, final String password) {
        new Thread(() -> {
            try {
                client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("userphonenumber", accountNumber)
                        .add("userpassword", password)
                        .build();
                HttpUtil.sendOKHttpRequest(URL, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //在这里进行异常情况的处理
                        Message msg = Message.obtain();
                        msg.what = ERROR;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //得到服务器返回的具体内容
                        //向服务器请求用户名和头像(服务器地址)
                        String responseData = response.body().string();
                        if (response.code() != 200) {
                            Message msg = Message.obtain();
                            msg.what = ERROR;
                            handler.sendMessage(msg);
                        } else if (responseData.equals("null")) {
                            Message msg = Message.obtain();
                            msg.what = FAILURE;
                            handler.sendMessage(msg);
                        } else {
                            //json解析
                            parseJSONWithJSONObject(responseData);
                            Message msg = Message.obtain();
                            msg.what = SUCCESS;
                            handler.sendMessage(msg);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = ERROR;
                handler.sendMessage(msg);
            }
        }).start();
    }

    //json解析
    private void parseJSONWithJSONObject(String jsonData) {
        Gson gson = new Gson();
        loginBean = gson.fromJson(jsonData, new TypeToken<LoginBean>() {
        }.getType());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!checkBox.isChecked()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userphonenumber", ReLoUtil.convertMD5(""));
            editor.putString("userpassword", ReLoUtil.convertMD5(""));
            editor.commit();
        }
    }


}
