package eva.monopoly.client.view;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import eva.monopoly.api.game.card.Card;
import eva.monopoly.api.game.card.Card.CardType;
import eva.monopoly.api.game.player.Player;
import eva.monopoly.api.game.street.BuyableStreet;
import eva.monopoly.api.game.street.NonBuyableStreet;
import eva.monopoly.api.game.street.Street;
import eva.monopoly.client.MonopolyClient;
import eva.monopoly.server.game.street.streets.BuyableFactoryStreet;
import eva.monopoly.server.game.street.streets.BuyableNormalStreet;
import eva.monopoly.server.game.street.streets.BuyableTrainstationStreet;
import eva.monopoly.server.game.street.streets.NonBuyableCommunityStreet;
import eva.monopoly.server.game.street.streets.NonBuyableEventStreet;
import eva.monopoly.server.game.street.streets.NonBuyableJailStreet;
import eva.monopoly.server.game.street.streets.NonBuyableMoneyStreet;
import eva.monopoly.server.game.street.streets.NonBuyableNormalStreet;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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
	@FXML
	Label turnName;
	@FXML
	VBox id0;
	@FXML
	VBox id1;
	@FXML
	VBox id2;
	@FXML
	VBox id3;
	@FXML
	VBox id4;
	@FXML
	VBox id5;
	@FXML
	VBox id6;
	@FXML
	VBox id7;
	@FXML
	VBox id8;
	@FXML
	VBox id9;
	@FXML
	VBox id10;
	@FXML
	VBox id11;
	@FXML
	VBox id12;
	@FXML
	VBox id13;
	@FXML
	VBox id14;
	@FXML
	VBox id15;
	@FXML
	VBox id16;
	@FXML
	VBox id17;
	@FXML
	VBox id18;
	@FXML
	VBox id19;
	@FXML
	VBox id20;
	@FXML
	VBox id21;
	@FXML
	VBox id22;
	@FXML
	VBox id23;
	@FXML
	VBox id24;
	@FXML
	VBox id25;
	@FXML
	VBox id26;
	@FXML
	VBox id27;
	@FXML
	VBox id28;
	@FXML
	VBox id29;
	@FXML
	VBox id30;
	@FXML
	VBox id31;
	@FXML
	VBox id32;
	@FXML
	VBox id33;
	@FXML
	VBox id34;
	@FXML
	VBox id35;
	@FXML
	VBox id36;
	@FXML
	VBox id37;
	@FXML
	VBox id38;
	@FXML
	VBox id39;
	private boolean doublets;
	private VBox[] streetIDs;
	private String uName;
	private Stage roundWindow;
	private VBox layout;
	private Player player;

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

	private void refreshMoney(int newMoney) {
		// show Money of the Player
		money.setText("" + newMoney);
	}

	public void showCards() {
		MonopolyClient.getCards();
	}

	public void displayCards(List<Card> cards) {
		Stage cardWindow = new Stage();
		cardWindow.setTitle("Karten");
		cardWindow.setMinWidth(400);
		cardWindow.setMinHeight(500);
		VBox cLayout = new VBox(10);
		for (Card c : cards) {
			HBox cardInfo = new HBox(10);
			Label name = new Label();
			if (c.getType().equals(CardType.COMMUNITY)) {
				name.setText("Gemeinschaftkarte");
			} else if (c.getType().equals(CardType.EVENT)) {
				name.setText("Ereigniskarte");
			}
			Button b = new Button("Text anzeigen");
			b.setOnAction(e -> {
				Stage cardTextWindow = new Stage();
				cardTextWindow.setTitle("Kartentext");
				cardTextWindow.setMinWidth(250);
				cardTextWindow.setMinHeight(300);
				TextArea text = new TextArea(c.getText());
				text.setEditable(false);
				Button closeButton = new Button("Schliessen");
				closeButton.setOnAction(ee -> {
					cardTextWindow.close();
				});
				VBox cTLayout = new VBox(10);
				cTLayout.setPadding(new Insets(10, 10, 10, 10));
				cTLayout.setAlignment(Pos.CENTER);
				cTLayout.getChildren().addAll(text, closeButton);
				Scene scene = new Scene(cTLayout);
				cardTextWindow.setScene(scene);
				cardTextWindow.showAndWait();
			});
			cardInfo.getChildren().addAll(name, b);
			cLayout.getChildren().add(cardInfo);
		}
		cLayout.setPadding(new Insets(10, 10, 10, 10));
		cLayout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(cLayout);
		cardWindow.setScene(scene);
		cardWindow.showAndWait();
	}

	public void showStreets() {
		// get Streets from Player and show it in an alert window
		MonopolyClient.getStreets();
	}

	public void displayStreets(List<BuyableStreet> list, boolean foreignAccess) {
		Stage streetWindow = new Stage();
		streetWindow.setTitle("Strassen");
		streetWindow.setMinWidth(400);
		streetWindow.setMinHeight(500);
		VBox sLayout = new VBox(10);
		for (Street s : list) {
			HBox streetInfo = new HBox(10);
			Label name = new Label(s.getName());
			Button b = new Button("Strasse anzeigen");
			b.setOnAction(e -> {
				Stage streetInfoWindow = new Stage();

				if (s instanceof BuyableStreet) {
					BuyableStreet buyStreet = (BuyableStreet) s;
					Rectangle color = new Rectangle();
					color.setWidth(116);
					color.setHeight(12);
					if (buyStreet.getGroup().equalsIgnoreCase("brown")) {
						color.setFill(Color.web("#6b004d"));
					} else if (buyStreet.getGroup().equalsIgnoreCase("lightblue")) {
						color.setFill(Color.web("#70a9ff"));
					} else if (buyStreet.getGroup().equalsIgnoreCase("pink")) {
						color.setFill(Color.web("#ff2370"));
					} else if (buyStreet.getGroup().equalsIgnoreCase("orange")) {
						color.setFill(Color.web("#ff8600"));
					} else if (buyStreet.getGroup().equalsIgnoreCase("red")) {
						color.setFill(Color.RED);
					} else if (buyStreet.getGroup().equalsIgnoreCase("yellow")) {
						color.setFill(Color.web("#ffe400"));
					} else if (buyStreet.getGroup().equalsIgnoreCase("green")) {
						color.setFill(Color.web("#04bf19"));
					} else if (buyStreet.getGroup().equalsIgnoreCase("blue")) {
						color.setFill(Color.web("#1306bd"));
					} else {
						color = null;
					}
					HBox stName = new HBox(10);
					Label lName = new Label("Name:");
					Label sName = new Label(buyStreet.getName());
					stName.getChildren().addAll(lName, sName);
					HBox rent = new HBox(10);
					VBox lRents = new VBox(10);
					VBox sRents = new VBox(10);
					Label lRent = new Label("Miete:");
					Button build = new Button("Bebauen");
					build.setDisable(true);
					if (buyStreet instanceof BuyableNormalStreet) {
						BuyableNormalStreet buyNormStreet = (BuyableNormalStreet) buyStreet;
						Label l0Rent = new Label("Kein Haus:");
						Label l1Rent = new Label("Ein Haus:");
						Label l2Rent = new Label("Zwei Häuser:");
						Label l3Rent = new Label("Drei Häuser:");
						Label l4Rent = new Label("Vier Häuser:");
						Label l5Rent = new Label("Hotel:");
						Label l6Rent = new Label("Gebäudekosten:");
						Label l7Rent = new Label("Häuser:");
						Label s0Rent = new Label("" + buyNormStreet.getNohouse());
						Label s1Rent = new Label("" + buyNormStreet.getOnehouse());
						Label s2Rent = new Label("" + buyNormStreet.getTwohouses());
						Label s3Rent = new Label("" + buyNormStreet.getThreehouses());
						Label s4Rent = new Label("" + buyNormStreet.getFourhouses());
						Label s5Rent = new Label("" + buyNormStreet.getHotel());
						Label s6Rent = new Label("" + buyNormStreet.getHousecost());
						String amount = buyNormStreet.getHouses() >= 5 ? "Hotel"
								: String.valueOf(buyNormStreet.getHouses());
						Label s7Rent = new Label("" + amount);
						lRents.getChildren().addAll(l0Rent, l1Rent, l2Rent, l3Rent, l4Rent, l5Rent, l6Rent, l7Rent);
						sRents.getChildren().addAll(s0Rent, s1Rent, s2Rent, s3Rent, s4Rent, s5Rent, s6Rent, s7Rent);
						if (!foreignAccess) {
							build.setDisable(false);
							build.setOnAction(ee -> {
								// TODO
							});
						}
					} else if (buyStreet instanceof BuyableFactoryStreet) {
						BuyableFactoryStreet factoryStreet = (BuyableFactoryStreet) buyStreet;
						Label l0Rent = new Label("Wenn man 1 Werke besitzt:");
						Label l1Rent = new Label("Wenn man 2 Werke besitzt:");
						Label s0Rent = new Label("" + factoryStreet.getFactorsingle());
						Label s1Rent = new Label("" + factoryStreet.getFactorgroup());
						lRents.getChildren().addAll(l0Rent, l1Rent);
						sRents.getChildren().addAll(s0Rent, s1Rent);

					} else if (buyStreet instanceof BuyableTrainstationStreet) {
						BuyableTrainstationStreet trainstationStreet = (BuyableTrainstationStreet) buyStreet;
						Label l0Rent = new Label("Wenn man 1 Bahnhöfe besitzt:");
						Label l1Rent = new Label("Wenn man 2 Bahnhöfe besitzt:");
						Label l2Rent = new Label("Wenn man 3 Bahnhöfe besitzt:");
						Label l3Rent = new Label("Wenn man 4 Bahnhöfe besitzt:");
						Label s0Rent = new Label("" + trainstationStreet.getOnestation());
						Label s1Rent = new Label("" + trainstationStreet.getTwostations());
						Label s2Rent = new Label("" + trainstationStreet.getThreestations());
						Label s3Rent = new Label("" + trainstationStreet.getFourstations());
						lRents.getChildren().addAll(l0Rent, l1Rent, l2Rent, l3Rent);
						sRents.getChildren().addAll(s0Rent, s1Rent, s2Rent, s3Rent);
					}
					rent.getChildren().addAll(lRents, sRents);
					HBox cost = new HBox(10);
					Label lCost = new Label("Kosten:");
					Label sCost = new Label("" + buyStreet.getCost()); // TODO
																		// DATA
																		// insert
					cost.getChildren().addAll(lCost, sCost);
					HBox mortg = new HBox(10);
					Label lMortg = new Label("Hypothek:");
					Label sMortg = new Label("" + buyStreet.getMortgageValue()); // TODO
																					// DATA
																					// insert
					cost.getChildren().addAll(lMortg, sMortg);
					VBox sILayout = new VBox(10);

					if (color != null) {
						sILayout.getChildren().add(color);
					}
					HBox actions = new HBox(10);
					Button close = new Button("Schliessen");
					close.setOnAction(ee -> {
						streetInfoWindow.close();
					});
					actions.getChildren().addAll(close, build);
					streetInfoWindow.setTitle("Strasseninfo");
					streetInfoWindow.setMinWidth(250);
					streetInfoWindow.setMinHeight(300);
					sILayout.setPadding(new Insets(10, 10, 10, 10));
					sILayout.setAlignment(Pos.CENTER);
					sILayout.getChildren().addAll(stName, lRent, rent, cost, mortg, actions);
					Scene scene = new Scene(sILayout);
					streetInfoWindow.setScene(scene);
					streetInfoWindow.showAndWait();

				}

			});
			streetInfo.getChildren().addAll(name, b);
			sLayout.getChildren().add(streetInfo);
		}
		sLayout.setPadding(new Insets(10, 10, 10, 10));
		sLayout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(sLayout);
		streetWindow.setScene(scene);
		streetWindow.showAndWait();
	}

	public void showPlayers() {
		MonopolyClient.getPlayers();
	}

	public void displayPlayers(List<Player> players) {
		Stage playerWindow = new Stage();
		playerWindow.setTitle("Spieler");
		playerWindow.setMinWidth(400);
		playerWindow.setMinHeight(500);
		VBox pLayout = new VBox(10);
		for (Player p : players) {
			HBox playerInfo = new HBox(10);
			Label name = new Label(p.getName());
			Label pawn = new Label(MainMenuController.getInstance().pawnToString(p.getPawn()));
			Label pos = new Label();
			if (streetIDs[p.getPositionIndex()].getChildren().get(0) instanceof Label) {
				pos.setText(((Label) streetIDs[p.getPositionIndex()].getChildren().get(0)).getText());
			} else if (streetIDs[p.getPositionIndex()].getChildren().get(0) instanceof Text) {
				pos.setText(((Text) streetIDs[p.getPositionIndex()].getChildren().get(0)).getText());
			} else {
				pos.setText(((Label) streetIDs[p.getPositionIndex()].getChildren().get(1)).getText());
			}
			Label lMoney = new Label("" + p.getMoney());
			Button b = new Button("Strassen");
			b.setOnAction(e -> {
				displayStreets(p.getStreets(), false);
			});
			playerInfo.getChildren().addAll(name, pawn, pos, lMoney, b);
			pLayout.getChildren().add(playerInfo);
		}
		pLayout.setPadding(new Insets(10, 10, 10, 10));
		pLayout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(pLayout);
		playerWindow.setScene(scene);
		playerWindow.showAndWait();
	}

	public void startRound() {
		/*
		 * show Dice roll, show data of target field, give button options to buy
		 * or ignore if field is not occupied, give option to place house if
		 * target field and corresponding street is owned by the player
		 */
		roundWindow = new Stage();
		roundWindow.initModality(Modality.APPLICATION_MODAL);
		roundWindow.setTitle("Rundenoptionen");
		roundWindow.setMinWidth(400);
		roundWindow.setMinHeight(500);
		if (player.isJailed()) { // give options to either pay 50, use card if
									// in possession, roll dice 3 times
			Label header = new Label("DU BIST IM GEFÄNGNIS!");
			Button firstOpt = new Button("Bezahle M 50");
			Button secondOpt = new Button("Gefängnis-frei Karte");
			Button thirdOpt = new Button("Würfeln");
			firstOpt.setOnAction(e -> {
				MonopolyClient.unjail("money");
				MonopolyClient.toInterruptInterrupt();

			});
			secondOpt.setOnAction(e -> {
				MonopolyClient.unjail("card");
				MonopolyClient.toInterruptInterrupt();

			});
			thirdOpt.setOnAction(e -> {
				MonopolyClient.rollDice();
				MonopolyClient.toInterruptInterrupt();
			});
			HBox dices = new HBox(10);
			dices.setAlignment(Pos.CENTER);
			VBox dice1 = new VBox(10);
			VBox dice2 = new VBox(10);
			Label header1 = new Label("Würfel");
			Label header2 = new Label("Pasch");
			Label diceNumber1 = new Label();
			Label diceNumber2 = new Label();
			dice1.getChildren().addAll(header1, diceNumber1);
			dice2.getChildren().addAll(header2, diceNumber2);
			dices.getChildren().addAll(dice1, dice2);
			layout = new VBox(10);
			layout.setPadding(new Insets(10, 10, 10, 10));
			layout.getChildren().addAll(header, firstOpt, secondOpt, thirdOpt, dices);
			layout.setAlignment(Pos.CENTER);
			Scene scene = new Scene(layout);
			roundWindow.setScene(scene);
			roundWindow.showAndWait();

		} else {
			HBox dices = new HBox(10);
			dices.setAlignment(Pos.CENTER);
			VBox dice1 = new VBox(10);
			VBox dice2 = new VBox(10);
			Label header1 = new Label("Augenzahl");
			Label header2 = new Label("Pasch");
			Label diceNumber1 = new Label();
			Label diceNumber2 = new Label();
			dice1.getChildren().addAll(header1, diceNumber1);
			dice2.getChildren().addAll(header2, diceNumber2);
			dices.getChildren().addAll(dice1, dice2);
			Button rollDice = new Button("Würfeln");
			rollDice.setOnAction(e -> { // send message to Server to roll Dice
				MonopolyClient.rollDice();
				MonopolyClient.toInterruptInterrupt();
			});
			layout = new VBox(10);
			layout.setPadding(new Insets(10, 10, 10, 10));
			layout.getChildren().addAll(dices, rollDice);
			layout.setAlignment(Pos.CENTER);
			Scene scene = new Scene(layout);
			roundWindow.setScene(scene);

			roundWindow.showAndWait();
		}
	}

	public void setDices(int dice, boolean doublets) {
		this.doublets = doublets;
		VBox root = (VBox) roundWindow.getScene().getRoot();
		Label diceNumber1 = (Label) ((VBox) ((HBox) root.getChildren().get(0)).getChildren().get(0)).getChildren()
				.get(1);
		Label diceNumber2 = (Label) ((VBox) ((HBox) root.getChildren().get(0)).getChildren().get(1)).getChildren()
				.get(1);
		Button continueBttn = (Button) (root.getChildren().get(1));
		diceNumber1.setText("" + dice);
		if (doublets) {
			diceNumber2.setText("Ja");
		} else {
			diceNumber2.setText("Nein");
		}
		continueBttn.setText("Bewegen");
		continueBttn.setOnAction(e -> {
			MonopolyClient.toInterruptInterrupt();
		});
	}

	public void jail() {
		player.jail();
		Label prisonTxt = new Label("DU KOMMST INS GEFÄNGNIS!");
		Button closeBttn = new Button("Runde beenden");
		closeBttn.setOnAction(e -> {
			roundWindow.close();
			MonopolyClient.toInterruptInterrupt();
			MonopolyClient.roundFinished();
		});
		layout = new VBox(10);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.getChildren().addAll(prisonTxt, closeBttn);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		roundWindow.setScene(scene);
	}

	public void showMoveData(Street street, Integer amount, int newMoney) { // show
																			// Data
																			// of
																			// the
																			// Street
																			// depending
																			// on
																			// type
																			// of
		// street and give corresponding options
		if (street instanceof BuyableStreet) {
			BuyableStreet buyStreet = (BuyableStreet) street;
			if (buyStreet.getOwner() == null) {
				Rectangle color = new Rectangle();
				color.setWidth(116);
				color.setHeight(12);
				if (buyStreet.getGroup().equalsIgnoreCase("brown")) {
					color.setFill(Color.web("#6b004d"));
				} else if (buyStreet.getGroup().equalsIgnoreCase("lightblue")) {
					color.setFill(Color.web("#70a9ff"));
				} else if (buyStreet.getGroup().equalsIgnoreCase("pink")) {
					color.setFill(Color.web("#ff2370"));
				} else if (buyStreet.getGroup().equalsIgnoreCase("orange")) {
					color.setFill(Color.web("#ff8600"));
				} else if (buyStreet.getGroup().equalsIgnoreCase("red")) {
					color.setFill(Color.RED);
				} else if (buyStreet.getGroup().equalsIgnoreCase("yellow")) {
					color.setFill(Color.web("#ffe400"));
				} else if (buyStreet.getGroup().equalsIgnoreCase("green")) {
					color.setFill(Color.web("#04bf19"));
				} else if (buyStreet.getGroup().equalsIgnoreCase("blue")) {
					color.setFill(Color.web("#1306bd"));
				} else {
					color = null;
				}
				HBox name = new HBox(10);
				Label lName = new Label("Name:");
				Label sName = new Label(buyStreet.getName());
				name.getChildren().addAll(lName, sName);
				HBox rent = new HBox(10);
				VBox lRents = new VBox(10);
				VBox sRents = new VBox(10);
				Label lRent = new Label("Miete:");
				if (buyStreet instanceof BuyableNormalStreet) {
					BuyableNormalStreet buyNormStreet = (BuyableNormalStreet) buyStreet;
					Label l0Rent = new Label("Kein Haus:");
					Label l1Rent = new Label("Ein Haus:");
					Label l2Rent = new Label("Zwei Häuser:");
					Label l3Rent = new Label("Drei Häuser:");
					Label l4Rent = new Label("Vier Häuser:");
					Label l5Rent = new Label("Hotel:");
					Label l6Rent = new Label("Gebäudekosten:");
					Label s0Rent = new Label("" + buyNormStreet.getNohouse());
					Label s1Rent = new Label("" + buyNormStreet.getOnehouse());
					Label s2Rent = new Label("" + buyNormStreet.getTwohouses());
					Label s3Rent = new Label("" + buyNormStreet.getThreehouses());
					Label s4Rent = new Label("" + buyNormStreet.getFourhouses());
					Label s5Rent = new Label("" + buyNormStreet.getHotel());
					Label s6Rent = new Label("" + buyNormStreet.getHousecost()); // Gebäudekosten
					lRents.getChildren().addAll(l0Rent, l1Rent, l2Rent, l3Rent, l4Rent, l5Rent, l6Rent);
					sRents.getChildren().addAll(s0Rent, s1Rent, s2Rent, s3Rent, s4Rent, s5Rent, s6Rent);
				} else if (buyStreet instanceof BuyableFactoryStreet) {
					BuyableFactoryStreet factoryStreet = (BuyableFactoryStreet) buyStreet;
					Label l0Rent = new Label("Wenn man 1 Werke besitzt:");
					Label l1Rent = new Label("Wenn man 2 Werke besitzt:");
					Label s0Rent = new Label("" + factoryStreet.getFactorsingle());
					Label s1Rent = new Label("" + factoryStreet.getFactorgroup());
					lRents.getChildren().addAll(l0Rent, l1Rent);
					sRents.getChildren().addAll(s0Rent, s1Rent);

				} else if (buyStreet instanceof BuyableTrainstationStreet) {
					BuyableTrainstationStreet trainstationStreet = (BuyableTrainstationStreet) buyStreet;
					Label l0Rent = new Label("Wenn man 1 Bahnhöfe besitzt:");
					Label l1Rent = new Label("Wenn man 2 Bahnhöfe besitzt:");
					Label l2Rent = new Label("Wenn man 3 Bahnhöfe besitzt:");
					Label l3Rent = new Label("Wenn man 4 Bahnhöfe besitzt:");
					Label s0Rent = new Label("" + trainstationStreet.getOnestation());
					Label s1Rent = new Label("" + trainstationStreet.getTwostations());
					Label s2Rent = new Label("" + trainstationStreet.getThreestations());
					Label s3Rent = new Label("" + trainstationStreet.getFourstations());
					lRents.getChildren().addAll(l0Rent, l1Rent, l2Rent, l3Rent);
					sRents.getChildren().addAll(s0Rent, s1Rent, s2Rent, s3Rent);
				}
				rent.getChildren().addAll(lRents, sRents);
				HBox cost = new HBox(10);
				Label lCost = new Label("Kosten:");
				Label sCost = new Label("" + buyStreet.getCost()); // TODO DATA
																	// insert
				cost.getChildren().addAll(lCost, sCost);
				HBox mortg = new HBox(10);
				Label lMortg = new Label("Hypothek:");
				Label sMortg = new Label("" + buyStreet.getMortgageValue()); // TODO
																				// DATA
																				// insert
				cost.getChildren().addAll(lMortg, sMortg);
				layout = new VBox(10);
				if (color != null) {
					layout.getChildren().add(color);
				}
				HBox actions = new HBox(10);
				Button buy = new Button("Kaufen");
				Button ignore = new Button("Ignorieren");
				buy.setOnAction(e -> {
					MonopolyClient.buyStreet(true); // TODO correct Parameter
					MonopolyClient.toInterruptInterrupt();
				});
				ignore.setOnAction(e -> {
					MonopolyClient.buyStreet(false);
					MonopolyClient.toInterruptInterrupt();

					continueRound();
				});
				actions.getChildren().addAll(buy, ignore);
				layout.setPadding(new Insets(10, 10, 10, 10));
				layout.getChildren().addAll(name, lRent, rent, cost, mortg, actions);
				layout.setAlignment(Pos.CENTER);
				Scene scene = new Scene(layout);
				roundWindow.setScene(scene);
			} else {

				// case normal street Owned

				Rectangle color = new Rectangle();
				color.setWidth(116);
				color.setHeight(12);
				if ("Placeholder".equals("brown")) {// TODO proper if handling
					color.setFill(Color.web("#6b004d"));
				} else if ("Placeholder".equals("lightblue")) {
					color.setFill(Color.web("#70a9ff"));
				} else if ("Placeholder".equals("pink")) {
					color.setFill(Color.web("#ff2370"));
				} else if ("Placeholder".equals("orange")) {
					color.setFill(Color.web("#ff8600"));
				} else if ("Placeholder".equals("red")) {
					color.setFill(Color.RED);
				} else if ("Placeholder".equals("yellow")) {
					color.setFill(Color.web("#ffe400"));
				} else if ("Placeholder".equals("green")) {
					color.setFill(Color.web("#04bf19"));
				} else if ("Placeholder".equals("blue")) {
					color.setFill(Color.web("#1306bd"));
				} else {
					color = null;
				}
				Label streetName = new Label(buyStreet.getName());
				HBox ownerName = new HBox(10);
				Label oName = new Label("Besitzer: ");
				Label owName = new Label(buyStreet.getOwner().getName());
				ownerName.getChildren().addAll(oName, owName);
				HBox costBox = new HBox(10);
				Label cost = new Label("Kosten: ");
				Label costs = new Label("" + amount);
				costBox.getChildren().addAll(cost, costs);
				Button pay = new Button("Bezahlen");
				pay.setOnAction(e -> {
					refreshMoney(newMoney);
					MonopolyClient.toInterruptInterrupt();
					continueRound();
				});
				layout = new VBox(10);
				if (color != null) {
					layout.getChildren().add(color);
				}
				layout.setPadding(new Insets(10, 10, 10, 10));
				layout.getChildren().addAll(streetName, ownerName, costBox, pay);
				layout.setAlignment(Pos.CENTER);
				Scene scene = new Scene(layout);
				roundWindow.setScene(scene);
			}
		} else if (street instanceof NonBuyableStreet) {
			NonBuyableStreet noBuyStreet = (NonBuyableStreet) street;
			if (noBuyStreet instanceof NonBuyableCommunityStreet || street instanceof NonBuyableEventStreet) {
				MonopolyClient.toInterruptInterrupt();// TODO
			} else if (noBuyStreet instanceof NonBuyableJailStreet) {
				jail();
			} else if (noBuyStreet instanceof NonBuyableMoneyStreet) {
				NonBuyableMoneyStreet noBuyMonStreet = (NonBuyableMoneyStreet) noBuyStreet;
				Label streetName = new Label(noBuyMonStreet.getName());
				HBox costBox = new HBox(10);
				Label cost = new Label("Betrag: ");
				Label costs = new Label("" + amount);
				costBox.getChildren().addAll(cost, costs);
				Button pay = new Button("Bestätigen");
				pay.setOnAction(e -> {
					refreshMoney(newMoney);
					MonopolyClient.toInterruptInterrupt();
					continueRound();
				});
				layout = new VBox(10);
				layout.setPadding(new Insets(10, 10, 10, 10));
				layout.getChildren().addAll(streetName, costBox, pay);
				layout.setAlignment(Pos.CENTER);
				Scene scene = new Scene(layout);
				roundWindow.setScene(scene);
			} else if (noBuyStreet instanceof NonBuyableNormalStreet) {
				Label streetName = new Label(noBuyStreet.getName());
				HBox costBox = new HBox(10);
				Button pay = new Button("Weiter");
				pay.setOnAction(e -> {
					refreshMoney(newMoney);
					MonopolyClient.toInterruptInterrupt();
					continueRound();
				});
				layout = new VBox(10);
				layout.setPadding(new Insets(10, 10, 10, 10));
				layout.getChildren().addAll(streetName, costBox, pay);
				layout.setAlignment(Pos.CENTER);
				Scene scene = new Scene(layout);
				roundWindow.setScene(scene);
			}
		}
	}

	private void auction() {
		HBox value = new HBox(10);
		Label crrntVal = new Label("Aktuelles Gebot:");
		Label val = new Label();
		value.getChildren().addAll(crrntVal, val);
		HBox highest = new HBox(10);
		Label highG = new Label("Höchstbietender: ");
		Label highestG = new Label();
		highest.getChildren().addAll(highG, highestG);
		TextField field = new TextField();
		HBox actions = new HBox(10);
		Button finish = new Button("Fertig");
		Button bidBttn = new Button("Bieten");
		actions.getChildren().addAll(bidBttn, finish);
		layout = new VBox(10);
		bidBttn.setOnAction(e -> {
			try {
				MonopolyClient.bid(Integer.parseInt(field.getText()));
			} catch (NumberFormatException err) {
				Label error = new Label("Bitte eine gültige Zahl eingeben!");
				error.setTextFill(Color.RED);
				field.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
				layout.getChildren().add(3, error);
			}
		});
		layout.getChildren().addAll(value, highest, field, actions);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		roundWindow.setScene(scene);
	}

	public void addNewBid(int amount, String name) {
		Label val = (Label) ((HBox) layout.getChildren().get(0)).getChildren().get(1);
		Label bName = (Label) ((HBox) layout.getChildren().get(1)).getChildren().get(1);
		val.setText("" + amount);
		bName.setText(name);
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
		goToMainMenu();
	}

	public void setTurnName(String name) {
		turnName.setText(name);
	}

	public void goToMainMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("mainMenu.fxml"));
		Parent mainMenuParent = loader.load();
		Scene mainMenu = new Scene(mainMenuParent);
		Stage window = (Stage) menuBar.getScene().getWindow();
		window.setScene(mainMenu);
		window.show();
	}

	private void continueRound() {
		if (doublets) {
			roundWindow.close();
			Platform.runLater(() -> {
				startRound();
			});

		} else {
			roundWindow.close();
			MonopolyClient.roundFinished();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		streetIDs = new VBox[] { id0, id1, id2, id3, id4, id5, id6, id7, id8, id9, id10, id11, id12, id13, id14, id15,
				id16, id17, id18, id19, id20, id21, id22, id23, id24, id25, id26, id27, id28, id29, id30, id31, id32,
				id33, id34, id35, id36, id37, id38, id39 };
	}

	public void initializeGame(Player p) {
		if (p.getName().equals(uName)) {
			streetIDs[p.getPositionIndex()].setStyle("-fx-border-color: green;");
			money.setText("" + p.getMoney());
			player = p;
		}

	}

	public void refreshTurnName(String name) {
		turnName.setText(name);
	}

	public void showBuyConfirmation(BuyableStreet street, int newMoney) {
		refreshMoney(newMoney);
		Label confirm = new Label(street.getName() + " wurde für " + street.getCost() + " gekauft!");
		Button endRound = new Button("Okay");
		endRound.setOnAction(e -> {
			continueRound();
		});
		layout = new VBox(10);
		layout.getChildren().addAll(confirm, endRound);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		roundWindow.setScene(scene);
	}

	public void showCard(CardType type, String cText, int newMoney) {
		Label header = new Label();
		if (type.equals(CardType.COMMUNITY)) {
			header.setText("GEMEINSCHAFTKARTE");
		} else if (type.equals(CardType.EVENT)) {
			header.setText("EREIGNISKARTE");
		}
		TextArea text = new TextArea();
		text.setText(cText);
		text.setEditable(false);
		Button confirm = new Button("Bestätigen");
		confirm.setOnAction(e -> {
			refreshMoney(newMoney);
			continueRound();
		});
		layout = new VBox(10);
		layout.getChildren().addAll(header, text, confirm);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		roundWindow.setScene(scene);

	}

	public boolean getDoublets() {
		return doublets;
	}

	public void showLos(Street street, Integer moneyAmount, int newMoney) {
		Stage losWindow = new Stage();
		refreshMoney(newMoney);
		losWindow.setTitle("LOS");
		losWindow.setMinWidth(250);
		losWindow.setMinHeight(300);
		Label text = new Label("Du bist über LOS gekommen, du erhälst M 200");
		Button closeButton = new Button("Schliessen");
		closeButton.setOnAction(ee -> {
			losWindow.close();
		});
		VBox cTLayout = new VBox(10);
		cTLayout.setPadding(new Insets(10, 10, 10, 10));
		cTLayout.setAlignment(Pos.CENTER);
		cTLayout.getChildren().addAll(text, closeButton);
		Scene scene = new Scene(cTLayout);
		losWindow.setScene(scene);
		losWindow.showAndWait();
	}
}
