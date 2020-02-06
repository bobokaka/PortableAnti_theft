package com.example.portableanti_theft.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portableanti_theft.MyApplication;
import com.example.portableanti_theft.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * @ProjectName: PortableAnti_theft
 * @Package: com.example.portableanti_theft.Fragment
 * @ClassName: Register2Fragment
 * @Description: java类作用描述写这里
 * @Author: ED_Peng
 * @CreateDate: 2019-3-28 0028 上午 05:46
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-3-28 0028 上午 05:46
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 **/
public class Register2Fragment extends Fragment implements View.OnClickListener {
    private static String TAG = "Register2Fragment";
    private TextView registe2PhoneNumber;
    private EditText register2_code;
    private Button register2_next_button;
    private Button register2_yanzhengma_button;
    private String userphonenumber;
    private String country = "86";
    private String code;
    private EventHandler eventHandler;
    private static final int YZM = 1;
    private static final int ADOPT = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case YZM:
                    register2_yanzhengma_button.setText("获取验证码");
                    register2_yanzhengma_button.setEnabled(true);
                    register2_yanzhengma_button.setBackgroundResource(R.drawable.shape5);
                    break;
                case ADOPT:
                    Log.d(TAG, "****************进入handler——SUCCESS***************" );
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    Register3Fragment register3Fragment = new Register3Fragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.add(R.id.register_framelayout, register3Fragment);
                    //add使用，要Hide，防止出现页面重叠
                    transaction.hide(Register2Fragment.this);
                    transaction.commit();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "************进入Register2Fragment的onCreateView******************");
        View view = inflater.inflate(R.layout.activity_register2, container, false);
        registe2PhoneNumber = (TextView) view.findViewById(R.id.registe2_phone_number);
        register2_code = (EditText) view.findViewById(R.id.register2_code);
        register2_next_button = (Button) view.findViewById(R.id.register2_next_button);
        register2_yanzhengma_button = (Button) view.findViewById(R.id.register2_yanzhengma_button);

        // 请求验证码
        SendYZM();
        
        register2_next_button.setOnClickListener(this);
        register2_yanzhengma_button.setOnClickListener(this);
        if (userphonenumber != null) {
            //设置屏幕上出现用户的手机号码
            registe2PhoneNumber.setText(userphonenumber);
            // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
//            SMSSDK.getVerificationCode(country, userphonenumber);
//            Smssdk();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register2_next_button:
                code = register2_code.getText().toString().trim();
                Log.d(TAG, "***************code:******************" +code);
                if (code.equals("")) {
                    Toast.makeText(MyApplication.getContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else if (code.length() != 4) {
                    Toast.makeText(MyApplication.getContext(), "验证码必须是4位数字", Toast.LENGTH_SHORT).show();
                } else {
                    // 提交验证码，其中的code表示验证码，如“1357”
//                    SMSSDK.submitVerificationCode(country, userphonenumber, code);
//                    Smssdk();
                    Message msg = Message.obtain();
                    msg.what = ADOPT;
                    handler.sendMessage(msg);
                }

                break;
            case R.id.register2_yanzhengma_button:
                SendYZM();
                break;
            default:
                break;
        }
    }

    private void Smssdk() {
        Log.d(TAG, "********************执行Smssdk方法*******************");
//        new Thread(() -> {
            eventHandler = new EventHandler() {
                public void afterEvent(int event, int result, Object data) {
                    // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
                    Message msg = new Message();
                    msg.arg1 = event;
                    msg.arg2 = result;
                    msg.obj = data;
                    new Handler(Looper.getMainLooper(), new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            int event = msg.arg1;
                            int result = msg.arg2;
                            Object data = msg.obj;
                            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                if (result == SMSSDK.RESULT_COMPLETE) {
                                    // TODO 处理成功得到验证码的结果
                                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                                } else {
                                    // TODO 处理错误的结果
                                    ((Throwable) data).printStackTrace();
                                }
                            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                if (result == SMSSDK.RESULT_COMPLETE) {
                                    // TODO 处理验证码验证通过的结果
                                    Log.d(TAG, "************处理验证码验证通过的结果*************");
                                    msg = Message.obtain();
                                    msg.what = ADOPT;
                                    handler.sendMessage(msg);
                                } else {
                                    // TODO 处理错误的结果
                                    ((Throwable) data).printStackTrace();
                                }
                            }
                            // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                            return false;
                        }
                    }).sendMessage(msg);
                }
            };
//        }).start();
    }

    //发送验证码按钮控制
    private void SendYZM() {
        Log.d(TAG, "*****************执行SendYZM方法*******************");
        //不可选中状态
        register2_yanzhengma_button.setEnabled(false);
        register2_yanzhengma_button.setBackgroundResource(R.drawable.shape6);
        // 注册一个事件回调，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler);
        // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
        SMSSDK.getVerificationCode(country, userphonenumber);
        Smssdk();
        new Thread(() -> {

            for (int i = 59; i > 0; i--) {
                register2_yanzhengma_button.setText("重新获取（" + i + "）");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message msg = Message.obtain();
            msg.what = YZM;
            handler.sendMessage(msg);

        }).start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "****************进入onAttach***********************");
        Bundle arguments = getArguments();
        userphonenumber = arguments.getString("userphonenumber");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
