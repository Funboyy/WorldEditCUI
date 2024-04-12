package de.funboyy.addon.worldedit.cui.v1_8_9;

import de.funboyy.addon.worldedit.cui.api.render.RenderHelper;
import net.labymod.api.models.Implements;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

@Implements(RenderHelper.class)
public class VersionedRenderHelper implements RenderHelper {

  @Override
  public void endTesselator() {
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
