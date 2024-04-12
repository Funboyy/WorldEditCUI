package de.funboyy.addon.worldedit.cui.api.protocol;

import java.nio.charset.StandardCharsets;

public class PacketMessage {

  private final boolean multi;
  private final String typeId;
  private final String[] parameters;

  public PacketMessage(final byte[] payload) {
    final String message = new String(payload, StandardCharsets.UTF_8);
    final String[] split = message.split("\\|", -1);

    this.multi = split[0].startsWith("+");
    this.typeId = split[0].substring(this.multi ? 1 : 0);

    final String[] parameters = message.substring(this.typeId.length() + (this.multi ? 2 : 1))
        .split("\\|", -1);

    if (parameters.length == 1 && parameters[0].isEmpty()) {
      this.parameters = new String[0];
    }

    else {
      this.parameters = parameters;
    }
  }

  public boolean isMulti() {
    return this.multi;
  }

  public String getTypeId() {
    return this.typeId;
  }

  public String[] getParameters() {
    return this.parameters;
  }

  public int getInt(final int index) {
    return (int) Float.parseFloat(this.parameters[index]);
  }

  public double getDouble(final int index) {
    return Double.parseDouble(this.parameters[index]);
  }

  public String getString(final int index) {
    return this.parameters[index];
  }

}
