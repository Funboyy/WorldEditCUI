package de.funboyy.addon.worldedit.cui.api.protocol;

import de.funboyy.addon.worldedit.cui.api.protocol.handler.ColorHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.CylinderHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.EllipsoidHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.GridHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.MinMaxHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.Point2DHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.PointHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.PolygonHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.SelectionHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.WorldEditHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.ColorPacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.CylinderPacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.EllipsoidPacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.GridPacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.MinMaxPacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.Point2DPacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.PointPacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.PolygonPacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.SelectionPacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.UpdatePacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.VersionPacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.WorldEditPacket;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import net.labymod.api.Laby;
import net.labymod.api.client.network.server.payload.PayloadRegistry;
import net.labymod.api.client.resources.ResourceLocation;
import org.enginehub.worldeditcui.WorldEdit;
import org.jetbrains.annotations.Nullable;

public class WorldEditProtocol {

  public static final int VERSION = 4;

  public static final ResourceLocation IDENTIFIER = ResourceLocation.create("worldedit", "cui");
  public static final ResourceLocation LEGACY_IDENTIFIER = ResourceLocation.create("minecraft", "wecui");
  public static final String LEGACY_CHANNEL = "WECUI";

  private final Set<Packet> packets;

  public WorldEditProtocol() {
    this.packets = new HashSet<>();
  }

  private <P extends WorldEditPacket> void registerPacket(final PacketType type, final Supplier<P> supplier, final WorldEditHandler<P> handler) {
    final WorldEditPacket anotherPacket = this.getPacket(type.getTypeId());

    if (anotherPacket != null) {
      throw new IllegalArgumentException(
          String.format(
              "The %s packet has already been registered with this %s ID.",
              anotherPacket.getClass().getName(),
              type.getTypeId()
          )
      );
    }

    this.packets.add(new Packet(type, supplier, handler));
  }

  @Nullable
  @SuppressWarnings("unchecked")
  public <P extends WorldEditPacket> P getPacket(final String id) {
    for (final Packet packet : this.packets) {
      if (packet.type().getTypeId().equalsIgnoreCase(id)) {
        return (P) packet.supplier().get();
      }
    }

    return null;
  }

  @Nullable
  @SuppressWarnings("unchecked")
  public <P extends WorldEditPacket> WorldEditHandler<P> getHandler(final Class<P> packetClass) {
    for (final Packet packet : this.packets) {
      final WorldEditHandler<?> handler = packet.handler();

      if (handler == null) {
        continue;
      }

      if (handler.getPacketClass().equals(packetClass)) {
        return (WorldEditHandler<P>) handler;
      }
    }

    return null;
  }

  public void register(final WorldEdit controller) {
    this.registerPacket(PacketType.VERSION, VersionPacket::new, null);
    this.registerPacket(PacketType.SELECTION, SelectionPacket::new, new SelectionHandler(controller));
    this.registerPacket(PacketType.POINT, PointPacket::new, new PointHandler(controller));
    this.registerPacket(PacketType.POINT2D, Point2DPacket::new, new Point2DHandler(controller));
    this.registerPacket(PacketType.ELLIPSOID, EllipsoidPacket::new, new EllipsoidHandler(controller));
    this.registerPacket(PacketType.CYLINDER, CylinderPacket::new, new CylinderHandler(controller));
    this.registerPacket(PacketType.MINMAX, MinMaxPacket::new, new MinMaxHandler(controller));
    this.registerPacket(PacketType.UPDATE, UpdatePacket::new, null);
    this.registerPacket(PacketType.POLYGON, PolygonPacket::new, new PolygonHandler(controller));
    this.registerPacket(PacketType.COLOR, ColorPacket::new, new ColorHandler(controller));
    this.registerPacket(PacketType.GRID, GridPacket::new, new GridHandler(controller));

    final PayloadRegistry registry = Laby.references().payloadRegistry();
    registry.registerPayloadChannel(IDENTIFIER);
    registry.registerPayloadChannel(LEGACY_IDENTIFIER);
  }

  public void handleIncoming(final byte[] payload) {
    final PacketMessage message = new PacketMessage(payload);
    final WorldEditPacket packet = this.getPacket(message.getTypeId());

    if (packet == null) {
      return;
    }

    try {
      packet.read(message);
    } catch (final Exception exception) {
      throw new IllegalStateException("An error occurred while parsing the packet.", exception);
    }

    packet.handle(this);
  }

  public void sendPacket(final WorldEditPacket packet) {
    Laby.references().serverController().sendPayload(IDENTIFIER, packet.write());
  }

  private record Packet(
      PacketType type,
      Supplier<? extends WorldEditPacket> supplier,
      WorldEditHandler<? extends WorldEditPacket> handler
  ) {

  }

}
