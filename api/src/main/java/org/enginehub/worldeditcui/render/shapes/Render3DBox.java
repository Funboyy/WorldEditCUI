package org.enginehub.worldeditcui.render.shapes;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import org.enginehub.worldeditcui.render.LineStyle;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.util.BoundingBox;
import org.enginehub.worldeditcui.util.Observable;
import org.enginehub.worldeditcui.util.Vector3;

public class Render3DBox extends RenderRegion {

	private Vector3 first, second;
	
	public Render3DBox(final RenderStyle style, final BoundingBox region) {
		this(style, region.getMin(), region.getMax());

		if (region.isDynamic()) {
			region.addObserver(this);
		}
	}
	
	public Render3DBox(final RenderStyle style, final Vector3 first, final Vector3 second) {
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
	
	@Override
	public void render(final RenderContext context) {
		final Vector3 camera = context.cameraPos();
		final double x1 = this.first.getX() - camera.getX();
		final double y1 = this.first.getY() - camera.getY();
		final double z1 = this.first.getZ() - camera.getZ();
		final double x2 = this.second.getX() - camera.getX();
		final double y2 = this.second.getY() - camera.getY();
		final double z2 = this.second.getZ() - camera.getZ();
		
		for (final LineStyle line : this.style.getLines()) {
			if (!context.apply(line, this.style.getRenderType())) {
				continue;
			}
			
			// Draw bottom face
      context.color(line);
      context.beginLineLoop()
				.vertex(x1, y1, z1)
				.vertex(x2, y1, z1)
				.vertex(x2, y1, z2)
				.vertex(x1, y1, z2)
				.endLineLoop();

			// Draw top face
      context.beginLineLoop()
        .vertex(x1, y2, z1)
        .vertex(x2, y2, z1)
        .vertex(x2, y2, z2)
        .vertex(x1, y2, z2)
        .endLineLoop();

			// Draw join top and bottom faces
      context.beginLines()
				.vertex(x1, y1, z1)
				.vertex(x1, y2, z1)

				.vertex(x2, y1, z1)
				.vertex(x2, y2, z1)

				.vertex(x2, y1, z2)
				.vertex(x2, y2, z2)

				.vertex(x1, y1, z2)
				.vertex(x1, y2, z2)
				.endLines();
		}
	}

}
