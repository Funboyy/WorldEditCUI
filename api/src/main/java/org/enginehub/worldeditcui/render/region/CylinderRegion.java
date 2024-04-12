package org.enginehub.worldeditcui.render.region;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.ConfiguredColor;
import org.enginehub.worldeditcui.render.points.PointCube;
import org.enginehub.worldeditcui.render.shapes.RenderCylinderBox;
import org.enginehub.worldeditcui.render.shapes.RenderCylinderCircles;
import org.enginehub.worldeditcui.render.shapes.RenderCylinderGrid;

public class CylinderRegion extends Region {

	private PointCube center;
	private double radX = 0, radZ = 0;
	private int minY = 0, maxY = 0;
	
	private RenderCylinderCircles circles;
	private RenderCylinderGrid grid;
	private RenderCylinderBox box;
	
	public CylinderRegion(final WorldEdit controller) {
		super(controller, ConfiguredColor.CYLINDER_BOX.style(), ConfiguredColor.CYLINDER_GRID.style(), ConfiguredColor.CYLINDER_CENTER.style());
	}
	
	@Override
	public void render(final RenderContext context) {
		if (this.center != null) {
			this.center.render(context);
			this.circles.render(context);
			this.grid.render(context);
			this.box.render(context);
		}
	}

	@Override
	public void setCylinderCenter(final int x, final int y, final int z) {
		this.center = new PointCube(x, y, z);
		this.center.setStyle(this.styles[2]);
		this.update();
	}
	
	@Override
	public void setCylinderRadius(final double x, final double z) {
		this.radX = x;
		this.radZ = z;
		this.update();
	}
	
	@Override
	public void setMinMax(final int min, final int max) {
		this.minY = min;
		this.maxY = max;
		this.update();
	}
	
	private void update() {
		int tMin = this.minY;
		int tMax = this.maxY;
		
		if (this.minY == 0 || this.maxY == 0) {
			tMin = (int)this.center.getPoint().getY();
			tMax = (int)this.center.getPoint().getY();
		}
		
		this.circles = new RenderCylinderCircles(this.styles[1], this.center, this.radX, this.radZ, tMin, tMax);
		this.grid = new RenderCylinderGrid(this.styles[1], this.center, this.radX, this.radZ, tMin, tMax);
		this.box = new RenderCylinderBox(this.styles[0], this.center, this.radX, this.radZ, tMin, tMax);
	}
	
	@Override
	protected void updateStyles() {
		if (this.box != null) {
			this.box.setStyle(this.styles[0]);
		}
		
		if (this.grid != null) {
			this.grid.setStyle(this.styles[1]);
		}
		
		if (this.circles != null) {
			this.circles.setStyle(this.styles[1]);
		}
		
		if (this.center != null) {
			this.center.setStyle(this.styles[2]);
		}
	}

	@Override
	public RegionType getType() {
		return RegionType.CYLINDER;
	}

}
