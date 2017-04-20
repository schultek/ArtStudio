package studio;

import arts.ArtObject;
import processing.core.PApplet;
import processing.event.MouseEvent;
import toolbox.GUIObject;

import java.util.ArrayList;

public class ModeManager extends GUIObject {

    private Loader loader;

    public interface ModeChanger {
        ArrayList<ImageList.ImageData> changeMode(Mode mode);
    }

    private ArrayList<Mode> modes;
    private ModeChanger modeChanger;
    private int selected = -1;

    private int modeX, modeY, modeWidth, modeHeight;

    private InputManager inputManager;
    private ColorManager colorManager;

    ModeManager(int x, int y, int w, int h, int x2, int y2, int w2, int h2, ModeChanger mc, Class<? extends ArtObject>[] classes, InputManager im, ColorManager cm, PApplet a, Loader loader) {
        super(x, y, w, h);
        modeWidth = w2;
        modeHeight = h2;
        modeX = x2;
        modeY = y2;
        modes = new ArrayList<>();
        modeChanger = mc;
        inputManager = im;
        colorManager = cm;
        this.loader = loader;
        parent = a;
        for (Class<? extends ArtObject> aClass : classes) {
            add(aClass.getSimpleName(), aClass);
        }
    }

    @Override
    public void draw() {
        for (int i=0; i<modes.size(); i++) {
            int y = yPos+20+(i*30);
            if (i != 0) {
                parent.stroke(255);
                parent.strokeWeight(1);
                parent.line(xPos, y-10, xPos+width, y-10);
            }
            parent.fill(255);
            parent.textAlign(PApplet.LEFT);
            parent.textSize(modes.get(i).isHovered()?15: 12);
            parent.text(modes.get(i).getTitle(), xPos, y+10);
        }
    }

    public <T extends ArtObject> void add(String t, Class<T> type) {
        inputManager.clearAll();
        colorManager.clearAll();
        modes.add(new Mode(t, type, loader));
        selected = modes.size()-1;
        modeChanger.changeMode(modes.get(selected));
    }

    @Override
    public boolean mouseEvent(MouseEvent e) {
        for (int i=0; i<modes.size(); i++) {
            if (modes.get(i).mouseEvent(e)) {
                if (modes.get(i).wasClicked()) {
                    modes.get(selected).setImages(modeChanger.changeMode(modes.get(i)));
                    selected = i;
                }
                return true;
            }
        }
        return false;
    }

    private int getY(Mode m) {
        return yPos+20+modes.indexOf(m)*30;
    }

    public class Mode {

        private String title;
        private ArtObject object;
        private ArrayList<GUIObject> inputElements;
        private ArrayList<GUIObject> colorElements;
        private ArrayList<ImageList.ImageData> images;


        private boolean hovered, clicked;

        <T extends ArtObject> Mode(String title, Class<T> type, Loader loader) {
            this.title = title;
            try {
                object = type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            object.init(modeX, modeY, modeWidth, modeHeight, inputManager.getInputControl(), colorManager.getColorControl(), parent, loader);
            object.init();
            object.startRenderer();
            inputElements = inputManager.clearAll();
            colorElements = colorManager.clearAll();
            images = new ArrayList<>();
        }

        public boolean mouseEvent(MouseEvent e) {
            if (e.getAction() == MouseEvent.MOVE) {
                hovered = mouseOver(e.getX(), e.getY());
                if (hovered) {
                    return true;
                }
            } else if (hovered && e.getAction() == MouseEvent.CLICK) {
                clicked = true;
                return true;
            }
            return false;
        }

        public boolean mouseOver(int mx, int my) {
            int y = getY(this);
            return (mx > xPos && mx < xPos+width && my > y && my < y+20);
        }

        boolean isHovered() {
            return hovered;
        }

        boolean wasClicked() {
            if (clicked) {
                clicked = false;
                return true;
            } else {
                return false;
            }
        }

        ArrayList<GUIObject> getInputElements() {
            return inputElements;
        }

        ArrayList<GUIObject> getColorElements() {
            return colorElements;
        }

        ArrayList<ImageList.ImageData> getImages() {
            return images;
        }

        String getTitle() {
            return title;
        }

        ArtObject getObject() {
            return object;
        }

        void setImages(ArrayList<ImageList.ImageData> images) {
            this.images = images;
        }
    }

}
