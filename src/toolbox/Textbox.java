package toolbox;

import processing.core.*;
import processing.event.*;

public class Textbox extends GUIObject implements PConstants {
  
  private int col, bgCol, hlCol;
  private String content = "";
  private boolean enterPressed;
  private int cursor = 0, startPos = 0;
  
  public Textbox(int x, int y, int w, int h) {
    super(x, y, w, h);
    col = 0;
    bgCol = 230;
    hlCol = 255;
  }

  public void keyEvent(KeyEvent e) {
    parent.textSize(20);
    if (e.getAction() == KeyEvent.PRESS) {
      if (e.getKeyCode() == 37 && cursor > 0) {
        cursor--;
        if (cursor < startPos) {
          startPos--;
        } // end of if
      } // end of if
      if (e.getKeyCode() == 39 && cursor < content.length()) {
        cursor++;
        while (parent.textWidth(content.substring(startPos, cursor)) > width - 10) {  
          startPos++;
        } // end of if
      } // end of if
      if (e.getKeyCode() == 35) {
        cursor = content.length();
        while (parent.textWidth(content.substring(startPos, cursor)) > width - 10) {  
          startPos++;
        } // end of if
      } // end of if
      if (e.getKeyCode() == 36) {
        cursor = 0;
        startPos = 0;
      } // end of if
    } // end of if
    if (focused && e.getAction() == KeyEvent.TYPE) {
      if (e.getKey() == ENTER) {
        focused = false;
        enterPressed = true;
      } else if (e.getKey() == BACKSPACE) { 
        if (cursor > 0) {
          if (cursor < content.length()) {
            String a = content.substring(0, cursor - 1);
            String b = content.substring(cursor);
            content = a + b;
          } else {  
            content = content.substring(0, content.length() - 1);   
          } // end of if-else
          cursor--;
          if (cursor < startPos) {
            startPos--;
          } // end of if
        } // end of if
      } else if (e.getKey() == DELETE) {
        if (cursor < content.length()) {
          String a = content.substring(0, cursor);
          String b = content.substring(cursor + 1);
          content = a + b;
        }
      } else {
        String a = content;
        String b = "";
        if (cursor < content.length()) {    
          a = content.substring(0, cursor);
          b = content.substring(cursor);
        }
        content = a + e.getKey() + b;
        cursor++;
        while (parent.textWidth(content.substring(startPos, cursor)) > width - 10) {
          startPos++;
        } 
      } // end of if
    }
  }
  
  
  public boolean mouseEvent(MouseEvent e) {
    hovered = mouseOver(e.getX(), e.getY());
    if (e.getAction() == MouseEvent.MOVE) {
      parent.cursor(hovered?TEXT:ARROW);
      if (hovered) {
        return true;
      } // end of if
    } else if (e.getAction() == MouseEvent.PRESS) {
      if (hovered) {
        return true;
      } // end of if
    } else if (e.getAction() == MouseEvent.RELEASE) {
      clicked(e.getX(), e.getY());
      if (focused) {
        return true;
      } // end of if
    } else if (focused && e.getAction() == MouseEvent.WHEEL) {
      cursor += e.getCount();
      if (cursor < 0) {
        cursor = content.length();
      } // end of if
      if (cursor > content.length()) {
        cursor = 0;
      }
      while (cursor < startPos) {
        startPos--;
      } // end of if
      while (parent.textWidth(content.substring(startPos, cursor)) > width - 10) {  
        startPos++;
      } // end of if
    }
    return false;
  }
  
  public void onResize(float xFactor, float yFactor) {
    super.onResize(xFactor, yFactor);
    while (parent.textWidth(content.substring(startPos, cursor)) > width - 30) {  
      startPos++;
    } // end of if
  }
  
  private void clicked(int x, int y) {
    if (hovered) {
      focused = true;
      int dX = x - 5 - xPos;
      int minD = 100;
      int ind = 0;
      parent.textSize(20);
      for (int i = 0; i <= content.substring(startPos).length() ;i++ ) {
        int d = (int)parent.abs(parent.textWidth(content.substring(startPos, startPos + i)) - dX);
        if (d < minD) {
          minD = d;
          ind = i;
        } // end of if
      } // end of for
      cursor = startPos + ind;
    } else {
      focused = false;
    } // end of if-else
  }
  
  public boolean wasEnterPressed() {
    if (enterPressed) {
      enterPressed = false;
      return true;
    } else {
      return false;
    } // end of if-else
  }
  
  public void draw() {
    parent.textSize(20);
    parent.textAlign(LEFT, CENTER);
    parent.noStroke();
    parent.fill(bgCol);
    parent.rect(xPos, yPos, width, height);
    parent.stroke(hlCol);
    parent.strokeWeight(2);
    parent.line(xPos, yPos - 2, xPos, yPos + height + 2);
    parent.line(xPos + width, yPos - 2, xPos + width, yPos + height + 2);
    parent.fill(col);
    parent.strokeWeight(1);
    int x = xPos + 5 + (int)parent.textWidth(content.substring(startPos, cursor));
    parent.text(content.substring(startPos), xPos + 5, yPos, width - 10, height);
    if (focused && parent.millis() % 800 < 300) {
      parent.line(x, yPos + 5, x, yPos + height - 5);
    } // end of if
    
  }
  
  public String getContent() {
    return content;
  }
  
  public void setContent(String c) {
    content = c;
    cursor = 0;
    startPos = 0;
  }
  
  public void setColor(int c) {

    setColor(c, bgCol, hlCol);
  }

  public void setColor(int c, int bgC) {
    setColor(c, bgC, hlCol);
  }

  public void setColor(int c, int bgC, int hlC) {
    col = c;
    bgCol = bgC;
    hlCol = hlC;
  }

}
