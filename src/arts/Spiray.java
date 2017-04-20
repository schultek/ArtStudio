package arts;


import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import studio.input.CheckProperty;
import studio.input.FileProperty;
import studio.input.SlideProperty;
import toolbox.ValueChangedListener;

import java.io.File;

public class Spiray extends SimpleDrawer {

    private PImage image;
    private String text = "Lorem ipsum";

    @Override
    protected void initInputs() {
        addInputProperty(new FileProperty("Image", "Load Image", new String[]{".jpg", ".png"}));
        addInputProperty(new FileProperty("Text", "Load Text", ".txt"));
        addInputProperty(new SlideProperty("Spiral Size", 20, 10, 50, 1));
        addInputProperty(new SlideProperty("Character Spacing", -1, -2, 30, 1));
        addInputProperty(new SlideProperty("X-Position", width / 2, 0, width, 1));
        addInputProperty(new SlideProperty("Y-Position", height / 2, 0, height, 1));
        addInputProperty(new SlideProperty("Start Radius", 5, 1, 100, 1));
        addInputProperty(new SlideProperty("Start Angle", 50, 0, 359, 1));
        addInputProperty(new SlideProperty("Borderwidth", 30, 0, 100, 1));
        addInputProperty(new CheckProperty("Fill Frame", true));
        addInputProperty(new CheckProperty("Use Image-Colors", true));
        addInputProperty(new CheckProperty("Repeat Text", true));
        //TODO Font Property
    }

    @Override
    protected void draw(PGraphics gr) {
        if (text == null || image == null) {
            return;
        }
        gr.translate(-width/2, -height/2);
        gr.textAlign(PConstants.CENTER);
        int iterCharPos = 0;
        char currSymbol;
        float spiralSpaceStep, xx, yy, angleStep = 0, radiusStep, currSymbolWidth;
        float angle = (float)getInputValue("Start Angle");
        float currRadius = (float)getInputValue("Start Radius");
        float xPos = (float) getInputValue("X-Position");
        float yPos = (float) getInputValue("Y-Position");
        float spiralSize = (float) getInputValue("Spiral Size");
        float borderDistance = (float)getInputValue("Borderwidth");
        float shiftSpiralSpaceStep = (float)getInputValue("Character Spacing");
        int[][] color = getColorList(text.length(), getColorValue("Foreground"));

        while (iterCharPos < text.length()) {
            currSymbol = text.charAt(iterCharPos);
            currSymbolWidth = gr.textWidth(currSymbol);
            spiralSpaceStep = currSymbolWidth + shiftSpiralSpaceStep;
            radiusStep = spiralSize / (PConstants.TWO_PI * currRadius / spiralSpaceStep);
            currRadius += radiusStep;

            angleStep += currSymbolWidth / 2 + shiftSpiralSpaceStep / 2;
            angle += angleStep / currRadius;
            angleStep = currSymbolWidth / 2 + shiftSpiralSpaceStep / 2;

            xx = PApplet.cos(angle) * currRadius + xPos;
            yy = PApplet.sin(angle) * currRadius + yPos;

            if (!(boolean)getInputValue("Fill Frame") && (xx - 10 < 0 || xx + 10 > width || yy - 10 < 0 || yy + 10 > height)) {
                break;
            } else if (!(xx < borderDistance || xx > width - borderDistance || yy < borderDistance || yy > height - borderDistance)) {
                if ((boolean)getInputValue("Use Image-Colors")) {
                    gr.fill(image.get((int) xx, (int) yy));
                } else {
                    gr.fill(color[iterCharPos][0], color[iterCharPos][1], color[iterCharPos][2]);
                }
                gr.pushMatrix();
                gr.translate(xx, yy);
                gr.rotate(PConstants.PI / 2 + angle);
                gr.text(currSymbol, 0, 0);
                gr.popMatrix();
                iterCharPos++;
                if (iterCharPos == text.length() && (boolean)getInputValue("Repeat Text")) {
                    iterCharPos = 0;
                }
            } else if (Math.max(Math.max(Math.max(PApplet.dist(xPos, yPos, borderDistance, borderDistance),
                    PApplet.dist(xPos, yPos, width - borderDistance, borderDistance)),
                    PApplet.dist(xPos, yPos, width - borderDistance, height - borderDistance)),
                    PApplet.dist(xPos, yPos, borderDistance, height - borderDistance)) < PApplet.dist(xx, yy, xPos, yPos)) {
                break;
            }
        }
        gr.translate(width/2, height/2);
    }

    @Override
    public ArtObject clone() {
        Spiray spiry = clone(new Spiray());
        spiry.image = image;
        spiry.text = text;
        return spiry;
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
            case "Text":
                return (val) -> {
                    text = getText((File) val);
                    if (!text.endsWith(" ")) {
                        text += " ";
                    }
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
        PImage i1 = image.copy();
        i1.resize(width, 0);
        if (i1.height > height) {
            return i1;
        } else {
            image.resize(0, height);
            return image;
        }
    }

    private String getText(File f) {
        return PApplet.join(applet.loadStrings(f.getAbsolutePath()), " ");
    }
}