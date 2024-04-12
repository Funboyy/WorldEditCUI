package org.enginehub.worldeditcui.render.shapes;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import java.util.List;
import org.enginehub.worldeditcui.render.LineStyle;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.render.points.PointRectangle;
import org.enginehub.worldeditcui.util.Vector2;

public class Render2DBox extends RenderRegion {

	private final List<PointRectangle> points;
	private final int min, max;
	
	public Render2DBox(final RenderStyle style, final List<PointRectangle> points, final int min, final int max) {
		super(style);
		this.points = points;
		this.min = min;
		this.max = max;
	}
	
	@Override
	public void render(final RenderContext context) {
		final double off = 0.03 - context.cameraPos().getY();

    for (final LineStyle line : this.style.getLines()) {
			if (!context.apply(line, this.style.getRenderType())) {
				continue;
			}

			context.beginLines();
			context.color(line);

			for (PointRectangle point : this.points) {
				if (point == null) {
          continue;
        }

        final Vector2 pos = point.getPoint();
        final double x = pos.getX() - context.cameraPos().getX();
        final double z = pos.getY() - context.cameraPos().getZ();

        context.vertex(x + 0.5, this.min + off, z + 0.5)
          .vertex(x + 0.5, this.max + 1 + off, z + 0.5);
			}

			context.endLines();
		}
	}

}
