package de.funboyy.addon.worldedit.cui.api.protocol.handler;

import de.funboyy.addon.worldedit.cui.api.protocol.packet.MinMaxPacket;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.region.Region;

public class MinMaxHandler extends WorldEditHandler<MinMaxPacket> {

  public MinMaxHandler(final WorldEdit controller) {
    super(controller);
  }

  @Override
  public void handle(final MinMaxPacket packet) {
    final Region selection = super.getController().getSelection(packet.isMulti());

    if (selection == null) {
      return;
    }

    selection.setMinMax(packet.getMin(), packet.getMax());
  }

}
