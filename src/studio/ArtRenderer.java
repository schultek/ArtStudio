package studio;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class ArtRenderer implements Runnable {

    public interface RenderingFinishedListener {
        void onRenderingFinished(PImage result);
    }

    protected int width, height;
    private int[] strokeColor, fillColor;
    protected boolean stroke, fill;
    PImage renderResult;
    private RenderingFinishedListener listener;
    private Thread renderThread;
    PApplet applet;
    private boolean rendering = false, waiting = false, rerender = false;

    private boolean stop = false;

    ArtRenderer(int w, int h, RenderingFinishedListener l, PApplet a) {
        width = w;
        height = h;
        listener = l;
        applet = a;
        renderThread = new Thread(this);
        strokeColor = new int[]{0,0,0,1};
        fillColor = new int[]{255,255,255,1};
        stroke = true;
        fill = true;
    }

    public synchronized void start() {
        renderThread.start();
    }

    public synchronized void render() {
        if (!stop) {
            if (waiting) {
                this.notify();
            } else if (rendering) {
                rerender = true;
            }
        }
    }

    public synchronized void stop() {
        stop = true;
    }

    public abstract ArtRenderer clone(RenderingFinishedListener l);

    public boolean isRendering() {
        return rendering;
    }

    public void resetRenderListener(RenderingFinishedListener l) {
        listener = l;
    }

    void clone(ArtRenderer r) {
        r.strokeColor = strokeColor.clone();
        r.fillColor = fillColor.clone();
        r.stroke = stroke;
        r.fill = fill;
        r.renderResult = renderResult;
    }

    @Override
    public void run() {
        while (!stop) {
            rendering = true;
            rerender = false;
            renderImage();
            rendering = false;
            synchronized (this) {
                listener.onRenderingFinished(renderResult.copy());
                if (!rerender) {
                    try {
                        waiting = true;
                        this.wait();
                        waiting = false;
                    } catch (InterruptedException e) {
                        stop();
                    }
                }
            }
        }
    }

    protected abstract void renderImage();

    private void setPixel(int x, int y, int[] color) {
        if (color[3] < 1) {
            int rgb = renderResult.pixels[y*width+x];
            int r = (int)applet.map(color[3], 0, 1, applet.red(rgb), color[0]);
            int g = (int)applet.map(color[3], 0, 1, applet.green(rgb), color[1]);
            int b = (int)applet.map(color[3], 0, 1, applet.blue(rgb), color[2]);
            renderResult.pixels[y*width+x] = applet.color(r, g, b);
        } else {
            renderResult.pixels[y*width+x] = applet.color(color[0], color[1], color[2]);
        }
    }

    protected final void makeRect(int x, int y, int w, int h) {
        if (stroke) {
            makeStrokeRect(x,y,w,h);
        }
        if (fill) {
            makeFillRect(x,y,w,h);
        }
    }

    protected final void makeStrokeRect(int x_, int y_, int w, int h) {
        for (int x = x_; x < x_+w; x++) {
            setPixel(x, y_, strokeColor);
            setPixel(x, y_+h, strokeColor);
        }
        for (int y = y_; y < y_+h; y++) {
            setPixel(x_, y, strokeColor);
            setPixel(x_+w, y, strokeColor);
        }
    }

    protected final void makeFillRect(int x_, int y_, int w, int h) {
        for (int x = x_+1; x<x_+w-1; x++) {
            for (int y = y_+1; y<y_+h-1; y++) {
                setPixel(x, y, fillColor);
            }
        }
    }

    protected final void makeEllipse(int x, int y, int w, int h) {
        if (stroke) {
            makeStrokeEllipse(x, y, w, h);
        }
        if (fill) {
            makeFillEllipse(x, y, w, h);
        }
    }

    protected final void makeStrokeEllipse(int x_, int y_, int w, int h) {

    }

    protected final void makeFillEllipse(int x_, int y_, int w, int h) {

    }
}
