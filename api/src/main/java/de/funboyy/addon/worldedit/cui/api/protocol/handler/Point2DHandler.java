package de.funboyy.addon.worldedit.cui.api.protocol.handler;

import de.funboyy.addon.worldedit.cui.api.protocol.packet.Point2DPacket;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.region.Region;

public class Point2DHandler extends WorldEditHandler<Point2DPacket> {

  public Point2DHandler(final WorldEdit controller) {
    super(controller);
  }

  @Override
  public void handle(final Point2DPacket packet) {
    final Region selection = super.getController().getSelection(packet.isMulti());

    if (selection == null) {
      return;
    }

    selection.setPolygonPoint(packet.getId(), packet.getX(), packet.getZ());
  }

  @Override
  public Class<Point2DPacket> getPacketClass() {
    return Point2DPacket.class;
  }

}
