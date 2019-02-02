package eva.monopoly.view;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

	public void initData(String nickname) {
		uName = nickname;
	}

	private void startGame(ActionEvent event) throws UnknownHostException {
		InetAddress inet = InetAddress.getLocalHost();
		Stage newWindow = new Stage();
		newWindow.initModality(Modality.APPLICATION_MODAL);
		newWindow.setTitle("Start Game");
		newWindow.setMinWidth(500);
		Label label = new Label();
		label.setText("Current IP Address: " + inet.getHostAddress());
		Button okBttn = new Button("START");
		Button cancelBttn = new Button("Cancel");
		okBttn.setOnAction(e -> {
			newWindow.close();
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
		layout.getChildren().addAll(label, okBttn, cancelBttn);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		newWindow.setScene(scene);
		newWindow.showAndWait();
	}

	private void findGame(ActionEvent event) {
		// find Game with a different host, show window to enter ip
		Stage newWindow = new Stage();
		Boolean test = true;
		newWindow.initModality(Modality.APPLICATION_MODAL);
		newWindow.setTitle("Find Game");
		newWindow.setMinWidth(500);
		Label label = new Label();
		label.setText("Enter IP Address");
		TextField ipField = new TextField();
		Button okBttn = new Button("Continue");
		Button cancelBttn = new Button("Cancel");
		okBttn.setOnAction(e -> {
			// get IP from TextField ipField.getText();
			newWindow.close();
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
		layout.getChildren().addAll(label, okBttn, cancelBttn);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		newWindow.setScene(scene);
		newWindow.showAndWait();
	}

	private void changeNick(ActionEvent event) throws IOException {
		Parent startUpParent = FXMLLoader.load(getClass().getResource("startupWindow.fxml"));
		Scene startUp = new Scene(startUpParent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(startUp);
		window.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		uNameLabel.setText("Hello, " + uName);
	}
}
