package studio;

import processing.core.PApplet;
import processing.core.PGraphics;

public class GraphicsRenderer extends ArtRenderer {


    public interface GraphicsDrawer {
        void draw(PGraphics pg);
    }

    private PGraphics pgraphics;
    private GraphicsDrawer drawer;

    public GraphicsRenderer(int w, int h, RenderingFinishedListener l, GraphicsDrawer d, PApplet a) {
        super(w, h, l, a);
        drawer = d;
        pgraphics = a.createGraphics(w, h);
    }

    public ArtRenderer clone(RenderingFinishedListener l, GraphicsDrawer d) {
        GraphicsRenderer r = new GraphicsRenderer(width, height, l, d, applet);
        clone(r);
        return r;
    }

    @Override
    public ArtRenderer clone(RenderingFinishedListener l) {
        throw new IllegalArgumentException("Should use clone(RenderingFinishedListener l, GraphicsDrawer d) on GraphicsRenderer");
    }

    public void resetDrawer(GraphicsDrawer dr) {
        drawer = dr;
    }

    @Override
    protected void renderImage() {
        pgraphics.beginDraw();
        drawer.draw(pgraphics);
        pgraphics.endDraw();
        renderResult = pgraphics.get();
    }

}
