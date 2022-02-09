package com.king.app.workhelper.activity

import android.media.MediaPlayer
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import com.king.app.workhelper.common.AppBaseActivity
import com.king.app.workhelper.databinding.ActivitySurfaceBinding
import kotlinx.android.synthetic.main.activity_surface.*

/**
 * 使用MediaPlayer+SurfaceView播放视频。也可以使用 VideoPlayer。
 *
 * SurfaceView 和 GLSurfaceView 是结合了 Surface 与 View 的实现。
 *
 * SurfaceTexture 是从 Android 3.0+ 开始提供的组件，提供了一个Surface跟GLES纹理的组合，而TextureView，则是一个SurfaceTexture跟View结合起来的组件。
 *
 * @author VanceKing
 * @since 2022/2/9
 */
class SurfaceActivity : AppBaseActivity(), SurfaceHolder.Callback {
    private lateinit var mBinding: ActivitySurfaceBinding

    private lateinit var mediaPlayer: MediaPlayer
    private val videoPath = filesDir.path + "/video.mp4"

    override fun getContentView(): View {
        mBinding = ActivitySurfaceBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun initContentView() {
        super.initContentView()
        sv_video.holder.setKeepScreenOn(true)
        sv_video.holder.addCallback(this)
        initMedia()
        btn_play.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                pause()
            } else {
                start()
            }
        }
    }

    /**
     * 初始化媒体播放
     */
    private fun initMedia() {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(videoPath)
        mediaPlayer.setOnPreparedListener {
            start()//缓冲完，播放
        }
        mediaPlayer.setOnCompletionListener {
            Toast.makeText(this, "播放完毕", Toast.LENGTH_SHORT).show()
            btn_play.text = "重新播放"
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mediaPlayer.setDisplay(holder)//设置播放的容器，将MediaPlayer和SurfaceView关联起来
        mediaPlayer.prepareAsync()//缓冲
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }

    private fun start() {
        btn_play.text = "暂停"
        mediaPlayer.start()
    }

    private fun pause() {
        btn_play.text = "播放"
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}