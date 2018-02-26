package me.jcarrete.battleship.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

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

	@FXML
	private void onMultiPress(ActionEvent event) {
		try {
			Socket socket = new Socket(InetAddress.getLocalHost(), 10000);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
