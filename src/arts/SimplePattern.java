package arts;

import processing.core.PApplet;
import processing.core.PGraphics;
import studio.ArtRenderer;
import studio.GraphicsRenderer;
import studio.input.SlideProperty;
import toolbox.ValueChangedListener;

public class SimplePattern extends ArtObject {

    private final int[][] defaultColors = {{232, 125, 30, 0},
                                     {161, 205, 69, 1},
                                     {141, 100, 170, 2},
                                     {75, 99, 174, 3},
                                     {240, 81, 51, 4},
                                     {69, 191, 180, 5},
                                     {228, 229, 230, 6}};

    private float gridCell;
    private float relGridCell;

    @Override
    public void init() {
        addInputProperty(new SlideProperty("Pattern Size", 30, 1, 100, 1));
        addColorProperty("Pattern", defaultColors, "Table", "Field");
        addColorProperty("Background", new int[][]{{255, 255, 255, 0}}, "Table", "Field");
        addRenderer(new GraphicsRenderer(width, height, getRenderListener(1), getDrawer(0), applet));
    }

    @Override
    protected ValueChangedListener getInputListener(String key) {
        return val -> render();
    }

    @Override
    protected ValueChangedListener getColorListener(String key) {
        return color -> render();
    }

    @Override
    protected ArtRenderer.RenderingFinishedListener getRenderListener(int i) {
        return (result) -> {renderedImage = result; updateThumbnail();};
    }

    @Override
    protected GraphicsRenderer.GraphicsDrawer getDrawer(int i) {
        return (gr) -> {
            int[] bg = getColorValue("Background")[0];
            gr.background(bg[0], bg[1], bg[2]);
            gridCell = (float)getInputValue("Pattern Size");
            relGridCell = PApplet.map(gridCell, 0, gridCell, 0, 100);
            for (float x = relGridCell*0.5F; x < (gr.width/gridCell)*relGridCell; x = x+relGridCell){
                for (float y = relGridCell*0.5F; y < (gr.height/gridCell)*relGridCell; y = y+relGridCell){
                    for (int ii=0; ii<2; ii++) {
                        int[] color = getColorValue("Pattern")[(int)(Math.random()*7)];
                        gr.stroke(color[0], color[1], color[2]);
                        gr.fill(color[0], color[1], color[2]);
                        drawShape(x, y, gr);
                    }
                }
            }


        };
    }

    @Override
    public ArtObject clone() {

            return clone(new SimplePattern());

    }


    private void drawShape(float xpos, float ypos, PGraphics gr){
        int toggle = (int)(Math.random()*13);
        switch (toggle) {
            case 12:
                drawCircleSmall(xpos, ypos, gr);
                break;
            case 11:
                drawCircleMid(xpos, ypos, gr);
                break;
            case 10:
                drawCircleBig(xpos, ypos, gr);
                break;
            case 9:
                diagonalSmallDown(xpos, ypos, gr);
                break;
            case 8:
                diagonalMidDown(xpos, ypos, gr);
                break;
            case 7:
                diagonalBigDown(xpos, ypos, gr);
                break;
            case 6:
                diagonalSmallUp(xpos, ypos, gr);
                break;
            case 5:
                diagonalMidUp(xpos, ypos, gr);
                break;
            case 4:
                diagonalBigUp(xpos, ypos, gr);
                break;
            case 3:
                drawLineOneHorz(xpos, ypos, gr);
                break;
            case 2:
                drawLineTwoHorz(xpos, ypos, gr);
                break;
            case 1:
                drawLineOneVert(xpos, ypos, gr);
                break;
            default:
                drawLineTwoVert(xpos, ypos, gr);
                break;
        }
    }


    private void drawCircleSmall(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noStroke();
        gr.ellipse(xpos, ypos, relGridCell*0.17F, relGridCell*0.17F);
        gr.popMatrix();
    }

    private void drawCircleMid(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noFill();
        gr.strokeCap(PApplet.ROUND);
        gr.strokeJoin(PApplet.ROUND);
        gr.strokeWeight(relGridCell*0.13F);
        gr.ellipse(xpos, ypos, relGridCell*0.30F, relGridCell*0.30F);
        gr.popMatrix();
    }

