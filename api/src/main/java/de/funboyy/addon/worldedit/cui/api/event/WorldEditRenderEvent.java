package de.funboyy.addon.worldedit.cui.api.event;

import net.labymod.api.event.Event;

public class WorldEditRenderEvent implements Event {

  private final float tickDelta;

  public WorldEditRenderEvent(final float tickDelta) {
    this.tickDelta = tickDelta;
  }

  public float tickDelta() {
    return this.tickDelta;
  }

}
