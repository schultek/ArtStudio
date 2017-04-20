package toolbox;

public interface Watchable<T> {
    void setListener(ValueChangedListener<T> l);
    default void notifyListener(ValueChangedListener<T> listener, T val) {
        if (listener != null) {
            listener.onValueChanged(val);
        }
    }
    void setValue(T value);
}
