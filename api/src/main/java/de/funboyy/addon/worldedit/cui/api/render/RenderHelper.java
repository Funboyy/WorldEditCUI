package de.funboyy.addon.worldedit.cui.api.render;

import net.labymod.api.client.render.vertex.BufferBuilder;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface RenderHelper {

  default Object getRenderResource() {
    return null;
  }

  default void setRenderResource(final Object shader) {
  }

  default Object getQuadsRenderResource() {
    return null;
  }

  default Object getLinesRenderResource() {
    return null;
  }

  default Object getDebugLinesRenderResource() {
    return this.getQuadsRenderResource();
  }

  void endTesselator(final BufferBuilder builder, final int depthFunc);

  default void popPose() {
  }

  default void pushPose() {
  }

  default void translate(final float x, final float y, final float z) {
  }

}
