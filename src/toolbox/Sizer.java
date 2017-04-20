package toolbox;

import processing.core.*;
import processing.event.*;
import java.util.ArrayList;

public class Sizer extends GUIObject implements PConstants {

  public interface OnMoveListener {
    public void onStart();
    public void onMove(int pos);
    public void onStop();
  }
  
  private boolean horizontal, active;
  private ArrayList<OnMoveListener> listeners;
  private int min, max, col;
  
  public Sizer(int x, int y, int w, boolean h) {
    super(x, y, h?w:1, h?1:w);
    horizontal = h;
    listeners = new ArrayList<>();
    col = 0;
  }
  
  public void addOnMoveListener(OnMoveListener l) {
    listeners.add(l);
  }
  
  public boolean isHorizontal() {
    return horizontal;
  }

  public void setBounds(int mi, int ma) {
    min = mi;
    max = ma;
  }

  public void draw() {
    parent.stroke(col);
    parent.strokeWeight(1);
    if (horizontal) {
      parent.line(xPos, yPos, xPos + width, yPos);
    } else {                          
      parent.line(xPos, yPos, xPos, yPos + height);
    } // end of if-else
  }

  @Override
  public boolean mouseEvent(MouseEvent e) {
    hovered = mouseOver(e.getX(), e.getY());
    if (e.getAction() == MouseEvent.MOVE) {
      if (hovered) {
        parent.cursor(MOVE);
        return true;
      } else {
        parent.cursor(ARROW);
        return false;
      } // end of if-else
    } else if (hovered && e.getAction() == MouseEvent.PRESS && !active) {
      active = true;
      if (listeners.size() > 0) {
        for (int i = 0; i < listeners.size(); i++) {
          listeners.get(i).onStart();
        } // end of for
      } // end of if
      return true;
    } else if (hovered && e.getAction() == MouseEvent.DRAG) {
      dragged(e.getX(), e.getY());
      return true;
    } else if (e.getAction() == MouseEvent.RELEASE && active) {
      if (listeners.size() > 0) {
        for (int i = 0; i < listeners.size(); i++) {
          listeners.get(i).onStop();
        } // end of for
      } // end of if
      active = false;
      return true;
    } // end of if-else
    return false;
  }
  
  protected boolean mouseOver(int x, int y) {
    if (horizontal) {
      return (x > xPos && x < xPos + width && y > yPos - 5 && y < yPos + 5);
    } else {
      return (x > xPos - 5 && x < xPos + 5 && y > yPos && y < yPos + height);
    } // end of if-else
  }
  
  private void dragged(int x, int y) {
    if (horizontal) {
      if (y >= min && y <= max) {
        yPos = y;
        if (listeners.size() > 0) {
          for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onMove(yPos);
          } // end of for
        } // end of if
      } // end of if
    } else {
      if (x >= min && x <= max) {
        xPos = x;               
        if (listeners.size() > 0) {
          for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onMove(xPos);
          } // end of for
        } // end of if
      }
    } // end of if-else
  }
  
}