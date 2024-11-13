package de.funboyy.addon.worldedit.cui.api.render;

import net.labymod.api.client.render.vertex.BufferBuilder;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface RenderHelper {

  default Object getShader() {
    return null;
  }

  default void setShader(final Object shader) {
  }

  default Object getPositionColorShader() {
    return null;
  }

  default Object getRenderTypeLinesShader() {
    return null;
  }

  void endTesselator(final BufferBuilder builder);

  default void popPose() {
  }

  default void pushPose() {
  }

  default void translate(final float x, final float y, final float z) {
  }

}
