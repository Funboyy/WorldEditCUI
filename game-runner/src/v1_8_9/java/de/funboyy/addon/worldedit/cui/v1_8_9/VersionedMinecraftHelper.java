package de.funboyy.addon.worldedit.cui.v1_8_9;

import de.funboyy.addon.worldedit.cui.api.MinecraftHelper;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.world.phys.hit.HitResult;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;

@Implements(MinecraftHelper.class)
public class VersionedMinecraftHelper implements MinecraftHelper {

  private final Minecraft minecraft = Minecraft.getMinecraft();

  @Override
  public HitResult pick(final Entity entity, final double distance, final float partialTicks, final boolean fluids) {
    return (HitResult) ((net.minecraft.entity.Entity) entity).rayTrace(distance, partialTicks);
  }

  @Override
  public double getPickDistance() {
    return this.minecraft.playerController.getBlockReachDistance();
  }

  @Override
  public void pushProfiler(final String name) {
    this.minecraft.mcProfiler.startSection(name);
  }

  @Override
  public void popProfiler() {
    this.minecraft.mcProfiler.endSection();
  }

}
