package com.hezeyi.privatechat.activity.chat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.xhab.chatui.utils.ScreenShotListenManager;
import com.xhab.utils.inteface.OnDataCallBack;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by dab on 2021/3/24 14:43
 */
public class ChatStatusListener {
    private Context mContext;


    public ChatStatusListener(Context context) {
        mContext = context;
        screenShotListenManager = new ScreenShotListenManager(context);

    }

    public void onResume() {

        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
        if (blueadapter != null) {
            boolean enabled = blueadapter.isEnabled();
            if (enabled && onBluetoothListener != null) {
                String connectedBtDevice = getConnectedBtDevice();
                String t = "的蓝牙处于打开状态!";
                if (!TextUtils.isEmpty(connectedBtDevice)) {
                    t = t + "连接的设备名称:"+connectedBtDevice;
                }
                onBluetoothListener.onCallBack(t);
            }
        }
        startScreenShotListen();
        // 初始化广播
        IntentFilter intentFilter = new IntentFilter();
        // 监视蓝牙关闭和打开的状态
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播
        mContext.registerReceiver(mReceiver, intentFilter);
    }

    public void onPause() {
        stopScreenShotListen();
        mContext.unregisterReceiver(mReceiver);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //截图检测
    private ScreenShotListenManager screenShotListenManager;
    private boolean isHasScreenShotListener = false;
    private ScreenShotListenManager.OnScreenShotListener onScreenShotListener;

    public void setOnScreenShotListener(ScreenShotListenManager.OnScreenShotListener onScreenShotListener) {
        this.onScreenShotListener = onScreenShotListener;
    }

    /**
     * 监听
     */
    private void startScreenShotListen() {
        if (!isHasScreenShotListener && screenShotListenManager != null) {
//            this.screenShotListenManager.setListener(imagePath -> {
//
//                //得到二次编辑的图片
////                this.screenShotListenManager.createScreenShotBitmap(LoginActivity.this, imagePath);
//            });
            this.screenShotListenManager.setListener(onScreenShotListener);
            this.screenShotListenManager.startListen();
            isHasScreenShotListener = true;
        }
    }

    /**
     * 停止监听
     */
    public void stopScreenShotListen() {
        if (isHasScreenShotListener && screenShotListenManager != null) {
            screenShotListenManager.stopListen();
            isHasScreenShotListener = false;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private OnDataCallBack<String> onBluetoothListener;

    public void setOnBluetoothListener(OnDataCallBack<String> onBluetoothListener) {
        this.onBluetoothListener = onBluetoothListener;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            switch (blueState) {
                case BluetoothAdapter.STATE_TURNING_ON:
//                    Toast.makeText(context, "蓝牙正在打开", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.STATE_ON:
                    if (onBluetoothListener != null) {
                        onBluetoothListener.onCallBack("的蓝牙已经打开了!");
                    }
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
//                    Toast.makeText(context, "蓝牙正在关闭", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.STATE_OFF:
//                    Toast.makeText(context, "蓝牙已经关闭", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //获取已连接的蓝牙设备
    private String getConnectedBtDevice() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {
            //得到连接状态的方法
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(adapter, (Object[]) null);
            if (state == BluetoothAdapter.STATE_CONNECTED) {
                Log.i("BLUETOOTH", "BluetoothAdapter.STATE_CONNECTED");
                Set<BluetoothDevice> devices = adapter.getBondedDevices(); //集合里面包括已绑定的设备和已连接的设备
                Log.i("BLUETOOTH", "devices:" + devices.size());
                for (BluetoothDevice device : devices) {
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                    if (isConnected) { //根据状态来区分是已连接的还是已绑定的，isConnected为true表示是已连接状态。
                        Log.i("BLUETOOTH-dh", "connected:" + device.getName());
                        return device.getName();
                        //deviceList.add(device);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
