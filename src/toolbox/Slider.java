package toolbox;

import processing.core.*;
import processing.event.*;

public class Slider extends GUIObject implements PConstants, Watchable<Float> {
  
  public static final int HORIZONTAL = 1;
  public static final int VERTICAL = 2;
  public static final int CIRCLE = 3;
  
  private int col, bgCol, style;
  private float startValue, stopValue, step, value, markerStep;
  private boolean showMarkers;
  private ValueChangedListener<Float> listener;
  private long scrollTime = 0;

  public Slider(int x, int y, int w) {
    super(x, y, w, 10);
    startValue = 0;
    stopValue = 100;
    step = 1;
    markerStep = 1;
    value = 0;
    style = 1;
    col = 0;
    bgCol = 255;
  }

  public float getValue() {
    return value;
  }

  public void setMarkerStep(float s) {
    markerStep = s;
  }
  
  public void setStep(float s) {
    step = s;
  }
  
  public void showMarkers(boolean b) {
    showMarkers = b;
  }

  public void setMarkers(float s) {
    setMarkerStep(s);
    showMarkers(true);
  }
  
  public void setValue(float i) {
    value = i;
    float min = startValue;
    float max = stopValue;
    if (startValue > stopValue) {
      min = stopValue;
      max = startValue;
    }
    if (value < min) {
      value = min;
    } // end of if
    if (value > max) {
      value = max;
    } // end of if
    if (value % step != 0) {
      float s = value % step;
      if (s < step / 2) {
        value -= s;
      } else {
        value += (step - s);
      } // end of if-else
    } // end of if
  }
  
  public void setColor(int c) {
    setColor(c, bgCol);
  }

  public void setColor(int c, int bgC) {
    col = c;
    bgCol = bgC;
  }

  public void setStyle(int i) {
    setStyle(i, col, bgCol);
  }

  public void setStyle(int i, int c, int bgC) {
    setColor(c, bgC);
    if (i == HORIZONTAL || i == VERTICAL || i == CIRCLE) {
      style = i;
      if (style == HORIZONTAL && height != 10) {
        if (width == 10) {
          width = height;
        } // end of if
        height = 10;
      } // end of if
      if (style == VERTICAL && width != 10) {
        if (height == 10) {
          height = width;
        } // end of if
        width = 10;
      } // end of if
      if (style == CIRCLE && width != height) {
        if (height == 10) {
          height = width;
        } else {
          width = height;
        } // end of if-else
      } // end of if
    } // end of if
  }

  public void initValues(float val, float stp, float start, float stop) {
    setStep(stp);
    setRange(start, stop);
    setValue(val);
  }

  public void setRange(float start, float stop) {
    setStartValue(start);
    setStopValue(stop);
  }
  
  public void setStopValue(float st) {
    stopValue = st;
    if (stopValue >= startValue && value > stopValue) {
      value = stopValue;
    } // end of if
    if (stopValue < startValue && value < stopValue) {
      value = stopValue;
    } // end of if    float min = startValue;
    if (value % step != 0) {
      float s = value % step;
      if (s < step / 2) {
        value -= s;
      } else {
        value += (step - s);
      } // end of if-else
    } // end of if
  }
  
  public void setStartValue(float st) {
    startValue = st;
    if (startValue <= stopValue && value < startValue) {
      value = startValue;
    } // end of if
    if (startValue > stopValue && value > startValue) {
      value = startValue;
    } // end of if if (value % step != 0) {
    float s = value % step;
    if (s < step / 2) {
      value -= s;
    } else {
      value += (step - s);
    } // end of if-else
  }                     
  
  public void draw() {
    handleScrollDelay();
    parent.strokeWeight(2);
    parent.stroke(col);
    parent.noFill();
    if (style == HORIZONTAL) {
      parent.line(xPos, yPos, xPos + width, yPos); 
    } else if (style == VERTICAL) {
      parent.line(xPos, yPos, xPos, yPos + height); 
    } else if (style == CIRCLE) {
      parent.ellipseMode(CENTER);
      parent.ellipse(xPos,yPos, width*2, width*2);
    }
    float min = startValue;
    float max = stopValue;
    if (startValue > stopValue) {
      min = stopValue;
      max = startValue;
    }
    if (showMarkers) {
      float p = min + (min % markerStep);
      
      for (float i = p; i <= max; i += markerStep ) {
        if (style == HORIZONTAL) {
          parent.line(getXFromValue(i), yPos - 10, getXFromValue(i), yPos + 10);
        } else if (style == VERTICAL) {
          parent.line(xPos - 10, getYFromValue(i), xPos + 10, getYFromValue(i)); 
        } else if (style == CIRCLE) {
          float a = i/(max-min)*360;
          int x1 = (int)getCoordinates(xPos, yPos, width-5, a, true);
          int y1 = (int)getCoordinates(xPos, yPos, width-5, a, false);
          int x2 = (int)getCoordinates(xPos, yPos, width+5, a, true);
          int y2 = (int)getCoordinates(xPos, yPos, width+5, a, false);
          parent.line(x1, y1, x2, y2);
        } // end of for
      } // end of if
    }
    
    parent.fill(col);
    parent.ellipseMode(CENTER);
    parent.stroke(bgCol);
    int x = xPos;
    int y = yPos;
    if (style == HORIZONTAL) {
      x = getXFromValue(value);
      y = yPos;
    } else if (style == VERTICAL){
      y = getYFromValue(value);
      x = xPos;
    } else if (style == CIRCLE) {
      float a = (value-min)/(max-min)*360 + 90;
      x = (int)getCoordinates(xPos, yPos, width, a, true);
      y = (int)getCoordinates(xPos, yPos, width, a, false); 
    }
    
    if (pressed) {
      parent.ellipse(x, y, 18, 18);
    } else if (hovered) {
      parent.ellipse(x, y, 20, 20);
    } else {   
      parent.ellipse(x, y, 16, 16);
    } // end of if-else  
  }

