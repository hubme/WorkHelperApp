package com.king.app.workhelper.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.king.applib.log.Logger;

/**
 * JobService 使用.
 * 总结：
 * 1）先继承JobService，并重写startJob和stopJob
 * 2）在manifest.xml中声明JobService的时候，记得一定要加上 android:permission=”android.permission.BIND_JOB_SERVICE”
 * 3）后台任务不能执行耗时任务，如果一定要这么做，一定要再起一个线程去做，使用 thread/handler/AsyncTask都可以。
 * 4）JobService一定要设置至少一个执行条件，如有网络连接、充电中、系统空闲...
 * 5）任务执行完后记得调用jobFinish通知系统释放相关资源
 * 如果我们的后台任务满足JobService的一个或多个约束条件，就可以考虑是不是应该用JobService来执行。
 *
 * @author huoguangxu
 * @since 2017/1/22.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {
    @Override public boolean onStartJob(JobParameters params) {
        Logger.i("MyJobService#onStartJob.JobId: " + params.getJobId());
        Toast.makeText(MyJobService.this, "start job:" + params.getJobId(), Toast.LENGTH_SHORT).show();
        jobFinished(params, false);//任务执行完后记得调用jobFinsih通知系统释放相关资源  
        return false;
    }

    @Override public boolean onStopJob(JobParameters params) {
        Logger.i("MyJobService#onStopJob");
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startJobService() {
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, componentName);

        builder.setMinimumLatency(2000);//设置JobService执行的最小延时时间
        builder.setOverrideDeadline(3000);//设置JobService执行的最晚时间
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);//设置执行的网络条件
        builder.setRequiresDeviceIdle(false);//是否要求设备为idle状态
        builder.setRequiresCharging(false);//是否要设备为充电状态
        scheduler.schedule(builder.build());
    }
}
