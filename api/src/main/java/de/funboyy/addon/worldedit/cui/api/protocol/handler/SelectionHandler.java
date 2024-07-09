package de.funboyy.addon.worldedit.cui.api.protocol.handler;

import de.funboyy.addon.worldedit.cui.api.protocol.packet.SelectionPacket;
import java.util.UUID;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.region.Region;

public class SelectionHandler extends WorldEditHandler<SelectionPacket> {

  public SelectionHandler(final WorldEdit controller) {
    super(controller);
  }

  @Override
  public void handle(final SelectionPacket packet) {
    final Region selection = super.getController().getSelectionProvider().createSelection(packet.getShape());

    if (selection != null) {
      selection.initialise();
    }

    final UUID id = packet.getId();

    if (packet.isMulti() && selection == null && id == null) {
      super.getController().clearRegions();
      return;
    }

    super.getController().setSelection(id, selection);
  }

  @Override
  public Class<SelectionPacket> getPacketClass() {
    return SelectionPacket.class;
  }

}
