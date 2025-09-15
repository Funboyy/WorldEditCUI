package de.funboyy.addon.worldedit.cui.api.event;

import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.event.Event;

public class WorldEditRenderEvent implements Event {

  private final Stack stack;
  private final float tickDelta;

  public WorldEditRenderEvent(final Stack stack, final float tickDelta) {
    this.stack = stack;
    this.tickDelta = tickDelta;
  }

  public Stack stack() {
    return this.stack;
  }

  public float tickDelta() {
    return this.tickDelta;
  }

}
