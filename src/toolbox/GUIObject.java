package toolbox;

import processing.core.*;
import processing.event.*;

import javax.tools.Tool;

public abstract class GUIObject {
  protected int xPos, yPos, width, height;
  protected int oldX, oldY, oldX2, oldY2;
  protected PApplet parent;
  protected Toolbox toolbox;
  protected boolean resizable[] = {false, false, false, false};
  protected boolean keepPropotions = false;
  protected float propotion;

  protected boolean hovered, pressed, clicked, toggled, scrolled, scrollEnd, rightClicked, dragged, released, focused;
  protected int scrollCount;
  
  
  public GUIObject(int x, int y) {
    this(x, y, 0, 0);
  }
  
  public GUIObject(int x, int y, int w, int h) {
    oldX = x;
    oldY = y;
    oldX2 = xPos + w;
    oldY2 = yPos + h;
    width = w;
    height = h;
    xPos = x;
    yPos = y;
    propotion = (float)w / h;
  }

  public void init(PApplet p, Toolbox t) {
    parent = p;
    toolbox = t;
  }

  public boolean isInitialized() {
    return parent != null && toolbox != null;
  }
  
  public abstract void draw();
  public void keyEvent(KeyEvent e) { }


  public boolean isHovered() {
    return hovered;
  }

  public boolean isPressed() {
    return pressed;
  }

  public boolean wasClicked() {
    if (clicked) {
      clicked = false;
      return true;
    } else {
      return false;
    } // end of if-else
  }

  public boolean isToggled() {
    return toggled;
  }

  public boolean wasScrolled() {
    if (scrollEnd) {
      scrollEnd = false;
      scrolled = false;
      return true;
    } else {
      return false;
    }
  }

  public int getScrollCount() {
    int sc = scrollCount;
    scrollCount = 0;
    return sc;
  }

  public boolean wasRightClicked() {
    if (rightClicked) {
      rightClicked = false;
      return true;
    } else {
      return false;
    } // end of if-else
  }

  public boolean isDragged() {
    return dragged;
  }

  public boolean wasReleased() {
    if (released) {
      released = false;
      return true;
    } else {
      return false;
    } // end of if-else
  }

  public boolean isFocused() { return focused; }

  public void requestFocus() { toolbox.requestFocus(this); focused = true; }

  public void removeFocus() { focused = false; }

  public boolean mouseEvent(MouseEvent e) {
    hovered = mouseOver(e.getX(), e.getY());
    if (scrolled && e.getAction() != MouseEvent.WHEEL) {
      scrollEnd = true;
    }
    if (e.getAction() == MouseEvent.MOVE && hovered) {
      return true;
    } else if (e.getAction() == MouseEvent.PRESS && hovered) {
      pressed = true;
      requestFocus();
      return true;
    } else if (e.getAction() == MouseEvent.RELEASE && hovered && pressed) {
      pressed = false;
      dragged = false;
      released = true;
      if (e.getButton() == PConstants.LEFT) {
        clicked = true;
      } else if (e.getButton() == PConstants.RIGHT) {
        rightClicked = true;
      } // end of if-else
      toggled = !toggled;
      return true;
    } else if (e.getAction() == MouseEvent.DRAG && pressed) {
      dragged = true;
      return true;
    } else if (e.getAction() == MouseEvent.WHEEL && hovered) {
      scrollCount += e.getCount();
      scrolled = true;
      return true;
    } else {
      return false;
    } // end of if-else
  }

  protected boolean mouseOver(int x, int y) {
    return (x > xPos && x < xPos + width && y > yPos && y < yPos + height);
  }
  
  public void onResize(float xFactor, float yFactor) {
    if (resizable[0]) {
      xPos = (int)(oldX * xFactor);
    } // end of if     
    if (resizable[1]) {
      yPos = (int)(oldY * yFactor);
    } // end of if
    if (resizable[2]) {
      width = (int)(oldX2 * xFactor) - xPos;
      if (width < 0) {
        width = 0;
      } // end of if
    } // end of if
    if (resizable[3]) {
      if (keepPropotions) {
        if (resizable[2]) {
          height = (int)(width * (1/propotion));
          if (yPos + height > parent.height) {
            height = parent.height - yPos; 
            width = (int)(height * propotion);   
          } // end of if
        } else {
          height = (int)(oldY2 * yFactor) - yPos;
          width = (int)(height * propotion);   
          if (xPos + width > parent.width) {
            width = parent.width - xPos; 
            height = (int)(width * (1/propotion));  
          } // end of if
        } // end of if-else
      } else {
        height = (int)(oldY2 * yFactor) - yPos;
      } // end of if-else
      if (height < 0) {
        height = 0;
      } // end of if
    } // end of if
  }
  
  public void onFixSize() {
    oldX = xPos;
    oldX2 = xPos + width;
    oldY = yPos;
    oldY2 = yPos + height;
  }
  
  public void setResizable(boolean r) {
    setResizable(r, r, r, r, r);
  }
  
  public void setResizable(boolean r, boolean p) {
    setResizable(r, r, r, r, p);
  }
  
  public void setResizable(boolean r1, boolean r2, boolean p) {
    setResizable(r1, r2, r1, r2, p);
  }
  
  public void setResizable(boolean x, boolean y, boolean w, boolean h) {
    setResizable(x, y, w, h, false);
  }
  
  public void setResizable(boolean x, boolean y, boolean w, boolean h, boolean p) {
    resizable[0] = x;
    resizable[1] = y;
    resizable[2] = w;
    resizable[3] = h;
    keepPropotions = p;
  }
  
  public int getX() {
    return xPos;
  }
  
  public int getY() {
    return yPos;
  }
  
  public int getWidth() {
    return width;
  }
  
  public int getHeight() {
    return height;
  }
  
  public void setX(int x) {
    xPos = x;
    onFixSize();
  }
  
  public void setY(int y) {
    yPos = y;     
    onFixSize();
  }
  
  public void setWidth(int w) {
    width = w; 
    onFixSize();
  }
  
  public void setHeight(int h) {
    height = h; 
    onFixSize();
  }

  
  
}