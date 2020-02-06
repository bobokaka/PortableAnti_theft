package com.example.portableanti_theft.Activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.portableanti_theft.Adapter.LocationSetAdapter;
import com.example.portableanti_theft.domain.LocationSet;
import com.example.portableanti_theft.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseLocationActivity extends BaseActivity {
    @BindView(R.id.nav_home_text)
    TextView navHomeText;

    // 注意：button 的修饰类型不能是：private 或者 static 。
    // 否则会报错：错误: @BindView fields must not be private or static.
    // (com.zyj.wifi.ButterknifeActivity.button1)
    @OnClick(R.id.nav_back) //给 button1 设置一个点击事件
    public void showToast() {
        finish();
    }

    private List<LocationSet> locationSetList = new ArrayList<>();
    private String[] data = {"长沙市", "株洲市", "湘潭市", "衡阳市",
            "邵阳市", "岳阳市", "常德市", "张家界市", "益阳市",
            "娄底市", "郴州市", "永州市", "怀化市", "湘西土家族苗族自治州"};

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        initLocationSet();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.location_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //调用下面这行代码可以实现布局横向排列
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        //        和ListView不同的地方在于，ListView的布局排列是由自身去管理
//        而RecyclerView则将这个工作交给LayoutManager，它制定了一套可扩展的布局排列接口
//        子类只要按照接口的规范来实现，就能定制出不同排列方式的布局了
        recyclerView.setLayoutManager(layoutManager);
        LocationSetAdapter adapter = new LocationSetAdapter(locationSetList);
        recyclerView.setAdapter(adapter);

        //绑定activity
        ButterKnife.bind(this);
        navHomeText.setText("选择城市");
    }

    public void initLocationSet() {
        for (String dataone : data) {
            LocationSet locationSet = new LocationSet(dataone);
            locationSetList.add(locationSet);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
