package eva.monopoly.view;

import eva.monopoly.game.player.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GameBoardController {
	@FXML
	Button cards;
	@FXML
	Button streets;
	@FXML
	TextField money;
	@FXML
	Label playerName;

	private void refreshMoney(Player player) {
		// show Money of the Player
		money.setText("" + player.getMoney());
	}

	private void showCards() {
		// get Cards from Player and show it in an alert window
	}

	private void showStreets() {
		// get Streets from Player and show it in an alert window
	}

	private void showNickname(Player player) {
		playerName.setText(player.getName());
	}
}