package de.funboyy.addon.worldedit.cui.v1_12_2;

import de.funboyy.addon.worldedit.cui.api.render.RenderHelper;
import net.labymod.api.client.render.vertex.BufferBuilder;
import net.labymod.api.models.Implements;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

@Implements(RenderHelper.class)
public class VersionedRenderHelper implements RenderHelper {

  @Override
  public void endTesselator(final BufferBuilder builder, final int depthFunc) {
    Tessellator.getInstance().draw();
  }

  @Override
  public void popPose() {
    GlStateManager.popMatrix();
  }

  @Override
  public void pushPose() {
    GlStateManager.pushMatrix();
  }

  @Override
  public void translate(final float x, final float y, final float z) {
    GlStateManager.translate(x, y, z);
  }

}
