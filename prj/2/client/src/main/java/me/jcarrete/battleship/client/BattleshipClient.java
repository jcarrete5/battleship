package me.jcarrete.battleship.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class BattleshipClient extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(BuildVersion.getImplTitle() + " v" + BuildVersion.getImplVersion());
		primaryStage.show();
		primaryStage.centerOnScreen();
	}
}
