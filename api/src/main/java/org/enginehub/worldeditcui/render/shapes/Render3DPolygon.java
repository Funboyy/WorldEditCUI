package org.enginehub.worldeditcui.render.shapes;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import org.enginehub.worldeditcui.render.LineStyle;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.util.Vector3;

public class Render3DPolygon extends RenderRegion {

	private final Vector3[] vertices;
	
	public Render3DPolygon(final RenderStyle style, final Vector3... vertices) {
		super(style);
		this.vertices = vertices;
	}
	
	@Override
	public void render(final RenderContext context) {
		for (final LineStyle line : this.style.getLines()) {
			if (!context.apply(line, this.style.getRenderType())) {
				continue;
			}

			context.beginLineLoop().color(line);

      for (final Vector3 vertex : this.vertices) {
        context.vertex(
            vertex.getX() - context.cameraPos().getX(),
            vertex.getY() - context.cameraPos().getY(),
            vertex.getZ() - context.cameraPos().getZ()
        );
      }

			context.endLineLoop();
		}
	}
}
