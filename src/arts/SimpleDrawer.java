package arts;

import processing.core.PGraphics;
import processing.core.PImage;
import studio.ArtRenderer;
import studio.GradientRenderer;
import studio.GraphicsRenderer;
import toolbox.ValueChangedListener;

public abstract class SimpleDrawer extends ArtObject {

    private PImage background;

    @Override
    public void init() {
        initInputs();
        addColorProperty("Foreground", new int[][]{{255,255,255,500}}, "Gradient", "Slider");
        addColorProperty("Background", new int[][]{{10,10,10,500}}, "Gradient", "Slider");
        addRenderer(new GradientRenderer(getColorValue("Background"), width, height, getRenderListener(0), applet));
        addRenderer(new GraphicsRenderer(width, height, getRenderListener(1), this::drawi, applet));
    }

    protected abstract void initInputs();

    private void drawi(PGraphics gr) {
        if (background != null) {
          gr.image(background, 0, 0);
        }
        gr.translate(width/2, height/2);
        draw(gr);
        gr.translate(-width/2, -height/2);
    }

    protected abstract void draw(PGraphics gr);

    protected ValueChangedListener getInputListener(String k) {
        return value -> render(1);
    }

    protected ValueChangedListener<int[][]> getColorListener(String k) {
        if (k.equals("Foreground")) {
            return (color) -> render(1);
        } else {
            return (color) -> {((GradientRenderer)renderer.get(0)).setGradient(color); render(0);};
        }
    }

    protected ArtRenderer.RenderingFinishedListener getRenderListener(int i) {
        if (i == 0) {
            return (result) -> {background = result; render(1);};
        } else {
            return (result) -> {renderedImage = result; updateThumbnail();};
        }
    }

    protected GraphicsRenderer.GraphicsDrawer getDrawer(int i) {
        return this::drawi;
    }


}
