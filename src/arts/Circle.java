package arts;

import processing.core.PApplet;
import processing.core.PGraphics;
import studio.input.SlideProperty;

public class Circle extends SimpleDrawer {

    @Override
    protected void initInputs() {
        addInputProperty(new SlideProperty("Radius", 20, 10, 300, 1));

    }

    @Override
    protected void draw(PGraphics gr) {
        float r = (float)getInputValue("Radius");
        int[] color = getColorValue("Foreground")[0];
        gr.stroke(color[0], color[1], color[2]);
        gr.noFill();
        gr.ellipseMode(PApplet.CENTER);
        gr.ellipse(0, 0, r, r);
    }

    @Override
    public ArtObject clone() {
        return clone(new Circle());
    }
}
