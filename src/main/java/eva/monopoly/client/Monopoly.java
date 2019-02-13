package eva.monopoly.client;

import java.io.IOException;
import java.net.UnknownHostException;

import eva.monopoly.api.network.client.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Monopoly extends Application {
	private static Client client;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("view/startupWindow.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("view/gameBoard.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void initializeClient(String ip, int port, String nick) throws UnknownHostException, IOException {
		client = new Client(ip, port, nick, null);
	}
}
