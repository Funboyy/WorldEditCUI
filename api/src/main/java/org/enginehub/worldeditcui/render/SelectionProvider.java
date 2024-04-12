package org.enginehub.worldeditcui.render;

import org.enginehub.worldeditcui.InitialisationFactory;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.exceptions.InitialisationException;
import org.enginehub.worldeditcui.render.region.Region;
import org.enginehub.worldeditcui.render.region.RegionType;

public class SelectionProvider implements InitialisationFactory {

	private final WorldEdit controller;
	
	public SelectionProvider(final WorldEdit controller) {
		this.controller = controller;
	}

	@Override
	public void initialise() throws InitialisationException {
	}
	
	public Region createSelection(final String key) {
		if ("clear".equals(key)) {
			return null;
		}

		final RegionType type = RegionType.named(key);

    if (type == null) {
      return null;
    }

    try {
      return type.make(this.controller);
    } catch (final Exception exception) {
        exception.printStackTrace();
    }

		return null;
	}
}
