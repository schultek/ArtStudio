package studio.color;

import processing.core.PApplet;
import processing.event.MouseEvent;
import toolbox.Slider;
import toolbox.Toolbox;

public class RGBSliders extends ColorPicker {

    private Slider slR, slG, slB;

    RGBSliders(int[] d, OnColorChangeListener l) {
        super(d, l);
        int x = d[0], y = d[1], w = d[2], h = d[3];
        slR = new Slider(x, y, w);
        slG = new Slider(x, y+(h/2), w);
        slB = new Slider(x, y+h, w);
        slR.setRange(0, 255);
        slG.setRange(0, 255);
        slB.setRange(0, 255);
    }

    @Override
    protected void updateGUI() {
        slR.setValue(r);
        slG.setValue(g);
        slB.setValue(b);
    }

    @Override
    public void init(PApplet p, Toolbox t) {
        super.init(p, t);
        slR.init(p, t);
        slG.init(p, t);
        slB.init(p, t);
        slR.setColor(p.color(255,0,0), 255);
        slG.setColor(p.color(0,255,0), 255);
        slB.setColor(p.color(0,0,255), 255);
    }

    @Override
    public void draw() {
        slR.draw();
        slG.draw();
        slB.draw();
        if (slR.valueChanged()) setR((int)slR.getValue());
        if (slG.valueChanged()) setG((int)slG.getValue());
        if (slB.valueChanged()) setB((int)slB.getValue());
    }

    @Override
    public boolean mouseEvent(MouseEvent e) {
        return slR.mouseEvent(e) || slG.mouseEvent(e) || slB.mouseEvent(e);
    }
}
