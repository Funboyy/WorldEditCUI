package org.enginehub.worldeditcui.util;

public interface Observer {

  void notifyChanged(final Observable<?> source);

}
