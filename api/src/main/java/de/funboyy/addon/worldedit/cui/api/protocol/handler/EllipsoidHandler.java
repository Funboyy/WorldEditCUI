package de.funboyy.addon.worldedit.cui.api.protocol.handler;

import de.funboyy.addon.worldedit.cui.api.protocol.packet.EllipsoidPacket;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.region.Region;

public class EllipsoidHandler extends WorldEditHandler<EllipsoidPacket> {

  public EllipsoidHandler(final WorldEdit controller) {
    super(controller);
  }

  @Override
  public void handle(final EllipsoidPacket packet) {
    final Region selection = super.getController().getSelection(packet.isMulti());

    if (selection == null) {
      return;
    }

    final int id = packet.getId();

    if (id == 0) {
      selection.setEllipsoidCenter(packet.getX().intValue(), packet.getY().intValue(), packet.getZ().intValue());
    }

    else if (id == 1) {
      selection.setEllipsoidRadii(packet.getX().doubleValue(), packet.getY().doubleValue(), packet.getZ().doubleValue());
    }
  }

}
