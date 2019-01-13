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
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("mainMenu.fxml"));
		Parent mainMenuParent = loader.load();
		Scene mainMenu = new Scene(mainMenuParent);
		String uName = nickname.getText();
		MainMenuController menuControl = loader.getController();
		menuControl.initData(uName);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(mainMenu);
		window.show();
	}
}

