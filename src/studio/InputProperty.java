package studio;

import toolbox.Checkbox;
import toolbox.Slider;
import toolbox.ValueChangedListener;
import toolbox.Watchable;

public abstract class InputProperty<T> {


    public interface InputControl<I extends InputProperty<T>, T> {
        Watchable<T> createInputControl(String label, I property);
    }

    protected T value;
    protected String label;
    protected ValueChangedListener<T> listener;
    protected Watchable<T> watchable;

    protected InputProperty(String lb, T init, ValueChangedListener<T> l, Watchable<T> w) {
        label = lb;
        value = init;
        listener = l;
        watchable = w;
        w.setListener(watchableListener());
    }

    public InputProperty(String lb, T init) {
        label = lb;
        value = init;
    }

    public <I extends InputProperty<T>> void init(ValueChangedListener l, InputControl<I, T> control) {
        listener = l;
        watchable = control.createInputControl(label, (I)this);
        watchable.setListener(watchableListener());
    }

    private ValueChangedListener<T> watchableListener() {
        return val -> {value = val; listener.onValueChanged(val);};
    }

    public void resetWatchableListener() {
        watchable.setListener(watchableListener());
        watchable.setValue(value);
    }

    public T getValue() {
        return value;
    }


    public String getLabel() {
        return label;
    }

    public abstract InputProperty<T> clone(ValueChangedListener<T> l);


}
