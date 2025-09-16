package de.funboyy.addon.worldedit.cui.v1_21_1.mixins;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.funboyy.addon.worldedit.cui.api.event.WorldEditRenderEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.v1_21_1.client.util.MinecraftUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
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
  private Float worldEdit$tickDelta;

  @Inject(
      method = "renderLevel",
      at = @At("HEAD")
  )
  private void worldEdit$renderLevel(
      final DeltaTracker deltaTracker,
      final boolean renderBlockOutline,
      final Camera camera,
      final GameRenderer gameRenderer,
      final LightTexture lightTexture,
      final Matrix4f positionMatrix,
      final Matrix4f projectionMatrix,
      final CallbackInfo callbackInfo) {

    this.worldEdit$tickDelta = deltaTracker.getGameTimeDeltaPartialTick(false);
  }

  @Inject(
      method = "renderLevel",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/Options;getCloudsType()Lnet/minecraft/client/CloudStatus;"
      )
  )
  public void worldEdit$getCloudsType(final CallbackInfo callbackInfo) {
    if (this.transparencyChain == null) {
      return;
    }

    try {
      final PoseStack stack = MinecraftUtil.levelRenderContext().getPoseStack();

      RenderSystem.getModelViewStack().pushMatrix();
      RenderSystem.getModelViewStack().mul(stack.last().pose());
      RenderSystem.applyModelViewMatrix();
      this.getTranslucentTarget().bindWrite(false);

      Laby.fireEvent(new WorldEditRenderEvent(((VanillaStackAccessor) stack).stack(), this.worldEdit$tickDelta));
    } finally {
      this.minecraft.getMainRenderTarget().bindWrite(false);
      RenderSystem.getModelViewStack().popMatrix();
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
      final PoseStack stack = MinecraftUtil.levelRenderContext().getPoseStack();

      RenderSystem.getModelViewStack().pushMatrix();
      RenderSystem.getModelViewStack().mul(stack.last().pose());
      RenderSystem.applyModelViewMatrix();

      Laby.fireEvent(new WorldEditRenderEvent(((VanillaStackAccessor) stack).stack(), this.worldEdit$tickDelta));
    } finally {
      RenderSystem.getModelViewStack().popMatrix();
      RenderSystem.applyModelViewMatrix();
    }
  }

}
