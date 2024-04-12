package org.enginehub.worldeditcui.render;

import net.labymod.api.util.Color;

public class CustomStyle implements RenderStyle {

	private Color color;
	private RenderType renderType = RenderType.ANY;
	private final LineStyle[] lines = new LineStyle[2];

	public CustomStyle(final Color color) {
		this.setColor(color);
	}
	
	@Override
	public void setRenderType(final RenderType renderType) {
		this.renderType = renderType;
	}
	
	@Override
	public RenderType getRenderType() {
		return this.renderType;
	}

	@Override
	public void setColor(final Color color) {
		this.color = color;
		this.lines[0] = new LineStyle(
        RenderType.HIDDEN,
			  LineStyle.DEFAULT_WIDTH,
        Math.round(color.getRed() * 0.75F),
			  Math.round(color.getGreen() * 0.75F),
			  Math.round(color.getBlue() * 0.75F),
			  Math.round(color.getAlpha() * 0.25F)
		);
		this.lines[1] =	new LineStyle(
			  RenderType.VISIBLE,
			  LineStyle.DEFAULT_WIDTH,
        color.getRed(),
        color.getGreen(),
        color.getBlue(),
        color.getAlpha()
		);
	}
	
	@Override
	public Color getColor()
	{
		return this.color;
	}

	@Override
	public LineStyle[] getLines()
	{
		return this.lines;
	}

}
