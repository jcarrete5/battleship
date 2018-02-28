package me.jcarrete.battleship.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.jcarrete.battleship.client.net.ServerConnection;
import me.jcarrete.battleship.common.logging.ConsoleFormatter;
import me.jcarrete.battleship.common.logging.LogFileFormatter;

import javax.swing.event.HyperlinkEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.*;

public class BattleshipClient extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public static final Logger LOGGER = Logger.getLogger(BattleshipClient.class.getPackage().getName());

	@Override
	public void init() throws Exception {
		final int MB = 1024 * 1024;
		LOGGER.setUseParentHandlers(false);
		LOGGER.setLevel(Level.ALL);

		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
		consoleHandler.setFormatter(new ConsoleFormatter());
		consoleHandler.setErrorManager(new ErrorManager());
		LOGGER.addHandler(consoleHandler);

		try {
			new File("logs/").mkdirs();
			FileHandler fileHandler = new FileHandler("logs/client-%g.log", 50 * MB, 1);
			fileHandler.setFormatter(new LogFileFormatter());
			fileHandler.setLevel(Level.ALL);
			fileHandler.setErrorManager(new ErrorManager());
			LOGGER.addHandler(fileHandler);
		} catch (IOException e) {
			LOGGER.log(Level.CONFIG, "Unable to setup FileHandler for 'logs/client-%g.log'", e);
		}

		Thread.setDefaultUncaughtExceptionHandler((thread, ex) ->
				LOGGER.log(Level.SEVERE, "Uncaught exception on " + thread.getName() + " thread", ex));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(ClassLoader.getSystemResource("fxml/main_menu.fxml"));

		primaryStage.setScene(new Scene(root, 400, 300));
		primaryStage.setTitle(BuildVersion.getImplTitle() + " v" + BuildVersion.getImplVersion());
		primaryStage.show();
		primaryStage.centerOnScreen();
	}

	@FXML
	private void onMultiPress(ActionEvent event) {
		event.consume();
		try (ServerConnection conn = ServerConnection.connectToGameServer(InetAddress.getLocalHost(), 10000)) {
			conn.findPartner().thenAcceptAsync(partner -> {
				LOGGER.info("PartnerConnection ip is " + partner.getInetAddress().getHostAddress() + ":" + partner.getPort());
				conn.getHasTurn().thenAccept(hasTurn -> {
					LOGGER.info(hasTurn ? "It is my turn" : "It is not my turn");
					//TODO Switch to a game screen with turn player info and partner connection
				}).exceptionally(ex -> {
					String msg = "Failed to determine who starts";
					LOGGER.log(Level.WARNING, msg, ex);
					Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, msg).showAndWait());
					return null;
				});

				// *** This is temporary ***
				try {
					partner.close();
				} catch (IOException ex) {
					LOGGER.log(Level.SEVERE, "Failed to close partner", ex);
				}
				// *************************
			}).exceptionally(ex -> {
				String msg = "Failed to find a partner";
				LOGGER.log(Level.WARNING, msg, ex);
				Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, msg).showAndWait());
				//TODO Might want to clean up some stuff with the UI
				return null;
			});

			//TODO Give the user visual feedback that we are waiting for a player ...
//			Dialog<Void> loadingDialog = new Dialog<>();
//			loadingDialog.initOwner(((Node)event.getTarget()).getScene().getWindow());
//			loadingDialog.initModality(Modality.WINDOW_MODAL);
//			loadingDialog.setContentText("Waiting for a player...");
//			ProgressIndicator progress = new ProgressIndicator();
//			Button btnCancel = new Button("Cancel");
//			btnCancel.setOnAction(action -> {
//
//			});
//			loadingDialog.getDialogPane().setContent(new VBox(progress, btnCancel));
//			loadingDialog.showAndWait();
		} catch (IOException e) {
			String msg = "Failed to establish a connection to server";
			LOGGER.log(Level.WARNING, msg, e);
			new Alert(Alert.AlertType.WARNING, msg).showAndWait();
		}
	}
}
