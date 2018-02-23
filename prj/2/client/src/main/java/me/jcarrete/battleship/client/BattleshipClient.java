package me.jcarrete.battleship.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BattleshipClient extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(ClassLoader.getSystemResource("fxml/main_menu.fxml"));

		primaryStage.setScene(new Scene(root, 800, 600));
		primaryStage.setTitle(BuildVersion.getImplTitle() + " v" + BuildVersion.getImplVersion());
		primaryStage.show();
		primaryStage.centerOnScreen();
	}
}
