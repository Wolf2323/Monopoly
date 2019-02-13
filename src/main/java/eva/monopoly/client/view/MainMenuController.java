package eva.monopoly.client.view;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import eva.monopoly.api.network.client.Client;
import eva.monopoly.client.Monopoly;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
	@FXML
	MenuBar menuBar;
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
				goToGameLobby(newWindow, ipField.getText(), portField.getText(), event);
			}
		});
		ipAndPort.getChildren().addAll(ipField, separator, portField);
		Button okBttn = new Button("Continue");
		Button cancelBttn = new Button("Cancel");
		okBttn.setOnAction(e -> {
			goToGameLobby(newWindow, ipField.getText(), portField.getText(), event);
		});
		cancelBttn.setOnAction(e -> newWindow.close());
		VBox layout = new VBox(10);
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
		Stage window = (Stage) menuBar.getScene().getWindow();
		window.setScene(startUp);
		window.show();
	}

	private void goToGameLobby(Stage stage, String ip, String port, ActionEvent event) {
		/*
		 * try { client = new Client(ip, Integer.parseInt(port), uName, null); }
		 * catch (NumberFormatException e2) { // TODO Auto-generated catch block
		 * e2.printStackTrace(); } catch (UnknownHostException e2) { // TODO
		 * Auto-generated catch block e2.printStackTrace(); } catch (IOException
		 * e2) { // TODO Auto-generated catch block e2.printStackTrace(); }
		 */
		try {
			Monopoly.initializeClient(ip, Integer.parseInt(port), uName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Stage newWindow = stage;
		newWindow.setTitle("Pre-Game Lobby");
		newWindow.setMinWidth(500);
		Label tabelLabel = new Label("Players");
		tabelLabel.setFont(new Font("Arial", 20));
		TableView connectedPlayers = new TableView();
		TableColumn name = new TableColumn("Name");
		TableColumn pawn = new TableColumn("Pawn");
		TableColumn ready = new TableColumn("Ready");
		name.setPrefWidth(200);
		pawn.setPrefWidth(100);
		ready.setPrefWidth(100);
		connectedPlayers.getColumns().addAll(name, pawn, ready);
		HBox bottom = new HBox(10);
		bottom.setPadding(new Insets(10, 10, 10, 10));
		bottom.setAlignment(Pos.CENTER);
		VBox buttons = new VBox(10);
		buttons.setPadding(new Insets(10, 10, 10, 10));
		Button readyBttn = new Button("Ready");
		Button cancelBttn = new Button("Cancel");
		buttons.getChildren().addAll(readyBttn, cancelBttn);
		VBox pawnBox = new VBox(10);
		pawnBox.setPadding(new Insets(10, 10, 10, 10));
		Label pawnLabel = new Label("Choose Pawn");
		ComboBox<String> pawns = new ComboBox<>();
		ObservableList<String> c = FXCollections.observableArrayList();
		c.addAll("TOPHAT", "THIMBLE", "IRON", "SHOE", "BATTLESHIP", "WHEELBARROW", "DOG", "CAR");
		pawns.setItems(c);
		pawns.setOnAction((e -> {
			// TODO notify server of Pawn Selection, check if possible, notify
			// user if pawn is not available
		}));
		pawnBox.getChildren().addAll(pawnLabel, pawns);
		bottom.getChildren().addAll(buttons, pawnBox);
		readyBttn.setOnAction(e -> {
			// TODO Code is placeholder,check if a pawn is selected(notify if
			// not), notify Server of ready status
			Parent gameBoardParent;
			try {
				newWindow.close();
				gameBoardParent = FXMLLoader.load(getClass().getResource("gameBoard.fxml"));
				Scene gameBoard = new Scene(gameBoardParent);
				Stage window = (Stage) menuBar.getScene().getWindow();
				window.setScene(gameBoard);
				window.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		cancelBttn.setOnAction(e -> newWindow.close());
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.getChildren().addAll(tabelLabel, connectedPlayers, bottom);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		newWindow.setScene(scene);
	}

	public void menuClose(ActionEvent event) throws IOException {
		Platform.exit();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}
