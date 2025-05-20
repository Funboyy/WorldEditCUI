package de.funboyy.addon.worldedit.cui.v1_21_5;

import com.mojang.blaze3d.platform.DepthTestFunction;

public interface RenderPipelineAccessor {

  void storeDepth();

  void setDepthTestFunction(final DepthTestFunction depthFunction);

  void setDepthWrite(final boolean depthWrite);

  void restoreDepth();

}
