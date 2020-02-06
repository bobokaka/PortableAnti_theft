package com.example.portableanti_theft.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.portableanti_theft.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @ProjectName: PortableAnti_theft
 * @Package: com.example.portableanti_theft.Fragment
 * @ClassName: Register3Fragment
 * @Description: java类作用描述写这里
 * @Author: ED_Peng
 * @CreateDate: 2019-3-28 0028 上午 05:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-3-28 0028 上午 05:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 **/
public class Register3Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register3, container, false);
        return view;
    }
}
