package eva.monopoly.client.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import eva.monopoly.api.game.player.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GameBoardController implements Initializable {
	@FXML
	Button cards;
	@FXML
	Button streets;
	@FXML
	TextField money;
	@FXML
	Label playerName;
	@FXML
	MenuBar menuBar;

	private String uName;
	
	private static GameBoardController instance;

	public static GameBoardController getInstance() {
		return instance;
	}

	public GameBoardController() {
		instance = this;
	}

	public void initData(String uName) {
		this.uName = uName;
		playerName.setText(uName);
	}

	private void refreshMoney(String amount) {
		// show Money of the Player
		money.setText("" + amount);
	}

	public void showCards() {
		// get Cards from Player and show it in an alert window
	}

	public void showStreets() {
		// get Streets from Player and show it in an alert window
	}

	public void startRound() {
		/*
		 * show Dice roll, show data of target field, give button options to buy
		 * or ignore if field is not occupied, give option to place house if
		 * target field and corresponding street is owned by the player
		 */
	}

	public void menuClose(ActionEvent event) throws IOException {
		Platform.exit();
	}

	public void menuChangeNick(ActionEvent event) throws IOException {
		Parent startUpParent = FXMLLoader.load(getClass().getResource("startupWindow.fxml"));
		Scene startUp = new Scene(startUpParent);
		Stage window = (Stage) menuBar.getScene().getWindow();
		window.setScene(startUp);
		window.show();
	}

	public void menuMainMenu(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("mainMenu.fxml"));
		Parent mainMenuParent = loader.load();
		Scene mainMenu = new Scene(mainMenuParent);
		Stage window = (Stage) menuBar.getScene().getWindow();
		window.setScene(mainMenu);
		window.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}
