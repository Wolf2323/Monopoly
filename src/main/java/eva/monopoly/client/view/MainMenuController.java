package eva.monopoly.client.view;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import eva.monopoly.api.network.client.Client;
import eva.monopoly.client.MonopolyClient;
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
import javafx.scene.paint.Color;
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
	private boolean isPawnErrorNotSelected = false;
	private boolean isPawnErrorWrongSelected = false;
	private String uName;
	private Pattern p = Pattern.compile("^" + "(((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}" // Domain name
			+ "|" + "localhost" // localhost
			+ "|" + "(([0-9]{1,3}\\.){3})[0-9]{1,3})" // Ip
			+ ":" + "[0-9]{1,5}$"); // Port

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
				try {
					goToGameLobby(newWindow, ipField.getText(), portField.getText(), event);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		ipAndPort.getChildren().addAll(ipField, separator, portField);
		Button okBttn = new Button("Continue");
		Button cancelBttn = new Button("Cancel");
		okBttn.setOnAction(e -> {
			try {
				goToGameLobby(newWindow, ipField.getText(), portField.getText(), event);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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

	private void goToGameLobby(Stage stage, String ip, String port, ActionEvent event) throws IOException {
		// TODO entferene HACK
		if (!p.matcher(ip + ":" + port).matches() && !ip.equalsIgnoreCase("Hack")) {
			errorWindow(stage, event, ip, port, "input");
			return;
		}
		try {
			MonopolyClient.initializeClient(ip, Integer.parseInt(port), uName);
		} catch (IOException e) {
			// TODO Better Exception handling
			e.printStackTrace();
			if (!ip.equalsIgnoreCase("Hack")) {
				errorWindow(stage, event, ip, port, "connection");
				return;
			}
		}
		Stage newWindow = stage;
		newWindow.setTitle("Pre-Game Lobby");
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
			if (pawns.getSelectionModel().getSelectedItem() != null) {
				String pawnSelection = pawns.getSelectionModel().getSelectedItem();
				MonopolyClient.notifyServerPawnChanged(pawnSelection);
			}
		}));
		pawnBox.getChildren().addAll(pawnLabel, pawns);
		bottom.getChildren().addAll(buttons, pawnBox);
		readyBttn.setOnAction(e -> {
			// TODO Code is placeholder,check if a pawn is selected(notify if
			// not), notify Server of ready status
			Parent gameBoardParent;
			try {
				if (pawns.getSelectionModel().getSelectedItem() == null) {
					if (!isPawnErrorNotSelected) {
						pawns.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
						Label errorLabel = new Label("Bitte eine Spielfigur auswählen!");
						errorLabel.setTextFill(Color.RED);
						pawnBox.getChildren().add(errorLabel);
						isPawnErrorNotSelected = true;
					}
				} else {
					newWindow.close();
					gameBoardParent = FXMLLoader.load(getClass().getResource("gameBoard.fxml"));
					Scene gameBoard = new Scene(gameBoardParent);
					Stage window = (Stage) menuBar.getScene().getWindow();
					window.setScene(gameBoard);
					window.show();
				}
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

	private void errorWindow(Stage stage, ActionEvent event, String ip, String port, String type) {
		Stage newWindow = stage;
		newWindow.setTitle("Find Game");
		HBox ipAndPort = new HBox();
		ipAndPort.setAlignment(Pos.CENTER);
		Label separator = new Label();
		separator.setText(" : ");
		TextField ipField = new TextField();
		ipField.setPromptText("Enter IP");
		ipField.setText(ip);
		ipField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
		TextField portField = new TextField();
		portField.setPromptText("Enter Port");
		portField.setText(port);
		portField.setMaxWidth(100);
		portField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
		portField.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				try {
					goToGameLobby(newWindow, ipField.getText(), portField.getText(), event);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		ipAndPort.getChildren().addAll(ipField, separator, portField);
		Button okBttn = new Button("Continue");
		Button cancelBttn = new Button("Cancel");
		okBttn.setOnAction(e -> {
			try {
				goToGameLobby(newWindow, ipField.getText(), portField.getText(), event);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		cancelBttn.setOnAction(e -> newWindow.close());
		Label errorMessage = new Label();
		if (type.equalsIgnoreCase("input")) {
			errorMessage.setText("Keine gültige IP und Port Eingabe!");
		} else if (type.equalsIgnoreCase("connection")) {
			errorMessage.setText("Verbindung zu diesem Server nicht möglich!");
		}
		errorMessage.setTextFill(Color.RED);
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.getChildren().addAll(ipAndPort, errorMessage, okBttn, cancelBttn);
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
