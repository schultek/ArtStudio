package studio;

import arts.ArtObject;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.event.MouseEvent;
import toolbox.GUIObject;

import java.util.ArrayList;

public class ImageList extends GUIObject {

    private ArtView view;
    private ArrayList<ImageData> data;
    private int selected = -1;

    private int scrollPos;

    ImageList(int x, int y, int w, int h, ArtView v) {
        super(x, y, w, h);
        data = new ArrayList<>();
        scrollPos = yPos;
        view = v;
    }

    public void add(ArtObject o) {
        data.add(new ImageData(o));
        selected = data.size()-1;
    }

    ArrayList<ImageData> clearImages() {
        ArrayList<ImageData> images = new ArrayList<>();
        while(data.size() > 0) {
            images.add(data.remove(0));
        }
        selected = -1;
        return images;
    }

    void setImages(ArrayList<ImageData> images) {
        data.clear();
        for (ImageData image : images) {
            data.add(image);
        }
        if (data.size() > 0) {
            select(data.get(0));
        }
    }

    private void deleteImage(ImageData img) {
        if (selected > data.indexOf(img)) {
            selected--;
        } else if (selected == data.indexOf(img)){
            selected = -1;
        }
        data.remove(img);
    }

    public void draw() {
        int y = 0;
        for (int i=0; i<data.size(); i++) {
            parent.noFill();
            if (i==selected) {
                parent.stroke(255);
            } else {
                parent.stroke(0);
            }
            data.get(i).draw(xPos+10, scrollPos+y);
            y += data.get(i).thumbnail.height+10;
        }
    }

    private void select(ImageData e) {
        selected = data.indexOf(e);
        view.switchArtObject(e.object);
    }

    public boolean mouseEvent(MouseEvent e) {
        if (e.getAction() == MouseEvent.WHEEL && data.size() > 0) {
            scrollPos -= e.getCount()*2;
            if (heightSum() > height) {
                scrollPos = PApplet.min(scrollPos, yPos);
                scrollPos = PApplet.max(scrollPos, yPos + height - heightSum());
            } else {
                scrollPos = yPos;
            }
        }
        for (ImageData aData : data) {
            if (aData.mouseEvent(e)) {
                return true;
            }
        }
        return false;
    }

    private int heightSum() {
        int sum = 0;
        for (int i = 0; i<data.size(); i++) {
            sum += data.get(i).thumbnail.height+10;
        }
        return sum;
    }


    public class ImageData {

        ArtObject object;
        PImage thumbnail;

        private int xp = 0, yp = 0;

        ImageData(ArtObject obj) {
            object = obj;
            thumbnail = getThumbnail(obj.getRenderedImage());
            obj.setThumbnailListener(result -> thumbnail = getThumbnail(result));
        }

        private PImage getThumbnail(PImage img) {
            PImage thumbnail = img.copy();
            thumbnail.resize(width-20, 0);
            return thumbnail;
        }

        public void draw(int x, int y) {
            parent.image(thumbnail, x, y, thumbnail.width, thumbnail.height);
            parent.rect(x, y, thumbnail.width, thumbnail.height);
            xp = x;
            yp = y;
        }

        public boolean mouseEvent(MouseEvent e) {
            if (e.getAction() == MouseEvent.RELEASE) {
                    if (e.getX() > xp && e.getX() < xp + thumbnail.width && e.getY() > yp && e.getY() < yp + thumbnail.height) {
                        if (e.getButton() == PConstants.LEFT) {
                            select(this);
                        } else {
                            deleteImage(this);
                        }
                        return true;
                    }
            }
            return false;
        }
    }
}