package com.king.applib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;

import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;

/**
 * AppUtil测试类
 * Created by VanceKing on 2016/12/18 0018.
 */

public class AppUtilTest extends BaseTestCase {
    public void testGetActivityProcessName() throws Exception {
        Logger.i(AppUtil.getActivityProcessName(Activity.class));
    }
    
    public void testsSdfds() throws Exception {
        float density = mContext.getResources().getDisplayMetrics().density;
        int densityDpi = mContext.getResources().getDisplayMetrics().densityDpi;
        Logger.i("density: " + density + "; densityDpi: " + densityDpi);
    }
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP) 
    public void testCamera() throws Exception {
        try {
            CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
            String[] cameraIds = manager.getCameraIdList();
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraIds[0]);
            Integer integer = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            assert integer != null;
            Logger.i("results: "+integer.toString());
        } catch (CameraAccessException e) {
            //
        }
    }
}
