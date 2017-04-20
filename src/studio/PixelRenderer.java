package studio;

import processing.core.PApplet;
import processing.core.PGraphics;

public class PixelRenderer extends ArtRenderer {

    public interface PixelDrawer {
        void makePixels(int[] pixels, int w, int h);
    }

    private PixelDrawer drawer;


    public PixelRenderer(int w, int h, PixelDrawer d, RenderingFinishedListener l, PApplet a) {
        super(w, h, l, a);
        drawer = d;

    }

    @Override
    public ArtRenderer clone(RenderingFinishedListener l) {
        PixelRenderer r = new PixelRenderer(width, height, drawer, l, applet);
        clone(r);
        return r;
    }

    @Override
    protected void renderImage() {
        renderResult = applet.createImage(width, height, PApplet.RGB);
        renderResult.loadPixels();
        drawer.makePixels(renderResult.pixels, renderResult.width, renderResult.height);
        renderResult.updatePixels();
    }


}
