package de.funboyy.addon.worldedit.cui.api;

import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.world.phys.hit.HitResult;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface MinecraftHelper {

  HitResult pick(final Entity entity, final double distance, final float partialTicks, final boolean fluids);

  float getPickDistance();

  void pushProfiler(final String name);

  void popProfiler();

}
