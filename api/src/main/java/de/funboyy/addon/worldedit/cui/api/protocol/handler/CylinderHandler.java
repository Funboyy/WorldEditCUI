package de.funboyy.addon.worldedit.cui.api.protocol.handler;

import de.funboyy.addon.worldedit.cui.api.protocol.packet.CylinderPacket;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.region.Region;

public class CylinderHandler extends WorldEditHandler<CylinderPacket> {

  public CylinderHandler(final WorldEdit controller) {
    super(controller);
  }

  @Override
  public void handle(final CylinderPacket packet) {
    final Region selection = super.getController().getSelection(packet.isMulti());

    if (selection == null) {
      return;
    }

    selection.setCylinderCenter(packet.getX(), packet.getY(), packet.getZ());
    selection.setCylinderRadius(packet.getRadX(), packet.getRadZ());
  }

}
