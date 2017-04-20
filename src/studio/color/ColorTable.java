package studio.color;

import processing.event.MouseEvent;
import toolbox.ValueChangedListener;

public class ColorTable extends ColorView {

    int[][] colors;
    int activeColumn = 0;

    public ColorTable(int x, int y, int w, int h, ValueChangedListener<int[][]> l, String p) {
        super(x, y, w, h, l, p);
    }

    @Override
    protected int[] getPickerBounds() {
        return new int[]{xPos+10, yPos+110, width-20, 60};
    }

    @Override
    protected ColorPicker.OnColorChangeListener getColorChangeListener() {
        return this::onColorChange;
    }

    private void onColorChange(int[] rgb) {
        colors[activeColumn][0] = rgb[0];
        colors[activeColumn][1] = rgb[1];
        colors[activeColumn][2] = rgb[2];
    }

    @Override
    public void setColors(int[][] colors) {
        this.colors = colors;
    }

    @Override
    protected void showColors() {
        if (colors.length < 5) {
            int i = 0;
            for (int x = xPos; x < xPos+width; x += (width+10)/colors.length) {
                parent.fill(colors[i][0], colors[i][1], colors[i][2]);
                parent.rect(x, yPos, (width+10)/colors.length-10, (width+10)/5-10);
                i++;
            }
        } else {
            int i=0;
            int y = yPos;
            while (i < colors.length) {
                for (int x = xPos; x < xPos+width; x += (width+10)/5) {
                    if (i >= colors.length) {
                        break;
                    }
                    parent.fill(colors[i][0], colors[i][1], colors[i][2]);
                    parent.rect(x, y, (width+10)/5-10, (width+10)/5-10);
                    i++;
                }
                yPos += (width+10)/5;
            }
        }
    }

    public boolean mouseEvent(MouseEvent e) {
        boolean event = super.mouseEvent(e);
        if (hovered && wasClicked()) {
            if (colors.length < 5) {
                int i=0;
                for (int x = xPos; x < xPos+width; x += (width+10)/colors.length) {
                    if (e.getX() > x && e.getX() < x+(width+10)/colors.length-10 && e.getY() > yPos && e.getY() < yPos+(width+10)/5-10) {
                        activeColumn = i;
                        return true;
                    }
                    i++;
                }
            } else {
                int i=0;
                int y = yPos;
                while (i < colors.length) {
                    for (int x = xPos; x < xPos+width; x += (width+10)/5) {
                        if (i >= colors.length) {
                            break;
                        }
                        if (e.getX() > x && e.getX() < x+(width+10)/5-10 && e.getY() > y && e.getY() < y+(width+10)/5-10) {
                            activeColumn = i;
                            return true;
                        }
                        i++;
                    }
                    yPos += (width+10)/5;
                }
            }
        }
        return event;
    }
}
