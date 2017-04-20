package studio.color;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.event.MouseEvent;
import toolbox.*;

import java.util.ArrayList;
import java.util.Arrays;

public class GradientBar extends ColorView {

        ArrayList<ColorMarker> marker = new ArrayList<>();
        private int activeMarker;
        private PImage gradient;
        private int[][] mcolors;
        private int[] colorLine;

        public GradientBar(int x, int y, int w, int h, ValueChangedListener<int[][]> l, String p) {
            super(x, y, w, h, l, p);
            colorLine = new int[1000];
            mcolors = new int[][]{{255,255,255,500}};
            marker.add(new ColorMarker(255, 255, 255, 0.5F));
            selectMarker(0);
        }

        protected int[] getPickerBounds() {
            return new int[]{xPos+10, yPos+110, width-20, 60};
        }
        protected ColorPicker.OnColorChangeListener getColorChangeListener() {
            return this::onColorChange;
        }

        public void init(PApplet p, Toolbox t) {
            super.init(p,t);
            if (isInitialized()) {
                redraw();
            }
        }

        void onColorChange(int[] rgb) {
            marker.get(activeMarker).onColorChange(rgb);
            redraw();
        }

        public void showColors() {
            parent.image(gradient, xPos, yPos);
            for (ColorMarker aMarker : marker) {
                aMarker.draw();
            }
        }

        void redraw() {
            updateColorArray();
            notifyListener(mcolors);
            updateColorLine();
            gradient = parent.createImage(width, 50, PConstants.RGB);
            gradient.loadPixels();
            for (int x=0; x<width; x++) {
                int pos = (int)(((float)x/width)*1000);
                for (int y=0; y<50; y++) {
                    gradient.pixels[y*width+x] = colorLine[pos];
                }
            }
            gradient.updatePixels();
        }

        private int getR(float pos) {
            return (int)parent.red(colorLine[(int)(pos*1000)]);
        }

        private int getG(float pos) {
            return (int)parent.green(colorLine[(int)(pos*1000)]);
        }

        private int getB(float pos) {
            return (int)parent.blue(colorLine[(int)(pos*1000)]);
        }

        private void updateColorArray() {
            mcolors = new int[marker.size()][4];
            for (int i=0; i<marker.size(); i++) {
                mcolors[i] = marker.get(i).toArray();
            }
            Arrays.sort(mcolors, (entry1, entry2) -> {
                final int pos1 = entry1[3];
                final int pos2 = entry2[3];
                return pos1-pos2;
            });
        }

        private void updateColorLine() {
            int r, g, b;
            if (mcolors[0][3] > 0) {
                int maxp = mcolors[0][3];
                for (int p = 0; p < maxp; p++) {
                    colorLine[p] = parent.color(mcolors[0][0], mcolors[0][1], mcolors[0][2]);
                }
            }
            for (int i=0; i<mcolors.length-1; i++) {
                int minp = mcolors[i][3];
                int maxp = mcolors[i+1][3];
                for (int p = minp; p < maxp; p++) {
                    r = (int) PApplet.map(p, minp, maxp, mcolors[i][0], mcolors[i+1][0]);
                    g = (int) PApplet.map(p, minp, maxp, mcolors[i][1], mcolors[i+1][1]);
                    b = (int) PApplet.map(p, minp, maxp, mcolors[i][2], mcolors[i+1][2]);
                    colorLine[p] = parent.color(r, g, b);
                }
            }
            if (mcolors[mcolors.length-1][3] < 1000) {
                int minp = mcolors[mcolors.length-1][3];
                for (int p = minp; p < 1000; p++) {
                    colorLine[p] = parent.color(mcolors[mcolors.length-1][0], mcolors[mcolors.length-1][1], mcolors[mcolors.length-1][2]);
                }
            }
        }

        private void selectMarker(int i) {
            activeMarker = i;
            picker.setColor(marker.get(i).getColors());
        }

