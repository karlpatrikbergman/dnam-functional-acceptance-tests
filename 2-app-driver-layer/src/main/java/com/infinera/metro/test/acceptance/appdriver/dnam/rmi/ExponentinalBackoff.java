package com.infinera.metro.test.acceptance.appdriver.dnam.rmi;

import com.github.rholder.retry.*;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ExponentinalBackoff {

    <T> T perform(Callable<T> callable) {
        Retryer<T> retryer = RetryerBuilder.<T>newBuilder()
            .retryIfResult(Objects::isNull)
            .retryIfException()
            .retryIfRuntimeException()
            .withWaitStrategy(WaitStrategies.exponentialWait(1000, 5, TimeUnit.SECONDS))
            .withStopStrategy(StopStrategies.stopAfterAttempt(300))
            .build();
        try {
            return retryer.call(callable);
        } catch (RetryException | ExecutionException e) {
           throw new RuntimeException("Failed to invoke " + callable.toString() + ": " + e.getMessage());
        }
    }
}
