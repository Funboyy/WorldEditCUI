package de.funboyy.addon.worldedit.cui.v26_2.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.funboyy.addon.worldedit.cui.api.event.WorldEditRenderEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.v26_2.client.util.MinecraftUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRender {

  @Final
  @Shadow
  private LevelTargetBundle targets;

  @Nullable
  @Shadow
  protected abstract PostChain getTransparencyChain();

  @Unique
  private Float worldEdit$tickDelta;
  @Unique
  private FrameGraphBuilder worldEdit$frameGraphBuilder;

  @Inject(
      method = "render",
      at = @At("HEAD")
  )
  private void worldEdit$renderLevel(
      final GraphicsResourceAllocator graphicsResourceAllocator,
      final DeltaTracker deltaTracker,
      final boolean renderBlockOutline,
      final CameraRenderState cameraState,
      final Matrix4fc modelViewMatrix,
      final GpuBufferSlice fogBuffer,
      final Vector4f fogColor,
      final boolean renderSky,
      final CallbackInfo callbackInfo) {

    this.worldEdit$tickDelta = deltaTracker.getGameTimeDeltaPartialTick(false);
  }

  @ModifyVariable(
      method = "render",
      at = @At("STORE")
  )
  private FrameGraphBuilder worldEdit$storeFrameGraphBuilder(final FrameGraphBuilder frameGraphBuilder) {
    this.worldEdit$frameGraphBuilder = frameGraphBuilder;

    return frameGraphBuilder;
  }

  @Inject(
      method = "render",
      at = @At(
          value = "FIELD",
          target = "Lnet/minecraft/client/renderer/state/OptionsRenderState;cloudStatus:Lnet/minecraft/client/CloudStatus;",
          opcode = Opcodes.GETFIELD
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

        Laby.fireEvent(new WorldEditRenderEvent(((VanillaStackAccessor) stack).stack(), this.worldEdit$tickDelta));
      } finally {
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

        Laby.fireEvent(new WorldEditRenderEvent(((VanillaStackAccessor) stack).stack(), this.worldEdit$tickDelta));
      } finally {
        RenderSystem.getModelViewStack().popMatrix();
      }
    };

    original.call(framePass, wrappedTask);
  }

}
