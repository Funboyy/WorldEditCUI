package org.enginehub.worldeditcui.render.region;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.ConfiguredColor;
import org.enginehub.worldeditcui.render.points.PointCube;
import org.enginehub.worldeditcui.render.shapes.RenderEllipsoid;
import org.enginehub.worldeditcui.util.Vector3;

public class EllipsoidRegion extends Region {

	private PointCube center;
	private Vector3 radii;
	
	private RenderEllipsoid ellipsoid;
	
	public EllipsoidRegion(final WorldEdit controller) {
		super(controller, ConfiguredColor.ELLIPSOID_GRID.style(), ConfiguredColor.ELLIPSOID_CENTER.style());
	}
	
	@Override
	public void render(final RenderContext context) {
		if (this.center != null && this.radii != null) {
			this.center.render(context);
			this.ellipsoid.render(context);
		}

		else if (this.center != null) {
			this.center.render(context);
		}
	}
	
	@Override
	public void setEllipsoidCenter(final int x, final int y, final int z) {
		this.center = new PointCube(x, y, z);
		this.center.setStyle(this.styles[1]);
		this.update();
	}
	
	@Override
	public void setEllipsoidRadii(final double x, final double y, final double z) {
		this.radii = new Vector3(x, y, z);
		this.update();
	}

	private void update() {
		if (this.center != null && this.radii != null) {
			this.ellipsoid = new RenderEllipsoid(this.styles[0], this.center, this.radii);
		}
	}
	
	@Override
	protected void updateStyles() {
    if (this.ellipsoid != null) {
			this.ellipsoid.setStyle(this.styles[0]);
		}

    if (this.center != null) {
			this.center.setStyle(this.styles[1]);
		}
	}

	@Override
	public RegionType getType() {
		return RegionType.ELLIPSOID;
	}
	
}
