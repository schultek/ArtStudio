package studio;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import studio.color.ColorProperty;
import studio.color.ColorView;
import studio.color.GradientBar;
import toolbox.GUIObject;
import toolbox.Label;
import toolbox.Toolbox;

import java.util.ArrayList;

public class ColorManager extends GUIObject {

    private ArrayList<ColorView> objects;
    private ArrayList<Label> labels;
    private int yStack = 20;

    ColorManager(int x, int y, int w, int h) {
        super(x, y, w, h);
        objects = new ArrayList<>();
        labels = new ArrayList<>();
    }

    @Override
    public void init(PApplet p, Toolbox t) {
        super.init(p, t);
        for (int i=0; i<objects.size(); i++) {
            objects.get(i).init(p, t);
            labels.get(i).init(p, t);
        }
    }

    public void add(ColorView g, String name) {
        Label label = new Label(name, g.getX(), g.getY(), g.getWidth(), 25);
        g.setY(g.getY()+30);
        objects.add(g);
        labels.add(label);
        if (parent != null) {
            g.init(parent, toolbox);
            label.init(parent, toolbox);
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
            if (isInitialized() && !element.isInitialized()) {
                element.init(parent, toolbox);
            }
            if (element instanceof Label) {
                labels.add((Label) element);
            } else {
                objects.add((ColorView) element);
            }
        }
    }

    ColorProperty.ColorControl getColorControl() {
        return (label, init, view, picker, listener) -> {
            GradientBar composer = new GradientBar(20, getY(200), width-40, 50, listener, picker);
            composer.setColors(init);
            add(composer, label);
            return composer;
        };
    }

    @Override
    public void draw() {
        for (Label label : labels) {
            label.draw();
        }
        for (ColorView object : objects) {
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
        for (ColorView object : objects) {
            if (object.mouseEvent(e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void keyEvent(KeyEvent e) {
        for (ColorView object : objects) {
            object.keyEvent(e);
        }
    }
}
