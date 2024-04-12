package org.enginehub.worldeditcui.util;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T extends Observer> {

  protected List<T> observers;

  public void addObserver(final T observer) {
    if (this.observers == null) {
      this.observers = new ArrayList<>();
    }

    this.observers.add(observer);
  }

  protected void notifyObservers() {
    if (this.observers != null) {
      for (final T observer : this.observers) {
        observer.notifyChanged(this);
      }
    }
  }

}
