package studio;

import processing.core.PApplet;

public class GradientRenderer extends ArtRenderer {

    private int[][] gradient;

    public GradientRenderer(int[][] g, int w, int h, RenderingFinishedListener l, PApplet a) {
        super(w, h, l, a);
        gradient = g;
        renderResult = applet.createImage(w, h, PApplet.RGB);
    }

    public void setGradient(int[][] grad) {
        gradient = grad;
    }

    @Override
    public ArtRenderer clone(RenderingFinishedListener l) {
        GradientRenderer r = new GradientRenderer(gradient, width, height, l, applet);
        clone(r);
        return r;
    }

    @Override
    protected void renderImage() {
        int r, g, b;
        renderResult.loadPixels();
        if (gradient[0][3] > 0) {
            int maxx = (int) PApplet.map(gradient[0][3], 0, 1000, 0, renderResult.width);
            for (int x = 0; x < maxx; x++) {
                int c = applet.color(gradient[0][0], gradient[0][1], gradient[0][2]);
                for (int y=0; y<renderResult.height;y++) {
                    renderResult.pixels[y*renderResult.width+x] = c;
                }
            }
        }
        for (int i=0; i<gradient.length-1; i++) {
            int minx = (int) PApplet.map(gradient[i][3], 0, 1000, 0, renderResult.width);
            int maxx = (int) PApplet.map(gradient[i+1][3], 0, 1000, 0, renderResult.width);
            for (int x = minx; x < maxx; x++) {
                r = (int) PApplet.map(x, minx, maxx, gradient[i][0], gradient[i+1][0]);
                g = (int) PApplet.map(x, minx, maxx, gradient[i][1], gradient[i+1][1]);
                b = (int) PApplet.map(x, minx, maxx, gradient[i][2], gradient[i+1][2]);
                int c = applet.color(r, g, b);
                for (int y=0; y<renderResult.height;y++) {
                    renderResult.pixels[y*renderResult.width+x] = c;
                }
            }
        }
        if (gradient[gradient.length-1][3] < 1000) {
            int minx = (int) PApplet.map(gradient[gradient.length-1][3], 0, 1000, 0, renderResult.width);
            for (int x = minx; x < renderResult.width; x++) {
                int c = applet.color(gradient[gradient.length-1][0], gradient[gradient.length-1][1], gradient[gradient.length-1][2]);
                for (int y=0; y<renderResult.height;y++) {
                    renderResult.pixels[y*renderResult.width+x] = c;
                }
            }
        }
        renderResult.updatePixels();
    }
}
