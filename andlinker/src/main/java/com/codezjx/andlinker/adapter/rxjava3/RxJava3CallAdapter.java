package com.codezjx.andlinker.adapter.rxjava3;

import com.codezjx.andlinker.Call;
import com.codezjx.andlinker.CallAdapter;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Scheduler;


/**
 * Created by codezjx on 2017/10/14.<br/>
 */
final class RxJava3CallAdapter<R> implements CallAdapter<R, Object> {

    private final Scheduler mScheduler;
    private final boolean mIsFlowable;

    RxJava3CallAdapter(Scheduler scheduler, boolean isFlowable) {
        mScheduler = scheduler;
        mIsFlowable = isFlowable;
    }

    @Override
    public Object adapt(Call<R> call) {
        Observable<R> observable = new CallExecuteObservable<>(call);

        if (mScheduler != null) {
            observable = observable.subscribeOn(mScheduler);
        }

        if (mIsFlowable) {
            return observable.toFlowable(BackpressureStrategy.LATEST);
        }
        
        return observable;
    }

}