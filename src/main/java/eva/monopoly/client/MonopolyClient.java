package eva.monopoly.client;

import java.io.IOException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eva.monopoly.api.network.client.Client;
import eva.monopoly.api.network.messages.PlayerStatusChanged;
import eva.monopoly.api.network.messages.PlayerStatusChanged.ConnectionState;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MonopolyClient extends Application {
	public final static Logger LOG = LoggerFactory.getLogger(MonopolyClient.class);
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
		client = new Client(ip, port, nick, (con, e) -> {
			try {
				con.closeConnection();
				client = null;
				// TODO hat verbindung unterbrochen, erneuter verbingungsaufbau?
				// initializeClient(ip, port, nick);
			} catch (IOException e1) {
			}
		});
		registerHandler();

		client.getSocketConnector().sendMessage(new PlayerStatusChanged(ConnectionState.CONNECTED));
	}

	private static void registerHandler() {
		client.getSocketConnector().registerHandle(PlayerStatusChanged.class, (con, state) -> {
			switch (state.getState()) {
			case CONNECTED:
				LOG.info("Der Spieler " + state.getName() + " hat sich zum Spiel verbunden");
				return;
			case DISCONNECTED:
				LOG.info("Der Spieler " + state.getName() + " hat die Verbindung getrennt");
				return;
			case RECONNECTED:
				LOG.info("Der Spieler " + state.getName() + " hat die Verbindung wiederhergestellt");
				return;
			case LOSTCONNECTION:
				LOG.info("Der Spieler " + state.getName() + " hat die Verbindung verloren");
				return;
			}
		});
	}
}
