package org.enginehub.worldeditcui.render.points;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import org.enginehub.worldeditcui.render.ConfiguredColor;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.render.shapes.Render3DBox;
import org.enginehub.worldeditcui.util.Vector2;

public class PointRectangle {

	private static final double OFF = 0.03;
	private static final Vector2 MIN_VEC = new Vector2(PointRectangle.OFF, PointRectangle.OFF);
	private static final Vector2 MAX_VEC = new Vector2(PointRectangle.OFF + 1, PointRectangle.OFF + 1);
	
	protected Vector2 point;
	protected RenderStyle style = ConfiguredColor.POLYGON_POINT.style();
	private int min, max;
	private Render3DBox box;
	
	public PointRectangle(final int x, final int z) {
		this(new Vector2(x, z));
	}
	
	public PointRectangle(final Vector2 point) {
		this.setPoint(point);
	}
	
	public void render(final RenderContext context) {
		this.box.render(context);
	}
	
	public Vector2 getPoint() {
		return this.point;
	}
	
	public void setPoint(final Vector2 point) {
		this.point = point;
	}
	
	public RenderStyle getStyle() {
		return this.style;
	}

  public void setStyle(final RenderStyle style) {
		this.style = style;
	}
	
	public void setMinMax(final int min, final int max) {
		this.min = min;
		this.max = max;
		this.update();
	}

	public int getMin() {
		return this.min;
	}

	public int getMax() {
		return this.max;
	}
	
	private void update() {
		this.box = new Render3DBox(this.style, this.point.subtract(PointRectangle.MIN_VEC)
        .toVector3(this.min - 0.03f), this.point.add(PointRectangle.MAX_VEC)
        .toVector3(this.max + 1 + 0.03f));
	}

}
