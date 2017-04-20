package arts;

import processing.core.*;
import studio.ArtRenderer;
import studio.GradientRenderer;
import studio.GraphicsRenderer;
import studio.input.SlideProperty;
import toolbox.ValueChangedListener;

import java.util.ArrayList;

public class Amoeba extends ArtObject {

    private PImage background;

    private static final float corr = 0.4F;
    private ArrayList<Ring> rings = new ArrayList<>();

    protected ValueChangedListener getInputListener(String k) {
        return value -> render(1);
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
    protected ValueChangedListener<int[][]> getColorListener(String k) {
        if (k.equals("Rings")) {
            return (color) -> render(1);
        } else {
            return (color) -> {((GradientRenderer)renderer.get(0)).setGradient(color); render(0);};
        }
    }

    public void init() {
        addInputProperty(new SlideProperty("Ring Count", 200, 5, 1000, 1));
        addInputProperty(new SlideProperty("Vertex Count", 100, 4, 800, 1));
        addInputProperty(new SlideProperty("Min. Radius", 200, 50, 600, 1));
        addInputProperty(new SlideProperty("Max. Radius", 400, 50, 600, 1));
        addInputProperty(new SlideProperty("Vertex Noise", 0.5F, 0, 1, 0.01F));
        addInputProperty(new SlideProperty("Ring Noise", 0.5F, 0, 1, 0.01F));
        addInputProperty(new SlideProperty("Linear Spread", 0, 0, 1, 0.01F));
        addInputProperty(new SlideProperty("Vertical Spread", 0, 0, 1, 0.01F));
        addColorProperty("Rings", new int[][]{{255,255,255,500}}, "Gradient", "Slider");
        addColorProperty("Background", new int[][]{{10,10,10,500}}, "Gradient", "Slider");
        addRenderer(new GradientRenderer(getColorValue("Background"), width, height, getRenderListener(0), applet));
        addRenderer(new GraphicsRenderer(width, height, getRenderListener(1), this::drawi, applet));
    }

    private void drawi(PGraphics gr) {
        if (background != null) {
            gr.image(background, 0, 0);
        }
        gr.translate(width/2, height/2);
        rings.clear();
        int n = (int)(float)getInputValue("Vertex Count");
        float min = (float)getInputValue("Min. Radius");
        float max = (float)getInputValue("Max. Radius");
        float vn = PApplet.map((float)getInputValue("Vertex Noise"), 0, 1, 0.005F, 0.1F);
        float rn = PApplet.map((float)getInputValue("Ring Noise"), 0, 1, 0.001F, 0.05F);
        float ls = (float)getInputValue("Linear Spread");
        float vs = (float)getInputValue("Vertical Spread");
        int frame = 0;
        for (int r=0; r<(float)getInputValue("Ring Count"); r++) {
            float[] merge = new float[2];
            float[] rads = new float[PApplet.parseInt(n*corr*2)+1];
            Ring ring = new Ring(n);
            for (int i=-PApplet.parseInt(n*corr); i<=n+PApplet.parseInt(n*corr); i++) {
                float rad = PApplet.map(applet.noise(i*vn, frame*rn), 0, 1, min, max);
                if (i > n-PApplet.parseInt(n*corr)) {
                    rad = PApplet.map(i, n-PApplet.parseInt(n*corr), n+PApplet.parseInt(n*corr), rad, rads[i+PApplet.parseInt(n*corr)-n]);
                } else if (i <= n*corr) {
                    rads[i+PApplet.parseInt(n*corr)] = rad;
                }
                if (i >= PApplet.parseInt(n*corr)) {
                    ring.next(rad);
                }
                if (i > PApplet.parseInt(n*corr) && i<PApplet.parseInt(n*corr)+3) {
                    merge[i-PApplet.parseInt(n*corr)-1] = rad;
                }
            }
            ring.next(merge[0]);
            ring.next(merge[1]);
            rings.add(ring);
            frame++;
        }
        gr.noFill();
        updateRingColors();
        gr.translate(-(width)*ls, 0);
        float noi = 0;
        for (Ring ring : rings) {
            ring.draw(25, gr);
            gr.translate(ls*(width*2/rings.size()), vs*(applet.noise(noi, noi)*10-5));
            noi+=1/rings.size();
        }
        gr.translate(-(width)*ls, 0);
        gr.translate(-width/2, -height/2);
    }

    private void updateRingColors() {
        int[][] ringcolor = getColorValue("Rings");
        if (ringcolor[0][3] > 0) {
            int maxring = (int) PApplet.map(ringcolor[0][3], 0, 1000, 0, rings.size());
            for (int r=0; r<maxring; r++) {
                rings.get(r).setColor(ringcolor[0]);
            }
        }
        for (int i=0; i<ringcolor.length-1; i++) {
            int minring = (int)PApplet.map(ringcolor[i][3], 0, 1000, 0, rings.size());
            int maxring = (int)PApplet.map(ringcolor[i+1][3], 0, 1000, 0, rings.size());
            for (int r=minring; r<maxring; r++) {
                rings.get(r).r = (int)PApplet.map(r, minring, maxring, ringcolor[i][0], ringcolor[i+1][0]);
                rings.get(r).g = (int)PApplet.map(r, minring, maxring, ringcolor[i][1], ringcolor[i+1][1]);
                rings.get(r).b = (int)PApplet.map(r, minring, maxring, ringcolor[i][2], ringcolor[i+1][2]);
            }
        }
        if (ringcolor[ringcolor.length-1][3] < 1000) {
            int minring = (int)PApplet.map(ringcolor[ringcolor.length-1][3], 0, 1000, 0, rings.size());
            for (int r=minring; r<rings.size(); r++) {
                rings.get(r).setColor(ringcolor[ringcolor.length-1]);
            }
        }
    }

    @Override
    public ArtObject clone() {
        Amoeba amoeba = clone(new Amoeba());
        amoeba.background = background;
        for (int i=0; i<rings.size(); i++) {
            amoeba.rings.add(rings.get(i).clone());
        }
        return amoeba;
    }

    public class Ring {

        private float[] radData;
        private float[][] posData;

        private int r = 255, g = 255, b = 255;
        private int itr = 0;

        Ring(int n) {
            radData = new float[n+3];
            posData = new float[n+3][2];
        }

        void next(float r) {
            radData[itr] = r;
            float ang = PApplet.map(itr, 0, radData.length-3, 0, (float)Math.PI*2-0.007F);
            posData[itr][0] = radData[itr]*(float)Math.cos(ang);
            posData[itr][1] = radData[itr]*(float)Math.sin(ang);
            itr++;
        }

        public void draw(int a, PGraphics gr) {
            gr.stroke(r, g, b, a);
            gr.noFill();
            gr.beginShape();
            for (int i=0; i<radData.length; i++) {
                gr.curveVertex(posData[i][0], posData[i][1]);
            }
            gr.endShape(PConstants.CLOSE);
        }

        @Override
        public Ring clone() {
            Ring clone = new Ring(radData.length-3);
            clone.radData = radData.clone();
            clone.posData = posData.clone();
            clone.r = r;
            clone.g = g;
            clone.b = b;
            return clone;
        }

        public void setColor(int[] rgb) {
            r = rgb[0];
            g = rgb[1];
            b = rgb[2];
        }

    }
}