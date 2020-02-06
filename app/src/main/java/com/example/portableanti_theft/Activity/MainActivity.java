package com.example.portableanti_theft.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.example.portableanti_theft.ActivityCollector;
import com.example.portableanti_theft.GeoHasher;
import com.example.portableanti_theft.Listenter.MyOrientationListener;
import com.example.portableanti_theft.MyApplication;
import com.example.portableanti_theft.R;
import com.example.portableanti_theft.Util.HttpUtil;
import com.example.portableanti_theft.domain.EquipmentidBean;
import com.example.portableanti_theft.domain.LoginBean;
import com.example.portableanti_theft.domain.SBBean;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.image.SmartImageView;

import org.litepal.LitePal;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private final String URLGPS = "http://129.204.201.70:8080/gpshyy/gpsl/gpsm";
    private final String URLSB = "http://129.204.201.70:8080/gpshyy/equs/seleq";
    private static final int USERSUCCESS = 1;
    private static final int USERFAILURE = 2;
    private static final int USERERROR = 3;
    private static final int GPSSUCCESS = 4;
    private static final int GPSFAILURE = 5;
    private static final int GPSERROR = 6;
    private LoginBean loginBean;
    //绑定的GPS设备数目
    private List<SBBean> GPSList = new ArrayList<SBBean>();
    //具体的GPS数据绑定
    private List<EquipmentidBean> equipmentidBeanList;
    private EquipmentidBean equipmentidBean = new EquipmentidBean();
    //查询GPS数据计时器
    private int equipmentindex = 0;
    //GPS设备实例
    private SBBean sbBean;
    //客户端连接服务端实例
    private OkHttpClient client = null;

    //设置back键按两次退出
    private long startTime = 0;
    /**
     * 带滑动侧边栏的总布局
     */
    private DrawerLayout mDrawerLayout;

    /**
     * 侧边栏菜单布局
     */
    private NavigationView navView;

    /**
     * 添加设备按钮
     */
    private Button mButtontianjiashebei;
    /**
     * 右上角定位按钮
     */
    private ImageButton imageButtondingqwei;
    /**
     * 右上角客服按钮
     */
    private ImageButton imageButtonkefu;
    /**
     * 右上角刷新按钮
     */
    private ImageButton imageButtonshuaxin;
    /**
     * 标题栏扫一扫按钮
     */
    private ImageView imageViewsaoyisao;
    /**
     * 标题栏定位文字和三角图片
     */
    private ImageView imageViewdidian;
    private TextView textViewdidian;
    /**
     * 标题栏左侧头像图片，可以进入侧边滑动栏主界面图标
     * 实现主屏幕图标可以提示
     */
    private ImageView mToolImage;
    /**
     * 侧边滑动栏主界面图标
     * 头像
     */
    private CircleImageView circleImageViewicon;
    /**
     * 侧边滑动栏主界面图标
     * 未登录按钮
     */
    private Button buttonweidenglu;
    /**
     * 侧边滑动栏主界面图标
     * 用户名
     */
    private TextView textViewUsername;

    /**
     * 侧边栏登录用户名（未登录）
     */
    private Button iconButton;
    //百度地图的总控制器
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private LocationClient mlocationclient;
    private SupportMapFragment supportMapFragment;
    private TextView positionText;
    private TextureMapView mapView;
    private MyLocationConfiguration configuration;
    private BitmapDescriptor bitmapDescriptor;
    private MyOrientationListener myOrientationListener;
    private float mLastX;
    private static final String TAG = "MainActivity";


    private StringBuffer stringBuffer = new StringBuffer();
    private List<LatLng> latLngsList;
    private List<Double> latitudeList;
    private List<Double> longitudeList;
    //上下文
    private double maxLatitude;
    private double minLatitude;
    private double maxLongitude;
    private double minLongitude;
    private double distance;
    private float level;
    private LatLng center;
    private BDLocation mbdLocation;
    /**
     * get请求返回主线程处理
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case USERSUCCESS:
                    //查询到userid下绑定的所有设备
                    for (SBBean user : GPSList) {
                        getGPS(user.getEquipmentid());
                    }
                    break;
                case USERFAILURE:

                    //没有绑定任何设备
                    Log.d(TAG, "*********************查无用户设备数据*********************");
                    break;
                case USERERROR:
                    //载入GPS数据异常
                    Toast.makeText(MainActivity.this, "用户载入异常", Toast.LENGTH_SHORT).show();
                    break;
                case GPSSUCCESS:
                    Log.d(TAG,
                            "********************执行GPSSUCCESS******************    " + equipmentindex);
                    //查询到userid下绑定的所有设备
                    equipmentindex++;
                    if (equipmentindex == GPSList.size()) {
                        equipmentindex = 0;
                        showLineMarker();
                        getPoints();
                        getMax();
                        calculateDistance();
                        getLevel();
                        setCenter();
                    }
                    break;
                case GPSFAILURE:
                    Log.d(TAG,
                            "********************执行GPSSUCCESS******************    " + equipmentindex);
                    if (equipmentidBean == null) {
                        equipmentindex++;
                    }

                    if (equipmentindex == GPSList.size()) {
                        equipmentindex = 0;
                        showLineMarker();
                        getPoints();
                        getMax();
                        calculateDistance();
                        getLevel();
                        setCenter();
                    }
                    //没有GPS数据
                    Log.d(TAG, "*********************查无GPS数据*********************");
                    break;
                case GPSERROR:
                    //载入GPS数据异常
                    Toast.makeText(MainActivity.this, "GPS数据载入异常", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要在setContentView方法之前实现
        setContentView(R.layout.activity_main);

        //标题栏加载
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SmartImageView smartImageView = (SmartImageView) findViewById(R.id.tool_image_home);
        smartImageView.setImageUrl("", R.drawable.morentouxiang0);

        //侧滑布局总布局加载
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(drawerListener);

        //初始化控件监控
        initViews();

        //侧边栏菜单监听事件处理
        initNavigationItemSelectedListener();

        Boolean isPersimmion = getPersimmions();
        if (isPersimmion) {
            //地图初始化
            initMap();
            //开启定位
            requesLocation();
            initMyLoc();
            baiduMap.getUiSettings().setCompassEnabled(true);
        }
    }

    @TargetApi(23)
    private Boolean getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionList = new ArrayList<>();
            //运行时权限申请处理
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!permissionList.isEmpty()) {
                String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
            } else {
                return true;
            }
            return false;
        }
        return true;
    }

    public void showLineMarker() {
        //构建marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark1);

        for (EquipmentidBean quipment : equipmentidBeanList) {
            double longitude = Double.parseDouble(quipment.getGpslongitude());
            double latitude = Double.parseDouble(quipment.getGpslatitude());
            LatLng latLng = new LatLng(longitude, latitude);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions overlayOptions = new MarkerOptions()
                    .icon(bitmap)
                    .animateType(MarkerOptions.MarkerAnimateType.grow)
                    .position(latLng);
            //生长动画
            //在地图上添加Marker，并显示
            baiduMap.addOverlay(overlayOptions);
            //设置Marker覆盖物的ZIndex
        }

    }

    public void getPoints() {
        latLngsList = null;
        //获取坐标点
        latLngsList = new ArrayList<LatLng>();

        //解析得到的坐标点
        for (EquipmentidBean equipment : equipmentidBeanList) {
            Log.d(TAG,
                    "point:   " + Double.parseDouble(equipment.getGpslatitude()) + "    "
                            + Double.parseDouble(equipment.getGpslongitude()));
            LatLng point = new LatLng(Double.parseDouble(equipment.getGpslatitude()),
                    Double.parseDouble(equipment.getGpslongitude()));
            latLngsList.add(point);

        }
        Log.d(TAG, "latLngsList.length:   " + latLngsList.size());

    }

    /**
     * 比较选出集合中最大经纬度
     */
    public void getMax() {
        latitudeList = new ArrayList<Double>();
        longitudeList = new ArrayList<Double>();
        for (LatLng latlng : latLngsList) {
            double latitude = latlng.latitude;
            double longitude = latlng.longitude;
            Log.d(TAG, "latitude+longitude:    " + latitude + "    " + longitude);
            latitudeList.add(latitude);
            longitudeList.add(longitude);
        }

        maxLatitude = Collections.max(latitudeList);
        minLatitude = Collections.min(latitudeList);
        maxLongitude = Collections.max(longitudeList);
        minLongitude = Collections.min(longitudeList);
        Log.d(TAG, "maxLatitude+minLatitude+maxLongitude+minLongitude" + maxLatitude + "  "
                + minLatitude + "  " + maxLongitude + "  " + minLongitude);
    }

    /**
     * 计算两个Marker之间的距离
     */
    public void calculateDistance() {
        distance = GeoHasher.GetDistance(maxLatitude, maxLongitude, minLatitude, minLongitude);
        Log.d(TAG, "distance:    " + distance);
    }

    /**
     * 根据距离判断地图级别
     */

    public void getLevel() {
        int zoom[] = {10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 1000, 2000, 25000, 50000,
                100000, 200000, 500000, 1000000, 2000000};
        Log.d(TAG, "maxLatitude==" + maxLatitude + ";minLatitude==" + minLatitude + ";" +
                "maxLongitude==" + maxLongitude + ";minLongitude==" + minLongitude);
        Log.d(TAG, "distance==" + distance);
        for (int i = 0; i < zoom.length; i++) {
            int zoomNow = zoom[i];
            if (zoomNow - distance * 1000 > 0) {
                level = 18 - i + 5;
                //设置地图显示级别为计算所得level
                Log.d(TAG, "level:   " + level);
                setCenter();
                break;
            }
        }
    }

    /**
     * 计算中心点经纬度，将其设为启动时地图中心
     */
    public void setCenter() {
        double intx = (maxLongitude + minLongitude) / 2;
        double inty = (maxLatitude + minLatitude) / 2;
        BigDecimal doublex = new BigDecimal(intx);
        BigDecimal doubley = new BigDecimal(inty);
        //取小数点后六位，四舍五入BigDecimal.ROUND_HALF_UP表示四舍五入
        // BigDecimal.ROUND_HALF_DOWN也是五舍六入
        // BigDecimal.ROUND_UP表示进位处理（就是直接加1）
        // BigDecimal.ROUND_DOWN表示直接去掉尾数。
        intx = doublex.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        inty = doubley.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        center = new LatLng(intx, inty);
        Log.d(TAG, "center==    " + center);
        MapStatusUpdate status1 = MapStatusUpdateFactory.newLatLng(center);
        baiduMap.animateMapStatus(status1);

//        baiduMap.animateMapStatus(update);
        status1 = MapStatusUpdateFactory.zoomTo(level);
        baiduMap.animateMapStatus(status1);
//        baiduMap.animateMapStatus(update);
    }

    //地图初始化
    private void initMap() {
        //地图布局，声明LocationClient类
        mlocationclient = new LocationClient(getApplicationContext());
        //注册监听函数
        mlocationclient.registerLocationListener(new MyLocationListener());

        mapView = (TextureMapView) findViewById(R.id.map_view);
        //设置logo位置,默认在左下角显示，不可以移除。使用枚举类型控制显示的位置，共支持6个显示位置(左下，中下，右下，左上，中上，右上)。
        mapView.setLogoPosition(LogoPosition.logoPostionleftBottom);
        baiduMap = mapView.getMap();
        //普通地图
        //baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //关闭指南针，此处有bug，可能百度地图SDK和百度定位SDK不是一拨人干的活
        //导致指南针在这里关闭一下，在回调监听打开就恢复使用了
        baiduMap.getUiSettings().setCompassEnabled(false);
        // 是否一直显示自己所在的位置标记
        baiduMap.setMyLocationEnabled(true);
    }

    public void initAlertDialog() {
        //创建AlertDialog实例
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        //设置标题
        dialog.setTitle("退出登录/关闭");
        //设置内容
        dialog.setMessage("是否退出本应用?");
        //是否可以用Back键关闭对话框
        dialog.setCancelable(true);
        //设置确认按钮的点击事件
        dialog.setPositiveButton("直接退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCollector.finishAll();
//                //杀掉当前进程代码，killProcess()方法
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        //设置取消按钮的点击事件
        dialog.setNegativeButton("切换账号", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginBean = null;
                Intent intent = new Intent(MainActivity.this, SignOrRegisterActivity.class);
                startActivity(intent);
            }
        });
        //配置好后用show()方法将电话框显示出来
        dialog.show();
    }

    //    初始化地图
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(1000);

        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备(GPS)；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(true);
        //设置是否需要返回海拔高度信息，
        // 可以在BDLocation.getAltitude()中得到数据，
        // GPS定位结果中默认返回，默认值Double.MIN_VALUE
        option.setIsNeedAltitude(true);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        //参数:
        //minTimeInterval - 最短定位时间间隔，单位毫秒，最小值0，开发者可以在设置希望的位置回调最短时间间隔
        //minDistance - 最短定位距离间隔，单位米，最小值0，开发者可以设置希望的位置回调距离间隔
        //locSensitivity - 定位变化敏感程
