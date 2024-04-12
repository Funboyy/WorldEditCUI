package org.enginehub.worldeditcui.render.shapes;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import org.enginehub.worldeditcui.render.LineStyle;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.render.points.PointCube;

public class RenderCylinderGrid extends RenderRegion {

	private final double radX;
	private final double radZ;
	private final int minY;
	private final int maxY;
	private final double centerX;
	private final double centerZ;
	
	public RenderCylinderGrid(final RenderStyle style, final PointCube center, final double radX, final double radZ, final int minY, final int maxY) {
		super(style);
		this.radX = radX;
		this.radZ = radZ;
		this.minY = minY;
		this.maxY = maxY;
		this.centerX = center.getPoint().getX() + 0.5;
		this.centerZ = center.getPoint().getZ() + 0.5;
	}
	
	@Override
	public void render(final RenderContext context) {
		final double xPos = this.centerX - context.cameraPos().getX();
		final double zPos = this.centerZ - context.cameraPos().getZ();

		for (final LineStyle line : this.style.getLines()) {
			if (!context.apply(line, this.style.getRenderType())) {
				continue;
			}
			
			final int tempMaxY = this.maxY + 1;
			final int tempMinY = this.minY;
			final int posRadiusX = (int) Math.ceil(this.radX);
			final int negRadiusX = (int) -Math.ceil(this.radX);
			final int posRadiusZ = (int) Math.ceil(this.radZ);
			final int negRadiusZ = (int) -Math.ceil(this.radZ);
			final double cameraY = context.cameraPos().getY();

			context.color(line);

			for (double tempX = negRadiusX; tempX <= posRadiusX; ++tempX) {
				final double tempZ = this.radZ * Math.cos(Math.asin(tempX / this.radX));

				context.beginLineLoop()
					.vertex(xPos + tempX, tempMaxY - cameraY, zPos + tempZ)
					.vertex(xPos + tempX, tempMaxY - cameraY, zPos - tempZ)
					.vertex(xPos + tempX, tempMinY - cameraY, zPos - tempZ)
					.vertex(xPos + tempX, tempMinY - cameraY, zPos + tempZ)
					.endLineLoop();
			}
			
			for (double tempZ = negRadiusZ; tempZ <= posRadiusZ; ++tempZ) {
				final double tempX = this.radX * Math.sin(Math.acos(tempZ / this.radZ));

				context.beginLineLoop()
					.vertex(xPos + tempX, tempMaxY - cameraY, zPos + tempZ)
					.vertex(xPos - tempX, tempMaxY - cameraY, zPos + tempZ)
					.vertex(xPos - tempX, tempMinY - cameraY, zPos + tempZ)
					.vertex(xPos + tempX, tempMinY - cameraY, zPos + tempZ)
					.endLineLoop();
			}
		}
	}

}
