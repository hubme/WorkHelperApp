package com.king.app.workhelper.fragment;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.applib.log.Logger;

import java.util.concurrent.TimeUnit;

import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 *
 * @author VanceKing
 * @since 2017/10/6
 */
public class RxRetryFragment extends AppBaseFragment {

    @Override protected int getContentLayout() {
        return R.layout.fragment_retry;
    }

    @OnClick(R.id.tv_retry)
    public void onRetryClick() {
        Observable.error(new RuntimeException("test"))
                .retryWhen(new RetryWithDelay(5, 1000))
                .doOnSubscribe(disposable -> Logger.i("Attempting the impossible 5 times in intervals of 1s"))
//                .subscribe(o -> Logger.i("accept"))//没有处理onError,会crash.
                .subscribe(o -> Logger.i("accept"), throwable -> Logger.i("onError"), () -> Logger.i("onComplete"));
    }

    //重试策略，重试的次数*重试时间
    private class RetryWithDelay implements Function<Observable<Throwable>, ObservableSource<?>> {
        private final int mMaxRetries;
        private final int mRetryDelayMills;
        private int mRetryCount = 0;

        public RetryWithDelay(int maxRetries, int retryDelayMills) {
            this.mMaxRetries = maxRetries;
            this.mRetryDelayMills = retryDelayMills;
        }

        @Override public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {
            return throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
                if (++mRetryCount < mMaxRetries) {
                    Logger.i("Retrying in %d ms", mRetryCount * mRetryDelayMills);
                    return Observable.timer(mRetryCount * mRetryDelayMills, TimeUnit.MILLISECONDS);
                } else {
                    Logger.i("Error, give up!");
                    return Observable.error(throwable);
                }
            });
        }
    }
}
