package com.netopyr.reduxfx.helloworld;

import com.netopyr.reduxfx.SimpleReduxFX;
import com.netopyr.reduxfx.helloworld.state.AppModel;
import com.netopyr.reduxfx.helloworld.updater.Updater;
import com.netopyr.reduxfx.helloworld.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This is the launcher of the application.
 */
public class HelloWorld extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setup the initial state
        final AppModel initialState = AppModel.create();

        // Start the ReduxFX application by passing the initial state, the update-function, the view-function, and
        // the stage to use with the resulting SceneGraph.
        SimpleReduxFX.start(initialState, Updater::update, MainView::view, primaryStage);

        primaryStage.setTitle("HelloWorld - ReduxFX");
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
