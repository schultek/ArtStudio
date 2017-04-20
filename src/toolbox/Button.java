package toolbox;

import processing.core.*;
import processing.event.*;

public class Button extends GUIObject {
  
  public final static int RECT = 1;
  public final static int ROUND = 2;
  
  protected String text;
  protected int col, bgCol, disCol;
  protected int textSize, style;
  protected boolean disabled;
  
  public Button(int x, int y, int w, int h) {
    this("", x, y, w, h);
  }

  public Button(String t, int x, int y, int w, int h) {
    super(x, y, w, h);
    setText(t);
    style = 1;
    col = 0;
    bgCol = 255;
    disCol = 30;
  }

  public void setText(String t) {
    text = t;
    if (text.length() > 0) {
      textSize = Math.min(height/2, width*5/3/text.length());
    }
  }  

  public void setColor(int c) {
    setColor(c, bgCol, disCol);
  }

  public void setColor(int c, int bgC) {
    setColor(c, bgC, disCol);
  }

  public void setColor(int c, int bgC, int disC) {
    col = c;
    bgCol = bgC;
    disCol = disC;
  }

  public void setStyle(int i) {
    setStyle(i, col, bgCol, disCol);
  }

  public void setStyle(int i, int c, int bgC) {
    setStyle(i, c, bgC, disCol);
  }

  public void setStyle(int i, int c, int bgC, int disC) {
    if (i == ROUND) {
      style = i;
      height = width;
    } else if (i == RECT) {
      style = i;
    }
    setColor(c, bgC, disC);
  }
  
  public void draw() {
    parent.textAlign(PConstants.CENTER, PConstants.CENTER);
    parent.strokeWeight(2);
    if (disabled) {
      parent.fill(col); 
      parent.noStroke();
      drawBody(xPos, yPos, width, height);
      parent.fill(disCol);
      drawBody(xPos, yPos, width, height);
      parent.fill(255, 255, 255);
      parent.textSize(width/6);
      parent.text("DISABLED", xPos + (width / 2), yPos + (height / 2));
    } else {
      parent.fill(pressed||hovered?col:bgCol);
      parent.stroke(pressed?bgCol:col);
      drawBody(xPos, yPos, width, height);
      parent.fill(pressed||hovered?bgCol:col);
      if (text.length() > 0) {
        parent.textSize(textSize);
        parent.text(text, xPos + (width / 2) + 1, yPos + (height / 2) - 3);
      }

    } // end of if-else
    
  }
  
  private void drawBody(int x, int y, int b, int h) {
    if (style == RECT) {
      parent.rect(x, y, b, h);
    } else if (style == ROUND) {
      parent.ellipseMode(PConstants.CORNER);
      parent.ellipse(x, y, b, h);
    }
  }

  public boolean mouseEvent(MouseEvent e) {
    if (disabled) {
      pressed = false;
      clicked = false;
      return false;
    } else {
      return super.mouseEvent(e);
    }
  }
  
  protected boolean mouseOver(int x, int y) {
    if (style == RECT) {
      return super.mouseOver(x, y);
    } else if (style == ROUND) {
      return (parent.sqrt(parent.pow(x - (xPos + (width/2)), 2) + parent.pow(y - (yPos + (width/2)), 2)) < width/2);
    } else {
      return false;
    }
  }

  public boolean isEnabled() {
    return !disabled;
  }
  
  public void disable() {
    disabled = true;
    hovered = false;
    pressed = false;
  }
  
  public void enable() {
    disabled = false;
  }
  
}
  
    