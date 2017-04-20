package studio;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import studio.input.*;
import toolbox.*;

import java.util.ArrayList;

public class InputManager extends GUIObject {

    private ArrayList<GUIObject> objects;
    private ArrayList<Label> labels;
    private int yStack = 20;

    InputManager(int x, int y, int w, int h) {
        super(x, y, w, h);
        objects = new ArrayList<>();
        labels = new ArrayList<>();
    }

    @Override
    public void init(PApplet p, Toolbox t) {
        super.init(p, t);
        for (GUIObject object : objects) {
            object.init(p, t);
        }
        for (Label label : labels) {
            label.init(p, t);
        }
    }

    public void add(GUIObject o, String name) {
        try {
            Watchable w = ((Watchable)o);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("GUIObject o must implement Watchable Interface!");
        }
        Label label = new Label(name, o.getX(), o.getY(), o.getWidth(), 25);
        o.setY(o.getY()+30);
        objects.add(o);
        labels.add(label);
        if (parent != null) {
            o.init(parent, toolbox);
            label.init(parent, toolbox);
        }
    }

    public void add(GUIObject o) {
        try {
            Watchable w = ((Watchable)o);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("GUIObject o must implement Watchable Interface!");
        }
        objects.add(o);
        if (parent != null) {
            o.init(parent, toolbox);
        }
    }

    ArrayList<GUIObject> clearAll() {
        ArrayList<GUIObject> elements = new ArrayList<>();
        while (objects.size() > 0) {
            elements.add(objects.remove(0));
        }
        while (labels.size() > 0) {
            elements.add(labels.remove(0));
        }
        yStack = 20;
        return elements;
    }

    void setElements(ArrayList<GUIObject> elements) {
        objects.clear();
        labels.clear();
        for (GUIObject element : elements) {
            if (!element.isInitialized()) {
                element.init(parent, toolbox);
            }
            if (element instanceof Label) {
                labels.add((Label) element);
            } else {
                objects.add(element);
            }
        }
    }

    InputProperty.InputControl getInputControl() {
        return (label, property) -> {
            if (property instanceof SlideProperty) {
                SlideProperty prop = (SlideProperty)property;
                Slider slider = new Slider(20, getY(50), width - 40);
                slider.initValues(prop.getValue(), (prop.getMax() - prop.getMin()) / 100, prop.getMin(), prop.getMax());
                slider.setStep(prop.getStep());
                slider.setColor(255, 255);
                add(slider, label);
                return slider;
            } else if (property instanceof CheckProperty){
                Checkbox check = new Checkbox(label, 20, getY(30), 20, 20, ((CheckProperty)property).getValue());
                check.setColor(255,40);
                add(check);
                return check;
            } else if (property instanceof SelectProperty) {
                String[] options = ((SelectProperty)property).getOptions();
                CheckboxGroup group = new CheckboxGroup();
                for (int i=0; i<options.length; i++) {
                    Checkbox c = new Checkbox(options[i], 20, getY(30), 20, 20, i==((SelectProperty) property).getValue()?true:false);
                    c.setStyle(Checkbox.ROUND, 255,40);
                    group.add(c);
                }
                add(group);
                return group;
            } else if (property instanceof FileProperty) {
                FilePicker picker = new FilePicker(((FileProperty)property).getPrompt(), ((FileProperty) property).getSuffix(), 20, getY(50), 80, 30);
                picker.setColor(255, 40);
                add(picker);
                return picker;
            } else {
                return null;
            }
        };
    }

    @Override
    public void draw() {

        for (Label label : labels) {
            label.draw();
        }
        for (GUIObject object : objects) {
            object.draw();
        }
    }

    private int getY(int dy) {
        int y = yStack;
        yStack += dy;
        return y;
    }

    @Override
    public boolean mouseEvent(MouseEvent e) {
        for (GUIObject object : objects) {
            if (object.mouseEvent(e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void keyEvent(KeyEvent e) {
        for (GUIObject object : objects) {
            object.keyEvent(e);
        }
    }
}
