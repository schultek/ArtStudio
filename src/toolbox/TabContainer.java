package toolbox;

import processing.core.*;
import processing.event.*;
import java.util.ArrayList;

public class TabContainer extends GUIContainer implements PConstants {

  private int selectedTab = 0;
  private int col, bgCol, mCol, menuHeight, tabWidth = 70, minTabWidth = 30;
  private int hovered = -1;
  
  
  public TabContainer(int x, int y, int w, int h, int hM) {
    super(x, y, w, h);
    menuHeight = hM;
    col = 0;
    bgCol = 255;
    mCol = 100;
  }

  public void setTabWidth(int w) {
    tabWidth = w;
  }
  
  public void setMinTabWidth(int w) {
    minTabWidth = w;
  }
  
  public void setMenuHeight(int h) {
    menuHeight = h;
  }

  public void setMenuStyle(int tabW, int minTabW, int menuH) {
    tabWidth = tabW;
    minTabWidth = minTabW;
    menuHeight = menuH;
  }
  
  public void setColor(int c) {
    setColor(c, bgCol, mCol);
  }

  public void setColor(int c, int bgC) {
    setColor(c, bgC, mCol);
  }

  public void setColor(int c, int bgC, int mC) {
    col = c;
    bgCol = bgC;
    mCol = mC;
  }
  
  public void newTab(String n) {
    objects.add(new Tab(n));
    objects.get(objects.size()-1).init(parent, toolbox);
  }
  
  public void removeTab(String n) {
    int i = getTabIndex(n);
    if (i>=0) {
      objects.remove(i);
    }
  }
  
  public int getSelectedTab() {
    return selectedTab;
  }

  public GUIContainer getTabObject(String n) {
    return (Tab)objects.get(getTabIndex(n));
  }
  
  public int getTabCount() {
    return objects.size();
  }
  
  public void chooseTab(int i) {
    selectedTab = i;
    if (selectedTab < 0) {
      selectedTab = objects.size() - 1;
    } // end of if
    if (selectedTab >= objects.size()) {
      selectedTab = 0;
    } // end of if
  }

  public void add(GUIObject o) {
    add(((Tab)objects.get(objects.size()-1)).name, o);
  }
  
  public void add(String n, GUIObject o) {
    int i = getTabIndex(n);
    if (i >= 0) {
      ((Tab) objects.get(i)).add(o);
      if (isInitialized() && !o.isInitialized()) {
        o.init(parent, toolbox);
      }
    }
  }

  private int getTabIndex(String n) {
    for (int i=0; i<objects.size(); i++) {
      if (n.equals(((Tab)objects.get(i)).name)) {
        return i;
      }
    }
    return -1;
  }
  
  public void draw() {
    parent.noStroke();
    parent.fill(bgCol);
    parent.rect(xPos, yPos + menuHeight, width, height - menuHeight);
    if (onDrawListener != null) {                 
      onDrawListener.draw(xPos, yPos, width, height);
    }
    parent.translate(xPos, yPos + menuHeight);
    if (selectedTab < objects.size()) {
      objects.get(selectedTab).draw();
    } // end of if
    parent.translate(-xPos, -yPos - menuHeight); 
    parent.noStroke();
    parent.fill(mCol);
    parent.rect(xPos, yPos, width, menuHeight);
    parent.textAlign(CENTER, CENTER);
    int tabW = tabWidth;
    int menuW = objects.size() * tabW;
    if (menuW > width) {
      tabW = width / objects.size();
      if (tabW < minTabWidth) {
        tabW = minTabWidth;
      } // end of if
    } // end of if
    for (int i = 0; i < objects.size() ; i++ ) {
      if (i == hovered && i != selectedTab) {
        parent.textSize(11);
      } else {
        parent.textSize(12);
      } // end of if-else
      parent.fill(col);
      int x = xPos + (i * tabW);
      int x2 = x + tabW;
      if (x < xPos + width) {
        if (x2 < xPos + width) {
          if (i == selectedTab) {
            parent.rect(x, yPos + menuHeight - 2, tabW, 2); 
          } // end of if
          parent.text(((Tab)objects.get(i)).name, x, yPos + (menuHeight/4), tabW, menuHeight/2);
        } else {        
          if (i == selectedTab) {
            parent.rect(x, yPos + menuHeight - 2, (xPos + width) - x, 2); 
          } // end of if
          parent.text(((Tab)objects.get(i)).name, x, yPos + (menuHeight/4), (xPos + width) - x, menuHeight/2);
        } // end of if-else
      }
    } // end of for 
  }
  
  public boolean mouseEvent(MouseEvent e) {
    if (e.getAction() == MouseEvent.MOVE) {
      mouseOver(e.getX(), e.getY());
      if (hovered > -1) {
        return true;
      } // end of if
    } else if (hovered > -1 && e.getAction() == MouseEvent.RELEASE) {
      selectedTab = hovered;
      return true;
    } else if (isBarHovered(e.getX(), e.getY()) && e.getAction() == MouseEvent.WHEEL) {
      chooseTab(selectedTab - e.getCount());
    }       
    MouseEvent eNew = new MouseEvent(e.getNative(), e.getMillis(), e.getAction(), e.getModifiers(), e.getX() - xPos, e.getY() - yPos - menuHeight, e.getButton(), e.getCount());
    if (selectedTab < objects.size()) {  
      if (objects.get(selectedTab).mouseEvent(eNew)) {
        return true;
      }
    }   
    return false;
  }

  private boolean isBarHovered(int x, int y) {
    return x > xPos && x < xPos + width && y > yPos && y < yPos + menuHeight;
  }
  
  protected boolean mouseOver(int x, int y) {
    for (int i = 0; i < objects.size() ; i++ ) {
      if (x > xPos + (i * 70) && x < xPos + (i * 70) + 70 && y > yPos && y < yPos + menuHeight) {
        hovered = i;
        return true;
      } // end of if
    } // end of for
    hovered = -1;
    return false;
  }
  
  protected class Tab extends GUIContainer implements PConstants {
    
    private String name;
    private ArrayList<GUIObject> objects;
    
    public Tab(String n) {
      super(0, 0);
      name = n;
      objects = new ArrayList<GUIObject>();
    }

    public void onResize(float xFactor, float yFactor) {
      for (int i = 0; i < objects.size() ; i++ ) {
        objects.get(i).onResize(xFactor, yFactor);
      } // end of for
    }
    
    public void onFixSize() {
      for (int i = 0; i < objects.size() ; i++ ) {
        objects.get(i).onFixSize();
      } // end of for
    }
    
    public void setResizable(boolean x, boolean y, boolean w, boolean h, boolean p) {
      for (int i = 0; i < objects.size() ; i++ ) {
        objects.get(i).setResizable(x, y, w, h, p);
      } // end of for
    }

  }
  
  
  
}