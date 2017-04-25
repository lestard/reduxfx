package com.netopyr.reduxfx.sample.externalstore.reducer;

import com.netopyr.reduxfx.sample.externalstore.actions.IncCounterAction;
import com.netopyr.reduxfx.sample.externalstore.state.AppModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;
import static javaslang.Predicates.instanceOf;

@SuppressWarnings("WeakerAccess")
public class Reducer {

    private static final Logger LOG = LoggerFactory.getLogger(Reducer.class);

    private Reducer() {
    }

    public static AppModel reduce(AppModel state, Object action) {
        Objects.requireNonNull(state, "The parameter 'state' must not be null");
        Objects.requireNonNull(action, "The parameter 'action' must not be null");

        // Here we assign the new state
        final AppModel newState =

                // This is part of Javaslang's pattern-matching API. It works similar to the regular switch-case
                // in Java, except that it is much more flexible and that it can be used as an expression.
                // We check which kind of action was received and in that case-branch we specify the value that
                // will be assigned to newState.
                Match(action).of(

                        // If the action is a UpdateColorAction, we return a new AppModel with the
                        // property color set to the new value.
                        Case(instanceOf(IncCounterAction.class),
                                incCounterAction -> state.withCounter(state.getCounter() + 1)
                        ),

                        // This is the default branch of this switch-case. If an unknown Action was passed to the
                        // updater, we simple return the old state. This is a convention, that is not needed right
                        // now, but will help once you start to decompose your updater.
                        Case($(), state)
                );

        LOG.trace("\nUpdater Old State:\n{}\nUpdater Action:\n{}\nUpdater New State:\n{}\n\n",
                state, action, newState);
        return newState;
    }
}
