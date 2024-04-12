package org.enginehub.worldeditcui.render.region;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import java.util.ArrayList;
import java.util.List;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.ConfiguredColor;
import org.enginehub.worldeditcui.render.points.PointRectangle;
import org.enginehub.worldeditcui.render.shapes.Render2DBox;
import org.enginehub.worldeditcui.render.shapes.Render2DGrid;

public class PolygonRegion extends Region {

	private final List<PointRectangle> points = new ArrayList<>();
	private int min, max;
	
	private Render2DBox box;
	private Render2DGrid grid;
	
	public PolygonRegion(final WorldEdit controller) {
		super(controller, ConfiguredColor.POLYGON_BOX.style(), ConfiguredColor.POLYGON_GRID.style(), ConfiguredColor.POLYGON_POINT.style());
	}
	
	@Override
	public void render(final RenderContext context) {
		if (this.points.isEmpty()) {
			return;
		}
		
		for (final PointRectangle point : this.points) {
			if (point != null) {
				point.render(context);
			}
		}
		
		this.box.render(context);
		this.grid.render(context);
	}
	
	@Override
	public void setMinMax(final int min, final int max) {
		this.min = min;
		this.max = max;
		this.update();
	}
	
	@Override
	public void setPolygonPoint(final int id, final int x, final int z) {
		final PointRectangle point = new PointRectangle(x, z);
		point.setStyle(this.styles[0]);
		point.setMinMax(this.min, this.max);
		
		if (id < this.points.size()) {
			this.points.set(id, point);
		}

		else {
			for (int i = 0; i < id - this.points.size(); i++) {
				this.points.add(null);
			}

			this.points.add(point);
		}

		this.update();
	}
	
	private void update() {
		if (this.points.isEmpty()) {
			return;
		}
		
		for (final PointRectangle point : this.points) {
			if (point != null) {
				point.setMinMax(this.min, this.max);
			}
		}
		
		this.box = new Render2DBox(this.styles[0], this.points, this.min, this.max);
		this.grid = new Render2DGrid(this.styles[1], this.points, this.min, this.max);
	}
	
	@Override
	protected void updateStyles() {
		if (this.box != null) {
			this.box.setStyle(this.styles[0]);
		}
		
		if (this.grid != null) {
			this.grid.setStyle(this.styles[1]);
		}
		
		for (final PointRectangle point : this.points) {
			point.setStyle(this.styles[0]);
		}
	}

	@Override
	public RegionType getType() {
		return RegionType.POLYGON;
	}

}

