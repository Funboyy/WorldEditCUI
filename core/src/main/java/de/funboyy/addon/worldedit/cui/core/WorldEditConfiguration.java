package de.funboyy.addon.worldedit.cui.core;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Color;
import org.enginehub.worldeditcui.render.ConfiguredColor;

@ConfigName("settings")
public class WorldEditConfiguration extends AddonConfig {

  @SwitchSetting(hotkey = true)
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SettingSection("cuboid")
  @ColorPickerSetting(alpha = true)
  private final ConfigProperty<Color> cuboidGrid = new ConfigProperty<>(ConfiguredColor.parse("#CC4C4CCC"))
      .addChangeListener(ConfiguredColor.CUBOID_GRID::setColor);

  @ColorPickerSetting(alpha = true)
  private final ConfigProperty<Color> cuboidBox = new ConfigProperty<>(ConfiguredColor.parse("#CC3333CC"))
      .addChangeListener(ConfiguredColor.CUBOID_BOX::setColor);

  @ColorPickerSetting(alpha = true)
  private final ConfigProperty<Color> cuboidPointOne = new ConfigProperty<>(ConfiguredColor.parse("#33CC33CC"))
      .addChangeListener(ConfiguredColor.CUBOID_POINT_ONE::setColor);

  @ColorPickerSetting(alpha = true)
  private final ConfigProperty<Color> cuboidPointTwo = new ConfigProperty<>(ConfiguredColor.parse("#3333CCCC"))
      .addChangeListener(ConfiguredColor.CUBOID_POINT_TWO::setColor);

  @SettingSection("polygon")
  @ColorPickerSetting(alpha = true)
  private final ConfigProperty<Color> polygonGrid = new ConfigProperty<>(ConfiguredColor.parse("#CC3333CC"))
      .addChangeListener(ConfiguredColor.POLYGON_GRID::setColor);

  @ColorPickerSetting(alpha = true)
  private final ConfigProperty<Color> polygonBox = new ConfigProperty<>(ConfiguredColor.parse("#CC4C4CCC"))
      .addChangeListener(ConfiguredColor.POLYGON_BOX::setColor);

  @ColorPickerSetting(alpha = true)
  private final ConfigProperty<Color> polygonPoint = new ConfigProperty<>(ConfiguredColor.parse("#33CCCCCC"))
      .addChangeListener(ConfiguredColor.POLYGON_POINT::setColor);

  @SettingSection("ellipsoid")
  @ColorPickerSetting(alpha = true)
  private final ConfigProperty<Color> ellipsoidGrid = new ConfigProperty<>(ConfiguredColor.parse("#CC4C4CCC"))
      .addChangeListener(ConfiguredColor.ELLIPSOID_GRID::setColor);

  @ColorPickerSetting(alpha = true)
  private final ConfigProperty<Color> ellipsoidCenter = new ConfigProperty<>(ConfiguredColor.parse("#CCCC33CC"))
      .addChangeListener(ConfiguredColor.ELLIPSOID_CENTER::setColor);

  @SettingSection("cylinder")
  @ColorPickerSetting(alpha = true)
  private final ConfigProperty<Color> cylinderGrid = new ConfigProperty<>(ConfiguredColor.parse("#CC3333CC"))
      .addChangeListener(ConfiguredColor.CYLINDER_GRID::setColor);

  @ColorPickerSetting(alpha = true)
  private final ConfigProperty<Color> cylinderBox = new ConfigProperty<>(ConfiguredColor.parse("#CC4C4CCC"))
      .addChangeListener(ConfiguredColor.CYLINDER_BOX::setColor);

  @ColorPickerSetting(alpha = true)
  private final ConfigProperty<Color> cylinderCenter = new ConfigProperty<>(ConfiguredColor.parse("#CC33CCCC"))
      .addChangeListener(ConfiguredColor.CYLINDER_CENTER::setColor);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

}
