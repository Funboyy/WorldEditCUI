package de.funboyy.addon.worldedit.cui.core;

import de.funboyy.addon.worldedit.cui.api.event.WorldEditRenderEvent;
import de.funboyy.addon.worldedit.cui.api.protocol.WorldEditProtocol;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.VersionPacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.WorldEditPacket;
import de.funboyy.addon.worldedit.cui.api.render.WorldEditRenderer;
import de.funboyy.addon.worldedit.cui.api.render.pipeline.OptiFinePipelineProvider;
import de.funboyy.addon.worldedit.cui.api.render.pipeline.VanillaPipelineProvider;
import java.util.List;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent.Side;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.ServerSwitchEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.PipelineProvider;

public class WorldEditListener {

  private static final List<PipelineProvider> RENDER_PIPELINES = List.of(
      new OptiFinePipelineProvider(),
      new VanillaPipelineProvider()
  );

  private final WorldEdit controller;
  private final WorldEditRenderer renderer;
  private final WorldEditProtocol protocol;

  public WorldEditListener() {
    this.renderer = new WorldEditRenderer(RENDER_PIPELINES);

    this.controller = this.renderer.getController();
    this.controller.initialise();

    this.protocol = new WorldEditProtocol();
    this.protocol.register(this.controller);
  }

  @Subscribe
  public void handleServerJoin(final ServerJoinEvent event) {
    this.handleJoin();
  }

  @Subscribe
  public void handleServerSwitch(final ServerSwitchEvent event) {
    this.handleJoin();
  }

  @Subscribe
  public void handleSubServerSwitch(final SubServerSwitchEvent event) {
    this.handleJoin();
  }

  private void handleJoin() {
    this.controller.clear();

    this.sendPacket(new VersionPacket(WorldEditProtocol.VERSION));
  }

  @Subscribe
  public void handleWorldEditRender(final WorldEditRenderEvent event) {
    this.renderer.render(event.stack(), event.tickDelta());
  }

  @Subscribe
  public void handleNetworkPayload(final NetworkPayloadEvent event) {
    if (event.side() != Side.RECEIVE) {
      return;
    }

    final ResourceLocation identifier = event.identifier();

    if (!identifier.equals(WorldEditProtocol.IDENTIFIER) && !identifier.equals(WorldEditProtocol.LEGACY_IDENTIFIER)) {
      return;
    }

    this.protocol.handleIncoming(event.getPayload());
  }

  private void sendPacket(final WorldEditPacket packet) {
    this.protocol.sendPacket(packet);
  }

}
