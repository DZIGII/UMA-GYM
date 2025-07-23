package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.util.JDBCUtilities;
import org.example.util.StartSqlServer;
import org.example.view.LogIn;
import org.example.view.MainView;

/**
 * Hello world!
 *
 */
public class App extends Application
{
    public static void main( String[] args )
    {
        new StartSqlServer();
        JDBCUtilities.connect();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LogIn logIn = LogIn.getInstance();
        logIn.show();
    }
}