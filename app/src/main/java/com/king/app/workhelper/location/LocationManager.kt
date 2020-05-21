package com.king.app.workhelper.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat

fun isProviderEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

@RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
private fun getLastKnownLocation(context: Context): Location? {
    if (checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
        return null
    }
    // 为获取地理位置信息时设置查询条件 是按GPS定位还是network定位
    val bestProvider: String? = getProvider(context)
    if (bestProvider.isNullOrBlank()) {
        return null
    }
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.getLastKnownLocation(bestProvider)
}

private fun checkPermission(context: Context, permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
}

/**
 * 定位查询条件
 * 返回查询条件 ，获取目前设备状态下，最适合的定位方式
 */
private fun getProvider(context: Context): String? {
    SomeSingleton.getInstance(context)

    // 构建位置查询条件
    val criteria = Criteria()
    // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
    //Criteria.ACCURACY_FINE,当使用该值时，在建筑物当中，可能定位不了,建议在对定位要求并不是很高的时候用Criteria.ACCURACY_COARSE，避免定位失败
    // 查询精度：高
    criteria.accuracy = Criteria.ACCURACY_FINE
    // 设置是否要求速度
    criteria.isSpeedRequired = false
    // 是否查询海拨：否
    criteria.isAltitudeRequired = false
    // 是否查询方位角 : 否
    criteria.isBearingRequired = false
    // 是否允许付费：是
    criteria.isCostAllowed = false
    // 电量要求：低
    criteria.powerRequirement = Criteria.POWER_LOW
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    // 返回最合适的符合条件的provider，第2个参数为true说明 , 如果只有一个provider是有效的,则返回当前provider
    return locationManager.getBestProvider(criteria, true)
}