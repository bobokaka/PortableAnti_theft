package com.example.portableanti_theft.Listenter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;

/**
 * 方向传感器监听
 * 定义一个了类实现SensorEventListener
 * 重写onSensorChanged()和onAccuracyChanged()方法
 * 其中onAccuracyChanged()监听精度改变不需要
 * 只要通过onSensorChanged()监听x轴方向改变就满足需要
 */
public class MyOrientationListener implements SensorEventListener {
    //传感器管理者
    private SensorManager mSensorManager;
    //上下文
    private Context mContext;
    //加速度传感器
    private Sensor mAccelerometer;
    //磁场传感器
    private Sensor mMagneticField;
    private TriggerEventListener mListener;
    //方向传感器有三个坐标，现在只关注X
    private float mLastX;
    private OnOrientationListener onOrientationListener;
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];

    //构造函数
    public MyOrientationListener(Context context) {
        this.mContext = context;
    }

    //开始监听
    @SuppressWarnings("deprecation")
    public void start() {
        //获得传感器管理者
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
//        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        if (mSensorManager != null) {//是否支持
            //加速度传感器
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            //磁场传感器
            mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
        if (mAccelerometer != null && mMagneticField != null) {
            mSensorManager.registerListener(this,
                    mAccelerometer, Sensor.TYPE_ACCELEROMETER);

            mSensorManager.registerListener(this, mMagneticField,
                    Sensor.TYPE_MAGNETIC_FIELD);
            calculateOrientation();
        }
    }

    // 计算方向
    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticFieldValues);
        SensorManager.getOrientation(R, values);
        values[0] = (float) Math.toDegrees(values[0]);
        //只获取x的值
        float x = values[0];
        //为了防止经常性的更新
//        if (Math.abs(x - mLastX) > 1.0) {
            if (onOrientationListener != null) {
                onOrientationListener.onOrientationChanged(x);
            }
//        }
        //x取值为-180-180，但是我们需要的mLastX值为顺时针的0-360.所以需要加法处理
        mLastX = x+180;
    }

    //结束监听
    public void stop() {
        //取消注册的方向传感器
        mSensorManager.unregisterListener(this);
    }

    //传感器发生改变时
    @SuppressWarnings("deprecation")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldValues = event.values;
        }
        calculateOrientation();
    }


    //当传感器精度发生改变，当前不用
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }



    public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
        this.onOrientationListener = onOrientationListener;
    }

    //回调方法
    public interface OnOrientationListener {
        void onOrientationChanged(float x);
    }
}