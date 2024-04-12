package de.funboyy.addon.worldedit.cui.api.protocol.handler;

import de.funboyy.addon.worldedit.cui.api.protocol.packet.GridPacket;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.RenderStyle.RenderType;
import org.enginehub.worldeditcui.render.region.Region;

public class GridHandler extends WorldEditHandler<GridPacket> {

  public GridHandler(final WorldEdit controller) {
    super(controller);
  }

  @Override
  public void handle(final GridPacket packet) {
    if (!packet.isMulti()) {
      throw new IllegalStateException("GRID packet is not valid for non-multi selections");
    }

    final Region selection = super.getController().getSelection(true);

    if (selection == null) {
      return;
    }

    selection.setGridSpacing(packet.getSpacing());

    if ("cull".equalsIgnoreCase(packet.getRenderType())) {
      selection.setRenderType(RenderType.VISIBLE);
      return;
    }

    selection.setRenderType(RenderType.ANY);
  }

}
