package toolbox;

import processing.core.*;
import processing.event.MouseEvent;

public class Checkbox extends Button implements Watchable<Boolean> {

  public final static int BUTTON = 3;
  private ValueChangedListener<Boolean> listener;

  public Checkbox(int x, int y) {
    this("", x, y, 30, 30, false);
  }

  public Checkbox(String t, int x, int y) {
    this(t, x, y, 30, 30, false);
  }

  public Checkbox(String t, int x, int y, boolean c) {
    this(t, x, y, 30, 30, c);
  }

  public Checkbox(String t, int x, int y, int w, int h, boolean c) {
    super(t, x, y, w, h);
    toggled = c;
    textSize = height / 2;
  }

  public void setStyle(int i, int c, int bgC, int disC) {
    super.setStyle(i, c, bgC, disC);
    if (i == RECT) {
      height = width;
    }
  }
  
  public void draw() {
    if (text.length() > 0) {
      parent.textSize(textSize);
    }
    parent.strokeWeight(2);
    
    if (pressed) {
      if (toggled) {
        parent.fill(bgCol);
        parent.stroke(col);
        drawBody(xPos + 2, yPos + 2, width - 4, height - 4);
      } else {
        parent.fill(bgCol);
        parent.stroke(col);
        drawBody(xPos + 2, yPos + 2, width - 4, height - 4);
        parent.fill(col);
        parent.noStroke();
        drawBody(xPos + 7, yPos + 7, width - 13, height - 13);
      }
    } else if ((hovered && !toggled) || (!hovered && toggled)) {
      parent.fill(bgCol);
      parent.stroke(col);
      drawBody(xPos, yPos, width, height);
      parent.fill(col);
      parent.noStroke();
      drawBody(xPos + 5, yPos + 5, width - 9, height - 9);
    } else if (!hovered & !toggled) {
      parent.fill(bgCol);
      parent.stroke(col);
      drawBody(xPos, yPos, width, height);
    } else {
      parent.fill(bgCol);
      parent.stroke(col);
      drawBody(xPos, yPos, width, height);
      parent.fill(col, 100);
      parent.noStroke();
      drawBody(xPos + 5, yPos + 5, width - 9, height - 9);
    }
    
    if (disabled) {
      parent.fill(disCol);
      parent.stroke(disCol);
      drawBody(xPos, yPos, width, height);
      if (style == BUTTON) {
        parent.fill(255, 255, 255);
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text("DISABLED", xPos + (width / 2), yPos + (height / 2));
      }
    }
  }
  
  private void drawBody(int x, int y, int b, int h) {
    if (style == RECT) {
      parent.textAlign(PConstants.LEFT, PConstants.CENTER);
      parent.rect(x, y, b, h);
      parent.fill(col);
      if (text.length() > 0) {
        parent.text(text, xPos + 35, yPos + (height / 2));
      }
    } else if (style == ROUND) {
      parent.textAlign(PConstants.LEFT, PConstants.CENTER);
      parent.ellipseMode(PConstants.CORNER);
      parent.ellipse(x, y, b, h);
      parent.fill(col);
      if (text.length() > 0) {
        parent.text(text, xPos + 35, yPos + (height / 2));
      }
    } else if (style == BUTTON) {
      parent.textAlign(PConstants.CENTER, PConstants.CENTER);
      parent.rect(x, y, b, h);
      if (text.length() > 0) {
        parent.text(text, xPos + (width / 2), yPos + (height / 2));
      }
    }
  }

  @Override
  public boolean mouseEvent(MouseEvent e) {
    boolean m = super.mouseEvent(e);
    if (wasClicked()) {
      clicked = true;
      notifyListener(listener, toggled);
    }
    return m;
  }

  protected boolean mouseOver(int x, int y) {
    if (style == BUTTON) {
      return (x > xPos && x < xPos + width && y > yPos && y < yPos + height);
    } else {
      return super.mouseOver(x, y);
    }
  }

  public void check() {
    check(true);
  }

  public void check(boolean c) {
    toggled = c;
    notifyListener(listener, toggled);
  }
  
  public void uncheck() {
    toggled = false;
    notifyListener(listener, toggled);
  }
  
  public void toggle() {
    toggled = !toggled;
    notifyListener(listener, toggled);
  }

  @Override
  public void setListener(ValueChangedListener<Boolean> l) {
     listener = l;
  }

  @Override
  public void setValue(Boolean value) {
    check(value);
  }
}