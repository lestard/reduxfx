package com.netopyr.reduxfx.examples.menus;

import com.netopyr.reduxfx.examples.menus.state.AppState;
import com.netopyr.reduxfx.examples.menus.updater.Updater;
import com.netopyr.reduxfx.examples.menus.view.ViewManager;
import com.netopyr.reduxfx.middleware.LoggingMiddleware;
import com.netopyr.reduxfx.store.SimpleReduxFXStore;
import com.netopyr.reduxfx.vscenegraph.ReduxFXView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This is the launcher of the application.
 */
public class Menus extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setup the initial state
        final AppState initialState = AppState.create();

        // Setup the ReduxFX-store passing the initialState and the update-function
        final SimpleReduxFXStore<AppState> store = new SimpleReduxFXStore<>(initialState, Updater::update, new LoggingMiddleware<>());

        // Setup the ReduxFX-view passing the view-function and the primary stage that should hold the calculated view
        final ReduxFXView<AppState> view = ReduxFXView.createStages(ViewManager::view, primaryStage);

        // Connect store and view
        view.connect(store.getStatePublisher(), store.createActionSubscriber());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
