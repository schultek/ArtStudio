package arts;

import processing.core.PApplet;
import processing.core.PImage;
import studio.*;
import studio.color.ColorProperty;
import toolbox.ValueChangedListener;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ArtObject {

    PApplet applet;
    PImage renderedImage;
    Loader loader;

    private ArtRenderer.RenderingFinishedListener thumbnailListener;

    protected int x, y, width, height;

    private HashMap<String, InputProperty> inputProperties;
    private HashMap<String, ColorProperty> colorProperties;
    ArrayList<ArtRenderer> renderer;

    private InputProperty.InputControl inputControl;
    private ColorProperty.ColorControl colorControl;

    public ArtObject() {}

    public void init(int x_, int y_, int w, int h, InputProperty.InputControl ic, ColorProperty.ColorControl cc, PApplet a, Loader l) {
        x = x_;
        y = y_;
        width = w;
        height = h;
        applet = a;
        loader = l;
        inputProperties = new HashMap<>();
        colorProperties = new HashMap<>();
        renderer = new ArrayList<>();
        inputControl = ic;
        colorControl = cc;

    }

    public void startRenderer() {
        for (ArtRenderer aRenderer : renderer) {
            aRenderer.start();
        }
    }

    public abstract void init();

    public void setThumbnailListener(ArtRenderer.RenderingFinishedListener l) {
        thumbnailListener = l;
    }

    void updateThumbnail() {
        if (thumbnailListener != null) {
            thumbnailListener.onRenderingFinished(renderedImage);
        }
    }

    void addRenderer(ArtRenderer r) {
        renderer.add(r);
    }


    <T extends InputProperty> void addInputProperty(T property) {
        property.init(getInputListener(property.getLabel()), inputControl);
        inputProperties.put(property.getLabel(), property);
    }

    void addColorProperty(String label, int[][] init, String view, String picker) {
        colorProperties.put(label, new ColorProperty(init, getColorListener(label), colorControl, label, view, picker));
    }

    private void setInputProperties(HashMap<String, InputProperty> inputProperties) {
        this.inputProperties = inputProperties;
    }

    private void setRenderer(ArrayList<ArtRenderer> renderer) {
        this.renderer = renderer;
    }

    private void setColorProperties(HashMap<String, ColorProperty> colorProperties) {
        this.colorProperties = colorProperties;
    }

    Object getInputValue(String input) {
        return inputProperties.get(input).getValue();
    }

    int[][] getColorValue(String color) { return colorProperties.get(color).getValue(); }

    public void makeDraw() {
        if (renderedImage != null) {
            applet.image(renderedImage, 0, 0);
        }
        if (rendering()) {
            applet.image(loader.frame(), width/2-loader.width/2, height/2-loader.height/2);
        }
    }

    private boolean rendering() {
        for (ArtRenderer aRenderer : renderer) {
            if (aRenderer.isRendering()) {
                return true;
            }
        }
        return false;
    }

    final void render() {
        for (ArtRenderer aRenderer : renderer) {
            aRenderer.render();
        }
    }

    final void render(int i) {
        renderer.get(i).render();

    }

    public PImage getRenderedImage() {
        return renderedImage;
    }

    public void resetListeners() {
        inputProperties.entrySet().forEach((e) -> e.getValue().resetWatchableListener());
        for (int i=0; i<renderer.size(); i++) {
            if (renderer.get(i) instanceof GraphicsRenderer) {
                ((GraphicsRenderer)renderer.get(i)).resetDrawer(getDrawer(i));
            } else {
                renderer.get(i).resetRenderListener(getRenderListener(i));
            }
        }
        colorProperties.entrySet().forEach((e) -> e.getValue().resetColorListener());
    }

    private void cloneInputProperties(ArtObject o) {
        HashMap<String, InputProperty> properties = new HashMap<>();
        inputProperties.entrySet().forEach((e) -> properties.put(e.getKey(), e.getValue().clone(o.getInputListener(e.getKey()))));
        o.setInputProperties(properties);
    }

    protected abstract ValueChangedListener getInputListener(String key);

    private void cloneColorProperties(ArtObject o) {
        HashMap<String, ColorProperty> properties = new HashMap<>();
        colorProperties.entrySet().forEach((e) -> properties.put(e.getKey(), e.getValue().clone(o.getColorListener(e.getKey()))));
        o.setColorProperties(properties);
    }

    protected abstract ValueChangedListener getColorListener(String key);

    private void cloneRenderer(ArtObject o) {
        ArrayList<ArtRenderer> rend = new ArrayList<>();
        for (int i=0; i<renderer.size(); i++) {
            if (renderer.get(i) instanceof GraphicsRenderer) {
                rend.add(((GraphicsRenderer) renderer.get(i)).clone(o.getRenderListener(i), o.getDrawer(i)));
            } else {
                rend.add(renderer.get(i).clone(o.getRenderListener(i)));
            }
            rend.get(rend.size()-1).start();
        }
        o.setRenderer(rend);
    }

    protected abstract ArtRenderer.RenderingFinishedListener getRenderListener(int i);

    protected abstract GraphicsRenderer.GraphicsDrawer getDrawer(int i);

    public abstract ArtObject clone();

    final <T extends ArtObject> T clone(T o) {
        o.init(x, y, width, height, inputControl, colorControl, applet, loader);
        cloneInputProperties(o);
        cloneColorProperties(o);
        cloneRenderer(o);
        o.renderedImage = renderedImage;
        return o;
    }


    int[][] getColorList(int n, int[][] color) {
        int[][] colorlist = new int[n][3];
        if (color[0][3] > 0) {
            int max = (int) PApplet.map(color[0][3], 0, 1000, 0, n);
            for (int i=0; i<max; i++) {
                colorlist[i] = color[0];
            }
        }
        for (int c=0; c<color.length-1; c++) {
            int min = (int)PApplet.map(color[c][3], 0, 1000, 0, n);
            int max = (int)PApplet.map(color[c+1][3], 0, 1000, 0, n);
            for (int i=min; i<max; i++) {
                colorlist[i][0] = (int)PApplet.map(i, min, max, color[c][0], color[c+1][0]);
                colorlist[i][1] = (int)PApplet.map(i, min, max, color[c][1], color[c+1][1]);
                colorlist[i][2] = (int)PApplet.map(i, min, max, color[c][2], color[c+1][2]);
            }
        }
        if (color[color.length-1][3] < 1000) {
            int min = (int)PApplet.map(color[color.length-1][3], 0, 1000, 0, n);
            for (int i=min; i<n; i++) {
                colorlist[i] = color[color.length-1];
            }
        }
        return colorlist;
    }

}
