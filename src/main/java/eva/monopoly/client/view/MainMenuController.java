package eva.monopoly.client.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import eva.monopoly.api.game.player.Player;
import eva.monopoly.api.game.player.Player.Pawn;
import eva.monopoly.client.MonopolyClient;
import eva.monopoly.client.utils.PlayerLobby;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private VBox layout;
	private Stage newWindow;
	private boolean isPawnErrorNotSelected = false;
	private boolean isPawnErrorWrongSelected = false;
	private ObservableList<PlayerLobby> tableItems;
	private String uName;
	private TableView<PlayerLobby> connectedPlayers = new TableView<>();
	private Pattern p = Pattern.compile("^" + "(((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}" // Domain
																									// name
			+ "|" + "localhost" // localhost
			+ "|" + "(([0-9]{1,3}\\.){3})[0-9]{1,3})" // Ip
			+ ":" + "[0-9]{1,5}$"); // Port

	private static MainMenuController instance;

	public static MainMenuController getInstance() {
		return instance;
	}

	public MainMenuController() {
		instance = this;
	}

	public void initData(String nickname) {
		uName = nickname;
		uNameLabel.setText("Hello " + uName);
	}

	// find Game with a different host, show window to enter ip
	public void findGame(ActionEvent event) {

		newWindow = new Stage();
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
		portField.setOnKeyPressed(e -> { // Enter Button confirms input
			if (e.getCode() == KeyCode.ENTER) {
				try {
					goToGameLobby(ipField.getText(), portField.getText(), event);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		ipAndPort.getChildren().addAll(ipField, separator, portField);
		Button okBttn = new Button("Continue");
		Button cancelBttn = new Button("Cancel");
		okBttn.setOnAction(e -> { // switch to the Pre-GameLobby
			try {
				goToGameLobby(ipField.getText(), portField.getText(), event);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		cancelBttn.setOnAction(e -> newWindow.close()); // close Window
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.getChildren().addAll(ipAndPort, okBttn, cancelBttn);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		newWindow.setScene(scene);
		newWindow.showAndWait();
	}

	// switch to startup screen
	public void changeNick(ActionEvent event) throws IOException {
		Parent startUpParent = FXMLLoader.load(getClass().getResource("startupWindow.fxml"));
		Scene startUp = new Scene(startUpParent);
		Stage window = (Stage) menuBar.getScene().getWindow();
		window.setScene(startUp);
		window.show();
	}

	// try to connect to Server, show Pre-Game Lobby
	private void goToGameLobby(String ip, String port, ActionEvent event) throws IOException {
		// TODO entferene HACK
		if (!p.matcher(ip + ":" + port).matches() && !ip.equalsIgnoreCase("Hack")) {
			errorWindow(event, ip, port, "input");
			return;
		}
		try {
			MonopolyClient.initializeClient(ip, Integer.parseInt(port), uName);
			MonopolyClient.getConnectedClients();
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
			if (!ip.equalsIgnoreCase("Hack")) {
				errorWindow(event, ip, port, "connection");
				return;
			} else {
				addPlayer(uName);
			}
		}
		newWindow.setTitle("Pre-Game Lobby");
		Label tabelLabel = new Label("Players");
		tabelLabel.setFont(new Font("Arial", 20));
		TableColumn<PlayerLobby, String> name = new TableColumn<>("Name");
		TableColumn<PlayerLobby, String> pawn = new TableColumn<>("Pawn");
		TableColumn<PlayerLobby, String> ready = new TableColumn<>("Ready");
		name.setPrefWidth(200);
		pawn.setPrefWidth(100);
		ready.setPrefWidth(100);
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		pawn.setCellValueFactory(new PropertyValueFactory<>("pawn"));
		ready.setCellValueFactory(new PropertyValueFactory<>("readyStatus"));
		connectedPlayers.getColumns().addAll(name, pawn, ready);
		connectedPlayers.setItems(tableItems);
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
		pawns.setOnAction((e -> { // Pawn Selection Action
			if (pawns.getSelectionModel().getSelectedItem() != null) {
				String pawnSelection = pawns.getSelectionModel().getSelectedItem();
				// TODO entferne if Abfragen

				/*
				 * if (pawnSelection.equalsIgnoreCase("TOPHAT")) {
				 * changePawn(uName, Pawn.TOPHAT); } else if
				 * (pawnSelection.equalsIgnoreCase("THIMBLE")) {
				 * changePawn(uName, Pawn.THIMBLE); } else if
				 * (pawnSelection.equalsIgnoreCase("IRON")) { changePawn(uName,
				 * Pawn.IRON); } else if
				 * (pawnSelection.equalsIgnoreCase("SHOE")) { changePawn(uName,
				 * Pawn.SHOE); } else if
				 * (pawnSelection.equalsIgnoreCase("BATTLESHIP")) {
				 * changePawn(uName, Pawn.BATTLESHIP); } else if
				 * (pawnSelection.equalsIgnoreCase("WHEELBARROW")) {
				 * changePawn(uName, Pawn.WHEELBARROW); } else if
				 * (pawnSelection.equalsIgnoreCase("DOG")) { changePawn(uName,
				 * Pawn.DOG); } else if (pawnSelection.equalsIgnoreCase("CAR"))
				 * { changePawn(uName, Pawn.CAR); }
				 */
				MonopolyClient.notifyServerPawnChanged(pawnSelection);
			}
		}));
		pawnBox.getChildren().addAll(pawnLabel, pawns);
		bottom.getChildren().addAll(buttons, pawnBox);
		readyBttn.setOnAction(e -> { // send Server notification for ready,
										// check if Pawn is selected
			// TODO Code is placeholder,check if a pawn is selected(notify if
			// not), notify Server of ready status
			if (pawns.getSelectionModel().getSelectedItem() == null) {
				if (!isPawnErrorNotSelected) {
					pawns.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
					Label errorLabel = new Label("Bitte eine Spielfigur auswählen!");
					errorLabel.setTextFill(Color.RED);
					pawnBox.getChildren().add(errorLabel);
					isPawnErrorNotSelected = true;
				}
			} else {
				// TODO entferne changeReady()
				// changeReady(uName);
				// readyNotPossible();
				MonopolyClient.notifyServerReadyStatus();
			}
		});
		cancelBttn.setOnAction(e -> {
			newWindow.close();
			MonopolyClient.disconnect();
		}); // close window
		layout = new VBox(10);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.getChildren().addAll(tabelLabel, connectedPlayers, bottom);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		newWindow.setScene(scene);
	}

	// show errors when somethings wrong in the connection screen
	private void errorWindow(ActionEvent event, String ip, String port, String type) {
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
					goToGameLobby(ipField.getText(), portField.getText(), event);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		ipAndPort.getChildren().addAll(ipField, separator, portField);
		Button okBttn = new Button("Continue");
		Button cancelBttn = new Button("Cancel");
		okBttn.setOnAction(e -> {
			try {
				goToGameLobby(ipField.getText(), portField.getText(), event);
			} catch (IOException e1) {
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

	public void addPlayer(String name, Pawn pawn) {
		tableItems.add(new PlayerLobby(name, pawnToString(pawn)));
		connectedPlayers.refresh();

	}

	public void addPlayer(String name) {
		tableItems.add(new PlayerLobby(name));
		connectedPlayers.refresh();

	}

	public void removePlayer(String name) {
		for (PlayerLobby p : tableItems) {
			if (p.getName().equals(name)) {
				tableItems.remove(p);
			}
		}
		connectedPlayers.refresh();

	}

	public void changePawn(String name, Pawn pawn) {
		for (PlayerLobby p : tableItems) {
			if (p.getName().equals(name)) {
				p.setPawn(pawnToString(pawn));
			}
		}
		connectedPlayers.refresh();

	}

	public void readyNotPossible() {
		if (!isPawnErrorWrongSelected) {
			Label errorMessage = new Label("Diese Spielfigur ist nicht verfügbar!");
			errorMessage.setTextFill(Color.RED);
			VBox pawnBox = ((VBox) ((HBox) layout.getChildren().get(2)).getChildren().get(1));
			ComboBox<String> pawns = (ComboBox<String>) pawnBox.getChildren().get(1);
			pawns.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			errorMessage.setTextFill(Color.RED);
			pawnBox.getChildren().add(errorMessage);
			isPawnErrorWrongSelected = true;
		}
	}

	public void changeReady(String name) {
		for (PlayerLobby p : tableItems) {
			if (p.getName().equals(name)) {
				p.setReadyStatus();
			}
		}
		connectedPlayers.refresh();

	}

	public void gameStart() {
		newWindow.close();
		Parent gameBoardParent;
		try {
			gameBoardParent = FXMLLoader.load(getClass().getResource("gameBoard.fxml"));
			Scene gameBoard = new Scene(gameBoardParent);
			GameBoardController menuControl = GameBoardController.getInstance();
			menuControl.initData(uName);
			Stage window = (Stage) menuBar.getScene().getWindow();
			window.setScene(gameBoard);
			window.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableItems = FXCollections.observableArrayList();
	}

	public String pawnToString(Pawn pawn) {
		switch (pawn) {
		case TOPHAT:
			return "Tophat";
		case IRON:
			return "Iron";
		case SHOE:
			return "Shoe";
		case BATTLESHIP:
			return "Battleship";
		case WHEELBARROW:
			return "Wheelbarrow";
		case DOG:
			return "Dog";
		case CAR:
			return "Car";
		case THIMBLE:
			return "Thimble";
		default:
			return null;
		}
	}

}
