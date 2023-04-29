package com.king.app.workhelper.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.os.Build
import android.os.UserHandle
import android.text.Annotation
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.king.app.workhelper.common.AppBaseActivity
import com.king.app.workhelper.databinding.ActivityTestBinding
import com.king.applib.log.Logger
import com.king.applib.util.DateTimeUtil
import com.king.applib.util.getUid


/**
 * @author VanceKing
 * @since 2017/12/11.
 */

class TestActivity : AppBaseActivity() {

    private lateinit var mBinding: ActivityTestBinding

    override fun getContentView(): View {
        mBinding = ActivityTestBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun initData() {

    }

    override fun initContentView() {
        super.initContentView()

        mBinding.tvText.text = "哈哈哈"

        mBinding.tvText.setOnClickListener {
            testSystemProperties()
        }

        mBinding.tvHehehe.setOnClickListener {
            test(1)
        }
    }

    private fun test(number: Int) {
        Log.i(TAG, "test: start")
        /*while (number == 1) {

        }*/

        Thread.sleep(5_000)
        Log.i(TAG, "test: end")
    }

    private fun testAnr() {
        Log.i(TAG, "testAnr: " + System.currentTimeMillis())
        val dateTime = DateTimeUtil.getCurrentTime(DateTimeUtil.I18N_DATE_FORMAT)
        Log.i(TAG, "testAnr: $dateTime")

        Thread.sleep(10_000)
        Toast.makeText(this, "哈哈哈", Toast.LENGTH_SHORT).show()
    }

    private fun testSystemProperties() {
        val clazz = Class.forName("android.os.SystemProperties");
        val mGetMethod = clazz.getDeclaredMethod("get", String::class.java)
        val mGetIntMethod =
            clazz.getDeclaredMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
    }

    @SuppressLint("SoonBlockedPrivateApi")
    private fun testLauncherApps() {
        val launcherApps = getSystemService(Context.LAUNCHER_APPS_SERVICE)
        /*val field = LauncherApps::class.java.getDeclaredMethod(
            "getAppUsageLimit",
            String::class.java,
            UserHandle::class.java
        )
        val uid = getUid(packageName)
        Log.i("aaa", "uid: $uid")
        val userHandler = UserHandle.getUserHandleForUid(uid)
        Log.i("aaa", "userHandler: $userHandler")
        val usageLimit = field.invoke(launcherApps, packageName, userHandler)
        Log.i("aaa", "usageLimit: $usageLimit")*/

        val mService = LauncherApps::class.java.getDeclaredField("mService")
        Log.i("aaa", "mService: $mService")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun testUserHandler() {
        val uid = getUid(packageName)//12a9b8d
        Log.i("aaa", "UID: $uid")
        val userHandleForUid = UserHandle.getUserHandleForUid(uid)
        Log.i("aaa", "UserHandle: $userHandleForUid")
        val declaredMethod = UserHandle::class.java.getDeclaredMethod("getIdentifier")
        val invoke = declaredMethod.invoke(userHandleForUid)
        Log.i("aaa", "invoke: $invoke")
    }


    //由于用户操作，应用进入后台时(home 键)调用，在 onPause() 之前执行
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Logger.i("onUserLeaveHint")
    }

    //reified 关键字来表示该泛型要进行实化
    inline fun <reified T : Activity> openActivity() {
        startActivity(Intent(this, T::class.java))
    }

    /**
     * Get the resource identifier for an annotation.
     * @param context The context this should be executed in.
     */
    private fun Annotation.getResId(context: Context): Int {
        return context.resources.getIdentifier(value, null, context.packageName)
    }

    // 在Activity中获取调用者包名。https://blog.csdn.net/zhangjg_blog/article/details/103280429
    private fun reflectGetReferrer(): String? {
        try {
            val referrerField = Activity::class.java.getDeclaredField("mReferrer")
            referrerField.isAccessible = true
            return referrerField[this] as String
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return ""
    }

    @Override
    fun getBasePackageName(): String {
        return "fake_package"
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun showBadge() {
        val badgeDrawable = BadgeDrawable.create(this).apply {
            number = 1898980
            maxCharacterCount = 5
            backgroundColor =
                ContextCompat.getColor(this@TestActivity, android.R.color.holo_red_dark)
        }
        // BadgeUtils 是实验性 API
        BadgeUtils.attachBadgeDrawable(badgeDrawable, mBinding.tvHehehe)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}
