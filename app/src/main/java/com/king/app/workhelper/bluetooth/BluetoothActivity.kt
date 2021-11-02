package com.king.app.workhelper.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.king.app.workhelper.common.AppBaseActivity
import com.king.app.workhelper.databinding.ActivityBluetoothBinding
import com.king.applib.log.Logger


class BluetoothActivity : AppBaseActivity() {
    private lateinit var mBinding: ActivityBluetoothBinding

    private lateinit var mActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                Logger.i(it.toString())
            }
    }

    override fun getContentView(): View {
        mBinding = ActivityBluetoothBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun initContentView() {
        super.initContentView()

        mBinding.tvOpenBluetooth.setOnClickListener {
            val mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
            if (mBluetoothManager.adapter == null || !mBluetoothManager.adapter.isEnabled) {
                mActivityResultLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).apply {
                    putExtra("aaa", "bbb")
                })
            } else {
                mBluetoothManager.adapter
            }
        }
    }
}