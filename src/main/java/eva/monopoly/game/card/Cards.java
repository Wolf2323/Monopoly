package eva.monopoly.game.card;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eva.monopoly.game.card.Card.CardType;
import eva.monopoly.game.card.cards.JailCard;
import eva.monopoly.game.card.cards.MoveCard;
import eva.monopoly.game.card.cards.MoveamountCard;
import eva.monopoly.game.card.cards.MovenextCard;
import eva.monopoly.game.card.cards.UnjailCard;
import eva.monopoly.game.player.Player;
import eva.monopoly.utils.ResourceReaderUtil;


public class Cards
{
	ArrayList<Card>	eventCards		= new ArrayList<>();
	ArrayList<Card>	communityCards	= new ArrayList<>();

	public void loadCards()
	{
		try
		{
			Path path = ResourceReaderUtil.getResourcePath("monopoly/resources/Cards.json");
			JsonArray json = ResourceReaderUtil.getObjectAsJsonFromFile(path, JsonArray.class);

			for(int i = 0; i < json.size(); i++)
			{
				JsonElement element = json.get(i);
				JsonObject obj = element.getAsJsonObject();

				String text = obj.get("text").getAsString();
				CardType type = CardType.valueOf(obj.get("type").getAsString().toUpperCase());

				JsonObject objAction = obj.get("action").getAsJsonObject();
				String actionType = objAction.get("type").getAsString();

				String target;
				int amount;
				int house;
				int hotel;

				Card card = null;

				switch(actionType)
				{
					case "jail":
						card = new JailCard(text, type);
						break;
					case "unjail":
						card = new UnjailCard(text, type, this);
						break;
					case "move":
						target = objAction.get("target").getAsString();
						card = new MoveCard(text, type, target);
						break;
					case "movenext":
						target = objAction.get("target").getAsString();
						card = new MovenextCard(text, type, target);
						break;
					case "moveamount":
						amount = objAction.get("amount").getAsInt();
						card = new MoveamountCard(text, type, amount);
						break;
					case "movenextmoneydouble":
						target = objAction.get("target").getAsString();
						break;
					case "money":
						amount = objAction.get("amount").getAsInt();
						break;
					case "moneybuildings":
						house = objAction.get("house").getAsInt();
						hotel = objAction.get("hotel").getAsInt();
						break;
					case "moneyplayer":
						amount = objAction.get("amount").getAsInt();
						break;
					default:
						throw new IllegalArgumentException("type '" + actionType + "' is unknown");
				}
				switch(type)
				{
					case EVENT:
						eventCards.add(card);
						break;
					case COMMUNITY:
						communityCards.add(card);
						break;
				}
			}
		}
		catch(URISyntaxException | IOException | IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}

	public void pickupCard(UnjailCard eventUnjailCard, Player p)
	{
		// TODO Auto-generated method stub
	}
}
