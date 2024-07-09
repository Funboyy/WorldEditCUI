package de.funboyy.addon.worldedit.cui.v1_16_5;

import com.mojang.blaze3d.vertex.Tesselator;
import de.funboyy.addon.worldedit.cui.api.render.RenderHelper;
import net.labymod.api.client.render.vertex.BufferBuilder;
import net.labymod.api.models.Implements;

@Implements(RenderHelper.class)
public class VersionedRenderHelper implements RenderHelper {

  @Override
  public void endTesselator(final BufferBuilder builder) {
    Tesselator.getInstance().end();
  }

}
