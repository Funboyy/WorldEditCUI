package de.funboyy.addon.worldedit.cui.v1_19_4;

import de.funboyy.addon.worldedit.cui.api.MinecraftHelper;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.world.phys.hit.HitResult;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;

@Implements(MinecraftHelper.class)
public class VersionedMinecraftHelper implements MinecraftHelper {

  private final Minecraft minecraft = Minecraft.getInstance();

  @Override
  public HitResult pick(final Entity entity, final double distance, final float partialTicks, final boolean fluids) {
    return (HitResult) ((net.minecraft.world.entity.Entity) entity).pick(distance, partialTicks, fluids);
  }

  @Override
  public double getPickDistance() {
    return this.minecraft.gameMode.getPickRange();
  }

  @Override
  public void pushProfiler(final String name) {
    this.minecraft.getProfiler().push(name);
  }

  @Override
  public void popProfiler() {
    this.minecraft.getProfiler().pop();
  }

}
