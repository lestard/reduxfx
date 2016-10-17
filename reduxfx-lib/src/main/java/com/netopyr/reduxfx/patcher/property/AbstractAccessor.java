package com.netopyr.reduxfx.patcher.property;

import com.netopyr.reduxfx.vscenegraph.property.VChangeListener;
import com.netopyr.reduxfx.vscenegraph.property.VInvalidationListener;
import com.netopyr.reduxfx.vscenegraph.property.VProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.util.function.Consumer;

abstract class AbstractAccessor<V_TYPE, ACTION, FX_TYPE> implements Accessor<V_TYPE, ACTION> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractAccessor.class);

    private final MethodHandle propertyGetter;
    private final Consumer<ACTION> dispatcher;

    AbstractAccessor(MethodHandle propertyGetter, Consumer<ACTION> dispatcher) {
        this.propertyGetter = propertyGetter;
        this.dispatcher = dispatcher;
    }

    @Override
    public void set(Node node, VProperty<V_TYPE, ACTION> vProperty) {

        final ReadOnlyProperty<FX_TYPE> property;
        try {
            property = (ReadOnlyProperty) propertyGetter.invoke(node);
        } catch (Throwable throwable) {
            throw new IllegalStateException("Unable to read property " + vProperty.getName() + " from Node-class " + node.getClass(), throwable);
        }

        setValue(property, vToFX(vProperty.getValue()));

        if (vProperty.getChangeListener().isDefined()) {
            setChangeListener(node, property, vProperty.getChangeListener().get(), dispatcher);
        }

        if (vProperty.getInvalidationListener().isDefined()) {
            setInvalidationListener(node, property, vProperty.getInvalidationListener().get(), dispatcher);
        }
    }

    protected abstract V_TYPE fxToV(FX_TYPE value);

    protected abstract FX_TYPE vToFX(V_TYPE value);

    protected abstract void setValue(ReadOnlyProperty<FX_TYPE> property, FX_TYPE value);

    @SuppressWarnings("unchecked")
    private void setChangeListener(Node node, ReadOnlyProperty<FX_TYPE> property, VChangeListener<? super V_TYPE, ACTION> listener, Consumer<ACTION> dispatcher) {
        final ChangeListener<FX_TYPE> oldListener = (ChangeListener) node.getProperties().get(property.getName() + ".change");
        if (oldListener != null) {
            property.removeListener(oldListener);
        }

        final ChangeListener<FX_TYPE> newListener = (source, oldValue, newValue) -> {
            final ACTION action = listener.onChange(fxToV(oldValue), fxToV(newValue));
            if (action != null) {
                dispatcher.accept(action);
            }
        };
        property.addListener(newListener);
        node.getProperties().put(property.getName() + ".change", newListener);
    }

    @SuppressWarnings("unchecked")
    private void setInvalidationListener(Node node, ReadOnlyProperty<FX_TYPE> property, VInvalidationListener<ACTION> listener, Consumer<ACTION> dispatcher) {
        final InvalidationListener oldListener = (InvalidationListener) node.getProperties().get(property.getName() + ".invalidation");
        if (oldListener != null) {
            property.removeListener(oldListener);
        }

        final InvalidationListener newListener = (source) -> {
            final ACTION action = listener.onInvalidation();
            if (action != null) {
                dispatcher.accept(action);
            }
        };
        property.addListener(newListener);
        node.getProperties().put(property.getName() + ".invalidation", newListener);
    }
}