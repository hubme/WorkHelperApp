package com.king.app.workhelper.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Annotation
import com.king.app.workhelper.R
import com.king.app.workhelper.common.AppBaseActivity
import com.king.app.workhelper.common.FirebaseManager
import com.king.applib.log.Logger
import com.king.applib.util.EncryptionUtil
import kotlinx.android.synthetic.main.activity_test.*


/**
 * @author VanceKing
 * @since 2017/12/11.
 */

class TestActivity : AppBaseActivity() {

    override fun getContentLayout(): Int {
        return R.layout.activity_test
    }

    override fun initData() {

    }

    override fun initContentView() {
        super.initContentView()

        val message = "abc123456"


        btn_des.setOnClickListener {
            FirebaseManager.getInstance(this).logEvent("MyEvent")
        }

        btn_aes.setOnClickListener {
            val aesKey = "@ng!@#4)8l;Jlk9$"
            val encryptText = EncryptionUtil.encryptByAES(message, aesKey)
            val plantText = EncryptionUtil.decryptByAES(encryptText, aesKey)
            Logger.i("密文：$encryptText \n 明文：$plantText")
        }

        val algorithm = "RSA"
        val keyPair = EncryptionUtil.generateRSAKeyPair(algorithm, 2048)
        btn_rsa.setOnClickListener {

            val encryptText = EncryptionUtil.encryptByRSA(algorithm, keyPair.public, message)
            val plantText = EncryptionUtil.decryptByRSA(algorithm, keyPair.private, encryptText)
            Logger.i("密文：$encryptText \n 明文：$plantText")
        }
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
}
