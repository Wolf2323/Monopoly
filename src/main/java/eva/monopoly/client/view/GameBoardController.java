package eva.monopoly.client.view;

import java.net.URL;
import java.util.ResourceBundle;

import eva.monopoly.api.game.player.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GameBoardController implements Initializable {
	@FXML
	Button cards;
	@FXML
	Button streets;
	@FXML
	TextField money;
	@FXML
	Label playerName;

	private String uName;

	public void initData(String uName) {
		this.uName = uName;
	}

	private void refreshMoney(Player player) {
		// show Money of the Player
		money.setText("" + player.getMoney());
	}

	public void showCards() {
		// get Cards from Player and show it in an alert window
	}

	public void showStreets() {
		// get Streets from Player and show it in an alert window
	}

	private void showNickname(Player player) {
		playerName.setText(player.getName());
	}

	public void showRoundData() {
		/*
		 * show Dice roll, show data of target field, give button options to buy or
		 * ignore if field is not occupied, give option to place house if target field
		 * and corresponding street is owned by the player
		 */
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		playerName.setText(uName);
	}
}
