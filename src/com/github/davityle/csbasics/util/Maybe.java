package com.github.davityle.csbasics.util;


import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Maybe<T> {

    private static final Maybe<?> EMPTY = new Maybe<>();

    private final Optional<T> option;

    private Maybe() {
        this(Optional.empty());
    }

    private Maybe(Optional<T> option) {
        this.option = option;
    }

    public static<T> Maybe<T> empty() {
        @SuppressWarnings("unchecked")
        Maybe<T> t = (Maybe<T>) EMPTY;
        return t;
    }

    public static <T> Maybe<T> of(T value) {
        return new Maybe<>(Optional.of(value));
    }

    public static <T> Maybe<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    public T get() {
        return option.get();
    }

    public boolean isPresent() {
        return option.isPresent();
    }

    public Maybe<T> ifPresent(Consumer<? super T> consumer) {
        option.ifPresent(consumer);
        return this;
    }

    public Maybe<T> ifNotPresent(Runnable runnable) {
        if(!isPresent())
            runnable.run();
        return this;
    }

    public Maybe<T> filter(Predicate<? super T> predicate) {
        return new Maybe<>(option.filter(predicate));
    }

    public<U> Maybe<U> map(Function<? super T, ? extends U> mapper) {
        return new Maybe<>(option.map(mapper));
    }

    public<U> Maybe<U> flatMap(Function<? super T, Optional<U>> mapper) {
        return new Maybe<>(option.flatMap(mapper));
    }

    public T orElse(T other) {
        return option.orElse(other);
    }

    public T orElseGet(Supplier<? extends T> other) {
        return option.orElseGet(other);
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return option.orElseThrow(exceptionSupplier);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Maybe)) {
            return false;
        }
        Maybe<?> other = (Maybe<?>) obj;
        return option.equals(other.option);
    }

    @Override
    public int hashCode() {
        return option.hashCode();
    }

    @Override
    public String toString() {
        return option.toString();
    }
}
