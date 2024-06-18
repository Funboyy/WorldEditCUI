package de.funboyy.addon.worldedit.cui.api.protocol.handler;

import de.funboyy.addon.worldedit.cui.api.protocol.packet.ColorPacket;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.ConfiguredColor;
import org.enginehub.worldeditcui.render.CustomStyle;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.render.region.Region;

public class ColorHandler extends WorldEditHandler<ColorPacket> {

  public ColorHandler(final WorldEdit controller) {
    super(controller);
  }

  @Override
  public void handle(final ColorPacket packet) {
    if (!packet.isMulti()) {
      throw new IllegalStateException("COLOR packet is not valid for non-multi selections");
    }

    final Region selection = super.getController().getSelection(true);

    if (selection == null) {
      return;
    }

    final RenderStyle[] defaultStyles = selection.getDefaultStyles();
    final RenderStyle[] styles = new RenderStyle[defaultStyles.length];
    final String[] colors = packet.getColors();

    for (int i = 0; i < defaultStyles.length; i++) {
      String color = colors[i];

      if (!color.startsWith("#")) {
        color = "#" + color;
      }

      styles[i] = new CustomStyle(ConfiguredColor.parseOr(color, defaultStyles[i].getColor()));
    }

    selection.setStyles(styles);
  }

  @Override
  public Class<ColorPacket> getPacketClass() {
    return ColorPacket.class;
  }

}
