package org.enginehub.worldeditcui.render.points;

import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.world.phys.hit.BlockHitResult;
import net.labymod.api.client.world.phys.hit.HitResult;
import net.labymod.api.util.math.vector.FloatVector3;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.util.Vector3;

public class PointCubeTracking extends PointCube {

  private final Entity entity;
  private final double traceDistance;
  private int lastX, lastY, lastZ;

  public PointCubeTracking(final Entity entity, final double traceDistance) {
    super(0, 0, 0);
    this.entity = entity;
    this.traceDistance = traceDistance;
  }

  @Override
  public boolean isDynamic() {
    return true;
  }

  @Override
  public Vector3 getPoint() {
    return this.point;
  }

  @Override
  public void updatePoint(final float partialTicks) {
    final HitResult res = WorldEdit.references().minecraftHelper()
        .pick(this.entity, this.traceDistance, partialTicks, false);

    if (res.location().distance(this.entity.position().toFloatVector3()) > this.traceDistance) {
      return;
    }

    if (!(res instanceof BlockHitResult)) {
      return;
    }

    final FloatVector3 pos = ((BlockHitResult) res).getBlockPosition();
    final int x = (int) pos.getX();
    final int y = (int) pos.getY();
    final int z = (int) pos.getZ();

    if (this.lastX != x || this.lastY != y || this.lastZ != z) {
      this.lastX = x;
      this.lastY = y;
      this.lastZ = z;
      this.point = new Vector3(x, y, z);
      this.box.setPosition(this.point.subtract(PointCube.MIN_VEC), this.point.add(PointCube.MAX_VEC));
      this.notifyObservers();
    }
  }

}
