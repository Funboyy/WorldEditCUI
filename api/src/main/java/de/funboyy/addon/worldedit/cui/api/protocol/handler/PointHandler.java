package de.funboyy.addon.worldedit.cui.api.protocol.handler;

import de.funboyy.addon.worldedit.cui.api.protocol.packet.PointPacket;
import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.Entity;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.region.Region;

public class PointHandler extends WorldEditHandler<PointPacket> {

  public PointHandler(final WorldEdit controller) {
    super(controller);
  }

  @Override
  public void handle(final PointPacket packet) {
    final Region selection = super.getController().getSelection(packet.isMulti());

    if (selection == null) {
      return;
    }

    final int id = packet.getId();

    if (packet.isMulti() && packet.isVertexLatch()) {
      final Minecraft minecraft = Laby.labyAPI().minecraft();
      final Entity entity = minecraft.getCameraEntity();
      final double hitDistance = WorldEdit.references().minecraftHelper().getPickDistance();

      selection.setCuboidVertexLatch(id, entity, Math.min(Math.max(packet.getDistance(), hitDistance), 256.0));
      return;
    }

    selection.setCuboidPoint(id, packet.getX(), packet.getY(), packet.getZ());
  }

}
