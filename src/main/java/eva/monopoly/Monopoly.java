package eva.monopoly;

import eva.monopoly.game.card.Cards;
import javafx.application.Application;
import javafx.stage.Stage;


public class Monopoly extends Application
{

	public static void main(String[] args)
	{
		new Cards().loadCards();
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		// TODO Auto-generated method stub

	}
}
