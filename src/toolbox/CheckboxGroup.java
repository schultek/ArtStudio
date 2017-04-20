package toolbox;

import processing.core.*;
import processing.event.*;

public class CheckboxGroup extends GUIContainer implements PConstants, Watchable<Integer> {
  
  private int checkedBox = 0;
  private ValueChangedListener<Integer> listener;

  public CheckboxGroup() {
    super(0, 0);
  }
  
  public void add(GUIObject o) {
    if (o instanceof Checkbox) {
      objects.add(o);
      if (objects.size() == 1 || o.isToggled()) {
        check(objects.indexOf(o));
      } // end of if
      if (isInitialized() && !o.isInitialized()) {
        o.init(parent, toolbox);
      }
    }
  }

  public void onResize(float xFactor, float yFactor) {
    for (int i = 0; i < objects.size() ; i++ ) {
      objects.get(i).onResize(xFactor, yFactor);
    } // end of for
  }
  
  public void setResizable(boolean x, boolean y, boolean w, boolean h, boolean p) {
    for (int i = 0; i < objects.size() ; i++ ) {
      objects.get(i).setResizable(x, y, w, h, p);
    } // end of for
  }
  
  public boolean mouseEvent(MouseEvent e) {
    for (int i = 0; i < objects.size(); i++) {
      if (objects.get(i).mouseEvent(e)) {
        if ((i != checkedBox && objects.get(i).isToggled()) || (i == checkedBox && !objects.get(i).isToggled())) {
          check(i);
          notifyListener(listener, checkedBox);
        }
        return true;
      }
    } // end of for
    return false;
  }


  @Override
  public void setListener(ValueChangedListener<Integer> l) {
    listener = l;
  }

  public void check(int i) {
    if (i >= 0 && i < objects.size()) {
      ((Checkbox)objects.get(checkedBox)).uncheck();
      ((Checkbox)objects.get(i)).check();
      checkedBox = i;
    }
  }

  @Override
  public void setValue(Integer value) {
    check(value);
  }
}