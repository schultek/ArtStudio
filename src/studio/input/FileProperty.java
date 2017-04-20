package studio.input;

import studio.InputProperty;
import toolbox.ValueChangedListener;
import toolbox.Watchable;

import java.io.File;

public class FileProperty extends InputProperty<File> {

    private String[] suffix;
    private String prompt;

    private FileProperty(String lb, File init, ValueChangedListener<File> l, Watchable<File> w) {
        super(lb, init, l, w);
    }

    public FileProperty(String lb, String prompt, String suffix) {
        super(lb, null);
        this.prompt = prompt;
        this.suffix = new String[]{suffix};
    }

    public FileProperty(String lb, String prompt, String[] suffix) {
        super(lb, null);
        this.prompt = prompt;
        this.suffix = suffix;
    }

    @Override
    public FileProperty clone(ValueChangedListener<File> l) {
        return new FileProperty(label, value, l, watchable);
    }

    public String getPrompt() {
        return prompt;
    }

    public String[] getSuffix() {
        return suffix;
    }
}