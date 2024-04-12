package org.enginehub.worldeditcui.exceptions;

public class InvalidSelectionTypeException extends RuntimeException {

  public InvalidSelectionTypeException(final String regionType, final String eventName) {
    super(String.format("Selection event %s is not supported for selection type %s", eventName, regionType));
  }

}
