package de.funboyy.addon.worldedit.cui.core;

import de.funboyy.addon.worldedit.cui.api.WorldEditLoader;
import de.funboyy.addon.worldedit.cui.api.event.WorldEditRenderEvent;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.VersionPacket;
import de.funboyy.addon.worldedit.cui.api.protocol.packet.WorldEditPacket;
import de.funboyy.addon.worldedit.cui.api.render.WorldEditRenderer;
import de.funboyy.addon.worldedit.cui.api.render.pipeline.VanillaPipelineProvider;
import de.funboyy.addon.worldedit.cui.core.protocol.WorldEditProtocol;
import java.util.List;
import javax.inject.Singleton;
import net.labymod.api.Laby;
import net.labymod.api.client.network.server.ServerController;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.ServerSwitchEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import net.labymod.api.models.Implements;
import net.labymod.serverapi.protocol.packet.protocol.Protocol;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.PipelineProvider;

@Singleton
@Implements(WorldEditLoader.class)
public class DefaultWorldEditLoader implements WorldEditLoader {

  private static final List<PipelineProvider> RENDER_PIPELINES = List.of(
      //new OptiFinePipelineProvider(),
      new VanillaPipelineProvider()
  );

  private final WorldEdit controller;
  private final WorldEditRenderer renderer;

  private boolean visible = true;
  private Protocol protocol;

  public DefaultWorldEditLoader() {
    this.renderer = new WorldEditRenderer(RENDER_PIPELINES);

    this.controller = this.renderer.getController();
    this.controller.initialise();

    Laby.labyAPI().eventBus().registerListener(this);
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
    this.visible = true;
    this.controller.clear();

    this.sendPacket(new VersionPacket(WorldEditProtocol.VERSION));
  }

  // ToDo: this for debug because of the missing LoginPacket

  @Subscribe
  public void handleChat(final ChatMessageSendEvent event) {
    if (!event.getMessage().equals("debug")) {
      return;
    }

    event.setCancelled(true);

    final ServerController serverController = Laby.references().serverController();
    serverController.loginOrServerSwitch(serverController.getCurrentServerData());
  }

  @Subscribe
  public void handleWorldEditRender(final WorldEditRenderEvent event) {
    if (this.visible) {
      this.renderer.render(event.tickDelta());
    }
  }

  @Override
  public void registerProtocol(final Protocol protocol) {
    this.protocol = protocol;
  }

  private void sendPacket(final WorldEditPacket packet) {
    if (this.protocol == null) {
      throw new IllegalStateException("Cannot send the packet, because there was no protocol registered.");
    }

    this.protocol.sendPacket(packet);
  }

  @Override
  public WorldEdit getController() {
    return this.controller;
  }

}
