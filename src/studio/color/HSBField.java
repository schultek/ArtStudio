package studio.color;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;
import studio.PixelRenderer;
import toolbox.Toolbox;

public class HSBField extends ColorPicker {

    int thirdDimen = 0;
    int h, s, br;

    PImage mainField, sideField;
    PixelRenderer mainRenderer, sideRenderer;


    HSBField(int[] d, OnColorChangeListener l) {
        super(d, l);
    }

    @Override
    public void init(PApplet p, Toolbox t) {
        super.init(p, t);
        mainRenderer = new PixelRenderer(width - 30, height, this::makeMain, result -> mainField = result, parent);
        mainRenderer.start();
        sideRenderer = new PixelRenderer(20, height, this::makeSide, result -> sideField = result, parent);
        sideRenderer.start();
        updateGUI();
    }

    @Override
    protected void updateGUI() {
        if (isInitialized()) {
            int rgb = parent.color(r, g, b);
            h = (int) parent.hue(rgb);
            s = (int) parent.saturation(rgb);
            br = (int) parent.brightness(rgb);
            mainRenderer.render();
        }
    }

    @Override
    public void draw() {
      parent.image(mainField, xPos, yPos);
      parent.image(sideField, xPos+width-20, yPos);
      parent.stroke(255);
      parent.strokeWeight(4);
      parent.point(xPos+parent.map(s, 0, 255, 0, mainField.width), yPos+parent.map(br, 255, 0, 0, mainField.height));
      parent.strokeWeight(2);
      parent.line(xPos+width-20, yPos+parent.map(h, 0, 255, 0, height), xPos+width, yPos+parent.map(h, 0, 255, 0, height));
    }

    @Override
    public boolean mouseEvent(MouseEvent e) {
        super.mouseEvent(e);
        if (hovered && (e.getAction() == MouseEvent.PRESS || e.getAction() == MouseEvent.DRAG)) {
            if (e.getX() < xPos+width-30) {
                s = (int)parent.map(e.getX(), xPos, xPos+width-30, 0, 255);
                br = (int)parent.map(e.getY(), yPos, yPos+height, 255, 0);
            } else if (e.getX() > xPos+width-20) {
                h = (int)parent.map(e.getY(), yPos, yPos+height, 0, 255);
            }
            int c = hsb(h, s, br);
            setR((int)parent.red(c));
            setG((int)parent.green(c));
            setB((int)parent.blue(c));
            mainRenderer.render();
            return true;
        }
        return false;
    }

    public void makeMain(int[] pixels, int w, int hg) {
        for (int x=0; x<w; x++) {
            for (int y=0; y<hg; y++) {
                pixels[y*w+x] = hsb(h, parent.map(x, 0, w, 0, 255), parent.map(y, 0, hg, 255, 0));
            }
        }
    }

    public void makeSide(int[] pixels, int w, int hg) {
        for (int y = 0; y<hg; y++) {
            int c = hsb(parent.map(y, 0, hg, 0, 255), 255, 255);
            for (int x=0; x<w; x++) {
                pixels[y*w+x] = c;
            }
        }
    }
}
