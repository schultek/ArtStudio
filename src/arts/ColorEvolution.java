package arts;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import studio.input.FileProperty;
import studio.input.SlideProperty;
import toolbox.ValueChangedListener;

import java.io.File;
import java.util.ArrayList;

public class ColorEvolution extends SimpleDrawer {

    ArrayList<int[]> edges;
    int white;
    PImage image = null;

    @Override
    protected void initInputs() {
        addInputProperty(new FileProperty("Image", "Load Image", new String[]{".jpg", ".png"}));
        addInputProperty(new SlideProperty("Randomness", 10, 0, 100, 1));
        addInputProperty(new SlideProperty("Saturation", 10, 0, 254, 1));
        addInputProperty(new SlideProperty("Brightness", 10, 0, 254, 1));
        white = applet.color(255);
    }

    @Override
    protected void draw(PGraphics gr) {
        if (image == null) {
            return;
        } else {
            gr = applet.createGraphics(image.width, image.height);
            gr.beginDraw();
        }
        gr.image(image, 0, 0);
        gr.endDraw();
        edges = new ArrayList<>();
        gr.loadPixels();
        for (int x=0; x<gr.width; x++) {
            for (int y=0; y<gr.height; y++) {
                if (testPixel(x, y, gr)) {
                    edges.add(new int[]{x, y});
                }
            }
        }
        gr.updatePixels();

        System.out.println(edges.size());
        int ram = (int)(float)getInputValue("Randomness");
        int minsat = (int)(float)getInputValue("Saturation")+1;
        int minbri = (int)(float)getInputValue("Brightness")+1;

        gr.colorMode(PApplet.HSB, 255,255,255);
        gr.loadPixels();
        while (edges.size() > 0) {

            int ind = (int)applet.random(edges.size());
            int[] p = edges.get(ind);
            if (!testPixel(p[0],p[1], gr)) {
                edges.remove(ind);
                continue;
            }
            int d = 0, e = 0;
            int s = (int)applet.random(4);
            for (int i=0; i<4; i++) {
                int c = (i+s)%4;
                d = c == 0 ? 1 : c == 2 ? -1 : 0;
                e = c == 1 ? 1 : c == 3 ? -1 : 0;

                if (p[0] + d >= 0 && p[1] + e >= 0 && p[0] + d < width && p[1] + e < height && isblack(gr.pixels[(p[1]+e)*gr.width+(p[0]+d)], gr)) {

                    int clr = gr.pixels[p[1]*gr.width+p[0]];
                    int hu = (int)(gr.hue(clr)+applet.random(-ram, ram))%255;
                    int sa = (int)applet.constrain(gr.saturation(clr)+applet.random(-ram, ram), minsat, 255);
                    int br = (int)applet.constrain(gr.brightness(clr)+applet.random(-ram, ram), minbri, 255);
                    gr.pixels[(p[1]+e)*gr.width+(p[0]+d)] = gr.color(hu, sa, br);
                    if (testPixel(p[0]+d, p[1]+e, gr)) {
                        edges.add(new int[]{p[0]+d, p[1]+e});
                    }
                    if (!testPixel(p[0],p[1], gr)) {
                        edges.remove(ind);
                    }
                    break;
                }
            }
        }

        gr.updatePixels();
    }

    private boolean testPixel(int x, int y, PGraphics gr) {
        if (!isblack(gr.pixels[y*gr.width+x], gr) && !iswhite(gr.pixels[y*gr.width+x], gr)) {
            return ((x < gr.width-1  && isblack(gr.pixels[y*gr.width+x+1], gr))   ||
                    (x > 0           && isblack(gr.pixels[y*gr.width+x-1], gr))   ||
                    (y < gr.height-1 && isblack(gr.pixels[(y+1)*gr.width+x], gr)) ||
                    (y > 0           && isblack(gr.pixels[(y-1)*gr.width+x], gr)));
        } else {
            return false;
        }
    }

    private boolean isblack(int c, PGraphics gr) {
        return gr.brightness(c)<=(float)getInputValue("Brightness") && gr.saturation(c)<=(float)getInputValue("Saturation") && !iswhite(c, gr);
    }

    private boolean iswhite(int c, PGraphics gr) {
        return gr.brightness(c)>100 && gr.saturation(c)<=(float)getInputValue("Saturation");
    }

    @Override
    protected ValueChangedListener getInputListener(String k) {
        switch (k) {
            case "Image":
                return (val) -> {
                    image = getImage((File) val);
                    render(1);
                    updateThumbnail();
                };
            default:
                return (val) -> {
                    render(1);
                    updateThumbnail();
                };
        }
    }

    private PImage getImage(File f) {
        PImage image = applet.loadImage(f.getAbsolutePath());
        return image;/*
        PImage i1 = image.copy();
        i1.resize(width, 0);
        if (i1.height > height) {
            return i1;
        } else {
            image.resize(0, height);
            return image;
        }*/
    }

    @Override
    public ArtObject clone() {
        return clone(new ColorEvolution());
    }

}
