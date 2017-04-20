package arts;

import processing.core.PApplet;
import processing.core.PGraphics;
import studio.input.SlideProperty;

public class RecursiveRect extends SimpleDrawer {

    @Override
    protected void initInputs() {
        addInputProperty(new SlideProperty("Size", 100, 10, 600, 1));
        addInputProperty(new SlideProperty("Count", 2, 1, 50, 1));
        addInputProperty(new SlideProperty("Abstand", 20, 1, 50, 1));
    }

    @Override
    protected void draw(PGraphics gr) {
        gr.strokeWeight(1);
        gr.noFill();
        gr.rectMode(PApplet.CENTER);
        float n = (float)getInputValue("Count");
        float s = (float)getInputValue("Size");
        float a = (float)getInputValue("Abstand");
        int[][] colors = getColorList((int)n, getColorValue("Foreground"));
        for (int i=0; i<n; i++) {
            gr.stroke(colors[i][0], colors[i][1], colors[i][2]);
            gr.rect(0,0,s+(i*a),s+(i*a));
        }
        gr.rectMode(PApplet.CORNER);
    }


    @Override
    public ArtObject clone() {
        return clone(new RecursiveRect());
    }
}
