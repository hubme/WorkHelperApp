package com.king.app.workhelper.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.UUID;

/**
 * https://www.jianshu.com/p/ac24239c1daf
 * <p>
 * android手机必须系统版本4.3及以上才支持BLE API。
 *
 * @author VanceKing
 * @since 21-10-11.
 */
public class Bluetooth {
    private static final String TAG = "aaa";
    private static final boolean LOG_ENABLE = true;

    private final BluetoothAdapter mBluetoothAdapter;
    private final MyAdvertiseCallback mAdvertiseCallback;
    private final BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private final BluetoothManager mBluetoothManager;
    private final Context mContext;

    public Bluetooth(Context context, String name) {
        mContext = context;
        mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = getBluetooth(name);
        //获取BLE广播的操作对象。
        //如果蓝牙关闭或此设备不支持蓝牙LE广播，则返回null。
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        if (mBluetoothLeAdvertiser != null) {
            mAdvertiseCallback = new MyAdvertiseCallback();
        } else {
            mAdvertiseCallback = null;
        }

    }

    private static class MyAdvertiseCallback extends AdvertiseCallback {
        //开启广播成功回调
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.d("daqi", "开启服务成功");
        }

        //无法启动广播回调。
        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.d("daqi", "开启服务失败，失败码 = " + errorCode);
        }
    }

    public BluetoothAdapter getBluetooth(String name) {
        //获取蓝牙设配器
        //BluetoothAdapter.getDefaultAdapter()
        BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();
        //设置设备蓝牙名称
        mBluetoothAdapter.setName(name);
        return mBluetoothAdapter;
    }

    public void startAdvertising(Context context) {
        if (!mBluetoothAdapter.isEnabled()) {
            logMsg("手机蓝牙未开启");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(intent);
            return;
        }
        if (mBluetoothLeAdvertiser == null) {
            logMsg("该手机不支持 BLE 广播");
            return;
        }

        //初始化广播包
        AdvertiseData mAdvertiseData = new AdvertiseData.Builder()
                //设置广播设备名称
                .setIncludeDeviceName(true)
                //设置发射功率级别
                .setIncludeTxPowerLevel(true).build();

        //初始化扫描响应包
        AdvertiseData mScanResponseData = new AdvertiseData.Builder()
                //隐藏广播设备名称
                .setIncludeDeviceName(false)
                //隐藏发射功率级别
                .setIncludeDeviceName(false)
                //设置广播的服务 UUID
                .addServiceUuid(new ParcelUuid(null))
                //设置厂商数据
                .addManufacturerData(0x11, new byte[1024]).build();


        AdvertiseSettings advertiseSettings = new AdvertiseSettings.Builder()
                //设置广播模式，以控制广播的功率和延迟
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
                //发射功率级别
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                //不得超过180000毫秒。值为0将禁用时间限制。
                .setTimeout(3_000)
                //设置是否可以连接
                .setConnectable(false).build();
        //开启广播
        mBluetoothLeAdvertiser.startAdvertising(advertiseSettings, mAdvertiseData,
                mScanResponseData, mAdvertiseCallback);

    }

    public void stopAdvertising() {
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
        }
    }

    public BluetoothGattService createGattService(UUID serviceUuid, UUID characteristicUuid,
                                                  UUID descriptorUuid) {
        BluetoothGattService mGattService = new BluetoothGattService(serviceUuid,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        //初始化特征值，特征属性表示该BluetoothGattCharacteristic拥有什么功能
        BluetoothGattCharacteristic mGattCharacteristic = new BluetoothGattCharacteristic(
                characteristicUuid,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_WRITE | BluetoothGattCharacteristic.PERMISSION_READ);

        //初始化描述
        BluetoothGattDescriptor mGattDescriptor = new BluetoothGattDescriptor(descriptorUuid,
                BluetoothGattDescriptor.PERMISSION_WRITE);

        //Service添加特征值
        mGattService.addCharacteristic(mGattCharacteristic);
        //特征值添加描述
        mGattCharacteristic.addDescriptor(mGattDescriptor);

        return mGattService;
    }

    private boolean addGattService(BluetoothGattService service) {
        BluetoothGattServer mBluetoothGattServer = mBluetoothManager.openGattServer(mContext,
                new BluetoothGattServerCallback() {
                    @Override
                    public void onConnectionStateChange(BluetoothDevice device, int status,
                                                        int newState) {
                        super.onConnectionStateChange(device, status, newState);
                        logMsg("设备连接/断开连接回调");
                    }

                    @Override
                    public void onServiceAdded(int status, BluetoothGattService service) {
                        super.onServiceAdded(status, service);
                        logMsg("添加本地服务回调");
                    }

                    @Override
                    public void onCharacteristicReadRequest(BluetoothDevice device, int requestId,
                                                            int offset,
                                                            BluetoothGattCharacteristic characteristic) {
                        super.onCharacteristicReadRequest(device, requestId, offset,
                                characteristic);
                        logMsg("特征值读取回调");
                    }

                    @Override
                    public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId,
                                                             BluetoothGattCharacteristic characteristic,
                                                             boolean preparedWrite,
                                                             boolean responseNeeded, int offset,
                                                             byte[] value) {
                        super.onCharacteristicWriteRequest(device, requestId, characteristic,
                                preparedWrite, responseNeeded, offset, value);
                        logMsg("特征值写入回调");
                    }

                    @Override
                    public void onDescriptorReadRequest(BluetoothDevice device, int requestId,
                                                        int offset,
                                                        BluetoothGattDescriptor descriptor) {
                        super.onDescriptorReadRequest(device, requestId, offset, descriptor);
                        logMsg("描述读取回调");
                    }

                    @Override
                    public void onDescriptorWriteRequest(BluetoothDevice device, int requestId,
                                                         BluetoothGattDescriptor descriptor,
                                                         boolean preparedWrite,
                                                         boolean responseNeeded, int offset,
                                                         byte[] value) {
                        super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite,
                                responseNeeded, offset, value);
                        logMsg("描述写入回调");
                    }
                });
        boolean result = mBluetoothGattServer.addService(service);
        if (result) {
            logMsg("添加服务成功");
        } else {
            logMsg("添加服务失败");
        }
        return result;
    }

    private static void logMsg(String message) {
        if (LOG_ENABLE) {
            Log.i(TAG, message);
        }
    }
}