        public boolean mouseEvent(MouseEvent e) {
            boolean event = super.mouseEvent(e);
            boolean markerpressed = marker.get(activeMarker).mouseEvent(e);
            if (!markerpressed) {
                for (int i = 0; i < marker.size(); i++) {
                    boolean mevent = marker.get(i).mouseEvent(e);
                    event = mevent | event;
                    if (e.getAction() == MouseEvent.PRESS && marker.get(i).pressed) {
                        markerpressed = true;
                        selectMarker(i);
                        break;
                    }
                }
            } else {
                if (marker.get(activeMarker).wasReleased()) {
                    redraw();
                }
            }
            if (wasClicked() && !markerpressed) {
                float p = (float)(e.getX()-xPos)/width;
                if (p < 0) { p = 0; }
                if (p > 1) { p = 1; }
                marker.add(new ColorMarker(getR(p), getG(p), getB(p), p));
                selectMarker(marker.size()-1);
            }
            return event;
        }

        @Override
        protected boolean mouseOver(int x, int y) {
            return (x > xPos-10 && x < xPos+width+10 && y > yPos && y < yPos+height+20);
        }

        public void setColors(int[][] colors) {
            marker.clear();
            for (int[] color : colors) {
                marker.add(new ColorMarker(color, (float) color[3] / 1000));
            }
            if (parent != null) {
                redraw();
            }
            selectMarker(0);
        }

        public class ColorMarker {

            private int r, g, b;
            private float pos;
            private boolean hovered;
            private boolean pressed;
            private boolean released;

            ColorMarker(int r, int g, int b, float pos) {
                this.r = r;
                this.g = g;
                this.b = b;
                this.pos = pos;
            }

            ColorMarker(int[] rgb, float pos) {
                this(rgb[0], rgb[1], rgb[2], pos);
            }

            public void draw() {
                int sx = 5;
                int sy = 10;
                if (hovered) {
                    sx = 6;
                    sy = 12;
                }
                parent.stroke(255);
                parent.fill(255);
                parent.beginShape();
                float x = xPos+pos*width;
                float y = yPos+height;
                parent.vertex(x, y);
                parent.vertex(x+sx, y+sx);
                parent.vertex(x+sx, y+sy);
                parent.vertex(x-sx, y+sy);
                parent.vertex(x-sx, y+sx);
                parent.vertex(x, y);
                parent.endShape(PApplet.CLOSE);
            }

            public boolean mouseEvent(MouseEvent e) {
                hovered = mouseOver(e.getX(), e.getY());
                if (hovered && e.getAction() == MouseEvent.PRESS) {
                    pressed = true;
                    return true;
                } else if (pressed && e.getAction() == MouseEvent.DRAG) {
                    hovered = true;
                    pos = (float)(e.getX()-xPos)/width;
                    if (pos < 0) { pos = 0; }
                    if (pos > 1) { pos = 1; }
                    return true;
                } else if (e.getAction() == MouseEvent.RELEASE && pressed) {
                    pressed = false;
                    hovered = false;
                    released = true;
                    if (e.getButton() == PApplet.RIGHT && marker.size() > 1) {
                        if (activeMarker == marker.indexOf(this)) {
                            activeMarker = 0;
                        }
                        marker.remove(this);
                        redraw();
                    }
                    return true;
                }
                return hovered;
            }

            public boolean mouseOver(int x, int y) {
                float p = (float)(x-xPos)/width;
                if (p < 0) { p = 0; }
                if (p > 1) { p = 1; }
                return (y > yPos+height && y < yPos+height+20 && p > pos - 0.04 && p < pos + 0.04);
            }

            int[] toArray() {
                return new int[]{r, g, b, (int)(pos*1000)};
            }

            void onColorChange(int[] rgb) {
                this.r = rgb[0];
                this.g = rgb[1];
                this.b = rgb[2];
            }

            int[] getColors() {
                return new int[]{r,g,b};
            }

            boolean wasReleased() {
                if (released) {
                    released = false;
                    return true;
                } else {
                    return false;
                }
            }
        }

}
