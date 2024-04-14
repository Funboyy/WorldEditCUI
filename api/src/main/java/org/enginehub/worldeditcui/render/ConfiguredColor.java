package org.enginehub.worldeditcui.render;

import java.util.regex.Pattern;
import net.labymod.api.util.Color;
import org.enginehub.worldeditcui.render.RenderStyle.RenderType;

public enum ConfiguredColor {

	CUBOID_GRID(parse("#CC4C4CCC")),
	CUBOID_BOX(parse("#CC3333CC")),
	CUBOID_POINT_ONE(parse("#33CC33CC")),
	CUBOID_POINT_TWO(parse("#3333CCCC")),
	POLYGON_GRID(parse("#CC3333CC")),
	POLYGON_BOX(parse("#CC4C4CCC")),
	POLYGON_POINT(parse("#33CCCCCC")),
	ELLIPSOID_GRID(parse("#CC4C4CCC")),
	ELLIPSOID_CENTER(parse("#CCCC33CC")),
	CYLINDER_GRID(parse("#CC3333CC")),
	CYLINDER_BOX(parse("#CC4C4CCC")),
	CYLINDER_CENTER(parse("#CC33CCCC"));

  private static final Pattern COLOR_PATTERN = Pattern.compile("^#[0-9a-f]{6,8}$", Pattern.CASE_INSENSITIVE);

  public static Color parse(final String hex) {
    final int red = Integer.parseInt(hex.substring(1, 3), 16) & 0xff;
    final int green = Integer.parseInt(hex.substring(3, 5), 16) & 0xff;
    final int blue = Integer.parseInt(hex.substring(5, 7), 16) & 0xff;
    final int alpha = hex.length() < 9 ? 0xCC : Integer.parseInt(hex.substring(7, 9), 16) & 0xff;

    return Color.ofRGB(red, green, blue, alpha);
  }

  public static Color parseOr(final String hex, final Color defaultColor) {
    if (hex == null) {
      return defaultColor;
    }

    if (!hex.startsWith("#")) {
      return defaultColor;
    }

    if (hex.length() != 7 && hex.length() != 9) {
      return defaultColor;
    }

    return COLOR_PATTERN.matcher(hex).matches() ? parse(hex) : defaultColor;
  }

	class Style implements RenderStyle {

		private RenderType renderType = RenderType.ANY;

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
		}

		@Override
		public Color getColor() {
			return ConfiguredColor.this.getColor();
		}

		@Override
		public LineStyle[] getLines() {
			return ConfiguredColor.this.getLines();
		}

	}

	private Color color;
  private LineStyle[] lines;

	ConfiguredColor(final Color color) {
		this.color = color;
		this.updateLines();
	}

  public RenderStyle style() {
		return new Style();
	}
	
	public void setColor(final Color color) {
		this.color = color;
		this.updateLines();
	}

	public Color getColor() {
		return this.color;
	}
	
	public LineStyle[] getLines() {
		return this.lines;
	}

  private void updateLines() {
    final LineStyle hidden = new LineStyle(
        RenderType.HIDDEN,
        LineStyle.DEFAULT_WIDTH,
        Math.round(this.color.getRed() * 0.75f),
        Math.round(this.color.getGreen() * 0.75F),
        Math.round(this.color.getBlue() * 0.75F),
        Math.round(this.color.getAlpha() * 0.25F));
    final LineStyle normal = new LineStyle(
        RenderType.VISIBLE,
        LineStyle.DEFAULT_WIDTH,
        this.color.getRed(),
        this.color.getGreen(),
        this.color.getBlue(),
        this.color.getAlpha());
		this.lines = new LineStyle[] { hidden, normal };
	}

}