    private void drawCircleBig(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noFill();
        gr.strokeCap(PApplet.ROUND);
        gr.strokeJoin(PApplet.ROUND);
        gr.strokeWeight(relGridCell*0.13F);
        gr.ellipse(xpos, ypos, relGridCell*0.78F, relGridCell*0.78F);
        gr.popMatrix();
    }

    private void diagonalSmallDown(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noFill();
        gr.strokeCap(PApplet.ROUND);
        gr.strokeJoin(PApplet.ROUND);
        gr.strokeWeight(relGridCell*0.13F);
        gr.line(xpos+34, ypos-37, xpos+37, ypos-34);
        gr.line(xpos-37, ypos+34, xpos-34, ypos+37);
        gr.popMatrix();
    }

    private void diagonalMidDown(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noFill();
        gr.strokeCap(PApplet.ROUND);
        gr.strokeJoin(PApplet.ROUND);
        gr.strokeWeight(relGridCell*0.13F);
        gr.line(xpos-37, ypos, xpos, ypos+37);
        gr.line(xpos, ypos-37, xpos+37, ypos);
        gr.popMatrix();
    }

    private void diagonalBigDown(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noFill();
        gr.strokeCap(PApplet.ROUND);
        gr.strokeJoin(PApplet.ROUND);
        gr.strokeWeight(relGridCell*0.13F);
        gr.line(xpos-37, ypos-37, xpos+37, ypos+37);
        gr.popMatrix();
    }

    private void diagonalSmallUp(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noFill();
        gr.strokeCap(PApplet.ROUND);
        gr.strokeJoin(PApplet.ROUND);
        gr.strokeWeight(relGridCell*0.13F);
        gr.line(xpos-37, ypos-34, xpos-34, ypos-37);
        gr.line(xpos+34, ypos+37, xpos+37, ypos+34);
        gr.popMatrix();
    }

    private void diagonalMidUp(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noFill();
        gr.strokeCap(PApplet.ROUND);
        gr.strokeJoin(PApplet.ROUND);
        gr.strokeWeight(relGridCell*0.13F);
        gr.line(xpos-37, ypos, xpos, ypos-37);
        gr.line(xpos, ypos+37, xpos+37, ypos);
        gr.popMatrix();
    }

    private void diagonalBigUp(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noFill();
        gr.strokeCap(PApplet.ROUND);
        gr.strokeJoin(PApplet.ROUND);
        gr.strokeWeight(relGridCell*0.13F);
        gr.line(xpos-37, ypos+37, xpos+37, ypos-37);
        gr.popMatrix();
    }

    private void drawLineOneHorz(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noFill();
        gr.strokeCap(PApplet.ROUND);
        gr.strokeJoin(PApplet.ROUND);
        gr.strokeWeight(relGridCell*0.13F);
        gr.line(xpos-37, ypos, xpos+37, ypos);
        gr.popMatrix();
    }

    private void drawLineTwoHorz(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noFill();
        gr.strokeCap(PApplet.ROUND);
        gr.strokeJoin(PApplet.ROUND);
        gr.strokeWeight(relGridCell*0.13F);
        gr.line(xpos-37, ypos-37, xpos+37, ypos-37);
        gr.line(xpos-37, ypos+37, xpos+37, ypos+37);
        gr.popMatrix();
    }

    private void drawLineOneVert(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noFill();
        gr.strokeCap(PApplet.ROUND);
        gr.strokeJoin(PApplet.ROUND);
        gr.strokeWeight(relGridCell*0.13F);
        gr.line(xpos, ypos-37, xpos, ypos+37);
        gr.popMatrix();
    }

    private void drawLineTwoVert(float xpos, float ypos, PGraphics gr){
        gr.pushMatrix();
        // translate(xpos*0.5, ypos*0.5);
        gr.scale(gridCell/100);
        gr.noFill();
        gr.strokeCap(PApplet.ROUND);
        gr.strokeJoin(PApplet.ROUND);
        gr.strokeWeight(relGridCell*0.13F);
        gr.line(xpos-37, ypos-37, xpos-37, ypos+37);
        gr.line(xpos+37, ypos-37, xpos+37, ypos+37);
        gr.popMatrix();
    }
}
