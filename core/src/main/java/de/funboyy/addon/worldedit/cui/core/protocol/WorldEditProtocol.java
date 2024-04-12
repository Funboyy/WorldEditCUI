package de.funboyy.addon.worldedit.cui.core.protocol;

import de.funboyy.addon.worldedit.cui.api.WorldEditLoader;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.ColorHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.CylinderHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.EllipsoidHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.GridHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.MinMaxHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.Point2DHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.PointHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.PolygonHandler;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.SelectionHandler;
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
import java.util.HashMap;
import java.util.Map;
import net.labymod.api.Laby;
import net.labymod.serverapi.protocol.api.ProtocolApiBridge;
import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.packet.protocol.Protocol;
import net.labymod.serverapi.protocol.packet.protocol.ProtocolService;
import net.labymod.serverapi.protocol.packet.protocol.execption.ProtocolException;
import net.labymod.serverapi.protocol.payload.identifier.PayloadChannelIdentifier;
import org.enginehub.worldeditcui.WorldEdit;
import org.jetbrains.annotations.NotNull;

public class WorldEditProtocol extends Protocol {

  public static final int VERSION = 4;

  public static final PayloadChannelIdentifier IDENTIFIER = PayloadChannelIdentifier.create("worldedit", "cui");
  public static final PayloadChannelIdentifier LEGACY_IDENTIFIER = PayloadChannelIdentifier.create("minecraft", "wecui");
  public static final String LEGACY_CHANNEL = "WECUI";

  private final ProtocolService service;
  private final Map<String, WorldEditPacket> packets;

  public WorldEditProtocol() {
    super(IDENTIFIER, LEGACY_IDENTIFIER);

    this.service = ProtocolApiBridge.getProtocolApi().getProtocolService();
    this.packets = new HashMap<>();

    this.registerPacket(new VersionPacket());
    this.registerPacket(new SelectionPacket());
    this.registerPacket(new PointPacket());
    this.registerPacket(new Point2DPacket());
    this.registerPacket(new EllipsoidPacket());
    this.registerPacket(new CylinderPacket());
    this.registerPacket(new MinMaxPacket());
    this.registerPacket(new UpdatePacket());
    this.registerPacket(new PolygonPacket());
    this.registerPacket(new ColorPacket());
    this.registerPacket(new GridPacket());
  }

  private void registerPacket(final WorldEditPacket packet) {
    final String id = packet.getType().getTypeId();
    final Packet anotherPacket = this.packets.get(id);

    if (anotherPacket != null) {
      throw new ProtocolException(
          String.format(
              "The %s packet has already been registered with this %s ID.",
              anotherPacket.getClass().getName(),
              id
          )
      );
    }

    this.packets.put(id, packet);
  }

  public void register() {
    this.service.registerCustomProtocol(this);
    this.service.registerTranslationListener(new WorldEditTranslationListener(this));

    final WorldEditLoader loader = WorldEdit.references().worldEditLoader();
    final WorldEdit controller = loader.getController();

    loader.registerProtocol(this);

    this.service.registerPacketHandler(SelectionPacket.class, new SelectionHandler(controller));
    this.service.registerPacketHandler(PointPacket.class, new PointHandler(controller));
    this.service.registerPacketHandler(Point2DPacket.class, new Point2DHandler(controller));
    this.service.registerPacketHandler(EllipsoidPacket.class, new EllipsoidHandler(controller));
    this.service.registerPacketHandler(CylinderPacket.class, new CylinderHandler(controller));
    this.service.registerPacketHandler(MinMaxPacket.class, new MinMaxHandler(controller));
    // UpdatePacket does nothing so no handler needed
    this.service.registerPacketHandler(PolygonPacket.class, new PolygonHandler(controller));
    this.service.registerPacketHandler(ColorPacket.class, new ColorHandler(controller));
    this.service.registerPacketHandler(GridPacket.class, new GridHandler(controller));

    Laby.references().payloadRegistry().registerPayloadChannel(this);
  }

  @Override
  public Packet readPacket(final byte @NotNull [] payload) {
    final PacketMessage message = new PacketMessage(payload);
    final WorldEditPacket packet = this.packets.get(message.getTypeId());

    if (packet == null) {
      return null;
    }

    try {
      packet.handleIncomingPayload(message);
    } catch (final Exception exception) {
      throw new ProtocolException("An error occurred while parsing the packet.", exception);
    }

    return packet;
  }

  @Override
  public void sendPacket(@NotNull final Packet packet) {
    if (!(packet instanceof WorldEditPacket worldEditPacket)) {
      super.sendPacket(packet);
      return;
    }

    this.service.sendPacket(IDENTIFIER, worldEditPacket.translateOutgoingPayload());
  }

}
