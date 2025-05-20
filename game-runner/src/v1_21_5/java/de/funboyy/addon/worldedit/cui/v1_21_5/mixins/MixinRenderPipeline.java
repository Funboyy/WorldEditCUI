package de.funboyy.addon.worldedit.cui.v1_21_5.mixins;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import de.funboyy.addon.worldedit.cui.v1_21_5.RenderPipelineAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Interface.Remap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RenderPipeline.class)
@Implements(
    @Interface(
        prefix = "accessor$",
        remap = Remap.NONE,
        iface = RenderPipelineAccessor.class
    )
)
public class MixinRenderPipeline {

  @Final
  @Shadow
  @Mutable
  private DepthTestFunction depthTestFunction;

  @Unique
  private DepthTestFunction worldEdit$storedDepthTestFunction;

  public void accessor$storeDepthTestFunction() {
    this.worldEdit$storedDepthTestFunction = this.depthTestFunction;
  }

  public void accessor$setDepthTestFunction(final DepthTestFunction depthFunction) {
    this.worldEdit$storedDepthTestFunction = this.depthTestFunction;
    this.depthTestFunction = depthFunction;
  }

  public void accessor$restoreDepthTestFunction() {
    this.depthTestFunction = this.worldEdit$storedDepthTestFunction;
  }

}
