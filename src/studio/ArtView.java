package studio;

import arts.ArtObject;
import processing.event.MouseEvent;
import toolbox.GUIObject;

public class ArtView extends GUIObject {


    private final InputManager inputManager;
    private final ColorManager colorManager;
    private ArtObject artObject;

    private int dragx, dragy, startx, starty, drawx, drawy;

    ArtView(int x, int y, int w, int h, InputManager im, ColorManager cm) {
        super(x, y, w, h);
        inputManager = im;
        colorManager = cm;
        drawx = 0;
        drawy = 0;
    }

    public void draw() {
        parent.translate(xPos+drawx, yPos+drawy);
        if (artObject != null) {
            artObject.makeDraw();
        }
        parent.translate(-drawx-xPos, -drawy-yPos);
    }
    public boolean mouseEvent(MouseEvent e) {
        boolean m = super.mouseEvent(e);
        if (e.getAction() == MouseEvent.RELEASE) {
            pressed = false;
            dragged = false;
        }
        if (e.getAction() == MouseEvent.PRESS && pressed) {
            dragx = e.getX();
            dragy = e.getY();
            startx = drawx;
            starty = drawy;
        }
        if (dragged) {
            drawx = Math.max(Math.min(startx - (dragx - e.getX()), 0), width-artObject.getRenderedImage().width);
            drawy = Math.max(Math.min(starty - (dragy - e.getY()), 0), height-artObject.getRenderedImage().height);
        }
        return m;
    }


    void updateMode(ModeManager.Mode mode) {
        artObject = mode.getObject();
        inputManager.setElements(mode.getInputElements());
        colorManager.setElements(mode.getColorElements());
    }

    void switchArtObject(ArtObject object) {
        artObject = object;
        object.resetListeners();
    }

    ArtObject dublicateArtObject() {
        switchArtObject(artObject.clone());
        return artObject;
    }
}