package studio;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class Loader {

    private PImage[] frames;
    private int cF = 0;
    public int width = 100;
    public int height = 100;

    public Loader(PApplet p) {
        createFrames(p);
    }

    public PImage frame() {
        cF = (cF+1)%frames.length;
        return frames[cF];
    }

    private void createFrames(PApplet p) {
        PGraphics gr = p.createGraphics(100, 100);
        frames = new PImage[200];

        float step = 20;
        float startPosition = -(float)(Math.PI/2);
        float num = 4;
        float theta = 0;

        for (int i=0; i<frames.length; i++) {
            gr.beginDraw();
            gr.loadPixels();
            for (int j=0; j<gr.pixels.length; j++) {
                gr.pixels[j] = p.color(0, 0);
            }
            gr.updatePixels();
            gr.noFill();
            gr.strokeWeight(4);
            gr.colorMode(PApplet.HSB);
            gr.translate(width/2, height/2);
            for (int n=1; n<num; n++) {

                // end point of arch in radians
                float angle = (theta+((PConstants.PI/4/num)*n)) % PConstants.PI;

                gr.stroke(angle*255/PConstants.PI, 255, 200);
                float arcEnd = p.map(p.sin(angle),-1,1, -PConstants.TWO_PI, PConstants.TWO_PI);


                if(angle <= (PConstants.PI/2) ) {
                    // (Centre of circle | Width and height in px | radians start and stop )
                    gr.arc(0, 0, n*step, n*step, 0+startPosition , arcEnd+startPosition );

                } else {
                    gr.arc(0, 0, n*step, n*step, PConstants.TWO_PI-arcEnd+startPosition , PConstants.TWO_PI+startPosition );
                }
            }
            gr.endDraw();
            frames[i] = gr.get();
            theta += PConstants.TWO_PI/frames.length;
        }
    }

}
