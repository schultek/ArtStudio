package studio.color;

import toolbox.ValueChangedListener;

public class ColorProperty {

    public interface ColorControl {
        ColorView createColorView(String label, int[][] init, String view, String picker, ValueChangedListener<int[][]> listener);
    }

    private int[][] colors;
    private ColorView view;
    private ValueChangedListener<int[][]> listener;

    public ColorProperty(int[][] col, ValueChangedListener<int[][]> l, ColorControl c, String label, String cview, String picker) {
        colors = col;
        listener = l;
        view = c.createColorView(label, col, cview, picker, colorListener());
    }

    private ColorProperty(int[][] col, ValueChangedListener<int[][]> l, ColorView v) {
        colors = col;
        view = v;
        listener = l;
    }

    private ValueChangedListener<int[][]> colorListener() {
        return col -> {colors = col; listener.onValueChanged(col);};
    }

    public void resetColorListener() {
        view.setListener(colorListener());
        view.setColors(colors);
    }

    public int[][] getValue() {
        return colors;
    }

    public ColorProperty clone(ValueChangedListener<int[][]> l) {
        return new ColorProperty(colors, l, view);
    }


}
