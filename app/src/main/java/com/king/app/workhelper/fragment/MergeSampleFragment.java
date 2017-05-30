package com.king.app.workhelper.fragment;

import com.king.app.workhelper.R;
import com.king.app.workhelper.api.GitHubService;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.model.entity.Contributor;
import com.king.applib.log.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Merge Operator
 * see: http://reactivex.io/documentation/operators/concat.html
 *
 * @author VanceKing
 * @since 2017/3/20.
 */
/*
concat()操作符只有需要数据的时候才会订阅所有的Observable数据源，而且按照一个接一个(sequentially)的顺序被检索的,所以后一个需要等待。
concat()和merge()区别：concat的数据源never interleave(交叉)，而merge的数据源maybe interleave.
 */
public class MergeSampleFragment extends AppBaseFragment {
    @Override protected int getContentLayout() {
        return R.layout.fragment_merge_sample;
    }

    @OnClick(R.id.tv_concat)
    public void onConcatClick() {
        Observable.concat(getCachedDiskData(), getFreshNetworkData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(4)
                .subscribe(new DisposableObserver<Contributor>() {
                    @Override public void onNext(Contributor contributor) {
                        Logger.i(contributor.login + "---" + contributor.contributions);
                    }

                    @Override public void onError(Throwable e) {
                        Logger.i("something went wrong");
                    }

                    @Override public void onComplete() {
                        Logger.i("done loading all data");
                    }
                });
    }

    @OnClick(R.id.tv_concatEager)
    public void onConcatEagerClick() {
        List<Observable<Contributor>> observables = Arrays.asList(getCachedDiskData(), getFreshNetworkData());
        Observable.concatEager(observables)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(4)
                .subscribe(new DisposableObserver<Contributor>() {
                    @Override public void onNext(Contributor contributor) {
                        Logger.i(contributor.login + "---" + contributor.contributions);
                    }

                    @Override public void onError(Throwable e) {
                        Logger.i("arr something went wrong" + e.toString());
                    }

                    @Override public void onComplete() {
                        Logger.i("done loading all data");
                    }
                });
    }

    @OnClick(R.id.tv_merge)
    public void onMergeClick() {
        List<Observable<Contributor>> observables = Arrays.asList(getCachedDiskData(), getFreshNetworkData());
        Observable.merge(observables)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(4)
                .subscribe(new DisposableObserver<Contributor>() {
                    @Override public void onNext(Contributor contributor) {
                        Logger.i(contributor.login + "---" + contributor.contributions);
                    }

                    @Override public void onError(Throwable e) {
                        Logger.i("arr something went wrong" + e.toString());
                    }

                    @Override public void onComplete() {
                        Logger.i("done loading all data");
                    }
                });
    }

    //从缓存获取数据
    private Observable<Contributor> getCachedDiskData() {
        List<Contributor> contributors = new ArrayList<>();
        final Map<String, Long> diskData = dummyDiskData();
        for (Map.Entry<String, Long> username : diskData.entrySet()) {
            contributors.add(new Contributor(username.getKey(), username.getValue()));
        }

        return Observable.fromIterable(contributors).doOnSubscribe(new Consumer<Disposable>() {
            @Override public void accept(@NonNull Disposable disposable) throws Exception {
                Logger.i("(disk) cache subscribed");
            }
        }).doOnComplete(new Action() {
            @Override public void run() throws Exception {
                Logger.i("(disk) cache completed");
            }
        });
    }

    //从缓存获取数据,模拟耗时.getFreshNetworkData()和getSlowCachedDiskData()会出现数据交叉的情况
    private Observable<Contributor> getSlowCachedDiskData() {
        return Observable.timer(1, TimeUnit.SECONDS).flatMap(new Function<Long, ObservableSource<Contributor>>() {
            @Override public ObservableSource<Contributor> apply(@NonNull Long aLong) throws Exception {
                return getCachedDiskData();
            }
        });
    }

    //从网络获取数据
    private Observable<Contributor> getFreshNetworkData() {
        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com")
                .build();
        GitHubService gitHubService = retrofit.create(GitHubService.class);

        return gitHubService.contributors("square", "retrofit").flatMap(new Function<List<Contributor>, ObservableSource<Contributor>>() {
            @Override public ObservableSource<Contributor> apply(@NonNull List<Contributor> contributors) throws Exception {
                return Observable.fromIterable(contributors);
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override public void accept(@NonNull Disposable disposable) throws Exception {
                Logger.i("(network) subscribed");
            }
        }).doOnComplete(new Action() {
            @Override public void run() throws Exception {
                Logger.i("(network) completed");
            }
        });
    }

    //假数据
    private Map<String, Long> dummyDiskData() {
        Map<String, Long> map = new HashMap<>();
        map.put("JakeWharton", 0L);
        map.put("bruceLee", 0L);
        return map;
    }
}
