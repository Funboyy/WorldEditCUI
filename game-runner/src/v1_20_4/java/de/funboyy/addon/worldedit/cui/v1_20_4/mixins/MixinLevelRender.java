package de.funboyy.addon.worldedit.cui.v1_20_4.mixins;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.funboyy.addon.worldedit.cui.api.event.WorldEditRenderEvent;
import net.labymod.api.Laby;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.PostChain;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRender {


  @Shadow @Nullable
  private PostChain transparencyChain;
  @Shadow @Final
  private Minecraft minecraft;

  @Shadow @Nullable
  public abstract RenderTarget getTranslucentTarget();

  @Unique
  private PoseStack worldEdit$stack;
  @Unique
  private Float worldEdit$tickDelta;
  @Unique
  private boolean worldEdit$didRenderParticles = false;

  @Inject(
      method = "renderLevel",
      at = @At("HEAD")
  )
  private void worldEdit$renderLevel(
      final PoseStack stack,
      final float tickDelta,
      final long limitTime,
      final boolean renderBlockOutline,
      final Camera camera,
      final GameRenderer gameRenderer,
      final LightTexture lightTexture,
      final Matrix4f matrix,
      final CallbackInfo callbackInfo) {

    this.worldEdit$stack = stack;
    this.worldEdit$tickDelta = tickDelta;
  }

  // would to inject when ParticleEngine#render gets called, but that would be incompatible with OptiFine
  @ModifyArg(
      method = "renderLevel",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V"
      )
  )
  private String worldEdit$popPush(final String name) {
    if (name.equals("particles")) {
      this.worldEdit$didRenderParticles = true;
    }

    return name;
  }

  @Inject(
      method = "renderLevel",
      at = @At(
          value = "INVOKE",
          target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V"
      )
  )
  private void worldEdit$pushPose(final CallbackInfo callbackInfo) {
    if (this.worldEdit$didRenderParticles) {
      this.worldEdit$didRenderParticles = false;

      if (this.transparencyChain == null) {
        return;
      }

      try {
        RenderSystem.getModelViewStack().pushPose();
        RenderSystem.getModelViewStack().mulPoseMatrix(this.worldEdit$stack.last().pose());
        RenderSystem.applyModelViewMatrix();
        this.getTranslucentTarget().bindWrite(false);

        Laby.fireEvent(new WorldEditRenderEvent(this.worldEdit$tickDelta));
      } finally {
        this.minecraft.getMainRenderTarget().bindWrite(false);
        RenderSystem.getModelViewStack().popPose();
      }
    }
  }

  @Inject(
      method = "renderLevel",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/renderer/LevelRenderer;renderDebug(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/Camera;)V"
      )
  )
  private void worldEdit$renderDebug(final CallbackInfo callbackInfo) {
    if (this.transparencyChain != null) {
      return;
    }

    try {
      RenderSystem.getModelViewStack().pushPose();
      RenderSystem.getModelViewStack().mulPoseMatrix(this.worldEdit$stack.last().pose());
      RenderSystem.applyModelViewMatrix();

      Laby.fireEvent(new WorldEditRenderEvent(this.worldEdit$tickDelta));
    } finally {
      RenderSystem.getModelViewStack().popPose();
      RenderSystem.applyModelViewMatrix();
    }
  }

}
