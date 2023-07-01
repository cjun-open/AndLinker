package com.codezjx.andlinker.adapter.rxjava3;

import com.codezjx.andlinker.CallAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;


/**
 * A {@linkplain CallAdapter.Factory call adapter} which uses RxJava 2 for creating observables.
 */
public class RxJava3CallAdapterFactory extends CallAdapter.Factory {

    private final Scheduler mScheduler;

    private RxJava3CallAdapterFactory(Scheduler scheduler) {
        mScheduler = scheduler;
    }

    public static RxJava3CallAdapterFactory create() {
        return new RxJava3CallAdapterFactory(null);
    }

    public static RxJava3CallAdapterFactory createWithScheduler(Scheduler scheduler) {
        if (scheduler == null) throw new NullPointerException("scheduler == null");
        return new RxJava3CallAdapterFactory(scheduler);
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations) {
        Class<?> rawType = getRawType(returnType);
        boolean isFlowable = (rawType == Flowable.class);
        if (rawType != Observable.class && !isFlowable) {
            return null;
        }

        if (!(returnType instanceof ParameterizedType)) {
            String name = isFlowable ? "Flowable" : "Observable";
            throw new IllegalStateException(name + " return type must be parameterized"
                    + " as " + name + "<Foo> or " + name + "<? extends Foo>");
        }
        
        return new RxJava3CallAdapter(mScheduler, isFlowable);
    }
    
}
