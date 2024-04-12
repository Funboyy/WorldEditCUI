package org.enginehub.worldeditcui;

import de.funboyy.addon.worldedit.cui.api.generated.ReferenceStorage;
import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.enginehub.worldeditcui.exceptions.InitialisationException;
import org.enginehub.worldeditcui.render.SelectionProvider;
import org.enginehub.worldeditcui.render.region.CuboidRegion;
import org.enginehub.worldeditcui.render.region.Region;
import org.jetbrains.annotations.ApiStatus.Internal;

public class WorldEdit {

  private static ReferenceStorage references;

  @Internal
  public static void init(ReferenceStorage references) {
    if (WorldEdit.references != null) {
      throw new IllegalStateException("WorldEdit already initialized");
    }

    WorldEdit.references = references;
  }

  public static ReferenceStorage references() {
    return references;
  }

  private final Map<UUID, Region> regions = new LinkedHashMap<>();
  private Region selection, activeRegion;
  private SelectionProvider selectionProvider;

  public void initialise() {
    this.selection = new CuboidRegion(this);
    this.selectionProvider = new SelectionProvider(this);

    try {
      this.selection.initialise();
      this.selectionProvider.initialise();
    }
    catch (final InitialisationException exception) {
      exception.printStackTrace();
    }
  }

  public SelectionProvider getSelectionProvider() {
    return this.selectionProvider;
  }

  public void clear() {
    this.clearSelection();
    this.clearRegions();
  }

  public void clearSelection() {
    this.selection = new CuboidRegion(this);
  }

  public void clearRegions() {
    this.activeRegion = null;
    this.regions.clear();
  }

  public Region getSelection(final boolean multi) {
    return multi ? this.activeRegion : this.selection;
  }

  public void setSelection(final UUID id, final Region region) {
    if (id == null) {
      this.selection = region;
      return;
    }

    if (region == null) {
      this.regions.remove(id);
      this.activeRegion = null;
      return;
    }

    this.regions.put(id, region);
    this.activeRegion = region;
  }

  public void renderSelections(final RenderContext context) {
    if (this.selection != null) {
      this.selection.render(context);
    }

    for (final Region region : this.regions.values()) {
      region.render(context);
    }
  }

}
