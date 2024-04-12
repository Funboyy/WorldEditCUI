package org.enginehub.worldeditcui.render.shapes;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import org.enginehub.worldeditcui.render.LineStyle;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.util.BoundingBox;
import org.enginehub.worldeditcui.util.Observable;
import org.enginehub.worldeditcui.util.Vector3;

public class Render3DGrid extends RenderRegion {

	private static final float CULL_RANGE = 128.0F;
	private static final double SKIP_THRESHOLD = 0.25f; // don't render another band if there is less than this amount left
	public static final double MIN_SPACING = 1.0;
	
	private Vector3 first, second;
	private double spacing = 1.0;
	
	public Render3DGrid(final RenderStyle style, final BoundingBox region) {
		this(style, region.getMin(), region.getMax());

		if (region.isDynamic()) {
			region.addObserver(this);
		}
	}
	
	public Render3DGrid(final RenderStyle style, final Vector3 first, final Vector3 second) {
		super(style);
		this.first = first;
		this.second = second;
	}
	
	@Override
	public void notifyChanged(final Observable<?> source) {
		this.setPosition((BoundingBox) source);
	}

	public void setPosition(final BoundingBox region) {
		this.setPosition(region.getMin(), region.getMax());
	}
	
	public void setPosition(final Vector3 first, final Vector3 second) {
		this.first = first;
		this.second = second;
	}
	
	public Render3DGrid setSpacing(final double spacing) {
		this.spacing = spacing;
		return this;
	}
	
	@Override
	public void render(final RenderContext context) {
		final Vector3 camera = context.cameraPos();
		final double x1 = this.first.getX() - camera.getX();
		final double y1 = this.first.getY() - camera.getY();
		final double z1 = this.first.getZ() - camera.getZ();
		final double x2 = this.second.getX() - camera.getX();
		final double y2 = this.second.getY() - camera.getY();
		final double z2 = this.second.getZ() - camera.getZ();

		if (this.spacing != 1.0) {
      context.disableCull();

			final double[] vertices = {
					x1, y1, z1,  x2, y1, z1,  x2, y1, z2,  x1, y1, z2, // bottom
					x1, y2, z1,  x2, y2, z1,  x2, y2, z2,  x1, y2, z2, // top
					x1, y1, z1,  x1, y1, z2,  x1, y2, z2,  x1, y2, z1, // west
					x2, y1, z1,  x2, y2, z1,  x2, y2, z2,  x2, y1, z2, // east
					x1, y1, z1,  x1, y2, z1,  x2, y2, z1,  x2, y1, z1, // north
					x1, y1, z2,  x2, y1, z2,  x2, y2, z2,  x1, y2, z2  // south
			};

			for (final LineStyle line : this.style.getLines()) {
				if (!context.apply(line, this.style.getRenderType())) {
          continue;
        }

        context.color(line, 0.25f).beginQuads();

        for (int i = 0; i < vertices.length; i += 3) {
          context.vertex(vertices[i], vertices[i + 1], vertices[i + 2]);
        }

        context.endQuads();
      }

			context.flush(); // only needed because of disable/enable cull
      context.enableCull();
		}
		
		if (this.spacing < Render3DGrid.MIN_SPACING) {
			return;
		}
		
		final double cullAt = Render3DGrid.CULL_RANGE * this.spacing;
		final double cullAtY = cullAt - frac(y1);
		final double cullAtX = cullAt - frac(x1);
		final double cullAtZ = cullAt - frac(z1);

		for (final LineStyle line : this.style.getLines()) {
			if (!context.apply(line, this.style.getRenderType())) {
				continue;
			}
			
			context.color(line).beginLines();

			final double yEnd = Math.min(y2 + OFFSET, cullAtY);

			for (double y = Math.max(y1, -cullAtY) + OFFSET; y <= yEnd; y += this.spacing) {
				if (yEnd - y < SKIP_THRESHOLD) {
					continue;
				}

				context.vertex(x1, y, z2)
					.vertex(x2, y, z2)
					.vertex(x1, y, z1)
					.vertex(x2, y, z1)
					.vertex(x1, y, z1)
					.vertex(x1, y, z2)
					.vertex(x2, y, z1)
					.vertex(x2, y, z2);
			}

			final double xEnd = Math.min(x2, cullAtX);
			for (double x = Math.max(x1, -cullAtX); x <= xEnd; x += this.spacing) {
				if (xEnd - x < SKIP_THRESHOLD) {
					continue;
				}

				context.vertex(x, y1, z1)
					.vertex(x, y2, z1)
					.vertex(x, y1, z2)
					.vertex(x, y2, z2)
					.vertex(x, y2, z1)
					.vertex(x, y2, z2)
					.vertex(x, y1, z1)
					.vertex(x, y1, z2);
			}

			final double zEnd = Math.min(z2, cullAtZ);
			for (double z = Math.max(z1, -cullAtZ); z <= zEnd; z += this.spacing) {
				if (zEnd - z < SKIP_THRESHOLD) {
					continue;
				}

				context.vertex(x1, y1, z)
					.vertex(x2, y1, z)
					.vertex(x1, y2, z)
					.vertex(x2, y2, z)
					.vertex(x2, y1, z)
					.vertex(x2, y2, z)
					.vertex(x1, y1, z)
					.vertex(x1, y2, z);
			}

			context.endLines();
		}
	}

  private static double frac(final double number) {
    return number - (double) floor(number);
  }

  private static long floor(final double value) {
    final long l = (long) value;
    return value < (double) l ? l - 1L : l;
  }

}
