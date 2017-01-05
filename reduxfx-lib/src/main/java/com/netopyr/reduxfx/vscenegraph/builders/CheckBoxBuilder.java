package com.netopyr.reduxfx.vscenegraph.builders;

import com.netopyr.reduxfx.vscenegraph.event.VEventHandlerElement;
import com.netopyr.reduxfx.vscenegraph.property.VProperty;
import javafx.scene.Node;
import javaslang.collection.Array;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CheckBoxBuilder<CLASS extends CheckBoxBuilder<CLASS>> extends ButtonBaseBuilder<CLASS> {

    private static final String SELECTED = "selected";

    public CheckBoxBuilder(Class<? extends Node> nodeClass,
                           Array<VProperty<?>> properties,
                           Array<VEventHandlerElement<?>> eventHandlers) {
        super(nodeClass, properties, eventHandlers);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected CLASS create(Class<? extends Node> nodeClass, Array<VProperty<?>> properties, Array<VEventHandlerElement<?>> eventHandlers) {
        return (CLASS) new CheckBoxBuilder<>(nodeClass, properties, eventHandlers);
    }


    public CLASS selected(boolean value) {
        return property(SELECTED, value);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .toString();
    }
}
