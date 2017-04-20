package studio.color;

import processing.core.PApplet;
import processing.event.MouseEvent;
import toolbox.GUIObject;
import toolbox.Toolbox;
import toolbox.ValueChangedListener;

public abstract class ColorView extends GUIObject {

    protected ColorPicker picker;
    private ValueChangedListener<int[][]> listener;

    public ColorView(int x, int y, int w, int h, ValueChangedListener<int[][]> l, String p) {
        super(x, y, w, h);
        switch (p) {
            case "Slider":
                picker = new RGBSliders(getPickerBounds(), getColorChangeListener()); break;
            case "Field":
            default:
                picker = new HSBField(getPickerBounds(), getColorChangeListener()); break;
        }
        listener = l;
    }

    protected abstract int[] getPickerBounds();
    protected abstract ColorPicker.OnColorChangeListener getColorChangeListener();

    @Override
    public void init(PApplet p, Toolbox t) {
        super.init(p, t);
        picker.init(p, t);
    }

    public void draw() {
        showColors();
        picker.draw();
    }

    public abstract void setColors(int[][] colors);

    protected abstract void showColors();

    protected void notifyListener(int[][] colors) {
        listener.onValueChanged(colors);
    }

    public void setListener(ValueChangedListener<int[][]> listener) {
        this.listener = listener;
    }

    @Override
    public boolean mouseEvent(MouseEvent e) {
        super.mouseEvent(e);
        return picker.mouseEvent(e);
    }
}
