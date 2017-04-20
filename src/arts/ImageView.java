package arts;

import processing.core.PApplet;
import processing.core.PImage;
import studio.ArtRenderer;
import studio.input.FileProperty;
import studio.GraphicsRenderer;
import toolbox.ValueChangedListener;

import java.io.File;

public class ImageView extends ArtObject {

    @Override
    public void init() {
        renderedImage = applet.createImage(width, height, PApplet.RGB);
        addInputProperty(new FileProperty("File", "Load Image", new String[]{".jpg", ".png", ".jpeg"}));
    }

    @Override
    protected ValueChangedListener getInputListener(String key) {
        if (key.equals("File")) {
            return (val) -> {
                PImage image = applet.loadImage(((File) val).getAbsolutePath());
                PImage i1 = image.copy();
                i1.resize(width, 0);
                if (i1.height > height) {
                    renderedImage = i1;
                } else {
                    image.resize(0, height);
                    renderedImage = image;
                }
                updateThumbnail();
            };
        } else {
            return null;
        }
    }

    @Override
    protected ValueChangedListener getColorListener(String key) {
        return null;
    }

    @Override
    protected ArtRenderer.RenderingFinishedListener getRenderListener(int i) {
        return null;
    }

    @Override
    protected GraphicsRenderer.GraphicsDrawer getDrawer(int i) {
        return null;
    }

    @Override
    public ArtObject clone() {
        return clone(new ImageView());
    }
}
