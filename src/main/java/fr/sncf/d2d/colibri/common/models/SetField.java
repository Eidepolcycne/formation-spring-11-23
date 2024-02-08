package fr.sncf.d2d.colibri.common.models;

import java.util.function.Consumer;

public class SetField<T> {
    
    private final T value;

    private final boolean mustUpdate;

    private SetField(T value, boolean mustUpdate){
        this.value = value;
        this.mustUpdate = mustUpdate;
    }

    public void ifMustUpdate(ThrowingConsumer<T> updateConsumer) throws Exception {
        if (!this.mustUpdate){
            return;
        }
        updateConsumer.accept(value);
    }

    public static <T> SetField<T> noop(){
        return new SetField<T>(null, false);
    }

    public static <T> SetField<T> withValue(T value){
        if (value == null){
            throw new NullPointerException();
        }
        return new SetField<T>(value, true);
    }

    public static <T> SetField<T> withNull(){
        return new SetField<T>(null, true);
    }

    public static <T> SetField<T> withNullable(T value){
        return value == null ? withNull() : withValue(value);
    }

    @FunctionalInterface
    public static interface ThrowingConsumer<T> {
        void accept(T value) throws Exception;
    }
}