//        option.setOpenAutoNotifyMode(1000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);

        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(true);

        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(true);

        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);

        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setWifiCacheTimeOut(5 * 60 * 1000);


        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setEnableSimulateGps(false);

        //需要地址
        option.setIsNeedAddress(true);

        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setCoorType("bd09ll");

        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mlocationclient.setLocOption(option);
    }


    //得到方向传感器
    private void initMyLoc() {
        //初始化图标
//        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.fangxiang10);
        //方向传感器监听
        myOrientationListener = new MyOrientationListener(this);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                //将获取的x轴方向赋值给全局变量
                mLastX = x;
            }
        });
    }

    /**
     * //初始化定位
     */
    private void requesLocation() {
        initLocation();
        mlocationclient.start();
    }


    //定位到手机所在点
    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            Log.d(TAG, "navigateTo location.getLatitude(), location.getLongitude():    "
                    + location.getLatitude() + location.getLongitude());
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(19f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
//            supportMapFragment =
//                (SupportMapFragment) (getSupportFragmentManager()
//                        .findFragmentById(R.id.bmapView));
//        supportMapFragment.getBaiduMap().setMapStatus(update);
//
//        //设置logo在右下角
//        supportMapFragment.getMapView().setLogoPosition(LogoPosition
//                .logoPostionRightBottom);
        }
    }

    //更新地图定位
    private void updateMapTo(BDLocation location) {
        if (isFirstLocate) {
            isFirstLocate = false;
        }
        Log.d(TAG,
                "updateMapTo location.getLatitude(), location.getLongitude():    "
                        + location.getLatitude() + location.getLongitude());
        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
        baiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(19f);
        baiduMap.animateMapStatus(update);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
            ) {
                mbdLocation = bdLocation;
                if (loginBean == null) {
                    navigateTo(bdLocation);
                }
//                Log.d(TAG, "信号好得不行");
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360,bdLocation.getDirection()
                        //mLastX就是获取到的方向传感器传来的x轴数值
                        .direction(mLastX)
                        .latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                baiduMap.setMyLocationData(locData);
//            等效于以下内容
//            MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
//            locationBuilder.latitude(bdLocation.getLatitude());
//            locationBuilder.longitude(bdLocation.getLongitude());
//            locationBuilder.accuracy(bdLocation.getRadius());
//            locationBuilder.direction(bdLocation.getDirection());
//            MyLocationData locationData = locationBuilder.build();
//            baiduMap.setMyLocationData(locationData);

                //更新经纬度
                double mLatitude = bdLocation.getLatitude();
                double mLongitude = bdLocation.getLongitude();
//            Log.d(TAG, "bdLocation：" + mLatitude+"    "+mLongitude);
                //配置定位图层显示方式，使用自己的定位图标
                // LocationMode定位模式有三种：普通模式，跟随模式，罗盘模式，在这使用普通模式
                MyLocationConfiguration configuration =
                        new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                                true
                                , bitmapDescriptor);
                baiduMap.setMyLocationConfiguration(configuration);

                if (equipmentidBeanList != null && equipmentidBeanList.size() == equipmentindex) {
                    try {
                        new Thread(() -> {
                            try {
                                Thread.sleep(3000);
                                SendGPS();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            SendGPS();
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    Log.d(TAG, "诊断监听器中的经纬度：    " + bdLocation.getLatitude()
//                            + "    " + bdLocation.getLongitude());
//                    navigateTo(bdLocation);

                }

            }
        }

        @Override
        public void onLocDiagnosticMessage(int locType, int diagnosticType,
                                           String diagnosticMessage) {

            //locType - 当前定位类型
            //diagnosticType - 诊断类型（1~9）
            //diagnosticMessage - 具体的诊断信息释义
            super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage);
            Log.d(TAG,
                    "诊断信息：locType+diagnosticType+diagnosticMessage：        "
                            + locType + "    " + diagnosticType + "    " + diagnosticMessage);
        }
    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {
        mButtontianjiashebei = findViewById(R.id.button_tianjiashebei);
        imageButtondingqwei = findViewById(R.id.dingwei_image_button);
        imageButtonkefu = findViewById(R.id.kefu_image_button);
        imageButtonshuaxin = findViewById(R.id.shuaxin_image_button);
        //侧边滑动栏主界面图标，实现主屏幕图标可以提示
        mToolImage = (ImageView) findViewById(R.id.tool_image_home);
        imageViewdidian = findViewById(R.id.tool_image_didian);
        textViewdidian = findViewById(R.id.tool_text_didian);
        imageViewsaoyisao = findViewById(R.id.tool_image_tianjia);

        //加载侧边栏菜单
        navView = (NavigationView) findViewById(R.id.nav_view);
        //侧边栏头布局加载
        View headView = navView.getHeaderView(0);
        circleImageViewicon = headView.findViewById(R.id.icon_image);
        buttonweidenglu = headView.findViewById(R.id.icon_button_weidenglu);
        textViewUsername = headView.findViewById(R.id.icon_text_username);
        mButtontianjiashebei.setOnClickListener(this);
        imageButtondingqwei.setOnClickListener(this);
        imageButtonkefu.setOnClickListener(this);
        imageButtonshuaxin.setOnClickListener(this);
        mToolImage.setOnClickListener(this);
        imageViewdidian.setOnClickListener(this);
        textViewdidian.setOnClickListener(this);
        imageViewsaoyisao.setOnClickListener(this);
        //侧边栏头布局监听
        circleImageViewicon.setOnClickListener(this);
        buttonweidenglu.setOnClickListener(this);
        textViewUsername.setOnClickListener(this);

    }

    /**
     * gps设备定位信息获取
     *
     * @param gpsId
     */
    public void getGPS(final String gpsId) {
        new Thread(() -> {
            try {

                client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("equipmentid", gpsId)
                        .build();
                HttpUtil.sendOKHttpRequest(URLGPS, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //在这里进行异常情况的处理
                        Message msg = Message.obtain();
                        msg.what = GPSERROR;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //得到服务器返回的具体内容
                        //向服务器请求用户名和头像(服务器地址)
                        String responseData = response.body().string();
                        if (response.code() != 200) {
                            Message msg = Message.obtain();
                            msg.what = GPSERROR;
                            handler.sendMessage(msg);
                        } else if (!responseData.equals("null")) {
                            Log.d(TAG,
                                    "********************GPS responseData:**********************" + responseData);
                            //解析数据
                            parseJSONWithJSONObjectGPS(responseData);
                            if (equipmentidBean != null) {
                                equipmentidBeanList.add(equipmentidBean);
                                Message msg = Message.obtain();
                                msg.what = GPSSUCCESS;
                                handler.sendMessage(msg);
                            }
                        } else {
                            Message msg = Message.obtain();
                            msg.what = GPSFAILURE;
                            handler.sendMessage(msg);
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = GPSERROR;
                handler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 用户绑定的GPS设备信息获取
     *
     * @param userId
     */
    public void getUseId(final String userId) {
        new Thread(() -> {
            try {

                client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("userid", userId)
                        .build();
                HttpUtil.sendOKHttpRequest(URLSB, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //在这里进行异常情况的处理
                        Message msg = Message.obtain();
                        msg.what = USERERROR;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //得到服务器返回的具体内容
                        //向服务器请求用户名和头像(服务器地址)
                        String responseData = response.body().string();
                        if (response.code() != 200) {
                            Message msg = Message.obtain();
                            msg.what = USERERROR;
                            handler.sendMessage(msg);
                        } else if (!responseData.equals("null")) {
                            Log.d(TAG,
                                    "********************Use responseData:**********************" + responseData);
                            //解析数据
                            parseJSONWithJSONObjectUser(responseData);

                            Message msg = Message.obtain();
                            msg.what = USERSUCCESS;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = Message.obtain();
                            msg.what = USERFAILURE;
                            handler.sendMessage(msg);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = USERERROR;
                handler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * json解析Useid数据
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectUser(String jsonData) {
        Gson gson = new Gson();
        GPSList = gson.fromJson(jsonData, new TypeToken<List<SBBean>>() {
        }.getType());
        for (SBBean user : GPSList) {
            Log.d(TAG, "****************GPSList:***************" + user.toString());
        }
    }

    /**
     * json解析GPSId数据
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectGPS(String jsonData) {
        Gson gson = new Gson();
        equipmentidBean = gson.fromJson(jsonData, new TypeToken<EquipmentidBean>() {
        }.getType());
        Log.d(TAG, "****************GPS:***************" + equipmentidBean.toString());
    }

    //监听侧边栏实现.
    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            //得到contentView 实现侧滑界面出现后主界面向右平移避免侧滑界面遮住主界面
            View content = mDrawerLayout.getChildAt(0);
            int offset = (int) (drawerView.getWidth() * slideOffset);
            content.setTranslationX(offset);
//            Log.d(TAG, "***************执行onDrawerSlide****************");
            iconButton = (Button) findViewById(R.id.icon_button_weidenglu);
            //1.用户名载入处理
            if (loginBean != null) {
                iconButton.setText(loginBean.getUsername());
                iconButton.setBackgroundResource(R.drawable.shape4);
                iconButton.setEnabled(false);
                //2.进行头像加载处理
            }
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            //打开侧滑界面触发
//            Log.d(TAG, "***************执行onDrawerOpened****************");

        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            //关闭侧滑界面触发
//            Log.d(TAG, "***************执行onDrawerClosed****************");
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            //状态改变时触发
//            Log.d(TAG, "***************执行onDrawerStateChanged****************");
        }
    };

    //侧边栏菜单监听事件处理
    private void initNavigationItemSelectedListener() {
        // navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.nav_shebeiguanli:
                                Toast.makeText(MyApplication.getContext(), "this:你点击了设备管理",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_shiwuzhaohui:
                                Toast.makeText(MyApplication.getContext(), "this:你点击了失物找回",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_yijianbaojing:
                                Toast.makeText(MyApplication.getContext(), "this:你点击了一键报警",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_xiaoxitongzhi:
                                Toast.makeText(MyApplication.getContext(), "this:你点击了消息通知",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_zaixiankefu:
                                Toast.makeText(MyApplication.getContext(), "this:你点击了在线客服",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_guanyuwomen:
                                Toast.makeText(MyApplication.getContext(), "this:你点击了关于我们",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_setting:
                                Toast.makeText(MyApplication.getContext(), "this:你点击了设置",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_quit:
                                Log.d(TAG, "***************你点击了退出登录/关闭*****************");
                                initAlertDialog();
                                break;
                            default:
                        }
                        mDrawerLayout.closeDrawer(navView);
                        return true;
                    }
                });
    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_image:
                Log.d(TAG, "***********************你点击了头像***********************");
                Intent signintent = new Intent(MainActivity.this, SignOrRegisterActivity.class);
                startActivity(signintent);
                mDrawerLayout.closeDrawer(navView);
                break;
            case R.id.icon_button_weidenglu:
                Log.d(TAG, "***********************你点击了未登录***********************");
                signintent = new Intent(MainActivity.this, SignOrRegisterActivity.class);
                startActivity(signintent);
                mDrawerLayout.closeDrawer(navView);
                break;
            case R.id.icon_text_username:
                Toast.makeText(this, "this:你点击了设备数量",
                        Toast.LENGTH_SHORT).show();
                break;
            //头像
            case R.id.tool_image_home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.button_tianjiashebei:
//                Toast.makeText(this, "this:你点击了添加设备", Toast.LENGTH_SHORT).show();
                if (loginBean == null) {
                    Intent intent = new Intent(MainActivity.this, SignOrRegisterActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.dingwei_image_button:
                Log.d(TAG, "********************你点击了定位自己按钮********************");
                if (mbdLocation != null) {
                    updateMapTo(mbdLocation);
                }
                break;
            case R.id.kefu_image_button:
                Toast.makeText(this, "this:你点击了客服按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.shuaxin_image_button:
                Log.d(TAG, "********************你点击了刷新地图按钮********************");
                mapView.onResume();
                requesLocation();
                if(loginBean!=null){
                    getLevel();
                }
                break;
            case R.id.tool_image_didian:
            case R.id.tool_text_didian:
//                Toast.makeText(this, "this:你点击了定位城市", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ChooseLocationActivity.class);
                startActivity(intent);
                break;
            case R.id.tool_image_tianjia:
                Toast.makeText(this, "this:你点击了扫一扫按钮", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 通过用户信息，载入GPS设备及其设备的定位信息
     */
    private void SendGPS() {
        //载入数据库数据
        loginBean = LitePal.findLast(LoginBean.class);
        //3.执行gps数据载入
        if (loginBean != null) {
            equipmentidBeanList = new ArrayList<EquipmentidBean>();
            getUseId(loginBean.getUserid());
//                    getGPS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int results : grantResults) {
                        if (results != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requesLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SendGPS();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        supportMapFragment.onResume();
        mapView.onResume();
        mlocationclient.start();
        myOrientationListener.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        supportMapFragment.onPause();
        mapView.onPause();//这里不能加，要加都得加，不加重载死全家
        mlocationclient.stop();
        myOrientationListener.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //百度地图相关，关闭
        mapView.onDestroy();
        mlocationclient.stop();

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        supportMapFragment.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) >= 2000) {
            Toast.makeText(MainActivity.this, "再按一次返回桌面", Toast.LENGTH_SHORT).show();
            startTime = currentTime;
        } else {
//            finish();
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
    }
}