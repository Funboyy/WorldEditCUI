package org.enginehub.worldeditcui.render.shapes;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import org.enginehub.worldeditcui.render.LineStyle;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.render.points.PointCube;

public class RenderCylinderBox extends RenderRegion {

	private final double radX, radZ;
	private final int minY, maxY;
	private final double centerX, centerZ;
	
	public RenderCylinderBox(final RenderStyle style, final PointCube center, final double radX, final double radZ, final int minY, final int maxY) {
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
			
			final double twoPi = Math.PI * 2;
			context.color(line);

			for (final int yBlock : new int[] { this.minY, this.maxY + 1 }) {
				context.beginLineLoop();

				for (int i = 0; i <= 75; i++) {
					final double tempTheta = i * twoPi / 75;
					final double tempX = this.radX * Math.cos(tempTheta);
					final double tempZ = this.radZ * Math.sin(tempTheta);

					context.vertex(xPos + tempX, yBlock - context.cameraPos().getY(), zPos + tempZ);
				}

				context.endLineLoop();
			}
		}
	}

}
