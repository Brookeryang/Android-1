package com.maqiang.doctor.rxadapter.rxcompat;

import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Joker on 2015/8/10.
 */
public class SchedulersCompat {

  private static final ObservableTransformer computationTransformer =
      new ObservableTransformer() {
        @Override
        public ObservableSource apply(io.reactivex.Observable upstream) {
          return upstream.subscribeOn(Schedulers.computation())
                  .unsubscribeOn(Schedulers.computation())
                  .observeOn(AndroidSchedulers.mainThread());
        }
      };

  private static final ObservableTransformer ioTransformer = new ObservableTransformer() {
    @Override
    public ObservableSource apply(io.reactivex.Observable upstream) {
      return upstream.subscribeOn(Schedulers.io())
              .unsubscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread());
    }
  };

  private static final ObservableTransformer newTransformer = new ObservableTransformer() {
    @Override
    public ObservableSource apply(io.reactivex.Observable upstream) {
      return upstream.subscribeOn(Schedulers.newThread())
              .unsubscribeOn(Schedulers.newThread())
              .observeOn(AndroidSchedulers.mainThread());
    }
  };

  private static final ObservableTransformer trampolineTransformer = new ObservableTransformer() {
    @Override
    public ObservableSource apply(io.reactivex.Observable upstream) {
      return upstream.subscribeOn(Schedulers.trampoline())
              .unsubscribeOn(Schedulers.trampoline())
              .observeOn(AndroidSchedulers.mainThread());
    }
  };

  private static final ObservableTransformer executorTransformer = new ObservableTransformer() {
    @Override
    public ObservableSource apply(io.reactivex.Observable upstream) {
      return upstream.subscribeOn(Schedulers.from(HttpExecutors.eventExecutor))
              .unsubscribeOn(Schedulers.from(HttpExecutors.eventExecutor))
              .observeOn(AndroidSchedulers.mainThread());
    }
  };

  /**
   * Don't break the chain: use RxJava's compose() operator
   */
  public static <T> ObservableTransformer<T, T> applyComputationSchedulers() {

    return (ObservableTransformer<T, T>) computationTransformer;
  }

  public static <T> ObservableTransformer<T, T> applyIoSchedulers() {

    return (ObservableTransformer<T, T>) ioTransformer;
  }

  public static <T> ObservableTransformer<T, T> applyNewSchedulers() {

    return (ObservableTransformer<T, T>) newTransformer;
  }

  public static <T> ObservableTransformer<T, T> applyTrampolineSchedulers() {

    return (ObservableTransformer<T, T>) trampolineTransformer;
  }

  public static <T> ObservableTransformer<T, T> applyExecutorSchedulers() {

    return (ObservableTransformer<T, T>) executorTransformer;
  }
}
