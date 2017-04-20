package studio.input;

import processing.core.PApplet;
import processing.event.MouseEvent;
import toolbox.Button;
import toolbox.ValueChangedListener;
import toolbox.Watchable;

import java.io.File;
import java.util.Arrays;

public class FilePicker extends Button implements Watchable<File>{

    private ValueChangedListener<File> listener;

    private File file;
    private String status = "No File selected";
    private boolean wrongFormat = false;
    private String[] suffix = {""};

    public FilePicker(String prompt, int x, int y, int w, int h) {
        super(prompt, x, y, w, h);
    }

    public FilePicker(String prompt, String suffix, int x, int y, int w, int h) {
        super(prompt, x, y, w, h);
        this.suffix = new String[]{suffix.toLowerCase()};
    }

    public FilePicker(String prompt, String[] suffix, int x, int y, int w, int h) {
        super(prompt, x, y, w, h);
        for (int i=0; i<suffix.length; i++) {
            suffix[i] = suffix[i].toLowerCase();
        }
        this.suffix = suffix;
    }

    @Override
    public void draw() {
        super.draw();
        parent.textAlign(PApplet.LEFT, PApplet.CENTER);
        if (wrongFormat) {
            parent.fill(255,0,0);
        } else {
            parent.fill(col);
        }
        parent.textSize(10);
        parent.text(status, xPos+width+10, yPos, 120, height);
    }

    @Override
    public boolean mouseEvent(MouseEvent e) {
        boolean m = super.mouseEvent(e);
        if (wasClicked()) {
            parent.selectInput("Select File", "fileSelected");
        }
        return m;
    }

    public void fileSelected(File file) {
        this.file = file;
        checkFormat();
        if (!wrongFormat) {
            notifyListener(listener, file);
        }
    }

    @Override
    public void setListener(ValueChangedListener<File> l) {
        listener = l;
    }

    @Override
    public void setValue(File value) {
        file = value;
        checkFormat();
    }

    public void checkFormat() {
        if (file == null) {
            status = "No file";
            wrongFormat = false;
            return;
        }
        wrongFormat = true;
        for (String s : suffix) {
            if (file.getAbsolutePath().endsWith(s)) {
                wrongFormat = false;
            }
        }
        if (!wrongFormat) {
            status = "File: " + file.getName();
        } else {
            status = "Wrong format! Choose from " + Arrays.toString(suffix) + "!";
        }
    }

}
