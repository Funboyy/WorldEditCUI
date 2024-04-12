package org.enginehub.worldeditcui.render;

public interface PipelineProvider {

    String id();

    boolean available();

    default boolean shouldRender() {
        return true;
    }

    RenderSink provide();

}
