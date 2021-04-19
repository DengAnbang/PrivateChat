package com.xhab.utils.utils;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dab on 2018/3/29 10:add_video
 */

public class RxUtils {
    public static Disposable runOnUiThread(final Runnable runnable) {
        return Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(integer -> runnable.run(), Throwable::printStackTrace);
    }

    public static Disposable runOnIoThread(final Runnable runnable) {
        return Observable.just(1).observeOn(Schedulers.io()).subscribe(integer -> runnable.run(), Throwable::printStackTrace);
    }

    public static Disposable interval(int period, final Runnable runnable) {
        return Observable.interval(period, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(integer -> runnable.run(), Throwable::printStackTrace);
    }
}
