package org.enginehub.worldeditcui.render;

public interface PipelineProvider {

    String id();

    boolean available();

    default boolean shouldRender() {
        return true;
    }

    default boolean useDebug() {
      return false;
    }

    RenderSink provide();

}
