package studio.input;

import studio.InputProperty;
import toolbox.ValueChangedListener;
import toolbox.Watchable;

public class CheckProperty extends InputProperty<Boolean> {

        private CheckProperty(String lb, boolean init, ValueChangedListener<Boolean> l, Watchable<Boolean> w) {
            super(lb, init, l, w);
        }

        public CheckProperty(String lb, boolean init) {
            super(lb, init);
        }

        @Override
        public CheckProperty clone(ValueChangedListener<Boolean> l) {
            return new CheckProperty(label, value, l, watchable);
        }

}
