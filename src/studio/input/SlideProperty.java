package studio.input;

import studio.InputProperty;
import toolbox.ValueChangedListener;
import toolbox.Watchable;

public class SlideProperty extends InputProperty<Float> {

    float max, min, step;

    private SlideProperty(String lb, float init, float min, float max, float step, ValueChangedListener<Float> l, Watchable<Float> w) {
        super(lb, init, l, w);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public SlideProperty(String lb, float init, float min, float max, float step) {
        super(lb, init);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    public float getStep() {
        return step;
    }

    @Override
    public SlideProperty clone(ValueChangedListener<Float> l) {
        return new SlideProperty(label, value, min, max, step, l, watchable);
    }
}
