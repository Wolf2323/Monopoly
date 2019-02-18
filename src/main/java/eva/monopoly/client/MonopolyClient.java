package eva.monopoly.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eva.monopoly.api.game.player.Player.Pawn;
import eva.monopoly.api.network.api.SocketConnector;
import eva.monopoly.api.network.client.Client;
import eva.monopoly.api.network.messages.BuyStreet;
import eva.monopoly.api.network.messages.CardPulled;
import eva.monopoly.api.network.messages.GameStateChanged;
import eva.monopoly.api.network.messages.GameStateChanged.GameState;
import eva.monopoly.api.network.messages.GetCards;
import eva.monopoly.api.network.messages.GetConnectedClients;
import eva.monopoly.api.network.messages.GetPlayers;
import eva.monopoly.api.network.messages.GetStreets;
import eva.monopoly.api.network.messages.PawnChanged;
import eva.monopoly.api.network.messages.PlayerStatusChanged;
import eva.monopoly.api.network.messages.PlayerStatusChanged.ConnectionState;
import eva.monopoly.api.network.messages.RollDice;
import eva.monopoly.api.network.messages.StartStopRound;
import eva.monopoly.api.network.messages.StreetBuyed;
import eva.monopoly.api.network.messages.StreetEntered;
import eva.monopoly.api.network.messages.Unjail;
import eva.monopoly.api.network.messages.Unjail.UnjailReason;
import eva.monopoly.client.view.GameBoardController;
import eva.monopoly.client.view.MainMenuController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MonopolyClient extends Application {
	public final static Logger LOG = LoggerFactory.getLogger(MonopolyClient.class);
	private static Client client;
	private static String name;
	private static boolean connected = false;
	private static Thread toInterrupt;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("view/startupWindow.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("view/gameBoard.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void initializeClient(String ip, int port, String nick) throws UnknownHostException, IOException {
		client = new Client(ip, port, nick, (con, e) -> {
			try {
				con.closeConnection();
			} catch (IOException e1) {
			}
			client = null;
			// TODO hat verbindung unterbrochen, erneuter verbingungsaufbau?
			// initializeClient(ip, port, nick);
		});
		registerHandler();

		client.getSocketConnector().sendMessage(new PlayerStatusChanged(name, ConnectionState.CONNECTED));
		connected = true;
	}

	private static void registerHandler() {
		client.getSocketConnector().registerHandle(PlayerStatusChanged.class, (con, state) -> {
			switch (state.getState()) {
			case CONNECTED:
				LOG.info("Der Spieler " + state.getName() + " hat sich zum Spiel verbunden");
				if (MainMenuController.getInstance().isGameNotStarted()) {
					MainMenuController.getInstance().addPlayer(state.getName());
				}
				return;
			case DISCONNECTED:
				LOG.info("Der Spieler " + state.getName() + " hat die Verbindung getrennt");
				if (MainMenuController.getInstance().isGameNotStarted()) {
					MainMenuController.getInstance().removePlayer(state.getName());
				}
				if (state.getName().equals(name)) {
					Platform.runLater(() -> {
						MainMenuController.getInstance().connectionRefused();
					});
					disconnect();
				}
				return;
			case RECONNECTED:
				LOG.info("Der Spieler " + state.getName() + " hat die Verbindung wiederhergestellt");
				if (MainMenuController.getInstance().isGameNotStarted()) {
					MainMenuController.getInstance().addPlayer(state.getName());
				}
				if (state.getName().equals(name)) {
					Platform.runLater(() -> {
						MainMenuController.getInstance().gameStart();
					});
				}
				return;
			case LOSTCONNECTION:
				LOG.info("Der Spieler " + state.getName() + " hat die Verbindung verloren");
				if (MainMenuController.getInstance().isGameNotStarted()) {
					MainMenuController.getInstance().removePlayer(state.getName());
				}
				return;
			}
		});
		client.getSocketConnector().registerHandle(PawnChanged.class, (con, state) -> {
			switch (state.getPawn()) {
			case TOPHAT:
				LOG.info("Der Spieler " + state.getName() + " hat seinen Pawn zu Tophat gewechselt");
				MainMenuController.getInstance().changePawn(state.getName(), state.getPawn());
				return;
			case IRON:
				LOG.info("Der Spieler " + state.getName() + " hat seinen Pawn zu Iron gewechselt");
				MainMenuController.getInstance().changePawn(state.getName(), state.getPawn());
				return;
			case SHOE:
				LOG.info("Der Spieler " + state.getName() + " hat seinen Pawn zu Shoe gewechselt");
				MainMenuController.getInstance().changePawn(state.getName(), state.getPawn());
				return;
			case BATTLESHIP:
				LOG.info("Der Spieler " + state.getName() + " hat seinen Pawn zu Battleship gewechselt");
				MainMenuController.getInstance().changePawn(state.getName(), state.getPawn());
				return;
			case WHEELBARROW:
				LOG.info("Der Spieler " + state.getName() + " hat seinen Pawn zu Wheelbarrow gewechselt");
				MainMenuController.getInstance().changePawn(state.getName(), state.getPawn());
				return;
			case DOG:
				LOG.info("Der Spieler " + state.getName() + " hat seinen Pawn zu Dog gewechselt");
				MainMenuController.getInstance().changePawn(state.getName(), state.getPawn());
				return;
			case CAR:
				LOG.info("Der Spieler " + state.getName() + " hat seinen Pawn zu Car gewechselt");
				MainMenuController.getInstance().changePawn(state.getName(), state.getPawn());
				return;
			case THIMBLE:
				LOG.info("Der Spieler " + state.getName() + " hat seinen Pawn zu Thimble gewechselt");
				MainMenuController.getInstance().changePawn(state.getName(), state.getPawn());
				return;
			}
		});
		client.getSocketConnector().registerHandle(GameStateChanged.class, (con, state) -> {
			switch (state.getGameState()) {
			case PREGAME:
				LOG.info("Der Spieler " + state.getName() + " ist nicht bereit");
				Platform.runLater(() -> {
					MainMenuController.getInstance().readyNotPossible();
				});
				return;
			case READY:
				LOG.info("Der Spieler " + state.getName() + " ist bereit");
				Platform.runLater(() -> {
					MainMenuController.getInstance().changeReady(state.getName());
				});

				return;
			case INGAME:
				LOG.info("Das Spiel startet");
				Platform.runLater(() -> {
					MainMenuController.getInstance().gameStart();
				});

				return;
			case FINISHED:
				LOG.info("Das Spiel ist beendet");
				Platform.runLater(() -> {
					try {
						GameBoardController.getInstance().goToMainMenu();
						disconnect();
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				return;
			}
		});
		client.getSocketConnector().registerHandle(GetConnectedClients.class, (con, state) -> {
			for (Entry<String, eva.monopoly.api.network.messages.GetConnectedClients.Client> entry : state.getClients()
					.entrySet()) {
				if (!entry.getKey().equals(name)) {
					MainMenuController.getInstance().addPlayer(entry.getKey(), entry.getValue().getPlayerPawn(),
							entry.getValue().isReady());
				}
			}
		});
		client.getSocketConnector().registerHandle(GetPlayers.class, (con, state) -> {
			Platform.runLater(() -> {
				GameBoardController.getInstance().displayPlayers(state.getPlayers());
			});
		});
		client.getSocketConnector().registerHandle(GetCards.class, (con, state) -> {
			Platform.runLater(() -> {
				GameBoardController.getInstance().displayCards(state.getCards());
			});
		});
		client.getSocketConnector().registerHandle(GetStreets.class, (con, state) -> {
			Platform.runLater(() -> {
				GameBoardController.getInstance().displayStreets(state.getStreets(), true);
			});
		});
		client.getSocketConnector().registerHandle(StartStopRound.class, (con, state) -> {
			if (state.getName().equals(name)) {
				Platform.runLater(() -> {
					GameBoardController.getInstance().initializeGame(state.getPlayer());
					GameBoardController.getInstance().refreshTurnName(state.getName());
					GameBoardController.getInstance().startRound();
				});
				waitForInterrupt();
			}
			Platform.runLater(() -> {
				GameBoardController.getInstance().refreshTurnName(state.getName());
			});
			LOG.info("Der Spieler " + state.getName() + " ist am Zug");
		});
		client.getSocketConnector().registerHandle(RollDice.class, (con, state) -> {
			if (state.getName().equals(name)) {
				if (state.isDoubletsJail()) {
					Platform.runLater(() -> {
						GameBoardController.getInstance().jail();
					});
				} else {
					Platform.runLater(() -> {
						GameBoardController.getInstance().setDices(state.getAmount(), state.isDoublets());
					});
				}
				waitForInterrupt();
			} else {
				if (state.isDoublets()) {
					LOG.info("Der Spieler " + state.getName() + " hat " + state.getAmount()
							+ " mit einem Pasch gewürfelt");
				} else {
					LOG.info("Der Spieler " + state.getName() + " hat " + state.getAmount()
							+ " ohne einen Pasch gewürfelt");
				}
			}
		});
		client.getSocketConnector().registerHandle(StreetEntered.class, (con, state) -> {
			if (state.getName().equals(name)) {
				if (!state.getStreet().getName().equalsIgnoreCase("Los")) {
					Platform.runLater(() -> {
						GameBoardController.getInstance().showMoveData(state.getStreet(), state.getMoneyAmount(),
								state.getNewMoney());
					});
					waitForInterrupt();
				} else {
					Platform.runLater(() -> {
						GameBoardController.getInstance().showLos(state.getStreet(), state.getMoneyAmount(),
								state.getNewMoney());
					});

				}
			} else {
				LOG.info("Der Spieler " + state.getName() + " ist auf " + state.getStreet().getName() + " gelandet");
			}
		});
		client.getSocketConnector().registerHandle(CardPulled.class, (con, state) -> {
			if (state.getName().equals(name)) {
				Platform.runLater(() -> {
					GameBoardController.getInstance().showCard(state.getCard().getType(), state.getCard().getText(),
							state.getNewMoney());
				});
				if (GameBoardController.getInstance().getDoublets()) {
					waitForInterrupt();
				}
			}
		});
		client.getSocketConnector().registerHandle(StreetBuyed.class, (con, state) -> {
			LOG.info("Der Spieler " + state.getName() + " hat die Strasse " + state.getStreet().getName() + " gekauft");
			if (state.getName().equals(name)) {
				Platform.runLater(() -> {
					GameBoardController.getInstance().showBuyConfirmation(state.getStreet(), state.getNewMoney());
				});
				if (GameBoardController.getInstance().getDoublets()) {
					waitForInterrupt();
				}
			}
		});
	}

	public static void notifyServerPawnChanged(String pawnSelection) {
		if (pawnSelection.equalsIgnoreCase("Tophat")) {
			client.getSocketConnector().sendMessage(new PawnChanged(name, Pawn.TOPHAT));
		} else if (pawnSelection.equalsIgnoreCase("Thimble")) {
			client.getSocketConnector().sendMessage(new PawnChanged(name, Pawn.THIMBLE));
		} else if (pawnSelection.equalsIgnoreCase("Iron")) {
			client.getSocketConnector().sendMessage(new PawnChanged(name, Pawn.IRON));
		} else if (pawnSelection.equalsIgnoreCase("Shoe")) {
			client.getSocketConnector().sendMessage(new PawnChanged(name, Pawn.SHOE));
		} else if (pawnSelection.equalsIgnoreCase("Battleship")) {
			client.getSocketConnector().sendMessage(new PawnChanged(name, Pawn.BATTLESHIP));
		} else if (pawnSelection.equalsIgnoreCase("Wheelbarrow")) {
			client.getSocketConnector().sendMessage(new PawnChanged(name, Pawn.WHEELBARROW));
		} else if (pawnSelection.equalsIgnoreCase("Dog")) {
			client.getSocketConnector().sendMessage(new PawnChanged(name, Pawn.DOG));
		} else if (pawnSelection.equalsIgnoreCase("Car")) {
			client.getSocketConnector().sendMessage(new PawnChanged(name, Pawn.CAR));
		}
	}

	public static void notifyServerReadyStatus() {
		client.getSocketConnector().sendMessage(new GameStateChanged(name, GameState.READY));
	}

	public static void setName(String name) {
		MonopolyClient.name = name;
	}

	public static void rollDice() {
		client.getSocketConnector().sendMessage(new RollDice(name));
	}

	public static void getConnectedClients() {
		client.getSocketConnector().sendMessage(new GetConnectedClients(name));

	}

	public static void disconnect() {
		if (connected) {
			client.getSocketConnector().sendMessage(new PlayerStatusChanged(name, ConnectionState.DISCONNECTED));
			client.closeConnection();
			connected = false;
		}
	}

	@Override
	public void stop() {
		System.out.println("stop");
		disconnect();
		SocketConnector.shutdownThreadPools();
		Platform.exit();
	}

	public static void unjail(String type) {
		if (type.equals("money")) {
			client.getSocketConnector().sendMessage(new Unjail(name, UnjailReason.PAYED));
		} else if (type.equals("card")) {
			client.getSocketConnector().sendMessage(new Unjail(name, UnjailReason.CARD));
		}
	}

	public static void buyStreet(boolean buy) {
		client.getSocketConnector().sendMessage(new BuyStreet(name, buy));
	}

	public static void bid(int amount) {
		// TODO client.getSocketConnector().sendMessage(new Bid(name, amount));
		// // TODO create ExchangeMEssage
	}

	public static void roundFinished() {
		client.getSocketConnector().sendMessage(new StartStopRound(name));
	}

	public static void getCards() {
		client.getSocketConnector().sendMessage(new GetCards(name));
	}

	public static void toInterruptInterrupt() {
		toInterrupt.interrupt();
	}

	private static void waitForInterrupt() {
		toInterrupt = Thread.currentThread();
		try {
			synchronized (toInterrupt) {
				toInterrupt.wait();
			}
		} catch (InterruptedException e) {
		}
	}

	public static void getStreets() {
		client.getSocketConnector().sendMessage(new GetStreets(name));
	}

	public static void getPlayers() {
		client.getSocketConnector().sendMessage(new GetPlayers(name));

	}
}
