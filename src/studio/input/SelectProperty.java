package studio.input;

import studio.InputProperty;
import toolbox.ValueChangedListener;
import toolbox.Watchable;

public class SelectProperty extends InputProperty<Integer> {

        String[] options;

        private SelectProperty(String lb, int init, String[] opt, ValueChangedListener<Integer> l, Watchable<Integer> w) {
            super(lb, init, l, w);
            options = opt;
        }

        public SelectProperty(String lb, int init, String[] opt) {
            super(lb, init);
            options = opt;
        }

    public String[] getOptions() {
        return options;
    }

    @Override
        public SelectProperty clone(ValueChangedListener<Integer> l) {
            return new SelectProperty(label, value, options, l, watchable);
        }




}
