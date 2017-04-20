package toolbox;

import processing.core.*;
import processing.opengl.*;
import processing.javafx.*;
import processing.event.*;
import processing.data.*;
import processing.awt.*;
import java.util.ArrayList;

public class ObjectContainer extends GUIContainer {
  
  private int bgCol;
  
  public ObjectContainer(int x, int y, int w, int h) {
    super(x, y, w, h);
    bgCol = -1;
  }

  public void setBackgroundColor(int c) {
    bgCol = c;
  }
  
  public void draw() {
    if (bgCol >= 0) {
      parent.fill(bgCol);
      parent.noStroke();
      parent.rect(xPos, yPos, width, height);
    } // end of if
    super.draw();
  }
  
}