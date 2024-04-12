package de.funboyy.addon.worldedit.cui.api.protocol.handler;

import de.funboyy.addon.worldedit.cui.api.protocol.packet.PolygonPacket;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.region.Region;

public class PolygonHandler extends WorldEditHandler<PolygonPacket> {

  public PolygonHandler(final WorldEdit controller) {
    super(controller);
  }

  @Override
  public void handle(final PolygonPacket packet) {
    final Region selection = super.getController().getSelection(packet.isMulti());

    if (selection == null) {
      return;
    }

    selection.addPolygon(packet.getVertices());
  }

}
