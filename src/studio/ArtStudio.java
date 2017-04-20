package studio;

import arts.*;
import processing.core.*;
import studio.input.FilePicker;
import toolbox.*;

import java.io.File;
import java.util.ArrayList;

public class ArtStudio extends PApplet {

  Class<? extends ArtObject>[] modes = new Class[]{Circle.class, Amoeba.class, Spiray.class, SimplePattern.class, ColorEvolution.class};

  private Button snap;
  private ArtView artview;
  private ImageList snapList;

  private Toolbox toolbox;

  public void setup() {

    int sbWidth = 300;

    toolbox = new Toolbox(this);
    TabContainer sidebar = new TabContainer(0, 0, sbWidth, height, 40);
    sidebar.setColor(255, 30, 50);

    InputManager inputManager = new InputManager(0, 0, sbWidth, height - 40);
    ColorManager colorManager = new ColorManager(0, 0, sbWidth, height - 40);

    artview = new ArtView(sbWidth,0,width- sbWidth,height, inputManager, colorManager);

    snapList = new ImageList(0, 70, sbWidth, height-110, artview);
    snap = new Button("S", sbWidth /2-25, 10, 50, 50);
    snap.setStyle(Button.ROUND, 255, 40);

    ModeManager modeManager = new ModeManager(20, 20, sbWidth - 40, height - 60, sbWidth, 0, width - sbWidth, height, (mode) -> {
      ArrayList<ImageList.ImageData> images = snapList.clearImages();
      artview.updateMode(mode);
      snapList.setImages(mode.getImages());
      return images;
    }, modes, inputManager, colorManager, this, new Loader(this));
    toolbox.add(artview);
    toolbox.add(sidebar);

    sidebar.newTab("Modes");
    sidebar.add("Modes", modeManager);

    sidebar.newTab("Settings");
    sidebar.add("Settings", inputManager);

    sidebar.newTab("Snaps");
    sidebar.add("Snaps", snapList);
    sidebar.add("Snaps", snap);

    sidebar.newTab("Colors");
    sidebar.add("Colors", colorManager);

  }

  public void fileSelected(File f) {
    if (f != null) {
      ((FilePicker) toolbox.getFocused()).fileSelected(f);
    }
  }

  public void draw() {
    if (snap.wasClicked()) {
      ArtObject snapped = artview.dublicateArtObject();
      snapList.add(snapped);
    }
  }

  public void settings() {  size(1300, 800); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "studio.ArtStudio" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