  private void handleScrollDelay() {
    if (scrolled && scrollTime+500<System.currentTimeMillis()) {
      scrollEnd = true;
      scrolled = false;
      notifyListener(listener, value);
    }
  }
  
  public boolean mouseEvent(MouseEvent e) {
    hovered = mouseOver(e.getX(), e.getY());
    if (scrolled && e.getAction() != MouseEvent.WHEEL) {
      scrollEnd = true;
      scrolled = false;
      notifyListener(listener, value);
    }
    if (e.getAction() == MouseEvent.MOVE && hovered) {
      return true;
    } else if (!pressed && hovered && e.getAction() == MouseEvent.PRESS) {
      pressed = true;   
      if (style == HORIZONTAL) {
        value = getValueFromX(e.getX());
      } else if (style == VERTICAL){
        value = getValueFromY(e.getY());
      } else if (style == CIRCLE){
        value = getValueFromCircle(e.getX(), e.getY());
      } // end of if-else
      return true;
    } else if (pressed && e.getAction() == MouseEvent.RELEASE) {
      pressed = false;
      released = true;
      notifyListener(listener, value);
      return true;
    } else if (pressed && e.getAction() == MouseEvent.DRAG) {
      if (style == HORIZONTAL) {
        value = getValueFromX(e.getX());
      } else if (style == VERTICAL) {
        value = getValueFromY(e.getY());
      } else if (style == CIRCLE){
        value = getValueFromCircle(e.getX(), e.getY());
      } // end of if-else
      return true;
    } else if (hovered && e.getAction() == MouseEvent.WHEEL) {
      setValue(value + (e.getCount() * step));
      scrolled = true;
      scrollTime = System.currentTimeMillis();
    } // end of if-else
    return false;
  }
  
  protected boolean mouseOver(int xT, int yT) {
    if (style == HORIZONTAL) {
      return (xT > xPos - 10 && xT < xPos + width + 10 && yT > yPos - 10 && yT < yPos + 10);
    } else if (style == VERTICAL) {
      return (xT > xPos - 10 && xT < xPos + 10 && yT > yPos -10 && yT < yPos + height + 10);
    } else if (style == CIRCLE) {
      return (width < getRadius(xPos, yPos, xT, yT)+5 && width > getRadius(xPos, yPos, xT, yT)-5);
    } else {
      return false;
    }
  }

  public boolean valueChanged() {
    if (released || scrollEnd) {
      released = false;
      scrollEnd = false;
      return true;
    } else {
      return false;
    } // end of if-else
  }
  private int getXFromValue(float v) {
    return (int)(xPos + ((float)width / (stopValue - startValue)) * (v - startValue));
  }  
  
  private int getYFromValue(float v) {
    return (int)(yPos + ((float)height / (stopValue - startValue)) * (v - startValue));
  }
  
  private float getValueFromCircle(int x, int y) {
    float min = startValue;
    float max = stopValue;
    if (startValue > stopValue) {
      min = stopValue;
      max = startValue;
    }
    float a = getAngle(xPos, yPos, x, y);
    float v = ((a+270)%360)/360*(max-min) + min;  
    if (v % step != 0) {
      float s = v % step;
      if (s < step / 2) {
        v -= s;
      } else {
        v += (step - s);
      } // end of if-else
    } // end of if
    return v;
  }
  
  private float getValueFromX(float x) {
    float v = (((float)(x - xPos) * (stopValue - startValue)) / (float)width) + startValue;
    float min = startValue;
    float max = stopValue;
    if (startValue > stopValue) {
      min = stopValue;
      max = startValue;
    }
    if (v < min) {
      v = min;
    } // end of if
    if (v > max) {
      v = max;
    } // end of if
    
    if (v % step != 0) {
      float s = v % step;
      if (s < step / 2) {
        v -= s;
      } else {
        v += (step - s);
      } // end of if-else
    } // end of if
    
    return v;
  }
  
  private float getValueFromY(int y) {
    float v = (((float)(y - yPos) * (stopValue - startValue)) / (float)height) + startValue;
    float min = startValue;
    float max = stopValue;
    if (startValue > stopValue) {
      min = stopValue;
      max = startValue;
    }
    if (v < min) {
      v = min;
    } // end of if
    if (v > max) {
      v = max;
    } // end of if
    
    if (v % step != 0) {
      float s = v % step;
      if (s < step / 2) {
        v -= s;
      } else {
        v += (step - s);
      } // end of if-else
    } // end of if
    
    return v;
  }
  public float getCoordinates(float startX, float startY, float r, float a, boolean x) {
    
    if (x) {
      return (r * parent.cos(parent.radians(a))) + startX;
    } else {
      return (-r * parent.sin(parent.radians(a))) + startY;
    }
  } // getCoordinates
  public float getRadius(float startX, float startY, float endX, float endY) {
    return parent.sqrt(parent.sq(startX - endX) + parent.sq(startY - endY));
  } // getRadius
  
  public float getAngle(float startX, float startY, float endX, float endY) {
    float r = getRadius(startX, startY, endX, endY);
    if (endY - startY > 0) { 
      return parent.degrees(parent.acos((endX - startX)/(-r))) + 180;
    } else {
      return parent.degrees(parent.acos((endX - startX)/r));
    }
  } // getAngle

  @Override
  public void setListener(ValueChangedListener<Float> l) {
     listener = l;
  }

  @Override
  public void setValue(Float value) {
    setValue((float)value);
  }
}
