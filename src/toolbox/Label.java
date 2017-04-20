package toolbox;

import processing.core.PConstants;

public class Label extends GUIObject {

    String text;
    int textsize;
    int col;

    public Label(String t, int x, int y, int w, int h) {
        super(x, y, w, h);
        col = 255;
        setText(t);
    }

    public void setText(String t) {
        text = t;
        textsize = Math.min(height/2, width/text.length());
    }

    public void setColor(int c) {
        col = c;
    }

    @Override
    public void draw() {
        parent.noStroke();
        parent.fill(col);
        parent.textSize(textsize);
        parent.textAlign(PConstants.LEFT, PConstants.TOP);
        parent.text(text, xPos, yPos);
    }
}
