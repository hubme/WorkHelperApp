package com.king.app.workhelper.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.log.Logger;

import java.util.Set;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Logger.i("ACTION_FOUND，device had paired: " + device.getName() + " : " + device.getAddress());
                } else {
                    Logger.i("ACTION_FOUND: " + device.getName() + " : " + device.getAddress());
                } 
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Logger.i("ACTION_DISCOVERY_FINISHED");
            }
        }
    };

    @Override
    protected void initData() {
        this.registerReceiver(mFindBlueToothReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        this.registerReceiver(mFindBlueToothReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mFindBlueToothReceiver);
    }

    @OnClick(R.id.tv_device)
    public void onViewClick(TextView textView) {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 130);
        } else {
            printBondedDevices();
        } 
    }

    @OnClick(R.id.tv_scan)
    public void onScanBluetooth(TextView textView) {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 130 && resultCode == RESULT_OK) {
            printBondedDevices();
        }
    }

    //获取配对过的蓝牙设备
    private void printBondedDevices() {
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : bondedDevices) {
            Log.i("aaa", device.getName() + " : " + device.getAddress());
        }
    }
}
