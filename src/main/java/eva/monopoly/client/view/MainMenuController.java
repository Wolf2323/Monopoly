package eva.monopoly.client.view;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import eva.monopoly.api.network.client.Client;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainMenuController implements Initializable {
	@FXML
	Button startBttn;
	@FXML
	Button findBttn;
	@FXML
	Button nicknameBttn;
	@FXML
	Label uNameLabel;
	private String uName;
	private Client client;

	public void initData(String nickname) {
		uName = nickname;
		uNameLabel.setText("Hello, " + uName);
	}

	public void findGame(ActionEvent event) {
		// find Game with a different host, show window to enter ip
		Stage newWindow = new Stage();
		newWindow.initModality(Modality.APPLICATION_MODAL);
		newWindow.setTitle("Find Game");
		newWindow.setMinWidth(500);
		HBox ipAndPort = new HBox();
		ipAndPort.setAlignment(Pos.CENTER);
		Label separator = new Label();
		separator.setText(" : ");
		TextField ipField = new TextField();
		ipField.setPromptText("Enter IP");
		TextField portField = new TextField();
		portField.setPromptText("Enter Port");
		portField.setMaxWidth(100);
		portField.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				sendIpAndPort(newWindow, ipField.getText(), portField.getText(), event);
			}
		});
		ipAndPort.getChildren().addAll(ipField, separator, portField);
		Button okBttn = new Button("Continue");
		Button cancelBttn = new Button("Cancel");
		okBttn.setOnAction(e -> {
			sendIpAndPort(newWindow, ipField.getText(), portField.getText(), event);
		});
		cancelBttn.setOnAction(e -> newWindow.close());
		VBox layout = new VBox(10);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.getChildren().addAll(ipAndPort, okBttn, cancelBttn);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		newWindow.setScene(scene);
		newWindow.showAndWait();
	}

	public void changeNick(ActionEvent event) throws IOException {
		Parent startUpParent = FXMLLoader.load(getClass().getResource("startupWindow.fxml"));
		Scene startUp = new Scene(startUpParent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(startUp);
		window.show();
	}

	private void sendIpAndPort(Stage stage, String ip, String port, ActionEvent event) {
		try {
			client = new Client(ip, Integer.parseInt(port), uName, null);
		} catch (NumberFormatException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		stage.close();
		Stage newWindow = new Stage();
		newWindow.initModality(Modality.APPLICATION_MODAL);
		newWindow.setTitle("Pre-Game Lobby");
		newWindow.setMinWidth(500);
		TableView connectedPlayers = new TableView();
		
		Button readyBttn = new Button("Ready");
		Button cancelBttn = new Button("Cancel");
		readyBttn.setOnAction(e -> {
			//TODO Code ist Platzhalter
			Parent gameBoardParent;
			try {
				gameBoardParent = FXMLLoader.load(getClass().getResource("gameBoard.fxml"));
				Scene gameBoard = new Scene(gameBoardParent);
				Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
				window.setScene(gameBoard);
				window.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		cancelBttn.setOnAction(e -> newWindow.close());
		VBox layout = new VBox(10);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.getChildren().addAll(readyBttn, cancelBttn);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		newWindow.setScene(scene);
		newWindow.showAndWait();
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}
