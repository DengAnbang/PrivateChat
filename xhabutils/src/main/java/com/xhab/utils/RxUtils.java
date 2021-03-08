package com.xhab.utils;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dab on 2018/3/29 10:add_video
 */

public class RxUtils {
    public static void runOnUiThread(final Runnable runnable) {
        Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(integer -> runnable.run(), Throwable::printStackTrace);
    }

    public static void runOnIoThread(final Runnable runnable) {
        Observable.just(1).observeOn(Schedulers.io()).subscribe(integer -> runnable.run(), Throwable::printStackTrace);
    }
}
