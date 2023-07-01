package com.codezjx.andlinker.adapter.rxjava3;

import com.codezjx.andlinker.Call;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;


/**
 * Created by codezjx on 2017/10/14.<br/>
 */
final class CallExecuteObservable<T> extends Observable<T> {

    private final Call<T> mCall;

    CallExecuteObservable(Call<T> call) {
        mCall = call;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        observer.onSubscribe(new CallDisposable(mCall));
        boolean terminated = false;
        try {
            T response = mCall.execute();
            if (!mCall.isCanceled()) {
                observer.onNext(response);
            }
            if (!mCall.isCanceled()) {
                terminated = true;
                observer.onComplete();
            }
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            if (terminated) {
                RxJavaPlugins.onError(t);
            } else if (!mCall.isCanceled()) {
                try {
                    observer.onError(t);
                } catch (Throwable inner) {
                    Exceptions.throwIfFatal(inner);
                    RxJavaPlugins.onError(new CompositeException(t, inner));
                }
            }
        }
    }

    private static final class CallDisposable implements Disposable {
        private final Call<?> mCall;

        CallDisposable(Call<?> call) {
            this.mCall = call;
        }

        @Override public void dispose() {
            mCall.cancel();
        }

        @Override public boolean isDisposed() {
            return mCall.isCanceled();
        }
    }
}