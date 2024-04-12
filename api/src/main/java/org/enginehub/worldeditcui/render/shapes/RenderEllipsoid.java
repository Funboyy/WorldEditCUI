package org.enginehub.worldeditcui.render.shapes;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import org.enginehub.worldeditcui.render.LineStyle;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.render.points.PointCube;
import org.enginehub.worldeditcui.util.Vector3;

public class RenderEllipsoid extends RenderRegion {

	protected final static double TAU = Math.PI * 2.0;
	protected static final double SUBDIVISIONS = 40;
	
	protected PointCube center;
	private final Vector3 radii;
	
	protected final double centerX, centerY, centerZ;
	
	public RenderEllipsoid(final RenderStyle style, final PointCube center, final Vector3 radii) {
		super(style);
		this.center = center;
		this.radii = radii;
		this.centerX = center.getPoint().getX() + 0.5;
		this.centerY = center.getPoint().getY() + 0.5;
		this.centerZ = center.getPoint().getZ() + 0.5;
	}
	
	@Override
	public void render(final RenderContext context) {
		context.flush();
		context.pushPose();
		context.translate(this.centerX - context.cameraPos().getX(), this.centerY - context.cameraPos().getY(), this.centerZ
        - context.cameraPos().getZ());
		context.applyMatrices();

		for (final LineStyle line : this.style.getLines()) {
			if (context.apply(line, this.style.getRenderType())) {
				context.color(line);

				this.drawXZPlane(context);
				this.drawYZPlane(context);
				this.drawXYPlane(context);
			}
		}

		context.flush();
		context.popPose();
		context.applyMatrices();
	}
	
	protected void drawXZPlane(final RenderContext context) {
		int yRad = (int) Math.floor(this.radii.getY());

		for (int yBlock = -yRad; yBlock < yRad; yBlock++) {
			context.beginLineLoop();

			for (int i = 0; i <= SUBDIVISIONS; i++) {
				final double tempTheta = i * TAU / SUBDIVISIONS;
				final double tempX = this.radii.getX() * Math.cos(tempTheta) * Math.cos(Math.asin(yBlock / this.radii.getY()));
				final double tempZ = this.radii.getZ() * Math.sin(tempTheta) * Math.cos(Math.asin(yBlock / this.radii.getY()));
				
				context.vertex(tempX, yBlock, tempZ);
			}

			context.endLineLoop();
		}

		context.beginLineLoop();

		for (int i = 0; i <= SUBDIVISIONS; i++) {
			final double tempTheta = i * TAU / SUBDIVISIONS;
			final double tempX = this.radii.getX() * Math.cos(tempTheta);
			final double tempZ = this.radii.getZ() * Math.sin(tempTheta);
			
			context.vertex(tempX, 0.0, tempZ);
		}

		context.endLineLoop();
	}
	
	protected void drawYZPlane(final RenderContext context) {
		int xRad = (int) Math.floor(this.radii.getX());

		for (int xBlock = -xRad; xBlock < xRad; xBlock++) {
			context.beginLineLoop();

			for (int i = 0; i <= SUBDIVISIONS; i++) {
				final double tempTheta = i * TAU / SUBDIVISIONS;
				final double tempY = this.radii.getY() * Math.cos(tempTheta) * Math.sin(Math.acos(xBlock / this.radii.getX()));
				final double tempZ = this.radii.getZ() * Math.sin(tempTheta) * Math.sin(Math.acos(xBlock / this.radii.getX()));
				
				context.vertex(xBlock, tempY, tempZ);
			}

			context.endLineLoop();
		}
		
		context.beginLineLoop();

		for (int i = 0; i <= SUBDIVISIONS; i++) {
			final double tempTheta = i * TAU / SUBDIVISIONS;
			final double tempY = this.radii.getY() * Math.cos(tempTheta);
			final double tempZ = this.radii.getZ() * Math.sin(tempTheta);
			
			context.vertex(0.0, tempY, tempZ);
		}

		context.endLineLoop();
	}
	
	protected void drawXYPlane(final RenderContext context) {
		int zRad = (int) Math.floor(this.radii.getZ());

    for (int zBlock = -zRad; zBlock < zRad; zBlock++) {
			context.beginLineLoop();

			for (int i = 0; i <= SUBDIVISIONS; i++) {
				final double tempTheta = i * TAU / SUBDIVISIONS;
				final double tempX = this.radii.getX() * Math.sin(tempTheta) * Math.sin(Math.acos(zBlock / this.radii.getZ()));
				final double tempY = this.radii.getY() * Math.cos(tempTheta) * Math.sin(Math.acos(zBlock / this.radii.getZ()));
				
				context.vertex(tempX, tempY, zBlock);
			}

			context.endLineLoop();
		}

		context.beginLineLoop();

		for (int i = 0; i <= SUBDIVISIONS; i++) {
			final double tempTheta = i * TAU / SUBDIVISIONS;
			final double tempX = this.radii.getX() * Math.cos(tempTheta);
			final double tempY = this.radii.getY() * Math.sin(tempTheta);
			
			context.vertex(tempX, tempY, 0.0);
		}

		context.endLineLoop();
	}

}
