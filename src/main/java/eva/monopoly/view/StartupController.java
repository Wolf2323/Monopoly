package eva.monopoly.view;

import java.io.IOException;

import eva.monopoly.game.player.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StartupController {
	@FXML
	TextField nickname;
	@FXML
	Button okBttn;
	public void enterNickname(ActionEvent event) throws IOException {
		// set Playername from Textfield nickname and switch to mainMenu.fxml
		nickname.getText();
		Parent mainMenuParent = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
		Scene mainMenu = new Scene(mainMenuParent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(mainMenu);
		window.show();
	}
}

