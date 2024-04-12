package org.enginehub.worldeditcui;

import org.enginehub.worldeditcui.exceptions.InitialisationException;

public interface InitialisationFactory {

  void initialise() throws InitialisationException;

}
