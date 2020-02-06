package com.example.portableanti_theft.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.portableanti_theft.MyApplication;
import com.example.portableanti_theft.R;
import com.example.portableanti_theft.Util.HttpUtil;
import com.example.portableanti_theft.Util.ReLoUtil;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @ProjectName: PortableAnti_theft
 * @Package: com.example.portableanti_theft.Fragment
 * @ClassName: Register1Fragment
 * @Description: java类作用描述写这里
 * @Author: ED_Peng
 * @CreateDate: 2019-3-28 0028 上午 06:03
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-3-28 0028 上午 06:03
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 **/
public class Register1Fragment extends Fragment implements View.OnClickListener {

    private OkHttpClient client = null;
    private static String URL = "http://129.204.201.70:8080/gpshyy/user/uph";
    private final String TAG = "Register1Fragment";
    /**
     * 注册手机号码
     */
    private EditText etPhoneNumber;
    private EditText ePassword;
    private Button register1NextButton;
    private String rephonenumber;
    private String repassword;

    private static final int SUCCESS = 1;
    private static final int FAILURE = 2;
    private static final int ERROR = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    Log.d(TAG, "****************进入handler——SUCCESS***************" );
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    Register2Fragment register2Fragment = new Register2Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("userphonenumber", rephonenumber);
                    register2Fragment.setArguments(bundle);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.add(R.id.register_framelayout, register2Fragment);
                    //add使用，要Hide，防止出现页面重叠
                    transaction.hide(Register1Fragment.this);
                    transaction.commit();

                    break;
                case FAILURE:
                    Toast.makeText(MyApplication.getContext(), "该手机号码已注册", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    //登陆失败
                    Toast.makeText(MyApplication.getContext(), "注册异常，请重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register1, container, false);
        register1NextButton = (Button) view.findViewById(R.id.register1_next_button);
        etPhoneNumber = (EditText) view.findViewById(R.id.register1_phone_number);
        ePassword = (EditText) view.findViewById(R.id.register1_password);
        register1NextButton.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register1_next_button:
                rephonenumber = etPhoneNumber.getText().toString().trim();
                repassword = ePassword.getText().toString().trim();
                Log.d(TAG, "******************用户名*****************" + rephonenumber +
                        "\n******************密码*****************" + repassword);
                //判断用户名或密码不为空
                if (TextUtils.isEmpty(rephonenumber) || TextUtils.isEmpty(repassword)) {
                    Toast.makeText(MyApplication.getContext(), "手机号码和密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!ReLoUtil.isMobileNO(rephonenumber)) {
                    Toast.makeText(MyApplication.getContext(), "您输入的不是有效的手机号码", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    isRegister(rephonenumber);
                }
                break;
            default:
                break;
        }
    }

    public void isRegister(final String accountNumber) {
        new Thread(() -> {
            try {
                Log.d(TAG, "***********进入isRegister线程**************");
                client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("userphonenumber", accountNumber)
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
                            Log.d(TAG, "************进入ERROR***********************" + responseData);
                            Message msg = Message.obtain();
                            msg.what = ERROR;
                            handler.sendMessage(msg);
                        } else if (responseData.equals("\"failure\"")) {
                            Log.d(TAG,
                                    "************进入SUCCESS***********************" + responseData);
                            Message msg = Message.obtain();
                            msg.what = SUCCESS;
                            handler.sendMessage(msg);
                        } else if (responseData.equals("\"success\"")) {
                            Log.d(TAG,
                                    "************进入FAILURE***********************" + responseData);
                            Message msg = Message.obtain();
                            msg.what = FAILURE;
                            handler.sendMessage(msg);
                        } else {
                            Log.d(TAG, "************进入else***********************" + responseData);
                            Message msg = Message.obtain();
                            msg.what = ERROR;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}