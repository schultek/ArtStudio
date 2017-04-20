package studio.color;

import processing.core.PApplet;
import processing.core.PGraphics;
import toolbox.GUIObject;
import toolbox.Toolbox;

public abstract class ColorPicker extends GUIObject {

    public interface OnColorChangeListener {
        void onChange(int[] rgb);
    }

    int r, g, b;


    private static PGraphics gr;
    private static boolean hsb = true;

    private OnColorChangeListener onColorChangeListener;

    ColorPicker(int[] dimen, OnColorChangeListener l) {
        super(dimen[0], dimen[1], dimen[2], dimen[3]);
        onColorChangeListener = l;
        gr = new PGraphics();
        gr.colorMode(PApplet.HSB, 255, 255, 255);
        hsb = true;
    }

    @Override
    public void init(PApplet p, Toolbox t) {
        super.init(p, t);
    }

    public void setColor(int[] rgb) {
        this.r = rgb[0];
        this.g = rgb[1];
        this.b = rgb[2];
        updateGUI();
    }

    protected abstract void updateGUI();

    public void setR(int r) {
        this.r = r;
        onColorChangeListener.onChange(new int[]{r,g,b});
    }

    void setG(int g) {
        this.g = g;
        onColorChangeListener.onChange(new int[]{r,g,b});
    }

    public void setB(int b) {
        this.b = b;
        onColorChangeListener.onChange(new int[]{r,g,b});
    }

    public static int hsb(float h, float s, float b) {
        if (!hsb) {
            gr.colorMode(PApplet.HSB, 255, 255, 255);
            hsb = true;
        }
        return gr.color(h, s, b);
    }

    public static int rgb(float r, float g, float b) {
        if (hsb) {
            gr.colorMode(PApplet.RGB, 255, 255, 255);
            hsb = false;
        }
        return gr.color(r, g, b);
    }

}
