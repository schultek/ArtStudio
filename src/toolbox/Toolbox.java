package toolbox;

import processing.core.*;
import processing.event.*;
import java.util.ArrayList;

public class Toolbox implements PConstants {
  PApplet parent;

  private int oldWidth, oldHeight, initWidth, initHeight;
  private long resizeMillis = 0;
  private ArrayList<GUIObject> objects = new ArrayList<>();
  private ArrayList<Boolean> inBackground = new ArrayList<>();
  private GUIObject focusedObject;

  public Toolbox(PApplet parent) {
    this.parent = parent;
    parent.registerMethod("pre", this);
    parent.registerMethod("draw", this);
    parent.registerMethod("mouseEvent", this);
    parent.registerMethod("keyEvent", this);
    parent.registerMethod("dispose", this);
    oldWidth = initWidth = parent.width;
    oldHeight = initHeight = parent.height;
  }
  public void setResizeable(boolean r) {
    parent.getSurface().setResizable(r);
  }

  public void add(GUIObject o) {
    add(o, false);
  }

  public void add(GUIObject o, boolean toBackground) {
    objects.add(o);
    inBackground.add(toBackground);
    o.init(parent, this);
  }

  public void sendToBackground(GUIObject o) {
    if (objects.contains(o)) {
      inBackground.set(objects.indexOf(o), true);
    }
  }

  public void sendToForeground(GUIObject o) {
    if (objects.contains(o)) {
      inBackground.set(objects.indexOf(o), false);
    }
  }
  
  public void clear() {
    objects.clear();
  }

  public void sendToFront(GUIObject o) {
    int i = objects.indexOf(o);
    if (i != -1) {
      objects.remove(i);
      objects.add(0, o);
    } // end of if
  }
  
  public void sendToBack(GUIObject o) {
    int i = objects.indexOf(o);
    if (i != -1) {
      objects.remove(i);
      objects.add(o);
    } // end of if
    
  }
  
  public void draw() {
    for (int i = 0; i < objects.size(); i++) {
      if (!inBackground.get(i)) {
        objects.get(i).draw();
      }
    }
  }
  
  public void pre() {
    int pW = parent.width;
    int pH = parent.height;
    if (pW != oldWidth || pH != oldHeight) {
      resizeMillis = parent.millis();
    }
    if (resizeMillis + 1000 > parent.millis()) {
      float xFactor = (float)parent.width / initWidth;
      float yFactor = (float)parent.height / initHeight;
      for (int i = 0; i < objects.size() ; i++ ) {
        objects.get(i).onResize(xFactor, yFactor);
      } // end of for   
    } else if (initWidth != oldWidth) {
      initWidth = oldWidth;
      initHeight = oldHeight;
      for (int i = 0; i < objects.size() ; i++ ) {
        objects.get(i).onFixSize();
      } // end of for  
    } // end of if-else
    
    oldWidth = pW;
    oldHeight = pH;
    
    for (int i = 0; i < objects.size(); i++) {
      if (inBackground.get(i)) {
        objects.get(i).draw();
      }
    }
  }
  
  public void mouseEvent(MouseEvent e) {  
    for (int i = objects.size() - 1; i >= 0; i--) {
      if (objects.get(i).mouseEvent(e)) {
        break;
      }
    }
  }  
  
  public void keyEvent(KeyEvent e) {
    if (focusedObject != null) {
      focusedObject.keyEvent(e);
    }
  }

  public void requestFocus(GUIObject o) {
    if (focusedObject != null) {
      focusedObject.removeFocus();
    }
    focusedObject = o;
  }
  
  public void dispose() {
    // Anything in here will be called automatically when 
    // the parent sketch shuts down. For instance, this might
    // shut down a thread used by this library.
  }

  public GUIObject getFocused() {
    return focusedObject;
  }
}