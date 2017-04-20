package arts;

import processing.core.PApplet;
import processing.core.PGraphics;
import studio.ArtRenderer;
import studio.input.CheckProperty;
import studio.GraphicsRenderer;
import studio.input.SlideProperty;
import toolbox.ValueChangedListener;

public class Polygon extends ArtObject {

    public void init() {
        addInputProperty(new SlideProperty("Vertex Count", 5, 3, 20, 1));
        addInputProperty(new SlideProperty("Radius", 200, 20, 800, 1));
        addInputProperty(new CheckProperty("Gefüllt", false));
        addColorProperty("Vertexes", new int[][]{{255,255,255,500}}, "Gradient", "Slider");
        addColorProperty("Background", new int[][]{{10,10,10,500}}, "Gradient", "Slider");
        addRenderer(new GraphicsRenderer(width, height, getRenderListener(1), getDrawer(0), applet));
    }

    private void drawi(PGraphics gr) {
        int[] background = getColorValue("Background")[0];
        gr.fill(background[0], background[1], background[2]);
        gr.rect(0, 0, width, height);
        gr.translate(width/2, height/2);
        int[] color = getColorValue("Vertexes")[0];
        float angle = PApplet.TWO_PI / (float)getInputValue("Vertex Count");
        float radius = (float)getInputValue("Radius");
        if ((boolean)getInputValue("Gefüllt")) {
            gr.fill(color[0], color[1], color[2]);
        } else {
            gr.noFill();
        }
        gr.stroke(color[0], color[1], color[2]);
        gr.beginShape();
        for (float a = 0; a < PApplet.TWO_PI; a += angle) {
            float sx = PApplet.cos(a) * radius;
            float sy = PApplet.sin(a) * radius;
            gr.vertex(sx, sy);
        }
        gr.endShape(PApplet.CLOSE);
        gr.translate(-width/2, -height/2);
    }


    @Override
    protected ValueChangedListener getInputListener(String key) {
        return val -> render();
    }

    @Override
    protected ValueChangedListener<int[][]> getColorListener(String key) {
        return color -> render();
    }

    @Override
    protected ArtRenderer.RenderingFinishedListener getRenderListener(int i) {
        return (result) -> {renderedImage = result; updateThumbnail();};
    }

    @Override
    protected GraphicsRenderer.GraphicsDrawer getDrawer(int i) {
        return this::drawi;
    }

    @Override
    public ArtObject clone() {
        return clone(new Polygon());
    }
}
