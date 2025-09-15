package de.funboyy.addon.worldedit.cui.v1_12_2.mixins;

import de.funboyy.addon.worldedit.cui.api.event.WorldEditRenderEvent;
import net.labymod.api.Laby;
import net.labymod.v1_12_2.client.render.matrix.VersionedStackProvider;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRender {

  @Inject(
      method = "renderWorldPass",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/renderer/GlStateManager;disableFog()V"
      )
  )
  private void worldEdit$renderWorld(
      final int phase,
      final float tickDelta,
      final long limitTime,
      final CallbackInfo callbackInfo
  ) {
    Laby.fireEvent(new WorldEditRenderEvent(VersionedStackProvider.DEFAULT_STACK, tickDelta));
  }

}
