package de.funboyy.addon.worldedit.cui.v1_21_4.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.funboyy.addon.worldedit.cui.api.event.WorldEditRenderEvent;
import net.labymod.api.Laby;
import net.labymod.v1_21_4.client.util.MinecraftUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRender {

  @Shadow @Final
  private Minecraft minecraft;

  @Shadow @Final
  private LevelTargetBundle targets;

  @Shadow @Nullable
  public abstract RenderTarget getTranslucentTarget();

  @Shadow @Nullable
  protected abstract PostChain getTransparencyChain();

  @Unique
  private Float worldEdit$tickDelta;
  @Unique
  private FrameGraphBuilder worldEdit$frameGraphBuilder;

  @Inject(
      method = "renderLevel",
      at = @At("HEAD")
  )
  private void worldEdit$renderLevel(
      final GraphicsResourceAllocator graphicsResourceAllocator,
      final DeltaTracker deltaTracker,
      final boolean renderBlockOutline,
      final Camera camera,
      final GameRenderer gameRenderer,
      final Matrix4f positionMatrix,
      final Matrix4f projectionMatrix,
      final CallbackInfo callbackInfo) {

    this.worldEdit$tickDelta = deltaTracker.getGameTimeDeltaPartialTick(false);
  }

  @Redirect(
      method = "renderLevel",
      at = @At(
          value = "NEW",
          target = "()Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;"
      )
  )
  private FrameGraphBuilder worldEdit$storeFrameGraphBuilder() {
    final FrameGraphBuilder frameGraphBuilder = new FrameGraphBuilder();

    this.worldEdit$frameGraphBuilder = frameGraphBuilder;

    return frameGraphBuilder;
  }

  @Inject(
      method = "renderLevel",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/Options;getCloudsType()Lnet/minecraft/client/CloudStatus;"
      )
  )
  public void worldEdit$getCloudsType(final CallbackInfo callbackInfo) {
    if (this.getTransparencyChain() == null) {
      return;
    }

    final FramePass pass = this.worldEdit$frameGraphBuilder.addPass("afterTranslucent");
    this.targets.main = pass.readsAndWrites(this.targets.main);

    pass.executes(() -> {
      try {
        final PoseStack stack = MinecraftUtil.levelRenderContext().getPoseStack();

        RenderSystem.getModelViewStack().pushMatrix();
        RenderSystem.getModelViewStack().mul(stack.last().pose());

        this.getTranslucentTarget().bindWrite(false);

        Laby.fireEvent(new WorldEditRenderEvent(this.worldEdit$tickDelta));
      } finally {
        this.minecraft.getMainRenderTarget().bindWrite(false);
        RenderSystem.getModelViewStack().popMatrix();
      }
    });
  }

  @WrapOperation(
      method = "addMainPass",
      at = @At(
          value = "INVOKE",
          target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V"
      )
  )
  private void worldEdit$renderLast(final FramePass framePass, final Runnable task, final Operation<Void> original) {
    Runnable wrappedTask = () -> {
      task.run();

        if (this.getTransparencyChain() != null) {
        return;
      }

      try {
        final PoseStack stack = MinecraftUtil.levelRenderContext().getPoseStack();

        RenderSystem.getModelViewStack().pushMatrix();
        RenderSystem.getModelViewStack().mul(stack.last().pose());

        Laby.fireEvent(new WorldEditRenderEvent(this.worldEdit$tickDelta));
      } finally {
        RenderSystem.getModelViewStack().popMatrix();
      }
    };

    original.call(framePass, wrappedTask);
  }

}
